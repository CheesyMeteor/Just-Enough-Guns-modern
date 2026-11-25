# Slice: common-util (notes file: docs/notes/common-util.md)
J = src/main/java/ttv/migami/jeg. You own exactly: J/common/*.java (AmmoContext, Attachments, BoundingBoxManager, ChargeTracker, CustomGun, CustomGunLoader, DelayedTask, FireMode, GripType, Gun, GunModifiers,
JsonDeserializers, NetworkGunManager, ProjectileManager, ReloadTracker, ReloadType, ShootTracker, SpreadTracker), J/common/headshot/*, J/common/network/* (ServerPlayHandler = server-side gun logic), J/common/properties/*,
J/util/* EXCEPT util/StackData.java. NOT yours: J/common/container/** (items-crafting).
- Heart of the mod. Keep every public method signature stable. Gun/ammo/attachment item NBT -> StackData. Gun JSON (data/jeg/guns/*.json) schema is unchanged: parse it the same way.
- NetworkGunManager/CustomGunLoader: reload listeners + client sync. Framework's login-data API (ILoginData) is gone. MANDATORY design: register via AddReloadListenerEvent (SimpleJsonResourceReloadListener or equivalent;
  1.21 `apply(Map<ResourceLocation, JsonElement>, ResourceManager, ProfilerFiller)`), and send `S2CMessageUpdateGuns` (owned by init-net; keep ctor/getters) to players from NeoForge's `OnDatapackSyncEvent`
  (`event.getRelevantPlayers()`, `PacketHandler.getPlayChannel().sendToPlayer(() -> player, msg)`). Client receiving side lives in J/client/network (other agent): keep the receiving-side methods public and stable; document them.
- Forge ModList/FMLLoader -> `net.neoforged.fml.ModList` / `FMLEnvironment.dist`.
- util/*: port what's needed; delete utilities that exist only for dropped optional-mod compat (PORT-DROPPED) and list deleted classes in notes.
- DamageSource creation goes through ModDamageTypes helpers (init-net; keep calling the original helper names).
