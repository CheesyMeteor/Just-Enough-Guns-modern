# blocks-world notes
(progress log; appended as files finish)
done: blockentity/DynamicLightBlockEntity (loadAdditional/saveAdditional/getUpdateTag w/ Provider, Delay saved; ADDED getDelay()/setDelay(double)), block/DynamicLightBlock
done: block/ScrapWorkbenchBlock, GunmetalWorkbenchBlock, GunniteWorkbenchBlock, BlueprintWorkbenchBlock, BrightDynamicLightBlock (codec, useWithoutItem + openMenu(provider, buf->writeBlockPos)); RotatedObjectBlock/SchematicStation pending
done: SchematicStationBlock; unchanged-OK: RotatedObjectBlock, BasicTurretBlock+BasicTurretBlockEntity (entirely commented out in original)
done: AbstractRecyclerBlock, RecyclerBlock
done: AmmoBoxBlock (saveToItem(stack, registryAccess); tooltip reads DataComponents.BLOCK_ENTITY_DATA; PORT-VERIFY getCloneItemStack(LevelReader,...); getDrops kept as protected-widened override), AmmoBoxBlockEntity (loadAdditional/saveAdditional; PORT-DROPPED createUnSidedHandler capability: if item-handler cap is needed, register Capabilities.ItemHandler.BLOCK in main-class RegisterCapabilitiesEvent with SidedInvWrapper)
done: ScrapBinBlock (useItemOn/useWithoutItem split; PORT-VERIFY isPathfindable(BlockState,PathComputationType)), BooNest (split interaction; PORT-VERIFY empty-hand check uses main hand)
done: blockentity/BeehiveBlockEntityAbstract. Forge capability (LazyOptional/CapabilityBee/INBTSerializable) removed: uses a plain IInhabitantStorage field (only interface methods: getInhabitants/setInhabitants/addInhabitant/getInhabitantListAsListNBT/setInhabitantsFromListNBT). NBT format kept ("BeeList" -> {"Inhabitants": [...]}).
  AT members relied on (Mojang names, must be public): BeehiveBlockEntity.savedFlowerPos (field, read+write); Bee.hivePos (field, write). Public API used: Bee#getSavedFlowerPos/hasSavedFlowerPos/setSavedFlowerPos/setStayOutOfHiveCountdown/resetTicksWithoutNectarSinceExitingHive/dropOffNectar/hasNectar, BeehiveBlockEntity.getHoneyLevel(BlockState) static, isSedated().
  PORT-VERIFY: addOccupant(Entity) override (1.21 dropped the boolean param); Entity#save(CompoundTag); getHoneyLevel static.
  API change: addOccupant(Entity,boolean) is now addOccupant(Entity); addOccupantWithPresetTicks(Entity,boolean,int) kept (no @Override).
  OUTSIDE SLICE: util/bee/InhabitantStorage (Forge INBTSerializable, NbtUtils.writeBlockPos/readBlockPos 1.21 signatures), util/bee/CapabilityBee (Forge Capability - delete, unused now), IInhabitantStorage ok.
done: blockentity/SyncedBlockEntity (onDataPacket override dropped, default handles it), AbstractWorkbenchBlockEntity; unchanged-OK: IStorageBlock, Gunnite/Gunmetal/Scrap/Blueprint WorkbenchBlockEntity, BooNestBlockEntity (reviewed below)
done: blockentity/AbstractRecyclerBlockEntity (Recipe<SingleRecipeInput> + RecipeHolder; no longer implements RecipeHolder/RecipeCraftingHolder interface; setRecipeUsed(RecipeHolder<?>), getRecipesToAwardAndPopExperience now List<RecipeHolder<?>> (API change); burn time via ItemStack#getBurnTime(RecipeType); loadAdditional/saveAdditional). PORT-VERIFY: getItems/setItems declared w/o @Override; ItemStack#getBurnTime(RecipeType) NeoForge ext; hasCraftingRemainingItem()/getCraftingRemainingItem() on ItemStack.
  PORT-DROPPED: Forge item-handler capabilities on Recycler/AmmoBox. If wanted for non-hopper automation, main class/ModCommonEventBus must add RegisterCapabilitiesEvent: event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModTileEntities.RECYCLER.get()/AMMO_BOX.get(), (be, side) -> new SidedInvWrapper(be, side)).
unchanged-OK: RecyclerBlockEntity
done: world/ProjectileExplosion (ignoreExplosion(this) per projectile-ai note; 13-arg Explosion ctor; EventHooks.onExplosionDetonate; knockback via Attributes.EXPLOSION_KNOCKBACK_RESISTANCE) PORT-VERIFY on ctor + onExplosionDetonate; AmmoBoxSlot, ModOrePlacement unchanged-OK
done: world/loot/* (25 files): MapCodec via RecordCodecBuilder.mapCodec + BuiltInRegistries.ITEM.byNameCodec(), codec() returns MapCodec; NeoForge LootModifier/IGlobalLootModifier (net.neoforged.neoforge.common.loot); ItemStack NBT via StackData.putString; FireStarter/AtlanticShooter use data-driven enchantment holders (ModEnchantments.holder / registry lookup, EnchantedBookItem.createForEnchantment). ModLootModifiers: DeferredRegister on NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS - REQUIRES ModLootModifiers.register(modBus) in mod ctor (JustEnoughGuns already calls it with `bus`; make sure it is the MOD bus). PORT-VERIFY: LootModifier.codecStart in NeoForge 21.1 is usable with RecordCodecBuilder.mapCodec.
  Loot modifier JSON (resources slice): conditions use "neoforge:loot_table_id" (not forge:), file path data/neoforge/loot_modifiers/global_loot_modifiers.json lists them; item field is a plain item id string.
done: world/gen/{ModConfiguredFeatures,ModPlacedFeatures} (BootstrapContext rename, Reference.id; only useful if bootstrapped from a datagen/registry-set builder - datagen dropped, so JSON in resources slice is the source of truth), ModOrePlacement unchanged-OK.

## Summary
(a) API changes others must know
- DynamicLightBlockEntity: NEW getDelay()/setDelay(double). OUTSIDE SLICE callers must stop using BlockEntity#getPersistentData("Delay"): event/FlashlightEvent.java ~197/237, event/GunFireEvent.java ~125 (and any remaining in AIGunEvent/FlameProjectileEntity round-trip) -> `if (be instanceof DynamicLightBlockEntity l) l.setDelay(x)`.
- BeehiveBlockEntityAbstract: addOccupant(Entity,boolean) -> addOccupant(Entity).
- AbstractRecyclerBlockEntity: setRecipeUsed(RecipeHolder<?>), getRecipeUsed() -> RecipeHolder<?>, getRecipesToAwardAndPopExperience(...) -> List<RecipeHolder<?>>; no longer implements RecipeHolder.
- AmmoBoxBlockEntity.loadFromTag(CompoundTag, HolderLookup.Provider).
- Menu-opening workbench blocks write only buf.writeBlockPos(pos) (matches init/ModContainers factories).
- Block classes now expose `public static final MapCodec CODEC` (BooNest's is typed MapCodec<BeehiveBlock>).
(c) outside slice
- util/bee/InhabitantStorage (Forge INBTSerializable, NbtUtils.writeBlockPos/readBlockPos 1.21 signatures), util/bee/CapabilityBee (Forge) - delete/port in common-util.
- AT (events-misc/resources): BeehiveBlockEntity.savedFlowerPos and Bee.hivePos must be public (Mojang names); Bee/BeehiveBlockEntity other members used are already public API.
- Loot modifier JSON + global_loot_modifiers.json must use the neoforge: condition namespace; register modifiers' MapCodecs via ModLootModifiers (already wired in JustEnoughGuns).
- Item-handler capabilities for Recycler/AmmoBox dropped (see above); add RegisterCapabilitiesEvent if pipes/other mods are wanted.
- ModBlocks: unchanged; all block ctors keep their original signatures (DynamicLight/BrightDynamicLight no-arg, others Properties).
(b) PORT-VERIFY list (grep "PORT-VERIFY" in block/ blockentity/ world/): AmmoBoxBlock getCloneItemStack(LevelReader), ScrapBinBlock isPathfindable(BlockState,PathComputationType), BooNest empty-hand check, BeehiveBlockEntityAbstract addOccupant/Entity#save/getHoneyLevel, AbstractRecyclerBlockEntity getItems/setItems, ProjectileExplosion 13-arg ctor + EventHooks.onExplosionDetonate.
(d) not finished: nothing in the slice left unported. NOT touched on purpose: BasicTurretBlock/BasicTurretBlockEntity (whole file is commented out in the original). No structures/POI/BiomeModifier classes exist in J/world in this tree. Nothing compiled (no compiler available).
