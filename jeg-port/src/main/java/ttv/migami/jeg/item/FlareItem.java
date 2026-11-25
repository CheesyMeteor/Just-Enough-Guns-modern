package ttv.migami.jeg.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import ttv.migami.jeg.entity.throwable.ThrowableFlareEntity;
import ttv.migami.jeg.entity.throwable.ThrowableGrenadeEntity;
import ttv.migami.jeg.init.ModItems;
import ttv.migami.jeg.util.StackData;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class FlareItem extends GrenadeItem
{
    public FlareItem(Properties properties, int maxCookTime)
    {
        super(properties, maxCookTime);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pStack.is(ModItems.TERROR_ARMADA_FLARE.get())) {
            pStack.set(DataComponents.CUSTOM_NAME, Component.translatable("item.jeg.terror_armada_flare").withStyle(style -> style.withColor(ChatFormatting.BLUE).withItalic(false)));
        } else if (StackData.getBoolean(pStack, "HasRaid")) {
            pStack.set(DataComponents.CUSTOM_NAME, Component.translatable("item.jeg.raid_flare").withStyle(style -> style.withColor(ChatFormatting.RED).withItalic(false)));
        } else if (!pStack.has(DataComponents.CUSTOM_NAME)) {
            pStack.remove(DataComponents.CUSTOM_NAME);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag tagCompound = StackData.getOrNull(stack);

        if (stack.is(ModItems.TERROR_ARMADA_FLARE.get())) {
            tooltip.add(Component.translatable("info.jeg.raid_flare").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD));
            tooltip.add(Component.literal(""));
            tooltip.add(Component.translatable("info.jeg.flare_raid").withStyle(ChatFormatting.GRAY)
                    .append(Component.translatable("faction.jeg.terror_armada").withStyle(ChatFormatting.BLUE)));
        } else if (tagCompound != null && tagCompound.getBoolean("HasRaid")) {
            String factionName;
            if (tagCompound != null && tagCompound.contains("Raid")) {
                factionName = tagCompound.getString("Raid");
            } else {
                factionName = "random";
            }
            tooltip.add(Component.translatable("info.jeg.raid_flare").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD));
            tooltip.add(Component.literal(""));
            tooltip.add(Component.translatable("info.jeg.flare_raid").withStyle(ChatFormatting.GRAY)
                    .append(Component.translatable("faction.jeg." + factionName).withStyle(ChatFormatting.WHITE)));
        }
    }

    @Override
    public ThrowableGrenadeEntity create(ItemStack stack, Level world, LivingEntity entity, int timeLeft)
    {
        if (stack.is(ModItems.TERROR_ARMADA_FLARE.get())) {
            return new ThrowableFlareEntity(world, entity, false, true);
        } else if (StackData.getBoolean(stack, "HasRaid")) {
            if (!StackData.getString(stack, "Raid").isEmpty()) {
                return new ThrowableFlareEntity(world, entity, true, StackData.getString(stack, "Raid"));
            } else {
                return new ThrowableFlareEntity(world, entity, true);
            }
        }
        return new ThrowableFlareEntity(world, entity);
    }

    @Override
    public boolean canCook()
    {
        return false;
    }

    @Override
    protected void onThrown(Level world, ThrowableGrenadeEntity entity)
    {
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}
