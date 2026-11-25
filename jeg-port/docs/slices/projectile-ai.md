# Slice: projectile-ai (notes file: docs/notes/projectile-ai.md)
J = src/main/java/ttv/migami/jeg. You own exactly: J/entity/projectile/**, J/entity/throwable/**, J/entity/*.java (4 top-level files), J/entity/ai/** (AIGunEvent, AIType, EntityHurtByTargetGoal, GunAttackGoal, ...).
NOT yours: entity/monster, entity/animal, entity/client, faction (mobs-faction).
- IMPORTANT for the end user: the ORIGINAL gunner AI is simple (no block placing/breaking, no forced huge follow range). Port it faithfully as-is. Add no features.
- Entities: defineSynchedData(SynchedEntityData.Builder); ItemStack NBT in save/read uses registry access; spawn data via IEntityWithComplexSpawn (RegistryFriendlyByteBuf), drop Forge NetworkHooks packet overrides.
- ProjectileEntity: ProjectileUtil, EntityHitResult, ClipContext, DamageSource from the registry (ModDamageTypes helper names stay), enchantment via Holders, gun/ammo data via StackData + Gun classes (common-util keeps API stable).
- AI goals: Goal API same; check Mob API renames. GunAttackGoal and AIGunEvent are most important - keep logic as-is where APIs allow.
- Throwables: ThrowableItemProjectile ctors/getDefaultItem; explosion API changes (Level.explode, ExplosionInteraction); ItemStack NBT -> StackData.
