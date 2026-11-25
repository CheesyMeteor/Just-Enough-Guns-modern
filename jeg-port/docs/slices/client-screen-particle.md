# Slice: client-screen-particle (notes file: docs/notes/client-screen-particle.md)
J = src/main/java/ttv/migami/jeg. You own exactly: J/client/screen/** (14 files), J/client/particle/** (27 files).
- Screens: AbstractContainerScreen renderBg unchanged; render(GuiGraphics,int,int,float) must call renderBackground(gg, mx, my, pt) (4 args) then super.render then renderTooltip; Checkbox builder; mouseScrolled has 4 params; GuiGraphics.blit common overloads unchanged.
  Menu screens registered via RegisterMenuScreensEvent elsewhere: keep ctors (Menu, Inventory, Component). Forge config GUI factories: drop/adapt per the port (FrameworkClientAPI), minimal.
- Particles: TextureSheetParticle/SpriteSet, ParticleProvider.createParticle unchanged, render(VertexConsumer, Camera, float) uses addVertex/setUv/setColor/setLight; descriptors stay in assets/jeg/particles. Providers registered via RegisterParticleProvidersEvent elsewhere: keep provider class names + (SpriteSet) ctors.
- Dropped compat (Controllable, JEI, ...): remove (PORT-DROPPED). Item NBT -> StackData. Calls into other client classes keep existing public names.
