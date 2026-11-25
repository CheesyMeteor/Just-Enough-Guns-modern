package ttv.migami.jeg.crafting;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;
import ttv.migami.jeg.Config;
import java.util.function.Supplier;

/**
 * JEG: Modern - a normal crafting recipe that stops matching while the server's magazine system is disabled.
 * Used for the magazines, which do nothing without it. The JSON format is identical to minecraft:crafting_shaped
 * (type jeg:magazine_gated) or minecraft:crafting_shapeless (type jeg:magazine_gated_shapeless).
 * <p>
 * The recipe only wraps a vanilla recipe and forwards everything to it, so nothing about the
 * pattern handling is reimplemented here.
 */
public class MagazineGatedRecipe implements CraftingRecipe
{
    private final CraftingRecipe inner;
    private final Supplier<? extends RecipeSerializer<?>> serializer;

    public MagazineGatedRecipe(CraftingRecipe inner, Supplier<? extends RecipeSerializer<?>> serializer)
    {
        this.inner = inner;
        this.serializer = serializer;
    }

    @Override
    public boolean matches(CraftingInput input, Level level)
    {
        return Config.magazineFeedEnabled() && this.inner.matches(input, level);
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries)
    {
        return this.inner.assemble(input, registries);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return this.inner.canCraftInDimensions(width, height);
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries)
    {
        return this.inner.getResultItem(registries);
    }

    @Override
    public NonNullList<Ingredient> getIngredients()
    {
        return this.inner.getIngredients();
    }

    @Override
    public String getGroup()
    {
        return this.inner.getGroup();
    }

    @Override
    public CraftingBookCategory category()
    {
        return this.inner.category();
    }

    @Override
    public boolean showNotification()
    {
        return this.inner.showNotification();
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return this.serializer.get();
    }

    public static class Serializer implements RecipeSerializer<MagazineGatedRecipe>
    {
        private final MapCodec<MagazineGatedRecipe> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, MagazineGatedRecipe> streamCodec;

        private <R extends CraftingRecipe> Serializer(RecipeSerializer<R> vanilla, Supplier<Supplier<? extends RecipeSerializer<?>>> self)
        {
            this.codec = vanilla.codec().xmap(r -> new MagazineGatedRecipe(r, self.get()), g -> cast(g.inner));
            this.streamCodec = vanilla.streamCodec().map(r -> new MagazineGatedRecipe(r, self.get()), g -> cast(g.inner));
        }

        @SuppressWarnings("unchecked")
        private static <R extends CraftingRecipe> R cast(CraftingRecipe recipe)
        {
            return (R) recipe;
        }

        public static Serializer shaped(Supplier<Supplier<? extends RecipeSerializer<?>>> self)
        {
            return new Serializer(new ShapedRecipe.Serializer(), self);
        }

        public static Serializer shapeless(Supplier<Supplier<? extends RecipeSerializer<?>>> self)
        {
            return new Serializer(new ShapelessRecipe.Serializer(), self);
        }

        @Override
        public MapCodec<MagazineGatedRecipe> codec()
        {
            return this.codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MagazineGatedRecipe> streamCodec()
        {
            return this.streamCodec;
        }
    }
}
