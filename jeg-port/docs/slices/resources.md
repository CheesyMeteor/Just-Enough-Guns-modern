# Slice: resources (notes file: docs/notes/resources.md) - JSON/assets only, no Java
You own src/main/resources/** and src/generated/resources/** EXCEPT jeg.mixins.json, META-INF/**, pack.mcmeta, data/jeg/enchantment/**, data/jeg/tags/enchantment/** (events-misc), and keep data/jeg/guns/*.json + data/jeg/modifiers/*.json CONTENT unchanged (custom schema parsed by Java; folder names `guns`, `modifiers` stay).
Use python3 -I scripts; verify by re-scanning.
1. Rename to 1.21 singular dirs under data/<ns>/: recipes->recipe, loot_tables->loot_table, advancements->advancement, structures->structure, tags/items->tags/item, tags/blocks->tags/block, tags/entity_types->tags/entity_type, tags/fluids->tags/fluid, tags/biomes->tags/biome. JEG custom folders (guns, modifiers, factions, damage_type, worldgen/*) are NOT renamed.
2. Recipes (203): custom `jeg:` types keep schema untouched. Vanilla types: result `"item"` -> `"result": {"id": .., "count": n}`; smelting result object form; `forge:conditions` -> `neoforge:conditions` (`forge:mod_loaded`->`neoforge:mod_loaded`, `forge:not`->`neoforge:not`);
   tag namespace `forge:` -> `c:` with same paths.
3. Loot tables: `minecraft:set_nbt` -> `minecraft:set_custom_data`; forge: -> neoforge: conditions.
4. data/forge/** -> data/neoforge/** or data/c/**: forge tags -> data/c/tags; global_loot_modifiers.json -> data/neoforge/loot_modifiers/global_loot_modifiers.json; data/jeg/loot_modifiers/*.json keep type, conditions forge: -> neoforge:.
5. data/jeg/forge/** (7 files): biome modifiers -> data/jeg/neoforge/biome_modifier/*.json (`forge:add_features` -> `neoforge:add_features`, `forge:add_spawns` -> `neoforge:add_spawns`...); handle other types by their NeoForge location; document.
6. Tags: forge: -> c: entries; data/minecraft/tags under singular folders.
7. assets/jeg/models/item: custom loader ids `forge:` -> `neoforge:`; check blockstates for forge keys.
8. Structures NBT + worldgen JSON: fix `"item":` stack formats to `{"id":..}`; report oddities.
9. assets/minecraft/** check for 1.21-incompatible content (report).
10. data/jeg/damage_type: 1.21 needs message_id, scaling, exhaustion; fix; also data/minecraft/tags/damage_type.
11. Validate every .json parses; notes with file move counts + uncertainties.
