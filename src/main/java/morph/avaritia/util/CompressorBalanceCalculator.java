package morph.avaritia.util;

import morph.avaritia.handler.ConfigHandler;
import net.minecraftforge.fml.common.Loader;

public class CompressorBalanceCalculator {

    public static int modifier = 0;
    public static int multiplier = 1;

    public static void gatherBalanceModifier() {//TODO, Update these for the correct ModID's
        if (Loader.isModLoaded("thaumcraft")) {
            modifier += 100;
        }
        if (Loader.isModLoaded("tconstruct")) {
            modifier += 100;
            multiplier += 1;
        }
        if (Loader.isModLoaded("thermalexpansion") || Loader.isModLoaded("ic2") || Loader.isModLoaded("thaumictinkerer")) {
            modifier += 300;
        }
        if (Loader.isModLoaded("technom")) {
            modifier += 600;
        }
        if (Loader.isModLoaded("mysticalagriculture")) {
            multiplier += 1;
        }
        if (Loader.isModLoaded("agricraft")) {
            multiplier += 1;
        }
        if (Loader.isModLoaded("minefactoryreloaded")) {
            multiplier += 3;
        }
        if (Loader.isModLoaded("bigreactors")) {
            modifier += 100;
        }//The mod name is ExtremeReactors but in game still uses bigreactors for items
        if (Loader.isModLoaded("ee3")) {
            multiplier += 1;
        } else if (Loader.isModLoaded("projecte")) {
            multiplier += 3;
        }
        if (Loader.isModLoaded("botania")) {
            modifier += 50;
        }
        if (Loader.isModLoaded("extrautils2")) {
            modifier += 500;
        }
        if (Loader.isModLoaded("appliedenergistics2")) {
            modifier += 200;
        }
        if (Loader.isModLoaded("immersiveengineering")) {
            modifier += 300;
        }
        if (Loader.isModLoaded("mekanism")) {
            modifier += 500;
            multiplier += 1;

        }
        if (Loader.isModLoaded("torcherino")) {
            multiplier += 2;
        }
        if (Loader.isModLoaded("draconicevolution")) {
            modifier += 300;
            multiplier += 1;
        }
        if (Loader.isModLoaded("rftools") || Loader.isModLoaded("rftoolsdim")) {
            modifier += 300;
            multiplier += 1;
        }

        modifier = Math.max(modifier + ConfigHandler.modifier, 0);
        multiplier = Math.max(multiplier + ConfigHandler.multiplier, 0);
    }

    public static int balanceCost(int cost) {
        return (cost + modifier) * multiplier;
    }

}
