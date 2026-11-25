package ttv.migami.jeg.client.util;

import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import ttv.migami.jeg.crafting.workbench.BlueprintWorkbenchRecipe;

public class RecipeHelper {

    public static BlueprintWorkbenchRecipe getRecipeFromItem(String namespace, String path, NonNullList<BlueprintWorkbenchRecipe> recipes) {
        ResourceLocation itemLocation = ResourceLocation.fromNamespaceAndPath(namespace, path);

        Item item = BuiltInRegistries.ITEM.get(itemLocation);
        if (item == null) {
            return null;
        }

        //if (item instanceof GunItem) {
            for (BlueprintWorkbenchRecipe recipe : recipes) {
                if (recipe.getItem().getItem() == item) {
                    return recipe;
                }
            }
        //}

        return null;
    }
}