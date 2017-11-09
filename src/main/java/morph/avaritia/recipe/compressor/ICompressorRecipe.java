package morph.avaritia.recipe.compressor;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.List;

/**
 * Created by covers1624 on 8/11/2017.
 */
public interface ICompressorRecipe {

    ItemStack getResult();

    int getCost();

    List<Ingredient> getIngredients();

    default boolean matches(ItemStack stack) {
        for (Ingredient ing : getIngredients()) {
            if (ing.apply(stack)) {
                return true;
            }
        }
        return false;
    }
}
