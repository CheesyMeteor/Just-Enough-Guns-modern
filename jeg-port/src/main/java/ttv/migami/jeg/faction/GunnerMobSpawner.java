package ttv.migami.jeg.faction;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.common.Gun;
import ttv.migami.jeg.entity.ai.AIType;
import ttv.migami.jeg.entity.ai.GunAttackGoal;
import ttv.migami.jeg.init.ModTags;
import ttv.migami.jeg.item.GunItem;
import ttv.migami.jeg.util.StackData;

// PORT: no longer registered by the main class; game-bus subscriber (init-net notes). All handlers are static.
@EventBusSubscriber(modid = Reference.MOD_ID)
public class GunnerMobSpawner {

    @SubscribeEvent
    public static void onSpecialSpawn(FinalizeSpawnEvent event) {
        if (!GunMobValues.enabled) {
            return;
        }

        LivingEntity entity = event.getEntity();

        if (!(entity instanceof PathfinderMob)) {
            return;
        }

        if (!entity.getType().is(ModTags.Entities.GUNNER)) {
            return;
        }

        long totalDayTime = entity.level().getDayTime();
        int currentDay = (int) (totalDayTime / 24000L);

        if (currentDay < GunMobValues.minDays) {
            return;
        }

        int daysOverMin = currentDay - GunMobValues.minDays;
        int currentChance = Math.min(GunMobValues.initialChance + (daysOverMin * GunMobValues.chanceIncrement), GunMobValues.maxChance);

        if (entity.getRandom().nextInt(100) >= currentChance) {
            return;
        }

        GunnerManager manager = GunnerManager.getInstance();
        Faction faction = manager.getFactionForMob(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()));

        if (faction != null) {
            entity.addTag("MobGunner");
        }
    }

    @SubscribeEvent
    public static void onLivingEquipmentChange(LivingEquipmentChangeEvent event) {
        if (!(event.getEntity() instanceof PathfinderMob mob)) {
            return;
        }

        ItemStack heldItem = mob.getMainHandItem();

        if (heldItem.getItem() instanceof GunItem) {
            reassessWeaponGoal(mob);
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(EntityTickEvent.Pre event) { // PORT: was the per-tick living event; now EntityTickEvent.Pre
        if (!GunMobValues.enabled) {
            return;
        }

        if (!(event.getEntity() instanceof PathfinderMob mob)) {
            return;
        }

        if (mob.tickCount >= 2) {
            return;
        }

        ItemStack heldItem = mob.getMainHandItem();

        if (mob.getTags().contains("MobGunner") && mob instanceof AbstractPiglin abstractPiglin && abstractPiglin.level().dimension() == Level.OVERWORLD) {
            abstractPiglin.setImmuneToZombification(true);
        }

        if (mob.getTags().contains("MobGunner") && !(heldItem.getItem() instanceof GunItem)) {
            GunnerManager manager = new GunnerManager(GunnerManager.getConfigFactions());
            Faction faction = manager.getFactionForMob(BuiltInRegistries.ENTITY_TYPE.getKey(mob.getType()));

            if (faction != null) {
                boolean isCloseRange = mob.getRandom().nextBoolean();
                int stopRange = isCloseRange ? 7 : 20;

                Item gun = faction.getRandomGun(isCloseRange);
                AIType aiType = AIType.values()[mob.getRandom().nextInt(AIType.values().length)];
                boolean elite = (mob.getRandom().nextFloat() < GunMobValues.eliteChance && GunMobValues.elitesEnabled);
                int aiLevel = faction.getAiLevel() + (elite ? 1 : 0);

                if (elite) {
                    gun = faction.getEliteGun();
                    applyEliteAttributes(mob);
                }

                if (!mob.level().isClientSide && !hasGunAttackGoal(mob)) {
                    mob.goalSelector.addGoal(2, new GunAttackGoal<>(mob, stopRange, 1.2F, aiType, aiLevel));
                    mob.addTag("GunAttackAssigned");
                }

                ItemStack modifiedGun = createModifiedGun(mob, gun);
                mob.setItemSlot(EquipmentSlot.MAINHAND, modifiedGun);
            }
        }

        if (heldItem.getItem() instanceof GunItem) {
            reassessWeaponGoal(mob);
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (!GunMobValues.enabled) {
            return;
        }

        if (!(event.getEntity() instanceof PathfinderMob mob)) {
            return;
        }

        mob.removeTag("GunAttackAssigned");

        ItemStack heldItem = mob.getMainHandItem();
        if (heldItem.getItem() instanceof GunItem) {
            reassessWeaponGoal(mob);
        }
    }

    public static boolean hasGunAttackGoal(PathfinderMob mob) {
        return mob.goalSelector.getAvailableGoals().stream()
                .anyMatch(goal -> goal.getGoal() instanceof GunAttackGoal<?>);
    }

    public static void reassessWeaponGoal(PathfinderMob mob) {
        if (mob.level().isClientSide || hasGunAttackGoal(mob)) {
            return;
        }

        AIType aiType = AIType.values()[mob.getRandom().nextInt(AIType.values().length)];
        boolean isCloseRange = mob.getRandom().nextBoolean();
        int stopRange = isCloseRange ? 7 : 20;
        int aiDifficulty = mob.getRandom().nextInt(4) + 1;

        mob.goalSelector.addGoal(2, new GunAttackGoal<>(mob, stopRange, 1.2F, aiType, aiDifficulty));
        mob.addTag("GunAttackAssigned");
    }

    private static void applyEliteAttributes(PathfinderMob mob) {
        mob.addTag("EliteGunner");
        mob.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        mob.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.TURTLE_HELMET));
        mob.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, -1, 2, false, true));
        mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, -1, 1, false, false));
    }

    private static ItemStack createModifiedGun(PathfinderMob mob, Item gun) {
        ItemStack gunStack = new ItemStack(gun);
        // PORT: original quirk kept - a fresh stack never has a tag, so this branch never ran in 1.20.1 either.
        if (gun instanceof GunItem gunItem && StackData.has(gunStack)) {
            Gun gunModified = gunItem.getModifiedGun(gunStack);
            int ammo = mob.getRandom().nextInt(gunModified.getReloads().getMaxAmmo());
            StackData.edit(gunStack, tag -> tag.putInt("AmmoCount", ammo));
        }
        return gunStack;
    }
}
