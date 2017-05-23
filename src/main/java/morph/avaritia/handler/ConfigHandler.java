package morph.avaritia.handler;

import com.google.common.collect.Sets;
import morph.avaritia.util.Lumberjack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

public class ConfigHandler {

    public static Configuration config;

    //@formatter:off
    public static Set<String> SUPPORTED_MODS = Sets.newHashSet(//TODO These are ModID's, They NEED to be updated to the current 1.10 + id's.
            "stevescarts2",
            "ae2",
            "extrautilities2",
            "ic2",
            "botania",
            "bloodmagic",
            "draconicevolution",
            "tconstruct",
            "projecte",
            "minefactoryreloaded",
            "arsmagica2",
            "thermalexpansion",
            "metallurgy",
            "enderio",
            "forestry",
            "forestrybees",
            "ee3",
            "extracells2"

    );
    //@formatter:on
    private static HashMap<String, Boolean> MOD_INTEGRATIONS = new HashMap<>();

    public static boolean craftingOnly = false;
    public static boolean endStone = true;
    public static boolean bedrockBreaker = true;
    public static boolean boringFood = false;
    public static boolean fractured = false;

    public static boolean copper = true;
    public static boolean tin = true;
    public static boolean silver = true;
    public static boolean lead = true;
    public static boolean steel = true;
    public static boolean nickel = true;

    public static int modifier = 0;
    public static int multiplier = 0;

    public static void init(File file) {
        if (config == null) {
            config = new Configuration(file);
        }
        loadConfig();
    }

    public static void loadConfig() {

        String category;
        String comment;
        String langKey;
        Property prop;


        /* GENERAL */

        category = "general";
        config.addCustomCategoryComment(category, "General configuration of Avaritia components.");

        comment = "Enable to completely disable most of the mod except for the Dire Crafting table. For if you just want the mod for MineTweaking purposes.";
        langKey = "avaritia:config.general.crafting_only";

        prop = config.get(category, "Crafting Only", false);
        prop.setComment(comment);
        prop.setLanguageKey(langKey);
        prop.setRequiresMcRestart(true);
        craftingOnly = prop.getBoolean();

        comment = "Disable to take end stone out of recipes some of Avaritia's recipes.";
        langKey = "avaritia.config.general.end_stone";

        prop = config.get(category, "Use End Stone", true);
        prop.setComment(comment);
        prop.setLanguageKey(langKey);
        prop.setRequiresMcRestart(true);
        endStone = prop.getBoolean();

        comment = "Disable if you don't want the World Breaker to break unbreakable blocks.";
        langKey = "avaritia.config.general.break_bedrock";

        prop = config.get(category, "Break Bedrock", true);
        prop.setComment(comment);
        prop.setLanguageKey(langKey);
        prop.setRequiresMcRestart(true);
        bedrockBreaker = prop.getBoolean();

        comment = "Enable to keep the Ultimate Stew and Cosmic Meatballs from grabbing more ingredients.";
        langKey = "avaritia.config.general.boring_food";

        prop = config.get(category, "Boring Food", false);
        prop.setComment(comment);
        prop.setLanguageKey(langKey);
        prop.setRequiresMcRestart(true);
        boringFood = prop.getBoolean();

        comment = "Enable if you don't have RotaryCraft installed and want some buggy fractured ores.";
        langKey = "avaritia.config.general.fractured_ores";

        prop = config.get(category, "Fractured Ores", false);
        prop.setComment(comment);
        prop.setLanguageKey(langKey);
        prop.setRequiresMcRestart(true);
        fractured = prop.getBoolean();

        /* MATERIALS */
        category = "materials";
        config.setCategoryComment(category, "Disable to stop using that material in recipes. Useful if a mod adds unobtainable placeholder ores.");
        comment = "";

        langKey = "avaritia:config.materials.copper";
        prop = config.get(category, "Copper", true);
        prop.setComment(comment);
        prop.setLanguageKey(langKey);
        prop.setRequiresMcRestart(true);
        copper = prop.getBoolean();

        langKey = "avaritia:config.materials.tin";
        prop = config.get(category, "Tin", true);
        prop.setComment(comment);
        prop.setLanguageKey(langKey);
        prop.setRequiresMcRestart(true);
        tin = prop.getBoolean();

        langKey = "avaritia:config.materials.silver";
        prop = config.get(category, "Silver", true);
        prop.setComment(comment);
        prop.setLanguageKey(langKey);
        prop.setRequiresMcRestart(true);
        silver = prop.getBoolean();

        langKey = "avaritia:config.materials.lead";
        prop = config.get(category, "Lead", true);
        prop.setComment(comment);
        prop.setLanguageKey(langKey);
        prop.setRequiresMcRestart(true);
        lead = prop.getBoolean();

        langKey = "avaritia:config.materials.nickel";
        prop = config.get(category, "Nickel", true);
        prop.setComment(comment);
        prop.setLanguageKey(langKey);
        prop.setRequiresMcRestart(true);
        nickel = prop.getBoolean();

        langKey = "avaritia:config.materials.steel";
        prop = config.get(category, "Steel", true);
        prop.setComment(comment);
        prop.setLanguageKey(langKey);
        prop.setRequiresMcRestart(true);
        steel = prop.getBoolean();

        category = "balance";
        config.setCategoryComment(category, "Balance modifications for the Compressor.");

        comment = "Added to the existing modifier to make prices more expensive or cheaper. Can be negative.";
        langKey = "avaritia:config.balance.modifier";

        prop = config.get(category, "Cost Modifier", 0);
        prop.setComment(comment);
        prop.setLanguageKey(langKey);
        prop.setRequiresMcRestart(true);
        modifier = prop.getInt();

        comment = "Added to the existing multiplier to make prices more expensive or cheaper. Can be negative.";
        langKey = "avaritia:config.balance.multiplier";

        prop.setComment(comment);
        prop.setLanguageKey(langKey);
        prop.setRequiresMcRestart(true);
        multiplier = prop.getInt();

        loadCompatibilityConfigs();
        loadTrashConfig();

        config.save();
    }

    private static void loadCompatibilityConfigs() {
        String category = "compatibility";
        config.setCategoryComment(category, "Disable to stop compatibility with that particular mod. Will not use the mod in recipes or add new items for that mod.");
        String langKey = "avaritia:config.compatibility.";
        Property prop;
        for (String mod : SUPPORTED_MODS) {
            prop = config.get(category, mod, true);
            prop.setLanguageKey(langKey + mod);
            prop.setRequiresMcRestart(true);
            MOD_INTEGRATIONS.put(mod, prop.getBoolean());
        }
    }

    public static boolean shouldIntegrate(String modid) {
        if (!SUPPORTED_MODS.contains(modid)) {
            Lumberjack.bigError("Unable to integrate with mod as mod is not supported!, someone is doing something wrong somewhere..");
            return false;
        }
        return Loader.isModLoaded(modid) && MOD_INTEGRATIONS.get(modid);
    }

    private static void loadTrashConfig() {
        String[] defaults = { "dirt", "sand", "gravel", "cobblestone", "netherrack", "stoneGranite", "stoneDiorite", "stoneAndesite" };
        String category;
        String comment;
        String langKey;
        Property prop;

        category = "general";
        comment = "These are the OreDictionary ID's for default trashed items. These are synced from the server to the client. And will appear as defaults on the client also. Clients can override these, They are defaults not Musts.";
        langKey = "avaritia:config.general.trash_defaults";

        prop = config.get(category, "AOE Trash Defaults", defaults);
        prop.setComment(comment);
        prop.setRequiresMcRestart(false);
        AvaritiaEventHandler.defaultTrashOres.clear();
        AvaritiaEventHandler.defaultTrashOres.addAll(Sets.newHashSet(prop.getStringList()));
    }
}
