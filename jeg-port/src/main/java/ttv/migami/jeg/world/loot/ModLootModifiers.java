package ttv.migami.jeg.world.loot;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import ttv.migami.jeg.Reference;

// PORT: no-op @Mod.EventBusSubscriber removed; registered via ModLootModifiers.register(modBus) from the mod constructor.
public class ModLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Reference.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<ScrapOnSimpleDungeonModifier>> SCRAP_SIMPLE_DUNGEON =
            LOOT_MODIFIER_SERIALIZERS.register("scrap_on_simple_dungeon", ScrapOnSimpleDungeonModifier.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<TechTrashOnSimpleDungeonModifier>> TECH_TRASH_SIMPLE_DUNGEON =
            LOOT_MODIFIER_SERIALIZERS.register("tech_trash_on_simple_dungeon", TechTrashOnSimpleDungeonModifier.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<AtlanteanSpearOnShipwreckTreasureModifier>> ATLANTEAN_SPEAR_SHIPWRECK_TREASURE =
            LOOT_MODIFIER_SERIALIZERS.register("atlantean_spear_on_shipwreck_treasure", AtlanteanSpearOnShipwreckTreasureModifier.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<TyphooneeOnElderGuardianModifier>> TYPHOONE_ELDER_GUARDIAN =
            LOOT_MODIFIER_SERIALIZERS.register("typhoonee_on_elder_guardian", TyphooneeOnElderGuardianModifier.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<GrenadeOnCreeperModifier>> GRENADE_CREEPER =
            LOOT_MODIFIER_SERIALIZERS.register("grenade_on_creeper", GrenadeOnCreeperModifier.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<RepeatingShotgunOnMineshaftModifier>> REPEATING_SHOTGUN_MINESHAFT =
            LOOT_MODIFIER_SERIALIZERS.register("repeating_shotgun_on_abandoned_mineshaft", RepeatingShotgunOnMineshaftModifier.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<RepeatingShotgunBPOnFishingModifier>> REPEATING_SHOTGUN_FISHING =
            LOOT_MODIFIER_SERIALIZERS.register("repeating_shotgun_bp_on_fishing", RepeatingShotgunBPOnFishingModifier.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<InfantryRifleOnMineshaftModifier>> INFANTRY_RIFLE_MINESHAFT =
            LOOT_MODIFIER_SERIALIZERS.register("infantry_rifle_on_abandoned_mineshaft", InfantryRifleOnMineshaftModifier.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<InfantryRifleBPOnFishingModifier>> INFANTRY_RIFLE_FISHING =
            LOOT_MODIFIER_SERIALIZERS.register("infantry_rifle_bp_on_fishing", InfantryRifleBPOnFishingModifier.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<ServiceRifleOnMineshaftModifier>> SERVICE_RIFLE_MINESHAFT =
            LOOT_MODIFIER_SERIALIZERS.register("service_rifle_on_abandoned_mineshaft", ServiceRifleOnMineshaftModifier.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<ServiceRifleBPOnFishingModifier>> SERVICE_RIFLE_FISHING =
            LOOT_MODIFIER_SERIALIZERS.register("service_rifle_bp_on_fishing", ServiceRifleBPOnFishingModifier.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<HollenfireMK2BPOnBarteringModifier>> HOLLENFIRE_MK2_BP_BARTERING =
            LOOT_MODIFIER_SERIALIZERS.register("hollenfire_mk2_bp_on_bartering", HollenfireMK2BPOnBarteringModifier.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<SoulhunterMK2BPOnBarteringModifier>> SOULHUNTER_MK2_BP_BARTERING =
            LOOT_MODIFIER_SERIALIZERS.register("soulhunter_mk2_bp_on_bartering", SoulhunterMK2BPOnBarteringModifier.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<FireStarterOnBarteringModifier>> FIRE_STARTER_BARTERING =
            LOOT_MODIFIER_SERIALIZERS.register("fire_starter_on_bartering", FireStarterOnBarteringModifier.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<FlareGunOnMineshaftModifier>> FLARE_GUN_MINESHAFT =
            LOOT_MODIFIER_SERIALIZERS.register("flare_gun_on_abandoned_mineshaft", FlareGunOnMineshaftModifier.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<BlueprintOnWither>> BLUEPRINT_WITHER =
            LOOT_MODIFIER_SERIALIZERS.register("blueprint_on_wither", BlueprintOnWither.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<BlueprintOnWarden>> BLUEPRINT_WARDEN =
            LOOT_MODIFIER_SERIALIZERS.register("blueprint_on_warden", BlueprintOnWarden.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<AtlanticShooterOnShipwreck>> ATLANTIC_SHOOTER_ON_SHIPWRECK =
            LOOT_MODIFIER_SERIALIZERS.register("atlantic_shooter_on_shipwreck", AtlanticShooterOnShipwreck.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<PrimitiveBowOnJungleTempleModifier>> PRIMITIVE_BOW_JUNGLE_TEMPLE =
            LOOT_MODIFIER_SERIALIZERS.register("primitive_bow_on_jungle_temple", PrimitiveBowOnJungleTempleModifier.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<PrimitiveBlowpipeOnJungleTempleModifier>> PRIMITIVE_BLOWPIPE_JUNGLE_TEMPLE =
            LOOT_MODIFIER_SERIALIZERS.register("primitive_blowpipe_on_jungle_temple", PrimitiveBlowpipeOnJungleTempleModifier.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<FireSweeperBastionModifier>> FIRE_SWEEPER_BASTION =
            LOOT_MODIFIER_SERIALIZERS.register("fire_sweeper_bastion", FireSweeperBastionModifier.CODEC);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<VindicatorSMGEvokerModifier>> VINDICATOR_SMG_EVOKER =
            LOOT_MODIFIER_SERIALIZERS.register("vindicator_smg_evoker", VindicatorSMGEvokerModifier.CODEC);

    public static void register(IEventBus bus) {
        LOOT_MODIFIER_SERIALIZERS.register(bus);
    }
}