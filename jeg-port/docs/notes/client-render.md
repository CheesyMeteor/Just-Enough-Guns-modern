# client-render slice notes (J/client/render/**)

## Status: all 72 files handled. Nothing compiled (no compiler); check_port.py: BANNED only false positives ("ClientTickEvent.Post"/"PlayerTickEvent.Pre" match the TickEvent regex).

## done
- gun/animated/{AnimatedGunRenderer,AnimatedGunModel,AttachmentRenderer,AttachmentRenderType,MuzzleFlashRenderType}
- GuiRenderer (RenderGuiOverlayEvent -> RenderGuiLayerEvent.Post, VanillaGuiLayers.SPYGLASS/HOTBAR), gun/ModelOverrides (PlayerTickEvent.Pre, client side only), crosshair/* (1.21 Tesselator/BufferBuilder)
- entity/DynamicHelmetRenderer (ArmorMaterial holder, DyedItemColor, ARGB colour ints)
- pose/{OneHandedPose,TwoHandedPose,WeaponPose} (getTimer partial tick, PlayerSkin.Model.SLIM), Dist/OnlyIn -> net.neoforged.api.distmarker (all files)
- gun/model/* (only getFrameTime -> getTimer().getGameTimeDeltaPartialTick(false))
- reviewed, no change needed: IHeldAnimation, IOverrideModel, entity/{BeamRenderer,BooRenderer,GhoulRenderer,GrenadeRenderer,MissileRenderer,ProjectileRenderer,RaidEntityRenderer,TerrorRaidEntityRenderer,ThrowableGrenadeRenderer}, pose/{AimPose,LimbPose,BazookaPose,MiniGunPose}, block/BasicTurretRenderer (fully commented out)
- `new ResourceLocation(Reference.MOD_ID, x)` -> `Reference.id(x)` everywhere.

## (a) public API changes
- AnimatedGunRenderer#renderRecursively now takes `int colour` (GeckoLib 4.8.3); getRenderColor returns software.bernie.geckolib.util.Color.
- AnimatedGunRenderer#onTick takes ClientTickEvent.Post. AttachmentRenderer ctor takes GeoRenderer<AnimatedGunItem>.
- ModelOverrides#onClientPlayerTick takes PlayerTickEvent.Pre.
- Constructors/class names unchanged (AnimatedGunRenderer(ResourceLocation) is still what AnimatedGunItem#createGeoRenderer calls).

## (b) PORT-VERIFY
- GuiRenderer: import net.neoforged.neoforge.client.gui.VanillaGuiLayers (package unverified); `Rarity#color()` accessor.
- DynamicHelmetRenderer: `armorItem.getMaterial() == ArmorMaterials.LEATHER` holder identity; `ArmorMaterial.Layer#texture(boolean)`.
- AnimatedGunRenderer: GeckoLib hook signatures copied from the reference port's AnimatedGunRenderer (renderRecursively, util.RenderUtil, util.Color); ModelPart#render(..., int colour) packed -1.
- AnimatedGunModel: ClientUtils.getClientPlayer replaced by Minecraft.getInstance().player (null-safe).
- Behaviour kept from original: AnimatedGunRenderer.get() constructs with a null `customPath` (original quirk).

## PORT-DROPPED
- none in this slice (no Controllable/Shoulder Surfing code lived here).

## (c) outside my slice
- AnimatedGunRenderer has @SubscribeEvent instance methods (onTick, onGunFire(GunFireEvent.Post)); something in client-core must register the instance on NeoForge.EVENT_BUS (originally MinecraftForge.EVENT_BUS.register(AnimatedGunRenderer.get())). Not found in current tree.
- AnimatedGunItem still imports software.bernie.geckolib.util.ClientUtils (should be ClientUtil in 4.8.x; PORT-VERIFY by items slice).
- Entity renderers: all already registered in JustEnoughGuns.ClientInit and client/GunEntityRenderers; nothing registered here. GunItem plain renderer is in item/ItemClientExtensions (not touched).
- Bullet trails: BulletTrailRenderEvent in events slice (not duplicated).
- GunEnchantmentHelper.getEnchantmentLevel(stack, key) used for ATLANTIC_SHOOTER/INFINITY (util slice).

## (d) not finished
- Nothing skipped. Gun/model/*Model classes rely on client/SpecialModels + client/util/RenderUtil (client-core) keeping their current signatures.
