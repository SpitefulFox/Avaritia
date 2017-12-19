package morph.avaritia.recipe.compressor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import morph.avaritia.util.CompressorBalanceCalculator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.List;

/**
 * Created by covers1624 on 8/11/2017.
 */
public class CompressorRecipe extends IForgeRegistryEntry.Impl<ICompressorRecipe> implements ICompressorRecipe {

    protected ItemStack result;
    protected int cost;
    protected boolean absolute;
    protected List<Ingredient> ingredients;

    public CompressorRecipe(ItemStack result, int cost, boolean absolute, List<Ingredient> ingredients) {
        this.result = result;
        this.cost = cost;
        this.absolute = absolute;
        this.ingredients = ingredients;
    }

    @Override
    public ItemStack getResult() {
        return result;
    }

    @Override
    public int getCost() {
        if (absolute) {
            return cost;
        }
        return CompressorBalanceCalculator.balanceCost(cost);
    }

    @Override
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public static ICompressorRecipe fromJson(JsonContext context, JsonObject json) {
        ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
        int cost = JsonUtils.getInt(json, "cost");
        boolean absolute = JsonUtils.getBoolean(json, "absolute", false);
        NonNullList<Ingredient> ings = NonNullList.create();
        for (JsonElement ele : JsonUtils.getJsonArray(json, "ingredients")) {
            Ingredient i = CraftingHelper.getIngredient(ele, context);
            if (i != Ingredient.EMPTY) {
                ings.add(i);
            }
        }
        if (ings.isEmpty()) {
            return null;
        }
        return new CompressorRecipe(result, cost, absolute, ings);
    }
}
