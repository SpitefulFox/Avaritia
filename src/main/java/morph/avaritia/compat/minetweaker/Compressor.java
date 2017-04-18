package morph.avaritia.compat.minetweaker;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.oredict.IOreDictEntry;
import morph.avaritia.recipe.compressor.CompressOreRecipe;
import morph.avaritia.recipe.compressor.CompressorManager;
import morph.avaritia.recipe.compressor.CompressorRecipe;
import morph.avaritia.recipe.extreme.ExtremeCraftingManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass ("mods.avaritia.Compressor")
public class Compressor {

    @ZenMethod
    public static void add(IItemStack output, int amount, IIngredient input, boolean exact) {
        CompressorRecipe recipe = null;
        if (input instanceof IOreDictEntry) {
            recipe = new CompressOreRecipe(toStack(output), amount, toString((IOreDictEntry) input), exact);
        } else if (input instanceof IItemStack) {
            recipe = new CompressorRecipe(toStack(output), amount, toStack((IItemStack) input), exact);
        }
        if (recipe != null) {
            MineTweakerAPI.apply(new Add(recipe));
        }
    }

    @ZenMethod
    public static void add(IItemStack output, int amount, IIngredient input) {
        add(output, amount, input, true);
    }

    @ZenMethod
    public static void remove(IItemStack output) {
        MineTweakerAPI.apply(new Remove(toStack(output)));
    }

    private static class Add implements IUndoableAction {

        CompressorRecipe recipe;

        public Add(CompressorRecipe add) {
            recipe = add;
        }

        @Override
        public void apply() {
            CompressorManager.getRecipes().add(recipe);
            MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(recipe);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            CompressorManager.getRecipes().remove(recipe);
            MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(recipe);
        }

        @Override
        public String describe() {
            return "Adding Compressor Recipe for " + recipe.getOutput().getDisplayName();
        }

        @Override
        public String describeUndo() {
            return "Un-adding Compressor Recipe for " + recipe.getOutput().getDisplayName();
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }

    }

    private static class Remove implements IUndoableAction {

        CompressorRecipe recipe = null;
        ItemStack remove;

        public Remove(ItemStack rem) {
            remove = rem;
        }

        @Override
        public void apply() {

            for (Object obj : ExtremeCraftingManager.getInstance().getRecipeList()) {
                if (obj instanceof IRecipe) {
                    CompressorRecipe craft = (CompressorRecipe) obj;
                    if (craft.getOutput().isItemEqual(remove)) {
                        recipe = craft;
                        CompressorManager.getRecipes().remove(obj);
                        MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(obj);
                        break;
                    }
                }
            }
        }

        @Override
        public boolean canUndo() {
            return recipe != null;
        }

        @Override
        public void undo() {
            CompressorManager.getRecipes().add(recipe);
            MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(recipe);
        }

        @Override
        public String describe() {
            return "Removing Compressor Recipe for " + remove.getDisplayName();
        }

        @Override
        public String describeUndo() {
            return "Un-removing Compressor Recipe for " + remove.getDisplayName();
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }

    }

    private static ItemStack toStack(IItemStack item) {
        if (item == null) {
            return null;
        } else {
            Object internal = item.getInternal();
            if (internal == null || !(internal instanceof ItemStack)) {
                MineTweakerAPI.getLogger().logError("Not a valid item stack: " + item);
            }
            return (ItemStack) internal;
        }
    }

    private static Object toObject(IIngredient ingredient) {
        if (ingredient == null) {
            return null;
        } else {
            if (ingredient instanceof IOreDictEntry) {
                return toString((IOreDictEntry) ingredient);
            } else if (ingredient instanceof IItemStack) {
                return toStack((IItemStack) ingredient);
            } else {
                return null;
            }
        }
    }

    private static Object[] toObjects(IIngredient[] list) {
        if (list == null) {
            return null;
        }
        Object[] ingredients = new Object[list.length];
        for (int x = 0; x < list.length; x++) {
            ingredients[x] = toObject(list[x]);
        }
        return ingredients;
    }

    private static Object toActualObject(IIngredient ingredient) {
        if (ingredient == null) {
            return null;
        } else {
            if (ingredient instanceof IOreDictEntry) {
                return OreDictionary.getOres(toString((IOreDictEntry) ingredient));
            } else if (ingredient instanceof IItemStack) {
                return toStack((IItemStack) ingredient);
            } else {
                return null;
            }
        }
    }

    private static String toString(IOreDictEntry entry) {
        return entry.getName();
    }
}
