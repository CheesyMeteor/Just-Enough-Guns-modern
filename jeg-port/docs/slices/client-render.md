# Slice: client-render (notes file: docs/notes/client-render.md)
J = src/main/java/ttv/migami/jeg. You own exactly: J/client/render/** (72 files).
NOT yours: client/handler, client/*.java, client/network (client-core), client/screen + client/particle (client-screen-particle), entity/client (mobs-faction).
- Vertex API: Tesselator.getInstance().begin(mode, format) -> BufferBuilder; addVertex/setColor/setUv/setOverlay/setLight/setNormal; buildOrThrow; BufferUploader.drawWithShader; packed int colours; RenderSystem.setShader(GameRenderer::getPositionTexShader) etc.;
  ModelResourceLocation.standalone/inventory; RenderType factories; JOML Matrix4f/Quaternionf (Axis.XP.rotationDegrees).
- GeckoLib 4.8.3: copy proven patterns from /home/claude/jeg-ref/port (GeoItemRenderer, GeoModel, GeoRenderLayer, GeoEntityRenderer, GeoBlockRenderer, RenderUtils, DefaultedItemGeoModel). Render hook signatures changed since 4.4 (packed int colour in actuallyRender etc.).
- Player/Humanoid layers (ItemInHandLayer, PlayerModel, HumanoidModel poses): RenderLayer.render signature same; ItemInHandRenderer changes.
- Dropped compat (Controllable, Shoulder Surfing, Valkyrien Skies): remove (PORT-DROPPED). Item NBT -> StackData. Calls into client-core classes keep current public names.
