package ttv.migami.jeg.item.attachment.item;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import ttv.migami.jeg.item.AttachmentItem;
import ttv.migami.jeg.item.IColored;
import ttv.migami.jeg.item.attachment.IMagazine;
import ttv.migami.jeg.item.attachment.impl.Magazine;

/**
 * A basic magazine attachment item implementation with color support
 *
 * Credit: MrCrayfish
 */
public class MagazineItem extends AttachmentItem implements IMagazine, IColored
{
    private final Magazine magazine;
    private final boolean colored;

    public MagazineItem(Magazine magazine, Properties properties)
    {
        super(properties);
        this.magazine = magazine;
        this.colored = true;
    }

    public MagazineItem(Magazine magazine, Properties properties, boolean colored)
    {
        super(properties);
        this.magazine = magazine;
        this.colored = colored;
    }

    @Override
    public Magazine getProperties()
    {
        return this.magazine;
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
