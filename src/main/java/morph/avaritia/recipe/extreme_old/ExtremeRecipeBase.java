package morph.avaritia.recipe.extreme_old;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Created by covers1624 on 21/09/2017.
 */
public abstract class ExtremeRecipeBase extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    @Override
    public boolean canFit(int p_194133_1_, int p_194133_2_) {
        return p_194133_1_ >= 9 && p_194133_2_ >= 9;
    }


}
