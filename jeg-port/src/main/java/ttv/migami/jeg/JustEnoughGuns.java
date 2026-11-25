package ttv.migami.jeg;

import com.mrcrayfish.framework.api.FrameworkAPI;
import com.mrcrayfish.framework.api.client.FrameworkClientAPI;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ttv.migami.jeg.block.ScrapBinBlock;
import ttv.migami.jeg.client.ClientHandler;
import ttv.migami.jeg.client.KeyBinds;
import ttv.migami.jeg.client.MetaLoader;
import ttv.migami.jeg.client.handler.CrosshairHandler;
import ttv.migami.jeg.client.render.entity.*;
import ttv.migami.jeg.common.BoundingBoxManager;
import ttv.migami.jeg.common.ProjectileManager;
import ttv.migami.jeg.entity.client.BubbleRenderer;
import ttv.migami.jeg.entity.client.PhantomGunnerRenderer;
import ttv.migami.jeg.entity.client.SplashRenderer;
import ttv.migami.jeg.entity.client.TerrorPhantomRenderer;
import ttv.migami.jeg.entity.projectile.*;
import ttv.migami.jeg.entity.throwable.GrenadeEntity;
import ttv.migami.jeg.event.ConfigPackLoader;
import ttv.migami.jeg.event.ServerTickHandler;
import ttv.migami.jeg.faction.GunMobValues;
import ttv.migami.jeg.init.*;
import ttv.migami.jeg.modifier.ModifierLoader;
import ttv.migami.jeg.network.PacketHandler;
import ttv.migami.jeg.world.loot.ModLootModifiers;

@Mod(Reference.MOD_ID)
public class JustEnoughGuns {
    public static boolean debugging = false;
    public static boolean controllableLoaded = false;
    public static boolean playerReviveLoaded = false;
    public static boolean yungsNetherFortLoaded = false;
    public static boolean valkyrienSkiesLoaded = false;
    public static boolean devilFruitsLoaded = false;
    public static boolean gunnersLoaded = false;
    public static boolean recruitsLoaded = false;
    public static boolean guardsLoaded = false;
    public static boolean shoulderSurfingLoaded = false;
    // I am not okay with the original mod's name haha
    public static boolean aQuietPlaceLoaded = false;
    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);

    public JustEnoughGuns(IEventBus bus, ModContainer container) {
        ConfigPackLoader.exportSampleResourcesIfMissing();
        ConfigPackLoader.exportSampleDataIfMissing();

        container.registerConfig(ModConfig.Type.CLIENT, Config.clientSpec);
        container.registerConfig(ModConfig.Type.COMMON, Config.commonSpec);
        container.registerConfig(ModConfig.Type.SERVER, Config.serverSpec);
        ModBlocks.REGISTER.register(bus);
        ModContainers.REGISTER.register(bus);
        ModEffects.REGISTER.register(bus);
        // PORT-VERIFY: enchantments are data-driven in 1.21 (data/jeg/enchantment), nothing to register on the mod bus
        ModEntities.REGISTER.register(bus);
        ModItems.REGISTER.register(bus);
        ModParticleTypes.REGISTER.register(bus);
        ModRecipeSerializers.REGISTER.register(bus);
        ModRecipeTypes.REGISTER.register(bus);
        ModSounds.REGISTER.register(bus);
        ModTileEntities.REGISTER.register(bus);
        ModPointOfInterestTypes.REGISTER.register(bus);
        ModLootModifiers.register(bus);
        bus.addListener(this::onCommonSetup);

        // GeckoLib 4.8 on NeoForge initialises itself (GeckoLib.initialize() is gone)

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientInit.init(bus);
        }
        // TrumpetSkeletonAI, GunnerMobSpawner and ModCommandsRegister are auto-registered by their own
        // @EventBusSubscriber annotation in NeoForge, so they are no longer registered by hand here.
        NeoForge.EVENT_BUS.addListener(this::onAddReloadListeners);
        NeoForge.EVENT_BUS.register(ServerTickHandler.class);
        controllableLoaded = false; // PORT-DROPPED: Controllable
        playerReviveLoaded = ModList.get().isLoaded("playerrevive");
        valkyrienSkiesLoaded = false; // PORT-DROPPED: Valkyrien Skies
        yungsNetherFortLoaded = ModList.get().isLoaded("betterfortresses");
        devilFruitsLoaded = false; // PORT-DROPPED: Superpower Allstars
        gunnersLoaded = false; // PORT-DROPPED: Villager Gunners
        recruitsLoaded = false; // PORT-DROPPED: Recruits
        guardsLoaded = false; // PORT-DROPPED: Guard Villagers
        shoulderSurfingLoaded = false; // PORT-DROPPED: Shoulder Surfing
        aQuietPlaceLoaded = false; // PORT-DROPPED: Death Angels
    }

    public void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new ModifierLoader());
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        // Framework's own network test builds its channels directly in FMLCommonSetupEvent (not enqueued), so payloads are
        // registered before RegisterPayloadHandlersEvent fires.
        PacketHandler.init();
        event.enqueueWork(() ->
        {
            ScrapBinBlock.bootStrap();
            GunMobValues.init();
            FrameworkAPI.registerSyncedDataKey(ModSyncedDataKeys.AIMING);
            FrameworkAPI.registerSyncedDataKey(ModSyncedDataKeys.RELOADING);
            FrameworkAPI.registerSyncedDataKey(ModSyncedDataKeys.SHOOTING);
            FrameworkAPI.registerSyncedDataKey(ModSyncedDataKeys.BURST_COUNT);
            // PORT-DROPPED: Framework login data (network_gun_manager / custom_gun_manager) no longer exists; common-util syncs guns via OnDatapackSyncEvent + S2CMessageUpdateGuns
            // PORT-VERIFY: CraftingHelper.register("workbench_ingredient", WorkbenchIngredient.Serializer.INSTANCE) has no 1.21 equivalent here;
            //   WorkbenchIngredient must become a NeoForge ICustomIngredient + IngredientType registered by the items-crafting slice (see notes).
            ProjectileManager.getInstance().registerFactory(Items.ARROW, (worldIn, entity, weapon, item, modifiedGun) -> new ArrowProjectileEntity(ModEntities.ARROW_PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
            ProjectileManager.getInstance().registerFactory(Items.FIRE_CHARGE, (worldIn, entity, weapon, item, modifiedGun) -> new FlameProjectileEntity(ModEntities.FLAME_PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
            ProjectileManager.getInstance().registerFactory(ModItems.RIFLE_AMMO.get(), (worldIn, entity, weapon, item, modifiedGun) -> new ProjectileEntity(ModEntities.PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
            ProjectileManager.getInstance().registerFactory(ModItems.PISTOL_AMMO.get(), (worldIn, entity, weapon, item, modifiedGun) -> new ProjectileEntity(ModEntities.PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
            ProjectileManager.getInstance().registerFactory(ModItems.HANDMADE_SHELL.get(), (worldIn, entity, weapon, item, modifiedGun) -> new ProjectileEntity(ModEntities.PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
            ProjectileManager.getInstance().registerFactory(ModItems.SHOTGUN_SHELL.get(), (worldIn, entity, weapon, item, modifiedGun) -> new ProjectileEntity(ModEntities.PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
            ProjectileManager.getInstance().registerFactory(ModItems.SPECTRE_ROUND.get(), (worldIn, entity, weapon, item, modifiedGun) -> new SpectreProjectileEntity(ModEntities.SPECTRE_PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
            ProjectileManager.getInstance().registerFactory(ModItems.BLAZE_ROUND.get(), (worldIn, entity, weapon, item, modifiedGun) -> new BlazeProjectileEntity(ModEntities.BLAZE_PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
            ProjectileManager.getInstance().registerFactory(Items.ECHO_SHARD, (worldIn, entity, weapon, item, modifiedGun) -> new SonicProjectileEntity(ModEntities.SONIC_PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
            ProjectileManager.getInstance().registerFactory(Items.EMERALD, (worldIn, entity, weapon, item, modifiedGun) -> new ResonanceProjectileEntity(ModEntities.RESONANCE_PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
            ProjectileManager.getInstance().registerFactory(Items.REDSTONE_TORCH, (worldIn, entity, weapon, item, modifiedGun) -> new BeamEntity(ModEntities.BEAM.get(), worldIn, entity, weapon, item, modifiedGun));

            ProjectileManager.getInstance().registerFactory(ModItems.FLARE.get(), (worldIn, entity, weapon, item, modifiedGun) -> new FlareProjectileEntity(ModEntities.FLARE_PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun));
            ProjectileManager.getInstance().registerFactory(ModItems.WATER_BOMB.get(), (worldIn, entity, weapon, item, modifiedGun) -> new WhirpoolEntity(ModEntities.WATER_BOMB.get(), worldIn, entity, weapon, item, modifiedGun));
            ProjectileManager.getInstance().registerFactory(ModItems.POCKET_BUBBLE.get(), (worldIn, entity, weapon, item, modifiedGun) -> new PocketBubbleEntity(ModEntities.POCKET_BUBBLE.get(), worldIn, entity, weapon, item, modifiedGun));
            ProjectileManager.getInstance().registerFactory(ModItems.GRENADE.get(), (worldIn, entity, weapon, item, modifiedGun) -> new GrenadeEntity(ModEntities.GRENADE.get(), worldIn, entity, weapon, item, modifiedGun));
            ProjectileManager.getInstance().registerFactory(ModItems.ROCKET.get(), (worldIn, entity, weapon, item, modifiedGun) -> new RocketEntity(ModEntities.ROCKET.get(), worldIn, entity, weapon, item, modifiedGun));
            if (Config.COMMON.gameplay.improvedHitboxes.get()) {
                NeoForge.EVENT_BUS.register(new BoundingBoxManager());
            }
        });
    }

    /** Client-only wiring, kept in a nested class so dedicated servers never load client classes. */
    private static final class ClientInit {
        static void init(IEventBus bus) {
            FrameworkClientAPI.registerDataLoader(MetaLoader.getInstance());
            ClientHandler.onRegisterCreativeTab(bus);
            bus.addListener(KeyBinds::registerKeyMappings);
            bus.addListener(CrosshairHandler::onConfigReload);
            bus.addListener(ClientHandler::onRegisterReloadListener);
            bus.addListener(ClientHandler::registerAdditional);
            bus.addListener(ClientHandler::registerScreenFactories);
            bus.addListener(ClientInit::onClientSetup);
            bus.addListener(ClientInit::onRegisterRenderers);
        }

        private static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(ClientHandler::setup);
        }

        // 1.21: entity renderers are registered through EntityRenderersEvent.RegisterRenderers instead of EntityRenderers.register in client setup
        private static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.RAID_ENTITY.get(), RaidEntityRenderer::new);
            event.registerEntityRenderer(ModEntities.TERROR_RAID_ENTITY.get(), TerrorRaidEntityRenderer::new);
            event.registerEntityRenderer(ModEntities.GHOUL.get(), GhoulRenderer::new);
            event.registerEntityRenderer(ModEntities.BOO.get(), BooRenderer::new);
            event.registerEntityRenderer(ModEntities.TERROR_PHANMTOM.get(), TerrorPhantomRenderer::new);
            event.registerEntityRenderer(ModEntities.PHANTOM_GUNNER.get(), PhantomGunnerRenderer::new);
            event.registerEntityRenderer(ModEntities.DYNAMIC_HELMET.get(), DynamicHelmetRenderer::new);
            event.registerEntityRenderer(ModEntities.SPLASH.get(), SplashRenderer::new);
            event.registerEntityRenderer(ModEntities.BUBBLE.get(), BubbleRenderer::new);
        }
    }

    // PORT-DROPPED: datagen (onGatherData) removed with the datagen package

    public static boolean isDebugging() {
        return false; //!FMLEnvironment.production;
    }
}
