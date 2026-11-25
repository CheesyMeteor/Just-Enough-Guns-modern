# Slice: client-core (notes file: docs/notes/client-core.md)
J = src/main/java/ttv/migami/jeg. You own exactly: J/client/*.java (17 top-level), J/client/handler/**, J/client/network/**, J/client/util/**, J/client/settings/**, J/client/audio/**, J/client/medal/**.
NOT yours: client/render/** (client-render), client/screen/** + client/particle/** (client-screen-particle), entity/client (mobs-faction).
- Client brain: input, aiming/recoil/FOV, HUD, key mappings. Events -> ClientTickEvent.Pre/Post, RenderGuiLayerEvent.Pre/Post + VanillaGuiLayers (custom HUD via RegisterGuiLayersEvent), InputEvent.MouseButton.Pre / InputEvent.Key / InputEvent.InteractionKeyMappingTriggered,
  ComputeFovModifierEvent, ViewportEvent.*, MovementInputUpdateEvent, RenderHandEvent, RenderPlayerEvent.Pre, RenderLevelStageEvent, RegisterKeyMappingsEvent, ScreenEvent.*, ClientPlayerNetworkEvent.LoggingOut. Partial tick via DeltaTracker (check the port for the proven path).
- Settings: client config -> ModConfigSpec CLIENT registered in JustEnoughGuns (init-net): keep static spec fields; document them.
- Remove Controllable / Shoulder Surfing / Valkyrien Skies etc. branches (PORT-DROPPED).
- ClientPlayHandler: static methods called by S2C handlers (init-net): keep every public static name+params; list them in notes.
- Item NBT -> StackData everywhere (ItemProperties predicates, HUD ammo reads, tooltips).
