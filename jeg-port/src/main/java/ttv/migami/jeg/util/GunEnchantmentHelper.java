package ttv.migami.jeg.util;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import ttv.migami.jeg.Config;
import ttv.migami.jeg.common.Gun;
import ttv.migami.jeg.common.ReloadType;
import ttv.migami.jeg.init.ModEnchantments;
import ttv.migami.jeg.init.ModItems;
import ttv.migami.jeg.item.GunItem;
import ttv.migami.jeg.item.attachment.IAttachment;
import ttv.migami.jeg.particles.TrailData;


/**
 * Author: MrCrayfish
 */
public class GunEnchantmentHelper
{
    /**
     * Gets the level of the given (data-driven) enchantment on the stack without needing registry access,
     * by matching the holders stored in the stack's enchantments component against the resource key.
     * Replacement for the 1.20.1 {@code stack.getEnchantmentLevel(Enchantment)} and
     * {@code EnchantmentHelper.getItemEnchantmentLevel(Enchantment, ItemStack)}.
     *
     * @return the level, or 0 if the stack does not have the enchantment
     */
    public static int getEnchantmentLevel(ItemStack stack, ResourceKey<Enchantment> key)
    {
        ItemEnchantments enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        for(Holder<Enchantment> holder : enchantments.keySet())
        {
            if(holder.is(key))
            {
                return enchantments.getLevel(holder);
            }
        }
        return 0;
    }

    public static boolean hasEnchantment(ItemStack stack, ResourceKey<Enchantment> key)
    {
        return getEnchantmentLevel(stack, key) > 0;
    }

    public static ParticleOptions getParticle(ItemStack weapon)
    {
        if(hasEnchantment(weapon, ModEnchantments.FIRE_STARTER))
        {
            return ParticleTypes.LAVA;
        }
        else if(hasEnchantment(weapon, ModEnchantments.PUNCTURING))
        {
            return ParticleTypes.ENCHANTED_HIT;
        }
        return new TrailData(weapon.isEnchanted());
    }

    public static int getRealReloadSpeed(ItemStack weapon)
    {
        Gun modifiedGun = ((GunItem) weapon.getItem()).getModifiedGun(weapon);
        if (modifiedGun.getReloads().getReloadType() == ReloadType.MAG_FED)
            return getMagReloadSpeed(weapon);
        else
            return getReloadInterval(weapon, modifiedGun.getReloads().getReloadTimer());
    }

    public static int getReloadInterval(ItemStack weapon, int interval)
    {
        int level = getEnchantmentLevel(weapon, ModEnchantments.QUICK_HANDS);
        if(level > 0)
        {
            interval -= 3 * level;
        }
        return Math.max(interval, 1);
    }

    public static int getMagReloadSpeed(ItemStack weapon)
    {
        Gun modifiedGun = ((GunItem) weapon.getItem()).getModifiedGun(weapon);

        int modifier = 1;
        if (Gun.getAttachment(IAttachment.Type.MAGAZINE, weapon).getItem() == ModItems.EXTENDED_MAG.get()) {
            modifier = 2;
        }
        if (Gun.getAttachment(IAttachment.Type.MAGAZINE, weapon).getItem() == ModItems.DRUM_MAG.get()) {
            modifier = 3;
        }

        int speed = modifiedGun.getReloads().getAdditionalReloadTimer() + modifiedGun.getReloads().getReloadTimer() * modifier;

        int level = getEnchantmentLevel(weapon, ModEnchantments.QUICK_HANDS);
        if(level > 0)
        {
            speed -= Math.round(((speed/4)) * level);
        }
        return Math.max(speed, 4);
    }

    public static float getReloadAnimationSpeed(ItemStack weapon) {
        int level = getEnchantmentLevel(weapon, ModEnchantments.QUICK_HANDS);
        return 1.0f + (0.2F * level);
    }

    public static int getModifiedDrawTick(ItemStack stack, int baseDrawTick) {
        if (!Config.COMMON.gameplay.drawAnimation.get()) {
            return 0;
        }
        int level = getEnchantmentLevel(stack, ModEnchantments.QUICK_HANDS);
        if (level > 0) {
            float modifier = 1.0f - (0.2F * level);
            return Math.max(Math.round(baseDrawTick * modifier), 1);
        }
        return baseDrawTick;
    }

    public static int getRate(ItemStack weapon, Gun modifiedGun)
    {
        int rate = modifiedGun.getGeneral().getRate();
        int level = getEnchantmentLevel(weapon, ModEnchantments.TRIGGER_FINGER);
        if(level > 0)
        {
            float newRate = rate * (0.25F * level);
            rate -= Mth.clamp(newRate, 0, rate);
        }
        return rate;
    }

    public static double getAimDownSightSpeed(ItemStack weapon)
    {
        int level = getEnchantmentLevel(weapon, ModEnchantments.LIGHTWEIGHT);
        return level > 0 ? 1.5 : 1.0;
    }

    /*public static int getAmmoCapacity(ItemStack weapon, Gun modifiedGun)
    {
        int capacity = modifiedGun.getReloads().getMaxAmmo();
        int level = getEnchantmentLevel(weapon, ModEnchantments.OVER_CAPACITY);
        if(level > 0)
        {
            capacity += Math.max(level, (capacity / 2) * level);
        }
        return capacity;
    }*/

    public static double getProjectileSpeedModifier(ItemStack weapon)
    {
        int level = getEnchantmentLevel(weapon, ModEnchantments.ACCELERATOR);
        if(level > 0)
        {
            return 1.0 + 0.5 * level;
        }
        return 1.0;
    }

    public static float getAcceleratorDamage(ItemStack weapon, float damage)
    {
        int level = getEnchantmentLevel(weapon, ModEnchantments.ACCELERATOR);
        if(level > 0)
        {
            return damage + damage * (0.1F * level);
        }
        return damage;
    }

    public static float getWitheredDamage(ItemStack weapon, float damage) {
        int level = getEnchantmentLevel(weapon, ModEnchantments.WITHERED);
        if (level > 0) {
            return damage * (float) Math.pow(1.15, level);
        }
        return damage;
    }

    public static float getPuncturingChance(ItemStack weapon)
    {
        int level = getEnchantmentLevel(weapon, ModEnchantments.PUNCTURING);
        return level * 0.05F;
    }
}
