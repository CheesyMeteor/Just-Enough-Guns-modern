package ttv.migami.jeg.crafting.workbench;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import ttv.migami.jeg.blockentity.BlueprintWorkbenchBlockEntity;
import ttv.migami.jeg.init.ModRecipeSerializers;
import ttv.migami.jeg.init.ModRecipeTypes;

public class BlueprintWorkbenchRecipe extends AbstractWorkbenchRecipe<BlueprintWorkbenchBlockEntity> {
    public BlueprintWorkbenchRecipe(ItemStack item, ImmutableList<WorkbenchIngredient> materials) {
        super(item, materials);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.BLUEPRINT_WORKBENCH.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.BLUEPRINT_WORKBENCH.get();
    }
}