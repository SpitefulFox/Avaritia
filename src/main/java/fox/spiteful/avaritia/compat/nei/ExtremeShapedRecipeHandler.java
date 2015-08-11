package fox.spiteful.avaritia.compat.nei;

import fox.spiteful.avaritia.crafting.ExtremeCraftingManager;
import fox.spiteful.avaritia.crafting.ExtremeShapedOreRecipe;
import fox.spiteful.avaritia.crafting.ExtremeShapedRecipe;
import fox.spiteful.avaritia.gui.GUIExtremeCrafting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.ShapedRecipeHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ExtremeShapedRecipeHandler extends ShapedRecipeHandler
{
    public class CachedExtremeRecipe extends CachedRecipe
    {
        public CachedExtremeRecipe(ExtremeShapedRecipe recipe)
        {
            this(recipe.recipeWidth, recipe.recipeHeight, recipe.recipeItems, recipe.getRecipeOutput());
        }

        public CachedExtremeRecipe(int width, int height, Object[] items, ItemStack out)
        {
            this.result = new PositionedStack(out, 201, 75);
            this.ingredients = new ArrayList<PositionedStack>();
            setIngredients(width, height, items);
        }

        public void setIngredients(int width, int height, Object[] items)
        {
            for(int x = 0; x < width; x++)
            {
                for(int y = 0; y < height; y++)
                {
                    if(items[y * width + x] == null)
                    {
                        continue;
                    }
                    int ex = 3 + x * 18;
                    int wy = 3 + y * 18;
                    if(wy == 129){
                        if(ex == 3 || ex == 129)
                            ex -= 1;
                        else if(ex == 21 || ex == 147)
                            ex += 1;
                    }
                    PositionedStack stack = new PositionedStack(items[y * width + x], ex, wy);
                    stack.setMaxSize(1);
                    this.ingredients.add(stack);
                }
            }
        }

        @Override
        public ArrayList<PositionedStack> getIngredients()
        {
            return (ArrayList<PositionedStack>) getCycledIngredients(ExtremeShapedRecipeHandler.this.cycleticks / 20, this.ingredients);
        }

        @Override
        public PositionedStack getResult()
        {
            return this.result;
        }

        public void computeVisuals() {
            for (PositionedStack p : ingredients)
                p.generatePermutations();
        }

        public ArrayList<PositionedStack> ingredients;
        public PositionedStack result;
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

    @Override
    public String getRecipeName()
    {
        return StatCollector.translateToLocal("crafting.extreme");
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("extreme") && getClass() == ExtremeShapedRecipeHandler.class) {
            for (IRecipe irecipe : (List<IRecipe>) ExtremeCraftingManager.getInstance().getRecipeList()) {
                CachedExtremeRecipe recipe = null;
                if (irecipe instanceof ExtremeShapedRecipe)
                    recipe = new CachedExtremeRecipe((ExtremeShapedRecipe) irecipe);
                else if (irecipe instanceof ExtremeShapedOreRecipe)
                    recipe = forgeExtremeShapedRecipe((ExtremeShapedOreRecipe) irecipe);

                if (recipe == null)
                    continue;

                recipe.computeVisuals();
                arecipes.add(recipe);
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (IRecipe irecipe : (List<IRecipe>) ExtremeCraftingManager.getInstance().getRecipeList()) {
            if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
                CachedExtremeRecipe recipe = null;
                if (irecipe instanceof ExtremeShapedRecipe)
                    recipe = new CachedExtremeRecipe((ExtremeShapedRecipe) irecipe);
                else if (irecipe instanceof ExtremeShapedOreRecipe)
                    recipe = forgeExtremeShapedRecipe((ExtremeShapedOreRecipe) irecipe);

                if (recipe == null)
                    continue;

                recipe.computeVisuals();
                arecipes.add(recipe);
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (IRecipe irecipe : (List<IRecipe>) ExtremeCraftingManager.getInstance().getRecipeList()) {
            CachedExtremeRecipe recipe = null;
            if (irecipe instanceof ExtremeShapedRecipe)
                recipe = new CachedExtremeRecipe((ExtremeShapedRecipe) irecipe);
            else if (irecipe instanceof ExtremeShapedOreRecipe)
                recipe = forgeExtremeShapedRecipe((ExtremeShapedOreRecipe) irecipe);

            if (recipe == null || !recipe.contains(recipe.ingredients, ingredient.getItem()))
                continue;

            recipe.computeVisuals();
            if (recipe.contains(recipe.ingredients, ingredient)) {
                recipe.setIngredientPermutation(recipe.ingredients, ingredient);
                arecipes.add(recipe);
            }
        }
    }

    public CachedExtremeRecipe forgeExtremeShapedRecipe(ExtremeShapedOreRecipe recipe) {
        int width = recipe.width;
        int height = recipe.height;

        Object[] items = recipe.getInput();
        for (Object item : items)
            if (item instanceof List && ((List<?>) item).isEmpty())//ore handler, no ores
                return null;

        return new CachedExtremeRecipe(width, height, items, recipe.getRecipeOutput());
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
        return "avaritia:textures/gui/extreme_nei.png";
    }

    @Override
    public boolean hasOverlay(GuiContainer gui, Container container, int recipe)
    {
        return RecipeInfo.hasDefaultOverlay(gui, "extreme");
    }

    @Override
    public void drawBackground(int recipe)
    {
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(-9, -20, 0, 0, 256, 208);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }
}
