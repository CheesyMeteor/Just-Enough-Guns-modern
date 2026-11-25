# Slice: items-crafting (notes file: docs/notes/items-crafting.md)
J = src/main/java/ttv/migami/jeg. You own exactly: J/item/** (60 files incl. item/attachment/**), J/crafting/** (DyeItemRecipe, handler, recycling, workbench recipes), J/common/container/** (workbench menus, AmmoBoxMenu, AttachmentContainer, SchematicStationMenu, recycler, slot).
- The user specifically prefers the ORIGINAL crafting process (workbench GUIs + custom workbench recipe types + recycler). Preserve behaviour exactly.
- Custom recipe types: KEEP THE JSON SCHEMA UNCHANGED (~200 recipe JSONs in data/jeg/recipes; the resources agent only touches vanilla-mandated parts). Implement 1.21 RecipeSerializer (MapCodec + StreamCodec) so it parses the SAME JSON
  the old fromJson did (including old keys like "item" for results if used). Document every custom recipe type's accepted JSON in notes. Use `Recipe<RecipeInput>` with a custom RecipeInput if the workbench container needs one (check the port).
- GunItem/AnimatedGunItem etc: item NBT -> StackData (most of the 50 getTag users). GeckoLib: copy proven 4.8.3 patterns from /home/claude/jeg-ref/port (GeoItem + renderer provider via initializeClient/RegisterClientExtensionsEvent).
- appendHoverText new signature; Item.Properties changes; attribute modifiers via ItemAttributeModifiers; food/use-duration APIs changed.
- Menus: ctor (int, Inventory, ...) with MenuType from ModContainers (init-net keeps names); keep a (id, inv, RegistryFriendlyByteBuf) factory pattern and document for init-net.
- GunnerSpawnEggItem: DeferredSpawnEggItem or SpawnEggItem subclass per the port. Drop compat refs (PORT-DROPPED).
