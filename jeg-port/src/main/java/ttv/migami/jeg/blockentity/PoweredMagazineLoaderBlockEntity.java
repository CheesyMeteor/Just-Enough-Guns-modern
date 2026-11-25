package ttv.migami.jeg.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;
import org.jetbrains.annotations.Nullable;
import ttv.migami.jeg.block.PoweredMagazineLoaderBlock;
import ttv.migami.jeg.common.container.PoweredMagazineLoaderMenu;
import ttv.migami.jeg.init.ModTileEntities;
import ttv.migami.jeg.item.AmmoMagazineItem;
import ttv.migami.jeg.util.MagazineAmmo;

/**
 * JEG: Modern - Powered Magazine Loader. Nine ammo slots feed nine magazine slots in parallel, one round per magazine
 * per tick, finished magazines go to nine output slots. Runs on RF: it burns anything in the fuel slot (badly) to make
 * its own, and also accepts RF from outside.
 */
public class PoweredMagazineLoaderBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer
{
    public static final int AMMO_START = 0;
    public static final int MAG_START = 9;
    public static final int OUT_START = 18;
    public static final int FUEL = 27;
    public static final int SIZE = 28;

    public static final int CAPACITY = 10000;
    public static final int MAX_RECEIVE = 1000;
    public static final int GENERATION = 50;      // RF per tick while burning
    public static final int LOAD_COST = 10;       // RF per tick per magazine being loaded
    public static final int IDLE_TICKS = 20;      // stays 'active' (hum on) this long after the last bit of work

    private static final int[] SLOTS_UP = range(AMMO_START, 9);
    private static final int[] SLOTS_DOWN = range(OUT_START, 9);
    private static final int[] SLOTS_SIDES = concat(range(MAG_START, 9), new int[]{FUEL});

    private NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
    private final LoaderEnergy energy = new LoaderEnergy(CAPACITY, MAX_RECEIVE);
    private int burnLeft;
    private int burnTotal;
    private int idle;

    /** Energy store that can also be generated into and spent from internally (outsiders can only insert). */
    public static class LoaderEnergy extends EnergyStorage
    {
        public LoaderEnergy(int capacity, int maxReceive)
        {
            super(capacity, maxReceive, 0);
        }

        public int generate(int amount)
        {
            int added = Math.min(amount, this.capacity - this.energy);
            this.energy += added;
            return added;
        }

        public boolean use(int amount)
        {
            if(this.energy >= amount)
            {
                this.energy -= amount;
                return true;
            }
            return false;
        }

        public void set(int value)
        {
            this.energy = Mth.clamp(value, 0, this.capacity);
        }
    }

    protected final ContainerData dataAccess = new ContainerData()
    {
        @Override
        public int get(int index)
        {
            return switch(index)
            {
                case 0 -> PoweredMagazineLoaderBlockEntity.this.energy.getEnergyStored();
                case 1 -> CAPACITY;
                case 2 -> Math.min(PoweredMagazineLoaderBlockEntity.this.burnLeft, 32000);
                case 3 -> Math.min(PoweredMagazineLoaderBlockEntity.this.burnTotal, 32000);
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value)
        {
        }

        @Override
        public int getCount()
        {
            return 4;
        }
    };

    public PoweredMagazineLoaderBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModTileEntities.POWERED_MAGAZINE_LOADER.get(), pos, state);
    }

    private static int[] range(int start, int count)
    {
        int[] result = new int[count];
        for(int i = 0; i < count; i++)
        {
            result[i] = start + i;
        }
        return result;
    }

    private static int[] concat(int[] a, int[] b)
    {
        int[] result = new int[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    public EnergyStorage getEnergyStorage()
    {
        return this.energy;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, PoweredMagazineLoaderBlockEntity be)
    {
        boolean changed = false;

        // Power: burn the next item in the fuel slot whenever the buffer has room.
        if(be.burnLeft <= 0 && be.energy.getEnergyStored() < CAPACITY)
        {
            ItemStack fuel = be.items.get(FUEL);
            if(!fuel.isEmpty())
            {
                int time = fuel.getBurnTime(null);
                if(time > 0)
                {
                    be.burnLeft = time;
                    be.burnTotal = time;
                    ItemStack remainder = fuel.getCraftingRemainingItem();
                    fuel.shrink(1);
                    if(fuel.isEmpty() && !remainder.isEmpty())
                    {
                        be.items.set(FUEL, remainder);
                    }
                    changed = true;
                }
            }
        }
        if(be.burnLeft > 0)
        {
            be.burnLeft--;
            be.energy.generate(GENERATION);
            changed = true;
        }

        // Loading: every magazine fills at the same time, each one costing LOAD_COST RF a tick.
        int active = 0;
        boolean outOfPower = false;
        for(int i = 0; i < 9 && !outOfPower; i++)
        {
            ItemStack magazineStack = be.items.get(MAG_START + i);
            if(!(magazineStack.getItem() instanceof AmmoMagazineItem magazine))
            {
                continue;
            }
            if(magazine.getAmmoCount(magazineStack) >= magazine.getCapacity())
            {
                if(be.moveToOutput(MAG_START + i))
                {
                    changed = true;
                }
                continue;
            }
            int ammoSlot = be.findAmmo(magazine, magazineStack);
            if(ammoSlot < 0)
            {
                continue;
            }
            if(!be.energy.use(LOAD_COST))
            {
                outOfPower = true;
                continue;
            }
            ItemStack ammo = be.items.get(ammoSlot);
            if(magazine.addRound(magazineStack, BuiltInRegistries.ITEM.getKey(ammo.getItem())))
            {
                ammo.shrink(1);
                active++;
                changed = true;
                if(magazine.getAmmoCount(magazineStack) >= magazine.getCapacity())
                {
                    be.moveToOutput(MAG_START + i);
                }
            }
        }

        // LIT: burning fuel. ACTIVE: burning or loading (drives the client-side hum), held for a moment so it doesn't flicker.
        boolean lit = be.burnLeft > 0;
        if(active > 0 || lit)
        {
            be.idle = IDLE_TICKS;
        }
        else if(be.idle > 0)
        {
            be.idle--;
        }
        boolean isActive = be.idle > 0;
        if(state.getValue(PoweredMagazineLoaderBlock.LIT) != lit || state.getValue(PoweredMagazineLoaderBlock.ACTIVE) != isActive)
        {
            level.setBlock(pos, state.setValue(PoweredMagazineLoaderBlock.LIT, lit).setValue(PoweredMagazineLoaderBlock.ACTIVE, isActive), 3);
        }

        if(changed)
        {
            be.setChanged();
        }
    }

    /** First ammo slot holding a bullet this magazine accepts, or -1. */
    private int findAmmo(AmmoMagazineItem magazine, ItemStack magazineStack)
    {
        ResourceLocation stored = magazine.getAmmoId(magazineStack);
        for(int j = AMMO_START; j < AMMO_START + 9; j++)
        {
            ItemStack ammo = this.items.get(j);
            if(ammo.isEmpty())
            {
                continue;
            }
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(ammo.getItem());
            if(MagazineAmmo.BULLETS.contains(id) && magazine.getType().supports(id) && (stored == null || stored.equals(id)))
            {
                return j;
            }
        }
        return -1;
    }

    /** Moves the magazine in {@code slot} to the first free output slot. Returns false if all are taken. */
    private boolean moveToOutput(int slot)
    {
        for(int o = OUT_START; o < OUT_START + 9; o++)
        {
            if(this.items.get(o).isEmpty())
            {
                this.items.set(o, this.items.get(slot));
                this.items.set(slot, ItemStack.EMPTY);
                return true;
            }
        }
        return false;
    }

    @Override
    protected Component getDefaultName()
    {
        return Component.translatable("container.jeg.powered_magazine_loader");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory)
    {
        return new PoweredMagazineLoaderMenu(id, inventory, this, this.dataAccess);
    }

    protected NonNullList<ItemStack> getItems()
    {
        return this.items;
    }

    protected void setItems(NonNullList<ItemStack> items)
    {
        this.items = items;
    }

    @Override
    public int getContainerSize()
    {
        return SIZE;
    }

    @Override
    public boolean isEmpty()
    {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot)
    {
        return this.items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount)
    {
        ItemStack result = ContainerHelper.removeItem(this.items, slot, amount);
        if(!result.isEmpty())
        {
            this.setChanged();
        }
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot)
    {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack)
    {
        this.items.set(slot, stack);
        if(stack.getCount() > this.getMaxStackSize())
        {
            stack.setCount(this.getMaxStackSize());
        }
        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player)
    {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent()
    {
        this.items.clear();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.loadAdditional(tag, registries);
        this.items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items, registries);
        this.energy.set(tag.getInt("Energy"));
        this.burnLeft = tag.getInt("BurnLeft");
        this.burnTotal = tag.getInt("BurnTotal");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, this.items, registries);
        tag.putInt("Energy", this.energy.getEnergyStored());
        tag.putInt("BurnLeft", this.burnLeft);
        tag.putInt("BurnTotal", this.burnTotal);
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return side == Direction.DOWN ? SLOTS_DOWN : side == Direction.UP ? SLOTS_UP : SLOTS_SIDES;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction)
    {
        return canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
    {
        return index >= OUT_START && index < OUT_START + 9;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack)
    {
        if(index >= AMMO_START && index < AMMO_START + 9)
        {
            return MagazineAmmo.BULLETS.contains(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        }
        if(index >= MAG_START && index < MAG_START + 9)
        {
            return stack.getItem() instanceof AmmoMagazineItem;
        }
        return index == FUEL && stack.getBurnTime(null) > 0;
    }
}
