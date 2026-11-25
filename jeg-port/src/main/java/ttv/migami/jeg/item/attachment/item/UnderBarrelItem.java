package ttv.migami.jeg.item.attachment.item;

import net.minecraft.core.Holder;
import ttv.migami.jeg.item.AttachmentItem;
import ttv.migami.jeg.item.IColored;
import ttv.migami.jeg.item.attachment.IUnderBarrel;
import ttv.migami.jeg.item.attachment.impl.UnderBarrel;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;

/**
 * A basic under barrel attachment item implementation with color support
 *
 * Author: MrCrayfish
 */
public class UnderBarrelItem extends AttachmentItem implements IUnderBarrel, IColored
{
    private final UnderBarrel underBarrel;
    private final boolean colored;

    public UnderBarrelItem(UnderBarrel underBarrel, Properties properties)
    {
        super(properties);
        this.underBarrel = underBarrel;
        this.colored = true;
    }

    public UnderBarrelItem(UnderBarrel underBarrel, Properties properties, boolean colored)
    {
        super(properties);
        this.underBarrel = underBarrel;
        this.colored = colored;
    }

    @Override
    public UnderBarrel getProperties()
    {
        return this.underBarrel;
    }

    @Override
    public boolean canColor(ItemStack stack)
    {
        return this.colored;
    }

    // PORT: canApplyAtEnchantingTable(ItemStack, Enchantment) -> supportsEnchantment(ItemStack, Holder<Enchantment>)
    // PORT-VERIFY: IItemExtension#supportsEnchantment signature in NeoForge 21.1
    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment)
    {
        return enchantment.is(Enchantments.BINDING_CURSE) || super.supportsEnchantment(stack, enchantment);
    }
}
