package ttv.migami.jeg.item;

import ttv.migami.jeg.util.NumberFormats;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLEnvironment;
import ttv.migami.jeg.Config;
import ttv.migami.jeg.JustEnoughGuns;
import ttv.migami.jeg.client.GunItemStackRenderer;
import ttv.migami.jeg.client.KeyBinds;
import ttv.migami.jeg.common.FireMode;
import ttv.migami.jeg.common.Gun;
import ttv.migami.jeg.common.NetworkGunManager;
import ttv.migami.jeg.common.ReloadType;
import ttv.migami.jeg.debug.Debug;
import ttv.migami.jeg.init.ModEnchantments;
import ttv.migami.jeg.init.ModItems;
import ttv.migami.jeg.init.ModTags;
import ttv.migami.jeg.modifier.Modifier;
import ttv.migami.jeg.modifier.type.IModifierEffect;
import ttv.migami.jeg.modifier.type.StatModifier;
import ttv.migami.jeg.modifier.type.StatType;
import ttv.migami.jeg.util.GunEnchantmentHelper;
import ttv.migami.jeg.util.GunModifierHelper;
import ttv.migami.jeg.util.StackData;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.WeakHashMap;

public class GunItem extends Item implements IColored, IMeta {
    // PORT: keyed by the CUSTOM_DATA component (stable object until the data changes) instead of the removed stack tag
    private final WeakHashMap<CustomData, Gun> modifiedGunCache = new WeakHashMap<>();
    private Gun gun = new Gun();
    private Modifier modifier = null;

    public GunItem(Item.Properties properties) {
        super(properties);
    }

    public void setGun(NetworkGunManager.Supplier supplier) {
        this.gun = supplier.getGun();
    }

    public Gun getGun() {
        return this.gun;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }

    public Modifier getModifier() {
        return this.modifier;
    }

    // Data-driven gun
    public static ItemStack makeGunStack(ResourceLocation gunId) {
        ItemStack stack = new ItemStack(ModItems.ABSTRACT_GUN.get());
        StackData.putString(stack, "GunId", gunId.toString());
        return stack;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (this.modifier != null && Config.COMMON.gameplay.gunModifiers.get()) {
            pStack.set(DataComponents.CUSTOM_NAME, pStack.getHoverName().copy().withStyle(style -> style.withColor(this.modifier.getColor()).withItalic(false)));

            /*if (pStack.getHoverName().getString().matches(Component.translatable("item." + this.getModID() + "." + this.toString()).getString())) {
                    pStack.setHoverName(Component.translatable("jeg.modifier." + modifierGroup.getName()).append(" ").append(pStack.getHoverName()));
            }*/
        }

        if (StackData.has(pStack) && !StackData.contains(pStack, "AmmoCount")) {
            StackData.putInt(pStack, "AmmoCount", 0);
        }
        if (pStack.is(ModItems.FLARE_GUN.get())) {
            if (StackData.getBoolean(pStack, "HasRaid")) {
                pStack.set(DataComponents.CUSTOM_NAME, Component.translatable("item.jeg.raid_flare_gun").withStyle(style -> style.withColor(ChatFormatting.RED).withItalic(false)));
            } else if (pStack.getHoverName().equals(Component.translatable("item.jeg.raid_flare_gun").withStyle(style -> style.withColor(ChatFormatting.RED).withItalic(false)))) {
                pStack.remove(DataComponents.CUSTOM_NAME);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag tagCompound = StackData.getOrNull(stack);
        Gun modifiedGun = this.getModifiedGun(stack);

        if (stack.is(ModItems.FLARE_GUN.get())) {
            if (tagCompound != null && tagCompound.getBoolean("HasRaid")) {
                String factionName;
                if (tagCompound != null && tagCompound.contains("Raid")) {
                    factionName = tagCompound.getString("Raid");
                } else {
                    factionName = "random";
                }
                tooltip.add(Component.translatable("info.jeg.raid_flare_gun").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD));
                tooltip.add(Component.literal(""));
                tooltip.add(Component.translatable("info.jeg.raid_flare").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD));
                tooltip.add(Component.literal(""));
                tooltip.add(Component.translatable("info.jeg.flare_raid").withStyle(ChatFormatting.GRAY)
                        .append(Component.translatable("faction.jeg." + factionName).withStyle(ChatFormatting.WHITE)));
                tooltip.add(Component.literal(""));
            } else {
                tooltip.add(Component.translatable("info.jeg.flare_gun.color").withStyle(ChatFormatting.BLUE));
            }
        }

        if (stack.getItem() != ModItems.FINGER_GUN.get()) {
            if (Screen.hasShiftDown()) {
                if (this.modifier != null && Config.COMMON.gameplay.gunModifiers.get()) {
                    tooltip.add(Component.translatable("info.jeg.modifier").withStyle(ChatFormatting.GRAY)
                            .append(Component.translatable("modifier.jeg." + this.modifier.getName()).withStyle(style -> style.withColor(this.modifier.getColor()))));
                }

                String fireMode = modifiedGun.getGeneral().getFireMode().getId().toString();
                tooltip.add(Component.translatable("info.jeg.fire_mode").withStyle(ChatFormatting.GRAY)
                        .append(Component.translatable("fire_mode." + fireMode).withStyle(ChatFormatting.WHITE)));

                Item ammo = BuiltInRegistries.ITEM.get(modifiedGun.getProjectile().getItem());
                Item reloadItem = BuiltInRegistries.ITEM.get(modifiedGun.getReloads().getReloadItem());
                if (modifiedGun.getReloads().getReloadType() == ReloadType.SINGLE_ITEM) {
                    ammo = reloadItem;
                }
                if (ammo != null) {
                    tooltip.add(Component.translatable("info.jeg.ammo_type", Component.translatable(ammo.getDescriptionId()).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GRAY));
                }

                String additionalDamageText = "";

                if (tagCompound != null) {
                    if (tagCompound.contains("AdditionalDamage", Tag.TAG_ANY_NUMERIC)) {
                        float additionalDamage = tagCompound.getFloat("AdditionalDamage");
                        additionalDamage += GunModifierHelper.getAdditionalDamage(stack);

                        if (additionalDamage > 0) {
                            additionalDamageText = ChatFormatting.GREEN + " +" + NumberFormats.ATTRIBUTE_MODIFIER_FORMAT.format(additionalDamage);
                        } else if (additionalDamage < 0) {
                            additionalDamageText = ChatFormatting.RED + " " + NumberFormats.ATTRIBUTE_MODIFIER_FORMAT.format(additionalDamage);
                        }
                    }
                }

                float damage = modifiedGun.getProjectile().getDamage();
                if (this.modifier != null && Config.COMMON.gameplay.gunModifiers.get()) {
                    float fireRateMultiplier = 1.0F;

                    for (IModifierEffect effect : this.modifier.getModifiers()) {
                        if (effect instanceof StatModifier statModifier && statModifier.getStatType() == StatType.DAMAGE) {
                            fireRateMultiplier *= statModifier.getValue();
                        }
                    }

                    damage *= fireRateMultiplier;
                }

                ResourceLocation advantage = modifiedGun.getProjectile().getAdvantage();
                damage = GunModifierHelper.getModifiedProjectileDamage(stack, damage);
                damage = GunEnchantmentHelper.getAcceleratorDamage(stack, damage);
                damage = GunEnchantmentHelper.getWitheredDamage(stack, damage);
                if (modifiedGun.getProjectile().getItem().equals(BuiltInRegistries.ITEM.getKey(Items.EMERALD))){
                    tooltip.add(Component.translatable("info.jeg.damage", ChatFormatting.WHITE + NumberFormats.ATTRIBUTE_MODIFIER_FORMAT.format(damage) + additionalDamageText)
                            .append(Component.literal(" - " + ChatFormatting.WHITE + NumberFormats.ATTRIBUTE_MODIFIER_FORMAT.format((damage * Config.COMMON.gameplay.maxResonanceLevel.get()) * 0.8) + additionalDamageText)).withStyle(ChatFormatting.GRAY));
                } else tooltip.add(Component.translatable("info.jeg.damage", ChatFormatting.WHITE + NumberFormats.ATTRIBUTE_MODIFIER_FORMAT.format(damage) + additionalDamageText).withStyle(ChatFormatting.GRAY));

                if (modifiedGun.getProjectile().getItem().equals(BuiltInRegistries.ITEM.getKey(Items.EMERALD))) {
                    tooltip.add(Component.translatable("info.jeg.resonance").withStyle(ChatFormatting.GREEN));
                    tooltip.add(Component.translatable("info.jeg.resonance_info").withStyle(ChatFormatting.GREEN));
                }

                if (!advantage.equals(ModTags.Entities.NONE.location()) && Config.COMMON.gameplay.gunAdvantage.get()) {
                    tooltip.add(Component.translatable("info.jeg.advantage").withStyle(ChatFormatting.GRAY)
                            .append(Component.translatable("advantage." + advantage).withStyle(ChatFormatting.GOLD)));
                }
            } else {
                tooltip.add(Component.translatable("info.jeg.shift_tooltip").withStyle(ChatFormatting.WHITE));
            }

            if (tagCompound != null) {
                if (tagCompound.getBoolean("IgnoreAmmo")) {
                    tooltip.add(Component.translatable("info.jeg.ignore_ammo").withStyle(ChatFormatting.AQUA));
                } else {
                    int ammoCount = tagCompound.getInt("AmmoCount");
                    tooltip.add(Component.translatable("info.jeg.ammo", ChatFormatting.WHITE.toString() + ammoCount + "/" + GunModifierHelper.getModifiedAmmoCapacity(stack, modifiedGun)).withStyle(ChatFormatting.GRAY));
                }
            }

            // PORT-DROPPED: Devil Fruits (spas) key-conflict tooltip variant "info.jeg.attachment_help_mdf"
            tooltip.add(Component.translatable("info.jeg.attachment_help", KeyBinds.KEY_ATTACHMENTS.getTranslatedKeyMessage().getString().toUpperCase(Locale.ENGLISH)).withStyle(ChatFormatting.YELLOW));

            if (this == ModItems.TYPHOONEE.get()) {
                tooltip.add(Component.translatable("info.jeg.tooltip_item.typhoonee").withStyle(ChatFormatting.GRAY));
            } else if (this == ModItems.ATLANTEAN_SPEAR.get()) {
                tooltip.add(Component.translatable("info.jeg.tooltip_item.atlantean_spear").withStyle(ChatFormatting.GRAY));
            }
            else if (this == ModItems.SOULHUNTER_MK2.get() || this == ModItems.HOLLENFIRE_MK2.get()) {
                tooltip.add(Component.translatable("info.jeg.tooltip_item.mk2_blueprint").withStyle(ChatFormatting.GRAY));
            }
            else if (this == ModItems.REPEATING_SHOTGUN.get() || this == ModItems.INFANTRY_RIFLE.get() || this == ModItems.SERVICE_RIFLE.get()) {
                tooltip.add(Component.translatable("info.jeg.tooltip_item.ww_blueprint").withStyle(ChatFormatting.GRAY));
            }
            else if (this == ModItems.SUBSONIC_RIFLE.get() || this == ModItems.SUPERSONIC_SHOTGUN.get() || this == ModItems.HYPERSONIC_CANNON.get()) {
                tooltip.add(Component.translatable("info.jeg.tooltip_item.warden_blueprint").withStyle(ChatFormatting.GRAY));
            }
            else if (this == ModItems.ROCKET_LAUNCHER.get()) {
                tooltip.add(Component.translatable("info.jeg.tooltip_item.wither_blueprint").withStyle(ChatFormatting.GRAY));
            }
            else if (this == ModItems.COMPOUND_BOW.get()) {
                tooltip.add(Component.translatable("info.jeg.tooltip_item.compound_bow_blueprint").withStyle(ChatFormatting.GRAY));
            }
            else if (this == ModItems.LIGHT_MACHINE_GUN.get()) {
                tooltip.add(Component.translatable("info.jeg.tooltip_item.light_machine_gun_blueprint").withStyle(ChatFormatting.GRAY));
            }
            else if (this == ModItems.GRENADE_LAUNCHER.get()) {
                tooltip.add(Component.translatable("info.jeg.tooltip_item.grenade_launcher_blueprint").withStyle(ChatFormatting.GRAY));
            }
            else if (this == ModItems.FLAMETHROWER.get()) {
                tooltip.add(Component.translatable("info.jeg.tooltip_item.flamethrower_blueprint").withStyle(ChatFormatting.GRAY));
            }
            else if (this == ModItems.MINIGUN.get()) {
                tooltip.add(Component.translatable("info.jeg.tooltip_item.minigun").withStyle(ChatFormatting.GRAY));
            }
            else if (this == ModItems.PRIMITIVE_BOW.get()) {
                tooltip.add(Component.translatable("info.jeg.tooltip_item.primitive_bow").withStyle(ChatFormatting.GRAY));
            }

            if (modifiedGun.getGeneral().getFireTimer() != 0) {
                tooltip.add(Component.literal(""));
                tooltip.add(Component.translatable("info.jeg.hold_fire").withStyle(ChatFormatting.WHITE));
            }

            if (this == ModItems.SUBSONIC_RIFLE.get() || this == ModItems.HYPERSONIC_CANNON.get() ||
                    this == ModItems.SUPERSONIC_SHOTGUN.get()) {
                tooltip.add(Component.literal(""));
                tooltip.add(Component.translatable("info.jeg.echo_shard").withStyle(ChatFormatting.WHITE));
            }


            if (GunEnchantmentHelper.getEnchantmentLevel(stack, ModEnchantments.INFINITY) != 0 || GunEnchantmentHelper.getEnchantmentLevel(stack, ModEnchantments.WITHERED) != 0) {
                tooltip.add(Component.literal(""));

                if (GunEnchantmentHelper.getEnchantmentLevel(stack, ModEnchantments.WITHERED) != 0 ) {
                    tooltip.add(Component.translatable("info.jeg.withered").withStyle(ChatFormatting.DARK_RED));
                }
                if (GunEnchantmentHelper.getEnchantmentLevel(stack, ModEnchantments.INFINITY) != 0 ) {
                    tooltip.add(Component.translatable("info.jeg.infinity").withStyle(ChatFormatting.AQUA));
                }
            }
        }
    }

    // PORT-VERIFY: NeoForge 21.1 IItemExtension#onEntitySwing(ItemStack, LivingEntity, InteractionHand) (hand param added in 1.21)
    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        return true;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
        return gun.getGeneral().getRate() * 4;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        //CompoundTag tagCompound = stack.getOrCreateTag();
        //Gun modifiedGun = this.getModifiedGun(stack);
        //return !tagCompound.getBoolean("IgnoreAmmo") && tagCompound.getInt("AmmoCount") != GunModifierHelper.getModifiedAmmoCapacity(stack, modifiedGun);
        return stack.isDamaged();
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        //return (int) (13.0 * (tagCompound.getInt("AmmoCount") / (double) GunModifierHelper.getModifiedAmmoCapacity(stack, modifiedGun)));
        return Math.round(13.0F - (float)stack.getDamageValue() * 13.0F / (float)this.getMaxDamage(stack));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        if (stack.getDamageValue() >= (stack.getMaxDamage() / 1.5)) {
            return Objects.requireNonNull(ChatFormatting.RED.getColor());
        }
        float stackMaxDamage = this.getMaxDamage(stack);
        float f = Math.max(0.0F, (stackMaxDamage - (float)stack.getDamageValue()) / stackMaxDamage);
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
        //return Objects.requireNonNull(ChatFormatting.WHITE.getColor());
    }

    @Override
    public Component getName(ItemStack stack) {
        if (StackData.contains(stack, "GunId")) {
            ResourceLocation gunID = ResourceLocation.parse(StackData.getString(stack, "GunId"));
            return Component.translatable("item.jeg." + gunID.getPath());
        }

        return super.getName(stack);
    }

    public Gun getModifiedGun(ItemStack stack) {
        // Data-driven gun
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        CompoundTag tagCompound = customData == null || customData.isEmpty() ? null : customData.copyTag();
        if (tagCompound != null && tagCompound.contains("GunId", Tag.TAG_STRING)) {
            ResourceLocation id = ResourceLocation.parse(tagCompound.getString("GunId"));
            Gun data = FMLEnvironment.dist.isClient()
                    ? NetworkGunManager.getClientGun(id)
                    : (NetworkGunManager.get() != null
                    ? NetworkGunManager.get().getRegisteredGuns().get(id)
                    : null);
            return data != null ? data : this.gun;
        }

        if (tagCompound != null && tagCompound.contains("Gun", Tag.TAG_COMPOUND)) {
            return this.modifiedGunCache.computeIfAbsent(customData, item ->
            {
                if (tagCompound.getBoolean("Custom")) {
                    return Gun.create(tagCompound.getCompound("Gun"));
                } else {
                    Gun gunCopy = this.gun.copy();
                    gunCopy.deserializeNBT(tagCompound.getCompound("Gun"));
                    return gunCopy;
                }
            });
        }
        if (JustEnoughGuns.isDebugging()) {
            return Debug.getGun(this);
        }
        return this.gun;
    }

    // PORT: canApplyAtEnchantingTable(ItemStack, Enchantment) + EnchantmentCategory.SEMI_AUTO_GUN -> supportsEnchantment (NeoForge IItemExtension).
    // Only Trigger Finger used the SEMI_AUTO_GUN category: it may not be applied to fully automatic guns.
    // PORT-VERIFY: IItemExtension#supportsEnchantment(ItemStack, Holder<Enchantment>) signature in NeoForge 21.1
    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        if (enchantment.is(ModEnchantments.TRIGGER_FINGER)) {
            Gun modifiedGun = this.getModifiedGun(stack);
            if (modifiedGun.getGeneral().getFireMode() == FireMode.AUTOMATIC) {
                return false;
            }
        }
        return super.supportsEnchantment(stack, enchantment);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return this.getMaxStackSize(stack) == 1;
    }

    @Override
    public int getEnchantmentValue() {
        return 5;
    }

    // PORT: initializeClient(Consumer<IClientItemExtensions>) was removed from Item in NeoForge 1.21; the BEWLR is now registered in
    // ttv.migami.jeg.item.ItemClientExtensions (RegisterClientExtensionsEvent).

    // Disables the enchantment foil to allow a different kind of customization.
    // Foil only applies when the gun is blessed by Infinity
    public boolean isFoil(ItemStack stack) {
        return GunEnchantmentHelper.getEnchantmentLevel(stack, ModEnchantments.INFINITY) != 0;
    }

    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return pRepair.is(ModItems.REPAIR_KIT.get());
    }

    public String getModID() {
        ResourceLocation registryName = BuiltInRegistries.ITEM.getKey(this);
        if (registryName != null)
            return registryName.getNamespace();
        else return null;
    }
}
