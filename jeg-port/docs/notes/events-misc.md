# events-misc notes
## Progress (append "done:" lines)
done: enchantment/* -> deleted the 14 class files (Enchantment is a record in 1.21; behaviour lives in util/GunEnchantmentHelper + callers, which already use ResourceKeys).
  NEW data: data/jeg/enchantment/{12}.json, data/jeg/tags/enchantment/exclusive_set/{ammo,weapon,projectile}.json (replaces GunEnchantment.Type incompat rule; BOSS type = no exclusive set so infinity+withered combine),
  data/jeg/tags/item/enchantable/gun.json (item tag listing all gun items; generated from ModItems - ADD NEW GUNS HERE; replaces EnchantmentTypes.GUN),
  data/minecraft/tags/enchantment/{in_enchanting_table,non_treasure,treasure,curse,tradeable,on_random_loot}.json (rarity/treasure/curse/discoverable flags).
  Lang keys enchantment.jeg.<name> already exist. Rarity -> weight (common10/uncommon5/rare2/very_rare1) + anvil_cost (1/2/4/8).
  Deviation: trigger_finger used SEMI_AUTO_GUN category (non-automatic fire mode); now supports all guns (fire mode is runtime JSON data, not tag-able). GunEnchantmentHelper could ignore it for automatic guns if desired.
done: particles/{BulletHoleData,LaserData,TrailData,ColoredFlareData}: Deserializer/writeToNetwork/writeToString removed; now expose `MapCodec CODEC` + `StreamCodec<RegistryFriendlyByteBuf,X> STREAM_CODEC`.
  OUTSIDE SLICE (init/ModParticleTypes): the 4 custom types still use `new ParticleType<>(bool, X.DESERIALIZER)` + `Codec codec()`; replace with the port's helper
  `createOptions(X.CODEC, overrideLimiter, X.STREAM_CODEC)` (anonymous ParticleType overriding `MapCodec<T> codec()` and `StreamCodec<? super RegistryFriendlyByteBuf,T> streamCodec()`). Flags: BULLET_HOLE false, LASER false, TRAIL false, COLORED_FLARE_SMOKE true.
done: effect/{Incurable,KillEffect,Smoked}Effect (applyEffectTick returns boolean, shouldApplyEffectTickThisTick, hasEffect(ModEffects.X) with holder, incurable via fillEffectCures PORT-VERIFY, NOTE_BLOCK_CHIME.value()).
done: modifier/* - NOTE this package is the gun "modifier" JSON groups (not loot modifiers; slice text was wrong). Only change: PotionEffectModifier now holds Holder<MobEffect>; ModifierDeserializer uses ResourceLocation.parse + getHolder. ModifierLoader unchanged (SimplePreparableReloadListener API same in 1.21.1). No GLM serializers in this package.
done: event/{ModCommonEventBus,ModClientEventsBus,ServerTickHandler,BeeReleaseEvent,PlayerCloneHandler,EntityHurtEvent,CameraEvent}
  - BeeReleaseEvent now implements ICancellableEvent. OUTSIDE SLICE: blockentity/BeehiveBlockEntityAbstract.java:269 `MinecraftForge.EVENT_BUS.post(new BeeReleaseEvent(..))` -> `NeoForge.EVENT_BUS.post(new BeeReleaseEvent(..)).isCanceled()`.
  - ServerTickHandler: ServerTickEvent.Pre, static, NO @EventBusSubscriber (main class registers the class). ModCommonEventBus: Bus.MOD, RegisterSpawnPlacementsEvent (note: original heightmap/placement kept).
  - EntityHurtEvent: LivingHurtEvent -> LivingIncomingDamageEvent (still cancellable). CameraEvent PORT-VERIFY Camera#setAnglesInternal.
done: event/{FirstJoinMessageHandler,EntitySpawnEventHandler,MedalEventHandler,ModCommandsRegister,JuniorRiderRenderHandler} (Bus.FORGE dropped = default game bus; FinalizeSpawnEvent; StackData in Medal; removed unused partialTick in JuniorRider). MedalEventHandler needs AT `Creeper swell`.
done: event/{GunFireEvent,GunReloadEvent,GunProjectileHitEvent,KillEffectEvent,KillEffectHandler,BurstFireEvent}: Pre events + GunProjectileHitEvent implement ICancellableEvent (Pre/Post nested stay PlayerEvent subclasses).
  - GunFireEvent.Post: StackData.edit for IsShooting/resetTags; Death Angels (aQuietPlace) branch PORT-DROPPED; Delay now via DynamicLightBlockEntity.getDelay/setDelay (already present). GeckoLib imports use geckolib.animatable.GeoAnimatable / geckolib.animation.AnimationController (as AnimatedGunItem does; PORT-VERIFY).
  - OUTSIDE SLICE: client/handler/ShootingHandler.java:404/433 and ReloadHandler.java:143/148 still use MinecraftForge.EVENT_BUS.post(...) as boolean -> NeoForge.EVENT_BUS.post(...).isCanceled().
done: event/ConfigPackLoader (AddPackFindersEvent neoforge; Pack API of 1.21.1 PORT-VERIFY: PackLocationInfo/PackSelectionConfig (net.minecraft.server.packs.repository.PackSelectionConfig? - could be Pack.SelectionConfig; verify); pack.mcmeta format 15->34)
done: event/EntityKillEventHandler (enchant levels via GunEnchantmentHelper/ModEnchantments.holder; EnchantmentHelper.updateEnchantments for Withered; original quirk kept: Reclaimed level is read from the VICTIM's main hand. serverLevel.playSound(entity,...) -> playSound(null,...) since an Entity is not a Player)
done: event/FlashlightEvent (PlayerTickEvent.Post replaces both-phase TickEvent.PlayerTickEvent; StackData; DynamicLightBlockEntity setDelay/getDelay replace getPersistentData)
done: event/GunEventBus (PlayerTickEvent.Post, StackData, GunEnchantmentHelper for Mending/Sharpness/Atlantic, hurtAndBreak(int,LivingEntity,EquipmentSlot) for the gun, attachments via hurtAttachment() = ServerLevel overload PORT-VERIFY; SwordItem#getDamage -> tier bonus + 3 PORT-VERIFY). Original quirk kept: Gun.getAttachment returns a copy, so attachment durability damage is not persisted (Stage 2 removes attachment durability anyway).
ALL of J/event/* done. check_port BANNED hits left in event/ are false positives on the new PlayerTickEvent.Post/ServerTickEvent.Pre names.
done: mixin/** (MixinPlugin class check -> FrameworkNeoForge; common/{AbstractSkeleton,Cookie,Entity,Lead,LivingEntity,Player,SittingPhase} unchanged - targets verified from memory only (PORT-VERIFY: AbstractDragonSittingPhase#onHurt, Player#isScoping);
  EndPortalBlockMixin retargeted to getPortalDestination(ServerLevel,Entity,BlockPos) HEAD (PORT-VERIFY, require=0) + StackData;
  client/GameRendererMixin (DeltaTracker signature, PORT-VERIFY, require=0), GuiMixin (Reference.id), ItemInHandLayerMixin + PlayerModelMixin (getTimer().getGameTimeDeltaPartialTick(false)),
  MinecraftMixin (MixinExtras @Local ItemStack instead of CAPTURE_FAILHARD; require=0), MouseHandlerMixin (two @ModifyArg on LocalPlayer#turn(DD)V instead of @ModifyVariable ordinal 2; require=0).
  client/LevelRendererMixin DELETED (removed from jeg.mixins.json) and replaced by NEW event/BulletTrailRenderEvent (RenderLevelStageEvent AFTER_ENTITIES -> BulletTrailRenderingHandler.get().render(poseStack, partial)). Client slice: do not add a second caller.
  HumanoidModelMixin unchanged. All "require = 0" injectors silently skip if their target is wrong: check the log for "Mixin ... failed" while testing.
done: compat/ShoulderSurfingHelper -> inert stub (PORT-DROPPED Shoulder Surfing; callers in client/handler keep compiling). compat/PlayerReviveHelper unchanged (pure reflection, no Forge types).
done: debug/* (Debug: DistExecutor->FMLEnvironment.dist, ServerStartedEvent; EditorScreen: 1.21 vertex API, list ctor/setX, Reference.id; PORT-VERIFY list background overrides; DebugSlider -> ExtendedSlider, custom render dropped). DebugButton/Toggle/Enum/IDebugWidget/IEditorMenu unchanged.
done: animations/{GunAnimations,TerrorPhantomAnimations} (GeckoLib 4.8 packages geckolib.animation.*, geckolib.animatable.GeoAnimatable PORT-VERIFY; StackData).
done: META-INF/accesstransformer.cfg rewritten to Mojang names, trimmed to members used in src/main/java (AbstractSkeleton bowGoal/meleeGoal added to the kept set for TrumpetSkeletonAI; Creeper swell; Mob populateDefaultEquipmentSlots; Bee members for entity/animal/Boo).
  PORT-VERIFY (names from memory): Bee beePollinateGoal/goToHiveGoal/goToKnownFlowerGoal, BeePollinateGoal#isPollinating, BeeGoToHiveGoal#clearBlacklist/isTargetBlacklisted/blacklistTarget, inner-goal ctor descriptors (copied from the old file).
  Dropped as unused: Display setters, Piglin createSpawnWeapon, Phantom goal classes, TemptGoal items (Boo no longer needs it), Bee misc helpers, ServerLevel m_184097_, Entity xRot, BeehiveBlockEntity flowerPos.

## (a) API changes other slices must know
- GunFireEvent.Pre, GunReloadEvent.Pre, KillEffectEvent.Pre, GunProjectileHitEvent, BeeReleaseEvent are ICancellableEvent. Post with `NeoForge.EVENT_BUS.post(evt).isCanceled()`.
- particles/*Data: `MapCodec CODEC` + `StreamCodec STREAM_CODEC` replace Codec/DESERIALIZER (see done list).
- PotionEffectModifier#getEffect now Holder<MobEffect>. ShoulderSurfingHelper is an inert stub. enchantment package deleted (no EnchantmentTypes/GunEnchantment classes).
- NEW event/BulletTrailRenderEvent replaces LevelRendererMixin.
- New guns must be added to data/jeg/tags/item/enchantable/gun.json.

## (c) Outside-slice items noticed
- init/ModParticleTypes: use createOptions(X.CODEC, flag, X.STREAM_CODEC) for BULLET_HOLE/LASER/TRAIL/COLORED_FLARE_SMOKE (still old Deserializer API).
- blockentity/BeehiveBlockEntityAbstract:269 BeeReleaseEvent post (see above). client/handler/ShootingHandler:404/433, ReloadHandler:143/148 still MinecraftForge.EVENT_BUS.post(...) boolean.
- world/loot/* (global loot modifiers) are in the blocks-world slice, not here; data/forge/loot_modifiers/global_loot_modifiers.json must move to data/neoforge/loot_modifiers/ (resources slice). Fire starter / Atlantic shooter modifiers there must build enchanted stacks with holders.
- world/ProjectileExplosion:118 entity.ignoreExplosion() needs an Explosion arg (already noted by projectile-ai).
- DynamicLightBlockEntity already has getDelay/setDelay (used here); AIGunEvent/FlameProjectileEntity could use it too instead of the save/load round-trip.
- data/forge/tags (resources slice) -> data/c/tags or neoforge; not touched.

## (d) Not finished / weak spots
- Nothing skipped, but unverified (no compiler): all mixin targets (require=0 on the risky ones), AT names marked PORT-VERIFY, ConfigPackLoader Pack API, ExtendedSlider, EditorScreen list API.
- trigger_finger can now be applied to automatic guns (EnchantmentTypes.SEMI_AUTO_GUN restriction not expressible in a tag).
- enchantment `exclusive_set` semantics approximate the old GunEnchantment.Type rule (same type = incompatible, BOSS types compatible).
