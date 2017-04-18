package morph.avaritia.compat.jei;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.ErrorUtil;
import mezz.jei.util.Log;
import morph.avaritia.recipe.extreme.ExtremeShapedOreRecipe;

import java.util.List;

/**
 * Created by brandon3055 on 18/02/2017.
 */
public class ExtremeShapedOreRecipeHandler implements IRecipeHandler<ExtremeShapedOreRecipe> {

    private IJeiHelpers jeiHelpers;

    public ExtremeShapedOreRecipeHandler(IJeiHelpers jeiHelpers) {
        this.jeiHelpers = jeiHelpers;
    }

    @Override
    public Class<ExtremeShapedOreRecipe> getRecipeClass() {
        return ExtremeShapedOreRecipe.class;
    }

    @Override
    public String getRecipeCategoryUid() {
        return RecipeCategoryUids.EXTREME_CRAFTING;
    }

    @Override
    public String getRecipeCategoryUid(ExtremeShapedOreRecipe extremeShapedRecipe) {
        return RecipeCategoryUids.EXTREME_CRAFTING;
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(ExtremeShapedOreRecipe extremeShapedRecipe) {
        return new ExtremeShapedOreRecipeWrapper(jeiHelpers, extremeShapedRecipe);
    }

    @Override
    public boolean isRecipeValid(ExtremeShapedOreRecipe recipe) {
        if (recipe.getRecipeOutput() == null) {
            String recipeInfo = ErrorUtil.getInfoFromRecipe(recipe, this);
            Log.error("Recipe has no output. {}", recipeInfo);
            return false;
        }
        int inputCount = 0;
        for (Object input : recipe.getInput()) {
            if (input instanceof List) {
                if (((List) input).isEmpty()) {
                    // missing items for an oreDict name. This is normal behavior, but the recipe is invalid.
                    return false;
                }
            }
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
