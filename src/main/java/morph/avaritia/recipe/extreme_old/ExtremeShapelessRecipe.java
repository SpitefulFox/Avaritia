package morph.avaritia.recipe.extreme_old;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class ExtremeShapelessRecipe extends ExtremeRecipeBase {

    /**
     * Is the ItemStack that you get when craft the recipe.
     */
    private final ItemStack recipeOutput;
    /**
     * Is a List of ItemStack that composes the recipe.
     */
    public final List<ItemStack> recipeItems;

    public ExtremeShapelessRecipe(ItemStack result, List<ItemStack> ingredients) {
        recipeOutput = result;
        recipeItems = ingredients;
    }

    public ItemStack getRecipeOutput() {
        return recipeOutput;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting matrix, World world) {
        ArrayList<ItemStack> inputCopy = new ArrayList<>(recipeItems);

        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                ItemStack itemstack = matrix.getStackInRowAndColumn(j, i);

                if (!itemstack.isEmpty()) {
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
        return recipeOutput.copy();
    }

    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize() {
        return recipeItems.size();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }
}
