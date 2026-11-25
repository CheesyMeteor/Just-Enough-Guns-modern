# Integration pass notes (cross-slice items)

Nothing was compiled (no compiler in the authoring sandbox). Everything below was done by inspection + scripts.
Scripts used: `tools/check_port.py` (improved: now skips block comments / trailing `//` comments and no longer
false-positives on `ClientTickEvent.`/`PlayerTickEvent.`; BANNED and INTERNAL are now both EMPTY, UNSEEN = 439 external
imports never used by the compiling reference port, spot-checked by name) plus ad-hoc sweeps (see "Sweeps").

## Resolved (known list, 1-13)
1. BANNED hits: the remaining checker hits were false positives (commented-out BasicTurret*, TickEvent regex). Real fixes:
   - `util/bee/InhabitantStorage`: no INBTSerializable / NbtUtils BlockPos; FlowerPos now stored as an int array. `util/bee/CapabilityBee` DELETED.
   - `BeehiveBlockEntityAbstract`: LazyOptional/getCapability already gone (plain `IInhabitantStorage` field). `onDataPacket` no longer
     calls `loadPacketNBT(pkt.getTag())` again (super.onDataPacket -> loadAdditional already does it).
2. `EVENT_BUS.post(..)` as boolean: all sites already use `.isCanceled()` (BeehiveBlockEntityAbstract, ShootingHandler, ReloadHandler, ServerPlayHandler, projectiles, C2S reload). No bare boolean use left.
3. `ModParticleTypes`: BULLET_HOLE/LASER/TRAIL/COLORED_FLARE_SMOKE use new `createOptions(MapCodec, overrideLimiter, StreamCodec)` helper (flags false/false/false/true).
4. `ModCommands` `GOAT_HORN_SOUND_VARIANTS.get(2).value()`.
5. `ClientHandler.registerScreenFactories(RegisterMenuScreensEvent)` (public static, hooked with `bus.addListener` in `JustEnoughGuns.ClientInit`). `MenuScreens`/`ItemBlockRenderTypes` imports removed.
6. `AnimatedGunRenderer`: DELIBERATELY NOT registered on any bus. In the 1.20.1 original nothing ever registered it either (grep of orig: only AnimatedGunItem references it),
   sprint/roll state is copied from GunRenderingHandler at render time, and `AnimatedGunRenderer.get()` would build a renderer with a null model path. A comment in the file says so.
   `AnimatedGunItem` and `GunAnimations`: `software.bernie.geckolib.util.ClientUtils` -> `ClientUtil`.
7. `render_type` added to block models: scrap/gunmetal/gunnite workbench, schematic_station, blueprint_workbench, recycler, ammo_box = `minecraft:cutout`; dynamic_light = `minecraft:translucent`.
8. boohive / boo_nest silk-touch: `BeehiveBlockEntityAbstract` now overrides `collectImplicitComponents` / `applyImplicitComponents` and carries its "BeeList" NBT in `DataComponents.CUSTOM_DATA`;
   both loot tables `copy_components` now include `minecraft:custom_data` (besides `minecraft:bees`).
9. `getPersistentData`: no block-entity use left (Delay -> `DynamicLightBlockEntity.getDelay/setDelay`; the save/load round-trips in `AIGunEvent` and `FlameProjectileEntity` replaced by it).
   `C2SMessageChargeSync` -> `ChargeTracker.setServerChargeProgress(uuid, f)` (in-memory server map), read by `ProjectileEntity` via `ChargeTracker.getServerChargeProgress`.
   Still on PLAYER persistent data (proven to compile in the reference port, and needs to survive death via PlayerCloneHandler): `FirstJoinMessageHandler` / `PlayerCloneHandler` first-join flag.
10. Trigger Finger vs automatic guns: items-crafting's `GunItem.supportsEnchantment` (refuses TRIGGER_FINGER when `getModifiedGun(stack).getGeneral().getFireMode() == FireMode.AUTOMATIC`) is the single
    source of truth; events-misc's JSON (`supported_items: #jeg:enchantable/gun`) is the coarse filter. They agree; the "can be applied to automatic guns" caveat in events-misc.md is OBSOLETE.
    Not enforced: a command/creative-given enchant on an automatic gun (same as the original, which only blocked table/anvil).
11. `client/CustomGunManager.LoginData` was already gone (only a PORT-DROPPED comment remains).
12. init/*: every `ModX.NAME` / `Reference.` / `Config.` symbol referenced anywhere resolves (script). Main class registers: Blocks, Containers, Effects, Entities, Items, ParticleTypes,
    RecipeSerializers, RecipeTypes, Sounds, TileEntities, PointOfInterestTypes, LootModifiers (mod bus) + the 3 configs + Framework network (`PacketHandler.init()` now called directly in
    FMLCommonSetupEvent like Framework's own network test, NOT inside enqueueWork) + synced data keys (enqueueWork, as in the Framework test) + client init (data loader, creative tab, key mappings, config reload,
    reload listener, additional models, menu screens, FMLClientSetup, entity renderers). ModEnchantments / ModDamageTypes / ModTags have no registers on purpose (data-driven).
    Every entity type has a renderer (GunEntityRenderers + main-class ClientInit). All 35 network messages have STREAM_CODEC + handle and are registered. Added `RegisterCapabilitiesEvent`
    (ModCommonEventBus) for Recycler + Ammo Box item-handler (SidedInvWrapper) - replaces the Forge capability dropped by blocks-world.
13. `neoforge.mods.toml`: added `[[accessTransformers]] file="META-INF/accesstransformer.cfg"`; mixins config present; framework modId is `framework` (checked in Framework source).
    `jeg.mixins.json`: all 15 listed mixin classes exist in `ttv.migami.jeg.mixin.{common,client}`, plugin class exists. `build.gradle`: AT file is passed to ModDevGradle
    (`accessTransformers = project.files(...)`, same syntax as the reference port); removed the Sponge mixin annotation processor / mixinextras compileOnly lines (provided by the NeoForge dev classpath; they needed repos we did not declare), added `mavenCentral()`.

## Extra real bugs found and fixed by the sweeps
- `ThrowableStunGrenadeEntity:101` the closing `)` of an `if` was inside a `//` comment (would not compile). Fixed. Scripted check for the same pattern elsewhere: none.
- `ModEffects.X.get()` passed where a `Holder<MobEffect>` is required (`getEffect`, `new MobEffectInstance`) in StunRingingSound, SoundHandler (x2), ResonanceProjectileEntity (x3), SmokeCloud. Now pass the DeferredHolder itself.
- `ThrowableStunGrenadeEntity`: `effect.is(ModEffects.BLINDED)` -> `effect.is(ModEffects.BLINDED.getId())` (Holder.is(ResourceLocation) is certain to exist).
- `BlueprintWorkbenchScreen`: missing `import ...util.StackData`.

## Sweeps run (all clean after the fixes above)
- every `import ttv.migami.jeg...` resolves to an existing class (incl. nested/static); every `Reference.X`, `Config.X`, `ModBlocks/ModItems/ModEntities/ModTileEntities/ModContainers/ModEffects/ModSounds/ModParticleTypes/ModRecipeTypes/ModRecipeSerializers/ModEnchantments/ModSyncedDataKeys/ModTags/ModDamageTypes/ModPointOfInterestTypes.X` exists;
- heuristic missing-import detector (simple name imported elsewhere in the project/ref port, used here, not imported): only `java.awt.*` wildcard hits and `Entity.RemovalReason` (inherited) remained, plus the StackData one fixed;
- brace/paren/bracket balance of all 551 files; every `com.mrcrayfish.framework` import exists in the Framework source; mixin list vs files; sounds.json vs ModSounds; blockstates/particle JSON vs registrations
  (`bright_dynamic_light` has no blockstate - same as the original; item models missing only for commented-out items).

## STILL UNVERIFIED - expect compile / startup errors here (grep PORT-VERIFY for the full list)
- Access transformer member names from memory (accesstransformer.cfg): Bee `isTiredOfLookingForNectar`, `spawnFluidParticle`, inner-goal ctors, `BeePollinateGoal#isPollinating`, `BeeGoToHiveGoal#clearBlacklist/isTargetBlacklisted/blacklistTarget`. A wrong name fails at STARTUP with a message naming it.
- Mixin targets (all risky injectors have `require = 0`; check the log for "Mixin ... failed"): EndPortalBlockMixin, GameRendererMixin, MinecraftMixin, MouseHandlerMixin, AbstractDragonSittingPhase#onHurt, Player#isScoping.
- GeckoLib 4.8.3 hook signatures in AnimatedGunRenderer/AttachmentRenderer/TerrorPhantomModel/PhantomGunnerRenderer (renderRecursively/renderFinal int colour), ParticleKeyframeEvent package.
- `GunItem.supportsEnchantment(ItemStack, Holder<Enchantment>)` NeoForge signature; `Holder.is(ResourceKey)`.
- ConfigPackLoader Pack API (PackLocationInfo/PackSelectionConfig), ProjectileExplosion 13-arg Explosion ctor, ClientboundExplodePacket ctor, FireworkRocketEntity ctor, MenuType/IMenuTypeExtension.create, RenderUtil putBulkData, SoundHandler reflection field `instanceToChannel`.
- Tag names `c:gunpowders`, `c:sandstone/blocks`, `c:glass_blocks`; loot function names `enchanted_count_increase`, `random_chance_with_enchanted_bonus`, `copy_components` with `custom_data`.
- Behavioural deviations to know: TerrorPhantom drops loot items instead of placing barrels; GunnerMobSpawner follow-range modifier removed; Shoulder Surfing/Controllable/etc. compat dropped; `mob_remover_cherry_grove` biome modifier stays extension-less (not loaded, as in the original).

## Not done (by instruction)
- docs/STAGE2.md is untouched.
