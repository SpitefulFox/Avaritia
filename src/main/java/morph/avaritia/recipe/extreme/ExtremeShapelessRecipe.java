package morph.avaritia.recipe.extreme;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class ExtremeShapelessRecipe implements IRecipe {

    /**
     * Is the ItemStack that you get when craft the recipe.
     */
    private final ItemStack recipeOutput;
    /**
     * Is a List of ItemStack that composes the recipe.
     */
    public final List<ItemStack> recipeItems;

    public ExtremeShapelessRecipe(ItemStack result, List<ItemStack> ingredients) {
        this.recipeOutput = result;
        this.recipeItems = ingredients;
    }

    public ItemStack getRecipeOutput() {
        return this.recipeOutput;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting matrix, World world) {
        ArrayList<ItemStack> inputCopy = new ArrayList<>(this.recipeItems);

        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                ItemStack itemstack = matrix.getStackInRowAndColumn(j, i);

                if (itemstack != null) {
                    boolean flag = false;

                    for (ItemStack stack : inputCopy) {
                        if (itemstack.getItem() == stack.getItem() && (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE || itemstack.getItemDamage() == stack.getItemDamage())) {
                            flag = true;
                            inputCopy.remove(stack);
                            break;
                        }
                    }

                    if (!flag) {
                        return false;
                    }
                }
            }
        }

        return inputCopy.isEmpty();
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting matrix) {
        return this.recipeOutput.copy();
    }

    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize() {
        return this.recipeItems.size();
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) //getRecipeLeftovers
    {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }
}
