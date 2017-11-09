package morph.avaritia.compat.jei.extreme;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import morph.avaritia.compat.jei.AvaritiaJEIPlugin;
import morph.avaritia.recipe.extreme.IExtremeRecipe;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by covers1624 on 31/07/2017.
 */
public class ExtremeRecipeWrapper implements IRecipeWrapper {

    protected final IExtremeRecipe recipe;

    public ExtremeRecipeWrapper(IExtremeRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, getRecipeInputs(recipe));
        ingredients.setOutput(ItemStack.class, getRecipeOutput(recipe));
    }

    protected List<List<ItemStack>> getRecipeInputs(IExtremeRecipe recipe) {
        return AvaritiaJEIPlugin.jeiHelpers.getStackHelper().expandRecipeItemStackInputs(recipe.getIngredients());
    }

    protected ItemStack getRecipeOutput(IExtremeRecipe recipe) {
        return recipe.getRecipeOutput();
    }
}


























