# common-util notes (resumed attempt)
- done: util/ItemStackNbt.java (NEW helper: parse(CompoundTag)/save(ItemStack)/provider() replacing ItemStack.of/save in static code; others may use it)
- done: common/Gun.java (INBTSerializable dropped from Gun + inner classes; no-arg serializeNBT()/deserializeNBT(CompoundTag) kept public & unchanged; DistExecutor->FMLEnvironment.dist; attachments read via StackData + ItemStackNbt; remove*Stack now mutate via StackData.edit; Infinity via GunEnchantmentHelper.hasEnchantment)
- done: common/network/ServerPlayHandler.java (PORT-VERIFY: GunFireEvent.Pre must be ICancellableEvent; firework 1.21 component ctors in getColoredFireworkStack; AnimatedGunItem.resetTags(CompoundTag) used inside StackData.edit; player.openMenu(SimpleMenuProvider) w/o buf - AttachmentContainer client ctor takes no data)
- done: util/GunModifierHelper (PORT-VERIFY SwordItem tier dmg), DyeUtils, InventoryUtil (isSameItemSameComponents), ReflectionUtil (PORT-VERIFY mojmap name updateRedstoneOutput); reviewed unchanged-OK: Attachments, AmmoContext, GunJsonUtil, SuperBuilder, BufferUtil, ScreenUtil, GunCompositeStatHelper, BoundingBoxManager, DelayedTask (BANNED hits there are checker false positives on PlayerTickEvent.Post/ServerTickEvent.Post)
- done: properties/SightAnimation (INBTSerializable dropped, no-arg serializeNBT/deserializeNBT(CompoundTag) kept; DistExecutor->FMLEnvironment.dist); CustomGunLoader reviewed OK
- reviewed OK (already ported): NetworkGunManager, CustomGun, CustomGunLoader, JsonDeserializers, ShootTracker, SpreadTracker, ChargeTracker, ReloadTracker, ProjectileManager, GunModifiers, FireMode, ReloadType, GripType, headshot/*
## Dependencies on other slices
- GunFireEvent.Pre must implement ICancellableEvent (events slice); S2CMessageUpdateGuns needs no-arg ctor + RegistryFriendlyByteBuf use of Writer/readRegisteredGuns/readCustomGuns (init-net).
- AnimatedGunItem.resetTags(CompoundTag) must stay public; AttachmentContainer client ctor takes no buf (player.openMenu(provider) sends no extra data).
## Not finished
- None known in slice; Gun.java (2865 lines) was ported by prior attempt and only spot-checked, not re-audited.
