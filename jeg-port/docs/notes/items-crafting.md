# items-crafting notes
## Progress log (append "done: file")
- (attempt 2 start) crafting/workbench + crafting/recycling were already modified by a previous attempt (no notes existed); being reviewed.
- reviewed OK (prev attempt): crafting/workbench/* (value-class WorkbenchIngredient + nested ICustomIngredient `Custom`), crafting/recycling/*
- done: crafting/DyeItemRecipe.java (CustomRecipe(CraftingBookCategory), CraftingInput, getRemainingItems via stack.getCraftingRemainingItem())
- done: common/container/{slot/AttachmentSlot (binding curse via holder lookup), AttachmentContainer, SchematicStationMenu, recycler/AbstractRecyclerMenu, recycler/RecyclerResultSlot}; reviewed OK unchanged: AbstractWorkbenchContainer, *WorkbenchContainer, AmmoBoxMenu, RecyclerMenu, RecyclerFuelSlot, BasicTurretContainer (commented out)
- (attempt 3) done: item/GunItem.java (StackData, data-driven GunId, CustomData-keyed gun cache, canApplyAtEnchantingTable PORT-DROPPED, initializeClient moved), item/AnimatedGunItem.java (GeoItem + createGeoRenderer, tag logic on StackData copy), NEW item/ItemClientExtensions.java (RegisterClientExtensionsEvent for non-animated GunItem BEWLR)
- done: item/IColored, AttachmentItem, attachment/impl/Attachment, attachment/impl/Scope (PORT-VERIFY DyeColor#getTextureDiffuseColor in IColored)
- done: item/{AdvancedBlueprintItem,BlueprintItem,FlareItem,FlashlightItem} (StackData; Flashlight tag logic on copy + write back)
- done: item/{LootDropItem (reloadableRegistries loot lookup), ScoreStreakItem, GunnerSpawnEggItem (DeferredSpawnEggItem; PORT-VERIFY getType(ItemStack)), SoultreatItem (null guard added), PocketBubbleItem (ok)}
- done: item/{WaterBombItem (getUseDuration(stack,entity)), TrumpetItem (hurtAndBreak slot), TerrorHornItem, ToolTipItem, ExplosiveChargeItem, FallbackItem} reviewed
- done: item/{GrenadeItem, MolotovCocktailItem, TelescopicScopeItem (getUseDuration 2-arg), AnimatedBowItem + GunItem (supportsEnchantment replaces canApplyAtEnchantingTable; PORT-VERIFY), Make*/AirStrike/PhantomBait/IAmmo reviewed OK}
- done: item/attachment/item/{Barrel,Magazine,Scope,Special,Stock,UnderBarrel}Item (supportsEnchantment binding curse), KillEffectItem, PaintJobCanItem, Pseudo* reviewed OK
- done (attempt 3): crafting/workbench/WorkbenchIngredient: Custom ICustomIngredient rewritten (own MapCodec item|tag + count; old version would recurse through Ingredient.CODEC_NONEMPTY on the 'type' key); IngredientType now self-registered via nested Registration (RegisterEvent) - NO main-class call needed

## Final summary (attempt 3)
### (a) API changes other slices must know
- AnimatedGunItem/GunItem: `initializeClient` removed (NeoForge). Non-animated GunItem BEWLR is registered in NEW `item/ItemClientExtensions` (RegisterClientExtensionsEvent, mod bus, client) -> client slice must NOT register extensions for GunItem again. AnimatedGunItem uses `GeoItem#createGeoRenderer` -> `new AnimatedGunRenderer(Reference.id(gunID))`.
- `AnimatedGunItem` no longer lists `GeoAnimatable` explicitly (GeoItem already extends it). `resetTags(ItemStack)` now writes via StackData; `resetTags(CompoundTag)` unchanged/public.
- `getUseDuration(ItemStack, LivingEntity)`, `onEntitySwing(ItemStack, LivingEntity, InteractionHand)`; GunItem.canApplyAtEnchantingTable removed: `supportsEnchantment(ItemStack, Holder<Enchantment>)` (Trigger Finger refused on AUTOMATIC guns; attachments accept Binding Curse; AnimatedBowItem allows Flame, refuses OverCapacity/TriggerFinger/FireStarter). `EnchantmentTypes` is no longer referenced by item/ (ClientHandler still uses `enchantment.category` -> client slice must fix).
- GunItem modifiedGun cache is keyed by the CUSTOM_DATA component object (WeakHashMap<CustomData, Gun>). Gun enchantment levels read via `GunEnchantmentHelper.getEnchantmentLevel(stack, ModEnchantments.X)` (ResourceKey).
- IColored keeps the "Color" int in CUSTOM_DATA (not DyedItemColor); the client item-colour handler must read `IColored#getColor`.
- Menus: unchanged ctors (see earlier entries); AttachmentContainer client ctor takes no buffer.
- WorkbenchIngredient: value class (`getIngredient()`, `getCount()`, `test`, `getItems`, CODEC, STREAM_CODEC) + nested `Custom` ICustomIngredient, type "jeg:workbench_ingredient", SELF-REGISTERED by nested `WorkbenchIngredient.Registration` (RegisterEvent). init-net: do NOT also call INGREDIENT_TYPES.register(...) (field removed).
- AbstractWorkbenchRecipe is `Recipe<AbstractWorkbenchRecipe.NoInput>`; `getId()` is filled in only for recipes fetched through `WorkbenchRecipes.getAll/getRecipeById`. Recycling recipes are `Recipe<SingleRecipeInput>`; get id from the RecipeHolder.

### Accepted JSON of the custom recipe types (UNCHANGED schema)
- `jeg:scrap_workbench`, `jeg:gunmetal_workbench`, `jeg:gunnite_workbench`, `jeg:blueprint_workbench`:
  `{"type":..., "materials":[{"item":"ns:id"|"tag":"ns:tag","count":n(optional,1)}...], "result":{"item":"ns:id","count":n(optional)} (also vanilla {"id":..} accepted), "time":30 (ignored)}`
- `jeg:recycling`: `{"type":"jeg:recycling","group":""?, "ingredient":{"item"|"tag"} (or array), "result":{"item","count"?} | "ns:id", "experience":0?, "recyclingtime":50? (default from serializer ctor)}`
- `jeg:dye_item`: special CraftingRecipe, `{"type":"jeg:dye_item"}` (+ optional "category").
- `jeg:workbench_ingredient` (new-style ingredient, usable anywhere an ingredient is): `{"type":"jeg:workbench_ingredient","item"|"tag":...,"count":n}`.

### (b) PORT-VERIFY / PORT-DROPPED (file:line from final grep)
GunItem 209 (PORT-DROPPED Devil Fruits tooltip), 273 onEntitySwing 3-arg, 359 supportsEnchantment; attachment/item/{Barrel,Magazine,Stock,UnderBarrel,Scope,Special}Item supportsEnchantment; AnimatedBowItem 17; AnimatedGunItem 16/20 (GeoAnimatable + ParticleKeyframeEvent packages; also ClientUtils import unverified); IColored 114 DyeColor#getTextureDiffuseColor; WorkbenchIngredient 44 (INGREDIENT_TYPES key, IngredientType ctor), 64 (Ingredient.CODEC_NONEMPTY tolerates extra "count" key), 84 (Ingredient.CONTENTS_STREAM_CODEC); GunnerSpawnEggItem getType(ItemStack) on DeferredSpawnEggItem; AmmoItem etc: nothing.

### (c) Outside my slice
- JSON `data/jeg/enchantment/trigger_finger.json` uses `#jeg:enchantable/gun`; semi-auto restriction is now in GunItem.supportsEnchantment (no JSON change needed). Flame for bows needs `minecraft:flame` supported_items to include the bow via tag if the enchanting table check goes through vanilla tags.
- Loot tables for LootDropItem: `data/jeg/loot_table/loot_drop_items/{ammo_pouch,badge_pack,skin_crate,<item path>}.json` (singular dir; resources slice).
- ClientHandler.java:333 still uses `enchantment.category`/EnchantmentTypes (removed in 1.21).
- GunAnimations.java (animations pkg) still uses getOrCreateTag.
- util/client RenderUtil still imports net.minecraftforge IClientItemExtensions.

### (d) Not finished / not verified
- Nothing compiled. All item/crafting/container files have been ported; common/container classes were only skimmed this attempt (reviewed by attempt 2). GunItem `Debug.getGun`, `NetworkGunManager` signatures assumed unchanged.
