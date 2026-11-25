# Notes: client-core slice
## Progress (done: files)
- done: client/SpecialModels (standalone ModelResourceLocation, neoforge events), client/CustomGunManager (LoginData removed, ItemStackNbt/StackData)
- done: client/{ClientHandler,ClientSideCache,DynamicTextureLocator,GunEntityRenderers,GunItemStackRenderer,KeyBinds,MetaLoader,ParticleFactoryRegistry}, client/network/ClientPlayHandler (all public static names/params unchanged: handleMessageGunSound, handleMessageBlood, handleExplosiveAmmo, handleMessageBulletTrail, handleExplosionStunGrenade, handleExplosionSmokeGrenade, handleLaser, handleProjectileHitBlock, handleProjectileHitEntity, handleRemoveProjectile, handleUpdateGuns), client/audio/* (unchanged-OK), GunModel/GunRenderType/BulletTrail/DotRenderMode/SwayType (unchanged-OK)
- DELETED (PORT-DROPPED Controllable): client/GunButtonBindings, client/GunConflictContext, client/handler/ControllerHandler
- done: client/handler/{AimingHandler,BulletTrailRenderingHandler (vertex(...) public signature: (Matrix4f,Matrix3f) -> PoseStack.Pose),CrosshairHandler (pose push/pop imbalance fixed),FlashlightHandler (Pre+Post, local player only)}
- done: client/handler/{GunRecoilHandler (RenderFrameEvent.Post; Shoulder Surfing camera block PORT-DROPPED),InspectHandler,MeleeHandler,PlayerModelHandler}
- done: client/handler/ReloadHandler
- done: client/handler/ShootingHandler (Shoulder Surfing/Controllable branches PORT-DROPPED)
- done: client/handler/SoundHandler (PORT-VERIFY SoundEngine field name instanceToChannel via ObfuscationReflectionHelper)
- done: client/handler/GunRenderingHandler (equip-progress reflection replaced by RenderHandEvent#getEquipProgress; HUD indicator sprites hud/crosshair_attack_indicator_* PORT-VERIFY; RenderFrameEvent.Post for the cook/cooldown bar; muzzle flash uses addVertex/setUv/setLight)
- done: client/util/RenderUtil (IClientItemExtensions -> net.neoforged; renderQuad/addVertex now take PoseStack.Pose instead of Matrix4f+Matrix3f; getSkin().texture(); putBulkData 8-arg PORT-VERIFY). ClientHandler item colours made opaque (ARGB in 1.21).
- done: client/util/{PropertyHelper,RecipeHelper},Easings/FPSUtil unchanged-OK, client/settings/* (all commented out, unchanged-OK), client/medal/* (MedalManager RL ctor; shader colour reset)

## (a) API changes other slices must know
- DELETED: client/GunButtonBindings, client/GunConflictContext, client/handler/ControllerHandler (Controllable dropped). No other file references them any more.
- BulletTrailRenderingHandler#vertex(...): `(Matrix4f, Matrix3f)` params -> single `PoseStack.Pose` (internal use only).
- RenderUtil#renderQuad / #addVertex: `(Matrix4f, Matrix3f)` -> `PoseStack.Pose` (only used inside RenderUtil; renderBeam/renderPart/renderMacerateWheel signatures unchanged).
- SpecialModels now holds `ModelResourceLocation(..., "standalone")` and registers via ModelEvent.RegisterAdditional; `getModel()` unchanged. ClientHandler#registerAdditional likewise.
- ClientPlayHandler: every public static handler name + params unchanged (handleMessageGunSound, handleMessageBlood, handleExplosiveAmmo, handleMessageBulletTrail, handleExplosionStunGrenade, handleExplosionSmokeGrenade, handleLaser, handleProjectileHitBlock, handleProjectileHitEntity, handleRemoveProjectile, handleUpdateGuns).
- ClientHandler: addPack and onScreenInit removed (were no-ops); item colour handler now returns opaque ARGB (1.21 item colours are ARGB; stored "Color" is RGB).
- ClientHandler creative tab: EnchantmentCategory is gone; books are listed for every enchantment in namespace `jeg` (`flags.holders()` lookup).
- GunRenderingHandler no longer reflects into ItemInHandRenderer (uses RenderHandEvent#getEquipProgress). BulletTrailRenderEvent (events-misc) remains the only caller of BulletTrailRenderingHandler#render.
- Handlers are still registered by ClientHandler#setup via NeoForge.EVENT_BUS.register(instance) (AimingHandler, BulletTrail..., CrosshairHandler, GunRenderingHandler, GunRecoilHandler, ReloadHandler, ShootingHandler, SoundHandler, MeleeHandler, InspectHandler, FlashlightHandler, PlayerModelHandler). FlashlightHandler's methods are static + @EventBusSubscriber (as in the original). I did NOT register any IClientItemExtensions (item/ItemClientExtensions already does).
- FlashlightHandler: now only the LOCAL player sends the flashlight packet (original also fired for every other player's tick); Pre+Post both forward (original TickEvent fired in both phases).
- CrosshairHandler: pose push/pop imbalance on early returns fixed.

## (b) PORT-VERIFY / PORT-DROPPED (file:line, see grep for current numbers)
- ClientHandler:64 addPack dropped; :81 Controllable; :91 render layers (setRenderLayer gone -> block model JSON `render_type`); :99 colours registered on ItemColors directly; :196 onScreenInit dropped; :204 devil-fruit branch; :222 standalone ModelResourceLocation.
- CustomGunManager:65 LoginData dropped. SpecialModels:221 standalone model locations.
- GunRenderingHandler:71 HUD sprite ids `hud/crosshair_attack_indicator_background/progress` (+ blitSprite 9-arg overload); :160 Controllable.
- PlayerModelHandler:54 `getSkin().model() == PlayerSkin.Model.SLIM` replaces getModelName().
- FlashlightHandler:64, AimingHandler:290, ShootingHandler:162 Controllable/Shoulder Surfing; GunRecoilHandler:99 Shoulder Surfing camera.
- SoundHandler:64 SoundEngine field "instanceToChannel" by mojmap name through ObfuscationReflectionHelper (an AT would be sturdier).
- ShootingHandler:15 / InspectHandler:9 GeckoLib import `software.bernie.geckolib.animatable.GeoAnimatable` (as GunFireEvent).
- RenderUtil:250 VertexConsumer#putBulkData 8-arg (with alpha); :321 ItemBlockRenderTypes.getRenderType(BlockState, boolean); MatrixUtil.mulComponentWise + ItemRenderer.getCompassFoilBuffer* assumed unchanged in 1.21.1; ModelResourceLocation.vanilla("trident","inventory").
- Unverified-from-memory (not marked inline): RenderFrameEvent.Post#getPartialTick() is a DeltaTracker; RenderHandEvent#getEquipProgress; SimpleSoundInstance 12-arg ctor; Level#addParticle(ParticleOptions, boolean, ...) overloads; ViewportEvent.ComputeFov#usedConfiguredFov; ItemCooldowns#getCooldownPercent(Item, float).

## (c) Outside my slice
- resources: block models for SCRAP/GUNMETAL/GUNNITE workbench, SCHEMATIC_STATION, BLUEPRINT_WORKBENCH, RECYCLER, AMMO_BOX need `"render_type": "minecraft:cutout"`, DYNAMIC_LIGHT `"minecraft:translucent"` (replaces ItemBlockRenderTypes.setRenderLayer).
- resources: `assets/jeg/models/special/**` must be loadable as standalone models (path unchanged).
- JustEnoughGuns still sets `shoulderSurfingLoaded` etc. flags (harmless). compat/ShoulderSurfingHelper stubs are kept and still called from AimingHandler/CrosshairHandler (always false).
- client/render, client/screen, client/particle slices: GunRenderingHandler calls RenderUtil.renderModel/renderFirstPersonArm/applyTransformType (signatures unchanged) and Crosshair#render(mc, stack, w, h, float partialTicks) (unchanged); ClientHandler imports ModelOverrides + render/gun/model/* + all screens by name.
- GunAnimations was already ported (StackData); no edit needed.

## (d) Not finished / unverified
- Everything is unverified by a compiler. Nothing known to be skipped in the slice; client/settings/* are entirely commented-out files (unchanged).
- No Stage-2 work done.
