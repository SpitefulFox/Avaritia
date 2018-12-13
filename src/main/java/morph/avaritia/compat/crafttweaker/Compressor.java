package morph.avaritia.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.compressor.CompressorRecipe;
import morph.avaritia.recipe.compressor.ICompressorRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.stream.Collectors;

/**
 * Created by covers1624 on 19/12/2017.
 */
@ZenRegister
@ZenClass ("mods.avaritia.Compressor")
public class Compressor {

    @ZenMethod
    public static void add(String name, IItemStack output, int amount, IIngredient input, @Optional boolean absolute) {
        NonNullList<Ingredient> ings = NonNullList.create();
        if (input == null) {
            throw new IllegalArgumentException("Input ingredient is null.");
        }
        ings.add(new CTIngredientWrapper(input));
        ICompressorRecipe recipe = new CompressorRecipe(CraftTweakerMC.getItemStack(output), amount, absolute, ings);
        if (name.contains(":")) {
            throw new IllegalArgumentException("Name must not contain ':'");
        }
        recipe.setRegistryName(new ResourceLocation("avaritia", "craft_tweaker/compressor/" + name));
        CraftTweakerAPI.apply(createAction(recipe));
    }

    @ZenMethod
    public static void remove(IItemStack stack) {
        ItemStack check = CraftTweakerMC.getItemStack(stack);
        RemoveRecipeAction<ICompressorRecipe> action = new RemoveRecipeAction<>("Compressor", AvaritiaRecipeManager.COMPRESSOR_RECIPES,//
                recipes -> recipes.stream()//
                        .filter(recipe -> recipe.getResult().isItemEqual(check))//
                        .map(IForgeRegistryEntry::getRegistryName)//
                        .collect(Collectors.toList()),//
                stack.getDisplayName());
        CraftTweakerAPI.apply(action);
    }

    @ZenMethod
    public static void removeAll() {
        RemoveRecipeAction<ICompressorRecipe> action = new RemoveRecipeAction<>("All Comoressor", AvaritiaRecipeManager.COMPRESSOR_RECIPES,//
                recipes -> recipes.stream()//
                        .map(IForgeRegistryEntry::getRegistryName)//
                        .collect(Collectors.toList()),//
                "");
        CraftTweakerAPI.apply(action);
    }

    private static IAction createAction(ICompressorRecipe recipe) {
        AddRecipeAction<ICompressorRecipe> action = new AddRecipeAction<>(recipe, AvaritiaRecipeManager.COMPRESSOR_RECIPES);
        action.setDesc("Compressor");
        action.setOutputAccessor(r -> r.getResult().getDisplayName());
        return action;
    }

}
