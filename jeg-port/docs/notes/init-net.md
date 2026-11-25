# init-net notes
(resumed attempt; messages mostly done by previous attempt)

## Resumed attempt (final state)
done: J/network/** (prev attempt), J/init/* (ModCommands finished: BuiltInRegistries + ResourceLocation.parse + StackData), J/Config.java (ForgeConfigSpec -> ModConfigSpec, field names unchanged),
J/JustEnoughGuns.java (ctor `(IEventBus bus, ModContainer container)`), J/annotation, J/cache, J/interfaces (no changes needed; identical to original), J/Reference.java (untouched).

### (a) API changes others must know
- Config.clientSpec/commonSpec/serverSpec are now ModConfigSpec (same names, package-private).
- JustEnoughGuns: constructor changed; datagen hook removed; compat flags (controllableLoaded, valkyrienSkiesLoaded, devilFruitsLoaded, gunnersLoaded, recruitsLoaded, guardsLoaded,
  shoulderSurfingLoaded, aQuietPlaceLoaded) kept as fields but always false (PORT-DROPPED). playerReviveLoaded / yungsNetherFortLoaded still use ModList.
- Main class no longer calls `NeoForge.EVENT_BUS.register` for TrumpetSkeletonAI, GunnerMobSpawner, ModCommandsRegister: those slices MUST annotate them with
  `@EventBusSubscriber(modid = Reference.MOD_ID)` (game bus). ServerTickHandler is registered by class (needs static @SubscribeEvent methods, NO @EventBusSubscriber, else double-registered). BoundingBoxManager is registered by instance when Config improvedHitboxes.
- Entity renderers now registered in a mod-bus EntityRenderersEvent.RegisterRenderers listener (inside nested ClientInit class) - client slices must not register the same types again.
- ModLootModifiers.register(IEventBus) is still called (must exist in blocks-world slice).
- Framework login data (network_gun_manager, custom_gun_manager) registration dropped; client/CustomGunManager.LoginData (ILoginData) is dead and must be removed by client slice.
- Client entry points called from main class: FrameworkClientAPI.registerDataLoader(MetaLoader.getInstance()); ClientHandler.onRegisterCreativeTab(IEventBus); KeyBinds.registerKeyMappings;
  CrosshairHandler.onConfigReload (ModConfigEvent.Reloading, mod bus); ClientHandler.onRegisterReloadListener; ClientHandler.registerAdditional; ClientHandler.setup (enqueueWork in FMLClientSetupEvent).
  Creative tab is still built by ClientHandler.onRegisterCreativeTab (client-only, as in original) - not moved into init.
- Other calls from main: ConfigPackLoader.exportSampleResourcesIfMissing/exportSampleDataIfMissing, ScrapBinBlock.bootStrap, GunMobValues.init, ProjectileManager.registerFactory, ModifierLoader (AddReloadListenerEvent via NeoForge bus).

### (b) PORT-VERIFY / PORT-DROPPED
- JustEnoughGuns.java: workbench_ingredient registration (CraftingHelper gone) - items-crafting must make WorkbenchIngredient an ICustomIngredient/IngredientType and register it (e.g. own DeferredRegister on NeoForgeRegistries.Keys.INGREDIENT_TYPES; add `.register(bus)` call in main ctor).
- JustEnoughGuns.java: PacketHandler.init() still run in FMLCommonSetupEvent enqueueWork per guide (verify Framework requires registration earlier).
- ModEnchantments has no REGISTER (data-driven) -> not registered.
- ModContainers.java: IMenuTypeExtension.create PORT-VERIFY (prev attempt).
- ModCommands.java 355/419: original `getTag() != null` check on a fresh stack kept as StackData.has (likely always false, as in original).
- ModCommandsRegister etc. (event slice) must be ported to ModCommands.register(dispatcher) on NeoForge.EVENT_BUS (RegisterCommandsEvent).

### (d) Not finished
Nothing known unfinished in owned files; nothing was compiled. UNSEEN imports remain (ModConfigSpec Pair, DeferredSpawnEggItem, PoiType, FeatureFlags, etc.) unverified.
