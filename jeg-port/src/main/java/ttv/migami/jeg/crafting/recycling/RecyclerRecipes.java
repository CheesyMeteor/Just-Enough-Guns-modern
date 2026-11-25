package ttv.migami.jeg.crafting.recycling;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class RecyclerRecipes {
    public static boolean isEmpty(Level world, RecipeType<?> type) {
        return world.getRecipeManager().getRecipes().stream().noneMatch(holder -> holder.value().getType() == type);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> NonNullList<T> getAll(Level world, RecipeType<T> type) {
        NonNullList<T> list = NonNullList.create();
        for (RecipeHolder<?> holder : world.getRecipeManager().getRecipes()) {
            if (holder.value().getType() == type) {
                list.add((T) holder.value());
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T extends Recipe<?>> T getRecipeById(Level world, ResourceLocation id, RecipeType<T> type) {
        for (RecipeHolder<?> holder : world.getRecipeManager().getRecipes()) {
            if (holder.value().getType() == type && holder.id().equals(id)) {
                return (T) holder.value();
            }
        }
        return null;
    }
}
