package morph.avaritia.compat.jei.extreme;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import morph.avaritia.Avaritia;
import morph.avaritia.compat.jei.AvaritiaJEIPlugin;
import net.minecraft.client.resources.I18n;

import static morph.avaritia.compat.jei.AvaritiaJEIPlugin.EXTREME_CRAFTING;

/**
 * Created by brandon3055 on 17/02/2017.
 */
public class ExtremeCraftingCategory extends BlankRecipeCategory<ExtremeRecipeWrapper> {

    private static final int craftOutputSlot = 0;
    private static final int craftInputSlot1 = 1;

    private final String localizedName;

    public ExtremeCraftingCategory() {
        localizedName = I18n.format("crafting.extreme");
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
    public String getModName() {
        return Avaritia.MOD_NAME;
    }

    @Override
    public IDrawable getBackground() {
        return AvaritiaJEIPlugin.extreme_crafting;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ExtremeRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(craftOutputSlot, false, 167, 73);

        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 9; ++x) {
                int index = craftInputSlot1 + x + (y * 9);
                guiItemStacks.init(index, true, (x * 18) + 1, (y * 18) + 1);
            }
        }
        guiItemStacks.set(ingredients);
    }
}
