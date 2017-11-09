package morph.avaritia.recipe.extreme;

import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Created by covers1624 on 9/10/2017.
 */
public abstract class ExtremeRecipeBase extends IForgeRegistryEntry.Impl<IExtremeRecipe> implements IExtremeRecipe {

    @Override
    public boolean canFit(int width, int height) {
        return width >= 9 && height >= 9;
    }
}
