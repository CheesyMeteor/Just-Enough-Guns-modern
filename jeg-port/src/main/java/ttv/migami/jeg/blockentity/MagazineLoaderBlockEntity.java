package ttv.migami.jeg.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import org.jetbrains.annotations.Nullable;
import ttv.migami.jeg.common.container.MagazineLoaderMenu;
import ttv.migami.jeg.init.ModTileEntities;
import ttv.migami.jeg.item.AmmoMagazineItem;
import ttv.migami.jeg.util.MagazineAmmo;

/**
 * JEG: Modern - Magazine Loader (placeholder block). Puts bullets from the ammo slot into the magazine slot,
 * one round every {@link #INTERVAL} ticks, so large stacks can be loaded without holding right-click.
 */
public class MagazineLoaderBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer
{
    public static final int SLOT_AMMO = 0;
    public static final int SLOT_MAGAZINE = 1;
    public static final int SLOT_OUTPUT = 2;
    public static final int INTERVAL = 3;

    private static final int[] SLOTS_UP = new int[]{SLOT_AMMO};
    private static final int[] SLOTS_DOWN = new int[]{SLOT_OUTPUT};
    private static final int[] SLOTS_SIDES = new int[]{SLOT_MAGAZINE};

    private NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    private int progress;

    protected final ContainerData dataAccess = new ContainerData()
    {
        @Override
        public int get(int index)
        {
            // 0: rounds in the magazine being loaded, 1: its capacity (drives the progress bar)
            ItemStack stack = MagazineLoaderBlockEntity.this.items.get(SLOT_MAGAZINE);
            if(stack.getItem() instanceof AmmoMagazineItem magazine)
            {
                return index == 0 ? magazine.getAmmoCount(stack) : magazine.getCapacity();
            }
            return index == 0 ? 0 : 1;
        }

        @Override
        public void set(int index, int value)
        {
        }

        @Override
        public int getCount()
        {
            return 2;
        }
    };

    public MagazineLoaderBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModTileEntities.MAGAZINE_LOADER.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, MagazineLoaderBlockEntity be)
    {
        ItemStack ammo = be.items.get(SLOT_AMMO);
        ItemStack magazineStack = be.items.get(SLOT_MAGAZINE);

        if(!(magazineStack.getItem() instanceof AmmoMagazineItem magazine))
        {
            be.stopProgress();
            return;
        }

        // A full magazine moves to the output slot.
        if(magazine.getAmmoCount(magazineStack) >= magazine.getCapacity())
        {
            be.stopProgress();
            if(be.items.get(SLOT_OUTPUT).isEmpty())
            {
                be.items.set(SLOT_OUTPUT, magazineStack);
                be.items.set(SLOT_MAGAZINE, ItemStack.EMPTY);
                be.setChanged();
            }
            return;
        }

        boolean canLoad = false;
        if(!ammo.isEmpty())
        {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(ammo.getItem());
            ResourceLocation stored = magazine.getAmmoId(magazineStack);
            canLoad = MagazineAmmo.BULLETS.contains(id)
                    && magazine.getType().supports(id)
                    && (stored == null || stored.equals(id));
        }

        if(!canLoad)
        {
            be.stopProgress();
            return;
        }

        be.progress++;
        if(be.progress >= INTERVAL)
        {
            be.progress = 0;
            if(magazine.addRound(magazineStack, BuiltInRegistries.ITEM.getKey(ammo.getItem())))
            {
                ammo.shrink(1);
            }
        }
        be.setChanged();
    }

    private void stopProgress()
    {
        if(this.progress != 0)
        {
            this.progress = 0;
            this.setChanged();
        }
    }

    @Override
    protected Component getDefaultName()
    {
        return Component.translatable("container.jeg.magazine_loader");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory)
    {
        return new MagazineLoaderMenu(id, inventory, this, this.dataAccess);
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
    public int getContainerSize()
    {
        return 3;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.loadAdditional(tag, registries);
        this.items = NonNullList.withSize(3, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items, registries);
        this.progress = tag.getInt("Progress");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, this.items, registries);
        tag.putInt("Progress", this.progress);
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return side == Direction.DOWN ? SLOTS_DOWN : side == Direction.UP ? SLOTS_UP : SLOTS_SIDES;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction)
    {
        return (index == SLOT_AMMO || index == SLOT_MAGAZINE) && canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
    {
        return index == SLOT_OUTPUT || index == SLOT_MAGAZINE;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack)
    {
        if(index == SLOT_AMMO)
        {
            return MagazineAmmo.BULLETS.contains(BuiltInRegistries.ITEM.getKey(stack.getItem()));
        }
        if(index == SLOT_MAGAZINE)
        {
            return stack.getItem() instanceof AmmoMagazineItem;
        }
        return false;
    }
}
