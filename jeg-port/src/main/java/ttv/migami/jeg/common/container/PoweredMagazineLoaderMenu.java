package ttv.migami.jeg.common.container;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import ttv.migami.jeg.blockentity.PoweredMagazineLoaderBlockEntity;
import ttv.migami.jeg.init.ModContainers;

/** Menu for the Powered Magazine Loader: 9 ammo, 9 magazine, 9 output slots and a fuel slot. */
public class PoweredMagazineLoaderMenu extends AbstractContainerMenu
{
    public static final int AMMO_X = 52;
    public static final int MAG_X = 112;
    public static final int OUT_X = 172;
    public static final int GRID_Y = 20;
    public static final int FUEL_X = 26;
    public static final int FUEL_Y = 20;
    public static final int INV_X = 34;
    public static final int INV_Y = 108;

    private static final int MACHINE_SLOTS = PoweredMagazineLoaderBlockEntity.SIZE;
    private static final int PLAYER_START = MACHINE_SLOTS;

    private final Container container;
    private final ContainerData data;

    public PoweredMagazineLoaderMenu(int id, Inventory inventory)
    {
        this(id, inventory, new SimpleContainer(MACHINE_SLOTS), new SimpleContainerData(4));
    }

    public PoweredMagazineLoaderMenu(int id, Inventory inventory, Container container, ContainerData data)
    {
        super(ModContainers.POWERED_MAGAZINE_LOADER.get(), id);
        checkContainerSize(container, MACHINE_SLOTS);
        checkContainerDataCount(data, 4);
        this.container = container;
        this.data = data;

        for(int row = 0; row < 3; row++)
        {
            for(int col = 0; col < 3; col++)
            {
                this.addSlot(new Slot(container, PoweredMagazineLoaderBlockEntity.AMMO_START + row * 3 + col, AMMO_X + col * 18, GRID_Y + row * 18)
                {
                    @Override
                    public boolean mayPlace(ItemStack stack)
                    {
                        return container.canPlaceItem(this.getContainerSlot(), stack);
                    }
                });
            }
        }
        for(int row = 0; row < 3; row++)
        {
            for(int col = 0; col < 3; col++)
            {
                this.addSlot(new Slot(container, PoweredMagazineLoaderBlockEntity.MAG_START + row * 3 + col, MAG_X + col * 18, GRID_Y + row * 18)
                {
                    @Override
                    public boolean mayPlace(ItemStack stack)
                    {
                        return container.canPlaceItem(this.getContainerSlot(), stack);
                    }

                    @Override
                    public int getMaxStackSize()
                    {
                        return 1;
                    }
                });
            }
        }
        for(int row = 0; row < 3; row++)
        {
            for(int col = 0; col < 3; col++)
            {
                this.addSlot(new Slot(container, PoweredMagazineLoaderBlockEntity.OUT_START + row * 3 + col, OUT_X + col * 18, GRID_Y + row * 18)
                {
                    @Override
                    public boolean mayPlace(ItemStack stack)
                    {
                        return false;
                    }
                });
            }
        }
        this.addSlot(new Slot(container, PoweredMagazineLoaderBlockEntity.FUEL, FUEL_X, FUEL_Y)
        {
            @Override
            public boolean mayPlace(ItemStack stack)
            {
                return container.canPlaceItem(PoweredMagazineLoaderBlockEntity.FUEL, stack);
            }
        });

        for(int row = 0; row < 3; row++)
        {
            for(int col = 0; col < 9; col++)
            {
                this.addSlot(new Slot(inventory, col + row * 9 + 9, INV_X + col * 18, INV_Y + row * 18));
            }
        }
        for(int col = 0; col < 9; col++)
        {
            this.addSlot(new Slot(inventory, col, INV_X + col * 18, INV_Y + 58));
        }

        this.addDataSlots(data);
    }

    public int getEnergy()
    {
        return this.data.get(0);
    }

    public int getCapacity()
    {
        return Math.max(1, this.data.get(1));
    }

    /** 0-14: how much of the flame is left on the current burn. */
    public int getFlameHeight()
    {
        int total = this.data.get(3);
        int left = this.data.get(2);
        if(total <= 0 || left <= 0)
        {
            return 0;
        }
        return Math.max(1, Math.min(14, left * 14 / total));
    }

    @Override
    public boolean stillValid(Player player)
    {
        return this.container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index)
    {
        ItemStack copy = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if(slot.hasItem())
        {
            ItemStack stack = slot.getItem();
            copy = stack.copy();
            int total = this.slots.size();
            if(index < PLAYER_START)
            {
                if(!this.moveItemStackTo(stack, PLAYER_START, total, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(this.container.canPlaceItem(PoweredMagazineLoaderBlockEntity.MAG_START, stack))
            {
                if(!this.moveItemStackTo(stack, PoweredMagazineLoaderBlockEntity.MAG_START, PoweredMagazineLoaderBlockEntity.MAG_START + 9, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(this.container.canPlaceItem(PoweredMagazineLoaderBlockEntity.AMMO_START, stack))
            {
                if(!this.moveItemStackTo(stack, PoweredMagazineLoaderBlockEntity.AMMO_START, PoweredMagazineLoaderBlockEntity.AMMO_START + 9, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(stack.getBurnTime(null) > 0)
            {
                if(!this.moveItemStackTo(stack, PoweredMagazineLoaderBlockEntity.FUEL, PoweredMagazineLoaderBlockEntity.FUEL + 1, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(index < PLAYER_START + 27)
            {
                if(!this.moveItemStackTo(stack, PLAYER_START + 27, total, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(!this.moveItemStackTo(stack, PLAYER_START, PLAYER_START + 27, false))
            {
                return ItemStack.EMPTY;
            }

            if(stack.isEmpty())
            {
                slot.setByPlayer(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }
            if(stack.getCount() == copy.getCount())
            {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, stack);
        }
        return copy;
    }
}
