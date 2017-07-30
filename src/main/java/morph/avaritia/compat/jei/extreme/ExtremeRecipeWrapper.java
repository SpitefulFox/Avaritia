package morph.avaritia.compat.jei.extreme;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import morph.avaritia.compat.jei.AvaritiaJEIPlugin;
import morph.avaritia.recipe.extreme.ExtremeShapedOreRecipe;
import morph.avaritia.recipe.extreme.ExtremeShapedRecipe;
import morph.avaritia.recipe.extreme.ExtremeShapelessOreRecipe;
import morph.avaritia.recipe.extreme.ExtremeShapelessRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by covers1624 on 31/07/2017.
 */
public abstract class ExtremeRecipeWrapper<T extends IRecipe> extends BlankRecipeWrapper {

    protected final T recipe;

    public ExtremeRecipeWrapper(T recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, getRecipeInputs(recipe));
        ingredients.setOutput(ItemStack.class, getRecipeOutput(recipe));
    }

    protected abstract List<List<ItemStack>> getRecipeInputs(T recipe);

    protected ItemStack getRecipeOutput(T recipe) {
        return recipe.getRecipeOutput();
    }

    public static class Shaped extends ExtremeRecipeWrapper<ExtremeShapedRecipe> {

        public Shaped(ExtremeShapedRecipe recipe) {
            super(recipe);
        }

        @Override
        protected List<List<ItemStack>> getRecipeInputs(ExtremeShapedRecipe recipe) {
            List<List<ItemStack>> inputs = new LinkedList<>();
            for (ItemStack stack : recipe.recipeItems) {
                inputs.add(Collections.singletonList(stack));
            }
            return inputs;
        }
    }

    public static class Shapeless extends ExtremeRecipeWrapper<ExtremeShapelessRecipe> {

        public Shapeless(ExtremeShapelessRecipe recipe) {
            super(recipe);
        }

        @Override
        protected List<List<ItemStack>> getRecipeInputs(ExtremeShapelessRecipe recipe) {
            List<List<ItemStack>> inputs = new LinkedList<>();
            for (ItemStack stack : recipe.recipeItems) {
                inputs.add(Collections.singletonList(stack));
            }
            return inputs;
        }
    }

    public static class ShapedOre extends ExtremeRecipeWrapper<ExtremeShapedOreRecipe> {

        public ShapedOre(ExtremeShapedOreRecipe recipe) {
            super(recipe);
        }

        @Override
        protected List<List<ItemStack>> getRecipeInputs(ExtremeShapedOreRecipe recipe) {
            return AvaritiaJEIPlugin.jeiHelpers.getStackHelper().expandRecipeItemStackInputs(Arrays.asList(recipe.getInput()));
        }
    }

    public static class ShapelessOre extends ExtremeRecipeWrapper<ExtremeShapelessOreRecipe> {

        public ShapelessOre(ExtremeShapelessOreRecipe recipe) {
            super(recipe);
        }

        @Override
        protected List<List<ItemStack>> getRecipeInputs(ExtremeShapelessOreRecipe recipe) {
            return AvaritiaJEIPlugin.jeiHelpers.getStackHelper().expandRecipeItemStackInputs(recipe.getInput());
        }
    }
}


























