# JEG 1.20.1 Forge -> 1.21.1 NeoForge porting guide

Target: Minecraft 1.21.1, NeoForge 21.1.x, Java 21, Parchment 2024.11.17 names, GeckoLib 4.8.3,
MrCrayfish Framework 0.13.x (kept - it provides the network + synced-data + menu-data APIs).
Goal: keep the ORIGINAL mod's behaviour (gun stats, crafting workbenches, gunner AI, etc.). Do not redesign.

## There is NO compiler in this sandbox
Nothing can be built here, so every line must be right by inspection. Sources of truth, in order:
1. `/home/claude/jeg-ref/port/src/main/java` - a DIFFERENT mod-port of the same mod that DOES compile on 1.21.1 NeoForge.
   Its classes are restructured, but its API usage is proven: `grep -rn "SomeClass\|someMethod" /home/claude/jeg-ref/port/src/main/java`
   before you invent a signature. Prefer copying a proven call shape over writing from memory.
2. `/home/claude/jeg-ref/framework` - Framework 1.21.1 source (`common/src/main/java/com/mrcrayfish/framework/...`).
3. Original sources (read-only): `/home/claude/jeg-ref/orig/src/main/java`. The working tree (`/home/claude/jeg-port`) starts as a copy.
4. Your own knowledge of 1.21.1 / NeoForge 21.1 (least reliable: names changed a lot).
If you cannot confirm an API from 1-3, use your best guess and mark the line `// PORT-VERIFY: <what you were unsure about>`.
Never leave Forge-only code in place silently. Never invent classes that don't exist.

## Rules
- Edit ONLY files you own (your slice). To fix something outside your slice, write it in your notes file instead.
- Keep the package + class names. Keep PUBLIC method signatures stable for other slices; only change types that must change
  (Forge->Neo types, FriendlyByteBuf->RegistryFriendlyByteBuf, etc.). If you must change a public signature, add the new
  one and note it in `docs/notes/<slice>.md` ("API changes other slices must know").
- Dropped features (already removed from the tree): datagen (`datagen/`), JEI (`jei/`). Optional-mod compat for Controllable,
  Shoulder Surfing, Valkyrien Skies, Guard Villagers, Recruits, Villager Gunners, Death Angels, Migamigos, Superpower Allstars etc.
  is DROPPED: delete the compat code/branches that reference those mods (mark `// PORT-DROPPED: <mod>`), keep vanilla behaviour.
- Remove any reference (imports/calls) to deleted classes in your own files.
- Do not reformat unrelated code. Keep original comments/credits (this is GPL code by MigaMi/MrCrayfish).
- Use `Reference.id("path")` for `jeg:path` ResourceLocations; otherwise `ResourceLocation.fromNamespaceAndPath/parse/withDefaultNamespace`.
- Run `python3 -I /home/claude/jeg-port/tools/check_port.py <your dirs/files>` until BANNED and INTERNAL are empty and every
  UNSEEN import has been double-checked (UNSEEN = never used by the compiling port; maybe fine, maybe renamed - verify by memory
  or add PORT-VERIFY).

## Project conventions (shared by all slices)
**Item NBT -> `ttv.migami.jeg.util.StackData`** (already written; wraps DataComponents.CUSTOM_DATA):
`hasTag()` -> `StackData.has(s)`; nullable `getTag()` -> `StackData.getOrNull(s)`; read via `getOrCreateTag()` -> `StackData.get(s)`
(a COPY); every WRITE -> `StackData.edit(s, tag -> {...})` or `StackData.set(s, tag)`; `removeTagKey` -> `StackData.removeKey`.
`ItemStack.save(...)`/`ItemStack.of(tag)` -> `ItemStack.save(registries)` / `ItemStack.parse(registries, tag)` (Optional).
Vanilla data that has real components (damage, enchantments, custom name, lore, dyed colour, attribute modifiers) must use the
real component API. Property-override model predicates (`ItemProperties.register`) read via StackData.

**Registries**: `DeferredRegister.create(Registries.X / BuiltInRegistries.X, Reference.MOD_ID)`; items via `DeferredRegister.Items`
(`DeferredItem`); `RegistryObject<T>` -> `DeferredHolder<R, T>` (or `Supplier<T>`); `ForgeRegistries.X` -> `BuiltInRegistries.X`.
Keep the original static field names in `init/*` so other slices keep compiling. Register on the MOD bus in the mod constructor:
`public JustEnoughGuns(IEventBus modBus, ModContainer container)`.

**Networking (Framework 1.21.1)** - messages are no longer `PlayMessage<T>` subclasses. Template (keep the original fields/ctors):
```java
public record/class C2SMessageAim(boolean aiming) {   // or class with fields
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SMessageAim> STREAM_CODEC =
        StreamCodec.of((buf, msg) -> encode(msg, buf), C2SMessageAim::decode);
    public static void encode(C2SMessageAim m, RegistryFriendlyByteBuf buf) { buf.writeBoolean(m.aiming); }
    public static C2SMessageAim decode(RegistryFriendlyByteBuf buf) { return new C2SMessageAim(buf.readBoolean()); }
    public static void handle(C2SMessageAim m, MessageContext ctx) {
        ctx.execute(() -> { ServerPlayer p = (ServerPlayer) ctx.getPlayer().orElse(null); ... });
        ctx.setHandled(true);
    }
}
```
Old encode/decode bodies carry over (RegistryFriendlyByteBuf extends FriendlyByteBuf) except `writeItem/readItem`
(-> `ItemStack.OPTIONAL_STREAM_CODEC.encode/decode(buf, ...)`) and NBT/ResourceLocation helpers that still exist.
`MessageContext#getPlayer()` is `Optional<Player>`. Registration (in `PacketHandler.init()`, called from FMLCommonSetupEvent):
`FrameworkAPI.createNetworkBuilder(Reference.id("play"), 1).registerPlayMessage("aim", C2SMessageAim.class, C2SMessageAim.STREAM_CODEC, C2SMessageAim::handle, PacketFlow.SERVERBOUND)...build()`.
`PacketHandler.getPlayChannel().sendToServer/sendToPlayer(() -> p, msg)/sendToTrackingEntity(() -> e, msg)...` keep working.
Direction enum `MessageDirection.PLAY_SERVER_BOUND` -> `PacketFlow.SERVERBOUND` (`net.minecraft.network.protocol.PacketFlow`).
Synced entity data (`ModSyncedDataKeys`) -> Framework `SyncedDataKey.builder(SyncedClassKey.X, Serializers...)...` - read the Framework source.

**Events**: `net.minecraftforge.eventbus.api.SubscribeEvent` -> `net.neoforged.bus.api.SubscribeEvent`;
`@Mod.EventBusSubscriber` -> check how the compiling port declares it (`net.neoforged.fml.common.EventBusSubscriber`, `bus = Bus.MOD/GAME`, `value = Dist.CLIENT`).
`MinecraftForge.EVENT_BUS` -> `NeoForge.EVENT_BUS`. Renames to expect: TickEvent.* -> `ClientTickEvent.Pre/Post`, `PlayerTickEvent.Pre/Post`,
`ServerTickEvent`, `LevelTickEvent`, `RenderFrameEvent`; `LivingHurtEvent` -> `LivingIncomingDamageEvent` / `LivingDamageEvent.Pre/Post`;
`LivingSetAttackTargetEvent` -> `LivingChangeTargetEvent`; `RenderGuiOverlayEvent` -> `RenderGuiLayerEvent`; `RegisterGuiOverlaysEvent` -> `RegisterGuiLayersEvent`;
`MobSpawnEvent.FinalizeSpawn` -> `FinalizeSpawnEvent`; `InputEvent.MouseButton.Pre`, `RegisterClientExtensionsEvent` (IClientItemExtensions), `RegisterMenuScreensEvent`,
`EntityRenderersEvent.*`, `RegisterParticleProvidersEvent`, `RegisterKeyMappingsEvent`, `ModelEvent.*`, `AddReloadListenerEvent`, `RegisterCommandsEvent`.
Always grep the port for the exact package/name.

**Entities**: `defineSynchedData(SynchedEntityData.Builder)`; spawn data via `IEntityWithComplexSpawn` (`RegistryFriendlyByteBuf`) and
`getAddEntityPacket(ServerEntity)`; `EntityType.Builder#build(String)`; attributes via `EntityAttributeCreationEvent`; attribute modifiers use
`ResourceLocation` ids (no UUIDs). `Entity#hurt(DamageSource,float)` unchanged; `DamageSource` needs a `Holder<DamageType>` from registry access
(damage types are data-driven; JSON in `data/jeg/damage_type`). Enchantments are DATA-DRIVEN (`Holder<Enchantment>`, JSON in `data/jeg/enchantment`;
levels via `EnchantmentHelper.getItemEnchantmentLevel(holder, stack)`; get holders from `level.registryAccess()`).
Item tooltips: `appendHoverText(ItemStack, Item.TooltipContext, List<Component>, TooltipFlag)`. Durability: `stack.hurtAndBreak(int, LivingEntity, EquipmentSlot)`.
Recipes: `Recipe<RecipeInput>`, `RecipeSerializer` has `MapCodec<T> codec()` + `StreamCodec<RegistryFriendlyByteBuf,T> streamCodec()`;
`assemble(Input, HolderLookup.Provider)`; `getResultItem(HolderLookup.Provider)`. Result JSON uses `"id"` not `"item"`.
Menus: `IMenuTypeExtension.create((id, inv, buf) -> ...)` or Framework's menu-with-data API; `player.openMenu(provider, buf -> ...)`.
Particles: `ParticleOptions` needs `MapCodec` + `StreamCodec`. Sounds: `SoundEvent.createVariableRangeEvent(Reference.id("x"))`.
Capabilities (rare): rewritten, `Capabilities.ItemHandler.*`; no LazyOptional.

**Client/rendering (1.21 changed a lot)**: `Tesselator.getInstance().begin(mode, format)` returns a `BufferBuilder`; finish with
`BufferUploader.drawWithShader(buf.buildOrThrow())`; vertex calls are `addVertex(...)`, `.setColor(...)`, `.setUv(...)`, `.setOverlay`, `.setLight`,
`.setNormal` (no `endVertex`). `partialTick` now comes from `DeltaTracker` (`getGameTimeDeltaPartialTick(false)`); `Minecraft#getFrameTime` is gone
(`minecraft.getTimer()` -> `getTimer()` is `DeltaTracker` in 1.21.1 - verify in port). `Screen#renderBackground` takes 4 args.
GeckoLib: copy the proven 4.8.3 patterns from the port (GeoItem, GeoItemRenderer, AnimatableManager.ControllerRegistrar, RawAnimation, GeoEntity...).
GUI overlays are now layers (`RegisterGuiLayersEvent`, `LayeredDraw.Layer` with `(GuiGraphics, DeltaTracker)`).

**Resources (slice "resources" only)**: data dirs are singular (`recipe`, `loot_table`, `advancement`, `tags/item`, `tags/block`, `tags/entity_type`, `structure`);
biome modifiers live in `data/<ns>/neoforge/biome_modifier`, global loot modifiers in `data/neoforge/loot_modifiers/global_loot_modifiers.json`;
recipe results `{"id": ..., "count": ...}`; pack_format 34.

## Output of each slice
1. The ported files, in place. 2. `docs/notes/<slice>.md`: (a) public API changes others must know, (b) list of every `PORT-VERIFY`/`PORT-DROPPED`
with file:line, (c) things outside your slice you noticed (e.g. "ModItems needs X"), (d) honest list of files you did NOT finish.
3. Final chat reply: <= 150 words, only facts. Do not claim it compiles - nothing here can compile.

## RESUME PROTOCOL (agents get cut off by usage limits - this is expected)
You may be a retry of an agent that was cut off. Files in your slice may already be partly or fully ported by the previous attempt.
1. FIRST run `cd /home/claude/jeg-port && git status --short <your dirs>` and `python3 -I tools/check_port.py <your dirs>`.
   A file with no BANNED hits and a diff against the baseline is probably done - skim it quickly, don't redo it. Files with BANNED hits or no diff still need porting.
   (`git diff <file>` shows what the previous attempt changed; baseline original is also at /home/claude/jeg-ref/orig.)
2. Port file by file and SAVE each file immediately when finished (never batch edits in your head).
3. Do the biggest/most central files of your slice first; cheap leaf files last.
4. Skip re-reading the whole guide's API essay if you already know it; spend tokens on porting, not on exploration.
5. Always update `docs/notes/<slice>.md` incrementally (create it early; append "done: file" lines and PORT-VERIFY items as you go), so a cut-off leaves a usable record.
