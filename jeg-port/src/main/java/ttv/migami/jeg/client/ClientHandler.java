package ttv.migami.jeg.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.lwjgl.glfw.GLFW;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.client.handler.*;
import ttv.migami.jeg.client.render.gun.ModelOverrides;
import ttv.migami.jeg.client.render.gun.model.*;
import ttv.migami.jeg.client.screen.*;
import ttv.migami.jeg.client.screen.recycler.RecyclerScreen;
import ttv.migami.jeg.client.util.PropertyHelper;
import ttv.migami.jeg.common.NetworkGunManager;
import ttv.migami.jeg.debug.IEditorMenu;
import ttv.migami.jeg.debug.client.screen.EditorScreen;
import ttv.migami.jeg.init.ModBlocks;
import ttv.migami.jeg.init.ModContainers;
import ttv.migami.jeg.init.ModItems;
import ttv.migami.jeg.item.FlareItem;
import ttv.migami.jeg.item.GunItem;
import ttv.migami.jeg.item.IColored;
import ttv.migami.jeg.item.attachment.IAttachment;
import ttv.migami.jeg.network.PacketHandler;
import ttv.migami.jeg.network.message.C2SMessageAttachments;
import ttv.migami.jeg.util.StackData;

/**
 * Author: MrCrayfish
 */
@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class ClientHandler {
    public static final String CONFIG_NS = Reference.MOD_ID + "_cfg";

    // PORT-DROPPED: addPack(AddPackFindersEvent) - in the original everything after the folder check was commented out
    //   (the PathPackResources was built but never registered), so it never did anything. Not ported.

    public static void setup() {
        NeoForge.EVENT_BUS.register(AimingHandler.get());
        NeoForge.EVENT_BUS.register(BulletTrailRenderingHandler.get());
        NeoForge.EVENT_BUS.register(CrosshairHandler.get());
        NeoForge.EVENT_BUS.register(GunRenderingHandler.get());
        NeoForge.EVENT_BUS.register(GunRecoilHandler.get());
        NeoForge.EVENT_BUS.register(ReloadHandler.get());
        NeoForge.EVENT_BUS.register(ShootingHandler.get());
        NeoForge.EVENT_BUS.register(SoundHandler.get());
        NeoForge.EVENT_BUS.register(MeleeHandler.get());
        NeoForge.EVENT_BUS.register(InspectHandler.get());
        NeoForge.EVENT_BUS.register(new PlayerModelHandler());

        // PORT-DROPPED: Controllable (ControllerHandler.init / GunButtonBindings.register)

        setupRenderLayers();
        registerColors();
        registerModelOverrides();
        registerBlockEntityRenderers();
    }

    private static void setupRenderLayers() {
        // PORT: ItemBlockRenderTypes.setRenderLayer no longer exists in 1.21.1 NeoForge. Render types now come from the block model
        //   JSON (DONE in integration pass: "render_type" added to the block models) ("render_type": "minecraft:cutout" for SCRAP_WORKBENCH, GUNMETAL_WORKBENCH, GUNNITE_WORKBENCH, SCHEMATIC_STATION,
        //   BLUEPRINT_WORKBENCH, RECYCLER, AMMO_BOX; "minecraft:translucent" for DYNAMIC_LIGHT) - resources slice must add them.
    }

    private static void registerColors() {
        // PORT: 1.21 item colours are ARGB; the stored/derived colours are plain RGB so every non -1 result is made opaque
        ItemColor color = (stack, index) -> opaque(rawColor(stack, index));
        // PORT-VERIFY: registered directly on ItemColors from client setup (RegisterColorHandlersEvent.Item would be the event-based way)
        BuiltInRegistries.ITEM.forEach(item ->
        {
            if (item instanceof IColored) {
                Minecraft.getInstance().getItemColors().register(color, item);
            }
        });
    }

    private static int opaque(int color) {
        return color == -1 ? -1 : FastColor.ARGB32.opaque(color);
    }

    private static int rawColor(ItemStack stack, int index)
    {
            if (!IColored.isDyeable(stack)) {
                return -1;
            }
            if (index == 0 && StackData.get(stack).contains("Color", Tag.TAG_INT)) {
                return StackData.getInt(stack, "Color");
            }
            if (index == 0 && stack.getItem() instanceof IAttachment) {
                ItemStack renderingWeapon = GunRenderingHandler.get().getRenderingWeapon();
                if (renderingWeapon != null) {
                    return Minecraft.getInstance().getItemColors().getColor(renderingWeapon, index);
                }
            }
            if (index == 2) // Reticle colour
            {
                return PropertyHelper.getReticleColor(stack);
            }
            return -1;
    }

    private static void registerModelOverrides() {
        /* Weapons */
        //ModelOverrides.register(ModItems.BAZOOKA.get(), new SimpleModel(SpecialModels.BAZOOKA::getModel));
        //ModelOverrides.register(ModItems.GRENADE_LAUNCHER.get(), new GrenadeLauncherModel());

        ModelOverrides.register(ModItems.COMBAT_RIFLE.get(), new CombatRifleModel());
        ModelOverrides.register(ModItems.ASSAULT_RIFLE.get(), new AssaultRifleModel());
        ModelOverrides.register(ModItems.REVOLVER.get(), new RevolverModel());
        ModelOverrides.register(ModItems.WATERPIPE_SHOTGUN.get(), new WaterpipeShotgunModel());
        ModelOverrides.register(ModItems.SEMI_AUTO_RIFLE.get(), new SemiAutoRifleModel());
        ModelOverrides.register(ModItems.BURST_RIFLE.get(), new BurstRifleModel());
        ModelOverrides.register(ModItems.PUMP_SHOTGUN.get(), new PumpShotgunModel());
        ModelOverrides.register(ModItems.BOLT_ACTION_RIFLE.get(), new BoltActionRifleModel());
        ModelOverrides.register(ModItems.CUSTOM_SMG.get(), new CustomSMGModel());
        ModelOverrides.register(ModItems.BLOSSOM_RIFLE.get(), new BlossomRifleModel());
        ModelOverrides.register(ModItems.DOUBLE_BARREL_SHOTGUN.get(), new DoubleBarrelShotgunModel());
        ModelOverrides.register(ModItems.HOLY_SHOTGUN.get(), new HolyShotgunModel());
        ModelOverrides.register(ModItems.ATLANTEAN_SPEAR.get(), new AtlanteanSpearModel());
        ModelOverrides.register(ModItems.TYPHOONEE.get(), new TyphooneeModel());
        ModelOverrides.register(ModItems.FLARE_GUN.get(), new FlareGunModel());
        ModelOverrides.register(ModItems.HOLLENFIRE_MK2.get(), new HollenfireMK2Model());
        ModelOverrides.register(ModItems.SOULHUNTER_MK2.get(), new SoulhunterMK2Model());
        ModelOverrides.register(ModItems.REPEATING_SHOTGUN.get(), new RepeatingShotgunModel());
        ModelOverrides.register(ModItems.INFANTRY_RIFLE.get(), new InfantryRifleModel());
        ModelOverrides.register(ModItems.SERVICE_RIFLE.get(), new ServiceRifleModel());
        ModelOverrides.register(ModItems.ROCKET_LAUNCHER.get(), new RocketLauncherModel());
        ModelOverrides.register(ModItems.PRIMITIVE_BOW.get(), new PrimitiveBowModel());
        ModelOverrides.register(ModItems.COMPOUND_BOW.get(), new CompoundBowModel());
        ModelOverrides.register(ModItems.LIGHT_MACHINE_GUN.get(), new LightMachineGunModel());
        ModelOverrides.register(ModItems.GRENADE_LAUNCHER.get(), new GrenadeLauncherModel());
        ModelOverrides.register(ModItems.SUPERSONIC_SHOTGUN.get(), new SupersonicShotgunModel());
        ModelOverrides.register(ModItems.SUBSONIC_RIFLE.get(), new SubsonicRifleModel());
        ModelOverrides.register(ModItems.HYPERSONIC_CANNON.get(), new HypersonicCannonModel());
        ModelOverrides.register(ModItems.FLAMETHROWER.get(), new FlamethrowerModel());
        ModelOverrides.register(ModItems.SEMI_AUTO_PISTOL.get(), new SemiAutoPistolModel());
        ModelOverrides.register(ModItems.COMBAT_PISTOL.get(), new CombatPistolModel());
        ModelOverrides.register(ModItems.MINIGUN.get(), new MinigunModel());
        //ModelOverrides.register(ModItems.BUBBLE_CANNON.get(), new BubbleCannonModel());
        ModelOverrides.register(Items.WOODEN_SWORD, new BayonetWoodenModel());
        ModelOverrides.register(Items.STONE_SWORD, new BayonetStoneModel());
        ModelOverrides.register(Items.IRON_SWORD, new BayonetIronModel());
        ModelOverrides.register(Items.GOLDEN_SWORD, new BayonetGoldenModel());
        ModelOverrides.register(Items.DIAMOND_SWORD, new BayonetDiamondModel());
        ModelOverrides.register(Items.NETHERITE_SWORD, new BayonetNetheriteModel());

    }

    // PORT: MenuScreens.register -> RegisterMenuScreensEvent (mod bus; hooked up in JustEnoughGuns.ClientInit)
    public static void registerScreenFactories(RegisterMenuScreensEvent event) {
        event.register(ModContainers.GUNNITE_WORKBENCH.get(), GunniteWorkbenchScreen::new);
        event.register(ModContainers.SCRAP_WORKBENCH.get(), ScrapWorkbenchScreen::new);
        event.register(ModContainers.GUNMETAL_WORKBENCH.get(), GunmetalWorkbenchScreen::new);
        event.register(ModContainers.ATTACHMENTS.get(), AttachmentScreen::new);
        event.register(ModContainers.RECYCLER.get(), RecyclerScreen::new);
        event.register(ModContainers.MAGAZINE_LOADER.get(), ttv.migami.jeg.client.screen.MagazineLoaderScreen::new);
        event.register(ModContainers.POWERED_MAGAZINE_LOADER.get(), ttv.migami.jeg.client.screen.PoweredMagazineLoaderScreen::new);
        event.register(ModContainers.SCHEMATIC_STATION.get(), SchematicStationScreen::new);
        event.register(ModContainers.BLUEPRINT_WORKBENCH.get(), BlueprintWorkbenchScreen::new);
        event.register(ModContainers.AMMO_BOX.get(), AmmoBoxScreen::new);
        //event.register(ModContainers.BASIC_TURRET_CONTAINER.get(), BasicTurretScreen::new);
    }

    private static void registerBlockEntityRenderers() {
        //BlockEntityRenderers.register(ModTileEntities.BASIC_TURRET.get(), BasicTurretRenderer::new);
    }

    // PORT-DROPPED: onScreenInit(ScreenEvent.Init.Post) - only reflected into MouseSettingsScreen's options list (SRG field name
    //   f_96218_) and never used the result (the addSmall call was commented out in the original).

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.screen == null && event.getAction() == GLFW.GLFW_PRESS) {
            if (KeyBinds.KEY_ATTACHMENTS.isDown() && mc.player.getMainHandItem().getItem() != ModItems.FINGER_GUN.get()) {
                // PORT-DROPPED: Superpower Allstars (devil fruit) branch
                PacketHandler.getPlayChannel().sendToServer(new C2SMessageAttachments());
            }
            /*else if(event.getKey() == GLFW.GLFW_KEY_KP_9)
            {
                mc.setScreen(new EditorScreen(null, new Debug.Menu()));
            }*/
        }
    }

    public static void onRegisterReloadListener(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((ResourceManagerReloadListener) manager -> {
            PropertyHelper.resetCache();
        });
    }

    public static void registerAdditional(ModelEvent.RegisterAdditional event)
    {
        event.register(new ModelResourceLocation(Reference.id("special/test"), "standalone")); // PORT-VERIFY: standalone ModelResourceLocation
    }

    public static void onRegisterCreativeTab(IEventBus bus)
    {
        DeferredRegister<CreativeModeTab> register = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MOD_ID);
        CreativeModeTab.Builder builder = CreativeModeTab.builder();
            builder.title(Component.translatable("itemGroup." + Reference.MOD_ID));
            builder.icon(() -> {
                ItemStack stack = new ItemStack(ModItems.ASSAULT_RIFLE.get());
                StackData.putBoolean(stack, "IgnoreAmmo", true);
                return stack;
            });
            builder.displayItems((flags, output) ->
            {
                ModItems.REGISTER.getEntries().forEach(registryObject ->
                {
                    if(registryObject.get() instanceof GunItem item)
                    {

                        ItemStack stack = new ItemStack(item);
                        StackData.putInt(stack, "AmmoCount", ttv.migami.jeg.util.MagazineGuns.enabled(stack) ? 0 : item.getGun().getReloads().getMaxAmmo());
                        if (!stack.is(ModItems.ABSTRACT_GUN.get())) output.accept(stack);
                        return;
                    }
                    if(registryObject.get().asItem() == ModBlocks.DYNAMIC_LIGHT.get().asItem() || registryObject.get().asItem() == ModBlocks.BRIGHT_DYNAMIC_LIGHT.get().asItem()) {
                        return;
                    }
                    if(registryObject.get() instanceof FlareItem)
                    {
                        ItemStack flareStack = new ItemStack(ModItems.FLARE.get());
                        flareStack.set(DataComponents.CUSTOM_NAME, Component.translatable("item.jeg.raid_flare").withStyle(style -> style.withColor(ChatFormatting.RED).withItalic(false)));
                        StackData.putBoolean(flareStack, "HasRaid", true);
                        output.accept(flareStack);
                    }
                    if(registryObject.get() instanceof SpawnEggItem) {
                        return;
                    }
                    output.accept(registryObject.get());
                });
                CustomGunManager.fill(output);
                // PORT: EnchantmentCategory is gone (1.21 enchantments are data-driven). Every jeg enchantment (all "#jeg:enchantable/gun")
                // is listed, which matches the original GUN / SEMI_AUTO_GUN categories (no vanilla enchantment was ever in them).
                HolderLookup.RegistryLookup<Enchantment> enchantments = flags.holders().lookupOrThrow(Registries.ENCHANTMENT);
                enchantments.listElements().forEach(holder ->
                {
                    if(holder.key().location().getNamespace().equals(Reference.MOD_ID))
                    {
                        output.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(holder, holder.value().getMaxLevel())), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
                    }
                });
            });
        register.register("creative_tab", builder::build);
        register.register(bus);
        onRegisterDataGunsTab(bus);
        onRegisterGunnersTab(bus);
    }

    public static void onRegisterDataGunsTab(IEventBus bus)
    {
        DeferredRegister<CreativeModeTab> register = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MOD_ID);
        CreativeModeTab.Builder builder = CreativeModeTab.builder();
        builder.title(Component.translatable( "itemGroup." + Reference.MOD_ID + ".data"));
        builder.icon(() -> {
            ItemStack stack = new ItemStack(ModItems.ABSTRACT_GUN.get());
            StackData.putString(stack, "GunId", "vindicator_smg");
            StackData.putBoolean(stack, "IgnoreAmmo", true);
            return stack;
        });
        builder.displayItems((flags, output) -> {
            NetworkGunManager.clientConfigGunIDs.forEach((id, gun) -> {
                ItemStack stack = GunItem.makeGunStack(id);
                if (gun != null) {
                    StackData.putInt(stack, "AmmoCount", gun.getReloads().getMaxAmmo());
                }
                output.accept(stack);
            });
        });
        register.register("data_guns_tab", builder::build);
        register.register(bus);
    }

    public static void onRegisterGunnersTab(IEventBus bus)
    {
        DeferredRegister<CreativeModeTab> register = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MOD_ID);
        CreativeModeTab.Builder builder = CreativeModeTab.builder();
        builder.title(Component.translatable("itemGroup." + Reference.MOD_ID + ".gunners"));
        builder.icon(() -> {
            ItemStack stack = new ItemStack(ModItems.GUNNER_ZOMBIE_SPAWN_EGG.get());
            return stack;
        });
        builder.displayItems((flags, output) ->
        {
            ModItems.REGISTER.getEntries().forEach(registryObject ->
            {
                if(registryObject.get() instanceof SpawnEggItem) {
                    output.accept(registryObject.get());
                }
            });
        });
        register.register("gunners_tab", builder::build);
        register.register(bus);
    }

    public static Screen createEditorScreen(IEditorMenu menu) {
        return new EditorScreen(Minecraft.getInstance().screen, menu);
    }

    /* Uncomment for debugging headshot hit boxes */

    /*@SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void onRenderLiving(RenderLivingEvent.Post event)
    {
        LivingEntity entity = event.getEntity();
        IHeadshotBox<LivingEntity> headshotBox = (IHeadshotBox<LivingEntity>) BoundingBoxManager.getHeadshotBoxes(entity.getType());
        if(headshotBox != null)
        {
            AxisAlignedBB box = headshotBox.getHeadshotBox(entity);
            if(box != null)
            {
                WorldRenderer.drawBoundingBox(event.getMatrixStack(), event.getBuffers().getBuffer(RenderType.getLines()), box, 1.0F, 1.0F, 0.0F, 1.0F);

                AxisAlignedBB boundingBox = entity.getBoundingBox().offset(entity.getPositionVec().inverse());
                boundingBox = boundingBox.grow(Config.COMMON.gameplay.growBoundingBoxAmount.get(), 0, Config.COMMON.gameplay.growBoundingBoxAmount.get());
                WorldRenderer.drawBoundingBox(event.getMatrixStack(), event.getBuffers().getBuffer(RenderType.getLines()), boundingBox, 0.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }*/
}
