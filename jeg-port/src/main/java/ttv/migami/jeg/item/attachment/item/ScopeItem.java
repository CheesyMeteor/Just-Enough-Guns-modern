package ttv.migami.jeg.item.attachment.item;

import net.minecraft.core.Holder;
import ttv.migami.jeg.item.AttachmentItem;
import ttv.migami.jeg.item.IColored;
import ttv.migami.jeg.item.attachment.IScope;
import ttv.migami.jeg.item.attachment.impl.Scope;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * A basic scope attachment item implementation with color support
 *
 * Author: MrCrayfish
 */
public class ScopeItem extends AttachmentItem implements IScope, IColored
{
    private final Scope scope;
    private final boolean colored;

    public ScopeItem(Scope scope, Item.Properties properties)
    {
        super(properties);
        this.scope = scope;
        this.colored = true;
    }

    public ScopeItem(Scope scope, Item.Properties properties, boolean colored)
    {
        super(properties);
        this.scope = scope;
        this.colored = colored;
    }

    @Override
    public Scope getProperties()
    {
        return this.scope;
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
