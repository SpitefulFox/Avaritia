package morph.avaritia.recipe.compressor_old;

import morph.avaritia.util.CompressorBalanceCalculator;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class CompressorRecipe {

    private ItemStack product;
    private int cost;
    private ItemStack input;
    private boolean specific;

    public CompressorRecipe(ItemStack output, int amount, ItemStack ingredient, boolean exact) {
        product = output;
        cost = amount;
        input = ingredient;
        specific = exact;
    }

    public CompressorRecipe(ItemStack output, int amount, ItemStack ingredient) {
        this(output, amount, ingredient, false);
    }

    public ItemStack getOutput() {
        return product.copy();
    }

    public List<ItemStack> getInputs() {
        return Collections.singletonList(input);
    }

    public int getCost() {
        if (specific) {
            return cost;
        } else {
            return CompressorBalanceCalculator.balanceCost(cost);
        }
    }

    public boolean isValidInput(ItemStack ingredient) {
        return ingredient.isItemEqual(input);
    }

    public String getIngredientName() {
        return input.getDisplayName();
    }

    public Object getIngredient() {
        return input;
    }

}
