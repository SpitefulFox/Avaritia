package morph.avaritia.recipe;

import codechicken.lib.recipe.AbstractConfigConditionalFactory;
import morph.avaritia.handler.ConfigHandler;

/**
 * Created by covers1624 on 8/11/2017.
 */
public class ConfigConditionalFactory extends AbstractConfigConditionalFactory {

    public ConfigConditionalFactory() {
        super(ConfigHandler.config);
    }
}
