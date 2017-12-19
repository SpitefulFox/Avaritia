package morph.avaritia.compat.jei.compressor;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableAnimated.StartDirection;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import morph.avaritia.compat.jei.AvaritiaJEIPlugin;
import morph.avaritia.recipe.compressor.ICompressorRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by covers1624 on 31/07/2017.
 */
public class CompressorRecipeWrapper implements IRecipeWrapper {

    //Defines the offset of the text relative to the top left corner of the background drawable.
    private static final int TEXT_Y_OFF = 19;

    public final ICompressorRecipe recipe;

    private final IDrawableAnimated singularity;

    public CompressorRecipeWrapper(ICompressorRecipe recipe) {
        this.recipe = recipe;
        IGuiHelper helper = AvaritiaJEIPlugin.jeiHelpers.getGuiHelper();
        singularity = helper.createAnimatedDrawable(AvaritiaJEIPlugin.static_singularity, Math.max(recipe.getCost() / 16, 1), StartDirection.BOTTOM, false);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, getRecipeInputs(recipe));
        ingredients.setOutput(ItemStack.class, recipe.getResult());
    }

    protected List<List<ItemStack>> getRecipeInputs(ICompressorRecipe recipe) {
        return AvaritiaJEIPlugin.jeiHelpers.getStackHelper().expandRecipeItemStackInputs(recipe.getIngredients());
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        singularity.draw(minecraft, 53, 6);
        String thing = "Input Amount: " + recipe.getCost();
        minecraft.fontRenderer.drawString(thing, recipeWidth / 2 - minecraft.fontRenderer.getStringWidth(thing) / 2, 31, 0x404040);
    }
}
