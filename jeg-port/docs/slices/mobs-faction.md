# Slice: mobs-faction (notes file: docs/notes/mobs-faction.md)
J = src/main/java/ttv/migami/jeg. You own exactly: J/entity/monster/**, J/entity/animal/**, J/entity/client/**, J/faction/**. NOT yours: entity/projectile|throwable|ai|top-level (projectile-ai), client/**.
- Mobs: createAttributes() + registration (event owned elsewhere; document method names), `finalizeSpawn(ServerLevelAccessor, DifficultyInstance, MobSpawnType, SpawnGroupData)` (no CompoundTag param in 1.21.1),
  populateDefaultEquipmentSlots(RandomSource, DifficultyInstance) (AT-exposed originally; events-misc rewrites the AT - list members you need), defineSynchedData(Builder), ServerBossEvent, SpawnPlacements via SpawnPlacementRegisterEvent, loot via ReloadableServerRegistries.
- entity/client renderers/models: GeckoLib 4.8.3 patterns from the port; keep class names + ctor (EntityRendererProvider.Context).
- Faction: JSON loading (data/jeg/factions) via reload listener; patrol/raid managers via LevelTickEvent.Post/ServerTickEvent; SavedData uses SavedData.Factory + load/save(tag, HolderLookup.Provider) + DimensionDataStorage.computeIfAbsent(Factory, name).
- Other-mod mobs referenced for compat (Guard Villagers, Recruits, Villager Gunners...) -> PORT-DROPPED.
