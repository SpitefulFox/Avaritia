package fox.spiteful.avaritia.crafting;

import cpw.mods.fml.common.Loader;

public class Gregorizer {

    public static int modifier = 0;
    public static int multiplier = 1;

    public static void balance(){
        if(Loader.isModLoaded("magicalcrops"))
            multiplier += 3;
        if(Loader.isModLoaded("MineFactoryReloaded"))
            multiplier += 1;
        if(Loader.isModLoaded("BigReactors"))
            modifier += 100;
        if(Loader.isModLoaded("ProjectE"))
            multiplier += 9;
        if(Loader.isModLoaded("EE3"))
            multiplier += 9;
        if(Loader.isModLoaded("Botania"))
            modifier += 50;
        if(Loader.isModLoaded("IC2"))
            modifier += 300;
        if(Loader.isModLoaded("ThermalExpansion"))
            modifier += 300;
        if(Loader.isModLoaded("technom"))
            modifier += 500;
        if(Loader.isModLoaded("ExtraUtilities"))
            modifier += 100;
        if(Loader.isModLoaded("Thaumcraft"))
            modifier += 100;
        if(Loader.isModLoaded("appliedenergistics2"))
            modifier += 200;
        if(Loader.isModLoaded("gregtech"))
            multiplier += 3;
        if(Loader.isModLoaded("TSteelworks"))
            modifier += 300;
    }

    public static int balanceCost(int cost){
        return (cost + modifier) * multiplier;
    }

}
