# Slice: blocks-world (notes file: docs/notes/blocks-world.md)
J = src/main/java/ttv/migami/jeg. You own exactly: J/block/**, J/blockentity/**, J/world/**.
- Blocks: `codec()` returning MapCodec<? extends Block> required; `use` split into `useWithoutItem` / `useItemOn` (ItemInteractionResult). Menu-opening blocks: player.openMenu(MenuProvider, buf -> ...) / Framework openMenuWithData.
- Block entities: loadAdditional/saveAdditional(CompoundTag, HolderLookup.Provider), getUpdateTag(Provider); items saved with ItemStack.save(provider)/parse or ContainerHelper.saveAllItems(tag, items, provider). GeckoLib block entities: port patterns.
- BeehiveBlockEntityAbstract uses Bee internals through the AT (events-misc rewrites the AT to Mojang names): list exactly which members you rely on in notes.
- World: structures (StructureType/StructurePiece use MapCodec; StructureTemplate APIs changed), features (Feature<Config>, FeaturePlaceContext), POI types (ModPointOfInterestTypes in init-net), custom BiomeModifier classes -> MapCodec + NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS
  (own DeferredRegister; document registration need). Forge hooks/ModList/Capability -> NeoForge equivalents.
