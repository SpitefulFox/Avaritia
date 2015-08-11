package fox.spiteful.avaritia.crafting;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

public class CompressOreRecipe extends CompressorRecipe {

    private int oreID;

    public CompressOreRecipe(ItemStack output, int amount, String lex){
        super(output, amount, null);
        oreID = OreDictionary.getOreID(lex);
    }

    @Override
    public boolean validInput(ItemStack ingredient){

        int[] ids = OreDictionary.getOreIDs(ingredient);
        for(int x = 0;x < ids.length;x++){
            if(oreID == ids[x])
                return true;
        }
        return false;
    }

    @Override
    public String getIngredientName(){
        ArrayList<ItemStack> ores = OreDictionary.getOres(OreDictionary.getOreName(oreID));
        if(!ores.isEmpty())
            return ores.get(0).getDisplayName();
        return OreDictionary.getOreName(oreID);
    }

    @Override
    public Object getIngredient(){
        return OreDictionary.getOres(OreDictionary.getOreName(oreID));
    }

}
