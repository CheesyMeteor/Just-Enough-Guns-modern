# mobs-faction notes
(progress log; append "done:" lines)
done: faction/GunnerMobSpawner (static handlers, @EventBusSubscriber game bus, FinalizeSpawnEvent, EntityTickEvent.Pre replaces LivingTickEvent; follow-range modifier REMOVED per instruction), GunnerManager, jeg/FactionData (BuiltInRegistries), raid/GunnerRaidData (SavedData.Factory)
unchanged-OK: faction/Faction, GunMobValues, jeg/FactionDataManager, patrol/GunnerPatrolSpawner, raid/GunnerRaidSpawner (CustomSpawner iface same in 1.21.1; driven by event/ServerTickHandler)
done: faction/raid/RaidEntity (loot via reloadableRegistries, synched data builder, onRemovedFromLevel PORT-VERIFY, FireworkRocketEntity(Level,ItemStack,x,y,z,bool) ctor PORT-VERIFY, GOAT_HORN .value())
done: faction/raid/TerrorRaidEntity (same changes as RaidEntity)
done: entity/animal/Boo (BlockPathTypes->PathType; doEnchantDamageEffects->EnchantmentHelper.doPostAttackEffects PORT-VERIFY; EntityDimensions.height(); TemptGoal passes tag predicate to super instead of assigning AT'd `items` field -> AT line `public-f TemptGoal items` no longer needed)
   AT NEEDED for Boo (mojmap, resources slice must rewrite the old SRG-named AT): Bee fields hivePos, beePollinateGoal, goToHiveGoal, goToKnownFlowerGoal, remainingCooldownBeforeLocatingNewHive, stayOutOfHiveCountdown;
   Bee methods setHasStung(Z), wantsToEnterHive(), isTiredOfLookingForNectar(), isHiveNearFire(), doesHiveHaveSpace(BlockPos), closerThan(BlockPos,int), spawnFluidParticle(Level,DDDDD,ParticleOptions) (private; Boo overrides it -> make public non-final),
   hasSavedFlowerPos(), setHasNectar(Z) (check visibility); inner classes + ctors: BeeBecomeAngryTargetGoal, BeeHurtByOtherGoal, BeeGrowCropGoal, BeeLocateHiveGoal, BeeGoToKnownFlowerGoal, BeeEnterHiveGoal, BeePollinateGoal, BeeGoToHiveGoal (+ BeePollinateGoal#isPollinating, BeeGoToHiveGoal#blacklistedTargets/clearBlacklist/isTargetBlacklisted if not public).
   Boo.checkAnimalSpawnRules / Ghoul.checkMonsterSpawnRules are static placement predicates (used by ModCommonEventBus RegisterSpawnPlacementsEvent).
done: entity/monster/Ghoul (removed redundant getClassification override that only called super)
done: entity/monster/phantom/PhantomSwarmData (SavedData.Factory)
done: entity/monster/phantom/gunner/{PhantomGunner,PhantomGunnerAnimations,PhantomGunnerFlySoundInstance}, terror/TerrorPhantomAnimations (GeckoLib 4.8.3 package moves; defineSynchedData builder; removed unused Forge OnlyIn import). Dive/other SoundInstance classes unchanged-OK
done: entity/monster/phantom/terror/TerrorPhantom
  - DEVIATION (per "no block placing"): original placed 2 BARRELS with loot tables entities/phantom/terror/terror_phantom_{supply,reward} on death (setBlock). Now the same tables are rolled and dropped as ItemEntity at ground pos. Public method spawnLootBarrels(...) signature kept. To restore original: re-add placeBarrelWithLoot (world.setBlock(BARREL...) + BarrelBlockEntity#setLootTable(ResourceKey<LootTable>, long)).
  - PORT-DROPPED: `implements RiderShieldingMount` (interface exists nowhere in original sources; getRiderShieldingHeight() kept as plain method).
  - PORT-VERIFY: getPassengerAttachmentPoint(Entity,EntityDimensions,float) override replaces removed getPassengersRidingOffset() (+0.2).
  - SoundEvents.GENERIC_EXPLODE.value() (Holder in 1.21.1 per port usage). serverLevel.playSound(Entity,...) -> playSound(null,...).
  - Loot table ids are Reference.id("entities/phantom/terror/...") => resources slice: data/jeg/loot_table/entities/phantom/terror/*.json.
done: entity/client/{BubbleRenderer(1.21 vertex API),ModModelLayers,SplashModel(int color),SplashRenderer,PhantomGunnerModel,TerrorPhantomModel (GeoBone/animation pkg; unused EntityModelData dropped),PhantomGunnerRenderer,TerrorPhantomRenderer (renderFinal int colour PORT-VERIFY)}

# ===== FINAL SUMMARY (mobs-faction) =====

## What the ORIGINAL raid / faction / spawn system is (needed by docs/STAGE2.md, `gunnerSpawnsOutsideRaids`)
There is NO JSON faction loader and NO FactionEventTicker in the original. Factions come from the COMMON CONFIG list `Config.COMMON.gunnerMobs.factions`
(string `name|aiLevel|mob1,mob2|closeGuns|longGuns|eliteGuns`; defaults: night_of_the_undead, the_rattlers, nosy_business, bad_piggies, hell_hogs, lost_souls),
parsed by `faction/GunnerManager` into `Faction`. (`data/jeg/factions/night_of_the_undead.json` exists but nothing reads it. `faction/jeg/FactionData(+Manager)` is a hard-coded
3-wave "terror_armada" definition used only by TerrorRaidEntity.) Gun mobs are always VANILLA mobs (zombie, skeleton, piglin, pillager ... + jeg:ghoul) that get tag "MobGunner" + a GunAttackGoal + a gun in the main hand.
Gunner mobs get their gun in these ways:
 1. NATURAL / BIOME SPAWNS (any spawn source: biome, spawner, egg...): `GunnerMobSpawner.onSpecialSpawn` (FinalizeSpawnEvent) - if `gunnerMobSpawning` on, entity is a PathfinderMob in entity-type tag `jeg:gunner`,
    day >= `minimunDays` (default 4) and roll `rand(100) < min(initialChance + (day-minDays)*chanceIncrement, maxChance)` (1% +1%/day, max 50%) and the mob is in some faction's mob list -> adds tag "MobGunner".
    Then `onLivingUpdate` (EntityTickEvent.Pre, tickCount<2) equips: random close/long gun of the mob's faction, elite roll (`eliteChance` 0.3: elite gun, turtle helmet, resistance III, slowness II, tag EliteGunner),
    and adds `GunAttackGoal`. -> THIS is the "biome spawn" path; a STAGE2 toggle `gunnerSpawnsOutsideRaids=false` should short-circuit `onSpecialSpawn` (tag only given when the spawn is from the mod's own raid/command/egg code).
    NOTE: tag data `jeg:gunner` (and undead/heavy/...) was DATAGEN output (src/generated/resources/data/jeg/tags/entity_types/*.json in orig; dir name must become `entity_type` in 1.21) - resources slice must ship it or this path never fires.
    Modded-in mobs: Ghoul (zombie, biome_modifier mob_ghoul.json: soul_sand_valley/dark_forest/birch/taiga..., weight 25, 2-4) and Boo (bee; mob_boo.json) spawn through BIOME MODIFIERS (+ placement predicates registered in event/ModCommonEventBus); these are not gunners by themselves
    (Ghoul is in `jeg:gunner` and in faction lost_souls).
 2. PATROLS (pillager-patrol-like): `faction/patrol/GunnerPatrolSpawner` (CustomSpawner, ticked every server tick on the overworld by event/ServerTickHandler). Config gunnerMobPatrols, patrolIntervalDays (5; 0=random ticks) + rand(12000), minimumDaysForPatrols 5.
    Picks a random player (not spectator, not near a village), a spot 24-48 blocks away, biome not #without_patrol_spawns, random faction, 2-6 mobs via `ModCommands.spawnPatrol` -> `getFactionMob(forceGuns=false)`; each mob tagged "GunnerPatroller",
    guns are given by the same chance as path 1 (initialChance etc.; `getFactionMob` uses currentDay, not day-minDays) or forced.
 3. FACTION RAIDS (village-raid-like): `faction/raid/GunnerRaidSpawner` (CustomSpawner, same ticker, nextTick persisted in SavedData `GunnerRaidData`). Config gunnerMobRaids, raidIntervalDays (30) + rand(12000), minimumDaysForRaids 15,
    random player, random faction -> `ModCommands.startRaid` -> `RaidEntity.summonRaidEntity` (invisible Entity `jeg:raid_entity`, noSave) + a flare entity dropped from 24 blocks above the player.
    RaidEntity: 3 waves, 64-block active radius, ServerBossEvent progress bar, waves of 20 mobs (max 10 alive, 2 per spawn tick every 60 ticks, 12-24 blocks around the entity) via `getFactionMob(forceGuns=true)`+`ModCommands.spawnRaider`;
    EliteGunners may get horses (`horsemen`), extra creepers/explosive-charge phantoms/silverfish (`explosiveMobs`), support phantoms/witches/evokers (`raidSupportMobs`). Wave reward loot table factions/raids/generic_raid_wave_reward,
    victory: generic_raid_victory_reward + factions/raids/<faction>/victory_reward. Fails (defeat) if no player in radius or Peaceful. Weather is changed (rain/thunder at start, cleared at end).
    Manual starts: `/jeg startRaid` command, flare item (ThrowableFlareEntity / FlareProjectileEntity with raid), TerrorPhantom death.
 4. TERROR ARMADA RAID (boss event): `TerrorRaidEntity` (`jeg:terror_raid_entity`), started ONLY by the terror flare / command `/jeg startTerrorRaid`; 3 waves from FactionDataManager (wave1 zombies+husks, wave2 +skeletons/strays and phantom swarms, wave3 + phantom gunner squads);
    after winning it summons the TerrorPhantom boss (BossBar). When TerrorPhantom dies: `releaseSwarm` sets SavedData PhantomSwarmData.phantomSwarm=true and a short "defeat" TerrorRaidEntity runs.
 5. PHANTOM SWARM (after boss defeat only, config phantomSwarm): `entity/monster/phantom/PhantomSwarmSpawner` (same ticker) every patrolIntervalDays spawns 4-7 phantoms (`ModCommands.spawnPhantomSwarm`);
    `event/EntitySpawnEventHandler` converts 20% of NATURAL phantom spawns to PhantomGunner (if phantomGunnersReplacePhantoms and swarm active); 5% of skeletons become "TrumpetBoi" (trumpetSpawning; entity/ai/trumpet/TrumpetSkeletonAI).
 6. Direct: spawn eggs (item/GunnerSpawnEggItem), `/jeg` commands (ModCommands: spawn gunner mob, spawnPatrol, startRaid, startTerrorRaid, swarm) - all tag "MobGunner" themselves; these must keep working when STAGE2 turns off natural spawns.
So "biome spawns vs raids" = path 1 (+Ghoul/Boo biome modifiers) vs paths 2-4 (the mod's own events). STAGE2 `gunnerSpawnsOutsideRaids=false` must gate path 1 (GunnerMobSpawner.onSpecialSpawn) and probably path 2 (patrols), keep path 3/4/6.

## (a) API changes other slices must know
- GunnerMobSpawner: handlers now static + @EventBusSubscriber(modid) (game bus). `GUN_FOLLOW_RANGE_MODIFIER_UUID`, `extendFollowRange(PathfinderMob)`, `resetFollowRange(PathfinderMob)` REMOVED (no external callers). Public `hasGunAttackGoal`, `reassessWeaponGoal` kept.
- RaidEntity/TerrorRaidEntity/PhantomGunner/TerrorPhantom: `defineSynchedData(SynchedEntityData.Builder)`; `onRemovedFromLevel()`.
- TerrorPhantom: no longer implements RiderShieldingMount; `spawnLootBarrels(...)` now drops items instead of placing barrels (signature unchanged).
- GunnerRaidData / PhantomSwarmData: `load(CompoundTag, HolderLookup.Provider)`, `save(CompoundTag, HolderLookup.Provider)`, accessed through `SavedData.Factory`; `get(ServerLevel)` unchanged.
- ModelLayers/Splash model: `SplashModel#renderToBuffer(..., int color)`.

## (b) PORT-VERIFY / PORT-DROPPED
TerrorPhantom.java:70 (RiderShieldingMount dropped), :157 (getPassengerAttachmentPoint); Boo.java:114 (doPostAttackEffects), :561 (TemptGoal predicate ctor); PhantomGunnerRenderer.java:30 + TerrorPhantomRenderer.java:26 (renderFinal int colour);
RaidEntity.java:606, TerrorRaidEntity.java:541 (onRemovedFromLevel); RaidEntity/TerrorRaidEntity/TerrorPhantom FireworkRocketEntity(Level, ItemStack, x, y, z, boolean) ctor assumed to exist in 1.21.1;
UNSEEN geckolib imports (animatable.GeoAnimatable, animation.PlayState/AnimationController/RawAnimation, cache.object.GeoBone, constant.DefaultAnimations) follow the port's 4.8.3 packages; DefaultAnimations only appears in a javadoc link.
Deviations from the instruction text (flag to user): (1) original GunnerMobSpawner DID have a follow-range modifier (to 64) - removed as instructed; (2) original TerrorPhantom DID place loot barrels - replaced by item drops as instructed (no block placing).
Original code that BREAKS blocks lives elsewhere (not in this slice): GrenadeEntity.createExplosion / config terrorPhantomDestroyBlocks (projectile-ai slice).

## (c) Outside my slice (needs fixing by owners)
- init/ModCommands.java:559 `SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(2).get()` -> `.value()` (Holder.Reference has no get()).
- resources: (1) entity tags `data/jeg/tags/entity_type/{gunner,undead,heavy,very_heavy,ghost,fire,none}.json` from orig src/generated/resources/.../tags/entity_types (datagen output; also blocks->block, items->item dirs);
  (2) loot tables `data/jeg/loot_table/factions/raids/**` and `data/jeg/loot_table/entities/phantom/terror/terror_phantom_{supply,reward}.json` (singular dir, "type":"minecraft:chest" so CHEST param set works);
  (3) biome modifiers mob_ghoul/mob_boo (`neoforge:add_spawns`, `data/jeg/neoforge/biome_modifier`); (4) AT: Bee members listed above (mojmap, rewrite SRG names), AbstractSkeleton bowGoal/meleeGoal, Mob#populateDefaultEquipmentSlots, Phantom inner goal classes are no longer referenced by my files (PhantomGunner/TerrorPhantom define their own).
- event/ModCommonEventBus: BOO attributes use `Bee.createAttributes()`, Bubble.createAttributes() is in projectile-ai's entity/Bubble.
- Entity renderers: init-net ClientInit registers them; `entity/client/*` class names + ctor (EntityRendererProvider.Context) unchanged. SoundHandler (client) still imports PhantomGunner/TerrorPhantom sound instances in this slice - unchanged names.
- RaidEntity/TerrorRaidEntity client tick uses `level().addParticle(ModParticleTypes.ENTITY_LASER.get() ...)` (needs init/ModParticleTypes SimpleParticleType).

## (d) Not finished / not verified
Everything in the slice is ported; nothing compiled. Unreviewed in depth: entity/monster/phantom/terror/*SoundInstance (identical to original, no Forge imports), Boo inner-class behaviour vs 1.21.1 Bee internals (needs AT + a real compile).
Not touched on purpose: STAGE2 (gunnerSpawnsOutsideRaids toggle).
