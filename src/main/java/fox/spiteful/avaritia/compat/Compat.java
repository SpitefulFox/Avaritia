package fox.spiteful.avaritia.compat;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import fox.spiteful.avaritia.Config;
import fox.spiteful.avaritia.Lumberjack;
import fox.spiteful.avaritia.blocks.LudicrousBlocks;
import fox.spiteful.avaritia.compat.bloodmagic.Bloody;
import fox.spiteful.avaritia.compat.botania.Tsundere;
import fox.spiteful.avaritia.compat.forestry.Ranger;
import fox.spiteful.avaritia.compat.minetweaker.Tweak;
import fox.spiteful.avaritia.compat.nei.NotEnough;
import fox.spiteful.avaritia.compat.tails.InfiniteFoxes;
import fox.spiteful.avaritia.compat.thaumcraft.Lucrum;
import fox.spiteful.avaritia.compat.ticon.Tonkers;
import fox.spiteful.avaritia.crafting.ExtremeCraftingManager;
import fox.spiteful.avaritia.crafting.Grinder;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.logging.log4j.Level;

public class Compat {

    public static boolean nei = false;
    public static boolean thaumic = false;
    public static boolean ae2 = false;
    public static boolean exu = false;
    public static boolean ic2 = false;
    public static boolean gt = false;
    public static boolean botan = false;
    public static boolean blood = false;
    public static boolean bigReactors = false;
    public static boolean ticon = false;
    public static boolean pe = false;
    public static boolean tweak = false;
    public static boolean mfr = false;
    public static boolean am2 = false;
    public static boolean forestry = false;
    public static boolean te = false;

    public static void census(){
        nei = Loader.isModLoaded("NotEnoughItems");
        tweak = Loader.isModLoaded("MineTweaker3");
        thaumic = Loader.isModLoaded("Thaumcraft") && Config.thaumic;
        ae2 = Loader.isModLoaded("appliedenergistics2") && Config.ae2;
        exu = Loader.isModLoaded("ExtraUtilities") && Config.exu;
        ic2 = Loader.isModLoaded("IC2") && Config.ic2;
        gt = Loader.isModLoaded("gregtech") && Config.gt;
        botan = Loader.isModLoaded("Botania") && Config.botan;
        blood = Loader.isModLoaded("AWWayofTime") && Config.blood;
        bigReactors = Loader.isModLoaded("BigReactors") && Config.bigReactors;
        ticon = Loader.isModLoaded("TConstruct") && Config.ticon;
        pe = Loader.isModLoaded("ProjectE") && Config.pe;
        mfr = Loader.isModLoaded("MineFactoryReloaded") && Config.mfr;
        am2 = Loader.isModLoaded("arsmagica2") && Config.am2;
        forestry = Loader.isModLoaded("Forestry") && Config.forestry;
        te = Loader.isModLoaded("ThermalExpansion") && Config.te;
    }

    public static void compatify(){
        if(nei){
            try
            {
                NotEnough.items();
            }
            catch(Throwable e)
            {
                Lumberjack.log(Level.INFO, e, "Avaritia had Too Many Items.");
            }
        }

        if(tweak){
            try
            {
                Tweak.registrate();
            }
            catch(Throwable e){
                Lumberjack.log(Level.INFO, e, "Avaritia is too good for tweaking, apparently.");
            }
        }

        if(Config.craftingOnly)
            return;

        if(thaumic){
            try
            {
                Lucrum.abracadabra();
            }
            catch(Throwable e){
                Lumberjack.log(Level.INFO, "Avaritia accumulated too much Warp!");
                e.printStackTrace();
                thaumic = false;
            }
        }

        if(Loader.isModLoaded("StevesCarts") && Config.sc2){
            try {
                Block resource = getBlock("StevesCarts", "BlockMetalStorage");
                ItemStack galg = new ItemStack(resource, 1, 2);
                Grinder.catalyst.getInput().add(galg);
            }
            catch(Throwable e){
                Lumberjack.log(Level.INFO, "Avaritia died of old age while trying to craft the Galgadorian Drill.");
                e.printStackTrace();
            }
        }

        if(ae2){
            try {
                Item resource = getItem("appliedenergistics2", "item.ItemMultiMaterial");
                if(Loader.isModLoaded("extracells") && Config.extracells){
                    Item extracell = getItem("extracells", "storage.component");
                    //16mb cell
                    Grinder.catalyst.getInput().add(new ItemStack(extracell, 1, 3));
                }
                else {
                    //64k Cell
                    Grinder.catalyst.getInput().add(new ItemStack(resource, 1, 38));
                }
                //Singularity
                Grinder.catalyst.getInput().add(new ItemStack(resource, 1, 47));

                Block creative = getBlock("appliedenergistics2", "tile.BlockCreativeEnergyCell");
                Block dense = getBlock("appliedenergistics2", "tile.BlockDenseEnergyCell");

                ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(creative, 1, 0),
                        "IIIIDIIII",
                        "IEEEDEEEI",
                        "IEEEDEEEI",
                        "IEEEDEEEI",
                        "DDDDDDDDD",
                        "IEEEDEEEI",
                        "IEEEDEEEI",
                        "IEEEDEEEI",
                        "IIIIDIIII",
                        'D', new ItemStack(resource, 1, 24),
                        'E', new ItemStack(dense, 1, 0),
                        'I', new ItemStack(LudicrousItems.resource, 1, 6));
            }
            catch (Throwable e){
                Lumberjack.log(Level.INFO, "Avaritia couldn't figure out how channels work.");
                e.printStackTrace();
                ae2 = false;
            }
        }

        if(exu){
            try {
                Block bedrockium = getBlock("ExtraUtilities", "block_bedrockium");
                Block deco = getBlock("ExtraUtilities", "decorativeBlock1");
                ItemStack bed = new ItemStack(bedrockium, 1, 0);
                ItemStack unstable = new ItemStack(deco, 1, 5);
                Grinder.catalyst.getInput().add(bed);
                Grinder.catalyst.getInput().add(unstable);
            }
            catch (Throwable e){
                Lumberjack.log(Level.INFO, "Avaritia was unable to stop Lavos.");
                e.printStackTrace();
                exu = false;
            }
        }

        if(ic2){
            try {
                if(gt){
                    try {
                        Item resource = getItem("gregtech", "gt.metaitem.01");
                        ItemStack lolneutronium = new ItemStack(resource, 1, 9129);
                        Grinder.catalyst.getInput().add(lolneutronium);
                    }
                    catch(Throwable e){
                        Lumberjack.log(Level.INFO, "Avaritia got sick of only getting 2 planks per log.");
                        e.printStackTrace();
                        gt = false;
                    }
                }

                if(!gt){
                    Item iridium = getItem("IC2", "itemPartIridium");
                    ItemStack plate = new ItemStack(iridium, 1, 0);
                    Grinder.catalyst.getInput().add(plate);
                }
            }
            catch (Throwable e){
                Lumberjack.log(Level.INFO, "Avaritia blew up the macerator.");
                e.printStackTrace();
                ic2 = false;
            }
        }

        if(botan){
            try {
                Tsundere.baka();
            }
            catch (Throwable e){
                Lumberjack.log(Level.INFO, "Avaritia is wondering where all the dayblooms went.");
                e.printStackTrace();
                botan = false;
            }
        }

        if(blood){
            try {
                Bloody.bloodlett();
            }
            catch (Throwable e){
                Lumberjack.log(Level.INFO, "Avaritia decided to use a Fallen Kanade instead.");
                e.printStackTrace();
                blood = false;
            }
        }

        if(Loader.isModLoaded("DraconicEvolution") && Config.lolDargon){
            try {
                Block dargon = getBlock("DraconicEvolution", "draconicBlock");
                ItemStack lol = new ItemStack(dargon, 1, 0);
                Grinder.catalyst.getInput().add(lol);
            }
            catch (Throwable e){
                Lumberjack.log(Level.INFO, e, "Avaritia was distracted by a giant glowing sphere.");
            }
        }

        if(bigReactors){
            try {
                Item ingot = getItem("BigReactors", "BRIngot");
                ItemStack ludicrite = new ItemStack(ingot, 1, 8);
                Grinder.catalyst.getInput().add(ludicrite);
            }
            catch (Throwable e){
                Lumberjack.log(Level.INFO, "Avaritia decided it just didn't need all that RF.");
                e.printStackTrace();
                bigReactors = false;
            }
        }

        if(ticon){
            try {
                Block metal = getBlock("TConstruct", "MetalBlock");
                ItemStack menomena = new ItemStack(metal, 1, 2);
                Grinder.catalyst.getInput().add(menomena);
                Tonkers.buildstruct();
            }
            catch (Throwable e){
                Lumberjack.log(Level.INFO, "Avaritia fell in the smeltery.");
                e.printStackTrace();
                ticon = false;
            }
        }

        if(pe){
            try {
                Item matter = getItem("ProjectE", "item.pe_matter");
                ItemStack red = new ItemStack(matter, 1, 1);
                Grinder.catalyst.getInput().add(red);
            }
            catch (Throwable e){
                Lumberjack.log(Level.INFO, "Avaritia got tired of abusing the blaze rod exploit.");
                e.printStackTrace();
                pe = false;
            }
        }

        if(mfr){
            try {
                Item ponk = getItem("MineFactoryReloaded", "pinkslime");
                ItemStack pank = new ItemStack(ponk, 1, 1);
                Grinder.catalyst.getInput().add(pank);
            }
            catch (Throwable e){
                Lumberjack.log(Level.INFO, "Avaritia walked into the laser drill.");
                e.printStackTrace();
                mfr = false;
            }
        }

        if(Loader.isModLoaded("TwilightForest") && Config.twilight){
            try {
                Item ironwood = getItem("TwilightForest", "item.ironwoodIngot");
                Grinder.catalyst.getInput().add(new ItemStack(ironwood, 1));
            }
            catch (Throwable e){
                Lumberjack.log(Level.INFO, e, "Avaritia was killed by a hydra.");
            }
        }

        if(Loader.isModLoaded("magicalcrops") && Config.magicrops){
            try {
                Item essence = getItem("magicalcrops", "magicalcrops_MagicEssence");
                Item meat = getItem("magicalcrops", "magicalcrops_RawMeat");
                Item crop = getItem("magicalcrops", "magicalcrops_CropProduce");
                ItemStack extreme = new ItemStack(essence, 1, 4);
                Grinder.catalyst.getInput().add(extreme);
                OreDictionary.registerOre("cropBlackberry", new ItemStack(crop, 1, 0));
                OreDictionary.registerOre("cropBlueberry", new ItemStack(crop, 1, 1));
                OreDictionary.registerOre("cropChilipepper", new ItemStack(crop, 1, 2));
                OreDictionary.registerOre("cropCucumber", new ItemStack(crop, 1, 3));
                OreDictionary.registerOre("cropGrape", new ItemStack(crop, 1, 4));
                OreDictionary.registerOre("cropRaspberry", new ItemStack(crop, 1, 5));
                OreDictionary.registerOre("cropStrawberry", new ItemStack(crop, 1, 6));
                OreDictionary.registerOre("cropCorn", new ItemStack(crop, 1, 7));
                OreDictionary.registerOre("cropTomato", new ItemStack(crop, 1, 8));
                OreDictionary.registerOre("rawMutton", new ItemStack(meat, 1, 0));
                OreDictionary.registerOre("rawCalamari", new ItemStack(meat, 1, 1));
            }
            catch (Throwable e){
                Lumberjack.log(Level.INFO, e, "Avaritia got bored of waiting for magical crops to grow.");
            }
        }

        if(Loader.isModLoaded("ganyssurface")){
            try {
                Item mutton = getItem("ganyssurface", "mutton_raw");
                OreDictionary.registerOre("rawMutton", new ItemStack(mutton, 1, 0));
            }
            catch (Throwable e){
                Lumberjack.log(Level.INFO, e, "Avaritia forgot which Gany's mod it was dealing with.");
            }
        }

        if(Loader.isModLoaded("harvestcraft")){
            try {
                Item mutton = getItem("harvestcraft", "muttonrawItem");
                Item beet = getItem("harvestcraft", "beetItem");
                Item calamari = getItem("harvestcraft", "calamarirawItem");
                OreDictionary.registerOre("rawMutton", new ItemStack(mutton, 1, 0));
                OreDictionary.registerOre("rawCalamari", new ItemStack(calamari, 1, 0));
                OreDictionary.registerOre("cropBeetroot", new ItemStack(beet, 1, 0));
            }
            catch (Throwable e){
                Lumberjack.log(Level.INFO, "Avaritia got overwhelmed by all the food choices. D:");
                e.printStackTrace();
            }
        }

        if(Loader.isModLoaded("Natura")){
            try {
                Item barley = getItem("Natura", "barleyFood");
                OreDictionary.registerOre("cropBarley", new ItemStack(barley, 1, 0));
            }
            catch (Throwable e){
                Lumberjack.log(Level.INFO, e, "Avaritia got overwhelmed by all the food choices. D:");
            }
        }

        if(am2){
            try {
                Item gem = getItem("arsmagica2", "itemOre");
                Item essence = getItem("arsmagica2", "essence");

                Grinder.catalyst.getInput().add(new ItemStack(essence, 1, 10));
                Grinder.catalyst.getInput().add(new ItemStack(gem, 1, 6));
            }
            catch (Throwable e){
                Lumberjack.log(Level.INFO, e, "Avaritia got sick of the arcane guardian's healspam.");
                am2 = false;
            }
        }

        if(forestry){
            try {
                Ranger.stopForestFires();
            }
            catch (Throwable e){
                Lumberjack.log(Level.INFO, e, "Avaritia got stung by a bee.");
                forestry = false;
            }
        }

        if(Loader.isModLoaded("EE3") && Config.ee3){
            try {
                Block fuel = getBlock("EE3", "alchemicalFuelBlock");
                Grinder.catalyst.getInput().add(new ItemStack(fuel, 1, 2));
            }
            catch (Throwable e){
                Lumberjack.log(Level.INFO, e, "Avaritia tried to do human transmutation.");
            }
        }

        if(te){
            try {
                Block cell = getBlock("ThermalExpansion", "Cell");
                ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(cell, 1, 0),
                        "IIIIRIIII",
                        "IEEEREEEI",
                        "IERRRRREI",
                        "IERRRRREI",
                        "RRRRXRRRR",
                        "IERRRRREI",
                        "IERRRRREI",
                        "IEEEREEEI",
                        "IIIIRIIII",
                        'X', new ItemStack(LudicrousBlocks.resource_block, 1, 1),
                        'E', "blockEnderium",
                        'I', new ItemStack(LudicrousItems.resource, 1, 6),
                        'R', new ItemStack(LudicrousItems.singularity, 1, 3));
            }
            catch (Throwable e){
                Lumberjack.log(Level.INFO, e, "Avaritia forgot one of Thermal Expansion's 500 prerequisites.");
                te = false;
            }
        }

        if(Loader.isModLoaded("witchery") && Config.witch){
            try {
                Item ingredient = getItem("witchery", "ingredient");
                Grinder.catalyst.getInput().add(new ItemStack(ingredient, 1, 150));

                Block egg = getBlock("witchery", "infinityegg");

                ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(egg, 1, 0),
                        "   NNN   ",
                        "  NNNNN  ",
                        "  NNNNN  ",
                        " NNNINNN ",
                        "NNNIIINNN",
                        "NNNIEINNN",
                        "NNNIIINNN",
                        " NNNNNNN ",
                        "  NNNNN  ",
                        'N', new ItemStack(LudicrousItems.resource, 1, 4),
                        'E', new ItemStack(Items.egg),
                        'I', new ItemStack(LudicrousItems.resource, 1, 6));
            }
            catch (Throwable e){
                Lumberjack.log(Level.INFO, e, "Avaritia suffered from Curse of the Incompatibility.");
            }
        }

        if(Loader.isModLoaded("RotaryCraft") && Config.rotisserie){
            try {
                Item stuff = getItem("RotaryCraft", "rotarycraft_item_compacts");
                Grinder.catalyst.getInput().add(new ItemStack(stuff, 1, 3));
            }
            catch (Throwable e){
                Lumberjack.log(Level.INFO, e, "Avaritia couldn't figure out how torque works.");
            }
        }
        
        if(Loader.isModLoaded("Tails")){
            try
            {
                InfiniteFoxes.floof();
            }
            catch(Throwable e){
                Lumberjack.log(Level.INFO, "Avaritia was not fluffy enough!");
                e.printStackTrace();
            }
        }
    }

    public static Block getBlock(String mod, String block) throws ItemNotFoundException {
        Block target = GameRegistry.findBlock(mod, block);
        if(target == null)
            throw new ItemNotFoundException(mod, block);
        return target;
    }

    public static Item getItem(String mod, String item) throws ItemNotFoundException {
        Item target = GameRegistry.findItem(mod, item);
        if(target == null)
            throw new ItemNotFoundException(mod, item);
        return target;
    }

    public static class ItemNotFoundException extends Exception {
        public ItemNotFoundException(String mod, String item){
            super("Unable to find " + item + " in mod " + mod + "! Are you using the correct version of the mod?");
        }
    }
}
