package ttv.migami.jeg.init;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import ttv.migami.jeg.Reference;

/**
 * Author: MrCrayfish
 * <p>
 * PORT: enchantments are data-driven in 1.21 (JSON in data/jeg/enchantment, written by the "events-misc" slice).
 * The old RegistryObject fields are now ResourceKeys (same names); resolve them to Holders with the helpers below.
 */
public class ModEnchantments
{
    public static final ResourceKey<Enchantment> QUICK_HANDS = key("quick_hands");
    public static final ResourceKey<Enchantment> TRIGGER_FINGER = key("trigger_finger");
    public static final ResourceKey<Enchantment> LIGHTWEIGHT = key("lightweight");
    public static final ResourceKey<Enchantment> COLLATERAL = key("collateral");
    public static final ResourceKey<Enchantment> OVER_CAPACITY = key("over_capacity");
    public static final ResourceKey<Enchantment> RECLAIMED = key("reclaimed");
    public static final ResourceKey<Enchantment> ACCELERATOR = key("accelerator");
    public static final ResourceKey<Enchantment> PUNCTURING = key("puncturing");
    public static final ResourceKey<Enchantment> FIRE_STARTER = key("fire_starter");
    public static final ResourceKey<Enchantment> ATLANTIC_SHOOTER = key("atlantic_shooter");

    public static final ResourceKey<Enchantment> INFINITY = key("infinity");
    public static final ResourceKey<Enchantment> WITHERED = key("withered");
    //Gravity Impulse (3 levels) - nearby entities will get knocked away from the target location

    private static ResourceKey<Enchantment> key(String name)
    {
        return ResourceKey.create(Registries.ENCHANTMENT, Reference.id(name));
    }

    /** Resolves an enchantment key to its Holder from any registry lookup (e.g. {@code level.registryAccess()}). */
    public static Holder<Enchantment> holder(HolderLookup.Provider registries, ResourceKey<Enchantment> key)
    {
        return registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);
    }

    public static Holder<Enchantment> holder(Level level, ResourceKey<Enchantment> key)
    {
        return holder(level.registryAccess(), key);
    }

    /** Level of the enchantment on the stack, or 0 when absent / the enchantment is not loaded. */
    public static int getLevel(HolderLookup.Provider registries, ResourceKey<Enchantment> key, ItemStack stack)
    {
        return registries.lookupOrThrow(Registries.ENCHANTMENT).get(key)
                .map(h -> EnchantmentHelper.getItemEnchantmentLevel(h, stack)).orElse(0);
    }

    public static int getLevel(Level level, ResourceKey<Enchantment> key, ItemStack stack)
    {
        return getLevel(level.registryAccess(), key, stack);
    }
}
