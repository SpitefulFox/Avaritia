package fox.spiteful.avaritia.crafting;

import net.minecraft.item.ItemStack;

public class CompressorRecipe {

    private ItemStack product;
    private int cost;
    private ItemStack input;

    public CompressorRecipe(ItemStack output, int amount, ItemStack ingredient){
        product = output;
        cost = amount;
        input = ingredient;
    }

    public ItemStack getOutput(){
        return product.copy();
    }

    public int getCost(){
        return cost;
    }

    public boolean validInput(ItemStack ingredient){
        return ingredient.isItemEqual(input);
    }

    public String getIngredientName(){
        return input.getDisplayName();
    }

    public Object getIngredient(){
        return input;
    }

}
