package morph.avaritia.compat.jei.compressor;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiIngredientGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import morph.avaritia.Avaritia;
import morph.avaritia.compat.jei.AvaritiaJEIPlugin;
import net.minecraft.client.resources.I18n;

/**
 * Created by covers1624 on 31/07/2017.
 */
public class CompressorRecipeCategory extends BlankRecipeCategory<CompressorRecipeWrapper> {

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    private final String localizedName;

    public CompressorRecipeCategory() {
        localizedName = I18n.format("container.neutronium_compressor");
    }

    @Override
    public String getUid() {
        return AvaritiaJEIPlugin.NEUTRONIUM_COMPRESSOR;
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
        return AvaritiaJEIPlugin.neutronium_compressor;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CompressorRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiIngredientGroup stackGroup = recipeLayout.getItemStacks();
        stackGroup.init(INPUT_SLOT, true, 1, 5);
        stackGroup.init(OUTPUT_SLOT, false, 79, 5);
        stackGroup.set(ingredients);
    }
}
