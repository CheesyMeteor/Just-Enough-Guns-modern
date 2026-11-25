package ttv.migami.jeg.init;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.entity.Bubble;
import ttv.migami.jeg.entity.DynamicHelmet;
import ttv.migami.jeg.entity.Splash;
import ttv.migami.jeg.entity.animal.Boo;
import ttv.migami.jeg.entity.monster.Ghoul;
import ttv.migami.jeg.entity.monster.phantom.gunner.PhantomGunner;
import ttv.migami.jeg.entity.monster.phantom.terror.TerrorPhantom;
import ttv.migami.jeg.entity.projectile.*;
import ttv.migami.jeg.entity.throwable.*;
import ttv.migami.jeg.faction.raid.RaidEntity;
import ttv.migami.jeg.faction.raid.TerrorRaidEntity;

import java.util.function.BiFunction;

/**
 * Author: MrCrayfish
 */
public class ModEntities
{
    public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(Registries.ENTITY_TYPE, Reference.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<RaidEntity>> RAID_ENTITY = REGISTER.register("raid_entity", () -> EntityType.Builder.<RaidEntity>of(RaidEntity::new, MobCategory.MISC).sized(3.0F, 3.0F).noSummon().noSave().fireImmune().build("raid_entity"));
    public static final DeferredHolder<EntityType<?>, EntityType<TerrorRaidEntity>> TERROR_RAID_ENTITY = REGISTER.register("terror_raid_entity", () -> EntityType.Builder.<TerrorRaidEntity>of(TerrorRaidEntity::new, MobCategory.MISC).sized(3.0F, 3.0F).noSummon().noSave().fireImmune().build("terror_raid_entity"));

    public static final DeferredHolder<EntityType<?>, EntityType<ArrowProjectileEntity>> ARROW_PROJECTILE = registerProjectile("arrow_projectile", ArrowProjectileEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<FlameProjectileEntity>> FLAME_PROJECTILE = registerProjectile("flame_projectile", FlameProjectileEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<BeamEntity>> BEAM = registerBasic("beam", BeamEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<ProjectileEntity>> PROJECTILE = registerProjectile("projectile", ProjectileEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<SpectreProjectileEntity>> SPECTRE_PROJECTILE = registerProjectile("spectre_projectile", SpectreProjectileEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<WaterProjectileEntity>> WATER_PROJECTILE = registerProjectile("water_projectile", WaterProjectileEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<BlazeProjectileEntity>> BLAZE_PROJECTILE = registerProjectile("blaze_projectile", BlazeProjectileEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<SonicProjectileEntity>> SONIC_PROJECTILE = registerProjectile("sonic_projectile", SonicProjectileEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<ResonanceProjectileEntity>> RESONANCE_PROJECTILE = registerProjectile("resonance_projectile", ResonanceProjectileEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<FlareProjectileEntity>> FLARE_PROJECTILE = registerBasic("flare_projectile", FlareProjectileEntity::new);

    public static final DeferredHolder<EntityType<?>, EntityType<WhirpoolEntity>> WATER_BOMB = registerBasic("water_bomb", WhirpoolEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<PocketBubbleEntity>> POCKET_BUBBLE = registerBasic("pocket_bubble", PocketBubbleEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<GrenadeEntity>> GRENADE = registerBasic("grenade", GrenadeEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<RocketEntity>> ROCKET = registerBasic("rocket", RocketEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<ThrowableGrenadeEntity>> THROWABLE_GRENADE = registerBasic("throwable_grenade", ThrowableGrenadeEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<ThrowableStunGrenadeEntity>> THROWABLE_STUN_GRENADE = registerBasic("throwable_stun_grenade", ThrowableStunGrenadeEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<ThrowableSmokeGrenadeEntity>> THROWABLE_SMOKE_GRENADE = registerBasic("throwable_smoke_grenade", ThrowableSmokeGrenadeEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<ThrowableMolotovCocktailEntity>> THROWABLE_MOLOTOV_COCKTAIL = registerBasic("throwable_molotov_cocktail", ThrowableMolotovCocktailEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<ThrowableWaterBombEntity>> THROWABLE_WATER_BOMB = registerBasic("throwable_water_bomb", ThrowableWaterBombEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<ThrowablePocketBubbleEntity>> THROWABLE_POCKET_BUBBLE = registerBasic("throwable_pocket_bubble", ThrowablePocketBubbleEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<ThrowableFlareEntity>> THROWABLE_FLARE = registerBasic("throwable_flare", ThrowableFlareEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<ThrowableExplosiveChargeEntity>> THROWABLE_EXPLOSIVE_CHARGE = registerBasic("throwable_explosive_charge", ThrowableExplosiveChargeEntity::new);

    /* Score Streaks */
    public static final DeferredHolder<EntityType<?>, EntityType<ThrowablePhantomGunnerBaitEntity>> THROWABLE_PHANTOM_GUNNER_BAIT = registerBasic("throwable_phantom_gunner_bait", ThrowablePhantomGunnerBaitEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<ThrowableAirStrikeFlareEntity>> THROWABLE_TERROR_PHANTOM_FLARE = registerBasic("throwable_terror_phantom_flare", ThrowableAirStrikeFlareEntity::new);

    /* Mobs */
    public static final DeferredHolder<EntityType<?>, EntityType<Ghoul>> GHOUL = REGISTER.register("ghoul", () -> EntityType.Builder.of(Ghoul::new, MobCategory.MONSTER).sized(0.6F, 1.95F).build("ghoul"));
    public static final DeferredHolder<EntityType<?>, EntityType<Boo>> BOO = REGISTER.register("boo", () -> EntityType.Builder.of(Boo::new, MobCategory.CREATURE).sized(0.7F, 0.6F).fireImmune().build("boo"));
    public static final DeferredHolder<EntityType<?>, EntityType<TerrorPhantom>> TERROR_PHANMTOM = REGISTER.register("terror_phantom", () -> EntityType.Builder.of(TerrorPhantom::new, MobCategory.MONSTER).sized(5.0F, 2.0F).fireImmune().build("terror_phantom"));
    public static final DeferredHolder<EntityType<?>, EntityType<PhantomGunner>> PHANTOM_GUNNER = REGISTER.register("phantom_gunner", () -> EntityType.Builder.of(PhantomGunner::new, MobCategory.MONSTER).sized(4.0F, 1.0F).fireImmune().build("phantom_gunner"));

    /* Custom */
    public static final DeferredHolder<EntityType<?>, EntityType<DynamicHelmet>> DYNAMIC_HELMET = REGISTER.register("dynamic_helmet", () -> EntityType.Builder.<DynamicHelmet>of(DynamicHelmet::new, MobCategory.MISC).sized(0.5F, 0.5F).noSummon().clientTrackingRange(8).updateInterval(1).fireImmune().build("dynamic_helmet"));
    public static final DeferredHolder<EntityType<?>, EntityType<Splash>> SPLASH = REGISTER.register("splash", () -> EntityType.Builder.<Splash>of(Splash::new, MobCategory.MISC).sized(5.0F, 5.0F).noSave().noSummon().fireImmune().build("splash"));
    public static final DeferredHolder<EntityType<?>, EntityType<Bubble>> BUBBLE = REGISTER.register("bubble", () -> EntityType.Builder.<Bubble>of(Bubble::new, MobCategory.MISC).sized(3.0F, 1.0F).noSave().noSummon().build("bubble"));

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> registerBasic(String id, BiFunction<EntityType<T>, Level, T> function)
    {
        return REGISTER.register(id, () -> EntityType.Builder.of(function::apply, MobCategory.MISC)
                .sized(0.25F, 0.25F)
                .clientTrackingRange(100)
                .updateInterval(1)
                .noSummon()
                .fireImmune()
                .noSave().build(id));
    }

    /**
     * Entity registration that prevents the entity from being sent and tracked by clients. Projectiles
     * are rendered separately from Minecraft's entity rendering system and their logic is handled
     * exclusively by the server, why send them to the client. Projectiles also have very short time
     * in the world and are spawned many times a tick. There is no reason to send unnecessary packets
     * when it can be avoided to drastically improve the performance of the game.
     *
     * @param id       the id of the projectile
     * @param function the factory to spawn the projectile for the server
     * @param <T>      an entity that is a projectile entity
     * @return A registry object containing the new entity type
     */
    private static <T extends ProjectileEntity> DeferredHolder<EntityType<?>, EntityType<T>> registerProjectile(String id, BiFunction<EntityType<T>, Level, T> function)
    {
        return REGISTER.register(id, () -> EntityType.Builder.of(function::apply, MobCategory.MISC)
                .sized(0.25F, 0.25F)
                .clientTrackingRange(0)
                .noSummon()
                .fireImmune()
                .noSave()
                .build(id));
    }

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> registerLight(String id, BiFunction<EntityType<T>, Level, T> function)
    {
        return REGISTER.register(id, () -> EntityType.Builder.of(function::apply, MobCategory.MISC)
                .sized(0.01F, 0.01F)
                .clientTrackingRange(256)
                .updateInterval(1)
                .noSummon()
                .fireImmune()
                .build(id));
    }
}
