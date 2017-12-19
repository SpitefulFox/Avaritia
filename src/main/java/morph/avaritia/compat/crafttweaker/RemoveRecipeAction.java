package morph.avaritia.compat.crafttweaker;

import com.google.common.base.Joiner;
import crafttweaker.IAction;
import morph.avaritia.handler.ConfigHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by covers1624 on 19/12/2017.
 */
public class RemoveRecipeAction<R extends IForgeRegistryEntry<R>> implements IAction {

    private final String desc;
    private final Map<ResourceLocation, R> registry;
    private final Function<Collection<R>, List<ResourceLocation>> filter;
    private final String shortDesc;

    public RemoveRecipeAction(String desc, Map<ResourceLocation, R> registry, Function<Collection<R>, List<ResourceLocation>> filter, String shortDesc) {
        this.desc = desc;
        this.registry = registry;
        this.filter = filter;
        this.shortDesc = shortDesc;
    }

    @Override
    public void apply() {
        filter.apply(registry.values()).forEach(registry::remove);
    }

    @Override
    public String describe() {
        List<ResourceLocation> toRemove = filter.apply(registry.values());
        StringBuilder builder = new StringBuilder("Removing ");
        builder.append(desc);
        builder.append(" Recipe");
        if (toRemove.size() > 1) {
            builder.append("s");
        }
        builder.append(" from Avaritia");
        if (!ConfigHandler.verboseCraftTweekerLogging) {
            if (shortDesc.isEmpty()) {
                builder.append(".");
            } else {
                builder.append(" with the output of: ");
                builder.append(shortDesc);
            }
        } else {
            builder.append(". Recipe names:");
            if (!toRemove.isEmpty()) {
                builder.append("\n\t");
                builder.append(Joiner.on("\n\t").join(toRemove));
            } else {
                builder.append("ERROR, empty list!");
            }
        }
        return builder.toString();
    }
}
