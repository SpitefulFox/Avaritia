package morph.avaritia.compat.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.ErrorUtil;
import mezz.jei.util.Log;
import morph.avaritia.recipe.extreme.ExtremeShapelessRecipe;
import net.minecraft.item.ItemStack;

/**
 * Created by brandon3055 on 18/02/2017.
 */
public class ExtremeShelessRecipeHandler implements IRecipeHandler<ExtremeShapelessRecipe> {

    @Override
    public Class<ExtremeShapelessRecipe> getRecipeClass() {
        return ExtremeShapelessRecipe.class;
    }

    @Override
    public String getRecipeCategoryUid() {
        return RecipeCategoryUids.EXTREME_CRAFTING;
    }

    @Override
    public String getRecipeCategoryUid(ExtremeShapelessRecipe extremeShapedRecipe) {
        return RecipeCategoryUids.EXTREME_CRAFTING;
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(ExtremeShapelessRecipe extremeShapedRecipe) {
        return new ExtremeShapelessRecipeWrapper(extremeShapedRecipe);
    }

    @Override
    public boolean isRecipeValid(ExtremeShapelessRecipe recipe) {
        if (recipe.getRecipeOutput() == null) {
            String recipeInfo = ErrorUtil.getInfoFromRecipe(recipe, this);
            Log.error("Recipe has no outputs. {}", recipeInfo);
            return false;
        }
        int inputCount = 0;
        for (ItemStack input : recipe.recipeItems) {
            if (input != null) {
                inputCount++;
            }
        }
        if (inputCount > 81) {
            String recipeInfo = ErrorUtil.getInfoFromRecipe(recipe, this);
            Log.error("Recipe has too many inputs. {}", recipeInfo);
            return false;
        }
        if (inputCount == 0) {
            String recipeInfo = ErrorUtil.getInfoFromRecipe(recipe, this);
            Log.error("Recipe has no inputs. {}", recipeInfo);
            return false;
        }
        return true;
    }
}
