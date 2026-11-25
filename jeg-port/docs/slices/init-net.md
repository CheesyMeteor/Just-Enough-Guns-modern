# Slice: init-net (notes file: docs/notes/init-net.md)
J = src/main/java/ttv/migami/jeg. You own exactly: J/JustEnoughGuns.java, J/Config.java, J/Reference.java (Reference.id exists; keep),
J/init/* (all 16 files), J/network/** (PacketHandler, BufferUtil, all 37 messages), J/annotation/*, J/cache/*, J/interfaces/*.
- Mod class: `public JustEnoughGuns(IEventBus modBus, ModContainer container)`: register every DeferredRegister on the mod bus, register configs
  (ForgeConfigSpec -> ModConfigSpec via container.registerConfig), wire FMLCommonSetupEvent (PacketHandler.init(), Framework synced data keys) and
  FMLClientSetupEvent. See how /home/claude/jeg-ref/port wires its main class. Client entry points live in J/client/** (other agents): keep the original
  call targets (e.g. ClientHandler.setup()) and list the exact ones you call in notes.
- Keep every static field name in init/* (ModItems.X.get(), ModEntities.X.get(), ModSounds.X...). RegistryObject -> DeferredHolder/DeferredItem/Supplier. Creative tab registration moves here.
- ModEnchantments: enchantments are data-driven. Provide ResourceKey<Enchantment> constants (old field names) + helper(s) resolving Holder<Enchantment> from a RegistryAccess/Level.
  The "events-misc" slice writes behaviour + JSON.
- ModDamageTypes: ResourceKey<DamageType> constants + helpers creating DamageSource from a Level's registryAccess (keep existing helper names). JSON exists in data/jeg/damage_type.
- ModRecipeSerializers/ModRecipeTypes: keep names; serializers are implemented in J/crafting/** by "items-crafting".
- ModContainers: menus per guide (Framework menu-with-data or IMenuTypeExtension; check the port).
- ModSyncedDataKeys: Framework 0.13 SyncedDataKey builder (read /home/claude/jeg-ref/framework).
- Network: convert all 37 messages with the guide template; keep fields/ctors so callers compile. S2CMessageUpdateGuns keeps carrying the gun data; "common-util" sends it from
  OnDatapackSyncEvent (NetworkGunManager) - keep ctor/getters unless data types force a change (document). S2C handlers calling client classes (ClientPlayHandler etc.) keep the same client
  method names; use the dist-safe pattern the port uses.
- Drop (PORT-DROPPED) init entries/commands for dropped optional-mod compat.
