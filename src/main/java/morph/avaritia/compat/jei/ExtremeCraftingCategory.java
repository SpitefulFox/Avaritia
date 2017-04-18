package morph.avaritia.compat.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICustomCraftingRecipeWrapper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

import static morph.avaritia.compat.jei.RecipeCategoryUids.EXTREME_CRAFTING;

/**
 * Created by brandon3055 on 17/02/2017.
 */
public class ExtremeCraftingCategory extends BlankRecipeCategory<IRecipeWrapper> {

    private static final int craftOutputSlot = 0;
    private static final int craftInputSlot1 = 1;

    private final IDrawable background;
    private final String localizedName;
    private final ICraftingGridHelper craftingGridHelper;

    public ExtremeCraftingCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation("avaritia:textures/gui/extreme_jei.png");
        background = guiHelper.createDrawable(location, 0, 0, 189, 163);
        localizedName = I18n.format("crafting.extreme");
        craftingGridHelper = guiHelper.createCraftingGridHelper(craftInputSlot1, craftOutputSlot);
    }

    @Override
    public String getUid() {
        return EXTREME_CRAFTING;
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(craftOutputSlot, false, 167, 73);

        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 9; ++x) {
                int index = craftInputSlot1 + x + (y * 9);
                guiItemStacks.init(index, true, (x * 18) + 1, (y * 18) + 1);
            }
        }

        if (recipeWrapper instanceof ICustomCraftingRecipeWrapper) {
            ICustomCraftingRecipeWrapper customWrapper = (ICustomCraftingRecipeWrapper) recipeWrapper;
            customWrapper.setRecipe(recipeLayout, ingredients);
            return;
        }

        List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
        List<ItemStack> outputs = ingredients.getOutputs(ItemStack.class);

        if (recipeWrapper instanceof IShapedCraftingRecipeWrapper) {
            IShapedCraftingRecipeWrapper wrapper = (IShapedCraftingRecipeWrapper) recipeWrapper;
            craftingGridHelper.setInputStacks(guiItemStacks, inputs, wrapper.getWidth(), wrapper.getHeight());
            craftingGridHelper.setOutput(guiItemStacks, outputs);
        } else {
            craftingGridHelper.setInputStacks(guiItemStacks, inputs);
            craftingGridHelper.setOutput(guiItemStacks, outputs);
        }
    }
}
