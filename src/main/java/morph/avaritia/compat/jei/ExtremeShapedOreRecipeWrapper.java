package morph.avaritia.compat.jei;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.util.BrokenCraftingRecipeException;
import mezz.jei.util.ErrorUtil;
import morph.avaritia.recipe.extreme.ExtremeShapedOreRecipe;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * Created by brandon3055 on 17/02/2017.
 */
public class ExtremeShapedOreRecipeWrapper extends BlankRecipeWrapper {

    private IJeiHelpers jeiHelpers;
    private final ExtremeShapedOreRecipe recipe;

    public ExtremeShapedOreRecipeWrapper(IJeiHelpers helpers, ExtremeShapedOreRecipe recipe) {
        this.jeiHelpers = helpers;
        this.recipe = recipe;
        for (Object input : this.recipe.getInput()) {
            if (input instanceof ItemStack) {
                ItemStack itemStack = (ItemStack) input;
                if (itemStack.stackSize != 1) {
                    itemStack.stackSize = 1;
                }
            }
        }
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        IStackHelper stackHelper = jeiHelpers.getStackHelper();
        ItemStack recipeOutput = recipe.getRecipeOutput();

        try {
            List<List<ItemStack>> inputs = stackHelper.expandRecipeItemStackInputs(Arrays.asList(recipe.getInput()));
            ingredients.setInputLists(ItemStack.class, inputs);
            if (recipeOutput != null) {
                ingredients.setOutput(ItemStack.class, recipeOutput);
            }
        } catch (RuntimeException e) {
            String info = ErrorUtil.getInfoFromBrokenCraftingRecipe(recipe, Arrays.asList(recipe.getInput()), recipeOutput);
            throw new BrokenCraftingRecipeException(info, e);
        }
    }
}
