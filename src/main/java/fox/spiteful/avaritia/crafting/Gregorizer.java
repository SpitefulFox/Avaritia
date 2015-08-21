package fox.spiteful.avaritia.crafting;

import cpw.mods.fml.common.Loader;
import fox.spiteful.avaritia.Config;

public class Gregorizer {

    public static int modifier = 0;
    public static int multiplier = 1;

    public static void balance(){
        if(Loader.isModLoaded("magicalcrops"))
            multiplier += 1;
        if(Loader.isModLoaded("MineFactoryReloaded"))
            multiplier += 1;
        if(Loader.isModLoaded("BigReactors"))
            modifier += 100;
        if(Loader.isModLoaded("EE3")||Loader.isModLoaded("ProjectE"))
            multiplier += 2;
        if(Loader.isModLoaded("Botania"))
            modifier += 50;
        if(Loader.isModLoaded("IC2"))
            modifier += 300;
        if(Loader.isModLoaded("ThermalExpansion"))
            modifier += 300;
        if(Loader.isModLoaded("technom"))
            modifier += 500;
        if(Loader.isModLoaded("ExtraUtilities"))
            modifier += 150;
        if(Loader.isModLoaded("Thaumcraft"))
            modifier += 100;
        if(Loader.isModLoaded("appliedenergistics2"))
            modifier += 200;
        if(Loader.isModLoaded("TSteelworks"))
            modifier += 300;
        if(Loader.isModLoaded("ImmersiveEngineering"))
            modifier += 400;
        if(Loader.isModLoaded("Mekanism")) {
            modifier += 300;
            multiplier += 1;
        }

        modifier += Config.modifier;
        multiplier += Config.multiplier;
    }

    public static int balanceCost(int cost){
        return (cost + modifier) * multiplier;
    }

}
