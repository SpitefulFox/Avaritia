package fox.spiteful.avaritia.crafting;

import cpw.mods.fml.common.Loader;
import fox.spiteful.avaritia.Config;

public class Gregorizer {

    public static int modifier = 0;
    public static int multiplier = 1;

    public static void balance(){
        if(Loader.isModLoaded("Thaumcraft"))
            modifier += 100;
        if(Loader.isModLoaded("TConstruct") || Loader.isModLoaded("HydCraft"))
            modifier += 100;
        if(Loader.isModLoaded("ThermalExpansion") || Loader.isModLoaded("TSteelworks") || Loader.isModLoaded("IC2") || Loader.isModLoaded("ThaumicTinkerer"))
            modifier += 300;
        if(Loader.isModLoaded("technom"))
            modifier += 600;
        if(Loader.isModLoaded("magicalcrops"))
            multiplier += 1;
        if(Loader.isModLoaded("AgriCraft"))
            multiplier += 1;
        if(Loader.isModLoaded("MineFactoryReloaded"))
            multiplier += 9;
        if(Loader.isModLoaded("BigReactors"))
            modifier += 100;
        if(Loader.isModLoaded("EE3"))
            multiplier += 1;
        else if(Loader.isModLoaded("ProjectE"))
            multiplier += 3;
        if(Loader.isModLoaded("Botania"))
            modifier += 50;
        if(Loader.isModLoaded("ExtraUtilities"))
            modifier += 500;
        if(Loader.isModLoaded("appliedenergistics2"))
            modifier += 200;
        if(Loader.isModLoaded("ImmersiveEngineering"))
            modifier += 300;
        if(Loader.isModLoaded("Mekanism")) {
            modifier += 500;
            multiplier += 1;
        }
        if(Loader.isModLoaded("Torcherino"))
            multiplier += 2;
        if(Loader.isModLoaded("DraconicEvolution")) {
            modifier += 300;
            multiplier += 1;
        }

        modifier = Math.max(modifier + Config.modifier, 0);
        multiplier = Math.max(multiplier + Config.multiplier, 1);
    }

    public static int balanceCost(int cost){
        return (cost + modifier) * multiplier;
    }

}
