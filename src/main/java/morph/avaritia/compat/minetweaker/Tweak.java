package morph.avaritia.compat.minetweaker;

import minetweaker.MineTweakerAPI;
import morph.avaritia.handler.ConfigHandler;

public class Tweak {

    public static void registrate() {
        MineTweakerAPI.registerClass(ExtremeCrafting.class);
        if (ConfigHandler.craftingOnly) {
            return;
        }

        MineTweakerAPI.registerClass(Compressor.class);
    }
}
