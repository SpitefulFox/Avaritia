package morph.avaritia.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.extreme.ExtremeShapedRecipe;
import morph.avaritia.recipe.extreme.ExtremeShapelessRecipe;
import morph.avaritia.recipe.extreme.IExtremeRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.registries.IForgeRegistryEntry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.stream.Collectors;

/**
 * Created by covers1624 on 19/12/2017.
 */
@ZenRegister
@ZenClass ("mods.avaritia.ExtremeCrafting")
public class ExtremeCrafting {

    @ZenMethod
    public static void addShapeless(String name, IItemStack output, IIngredient[] input) {
        IExtremeRecipe recipe = new ExtremeShapelessRecipe(getIngredients(input), CraftTweakerMC.getItemStack(output));
        if (name.contains(":")) {
            throw new IllegalArgumentException("Name must not contain ':'");
        }
        recipe.setRegistryName(new ResourceLocation("craft_tweaker/extreme/shapeless/" + name));
        CraftTweakerAPI.apply(createAction(recipe));
    }

    @ZenMethod
    public static void addShaped(String name, IItemStack output, IIngredient[][] ingredients) {
        ShapedPrimer primer = new ShapedPrimer();
        primer.height = ingredients.length;
        for (IIngredient[] row : ingredients) {
            primer.width = Math.max(primer.width, row.length);
        }
        primer.input = NonNullList.withSize(primer.width * primer.height, Ingredient.EMPTY);
        int x = 0;
        for (IIngredient[] row : ingredients) {
            for (IIngredient ing : row) {
                primer.input.set(x++, ing == null ? Ingredient.EMPTY : new CTIngredientWrapper(ing));
            }
        }
        IExtremeRecipe recipe = new ExtremeShapedRecipe(CraftTweakerMC.getItemStack(output), primer);
        recipe.setRegistryName(new ResourceLocation("avaritia", "craft_tweaker_shaped_" + name));
        CraftTweakerAPI.apply(createAction(recipe));
    }

    @ZenMethod
    public static void remove(IItemStack stack) {
        ItemStack check = CraftTweakerMC.getItemStack(stack);
        RemoveRecipeAction<IExtremeRecipe> action = new RemoveRecipeAction<>("Extreme", AvaritiaRecipeManager.EXTREME_RECIPES,//
                recipes -> recipes.stream()//
                        .filter(recipe -> recipe.getRecipeOutput().isItemEqual(check))//
                        .map(IForgeRegistryEntry::getRegistryName)//
                        .collect(Collectors.toList()),//
                stack.getDisplayName());
        CraftTweakerAPI.apply(action);
    }

    @ZenMethod
    public static void removeShaped(IItemStack stack) {
        ItemStack check = CraftTweakerMC.getItemStack(stack);
        RemoveRecipeAction<IExtremeRecipe> action = new RemoveRecipeAction<>("Extreme Shaped", AvaritiaRecipeManager.EXTREME_RECIPES,//
                recipes -> recipes.stream()//
                        .filter(IExtremeRecipe::isShapedRecipe)//
                        .filter(recipe -> recipe.getRecipeOutput().isItemEqual(check))//
                        .map(IForgeRegistryEntry::getRegistryName)//
                        .collect(Collectors.toList()),//
                stack.getDisplayName());
        CraftTweakerAPI.apply(action);
    }

    @ZenMethod
    public static void removeShapeless(IItemStack stack) {
        ItemStack check = CraftTweakerMC.getItemStack(stack);
        RemoveRecipeAction<IExtremeRecipe> action = new RemoveRecipeAction<>("Extreme Shapeless", AvaritiaRecipeManager.EXTREME_RECIPES,//
                recipes -> recipes.stream()//
                        .filter(r -> !r.isShapedRecipe())//
                        .filter(r -> r.getRecipeOutput().isItemEqual(check))//
                        .map(IForgeRegistryEntry::getRegistryName)//
                        .collect(Collectors.toList()),//
                stack.getDisplayName());
        CraftTweakerAPI.apply(action);
    }

    @ZenMethod
    public static void removeAll() {
        RemoveRecipeAction<IExtremeRecipe> action = new RemoveRecipeAction<>("All Extreme", AvaritiaRecipeManager.EXTREME_RECIPES,//
                recipes -> recipes.stream()//
                        .map(IForgeRegistryEntry::getRegistryName)//
                        .collect(Collectors.toList()),//
                "");
        CraftTweakerAPI.apply(action);
    }

    @ZenMethod
    public static void removeAllShaped() {
        RemoveRecipeAction<IExtremeRecipe> action = new RemoveRecipeAction<>("All Extreme Shaped", AvaritiaRecipeManager.EXTREME_RECIPES,//
                recipes -> recipes.stream()//
                        .filter(IExtremeRecipe::isShapedRecipe)//
                        .map(IForgeRegistryEntry::getRegistryName)//
                        .collect(Collectors.toList()),//
                "");
        CraftTweakerAPI.apply(action);
    }

    @ZenMethod
    public static void removeAllShapeless() {
        RemoveRecipeAction<IExtremeRecipe> action = new RemoveRecipeAction<>("All Extreme Shapeless", AvaritiaRecipeManager.EXTREME_RECIPES,//
                recipes -> recipes.stream()//
                        .filter(r -> !r.isShapedRecipe())//
                        .map(IForgeRegistryEntry::getRegistryName)//
                        .collect(Collectors.toList()),//
                "");
        CraftTweakerAPI.apply(action);
    }

    private static IAction createAction(IExtremeRecipe recipe) {
        AddRecipeAction<IExtremeRecipe> action = new AddRecipeAction<>(recipe, AvaritiaRecipeManager.EXTREME_RECIPES);
        action.setDesc("Extreme");
        action.setOutputAccessor(r -> r.getRecipeOutput().getDisplayName());
        return action;
    }

    private static NonNullList<Ingredient> getIngredients(IIngredient[] input) {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        for (IIngredient ing : input) {
            ingredients.add(ing == null ? Ingredient.EMPTY : new CTIngredientWrapper(ing));
        }
        return ingredients;
    }

}
