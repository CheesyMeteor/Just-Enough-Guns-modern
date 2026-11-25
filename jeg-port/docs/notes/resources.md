# resources slice notes (JSON/assets only; nothing compiled or loaded in-game)
Migration script (kept for reference, re-runnable on a fresh copy of the original): scratchpad `migrate.py`; all 881 .json/.mcmeta files re-parsed OK.

## (a) What was done
- Dir renames (singular): data/jeg/recipes->recipe (203 files), loot_tables->loot_table (33), tags/blocks->block (jeg 7, minecraft 8), tags/items->item, tags/entity_types->entity_type (7: fire, ghost, gunner, heavy, none, undead, very_heavy - SHIPPED, src/generated/resources/data/jeg/tags/entity_type), generated tags/block (6) + tags/item/ammo. JEG custom dirs (guns, modifiers, factions, damage_type, worldgen/*, loot_modifiers, point_of_interest_type) not renamed. gun/modifier JSON content untouched.
- Recipes: 25 vanilla (11 shaped, 6 shapeless, 4 smelting, 4 blasting) result -> `{"id":..,"count"?}`; 178 jeg: recipes untouched except tag names. No forge:conditions existed. Tags: `forge:` -> `c:` same path, EXCEPT `forge:gunpowder` -> `c:gunpowders`, `forge:sandstone` -> `c:sandstone/blocks`, `#forge:glass` -> `#c:glass_blocks` (NeoForge 1.21 c: names; UNVERIFIED from memory). `c:ingots/{iron,copper,gold}`, `c:nuggets/iron`, `c:dyes/black` unchanged paths.
- Loot tables: set_nbt -> set_custom_data (5 blueprint rewards, SNBT string tag {Namespace,Path}); match_tool silk_touch -> `predicates: {minecraft:enchantments: [...]}` (7); looting_enchant -> enchanted_count_increase (+enchantment minecraft:looting) (2); random_chance_with_looting -> random_chance_with_enchanted_bonus (ghoul: 0.3 base, linear 0.4 + 0.1/level above first = same as old 0.3 + 0.1*level); copy_nbt removed:
  - ammo_box: copy_components(custom_name, container, lock, container_loot) + set_contents{component:minecraft:container,...} (shulker-box style; AmmoBoxBlockEntity extends RandomizableContainerBlockEntity).
  - boohive / boo_nest: copy_components include `minecraft:bees` (vanilla beehive style). PORT-VERIFY: BeehiveBlockEntityAbstract keeps its own "BeeList" NBT, so the vanilla BEES component may not carry JEG's inhabitants (see c).
  - Raid tables (generic_raid_{wave,victory}_reward + 5 faction victory_reward), terror_phantom_{supply,reward}, loot_drop_items/{ammo_pouch,badge_pack,skin_crate,warden_treasure_bag} all present, `"type":"minecraft:chest"`.
- Global loot modifiers: data/forge/loot_modifiers/global_loot_modifiers.json -> data/neoforge/loot_modifiers/global_loot_modifiers.json (22 entries); 22 data/jeg/loot_modifiers/*.json keep type, `forge:loot_table_id` -> `neoforge:loot_table_id` (44 conditions->22 files).
- data/forge/tags/blocks/ores.json -> data/c/tags/block/ores.json.
- Biome modifiers data/jeg/forge/biome_modifier -> data/jeg/neoforge/biome_modifier (6 loaded: add_features x4 -> neoforge:add_features, add_spawns x2 mob_ghoul/mob_boo -> neoforge:add_spawns). ODDITY KEPT: `mob_remover_cherry_grove` has NO .json extension in the original, so it was never loaded; converted to neoforge:remove_spawns but left extension-less (still not loaded, behaviour preserved). Rename to .json to enable (it would remove zombie/skeleton spawns in cherry grove).
- Assets: no `forge:` loader ids, forge_marker or forge parents anywhere in assets/jeg/models or blockstates (nothing to change). assets/minecraft/atlases/blocks.json (directory source) is 1.21-valid. pack.mcmeta already pack_format 34 (events-misc).
- No advancements/structures/NBT structure files exist in the original -> nothing to migrate. Worldgen configured/placed feature JSON (ore) is 1.21-valid unchanged; ids match the biome modifiers (`jeg:*_ore_placed`).
- damage_type jeg:bullet already has message_id/scaling/exhaustion (valid 1.21); no data/minecraft/tags/damage_type existed originally and none was added (if bullets should count as is_projectile / bypass rules, add tags - behaviour question for events-misc/projectile).
- Access transformer (META-INF/accesstransformer.cfg, Mojang names; base done by events-misc): ADDED BeehiveBlockEntity.savedFlowerPos, Bee.savedFlowerPos, Bee.remainingCooldownBeforeLocatingNewFlower, Bee.hasSavedFlowerPos(), Bee.isTiredOfLookingForNectar() (Boo line ~261), Bee.spawnFluidParticle(...) (private in Bee, Boo @Overrides it). Already present: AbstractSkeleton bowGoal/meleeGoal, Bee.hivePos + goals + inner goal ctors, Creeper.swell, Mob.populateDefaultEquipmentSlots. Phantom goal classes/Display setters not referenced by the Java -> not added.

## (b) PORT-VERIFY
- accesstransformer.cfg: Bee isTiredOfLookingForNectar, spawnFluidParticle, savedFlowerPos names from memory (wrong Mojang name = startup error naming the member), plus the events-misc PORT-VERIFY lines (isPollinating, clearBlacklist, isTargetBlacklisted, blacklistTarget, beePollinateGoal/goToHiveGoal/goToKnownFlowerGoal).
- c: tag names `c:gunpowders`, `c:sandstone/blocks`, `c:glass_blocks`, `c:ores` (the latter is only a file name).
- loot: `enchanted_count_increase`, `random_chance_with_enchanted_bonus`, match_tool `predicates` format, `copy_components`/`set_contents` `component` field are 1.21.1 formats from memory.
- Boo/Boohive AT: Boo.java may use further package-private Bee members not listed in notes (hasHive/isTooFarAway are redeclared in Boo); a compile will tell.

## (c) Outside my slice
- Boohive/Boo nest drop: BeehiveBlockEntityAbstract should implement collectImplicitComponents/applyImplicitComponents to carry its "BeeList" (custom NBT) through the item, otherwise silk-touched nests drop empty (blocks-world).
- New guns must be added to data/jeg/tags/item/enchantable/gun.json (events-misc).
- `data/jeg/loot_modifiers/*.json` are loaded through NeoForge's `neoforge:loot_modifiers/` mechanism: folder name `loot_modifiers` kept; blocks-world's MapCodecs are registered via ModLootModifiers.

## (d) Not finished / uncertain
- Nothing known unfinished. JSON was reformatted to 2-space indent for recipes/loot tables (content-equivalent apart from the changes above). Backup of the pre-migration src is in the session scratchpad only.
