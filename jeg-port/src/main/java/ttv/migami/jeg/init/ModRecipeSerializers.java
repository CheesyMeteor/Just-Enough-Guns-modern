package ttv.migami.jeg.init;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import ttv.migami.jeg.Reference;
import ttv.migami.jeg.crafting.*;
import ttv.migami.jeg.crafting.recycling.RecyclingRecipe;
import ttv.migami.jeg.crafting.recycling.SimpleRecyclingSerializer;
import ttv.migami.jeg.crafting.workbench.*;

/**
 * Author: MrCrayfish
 */
public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> REGISTER = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Reference.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<DyeItemRecipe>> DYE_ITEM = REGISTER.register("dye_item", () -> new SimpleCraftingRecipeSerializer<>(DyeItemRecipe::new));

    public static final DeferredHolder<RecipeSerializer<?>, MagazineGatedRecipe.Serializer> MAGAZINE_GATED = REGISTER.register("magazine_gated", () -> MagazineGatedRecipe.Serializer.shaped(() -> ModRecipeSerializers.MAGAZINE_GATED));
    public static final DeferredHolder<RecipeSerializer<?>, MagazineGatedRecipe.Serializer> MAGAZINE_GATED_SHAPELESS = REGISTER.register("magazine_gated_shapeless", () -> MagazineGatedRecipe.Serializer.shapeless(() -> ModRecipeSerializers.MAGAZINE_GATED_SHAPELESS));

    public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<GunRepairRecipe>> GUN_REPAIR = REGISTER.register("gun_repair", () -> new SimpleCraftingRecipeSerializer<>(GunRepairRecipe::new));

    public static final DeferredHolder<RecipeSerializer<?>, AbstractWorkbenchRecipeSerializer<ScrapWorkbenchRecipe>> SCRAP_WORKBENCH = REGISTER.register("scrap_workbench", ScrapWorkbenchRecipeSerializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, AbstractWorkbenchRecipeSerializer<GunmetalWorkbenchRecipe>> GUNMETAL_WORKBENCH = REGISTER.register("gunmetal_workbench", GunmetalWorkbenchRecipeSerializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, AbstractWorkbenchRecipeSerializer<GunniteWorkbenchRecipe>> GUNNITE_WORKBENCH = REGISTER.register("gunnite_workbench", GunniteWorkbenchRecipeSerializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, AbstractWorkbenchRecipeSerializer<BlueprintWorkbenchRecipe>> BLUEPRINT_WORKBENCH = REGISTER.register("blueprint_workbench", BlueprintWorkbenchRecipeSerializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, SimpleRecyclingSerializer<RecyclingRecipe>> RECYCLING_RECIPE = REGISTER.register("recycling", () -> new SimpleRecyclingSerializer<>(RecyclingRecipe::new, 1200));
}