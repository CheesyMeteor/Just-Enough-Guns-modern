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
import ttv.migami.jeg.blockentity.MagazineLoaderBlockEntity;
import ttv.migami.jeg.init.ModContainers;

/** Placeholder furnace-style menu for the Magazine Loader: ammo in, magazine out. */
public class MagazineLoaderMenu extends AbstractContainerMenu
{
    private final Container container;
    private final ContainerData data;

    public MagazineLoaderMenu(int id, Inventory inventory)
    {
        this(id, inventory, new SimpleContainer(3), new SimpleContainerData(2));
    }

    public MagazineLoaderMenu(int id, Inventory inventory, Container container, ContainerData data)
    {
        super(ModContainers.MAGAZINE_LOADER.get(), id);
        checkContainerSize(container, 3);
        checkContainerDataCount(data, 2);
        this.container = container;
        this.data = data;

        this.addSlot(new Slot(container, MagazineLoaderBlockEntity.SLOT_AMMO, 56, 17)
        {
            @Override
            public boolean mayPlace(ItemStack stack)
            {
                return container.canPlaceItem(MagazineLoaderBlockEntity.SLOT_AMMO, stack);
            }
        });
        this.addSlot(new Slot(container, MagazineLoaderBlockEntity.SLOT_MAGAZINE, 56, 53)
        {
            @Override
            public boolean mayPlace(ItemStack stack)
            {
                return container.canPlaceItem(MagazineLoaderBlockEntity.SLOT_MAGAZINE, stack);
            }

            @Override
            public int getMaxStackSize()
            {
                return 1;
            }
        });
        this.addSlot(new Slot(container, MagazineLoaderBlockEntity.SLOT_OUTPUT, 116, 35)
        {
            @Override
            public boolean mayPlace(ItemStack stack)
            {
                return false;
            }
        });

        for(int row = 0; row < 3; row++)
        {
            for(int col = 0; col < 9; col++)
            {
                this.addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for(int col = 0; col < 9; col++)
        {
            this.addSlot(new Slot(inventory, col, 8 + col * 18, 142));
        }

        this.addDataSlots(data);
    }

    public static final int BAR_WIDTH = 32;

    /** Width (0-32) of the progress bar: how full the magazine being loaded is. */
    public int getProgressWidth()
    {
        int rounds = this.data.get(0);
        int capacity = Math.max(1, this.data.get(1));
        if(rounds <= 0)
        {
            return 0;
        }
        return Math.max(1, Math.min(BAR_WIDTH, rounds * BAR_WIDTH / capacity));
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
            if(index < 3)
            {
                if(!this.moveItemStackTo(stack, 3, this.slots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(this.container.canPlaceItem(MagazineLoaderBlockEntity.SLOT_MAGAZINE, stack))
            {
                if(!this.moveItemStackTo(stack, MagazineLoaderBlockEntity.SLOT_MAGAZINE, MagazineLoaderBlockEntity.SLOT_MAGAZINE + 1, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(this.container.canPlaceItem(MagazineLoaderBlockEntity.SLOT_AMMO, stack))
            {
                if(!this.moveItemStackTo(stack, MagazineLoaderBlockEntity.SLOT_AMMO, MagazineLoaderBlockEntity.SLOT_AMMO + 1, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(index < 30)
            {
                if(!this.moveItemStackTo(stack, 30, 39, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(!this.moveItemStackTo(stack, 3, 30, false))
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
