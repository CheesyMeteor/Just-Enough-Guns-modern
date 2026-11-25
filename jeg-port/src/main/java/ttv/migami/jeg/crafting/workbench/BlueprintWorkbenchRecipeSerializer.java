package ttv.migami.jeg.crafting.workbench;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.ItemStack;

public class BlueprintWorkbenchRecipeSerializer extends AbstractWorkbenchRecipeSerializer<BlueprintWorkbenchRecipe> {
    @Override
    protected BlueprintWorkbenchRecipe createRecipe(ItemStack result, ImmutableList<WorkbenchIngredient> materials) {
        return new BlueprintWorkbenchRecipe(result, materials);
    }
}
