package ttv.migami.jeg.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import ttv.migami.jeg.Config;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.common.Gun;
import ttv.migami.jeg.init.ModEnchantments;
import ttv.migami.jeg.item.AmmoMagazineItem;
import ttv.migami.jeg.item.AmmoMagazineItem.Family;
import ttv.migami.jeg.item.GunItem;

import java.util.Map;

/**
 * JEG: Modern - which guns take magazines, and the magazine swap.
 * <p>
 * Magazine guns hold the magazine that was last loaded into them: the gun's {@code AmmoCount} is that magazine's
 * rounds, and its capacity is that magazine's capacity. Reloading swaps in the fullest compatible magazine from the
 * inventory and hands the old one (with whatever is left in it) back. Guns not listed here reload the old way.
 */
public final class MagazineGuns
{
    public static final String LOADED_KEY = "LoadedMag";

    private static final Map<String, Family> GUNS = Map.ofEntries(
            Map.entry("semi_auto_pistol", Family.PISTOL),
            Map.entry("combat_pistol", Family.PISTOL),
            Map.entry("custom_smg", Family.PISTOL),
            Map.entry("assault_rifle", Family.RIFLE),
            Map.entry("burst_rifle", Family.RIFLE),
            Map.entry("combat_rifle", Family.RIFLE),
            Map.entry("service_rifle", Family.RIFLE),
            Map.entry("semi_auto_rifle", Family.RIFLE),
            Map.entry("blossom_rifle", Family.RIFLE),
            Map.entry("hollenfire_mk2", Family.RIFLE),
            Map.entry("soulhunter_mk2", Family.RIFLE),
            Map.entry("light_machine_gun", Family.MACHINE_GUN),
            Map.entry("minigun", Family.MINIGUN));

    private MagazineGuns() {}

    /** The magazine family this gun uses, or null if it has no magazine. */
    public static Family family(ItemStack gun)
    {
        if(!(gun.getItem() instanceof GunItem))
        {
            return null;
        }
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(gun.getItem());
        if(!Reference.MOD_ID.equals(id.getNamespace()))
        {
            return null;
        }
        return GUNS.get(id.getPath());
    }

    /** True when magazine feed is on and this gun uses magazines. */
    public static boolean enabled(ItemStack gun)
    {
        return Config.magazineFeedEnabled() && family(gun) != null;
    }

    /** True when reloading this gun for this player means swapping magazines (not creative, Infinity or IgnoreAmmo). */
    public static boolean swapsFor(Player player, ItemStack gun)
    {
        return enabled(gun)
                && !player.getAbilities().instabuild
                && GunEnchantmentHelper.getEnchantmentLevel(gun, ModEnchantments.INFINITY) == 0
                && !StackData.getBoolean(gun, "IgnoreAmmo");
    }

    private static AmmoMagazineItem.Type baseType(Family family)
    {
        return switch(family)
        {
            case PISTOL -> AmmoMagazineItem.Type.PISTOL;
            case RIFLE -> AmmoMagazineItem.Type.RIFLE;
            case MACHINE_GUN -> AmmoMagazineItem.Type.MACHINE_GUN;
            case MINIGUN -> AmmoMagazineItem.Type.MINIGUN;
        };
    }

    private static Item baseItem(Family family)
    {
        return switch(family)
        {
            case PISTOL -> ttv.migami.jeg.init.ModItems.PISTOL_MAGAZINE.get();
            case RIFLE -> ttv.migami.jeg.init.ModItems.RIFLE_MAGAZINE.get();
            case MACHINE_GUN -> ttv.migami.jeg.init.ModItems.MACHINE_GUN_MAGAZINE.get();
            case MINIGUN -> ttv.migami.jeg.init.ModItems.MINIGUN_AMMO_DRUM.get();
        };
    }

    /** The magazine currently in the gun (its item), defaulting to the family's normal magazine. */
    public static AmmoMagazineItem loadedMagazine(ItemStack gun)
    {
        String stored = StackData.getString(gun, LOADED_KEY);
        if(!stored.isEmpty())
        {
            ResourceLocation id = ResourceLocation.tryParse(stored);
            if(id != null && BuiltInRegistries.ITEM.get(id) instanceof AmmoMagazineItem magazine)
            {
                return magazine;
            }
        }
        return (AmmoMagazineItem) baseItem(family(gun));
    }

    /** Capacity of the magazine in the gun. Only call when {@link #enabled} is true. */
    public static int capacity(ItemStack gun)
    {
        return loadedMagazine(gun).getCapacity();
    }

    private static boolean compatible(ItemStack candidate, Family family, ResourceLocation gunAmmo)
    {
        return candidate.getItem() instanceof AmmoMagazineItem magazine
                && magazine.getType().family() == family
                && magazine.getAmmoCount(candidate) > 0
                && gunAmmo.equals(magazine.getAmmoId(candidate));
    }

    /**
     * Inventory slot of the compatible magazine to swap in, or -1. Prefers the fullest magazine holding more rounds than
     * the gun; if there is none, the fullest of the rest, so a fresher-but-emptier magazine can still be chosen.
     */
    public static int bestSlot(Player player, ItemStack gun, Gun modified)
    {
        Family family = family(gun);
        if(family == null)
        {
            return -1;
        }
        ResourceLocation gunAmmo = modified.getProjectile().getItem();
        int current = StackData.getInt(gun, "AmmoCount");
        int bestUp = -1;
        int bestUpCount = current;
        int bestAny = -1;
        int bestAnyCount = 0;
        int size = player.getInventory().getContainerSize();
        for(int i = 0; i < size; i++)
        {
            ItemStack candidate = player.getInventory().getItem(i);
            if(compatible(candidate, family, gunAmmo))
            {
                int count = ((AmmoMagazineItem) candidate.getItem()).getAmmoCount(candidate);
                if(count > bestUpCount)
                {
                    bestUp = i;
                    bestUpCount = count;
                }
                if(count > bestAnyCount)
                {
                    bestAny = i;
                    bestAnyCount = count;
                }
            }
        }
        return bestUp >= 0 ? bestUp : bestAny;
    }

    /** Multiplier on reload time from the magazine about to be loaded (bigger magazines are slower). */
    public static float reloadMultiplier(Player player, ItemStack gun, Gun modified)
    {
        int slot = bestSlot(player, gun, modified);
        if(slot < 0)
        {
            return 1.0F;
        }
        ItemStack mag = player.getInventory().getItem(slot);
        return mag.getItem() instanceof AmmoMagazineItem item ? item.getType().reloadMultiplier() : 1.0F;
    }

    /** Multiplier on recoil from the magazine currently in the gun. */
    public static float recoilMultiplier(ItemStack gun)
    {
        return enabled(gun) ? loadedMagazine(gun).getType().recoilMultiplier() : 1.0F;
    }

    /** Number of compatible magazines in the inventory that hold any ammo at all (for the HUD). */
    public static int magazineCount(Player player, ItemStack gun, Gun modified)
    {
        Family family = family(gun);
        if(family == null)
        {
            return 0;
        }
        ResourceLocation gunAmmo = modified.getProjectile().getItem();
        int total = 0;
        for(int i = 0; i < player.getInventory().getContainerSize(); i++)
        {
            if(compatible(player.getInventory().getItem(i), family, gunAmmo))
            {
                total++;
            }
        }
        return total;
    }

    /** Puts the magazine from {@code slot} into the gun; the old magazine (with its remaining rounds) takes the slot. */
    public static void swap(Player player, ItemStack gun, Gun modified, int slot)
    {
        ItemStack newMag = player.getInventory().getItem(slot);
        if(!(newMag.getItem() instanceof AmmoMagazineItem newItem))
        {
            return;
        }
        ResourceLocation gunAmmo = modified.getProjectile().getItem();
        int newCount = newItem.getAmmoCount(newMag);
        int oldCount = StackData.getInt(gun, "AmmoCount");
        boolean hadMagazine = !StackData.getString(gun, LOADED_KEY).isEmpty();

        ItemStack old = ItemStack.EMPTY;
        ItemStack loose = ItemStack.EMPTY;
        if(hadMagazine)
        {
            old = new ItemStack(loadedMagazine(gun));
            if(oldCount > 0)
            {
                StackData.putString(old, AmmoMagazineItem.AMMO_KEY, gunAmmo.toString());
                StackData.putInt(old, AmmoMagazineItem.COUNT_KEY, oldCount);
            }
        }
        else if(oldCount > 0)
        {
            // A gun that was loaded before magazines existed: give its rounds back loose.
            loose = new ItemStack(BuiltInRegistries.ITEM.get(gunAmmo), oldCount);
        }

        StackData.putInt(gun, "AmmoCount", newCount);
        StackData.putString(gun, LOADED_KEY, BuiltInRegistries.ITEM.getKey(newItem).toString());
        player.getInventory().setItem(slot, old);

        if(!loose.isEmpty())
        {
            give(player, loose);
        }
    }

    private static void give(Player player, ItemStack stack)
    {
        player.getInventory().add(stack);
        if(!stack.isEmpty())
        {
            player.level().addFreshEntity(new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), stack.copy()));
        }
    }
}
