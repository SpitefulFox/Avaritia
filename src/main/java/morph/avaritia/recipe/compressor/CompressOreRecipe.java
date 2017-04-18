package morph.avaritia.recipe.compressor;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

public class CompressOreRecipe extends CompressorRecipe {

    private int oreID;

    public CompressOreRecipe(ItemStack output, int amount, String lex, boolean exact) {
        super(output, amount, null, exact);
        oreID = OreDictionary.getOreID(lex);
    }

    public CompressOreRecipe(ItemStack output, int amount, String lex) {
        this(output, amount, lex, false);
    }

    @Override
    public boolean validInput(ItemStack ingredient) {

        int[] ids = OreDictionary.getOreIDs(ingredient);
        for (int id : ids) {
            if (oreID == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getIngredientName() {
        ArrayList<ItemStack> ores = (ArrayList<ItemStack>) OreDictionary.getOres(OreDictionary.getOreName(oreID));
        if (!ores.isEmpty()) {
            return ores.get(0).getDisplayName();
        }
        return OreDictionary.getOreName(oreID);
    }

    @Override
    public Object getIngredient() {
        return OreDictionary.getOres(OreDictionary.getOreName(oreID));
    }

}
