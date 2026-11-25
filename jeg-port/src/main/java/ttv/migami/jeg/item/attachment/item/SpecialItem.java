package ttv.migami.jeg.item.attachment.item;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import ttv.migami.jeg.item.AttachmentItem;
import ttv.migami.jeg.item.IColored;
import ttv.migami.jeg.item.attachment.ISide;
import ttv.migami.jeg.item.attachment.impl.Side;

/**
 * A basic side attachment item implementation with color support
 *
 * Credit: MrCrayfish
 */
public class SpecialItem extends AttachmentItem implements ISide, IColored
{
    private final Side side;
    private final boolean colored;

    public SpecialItem(Side side, Properties properties)
    {
        super(properties);
        this.side = side;
        this.colored = true;
    }

    public SpecialItem(Side side, Properties properties, boolean colored)
    {
        super(properties);
        this.side = side;
        this.colored = colored;
    }

    @Override
    public Side getProperties()
    {
        return this.side;
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
