package ttv.migami.jeg.crafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import ttv.migami.jeg.init.ModItems;
import ttv.migami.jeg.init.ModRecipeSerializers;
import ttv.migami.jeg.item.GunItem;

/**
 * JEG: Modern - repairs a damaged gun completely by combining it with a Repair Kit in a crafting grid.
 * Replaces the anvil/XP repair of the original.
 */
public class GunRepairRecipe extends CustomRecipe
{
    public GunRepairRecipe(CraftingBookCategory category)
    {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level)
    {
        boolean gun = false;
        boolean kit = false;

        for(int i = 0; i < input.size(); i++)
        {
            ItemStack stack = input.getItem(i);
            if(stack.isEmpty())
            {
                continue;
            }
            if(stack.getItem() instanceof GunItem)
            {
                if(gun || !stack.isDamaged())
                {
                    return false;
                }
                gun = true;
            }
            else if(stack.is(ModItems.REPAIR_KIT.get()))
            {
                if(kit)
                {
                    return false;
                }
                kit = true;
            }
            else
            {
                return false;
            }
        }
        return gun && kit;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries)
    {
        for(int i = 0; i < input.size(); i++)
        {
            ItemStack stack = input.getItem(i);
            if(stack.getItem() instanceof GunItem)
            {
                ItemStack repaired = stack.copy();
                repaired.setDamageValue(0);
                return repaired;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height)
    {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return ModRecipeSerializers.GUN_REPAIR.get();
    }
}
