package ttv.migami.jeg.crafting.workbench;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.ItemStack;

public class ScrapWorkbenchRecipeSerializer extends AbstractWorkbenchRecipeSerializer<ScrapWorkbenchRecipe> {
    @Override
    protected ScrapWorkbenchRecipe createRecipe(ItemStack result, ImmutableList<WorkbenchIngredient> materials) {
        return new ScrapWorkbenchRecipe(result, materials);
    }
}
