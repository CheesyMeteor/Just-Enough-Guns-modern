package ttv.migami.jeg.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import ttv.migami.jeg.util.StackData;

import javax.annotation.Nullable;
import java.util.List;

public class AdvancedBlueprintItem extends BlueprintItem {
    public AdvancedBlueprintItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flag) {

        CompoundTag tag = StackData.getOrNull(stack);
        if (tag != null) {
            if (!tag.getString("Namespace").isEmpty() && !tag.getString("Path").isEmpty()) {
                tooltip.add(Component.translatable("info.jeg.blueprinted_gun").withStyle(ChatFormatting.GRAY)
                        .append(Component.translatable("item." + tag.getString("Namespace") + "." + tag.getString("Path")).withStyle(ChatFormatting.GOLD)));            }
        }

        tooltip.add(Component.translatable("info.jeg.tooltip_item" + "." + ttv.migami.jeg.util.ItemNames.path(this.asItem())).withStyle(ChatFormatting.GRAY));
    }
}
