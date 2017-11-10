package fox.spiteful.avaritia;

import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

import java.io.File;

public class Config {

    public static boolean craftingOnly = false;
    public static boolean endStone = true;
    public static boolean bedrockBreaker = true;
    public static boolean boringFood = false;
    public static boolean fractured = false;
    public static boolean fast = true;

    public static boolean thaumic = true;
    public static boolean sc2 = true;
    public static boolean ae2 = true;
    public static boolean exu = true;
    public static boolean ic2 = true;
    public static boolean gt = true;
    public static boolean botan = true;
    public static boolean blood = true;
    public static boolean lolDargon = true;
    public static boolean bigReactors = true;
    public static boolean ticon = true;
    public static boolean pe = true;
    public static boolean mfr = true;
    public static boolean twilight = true;
    public static boolean magicrops = true;
    public static boolean am2 = true;
    public static boolean te = true;
    public static boolean numanuma = true;
    public static boolean metallurgy = true;
    public static boolean enderio = true;
    public static boolean forestry = true;
    public static boolean bees = false;
    public static boolean ee3 = true;
    public static boolean extracells = true;
    public static boolean witch = true;
    public static boolean rotisserie = true;

    //public static boolean alfheim = false;
    //public static int alfheimID;

    public static boolean copper = true;
    public static boolean tin = true;
    public static boolean silver = true;
    public static boolean lead = true;
    public static boolean steel = true;
    public static boolean nickel = true;
    public static boolean ultimateBalance = true;

    public static int modifier = 0;
    public static int multiplier = 0;

    public static void configurate(File file){
        Configuration conf = new Configuration(file);

        try {
            conf.load();

            craftingOnly = conf.get("general", "Crafting Only", craftingOnly, "Enable to completely disable most of the mod except for the Dire Crafting table. For if you just want the mod for Minetweaking purposes.").getBoolean(false);
            endStone = conf.get("general", "Use End Stone", endStone, "Disable to take end stone out of recipes").getBoolean(true);
            bedrockBreaker = conf.get("general", "Break Bedrock", bedrockBreaker, "Disable if you don't want the World Breaker to break unbreakable blocks").getBoolean(true);
            boringFood = conf.get("general", "Boring Food", boringFood, "Enable to keep the Ultimate Stew and Cosmic Meatballs from grabbing more ingredients").getBoolean(false);
            fractured = conf.get("general", "Fractured Ores", fractured, "Enable if you don't have Rotarycraft installed and want some buggy fractured ores").getBoolean(false);
            fast = conf.get("general", "Gotta Go Fast", fast, "Disable if the Infinity Boots' speed boost is too ridiculous").getBoolean(true);

            conf.addCustomCategoryComment("compatibility", "Disable to stop compatibility with that particular mod. Will not use the mod in recipes or add new items for that mod.");
            thaumic = conf.get("compatibility", "Thaumcraft", true).getBoolean(true);
            sc2 = conf.get("compatibility", "Steve's Carts 2", true).getBoolean(true);
            ae2 = conf.get("compatibility", "Applied Energistics 2", true).getBoolean(true);
            exu = conf.get("compatibility", "Extra Utilities", true).getBoolean(true);
            ic2 = conf.get("compatibility", "Industrialcraft 2 Experimental", true).getBoolean(true);
            gt = conf.get("compatibility", "Gregtech 5", true).getBoolean(true);
            botan = conf.get("compatibility", "Botania", true).getBoolean(true);
            blood = conf.get("compatibility", "Blood Magic", true).getBoolean(true);
            lolDargon = conf.get("compatibility", "Draconic Evolution", true).getBoolean(true);
            bigReactors = conf.get("compatibility", "Big Reactors", true).getBoolean(true);
            ticon = conf.get("compatibility", "Tinkers Construct", true).getBoolean(true);
            pe = conf.get("compatibility", "Project E", true).getBoolean(true);
            mfr = conf.get("compatibility", "MineFactory Reloaded", true).getBoolean(true);
            twilight = conf.get("compatibility", "Twilight Forest", true).getBoolean(true);
            magicrops = conf.get("compatibility", "Magical Crops", true).getBoolean(true);
            am2 = conf.get("compatibility", "Ars Magica 2", true).getBoolean(true);
            te = conf.get("compatibility", "Thermal Expansion", true).getBoolean(true);
            numanuma = conf.get("compatibility", "Pneumaticraft", true).getBoolean(true);
            metallurgy = conf.get("compatibility", "Metallurgy", true).getBoolean(true);
            enderio = conf.get("compatibility", "EnderIO", true).getBoolean(true);
            forestry = conf.get("compatibility", "Forestry", true).getBoolean(true);
            bees = conf.get("compatibility", "Forestry Bees", false).getBoolean(false);
            ee3 = conf.get("compatibility", "Equivalent Exchange 3", true).getBoolean(true);
            extracells = conf.get("compatibility", "Extra Cells", true).getBoolean(true);
            witch = conf.get("compatibility", "Witchery", true).getBoolean(true);
            rotisserie = conf.get("compatibility", "Rotarycraft", true).getBoolean(true);

            //alfheim = conf.get("compatibility", "Alfheim", false).getBoolean(false);

            conf.addCustomCategoryComment("materials", "Disable to stop using that material in recipes. Useful if a mod adds unobtainable placeholder ores.");
            copper = conf.get("materials", "Copper", true).getBoolean(true);
            tin = conf.get("materials", "Tin", true).getBoolean(true);
            silver = conf.get("materials", "Silver", true).getBoolean(true);
            lead = conf.get("materials", "Lead", true).getBoolean(true);
            nickel = conf.get("materials", "Nickel/Ferrous", true).getBoolean(true);
            steel = conf.get("materials", "Steel", true).getBoolean(true);
            ultimateBalance = conf.get("materials", "Clay", true).getBoolean(true);

            modifier = conf.get("balance!", "Cost Modifier", 0, "Added to the existing modifier to make prices more expensive or cheaper. Can be negative.").getInt(0);
            multiplier = conf.get("balance!", "Cost Multiplier", 0, "Added to the existing multiplier to make prices more expensive or cheaper. Can be negative.").getInt(0);
        }
        catch(Exception e){
            Lumberjack.log(Level.ERROR, e, "Avaritia couldn't find its config!");
        }
        finally {
            conf.save();
        }
    }

}
