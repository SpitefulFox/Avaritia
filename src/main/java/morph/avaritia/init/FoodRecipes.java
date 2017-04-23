package morph.avaritia.init;

import morph.avaritia.handler.ConfigHandler;
import morph.avaritia.recipe.extreme.ExtremeCraftingManager;
import morph.avaritia.recipe.extreme.ExtremeShapelessOreRecipe;
import morph.avaritia.util.Lumberjack;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FoodRecipes {

    public static ExtremeShapelessOreRecipe stewRecipe;
    public static ExtremeShapelessOreRecipe meatballRecipe;

    // here's where all the food magic goes on
    //@formatter:off
    private static final String[] sacredCropNames = new String[] {
            "cropWheat", "cropCarrot", "cropBeetroot", "cropPotato", "cropApple",
            "cropMelon", "cropPumpkin", "cropCactus", "cropMushroomRed",
            "cropMushroomBrown", "cropCherry"
    };
    private static final String[] forbiddenCropNames = new String[] {
            "cropEdibleroot", "cropWhitemushroom", "CropBeet", "cropCotton",
            "cropPoppy", "cropTulipRed", "cropTulipWhite", "cropDaisy",
            "cropTulipPink", "cropAllium", "cropOrchid", "cropTulipOrange",
            "cropDandelion", "cropShroomRed", "cropShroomBrown", "cropFerranium",
            "cropAurigold", "cropDiamahlia", "cropLapender", "cropEmeryllis",
            "cropRedstodendron", "cropCuprosia", "cropPetinia", "cropPlombean",
            "cropSilverweed", "cropJaslumine", "cropNiccissus", "cropPlatiolus",
            "cropOsmonium", "cropSandPear", "cropCitron"
    };
    private static final String[] knownMeatEntries = new String[] {
            "ingotMeatRaw", "dustMeat", "rawMutton"
    };
    //@formatter:on
    private static List<ItemStack> knownMeats = new ArrayList<>();

    static {
        knownMeats.add(new ItemStack(Items.BEEF));
        knownMeats.add(new ItemStack(Items.CHICKEN));
        knownMeats.add(new ItemStack(Items.PORKCHOP));
        knownMeats.add(new ItemStack(Items.RABBIT));

		/*for (int i=0; i<ItemFishFood.FishType.values().length; i++) {
            knownMeats.add(new ItemStack(Items.fish, 1, i));
		}*/
        knownMeats.add(new ItemStack(Items.FISH));
        OreDictionary.registerOre("cropCactus", new ItemStack(Blocks.CACTUS));
        OreDictionary.registerOre("cropMushroomRed", new ItemStack(Blocks.RED_MUSHROOM));
        OreDictionary.registerOre("cropMushroomBrown", new ItemStack(Blocks.BROWN_MUSHROOM));
    }

    private static Random randy;

    public static void initFoodRecipes() {

        if (ConfigHandler.boringFood) {
            //@formatter:off
            stewRecipe = ExtremeCraftingManager.getInstance().addShapelessOreRecipe(
                    new ItemStack(ModItems.ultimate_stew, 1),
                    new ItemStack(Items.WHEAT, 1),
                    new ItemStack(Items.CARROT),
                    new ItemStack(Items.POTATO),
                    new ItemStack(Items.BEETROOT),
                    new ItemStack(Items.APPLE),
                    new ItemStack(Items.MELON),
                    new ItemStack(Blocks.PUMPKIN),
                    new ItemStack(Blocks.CACTUS),
                    new ItemStack(Blocks.RED_MUSHROOM),
                    new ItemStack(Blocks.BROWN_MUSHROOM)

                     );
            meatballRecipe = ExtremeCraftingManager.getInstance().addShapelessOreRecipe(
                    new ItemStack(ModItems.cosmic_meatballs, 1),
                    new ItemStack(Items.BEEF),
                    new ItemStack(Items.BEEF),
                    new ItemStack(Items.CHICKEN),
                    new ItemStack(Items.CHICKEN),
                    new ItemStack(Items.PORKCHOP),
                    new ItemStack(Items.PORKCHOP),
                    new ItemStack(Items.RABBIT),
                    new ItemStack(Items.RABBIT),
                    new ItemStack(Items.FISH),
                    new ItemStack(Items.FISH)
            );
            return;
            //@formatter:on
        }

        String[] oreNames = OreDictionary.getOreNames();

        List<String> rawCrops = new ArrayList<>();
        List<String> crops = new ArrayList<>();
        List<String> rawMeats = new ArrayList<>();
        List<String> meats = new ArrayList<>();

        //Cull banned ore names.
        for (String oreName : oreNames) {
            if (oreName.startsWith("crop") && !isBannedCrop(oreName) && !oreName.startsWith("cropBotania")) {
                rawCrops.add(oreName);
            }
        }

        //Lumberjack.info("End of ore crop names: "+rawCrops.size()+" names found.");

        // Ultimate Stew recipe

        // move the sacred crops if they exist... THEY CANNOT BE DEFILED!
        for (String crop : sacredCropNames) {
            if (rawCrops.contains(crop)) {
                rawCrops.remove(crop);
                crops.add(crop);
            }
        }

        // prepare for culling
        List<FoodInfo> cropSortingList = new ArrayList<>();
        randy = new Random(12345);

        for (String rawCrop : rawCrops) {
            List<ItemStack> ores = OreDictionary.getOres(rawCrop);

            if (ores.size() > 0) {
                cropSortingList.add(new FoodInfo(rawCrop, ores.size()));
            }
        }

        //Lumberjack.info("pre-sort: "+cropSortingList);

        // sort into size/alphabetic order first to standardise them
        cropSortingList.sort((a, b) -> {
            if (a.count != b.count) {
                return b.count > a.count ? 1 : -1;
            }

            return a.orename.compareTo(b.orename);
        });

        //Lumberjack.info("first sort: "+cropSortingList);

        // sort into size/random order, should be deterministic because previous sort
        Collections.shuffle(cropSortingList, randy);

        cropSortingList.sort((a, b) -> {
            if (a.count != b.count) {
                return b.count > a.count ? 1 : -1;
            }

            return 0;
        });

        //Lumberjack.info("second sort: "+cropSortingList);

        // CULL!

        if (cropSortingList.size() > 80 - crops.size()) {
            int shouldHave = 80 - crops.size();
            cropSortingList = cropSortingList.subList(0, shouldHave);
        }
        for (FoodInfo aCropSortingList : cropSortingList) {
            crops.add(aCropSortingList.orename);
        }

        // calculate how much stew the recipe makes!
        //int types = Math.min(80,crops.size());
        int croptypes = crops.size();
        int cropmultiplier = 1;

        while (croptypes * cropmultiplier < 8) {
            cropmultiplier++;
        }

        int makesstew = (int) Math.round(croptypes * cropmultiplier / 9.0);

        //Lumberjack.info(types+" types x"+multiplier+" = "+(multiplier*types)+" items, makes "+makes+" pots of stew");

        // time to actually MAKE the damn thing...

        stewRecipe = ExtremeCraftingManager.getInstance().addShapelessOreRecipe(new ItemStack(ModItems.ultimate_stew, makesstew), ModItems.neutron_pile);

        List<Object> stewInputs = stewRecipe.getInput();
        for (String crop : crops) {
            for (int j = 0; j < cropmultiplier; j++) {
                stewInputs.add(OreDictionary.getOres(crop));
            }
        }

        // ok, now on to the meatballs!

        //#####################################################################

        // Cosmic Meatball recipe
        List<FoodInfo> meatSortingList = new ArrayList<>();
        randy = new Random(54321);

        for (String knownMeatEntry : knownMeatEntries) {
            if (OreDictionary.doesOreNameExist(knownMeatEntry)) {
                List<ItemStack> meatstacks = OreDictionary.getOres(knownMeatEntry);
                if (!meatstacks.isEmpty()) {
                    rawMeats.add(knownMeatEntry);
                    meatSortingList.add(new FoodInfo(knownMeatEntry, meatstacks.size()));
                }
            }
        }

		/*for (int i=0; i<meatNames.size(); i++) {
			if (!rawMeats.contains(meatNames.get(i))) {
				List<ItemStack> meatstacks = OreDictionary.getOres(meatNames.get(i));
				if (!meatstacks.isEmpty()) {
					meatSortingList.add(new FoodInfo(meatNames.get(i), meatstacks.size()));
				}
			}
		}*/

        /*if(Loader.isModLoaded("TwilightForest") && Config.twilight){
            try {
                Item venison = Compat.getItem("TwilightForest", "item.venisonRaw");
                Item meef = Compat.getItem("TwilightForest", "item.meefRaw");
                knownMeats.add(new ItemStack(venison));
                knownMeats.add(new ItemStack(meef));
            }
            catch(Exception e){}
        }

        if(Loader.isModLoaded("Natura")){
            try {
                Item imp = Compat.getItem("Natura", "impmeat");
                knownMeats.add(new ItemStack(imp));
            }
            catch(Exception e){}
        }

        if(Compat.am2){
            try {
                Item stuff = Compat.getItem("arsmagica2", "itemOre");
                knownMeats.add(new ItemStack(stuff, 1, 8));
            }
            catch(Exception e){
                Lumberjack.log(Level.INFO, e, "Avaritia got sick of the arcane guardian's healspam.");
                Compat.am2 = false;
            }
        }
		*/
        Lumberjack.info("rawMeats: " + rawMeats);
        Lumberjack.info("knownMeats: " + knownMeats);

        // sort into size/alphabetic order first to standardise them
        meatSortingList.sort((a, b) -> {
            if (a.count != b.count) {
                return b.count > a.count ? 1 : -1;
            }

            return a.orename.compareTo(b.orename);
        });

        //Lumberjack.info("first sort: "+meatSortingList);

        // sort into size/random order, should be deterministic because previous sort
        meatSortingList.sort((a, b) -> {
            if (a.count != b.count) {
                return b.count > a.count ? 1 : -1;
            }

            return randy.nextBoolean() ? 1 : -1;
        });

        //Lumberjack.info("second sort: "+meatSortingList);

        // CULL!

        if (meatSortingList.size() > 80 - meats.size() - knownMeats.size()) {
            int shouldHave = 80 - crops.size() - knownMeats.size();
            meatSortingList = meatSortingList.subList(0, shouldHave);
        }
        for (FoodInfo aMeatSortingList : meatSortingList) {
            meats.add(aMeatSortingList.orename);
        }

        // calculate how many meatballs the recipe makes!
        int meattypes = meats.size() + knownMeats.size();
        int meatmultiplier = 1;

        while (meattypes * meatmultiplier < 8) {
            meatmultiplier++;
        }

        int makesmeatballs = (int) Math.round(meattypes * meatmultiplier / 9.0);

        //Lumberjack.info(types+" types x"+multiplier+" = "+(multiplier*types)+" items, makes "+makes+" pots of stew");

        // time to actually MAKE the damn thing...

        meatballRecipe = ExtremeCraftingManager.getInstance().addShapelessOreRecipe(new ItemStack(ModItems.cosmic_meatballs, makesmeatballs), ModItems.neutron_pile);

        List<Object> meatballInputs = meatballRecipe.getInput();

        for (ItemStack knownMeat : knownMeats) {
            for (int j = 0; j < meatmultiplier; j++) {
                meatballInputs.add(knownMeat);
            }
        }

        for (String meat : meats) {
            for (int j = 0; j < meatmultiplier; j++) {
                meatballInputs.add(OreDictionary.getOres(meat));
            }
        }
    }

    private static class FoodInfo {

        public final String orename;
        public final int count;

        public FoodInfo(String orename, int count) {
            this.orename = orename;
            this.count = count;
        }

        public String toString() {
            return this.orename + ": " + this.count;
        }
    }

    private static boolean isBannedCrop(String crop) {
        for (String ban : forbiddenCropNames) {
            if (ban.equals(crop)) {
                return true;
            }
        }
        return false;
    }

}
