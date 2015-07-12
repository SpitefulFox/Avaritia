package fox.spiteful.avaritia.compat;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.ShapelessRecipeHandler;
import fox.spiteful.avaritia.crafting.ExtremeCraftingManager;
import fox.spiteful.avaritia.crafting.ExtremeShapelessRecipe;
import fox.spiteful.avaritia.gui.GUIExtremeCrafting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExtremeShapelessRecipeHandler extends ShapelessRecipeHandler{

    public class CachedExtremeShapelessRecipe extends CachedRecipe
    {
        public CachedExtremeShapelessRecipe() {
            ingredients = new ArrayList<PositionedStack>();
        }

        public CachedExtremeShapelessRecipe(ItemStack output) {
            this();
            setResult(output);
        }

        public CachedExtremeShapelessRecipe(Object[] input, ItemStack output) {
            this(Arrays.asList(input), output);
        }

        public CachedExtremeShapelessRecipe(List<?> input, ItemStack output) {
            this(output);
            setIngredients(input);
        }

        public void setIngredients(List<?> items) {
            ingredients.clear();
            for (int ingred = 0; ingred < items.size(); ingred++) {
                PositionedStack stack = new PositionedStack(items.get(ingred), 3 + (ingred % 9) * 18, 3 + (ingred / 9) * 18);
                stack.setMaxSize(1);
                ingredients.add(stack);
            }
        }

        public void setResult(ItemStack output) {
            result = new PositionedStack(output, 201, 75);
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 20, ingredients);
        }

        @Override
        public PositionedStack getResult() {
            return result;
        }

        public ArrayList<PositionedStack> ingredients;
        public PositionedStack result;
    }

    public String getRecipeName() {
        return StatCollector.translateToLocal("crafting.extreme.shapeless");
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("extreme") && getClass() == ExtremeShapelessRecipeHandler.class) {
            List<IRecipe> allrecipes = ExtremeCraftingManager.getInstance().getRecipeList();
            for (IRecipe irecipe : allrecipes) {
                CachedExtremeShapelessRecipe recipe = null;
                if (irecipe instanceof ExtremeShapelessRecipe)
                    recipe = shapelessRecipe((ExtremeShapelessRecipe) irecipe);

                if (recipe == null)
                    continue;

                arecipes.add(recipe);
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        List<IRecipe> allrecipes = ExtremeCraftingManager.getInstance().getRecipeList();
        for (IRecipe irecipe : allrecipes) {
            if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
                CachedExtremeShapelessRecipe recipe = null;
                if (irecipe instanceof ExtremeShapelessRecipe)
                    recipe = shapelessRecipe((ExtremeShapelessRecipe) irecipe);

                if (recipe == null)
                    continue;

                arecipes.add(recipe);
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        List<IRecipe> allrecipes = ExtremeCraftingManager.getInstance().getRecipeList();
        for (IRecipe irecipe : allrecipes) {
            CachedExtremeShapelessRecipe recipe = null;
            if (irecipe instanceof ExtremeShapelessRecipe)
                recipe = shapelessRecipe((ExtremeShapelessRecipe) irecipe);

            if (recipe == null)
                continue;

            if (recipe.contains(recipe.ingredients, ingredient)) {
                recipe.setIngredientPermutation(recipe.ingredients, ingredient);
                arecipes.add(recipe);
            }
        }
    }

    private CachedExtremeShapelessRecipe shapelessRecipe(ExtremeShapelessRecipe recipe) {
        if(recipe.recipeItems == null) //because some mod subclasses actually do this
            return null;

        return new CachedExtremeShapelessRecipe(recipe.recipeItems, recipe.getRecipeOutput());
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(166, 74, 24, 18), "extreme"));
    }

    @Override
    public String getOverlayIdentifier() {
        return "extreme";
    }

    @Override
    public String getGuiTexture()
    {
        return "avaritia:textures/gui/dire_crafting_gui.png";
    }

    @Override
    public boolean hasOverlay(GuiContainer gui, net.minecraft.inventory.Container container, int recipe)
    {
        return RecipeInfo.hasDefaultOverlay(gui, "extreme");
    }

    @Override
    public void drawBackground(int recipe)
    {
        GL11.glColor4f(1, 1, 1, 1);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 9, 5, 223, 166);
    }

    @Override
    public int recipiesPerPage()
    {
        return 1;
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass()
    {
        return GUIExtremeCrafting.class;
    }

}