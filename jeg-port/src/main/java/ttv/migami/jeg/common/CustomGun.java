package ttv.migami.jeg.common;

import ttv.migami.jeg.annotation.Ignored;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.neoforged.neoforge.common.util.INBTSerializable;

/**
 * Author: MrCrayfish
 */
public class CustomGun implements INBTSerializable<CompoundTag>
{
    @Ignored
    public ItemStack model;
    public Gun gun;

    public ItemStack getModel()
    {
        return this.model;
    }

    public Gun getGun()
    {
        return this.gun;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider registries)
    {
        CompoundTag compound = new CompoundTag();
        compound.put("Model", this.model.save(registries));
        compound.put("Gun", this.gun.serializeNBT());
        return compound;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider registries, CompoundTag compound)
    {
        this.model = ItemStack.parse(registries, compound.getCompound("Model")).orElse(ItemStack.EMPTY);
        this.gun = Gun.create(compound.getCompound("Gun"));
    }
}
