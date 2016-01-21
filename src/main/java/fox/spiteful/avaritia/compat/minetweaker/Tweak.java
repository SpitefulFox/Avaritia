package fox.spiteful.avaritia.compat.minetweaker;

import fox.spiteful.avaritia.Config;
import minetweaker.MineTweakerAPI;

public class Tweak {

    public static void registrate(){
        MineTweakerAPI.registerClass(ExtremeCrafting.class);
        if(Config.craftingOnly)
            return;

        MineTweakerAPI.registerClass(Compressor.class);
    }
}
