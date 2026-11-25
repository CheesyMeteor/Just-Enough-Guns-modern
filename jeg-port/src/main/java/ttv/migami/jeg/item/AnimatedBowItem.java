package ttv.migami.jeg.item;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import software.bernie.geckolib.animatable.GeoItem;
import ttv.migami.jeg.init.ModEnchantments;

public class AnimatedBowItem extends AnimatedGunItem implements GeoItem {

    public AnimatedBowItem(Properties properties, String path) {
        super(properties, path);
    }

    // PORT: canApplyAtEnchantingTable(ItemStack, Enchantment) -> supportsEnchantment(ItemStack, Holder<Enchantment>)
    // PORT-VERIFY: IItemExtension#supportsEnchantment signature in NeoForge 21.1; Flame must also be allowed by the exclusive sets / enchantment JSON
    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        if (enchantment.is(Enchantments.FLAME)) {
            return true;
        }
        if (enchantment.is(ModEnchantments.OVER_CAPACITY) ||
                enchantment.is(ModEnchantments.TRIGGER_FINGER) ||
                enchantment.is(ModEnchantments.FIRE_STARTER)) {
            return false;
        }
        return super.supportsEnchantment(stack, enchantment);
    }
}
