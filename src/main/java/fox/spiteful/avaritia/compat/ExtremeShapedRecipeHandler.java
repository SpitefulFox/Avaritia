package fox.spiteful.avaritia.compat;

import fox.spiteful.avaritia.crafting.ExtremeCraftingManager;
import fox.spiteful.avaritia.crafting.ExtremeShapedRecipe;
import fox.spiteful.avaritia.gui.GUIExtremeCrafting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import org.lwjgl.opengl.GL11;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.ShapedRecipeHandler;

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
                    PositionedStack stack = new PositionedStack(items[y * width + x], 3 + x * 18, 3 + y * 18);
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
        return "Xtreme Crafting";
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results)
    {
        if(outputId.equals("extended") && getClass() == ExtremeShapedRecipeHandler.class)
        {
            List<IRecipe> allrecipes = ExtremeCraftingManager.getInstance().getRecipeList();
            for(IRecipe irecipe : allrecipes)
            {
                CachedRecipe recipe = null;
                if(irecipe instanceof ExtremeShapedRecipe)
                    recipe = new CachedExtremeRecipe((ExtremeShapedRecipe)irecipe);

                if(recipe == null)
                    continue;

                this.arecipes.add(recipe);
            }
        }
        else
        {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result)
    {
        List<IRecipe> allrecipes = ExtremeCraftingManager.getInstance().getRecipeList();
        for(IRecipe irecipe : allrecipes)
        {
            if(NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result))
            {
                CachedRecipe recipe = null;
                if(irecipe instanceof ExtremeShapedRecipe)
                    recipe = new CachedExtremeRecipe((ExtremeShapedRecipe)irecipe);

                if(recipe == null)
                    continue;

                this.arecipes.add(recipe);
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient)
    {
        List<IRecipe> allrecipes = ExtremeCraftingManager.getInstance().getRecipeList();
        for(IRecipe irecipe : allrecipes)
        {
            if(irecipe instanceof ExtremeShapedRecipe)
            {
                CachedExtremeRecipe recipe = new CachedExtremeRecipe((ExtremeShapedRecipe)irecipe);
                if(recipe.contains(recipe.ingredients, ingredient))
                {
                    recipe.setIngredientPermutation(recipe.ingredients, ingredient);
                    if(!this.arecipes.contains(recipe))
                        this.arecipes.add(recipe);
                }
            }

        }
    }

    @Override
    public String getGuiTexture()
    {
        return "avaritia:textures/gui/dire_crafting_gui.png";
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
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 9, 5, 223, 166);
    }
}
