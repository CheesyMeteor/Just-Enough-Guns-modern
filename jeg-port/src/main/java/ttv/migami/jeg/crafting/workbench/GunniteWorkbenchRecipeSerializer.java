package ttv.migami.jeg.crafting.workbench;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.ItemStack;

public class GunniteWorkbenchRecipeSerializer extends AbstractWorkbenchRecipeSerializer<GunniteWorkbenchRecipe> {
    @Override
    protected GunniteWorkbenchRecipe createRecipe(ItemStack result, ImmutableList<WorkbenchIngredient> materials) {
        return new GunniteWorkbenchRecipe(result, materials);
    }
}
