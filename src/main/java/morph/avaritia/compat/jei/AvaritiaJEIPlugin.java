package morph.avaritia.compat.jei;

import mezz.jei.api.*;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import morph.avaritia.client.gui.GUIExtremeCrafting;
import morph.avaritia.client.gui.GUINeutroniumCompressor;
import morph.avaritia.compat.jei.compressor.CompressorRecipeCategory;
import morph.avaritia.compat.jei.compressor.CompressorRecipeWrapper;
import morph.avaritia.compat.jei.extreme.ExtremeCraftingCategory;
import morph.avaritia.compat.jei.extreme.ExtremeRecipeWrapper;
import morph.avaritia.container.ContainerExtremeCrafting;
import morph.avaritia.init.ModBlocks;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.compressor.ICompressorRecipe;
import morph.avaritia.recipe.extreme.ExtremeShapedRecipe;
import morph.avaritia.recipe.extreme.ExtremeShapelessRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JEIPlugin
public class AvaritiaJEIPlugin implements IModPlugin {

    public static final String EXTREME_CRAFTING = "Avatitia.Extreme";
    public static final String NEUTRONIUM_COMPRESSOR = "Avatitia.Compressor";

    public static IJeiHelpers jeiHelpers;

    public static IDrawableStatic extreme_crafting;
    public static IDrawableStatic neutronium_compressor;

    public static IDrawableStatic static_singularity;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new ExtremeCraftingCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new CompressorRecipeCategory());
    }

    @Override
    public void register(IModRegistry registry) {
        jeiHelpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        setupDrawables(guiHelper);

        registry.addRecipes(AvaritiaRecipeManager.EXTREME_RECIPES.values(), EXTREME_CRAFTING);
        registry.handleRecipes(ExtremeShapedRecipe.class, ExtremeRecipeWrapper::new, EXTREME_CRAFTING);
        registry.handleRecipes(ExtremeShapelessRecipe.class, ExtremeRecipeWrapper::new, EXTREME_CRAFTING);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.extremeCraftingTable), EXTREME_CRAFTING);
        registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerExtremeCrafting.class, EXTREME_CRAFTING, 1, 81, 82, 36);
        registry.addRecipeClickArea(GUIExtremeCrafting.class, 175, 79, 28, 26, EXTREME_CRAFTING);

        registry.addRecipes(AvaritiaRecipeManager.COMPRESSOR_RECIPES.values(), NEUTRONIUM_COMPRESSOR);
        registry.handleRecipes(ICompressorRecipe.class, CompressorRecipeWrapper::new, NEUTRONIUM_COMPRESSOR);
        registry.addRecipeClickArea(GUINeutroniumCompressor.class, 62, 35, 22, 15, NEUTRONIUM_COMPRESSOR);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.neutronium_compressor), NEUTRONIUM_COMPRESSOR);
    }

    private static void setupDrawables(IGuiHelper helper) {
        ResourceLocation location = new ResourceLocation("avaritia:textures/gui/extreme_jei.png");
        extreme_crafting = helper.createDrawable(location, 0, 0, 189, 163);

        location = new ResourceLocation("avaritia:textures/gui/compressor.png");
        neutronium_compressor = helper.createDrawable(location, 37, 29, 102, 41);
        static_singularity = helper.createDrawable(location, 176, 16, 16, 16);
    }
}
