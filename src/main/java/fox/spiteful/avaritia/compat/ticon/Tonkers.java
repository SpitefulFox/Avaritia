package fox.spiteful.avaritia.compat.ticon;

import cpw.mods.fml.common.FMLCommonHandler;
import fox.spiteful.avaritia.compat.Compat;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import fox.spiteful.avaritia.crafting.ExtremeCraftingManager;
import fox.spiteful.avaritia.items.LudicrousItems;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.ModifyBuilder;
import tconstruct.library.crafting.PatternBuilder;
import tconstruct.library.tools.ToolMaterial;
import tconstruct.modifiers.tools.ModExtraModifier;
import tconstruct.tools.TinkerTools;
import tconstruct.weaponry.TinkerWeaponry;

public class Tonkers {

	public static ToolMaterial neutronium;
	public static ToolMaterial infinityMetal;
	public static final int neutroniumId = 500;
	public static final int infinityMetalId = 501;
	public static final String neutroniumName = "avaritia_neutronium";
	public static final String infinityMetalName = "avaritia_infinitymetal";

	public static void buildstruct() {
		
		neutronium = new ToolMaterial(neutroniumName, "material."+neutroniumName, 5, 4800, 900, 8, 2.5f, 0, 0.0f, EnumChatFormatting.DARK_GRAY.toString(), 0x303030);
		infinityMetal = new ToolMaterial(infinityMetalName, "material."+infinityMetalName, 5, 10000, 6000, 50, 10.0f, 10, 0.0f, LudicrousItems.cosmic.rarityColor.toString(), 0xFFFFFF);

		TConstructRegistry.addtoolMaterial(neutroniumId, neutronium);
		TConstructRegistry.addtoolMaterial(infinityMetalId, infinityMetal);
		
		TConstructRegistry.addDefaultToolPartMaterial(neutroniumId);
		TConstructRegistry.addDefaultToolPartMaterial(infinityMetalId);
		
		TConstructRegistry.addBowMaterial(neutroniumId, 109, 10.0f);
		TConstructRegistry.addBowMaterial(infinityMetalId, 10, 60.0f);
		
		TConstructRegistry.addArrowMaterial(neutroniumId, 5.0f, 0.1f);
		TConstructRegistry.addArrowMaterial(infinityMetalId, 4.0f, 0.0f);
		
		TConstructRegistry.addDefaultShardMaterial(neutroniumId);
		
		// register neutronium ingot
		PatternBuilder pb = PatternBuilder.instance;
		pb.registerFullMaterial(new ItemStack(LudicrousItems.resource, 1, 4), 2, neutroniumName, new ItemStack(TinkerTools.toolShard, 1, neutroniumId), new ItemStack(TinkerTools.toolRod, 1, neutroniumId), neutroniumId);
		
		// register patterns
		for (int m = 0; m < TinkerTools.patternOutputs.length; m++)
        {
            if (TinkerTools.patternOutputs[m] != null)
                TConstructRegistry.addPartMapping(TinkerTools.woodPattern, m + 1, neutroniumId, new ItemStack(TinkerTools.patternOutputs[m], 1, neutroniumId));
        }
		for (int m = 0; m < TinkerWeaponry.patternOutputs.length; m++) {
            TConstructRegistry.addPartMapping(TinkerWeaponry.woodPattern, m, neutroniumId, new ItemStack(TinkerWeaponry.patternOutputs[m], 1, neutroniumId));
		}
		TConstructRegistry.addPartMapping(TinkerTools.woodPattern, 25, neutroniumId, new ItemStack(TinkerWeaponry.arrowhead, 1, neutroniumId));
		
		TonkersEvents events = new TonkersEvents();
		MinecraftForge.EVENT_BUS.register(events);
        FMLCommonHandler.instance().bus().register(events);
        
        // recipes
        ItemStack ingot = new ItemStack(LudicrousItems.resource, 1, 6);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.toolRod, 1, infinityMetalId),
	        	"    X",
	        	"   X ",
	        	"  X  ",
	        	" X   ",
	        	"X    ",
	        	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.pickaxeHead, 1, infinityMetalId),
	        	"XXX  ",
	        	"  XX ",
	        	"   XX",
	        	"    X",
	        	"    X",
	        	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.shovelHead, 1, infinityMetalId),
            	" XXX",
            	"XXXX",
            	"  XX",
            	"  X ",
            	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.hatchetHead, 1, infinityMetalId),
            	"  X  ",
            	" XXX ",
            	"XXXXX",
            	"   X ",
            	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.binding, 1, infinityMetalId),
            	"X X",
            	" X ",
            	"X X",
            	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.toughBinding, 1, infinityMetalId),
            	"X   X",
            	" X X ",
            	"  X  ",
            	" X X ",
            	"X   X",
            	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.toughRod, 1, infinityMetalId),
            	"        X",
            	"       X ",
            	"      X  ",
            	"     X   ",
            	"    X    ",
            	"   X     ",
            	"  X      ",
            	" X       ",
            	"X        ",
            	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.largePlate, 1, infinityMetalId),
            	"XXXXX",
            	"X X X",
            	"XXXXX",
            	"X   X",
            	"XXXXX",
            	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.swordBlade, 1, infinityMetalId),
            	"   XX",
            	"  XXX",
            	" XXX ",
            	"XXX  ",
            	" X   ",
            	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.wideGuard, 1, infinityMetalId),
            	"X   ",
            	" X  ",
            	" XX ",
            	"   X",
            	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.handGuard, 1, infinityMetalId),
            	"XXX",
            	"  X",
            	"  X",
            	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.crossbar, 1, infinityMetalId),
            	"X  ",
            	"XX ",
            	" XX",
            	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.knifeBlade, 1, infinityMetalId),
            	"  XX",
            	" XX ",
            	"XX  ",
            	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.frypanHead, 1, infinityMetalId),
            	" XX ",
            	"XXXX",
            	"XXXX",
            	" XX ",
            	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.signHead, 1, infinityMetalId),
            	"XXXXX",
            	"XXXXX",
            	"XXXXX",
            	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.chiselHead, 1, infinityMetalId),
            	"    X ",
            	"   XXX",
            	"   XX ",
            	"  X   ",
            	" X    ",
            	"X     ",
            	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.scytheBlade, 1, infinityMetalId),
            	"XXXX    ",
            	"  XXXX  ",
            	"    XXX ",
            	"     XXX",
            	"      X ",
            	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.broadAxeHead, 1, infinityMetalId),
            	"  XX",
            	" XXX",
            	"XXXX",
            	"XXXX",
            	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.excavatorHead, 1, infinityMetalId),
            	"  X  ",
            	" XXX ",
            	"XXXXX",
            	"XXXX ",
            	" XX  ",
            	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.largeSwordBlade, 1, infinityMetalId),
            	"    XXXX",
            	"   XXXX ",
            	"  XXXX  ",
            	" XXXX   ",
            	"XXXX    ",
            	" XX     ",
            	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.hammerHead, 1, infinityMetalId),
            	"  X    ",
            	" XXX   ",
            	"XXXXX  ",
            	" XXXXX ",
            	"  XXXXX",
            	"   XXX ",
            	"    X  ",
            	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerWeaponry.arrowhead, 1, infinityMetalId),
            	"XX  ",
            	"XXXX",
            	" XX ",
            	" X  ",
            	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerWeaponry.partShuriken, 1, infinityMetalId),
            	"  XX",
            	"XXXX",
            	" XX ",
            	"X X ",
            	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerWeaponry.partBowLimb, 1, infinityMetalId),
            	"XXXXXXX",
            	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerWeaponry.partCrossbowLimb, 1, infinityMetalId),
            	"   XXXX",
            	"  XX   ",
            	" XX    ",
            	"XX     ",
            	"X      ",
            	"X      ",
            	'X', ingot);
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerWeaponry.partCrossbowBody, 1, infinityMetalId),
            	"X      ",
            	" XX    ",
            	" XXX   ",
            	"  XXX  ",
            	"   XX  ",
            	"     X ",
            	"      X",
            	'X', ingot);
        
        /*ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerWeaponry.partBolt, 1, infinityMetalId),
            	"   X",
            	"  X ",
            	" X  ",
            	"X   ",
            	'X', ingot);*/
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(TinkerTools.fullGuard, 1, infinityMetalId),
            	" XXX",
            	"XXX ",
            	"XXX ",
            	" XXX",
            	'X', ingot);

        // extra modifiers from catalyst
        ModifyBuilder.registerModifier(new ModExtraModifier(new ItemStack[] { new ItemStack(LudicrousItems.resource, 1, 5) }, "AvaritiaFree"){
            @Override
            public void modify (ItemStack[] recipe, ItemStack input)
            {
                NBTTagCompound tags = this.getModifierTag(input);
                tags.setBoolean(key, true);
                int modifiers = tags.getInteger("Modifiers");
                modifiers += 5;
                tags.setInteger("Modifiers", modifiers);
            }
        });

        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(getBolt(neutroniumId, neutroniumId),
                "XX", "RX", 'X', new ItemStack(LudicrousItems.resource, 1, 4), 'R', new ItemStack(TinkerTools.toolRod, 1, neutroniumId));

        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(getBolt(neutroniumId, infinityMetalId),
                "XX", "RX", 'X', new ItemStack(LudicrousItems.resource, 1, 6), 'R', new ItemStack(TinkerTools.toolRod, 1, neutroniumId));

        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(getBolt(infinityMetalId, infinityMetalId),
                "XX", "RX", 'X', new ItemStack(LudicrousItems.resource, 1, 6), 'R', new ItemStack(TinkerTools.toolRod, 1, infinityMetalId));

	}

    private static ItemStack getBolt(int main, int tip) {
        ItemStack bolt = new ItemStack(TinkerWeaponry.partBolt, 1, main);
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound mat = new NBTTagCompound();
        mat.setInteger("Material2", tip);
        tag.setTag("DualMat", mat);
        bolt.setTagCompound(tag);
        return bolt;
    }
}
