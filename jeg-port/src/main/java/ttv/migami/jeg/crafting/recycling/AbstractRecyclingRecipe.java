package ttv.migami.jeg.crafting.recycling;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

/**
 * PORT: now a {@code Recipe<SingleRecipeInput>} (like the vanilla furnace recipes); the recycler block entity must
 * query it with {@code recipeManager.getRecipeFor(type, new SingleRecipeInput(stackInInputSlot), level)} and gets a
 * RecipeHolder back (the id lives on the holder, not on the recipe).
 */
public abstract class AbstractRecyclingRecipe implements Recipe<SingleRecipeInput> {
   protected final RecipeType<?> type;
   protected final String group;
   protected final Ingredient ingredient;
   protected final ItemStack result;
   protected final float experience;
   protected final int recyclingTime;

   public AbstractRecyclingRecipe(RecipeType<?> pType, String pGroup, Ingredient pIngredient, ItemStack pResult, float pExperience, int pCookingTime) {
      this.type = pType;
      this.group = pGroup;
      this.ingredient = pIngredient;
      this.result = pResult;
      this.experience = pExperience;
      this.recyclingTime = pCookingTime;
   }

   public ItemStack getItem() {
      return this.result.copy();
   }

   public Ingredient getIngredient() {
      return this.ingredient;
   }

   public boolean matches(SingleRecipeInput pInv, Level pLevel) {
      return this.ingredient.test(pInv.item());
   }

   public ItemStack assemble(SingleRecipeInput pContainer, HolderLookup.Provider pRegistries) {
      return this.result.copy();
   }

   public boolean canCraftInDimensions(int pWidth, int pHeight) {
      return true;
   }

   public NonNullList<Ingredient> getIngredients() {
      NonNullList<Ingredient> nonnulllist = NonNullList.create();
      nonnulllist.add(this.ingredient);
      return nonnulllist;
   }

   public float getExperience() {
      return this.experience;
   }

   public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
      return this.result;
   }

   public String getGroup() {
      return this.group;
   }

   public int getRecyclingTime() {
      return this.recyclingTime;
   }

   public RecipeType<?> getType() {
      return this.type;
   }

   @Override
   public boolean isSpecial()
   {
      return true;
   }
}
