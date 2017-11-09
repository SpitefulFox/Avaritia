package morph.avaritia.recipe;

import com.google.gson.JsonObject;
import net.minecraftforge.common.crafting.JsonContext;

/**
 * Created by covers1624 on 10/10/2017.
 */
@FunctionalInterface
public interface IRecipeFactory<R> {

    R load(JsonContext ctx, JsonObject json);

}
