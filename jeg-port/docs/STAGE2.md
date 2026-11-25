# Stage 2 (AFTER base port compiles on the user's PC; do NOT start earlier)

Decisions from the user:
1. Magazine/ammo system (from JEGN, /home/claude/jeg-ref/port: gun/Magazine*, item/MagazineItem, block/MagazineLoaderBlock(+Entity), menu/MagazineLoaderMenu, mixin/MagazineMode*, network/Magazine*, client/MagazineModeClient, recipes in MagazineModeRules):
   craft bullets in bulk, manually load into magazines, or use a loader block. Re-implement on the ORIGINAL GunItem, not a copy.
   Loader block + UI: placeholder only. Block model/texture = fletching table look, menu UI = furnace-style screen, until the user makes real ones.
   Server config toggle magazineFeedEnabled (default true).
2. Repair: remove anvil/XP repair. Crafting-table special recipe: gun + repair kit = full repair.
3. Remove attachment durability entirely (check whether the original has it at all).
4. Server config toggle `gunnerSpawnsOutsideRaids` (default true = normal). When false, gun-wielding mobs spawn ONLY in the mod's own base raids (village-raid-like events); no natural/biome spawns. Spawn eggs/commands still work. Verify what raid system the original has (JEGN: FactionEventTicker).
Assets from JEGN are private-use only.
