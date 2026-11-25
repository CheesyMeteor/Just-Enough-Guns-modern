package ttv.migami.jeg.crafting.workbench;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import ttv.migami.jeg.util.InventoryUtil;

import javax.annotation.Nullable;

public abstract class AbstractWorkbenchRecipe<T extends BlockEntity & Container> implements Recipe<AbstractWorkbenchRecipe.NoInput> {
    /**
     * PORT: 1.21 recipes are identified by their RecipeHolder, not by the recipe. Workbench screens/network code still
     * call {@link #getId()}, so {@link WorkbenchRecipes} fills this in whenever it hands a recipe out.
     */
    @Nullable
    private ResourceLocation id;
    private final ItemStack item;
    private final ImmutableList<WorkbenchIngredient> materials;

    public AbstractWorkbenchRecipe(ItemStack item, ImmutableList<WorkbenchIngredient> materials) {
        this.item = item;
        this.materials = materials;
    }

    public ItemStack getItem() {
        return this.item.copy();
    }

    public ImmutableList<WorkbenchIngredient> getMaterials() {
        return this.materials;
    }

    @Override
    public boolean matches(NoInput inv, Level worldIn) {
        return false;
    }

    @Override
    public ItemStack assemble(NoInput input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.item.copy();
    }

    /** Only valid for recipes obtained through {@link WorkbenchRecipes}. */
    @Nullable
    public ResourceLocation getId() {
        return this.id;
    }

    void setId(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public abstract RecipeSerializer<?> getSerializer();

    @Override
    public abstract RecipeType<?> getType();

    public boolean hasMaterials(Player player) {
        for (WorkbenchIngredient ingredient : this.getMaterials()) {
            if (!InventoryUtil.hasWorkstationIngredient(player, ingredient)) {
                return false;
            }
        }
        return true;
    }

    public void consumeMaterials(Player player) {
        for (WorkbenchIngredient ingredient : this.getMaterials()) {
            InventoryUtil.removeWorkstationIngredient(player, ingredient);
        }
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    /** Workbench recipes are never matched against an inventory; they only need a RecipeInput type. */
    public static final class NoInput implements RecipeInput {
        public static final NoInput INSTANCE = new NoInput();

        private NoInput() {}

        @Override
        public ItemStack getItem(int index) {
            return ItemStack.EMPTY;
        }

        @Override
        public int size() {
            return 0;
        }
    }
}
