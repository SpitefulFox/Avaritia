package morph.avaritia.compat.crafttweaker;

import crafttweaker.IAction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Map;
import java.util.function.Function;

/**
 * Created by covers1624 on 19/12/2017.
 */
public class AddRecipeAction<R extends IForgeRegistryEntry<R>> implements IAction {

    private final R recipe;
    private final Map<ResourceLocation, R> registry;

    private String desc;
    private Function<R, String> outputAccessor;

    public AddRecipeAction(R recipe, Map<ResourceLocation, R> registry) {
        this.recipe = recipe;
        this.registry = registry;
    }

    @Override
    public void apply() {
        registry.put(recipe.getRegistryName(), recipe);
    }

    @Override
    public String describe() {
        if (desc != null && outputAccessor != null) {
            return String.format("Adding %s recipe %s for %s", desc, recipe.getRegistryName(), outputAccessor.apply(recipe));
        } else {
            return String.format("WARNING, Invalid use of AddRecipeAction!!!, Adding avaritia recipe for the recipe class %s: ", recipe.getClass().getName());
        }
    }

    public void setOutputAccessor(Function<R, String> outputAccessor) {
        this.outputAccessor = outputAccessor;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
