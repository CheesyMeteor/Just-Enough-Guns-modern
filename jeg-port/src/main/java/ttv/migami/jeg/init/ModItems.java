package ttv.migami.jeg.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.common.Attachments;
import ttv.migami.jeg.common.GunModifiers;
import ttv.migami.jeg.item.*;
import ttv.migami.jeg.item.attachment.impl.*;
import ttv.migami.jeg.item.attachment.item.*;

public class ModItems {

    public static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(Reference.MOD_ID);

    public static final DeferredItem<AnimatedGunItem> ABSTRACT_GUN =
            REGISTER.register("abstract_gun",
                    () -> new AnimatedGunItem(new Item.Properties().stacksTo(1), "abstract_gun"));

    /* Firearms */
    /* Scrap Tier */
    public static final DeferredItem<AnimatedGunItem> REVOLVER = REGISTER.register("revolver",
            () -> new AnimatedMakeshiftGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(128),
                    "revolver"
            ));
    public static final DeferredItem<AnimatedGunItem> WATERPIPE_SHOTGUN = REGISTER.register("waterpipe_shotgun",
            () -> new AnimatedMakeshiftGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(128),
                    "waterpipe_shotgun"
            ));
    public static final DeferredItem<AnimatedGunItem> CUSTOM_SMG = REGISTER.register("custom_smg",
            () -> new AnimatedMakeshiftGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(312)
                    .rarity(Rarity.UNCOMMON),
                    "custom_smg"
            ));
    public static final DeferredItem<AnimatedGunItem> DOUBLE_BARREL_SHOTGUN = REGISTER.register("double_barrel_shotgun",
            () -> new AnimatedMakeshiftGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(128),
                    "double_barrel_shotgun"
            ));
    /* Gunmetal Tier */
    public static final DeferredItem<AnimatedGunItem> SEMI_AUTO_PISTOL = REGISTER.register("semi_auto_pistol",
            () -> new AnimatedMakeshiftGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(256)
                    .rarity(Rarity.UNCOMMON),
                    "semi_auto_pistol"
            ));
    public static final DeferredItem<AnimatedGunItem> SEMI_AUTO_RIFLE = REGISTER.register("semi_auto_rifle",
            () -> new AnimatedMakeshiftGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(384)
                    .rarity(Rarity.UNCOMMON),
                    "semi_auto_rifle"
            ));
    public static final DeferredItem<AnimatedGunItem> ASSAULT_RIFLE = REGISTER.register("assault_rifle",
            () -> new AnimatedMakeshiftGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(384)
                    .rarity(Rarity.RARE),
                    "assault_rifle"
            ));
    public static final DeferredItem<AnimatedGunItem> PUMP_SHOTGUN = REGISTER.register("pump_shotgun",
            () -> new AnimatedMakeshiftGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(384)
                    .rarity(Rarity.RARE),
                    "pump_shotgun"
            ));
    /* Gunnite Tier */
    public static final DeferredItem<AnimatedGunItem> COMBAT_PISTOL = REGISTER.register("combat_pistol",
            () -> new AnimatedGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(480)
                    .rarity(Rarity.RARE),
                    "combat_pistol"
            ));
    public static final DeferredItem<AnimatedGunItem> BURST_RIFLE = REGISTER.register("burst_rifle",
            () -> new AnimatedGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(640)
                    .rarity(Rarity.EPIC),
                    "burst_rifle"
            ));
    public static final DeferredItem<AnimatedGunItem> COMBAT_RIFLE = REGISTER.register("combat_rifle",
            () -> new AnimatedGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(640)
                    .rarity(Rarity.EPIC),
                    "combat_rifle"
            ));
    public static final DeferredItem<AnimatedGunItem> BOLT_ACTION_RIFLE = REGISTER.register("bolt_action_rifle",
            () -> new AnimatedGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(384)
                    .rarity(Rarity.EPIC),
                    "bolt_action_rifle"
            ));
    public static final DeferredItem<AnimatedGunItem> FLARE_GUN = REGISTER.register("flare_gun",
            () -> new AnimatedGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(32)
                    .rarity(Rarity.UNCOMMON),
                    "flare_gun"
            ));

    /* Blue-printable */
    public static final DeferredItem<AnimatedGunItem> REPEATING_SHOTGUN = REGISTER.register("repeating_shotgun",
            () -> new AnimatedGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(384)
                    .rarity(Rarity.EPIC),
                    "repeating_shotgun"
            ));
    public static final DeferredItem<AnimatedGunItem> INFANTRY_RIFLE = REGISTER.register("infantry_rifle",
            () -> new AnimatedGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(512)
                    .rarity(Rarity.EPIC),
                    "infantry_rifle"
            ));
    public static final DeferredItem<AnimatedGunItem> SERVICE_RIFLE = REGISTER.register("service_rifle",
            () -> new AnimatedGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(512)
                    .rarity(Rarity.EPIC),
                    "service_rifle"
            ));

    /* Spectre Tier */
    public static final DeferredItem<AnimatedGunItem> BLOSSOM_RIFLE = REGISTER.register("blossom_rifle",
            () -> new AnimatedGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(512)
                    .rarity(Rarity.EPIC),
                    "blossom_rifle"
            ));
    public static final DeferredItem<GunItem> HOLY_SHOTGUN = REGISTER.register("holy_shotgun",
            () -> new GunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(384)
                    .rarity(Rarity.EPIC)
            ));

    /* Water Tier */
    public static final DeferredItem<GunItem> ATLANTEAN_SPEAR = REGISTER.register("atlantean_spear",
            () -> new GunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(128)
                    .rarity(Rarity.EPIC)
            ));
    public static final DeferredItem<GunItem> TYPHOONEE = REGISTER.register("typhoonee",
            () -> new GunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(128)
                    .rarity(Rarity.EPIC)
            ));

    /*public static final DeferredItem<UnderwaterFirearmItem> BUBBLE_CANNON = REGISTER.register("bubble_cannon",
            () -> new UnderwaterFirearmItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(128)
                    .rarity(Rarity.EPIC)
            ));*/

    /* Fire Tier */
    public static final DeferredItem<AnimatedGunItem> HOLLENFIRE_MK2 = REGISTER.register("hollenfire_mk2",
            () -> new AnimatedGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(400)
                    .rarity(Rarity.EPIC),
                    "hollenfire_mk2"
            ));
    public static final DeferredItem<AnimatedGunItem> SOULHUNTER_MK2 = REGISTER.register("soulhunter_mk2",
            () -> new AnimatedGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(400)
                    .rarity(Rarity.EPIC),
                    "soulhunter_mk2"
            ));

    /* Sculk Tier */
    public static final DeferredItem<AnimatedGunItem> SUBSONIC_RIFLE = REGISTER.register("subsonic_rifle",
            () -> new AnimatedGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(384)
                    .rarity(Rarity.EPIC),
                    "subsonic_rifle"
            ));
    public static final DeferredItem<AnimatedGunItem> SUPERSONIC_SHOTGUN = REGISTER.register("supersonic_shotgun",
            () -> new AnimatedGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(86)
                    .rarity(Rarity.EPIC),
                    "supersonic_shotgun"
            ));
    public static final DeferredItem<AnimatedGunItem> HYPERSONIC_CANNON = REGISTER.register("hypersonic_cannon",
            () -> new AnimatedGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(100)
                    .rarity(Rarity.EPIC),
                    "hypersonic_cannon"
            ));

    public static final DeferredItem<AnimatedGunItem> ROCKET_LAUNCHER = REGISTER.register("rocket_launcher",
            () -> new AnimatedGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(32)
                    .rarity(Rarity.EPIC),
                    "rocket_launcher"
            ));
    public static final DeferredItem<AnimatedBowItem> PRIMITIVE_BOW = REGISTER.register("primitive_bow",
            () -> new AnimatedBowItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(128)
                    .rarity(Rarity.RARE),
                    "primitive_bow"
            ));
    public static final DeferredItem<AnimatedBowItem> COMPOUND_BOW = REGISTER.register("compound_bow",
            () -> new AnimatedBowItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(128)
                    .rarity(Rarity.EPIC),
                    "compound_bow"
            ));
    public static final DeferredItem<AnimatedGunItem> GRENADE_LAUNCHER = REGISTER.register("grenade_launcher",
            () -> new AnimatedGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(64)
                    .rarity(Rarity.EPIC),
                    "grenade_launcher"
            ));
    public static final DeferredItem<AnimatedGunItem> LIGHT_MACHINE_GUN = REGISTER.register("light_machine_gun",
            () -> new AnimatedGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(1024)
                    .rarity(Rarity.EPIC),
                    "light_machine_gun"
            ));
    public static final DeferredItem<AnimatedGunItem> FLAMETHROWER = REGISTER.register("flamethrower",
            () -> new AnimatedGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(1024)
                    .rarity(Rarity.EPIC),
                    "flamethrower"
            ));
    public static final DeferredItem<AnimatedGunItem> MINIGUN = REGISTER.register("minigun",
            () -> new AnimatedGunItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(2048)
                    .rarity(Rarity.EPIC),
                    "minigun"
            ));

    public static final DeferredItem<AnimatedGunItem> FINGER_GUN = REGISTER.register("finger_gun",
            () -> new AnimatedGunItem(new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.EPIC),
                    "finger_gun"
            ));

    //public static final DeferredItem<Item> GRENADE_LAUNCHER = REGISTER.register("grenade_launcher", () -> new GunItem(new Item.Properties().stacksTo(1)));

    // Score Streaks
    public static final DeferredItem<ScoreStreakItem> AIR_STRIKE_FLARE = REGISTER.register("air_strike_flare",
            () -> new AirStrikeFlareItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1), 5000));

    public static final DeferredItem<ScoreStreakItem> PHANTOM_GUNNER_BAIT = REGISTER.register("phantom_gunner_bait",
            () -> new PhantomGunnerBaitItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(1), 3000));

    public static final DeferredItem<TerrorHornItem> TERROR_HORN = REGISTER.register("terror_horn",
            () -> new TerrorHornItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)));

    /* Projectiles And Throwables */
    public static final DeferredItem<Item> GRENADE = REGISTER.register("grenade",
            () -> new GrenadeItem(new Item.Properties()
                    .stacksTo(16)
                    , 20 * 4
            ));
    public static final DeferredItem<Item> STUN_GRENADE = REGISTER.register("stun_grenade",
            () -> new StunGrenadeItem(new Item.Properties()
                    .stacksTo(16)
                    , 72000
            ));
    public static final DeferredItem<Item> MOLOTOV_COCKTAIL = REGISTER.register("molotov_cocktail",
            () -> new MolotovCocktailItem(new Item.Properties()
                    .stacksTo(16)
                    , 72000
            ));
    // Thanks to An0m3l1 on Discord!
    public static final DeferredItem<Item> SMOKE_GRENADE = REGISTER.register("smoke_grenade",
            () -> new SmokeGrenadeItem(new Item.Properties()
                    .stacksTo(16)
                    , 20 * 5
            ));
    public static final DeferredItem<Item> WATER_BOMB = REGISTER.register("water_bomb",
            () -> new WaterBombItem(new Item.Properties()
                    .stacksTo(16)
                    , 72000
            ));
    public static final DeferredItem<Item> POCKET_BUBBLE = REGISTER.register("pocket_bubble",
            () -> new PocketBubbleItem(new Item.Properties()
                    .stacksTo(16)
            ));
    public static final DeferredItem<Item> ROCKET = REGISTER.register("rocket",
            () -> new AmmoItem(new Item.Properties().rarity(Rarity.EPIC)
                    .stacksTo(4)
            ));
    public static final DeferredItem<Item> EXPLOSIVE_CHARGE = REGISTER.register("explosive_charge",
            () -> new ExplosiveChargeItem(new Item.Properties()
                    .stacksTo(2).rarity(Rarity.EPIC)
                    , 72000
            ));
    public static final DeferredItem<Item> FLARE = REGISTER.register("flare",
            () -> new FlareItem(new Item.Properties()
                    .stacksTo(16)
                    , 72000
            ));
    public static final DeferredItem<Item> TERROR_ARMADA_FLARE = REGISTER.register("terror_armada_flare",
            () -> new FlareItem(new Item.Properties()
                    .stacksTo(1).rarity(Rarity.EPIC)
                    , 72000
            ));


    /* Ammo */
    public static final DeferredItem<Item> RIFLE_AMMO = REGISTER.register("rifle_ammo",
            () -> new AmmoItem(new Item.Properties()
            ));
    public static final DeferredItem<Item> PISTOL_AMMO = REGISTER.register("pistol_ammo",
            () -> new AmmoItem(new Item.Properties()
            ));
    public static final DeferredItem<Item> HANDMADE_SHELL = REGISTER.register("handmade_shell",
            () -> new AmmoItem(new Item.Properties()
            ));
    public static final DeferredItem<Item> SHOTGUN_SHELL = REGISTER.register("shotgun_shell",
            () -> new AmmoItem(new Item.Properties()
            ));
    public static final DeferredItem<Item> SPECTRE_ROUND = REGISTER.register("spectre_round",
            () -> new AmmoItem(new Item.Properties()
            ));
    public static final DeferredItem<Item> BLAZE_ROUND = REGISTER.register("blaze_round",
            () -> new AmmoItem(new Item.Properties()
            ));

    /* Healing Items */

    /* Utility Items */
    /*public static final DeferredItem<Item> GRAPPLING_HOOK = REGISTER.register("grappling_hook",
            () -> new GrapplingHookItem(new Item.Properties()
                    .stacksTo(1)
            ));*/

    /* Paint Job Cans */
    /*public static final DeferredItem<Item> CAMO_SPRAY_CAN = REGISTER.register("camo_spray_can",
            () -> new PaintJobCanItem(new Item.Properties().stacksTo(1), "camo"));*/

    public static final DeferredItem<Item> CLASSIC_SPRAY_CAN = REGISTER.register("classic_spray_can",
            () -> new PaintJobCanItem(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1), "classic"));

    public static final DeferredItem<Item> TOY_SPRAY_CAN = REGISTER.register("toy_spray_can",
            () -> new PaintJobCanItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(1), "toy"));

    public static final DeferredItem<Item> WHITEOUT_SPRAY_CAN = REGISTER.register("whiteout_spray_can",
            () -> new PaintJobCanItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(1), "whiteout"));

    /*public static final DeferredItem<Item> SCORCHED_SPRAY_CAN = REGISTER.register("scorched_spray_can",
            () -> new PaintJobCanItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(1), "scorched"));*/

    /*public static final DeferredItem<Item> ANIME_SPRAY_CAN = REGISTER.register("anime_spray_can",
            () -> new PaintJobCanItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1), "anime"));*/

    public static final DeferredItem<Item> GOLDEN_SPRAY_CAN = REGISTER.register("golden_spray_can",
            () -> new PaintJobCanItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1), "golden"));

    /* Kill Effects */
    public static final DeferredItem<Item> CREEPER_BIRTHDAY_PARTY_BADGE   = REGISTER.register("creeper_birthday_party_badge",
            () -> new KillEffectItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)));

    public static final DeferredItem<Item> HEADPOPPER_BADGE = REGISTER.register("headpoppper_badge",
            () -> new KillEffectItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)));

    public static final DeferredItem<Item> TRICKSHOT_BADGE = REGISTER.register("trickshot_badge",
            () -> new KillEffectItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)));

    /* Scope Attachments */
    public static final DeferredItem<Item> REFLEX_SIGHT = REGISTER.register("reflex_sight",
            () -> new ScopeItem(Attachments.REFLEX_SIGHT, new Item.Properties()
                    .stacksTo(1)));

    public static final DeferredItem<Item> MONOCLE_SIGHT = REGISTER.register("monocle_sight",
            () -> new ScopeItem(Attachments.MONOCLE_SIGHT, new Item.Properties()
                    .stacksTo(1)));

    public static final DeferredItem<Item> HOLOGRAPHIC_SIGHT = REGISTER.register("holographic_sight",
            () -> new ScopeItem(Attachments.HOLOGRAPHIC_SIGHT, new Item.Properties().rarity(Rarity.UNCOMMON)
                    .stacksTo(1)));

    public static final DeferredItem<Item> COMBAT_SCOPE = REGISTER.register("combat_scope",
            () -> new ScopeItem(Attachments.COMBAT_SCOPE, new Item.Properties().rarity(Rarity.RARE)
                    .stacksTo(1)));

    public static final DeferredItem<Item> TELESCOPIC_SIGHT = REGISTER.register("telescopic_sight",
            () -> new TelescopicScopeItem(Attachments.TELESCOPIC_SIGHT, new Item.Properties().rarity(Rarity.EPIC)
                    .stacksTo(1)));

    /* Stock Attachments */
    public static final DeferredItem<Item> MAKESHIFT_STOCK = REGISTER.register("makeshift_stock",
            () -> new MakeshiftStockItem(Stock.create(
                    GunModifiers.MAKESHIFT_CONTROL),
                    new Item.Properties()
                            .stacksTo(1)
                    , false
            ));
    public static final DeferredItem<Item> LIGHT_STOCK = REGISTER.register("light_stock",
            () -> new StockItem(Stock.create(
                    GunModifiers.BETTER_CONTROL),
                    new Item.Properties()
                            .stacksTo(1)
                    , false
            ));
    public static final DeferredItem<Item> TACTICAL_STOCK = REGISTER.register("tactical_stock",
            () -> new StockItem(Stock.create(
                    GunModifiers.STABILISED),
                    new Item.Properties()
                            .stacksTo(1)
                    , false
            ));
    public static final DeferredItem<Item> WEIGHTED_STOCK = REGISTER.register("weighted_stock",
            () -> new StockItem(Stock.create(
                    GunModifiers.SUPER_STABILISED),
                    new Item.Properties()
                            .stacksTo(1)));

    /* Barrel Attachments */
    public static final DeferredItem<Item> SILENCER = REGISTER.register("silencer",
            () -> new BarrelItem(Barrel.create(
                    0.0F,
                    GunModifiers.SILENCED),
                    new Item.Properties()
                            .stacksTo(1)));
                            //GunModifiers.REDUCED_DAMAGE),

    public static final DeferredItem<Item> EXPLOSIVE_MUZZLE = REGISTER.register("explosive_muzzle",
            () -> new BarrelItem(Barrel.create(
                    0.0F,
                    GunModifiers.EXPLOSIVE_AMMO, GunModifiers.INCREASED_JAMMING, GunModifiers.INSCREASED_DAMAGE, GunModifiers.WORSE_CONTROL),
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(64)
                            .rarity(Rarity.UNCOMMON)
            ));

    public static final DeferredItem<Item> TRUMPET = REGISTER.register("trumpet",
            () -> new TrumpetItem(Barrel.create(
                    0.0F,
                    GunModifiers.ANNOYING),
                    new Item.Properties()
                            .rarity(Rarity.RARE)
                            .stacksTo(1)
                            .durability(64)
            ));

    /* Under Barrel Attachments */
    public static final DeferredItem<Item> LIGHT_GRIP = REGISTER.register("light_grip",
            () -> new UnderBarrelItem(UnderBarrel.create(
                    GunModifiers.LIGHT_RECOIL), new
                    Item.Properties()
                    .stacksTo(1)));
    public static final DeferredItem<Item> VERTICAL_GRIP = REGISTER.register("vertical_grip",
            () -> new UnderBarrelItem(UnderBarrel.create(
                    GunModifiers.REDUCED_RECOIL), new
                    Item.Properties()
                    .stacksTo(1)));

    public static final DeferredItem<Item> ANGLED_GRIP = REGISTER.register("angled_grip",
            () -> new UnderBarrelItem(UnderBarrel.create(
                    GunModifiers.REDUCED_RECOIL), new
                    Item.Properties()
                    .stacksTo(1)));

    /* Magazine */
    public static final DeferredItem<Item> EXTENDED_MAG = REGISTER.register("extended_mag",
            () -> new MagazineItem(Magazine.create(
                    GunModifiers.SLOW_ADS),
                    new Item.Properties()
                            .stacksTo(1)
            ));

    public static final DeferredItem<Item> DRUM_MAG = REGISTER.register("drum_mag",
            () -> new MagazineItem(Magazine.create(
                    GunModifiers.SLOWER_ADS),
                    new Item.Properties()
                            .stacksTo(1)
            ));

    /* Ammo magazines (JEG: Modern) */
    public static final DeferredItem<Item> PISTOL_MAGAZINE = REGISTER.register("pistol_magazine",
            () -> new AmmoMagazineItem(new Item.Properties().stacksTo(1), AmmoMagazineItem.Type.PISTOL));
    public static final DeferredItem<Item> PISTOL_EXTENDED_MAGAZINE = REGISTER.register("pistol_extended_magazine",
            () -> new AmmoMagazineItem(new Item.Properties().stacksTo(1), AmmoMagazineItem.Type.PISTOL_EXTENDED));
    public static final DeferredItem<Item> PISTOL_DRUM_MAGAZINE = REGISTER.register("pistol_drum_magazine",
            () -> new AmmoMagazineItem(new Item.Properties().stacksTo(1), AmmoMagazineItem.Type.PISTOL_DRUM));
    public static final DeferredItem<Item> RIFLE_MAGAZINE = REGISTER.register("rifle_magazine",
            () -> new AmmoMagazineItem(new Item.Properties().stacksTo(1), AmmoMagazineItem.Type.RIFLE));
    public static final DeferredItem<Item> RIFLE_EXTENDED_MAGAZINE = REGISTER.register("rifle_extended_magazine",
            () -> new AmmoMagazineItem(new Item.Properties().stacksTo(1), AmmoMagazineItem.Type.RIFLE_EXTENDED));
    public static final DeferredItem<Item> RIFLE_DRUM_MAGAZINE = REGISTER.register("rifle_drum_magazine",
            () -> new AmmoMagazineItem(new Item.Properties().stacksTo(1), AmmoMagazineItem.Type.RIFLE_DRUM));
    public static final DeferredItem<Item> MINIGUN_AMMO_DRUM = REGISTER.register("minigun_ammo_drum",
            () -> new AmmoMagazineItem(new Item.Properties().stacksTo(1), AmmoMagazineItem.Type.MINIGUN));
    public static final DeferredItem<Item> MACHINE_GUN_MAGAZINE = REGISTER.register("machine_gun_magazine",
            () -> new AmmoMagazineItem(new Item.Properties().stacksTo(1), AmmoMagazineItem.Type.MACHINE_GUN));

    /* Side */
    public static final DeferredItem<Item> FLASHLIGHT = REGISTER.register("flashlight",
            () -> new FlashlightItem(Side.create(
                    GunModifiers.FLASHLIGHT, GunModifiers.SLOW_ADS),
                    new Item.Properties()
                            .rarity(Rarity.UNCOMMON)
                            .stacksTo(1)
            ));

    public static final DeferredItem<Item> LASER_POINTER = REGISTER.register("laser_pointer",
            () -> new SpecialItem(Side.create(
                    GunModifiers.LASER_POINTER, GunModifiers.BETTER_CONTROL),
                    new Item.Properties()
                            .rarity(Rarity.UNCOMMON)
                            .stacksTo(1)
            ));

    /* Loot Drop Items */
    public static final DeferredItem<LootDropItem> AMMO_POUCH = REGISTER.register("ammo_pouch",
            () -> new LootDropItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<LootDropItem> BADGE_PACK = REGISTER.register("badge_pack",
            () -> new LootDropItem(new Item.Properties().rarity(Rarity.EPIC)));
    public static final DeferredItem<LootDropItem> SKIN_CRATE = REGISTER.register("skin_crate",
            () -> new LootDropItem(new Item.Properties().rarity(Rarity.EPIC)));

    /* Items */
    public static final DeferredItem<Item> SCRAP = REGISTER.register("scrap",
            () -> new ToolTipItem(new Item.Properties()));
    public static final DeferredItem<Item> REPAIR_KIT = REGISTER.register("repair_kit",
            () -> new ToolTipItem(new Item.Properties()));
    public static final DeferredItem<Item> TECH_TRASH = REGISTER.register("tech_trash",
            () -> new ToolTipItem(new Item.Properties()));
    public static final DeferredItem<Item> CIRCUIT_BOARD = REGISTER.register("circuit_board",
            () -> new Item(new Item.Properties()));
    //public static final DeferredItem<Item> SPRING = REGISTER.register("spring",
    //        () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GUNMETAL_GRIT = REGISTER.register("gunmetal_grit",
            () -> new ToolTipItem(new Item.Properties()));
    public static final DeferredItem<Item> GUNMETAL_INGOT = REGISTER.register("gunmetal_ingot",
            () -> new ToolTipItem(new Item.Properties()));
    public static final DeferredItem<Item> GUNMETAL_NUGGET = REGISTER.register("gunmetal_nugget",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GUNNITE_INGOT = REGISTER.register("gunnite_ingot",
            () -> new ToolTipItem(new Item.Properties()));
    public static final DeferredItem<Item> RAW_BRIMSTONE = REGISTER.register("raw_brimstone",
            () -> new ToolTipItem(new Item.Properties()));
    public static final DeferredItem<Item> BRIMSTONE_CRYSTAL = REGISTER.register("brimstone_crystal",
            () -> new ToolTipItem(new Item.Properties()));
    public static final DeferredItem<Item> ECTOPLASM = REGISTER.register("ectoplasm",
            () -> new ToolTipItem(new Item.Properties()));
    public static final DeferredItem<BlueprintItem> FIREARM_BLUEPRINT = REGISTER.register("firearm_blueprint",
            () -> new BlueprintItem(new Item.Properties()));
    public static final DeferredItem<BlueprintItem> ADVANCED_FIREARM_BLUEPRINT = REGISTER.register("advanced_firearm_blueprint",
            () -> new AdvancedBlueprintItem(new Item.Properties().rarity(Rarity.RARE)));

    // Mobs
    public static final DeferredItem<Item> GHOUL_SPAWN_TALISMAN = REGISTER.register("ghoul_spawn_talisman",
            () -> new DeferredSpawnEggItem(ModEntities.GHOUL, 0xFFFFFF, 0xFFFFFF, new Item.Properties()));

    public static final DeferredItem<Item> BOO_SPAWN_HONEYCOMB = REGISTER.register("boo_spawn_honeycomb",
            () -> new DeferredSpawnEggItem(ModEntities.BOO, 0xFFFFFF, 0xFFFFFF, new Item.Properties()));

    public static final DeferredItem<Item> TERROR_PHANTOM_SPAWN_EGG = REGISTER.register("terror_phantom_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.TERROR_PHANMTOM, 0xFFFFFF, 0xFFFFFF, new Item.Properties()));

    public static final DeferredItem<Item> PHANTOM_GUNNER_SPAWN_EGG = REGISTER.register("phantom_gunner_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.PHANTOM_GUNNER, 0xFFFFFF, 0xFFFFFF, new Item.Properties()));

    public static final DeferredItem<Item> SOUL_TREAT = REGISTER.register("soul_treat",
            () -> new SoultreatItem(new Item.Properties()));

    // Gunner Mobs
    public static final DeferredItem<Item> GUNNER_ZOMBIE_SPAWN_EGG = REGISTER.register("gunner_zombie_spawn_egg",
            () -> new GunnerSpawnEggItem(() -> EntityType.ZOMBIE, 0x00AFAF, 0x799C65, new Item.Properties()));

    public static final DeferredItem<Item> GUNNER_HUSK_SPAWN_EGG = REGISTER.register("gunner_husk_spawn_egg",
            () -> new GunnerSpawnEggItem(() -> EntityType.HUSK, 0x797061, 0xE6CC94, new Item.Properties()));

    public static final DeferredItem<Item> GUNNER_DROWNED_SPAWN_EGG = REGISTER.register("gunner_drowned_spawn_egg",
            () -> new GunnerSpawnEggItem(() -> EntityType.DROWNED, 0x8FF1D7, 0x799C65, new Item.Properties()));

    public static final DeferredItem<Item> GUNNER_ZOMBIE_VILLAGER_SPAWN_EGG = REGISTER.register("gunner_zombie_villager_spawn_egg",
            () -> new GunnerSpawnEggItem(() -> EntityType.ZOMBIE_VILLAGER, 0x563C33, 0x799C65, new Item.Properties()));

    public static final DeferredItem<Item> GUNNER_SKELETON_SPAWN_EGG = REGISTER.register("gunner_skeleton_spawn_egg",
            () -> new GunnerSpawnEggItem(() -> EntityType.SKELETON, 0xC1C1C1, 0x494949, new Item.Properties()));

    public static final DeferredItem<Item> GUNNER_STRAY_SPAWN_EGG = REGISTER.register("gunner_stray_spawn_egg",
            () -> new GunnerSpawnEggItem(() -> EntityType.STRAY, 0x617677, 0xDDEAEA, new Item.Properties()));

    public static final DeferredItem<Item> GUNNER_PILLAGER_SPAWN_EGG = REGISTER.register("gunner_pillager_spawn_egg",
            () -> new GunnerSpawnEggItem(() -> EntityType.PILLAGER, 0x532F36, 0x959B9B, new Item.Properties()));

    public static final DeferredItem<Item> GUNNER_VINDICATOR_SPAWN_EGG = REGISTER.register("gunner_vindicator_spawn_egg",
            () -> new GunnerSpawnEggItem(() -> EntityType.VINDICATOR, 0x959B9B, 0x275E61, new Item.Properties()));

    public static final DeferredItem<Item> GUNNER_PIGLIN_SPAWN_EGG = REGISTER.register("gunner_piglin_spawn_egg",
            () -> new GunnerSpawnEggItem(() -> EntityType.PIGLIN, 0x995F40, 0xF9F3A4, new Item.Properties()));

    public static final DeferredItem<Item> GUNNER_PIGLIN_BRUTE_SPAWN_EGG = REGISTER.register("gunner_piglin_brute_spawn_egg",
            () -> new GunnerSpawnEggItem(() -> EntityType.PIGLIN_BRUTE, 0x592A10, 0xF9F3A4, new Item.Properties()));

    public static final DeferredItem<Item> GUNNER_ZOMBIFIED_PIGLIN_SPAWN_EGG = REGISTER.register("gunner_zombified_piglin_spawn_egg",
            () -> new GunnerSpawnEggItem(() -> EntityType.ZOMBIFIED_PIGLIN, 0xEA9393, 0x4C7129, new Item.Properties()));

    public static final DeferredItem<Item> GUNNER_WITHER_SKELETON_SPAWN_EGG = REGISTER.register("gunner_wither_skeleton_spawn_egg",
            () -> new GunnerSpawnEggItem(() -> EntityType.WITHER_SKELETON, 0x141414, 0x474D4D, new Item.Properties()));

    public static final DeferredItem<Item> GUNNER_GHOUL_SPAWN_EGG = REGISTER.register("gunner_ghoul_spawn_egg",
            () -> new GunnerSpawnEggItem(() -> ModEntities.GHOUL.get(), 0xF9F9F9, 0x00ADFF, new Item.Properties()));

    // Fallbacks
    /*public static final DeferredItem<Item> SCAR_L_FALLBACK = REGISTER.register("scar_l",
            () -> new FallbackItem(new Item.Properties()));

    public static final DeferredItem<Item> HK_G36_FALLBACK = REGISTER.register("hk_g36",
            () -> new FallbackItem(new Item.Properties()));*/
}