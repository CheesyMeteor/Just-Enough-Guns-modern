# client-screen-particle notes
## done
- client/particle/** (all 27 + phantom/): OnlyIn annotation+import removed (not used by compiling port); 1.21 vertex API (addVertex/setUv/setColor/setLight) in BloodParticle, BulletHoleParticle, LaserParticle, phantom/PhantomGunnerParticle; ScrapParticle sprite lookup.
- client/screen/**: AmmoBoxScreen, AttachmentScreen, AttachmentScreenBak (dead, unreferenced backup; ported mechanically), BlueprintWorkbenchScreen, Gunmetal/Gunnite/ScrapWorkbenchScreen, SchematicStationScreen, CheckBox, recycler/{AbstractRecyclerScreen,RecyclerScreen}. Unchanged-OK: widget/MiniButton, ButtonAlignment, BasicTurretScreen (fully commented out; checker hit is a false positive).
- Changes: ResourceLocation factories, BuiltInRegistries.ITEM, StackData for blueprint/medal/AmmoCount NBT, renderBackground(gg,mx,my,pt) 4-arg, getFrameTime -> getTimer().getGameTimeDeltaPartialTick(false), config screen via IConfigScreenFactory (as in ref port), 3D item previews now draw on `pGuiGraphics.pose()` instead of RenderSystem.getModelViewStack() (a JOML Matrix4fStack in 1.21; no applyModelViewMatrix).
- SchematicStationScreen: the odd renderBackground call that lived in renderBg moved to start of render().
- No public signatures changed (screen ctors still (Menu, Inventory, Component); particle providers still (SpriteSet)).
## PORT-VERIFY
- particle/ScrapParticle.java ~18: BakedModel#getParticleIcon(ModelData) overload.
- screen/AttachmentScreen.java ~198: Rarity colour accessor (`getRarity().color.getColor()`; may be `color()` in 1.21.1).
- screen/SchematicStationScreen.java ~123: MapItem.getSavedData(ItemStack, Level).
- All screens call renderBackground explicitly (guide + ref port do); if 1.21.1 Screen.render already draws it, background is drawn twice (cosmetic only).
## PORT-DROPPED
- none in this slice (no compat code present).
## Outside slice
- ClientHandler.java:173-180 registers screens via `MenuScreens.register(...)` - in NeoForge 1.21 prefer `RegisterMenuScreensEvent` (event.register(type, Screen::new)); verify it still works at that point of lifecycle.
- Workbench menus in init/ModContainers already use data.readBlockPos() (consistent with blocks-world.md).
- particle providers: events-misc note says ModParticleTypes custom types (BULLET_HOLE, LASER, TRAIL, COLORED_FLARE_SMOKE) still need codec/streamCodec fix; ColoredFlareSmokeParticle/TrailParticle only use getR/G/B / isEnchanted, which exist.
## Not finished
- None known; nothing could be compiled.
