package fox.spiteful.avaritia.crafting;

import java.util.*;

import cpw.mods.fml.common.Loader;
import fox.spiteful.avaritia.Config;
import fox.spiteful.avaritia.Lumberjack;
import fox.spiteful.avaritia.compat.Compat;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.apache.logging.log4j.Level;

public class Mincer {
    public static ShapelessOreRecipe stewRecipe;
    public static ShapelessOreRecipe meatballRecipe;

	// here's where all the food magic goes on
	private static final String[] sacredCropNames = new String[]{"cropWheat", "cropCarrot", "cropPotato", "cropApple", "cropMelon", "cropPumpkin", "cropCactus", "cropMushroomRed", "cropMushroomBrown", "cropCherry"};
    private static final String[] forbiddenCropNames = new String[] {"cropEdibleroot", "cropWhitemushroom", "cropBeet", "cropCotton", "cropPoppy", "cropTulipRed", "cropTulipWhite", "cropDaisy",
        "cropTulipPink", "cropAllium", "cropOrchid", "cropTulipOrange", "cropDandelion", "cropShroomRed", "cropShroomBrown",
        "cropFerranium", "cropAurigold", "cropDiamahlia", "cropLapender", "cropEmeryllis", "cropRedstodendron", "cropCuprosia",
        "cropPetinia", "cropPlombean", "cropSilverweed", "cropJaslumine", "cropNiccissus", "cropPlatiolus", "cropOsmonium",
        "cropSandPear", "cropCitron"};
	private static final String[] knownMeatEntries = new String[]{"ingotMeatRaw", "dustMeat", "rawMutton"};
	private static List<ItemStack> knownMeats = new ArrayList<ItemStack>();

    static {
		knownMeats.add(new ItemStack(Items.beef));
		knownMeats.add(new ItemStack(Items.chicken));
		knownMeats.add(new ItemStack(Items.porkchop));
		
		/*for (int i=0; i<ItemFishFood.FishType.values().length; i++) {
			knownMeats.add(new ItemStack(Items.fish, 1, i));
		}*/
		knownMeats.add(new ItemStack(Items.fish));
        OreDictionary.registerOre("cropCactus", new ItemStack(Blocks.cactus));
        OreDictionary.registerOre("cropMushroomRed", new ItemStack(Blocks.red_mushroom));
        OreDictionary.registerOre("cropMushroomBrown", new ItemStack(Blocks.brown_mushroom));
	}
	private static Random randy;
	
	public static void countThoseCalories() {
		
        if(Config.boringFood){
            stewRecipe = ExtremeCraftingManager.getInstance().addShapelessOreRecipe(new ItemStack(LudicrousItems.ultimate_stew, 1), new ItemStack(Items.wheat, 1), new ItemStack(Items.carrot), new ItemStack(Items.potato), new ItemStack(Items.apple), new ItemStack(Items.melon), new ItemStack(Blocks.pumpkin), new ItemStack(Blocks.cactus), new ItemStack(Blocks.red_mushroom), new ItemStack(Blocks.brown_mushroom));
            meatballRecipe = ExtremeCraftingManager.getInstance().addShapelessOreRecipe(new ItemStack(LudicrousItems.cosmic_meatballs, 1), new ItemStack(Items.beef), new ItemStack(Items.beef), new ItemStack(Items.chicken), new ItemStack(Items.chicken), new ItemStack(Items.porkchop), new ItemStack(Items.porkchop), new ItemStack(Items.fish), new ItemStack(Items.fish));
            return;
        }

        String[] orenames = OreDictionary.getOreNames();
		
		List<String> rawCrops = new ArrayList<String>();
		List<String> crops = new ArrayList<String>();
		List<String> meatNames = new ArrayList<String>();
		List<String> rawMeats = new ArrayList<String>();
		List<String> meats = new ArrayList<String>();
		
		String orename;
		for (int i=0; i<orenames.length; i++) {
			orename = orenames[i];
			if (orename.startsWith("crop") && !bannedCrop(orename) && !orename.startsWith("cropBotania")) {
				rawCrops.add(orename);
			}
		}

		//Lumberjack.info("End of ore crop names: "+rawCrops.size()+" names found.");
		
		// Ultimate Stew recipe
		
		// move the sacred crops if they exist... THEY CANNOT BE DEFILED!
		for (int i=0; i<sacredCropNames.length; i++) {
			String crop = sacredCropNames[i];
			if (rawCrops.contains(crop)) {
				rawCrops.remove(crop);
				crops.add(crop);
			}
		}
		
		// prepare for culling
		List<FoodInfo> cropSortingList = new ArrayList<FoodInfo>();
		randy = new Random(12345);
		
		for (int i=0; i<rawCrops.size(); i++) {
			List<ItemStack> ores = OreDictionary.getOres(rawCrops.get(i));
			
			if (ores.size() > 0) {
				cropSortingList.add(new FoodInfo(rawCrops.get(i), ores.size()));
			}
		}
		
		//Lumberjack.info("pre-sort: "+cropSortingList);
		
		// sort into size/alphabetic order first to standardise them
		Collections.sort(cropSortingList, new Comparator<FoodInfo>(){
			@Override
			public int compare(FoodInfo a, FoodInfo b) {
				if (a.count != b.count) {
					return b.count > a.count ? 1 : -1;
				}
				
				return a.orename.compareTo(b.orename);
			}
		});
		
		//Lumberjack.info("first sort: "+cropSortingList);
		
		// sort into size/random order, should be deterministic because previous sort
		Collections.shuffle(cropSortingList, randy);
		
		Collections.sort(cropSortingList, new Comparator<FoodInfo>(){
			@Override
			public int compare(FoodInfo a, FoodInfo b) {
				if (a.count != b.count) {
					return b.count > a.count ? 1 : -1;
				}

				return 0;
			}
		});
		
		//Lumberjack.info("second sort: "+cropSortingList);
		
		// CULL!
		
		if (cropSortingList.size() > 80 - crops.size()) {
			int shouldHave = 80 - crops.size();
			cropSortingList = cropSortingList.subList(0, shouldHave);
		}
		for (int i=0; i<cropSortingList.size(); i++) {
			crops.add(cropSortingList.get(i).orename);
		}
		
		// calculate how much stew the recipe makes!
		//int types = Math.min(80,crops.size());
		int croptypes = crops.size();
		int cropmultiplier = 1;
		
		while (croptypes * cropmultiplier < 8) {
			cropmultiplier++;
		}
		
		int makesstew = (int) Math.round(croptypes*cropmultiplier/9.0);
		
		//Lumberjack.info(types+" types x"+multiplier+" = "+(multiplier*types)+" items, makes "+makes+" pots of stew");
		
		// time to actually MAKE the damn thing...
		
		stewRecipe = ExtremeCraftingManager.getInstance().addShapelessOreRecipe(new ItemStack(LudicrousItems.ultimate_stew, makesstew), new ItemStack(LudicrousItems.resource, 1, 2));
		
		List<Object> stewInputs = stewRecipe.getInput();
		for (int i=0; i<crops.size(); i++) {
			for (int j=0; j<cropmultiplier; j++) {
				stewInputs.add(OreDictionary.getOres(crops.get(i)));
			}
		}
		
		// ok, now on to the meatballs!
		
		//#####################################################################
		
		// Cosmic Meatball recipe
		List<FoodInfo> meatSortingList = new ArrayList<FoodInfo>();
		randy = new Random(54321);

		for (int i=0; i<knownMeatEntries.length; i++) {
			if(OreDictionary.doesOreNameExist(knownMeatEntries[i])) {
				List<ItemStack> meatstacks = OreDictionary.getOres(knownMeatEntries[i]);
				if (!meatstacks.isEmpty()) {
					rawMeats.add(knownMeatEntries[i]);
					meatSortingList.add(new FoodInfo(knownMeatEntries[i], meatstacks.size()));
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

        if(Loader.isModLoaded("TwilightForest") && Config.twilight){
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
		
		Lumberjack.info("rawMeats: "+rawMeats);
		Lumberjack.info("knownMeats: "+knownMeats);
		
		//Lumberjack.info("pre-sort: "+meatSortingList);
		
		// sort into size/alphabetic order first to standardise them
		Collections.sort(meatSortingList, new Comparator<FoodInfo>(){
			@Override
			public int compare(FoodInfo a, FoodInfo b) {
				if (a.count != b.count) {
					return b.count > a.count ? 1 : -1;
				}
				
				return a.orename.compareTo(b.orename);
			}
		});
		
		//Lumberjack.info("first sort: "+meatSortingList);
		
		// sort into size/random order, should be deterministic because previous sort
		Collections.sort(meatSortingList, new Comparator<FoodInfo>(){
			@Override
			public int compare(FoodInfo a, FoodInfo b) {
				if (a.count != b.count) {
					return b.count > a.count ? 1 : -1;
				}
				
				return randy.nextBoolean() ? 1 : -1;
			}
		});
		
		//Lumberjack.info("second sort: "+meatSortingList);
		
		// CULL!
		
		if (meatSortingList.size() > 80 - meats.size() - knownMeats.size()) {
			int shouldHave = 80 - crops.size() - knownMeats.size();
			meatSortingList = meatSortingList.subList(0, shouldHave);
		}
		for (int i=0; i<meatSortingList.size(); i++) {
			meats.add(meatSortingList.get(i).orename);
		}
		
		// calculate how many meatballs the recipe makes!
		int meattypes = meats.size() + knownMeats.size();
		int meatmultiplier = 1;
		
		while (meattypes * meatmultiplier < 8) {
			meatmultiplier++;
		}
		
		int makesmeatballs = (int) Math.round(meattypes*meatmultiplier/9.0);
		
		//Lumberjack.info(types+" types x"+multiplier+" = "+(multiplier*types)+" items, makes "+makes+" pots of stew");
		
		// time to actually MAKE the damn thing...
		
		meatballRecipe = ExtremeCraftingManager.getInstance().addShapelessOreRecipe(new ItemStack(LudicrousItems.cosmic_meatballs, makesmeatballs), new ItemStack(LudicrousItems.resource, 1, 2));
		
		List<Object> meatballInputs = meatballRecipe.getInput();
		
		for (int i=0; i<knownMeats.size(); i++) {
			for (int j=0; j<meatmultiplier; j++) {
				meatballInputs.add(knownMeats.get(i));
			}
		}
		
		for (int i=0; i<meats.size(); i++) {
			for (int j=0; j<meatmultiplier; j++) {
				meatballInputs.add(OreDictionary.getOres(meats.get(i)));
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
			return this.orename+": "+this.count;
		}
	}

    private static boolean bannedCrop(String crop){
        for(String ban : forbiddenCropNames){
            if(ban.equals(crop))
                return true;
        }
        return false;
    }
	
}
