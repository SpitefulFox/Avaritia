package fox.spiteful.avaritia.compat.ticon;

import fox.spiteful.avaritia.crafting.ExtremeCraftingManager;
import fox.spiteful.avaritia.items.LudicrousItems;
import morph.avaritia.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.tools.TinkerTools;

public class Tonkers {

	public static Material neutronium;
	public static Material infinityMetal;
	public static final int neutroniumId = 500;
	public static final int infinityMetalId = 501;
	public static final String neutroniumName = "avaritia_neutronium";
	public static final String infinityMetalName = "avaritia_infinitymetal";

	public static void buildstruct() {

		neutronium = new Material(neutroniumName, "material." + neutroniumName, 5, 2400, 900, 6, 2.5f, 0, 0.0f, TextFormatting.DARK_GRAY.toString(), 0x303030);
		infinityMetal = new Material(infinityMetalName, "material." + infinityMetalName, 5, 10000, 6000, 50, 10.0f, 10, 0.0f, ModItems.COSMIC_RARITY.rarityColor.toString(), 0xFFFFFF);

		TinkerRegistry.addMaterial(neutroniumId, neutronium);
		TinkerRegistry.addtoolMaterial(infinityMetalId, infinityMetal);

		TinkerRegistry.addDefaultToolPartMaterial(neutroniumId);
		TinkerRegistry.addDefaultToolPartMaterial(infinityMetalId);

		TinkerRegistry.addBowMaterial(neutroniumId, 50, 5.0f);
		TinkerRegistry.addBowMaterial(infinityMetalId, 30, 10.0f);

		TinkerRegistry.addArrowMaterial(neutroniumId, 5.0f, 0.0f);
		TinkerRegistry.addArrowMaterial(infinityMetalId, 2.0f, 0.0f);

		TinkerRegistry.addDefaultShardMaterial(neutroniumId);

		// register neutronium ingot
		PatternBuilder pb = PatternBuilder.instance;
		pb.registerFullMaterial(new ItemStack(LudicrousItems.resource, 1, 4), 2, neutroniumName, new ItemStack(TinkerTools.shard, 1, neutroniumId), new ItemStack(TinkerTools.toolRod, 1, neutroniumId), neutroniumId);

		// register patterns
		for (int m = 0; m < TinkerTools.patternOutputs.length; m++) {
			if (TinkerTools.patternOutputs[m] != null) {
				TConstructRegistry.addPartMapping(TinkerTools.pattern, m + 1, neutroniumId, new ItemStack(TinkerTools.patternOutputs[m], 1, neutroniumId));
			}
		}
		for (int m = 0; m < TinkerWeaponry.patternOutputs.length; m++) {
			TConstructRegistry.addPartMapping(TinkerTools.woodPattern, m, neutroniumId, new ItemStack(TinkerWeaponry.patternOutputs[m], 1, neutroniumId));
		}
		TConstructRegistry.addPartMapping(TinkerTools.pattern, 25, neutroniumId, new ItemStack(TinkerWeaponry.arrowhead, 1, neutroniumId));

		TonkersEvents events = new TonkersEvents();
		MinecraftForge.EVENT_BUS.register(events);
		FMLCommonHandler.instance().bus().register(events);

		// recipes
		ItemStack ingot = new ItemStack(LudicrousItems.resource, 1, 6);

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.toolRod, 1, infinityMetalId), "    X", "   X ", "  X  ", " X   ", "X    ", 'X', ingot);

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.pickHead, 1, infinityMetalId), "XXX  ", "  XX ", "   XX", "    X", "    X", 'X', ingot);

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.shovelHead, 1, infinityMetalId), " XXX", "XXXX", "  XX", "  X ", 'X', ingot);

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.axeHead, 1, infinityMetalId), "  X  ", " XXX ", "XXXXX", "   X ", 'X', ingot);

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.binding, 1, infinityMetalId), "X X", " X ", "X X", 'X', ingot);

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.toughBinding, 1, infinityMetalId), "X   X", " X X ", "  X  ", " X X ", "X   X", 'X', ingot);

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.toughToolRod, 1, infinityMetalId), "        X", "       X ", "      X  ", "     X   ", "    X    ", "   X     ", "  X      ", " X       ", "X        ", 'X', ingot);

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.largePlate, 1, infinityMetalId), "XXXXX", "X X X", "XXXXX", "X   X", "XXXXX", 'X', ingot);

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.swordBlade, 1, infinityMetalId), "   XX", "  XXX", " XXX ", "XXX  ", " X   ", 'X', ingot);

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.wideGuard, 1, infinityMetalId), "X   ", " X  ", " XX ", "   X", 'X', ingot);

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.handGuard, 1, infinityMetalId), "XXX", "  X", "  X", 'X', ingot);

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.crossGuard, 1, infinityMetalId), "X  ", "XX ", " XX", 'X', ingot);

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.knifeBlade, 1, infinityMetalId), "  XX", " XX ", "XX  ", 'X', ingot);

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.panHead, 1, infinityMetalId), " XX ", "XXXX", "XXXX", " XX ", 'X', ingot);

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.signHead, 1, infinityMetalId), "XXXXX", "XXXXX", "XXXXX", 'X', ingot);

		//ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.chiselHead, 1, infinityMetalId), "    X ", "   XXX", "   XX ", "  X   ", " X    ", "X     ", 'X', ingot);

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.scytheHead, 1, infinityMetalId), "XXXX    ", "  XXXX  ", "    XXX ", "     XXX", "      X ", 'X', ingot);

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.broadAxeHead, 1, infinityMetalId), "  XX", " XXX", "XXXX", "XXXX", 'X', ingot);

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.excavatorHead, 1, infinityMetalId), "  X  ", " XXX ", "XXXXX", "XXXX ", " XX  ", 'X', ingot);

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.largeSwordBlade, 1, infinityMetalId), "    XXXX", "   XXXX ", "  XXXX  ", " XXXX   ", "XXXX    ", " XX     ", 'X', ingot);

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.hammerHead, 1, infinityMetalId), "  X    ", " XXX   ", "XXXXX  ", " XXXXX ", "  XXXXX", "   XXX ", "    X  ", 'X', ingot);

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.arrowHead, 1, infinityMetalId), "XX  ", "XXXX", " XX ", " X  ", 'X', ingot);

		//ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.partShuriken, 1, infinityMetalId), "  XX", "XXXX", " XX ", "X X ", 'X', ingot);

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.bowLimb, 1, infinityMetalId), "XXXXXXX", 'X', ingot);

		//ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.partCrossbowLimb, 1, infinityMetalId), "   XXXX", "  XX   ", " XX    ", "XX     ", "X      ", "X      ", 'X', ingot);

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.crossGuard, 1, infinityMetalId), "X      ", " XX    ", " XXX   ", "  XXX  ", "   XX  ", "     X ", "      X", 'X', ingot);

		/*ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerWeaponry.partBolt, 1, infinityMetalId),
		    	"   X",
		    	"  X ",
		    	" X  ",
		    	"X   ",
		    	'X', ingot);*/

		//ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.fullGuard, 1, infinityMetalId), " XXX", "XXX ", "XXX ", " XXX", 'X', ingot);

		// extra modifiers from catalyst
		ModifyBuilder.registerModifier(new ModExtraModifier(new ItemStack[] {
				new ItemStack(LudicrousItems.resource, 1, 5)
		}, "AvaritiaFree") {
			@Override
			public void modify(ItemStack[] recipe, ItemStack input) {
				NBTTagCompound tags = this.getModifierTag(input);
				tags.setBoolean(key, true);
				int modifiers = tags.getInteger("Modifiers");
				modifiers += 5;
				tags.setInteger("Modifiers", modifiers);
			}
		});

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(getBolt(neutroniumId, neutroniumId), "XX", "RX", 'X', new ItemStack(LudicrousItems.resource, 1, 4), 'R', new ItemStack(TinkerTools.toolRod, 1, neutroniumId));

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(getBolt(neutroniumId, infinityMetalId), "XX", "RX", 'X', new ItemStack(LudicrousItems.resource, 1, 6), 'R', new ItemStack(TinkerTools.toolRod, 1, neutroniumId));

		ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(getBolt(infinityMetalId, infinityMetalId), "XX", "RX", 'X', new ItemStack(LudicrousItems.resource, 1, 6), 'R', new ItemStack(TinkerTools.toolRod, 1, infinityMetalId));

	}

	private static ItemStack getBolt(int main, int tip) {
		ItemStack bolt = new ItemStack(TinkerTools.boltCore, 1, main);
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagCompound mat = new NBTTagCompound();
		mat.setInteger("Material2", tip);
		tag.setTag("DualMat", mat);
		bolt.setTagCompound(tag);
		return bolt;
	}
}
