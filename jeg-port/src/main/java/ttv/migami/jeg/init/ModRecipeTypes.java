package ttv.migami.jeg.init;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.crafting.recycling.RecyclingRecipe;
import ttv.migami.jeg.crafting.workbench.BlueprintWorkbenchRecipe;
import ttv.migami.jeg.crafting.workbench.GunmetalWorkbenchRecipe;
import ttv.migami.jeg.crafting.workbench.GunniteWorkbenchRecipe;
import ttv.migami.jeg.crafting.workbench.ScrapWorkbenchRecipe;

/**
 * Author: MrCrayfish
 */
public class ModRecipeTypes {

    public static final DeferredRegister<RecipeType<?>> REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, Reference.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<ScrapWorkbenchRecipe>> SCRAP_WORKBENCH = create("scrap_workbench");
    public static final DeferredHolder<RecipeType<?>, RecipeType<GunmetalWorkbenchRecipe>> GUNMETAL_WORKBENCH = create("gunmetal_workbench");
    public static final DeferredHolder<RecipeType<?>, RecipeType<GunniteWorkbenchRecipe>> GUNNITE_WORKBENCH = create("gunnite_workbench");
    public static final DeferredHolder<RecipeType<?>, RecipeType<RecyclingRecipe>> RECYCLING = create("recycling");
    public static final DeferredHolder<RecipeType<?>, RecipeType<BlueprintWorkbenchRecipe>> BLUEPRINT_WORKBENCH = create("blueprint_workbench");

    private static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> create(String name)
    {
        return REGISTER.register(name, () -> new RecipeType<>()
        {
            @Override
            public String toString()
            {
                return name;
            }
        });
    }
}
