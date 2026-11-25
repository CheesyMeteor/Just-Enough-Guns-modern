package ttv.migami.jeg.crafting.workbench;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import ttv.migami.jeg.blockentity.GunniteWorkbenchBlockEntity;
import ttv.migami.jeg.init.ModRecipeSerializers;
import ttv.migami.jeg.init.ModRecipeTypes;

public class GunniteWorkbenchRecipe extends AbstractWorkbenchRecipe<GunniteWorkbenchBlockEntity> {
    public GunniteWorkbenchRecipe(ItemStack item, ImmutableList<WorkbenchIngredient> materials) {
        super(item, materials);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.GUNNITE_WORKBENCH.get();
    }

    @Override
    public net.minecraft.world.item.crafting.RecipeType<?> getType() {
        return ModRecipeTypes.GUNNITE_WORKBENCH.get();
    }
}