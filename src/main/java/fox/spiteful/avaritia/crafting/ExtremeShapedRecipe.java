package fox.spiteful.avaritia.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ExtremeShapedRecipe implements IRecipe
{
    /** How many horizontal slots this recipe is wide. */
    public final int recipeWidth;
    /** How many vertical slots this recipe uses. */
    public final int recipeHeight;
    /** Is a array of ItemStack that composes the recipe. */
    public final ItemStack[] recipeItems;
    /** Is the ItemStack that you get when craft the recipe. */
    private ItemStack recipeOutput;

    public ExtremeShapedRecipe(int width, int height, ItemStack[] ingredients, ItemStack result)
    {
        this.recipeWidth = width;
        this.recipeHeight = height;
        this.recipeItems = ingredients;
        this.recipeOutput = result;
    }

    public ItemStack getRecipeOutput()
    {
        return this.recipeOutput;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting matrix, World world)
    {
        for (int i = 0; i <= 9 - this.recipeWidth; ++i)
        {
            for (int j = 0; j <= 9 - this.recipeHeight; ++j)
            {
                if (this.checkMatch(matrix, i, j, true))
                {
                    return true;
                }

                if (this.checkMatch(matrix, i, j, false))
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if the region of a crafting inventory is match for the recipe.
     */
    private boolean checkMatch(InventoryCrafting matrix, int x, int y, boolean mirrored)
    {
        for (int k = 0; k < 9; ++k)
        {
            for (int l = 0; l < 9; ++l)
            {
                int i1 = k - x;
                int j1 = l - y;
                ItemStack itemstack = null;

                if (i1 >= 0 && j1 >= 0 && i1 < this.recipeWidth && j1 < this.recipeHeight)
                {
                    if (mirrored)
                    {
                        itemstack = this.recipeItems[this.recipeWidth - i1 - 1 + j1 * this.recipeWidth];
                    }
                    else
                    {
                        itemstack = this.recipeItems[i1 + j1 * this.recipeWidth];
                    }
                }

                ItemStack itemstack1 = matrix.getStackInRowAndColumn(k, l);

                if (itemstack1 != null || itemstack != null)
                {
                    if (itemstack1 == null && itemstack != null || itemstack1 != null && itemstack == null)
                    {
                        return false;
                    }

                    if (itemstack.getItem() != itemstack1.getItem())
                    {
                        return false;
                    }

                    if (itemstack.getItemDamage() != 32767 && itemstack.getItemDamage() != itemstack1.getItemDamage())
                    {
                        return false;
                    }

                    if (itemstack.hasTagCompound() &&  !ItemStack.areItemStackTagsEqual(itemstack, itemstack1))
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting p_77572_1_)
    {
        return this.getRecipeOutput().copy();

    }

    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize()
    {
        return this.recipeWidth * this.recipeHeight;
    }

}