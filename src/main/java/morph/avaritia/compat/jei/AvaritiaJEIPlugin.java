package morph.avaritia.compat.jei;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import morph.avaritia.client.gui.GUIExtremeCrafting;
import morph.avaritia.init.ModBlocks;
import morph.avaritia.recipe.extreme.ExtremeCraftingManager;
import net.minecraft.item.ItemStack;

/**
 * Created by brandon3055 on 17/02/2017.
 */
@JEIPlugin
public class AvaritiaJEIPlugin implements IModPlugin {

    @Override
    public void registerItemSubtypes(ISubtypeRegistry iSubtypeRegistry) {

    }

    @Override
    public void registerIngredients(IModIngredientRegistration iModIngredientRegistration) {

    }

    @Override
    public void register(IModRegistry registry) {
        IJeiHelpers helpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = helpers.getGuiHelper();

        registry.addRecipeCategories(new ExtremeCraftingCategory(guiHelper));

        registry.addRecipeHandlers(new ExtremeShapedRecipeHandler(), new ExtremeShapedOreRecipeHandler(helpers), new ExtremeShelessRecipeHandler(), new ExtremeShapelessOreRecipeHandler(helpers));

        registry.addRecipeClickArea(GUIExtremeCrafting.class, 175, 79, 28, 26, RecipeCategoryUids.EXTREME_CRAFTING);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.dire_craft), RecipeCategoryUids.EXTREME_CRAFTING);

        registry.addRecipes(ExtremeCraftingManager.getInstance().getRecipeList());
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime iJeiRuntime) {

    }
}
