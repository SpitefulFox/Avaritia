package fox.spiteful.avaritia.crafting;

import cpw.mods.fml.common.registry.GameRegistry;
import fox.spiteful.avaritia.blocks.LudicrousBlocks;
import fox.spiteful.avaritia.compat.Compat;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class Grinder {

    public static ShapelessOreRecipe catalyst;

    public static void artsAndCrafts(){

        GameRegistry.addShapedRecipe(new ItemStack(LudicrousItems.resource, 1, 0), new Object[]{"X X", " X ", "X X", 'X', new ItemStack(Items.diamond)});
        GameRegistry.addShapedRecipe(new ItemStack(LudicrousItems.resource, 1, 1), new Object[]{"DSD", "DSD", 'D', new ItemStack(LudicrousItems.resource, 1, 0), 'S', new ItemStack(Items.nether_star)});
        GameRegistry.addShapedRecipe(new ItemStack(LudicrousBlocks.double_craft, 1), new Object[]{"CCC", "CCC", "CCC", 'C', new ItemStack(Blocks.crafting_table)});
        GameRegistry.addShapedRecipe(new ItemStack(LudicrousBlocks.triple_craft, 1), new Object[]{"CCC", "CCC", "CCC", 'C', new ItemStack(LudicrousBlocks.double_craft)});
        GameRegistry.addShapedRecipe(new ItemStack(Blocks.crafting_table, 9), new Object[]{"C", 'C', new ItemStack(LudicrousBlocks.double_craft)});
        GameRegistry.addShapedRecipe(new ItemStack(LudicrousBlocks.double_craft, 9), new Object[]{"C", 'C', new ItemStack(LudicrousBlocks.triple_craft)});
        GameRegistry.addShapedRecipe(new ItemStack(LudicrousBlocks.crystal_matrix, 1, 0), new Object[]{"CCC", "CCC", "CCC", 'C', new ItemStack(LudicrousItems.resource, 1, 1)});
        GameRegistry.addShapedRecipe(new ItemStack(LudicrousItems.resource, 9, 1), new Object[]{"C", 'C', new ItemStack(LudicrousBlocks.crystal_matrix, 1, 0)});
        GameRegistry.addShapedRecipe(new ItemStack(LudicrousBlocks.dire_crafting, 1), new Object[]{"CCC", "CXC", "CCC", 'C', new ItemStack(LudicrousItems.resource, 1, 1), 'X', new ItemStack(LudicrousBlocks.triple_craft)});
        GameRegistry.addShapedRecipe(new ItemStack(LudicrousItems.resource, 1, 3), new Object[]{"CCC", "CCC", "CCC", 'C', new ItemStack(LudicrousItems.resource, 1, 2)});
        GameRegistry.addShapedRecipe(new ItemStack(LudicrousItems.resource, 1, 4), new Object[]{"CCC", "CCC", "CCC", 'C', new ItemStack(LudicrousItems.resource, 1, 3)});
        GameRegistry.addShapedRecipe(new ItemStack(LudicrousItems.resource, 9, 3), new Object[]{"C", 'C', new ItemStack(LudicrousItems.resource, 1, 4)});
        GameRegistry.addShapedRecipe(new ItemStack(LudicrousBlocks.resource_block, 1, 0), new Object[]{"CCC", "CCC", "CCC", 'C', new ItemStack(LudicrousItems.resource, 1, 4)});
        GameRegistry.addShapedRecipe(new ItemStack(LudicrousItems.resource, 9, 4), new Object[]{"C", 'C', new ItemStack(LudicrousBlocks.resource_block, 1, 0)});

        ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(LudicrousBlocks.neutron_collector, 1), new Object[]{
                "IIQQQQQII",
                "I QQQQQ I",
                "I  RRR  I",
                "X RRRRR X",
                "I RRXRR I",
                "X RRRRR X",
                "I  RRR  I",
                "I       I",
                "IIIXIXIII",
                'X', new ItemStack(LudicrousItems.resource, 1, 1),
                'I', new ItemStack(Blocks.iron_block, 1),
                'Q', new ItemStack(Blocks.quartz_block, 1),
                'R', new ItemStack(Blocks.redstone_block, 1)});

        ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(LudicrousItems.resource, 1, 6), new Object[]{
                "NNNNNNNNN",
                "NCXXCXXCN",
                "NXCCXCCXN",
                "NCXXCXXCN",
                "NNNNNNNNN",
                'C', new ItemStack(LudicrousItems.resource, 1, 1),
                'N', new ItemStack(LudicrousItems.resource, 1, 4),
                'X', new ItemStack(LudicrousItems.resource, 1, 5)});

        catalyst = ExtremeCraftingManager.getInstance().addShapelessOreRecipe(new ItemStack(LudicrousItems.resource, 2, 5), new Object[]{
                new ItemStack(Blocks.emerald_block, 1),
                new ItemStack(LudicrousItems.singularity, 1, 0), new ItemStack(LudicrousItems.singularity, 1, 1),
                new ItemStack(LudicrousItems.singularity, 1, 2), new ItemStack(LudicrousItems.singularity, 1, 3),
                new ItemStack(LudicrousItems.singularity, 1, 4), new ItemStack(LudicrousItems.ultimate_stew),
                new ItemStack(LudicrousItems.cosmic_meatballs), new ItemStack(LudicrousItems.endest_pearl)});

        ItemStack result = new ItemStack(LudicrousItems.infinity_pickaxe, 1);
        result.addEnchantment(Enchantment.fortune, 10);
        ExtremeCraftingManager.getInstance().addRecipe(result, new Object[]{
                " IIIIIII ",
                "IIIIIIIII",
                "II  N  II",
                "    N    ",
                "    N    ",
                "    N    ",
                "    N    ",
                "    N    ",
                "    N    ",
                'I', new ItemStack(LudicrousItems.resource, 1, 6),
                'N', new ItemStack(LudicrousItems.resource, 1, 4)});

        ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(LudicrousItems.infinity_sword), new Object[]{
                "       II",
                "      III",
                "     III ",
                "    III  ",
                " C III   ",
                "  CII    ",
                "  NC     ",
                " N  C    ",
                "X        ",
                'I', new ItemStack(LudicrousItems.resource, 1, 6),
                'X', new ItemStack(LudicrousItems.resource, 1, 5),
                'C', new ItemStack(LudicrousItems.resource, 1, 1),
                'N', new ItemStack(LudicrousItems.resource, 1, 4)});

        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(LudicrousItems.skull_sword), new Object[]{
                "       IX",
                "      IXI",
                "     IXI ",
                "    IXI  ",
                " B IXI   ",
                "  BXI    ",
                "  WB     ",
                " W  B    ",
                "D        ",
                'I', new ItemStack(LudicrousItems.resource, 1, 1),
                'X', new ItemStack(Items.blaze_powder),
                'B', new ItemStack(Items.bone),
                'D', new ItemStack(Items.nether_star),
                'W', "logWood"});

        ExtremeCraftingManager.getInstance().addSingularityRecipe(new ItemStack(LudicrousItems.singularity, 1, 0), new ItemStack(Blocks.iron_block, 1));
        ExtremeCraftingManager.getInstance().addSingularityRecipe(new ItemStack(LudicrousItems.singularity, 1, 1), new ItemStack(Blocks.gold_block, 1));
        ExtremeCraftingManager.getInstance().addSingularityRecipe(new ItemStack(LudicrousItems.singularity, 1, 2), new ItemStack(Blocks.lapis_block, 1));
        ExtremeCraftingManager.getInstance().addSingularityRecipe(new ItemStack(LudicrousItems.singularity, 1, 3), new ItemStack(Blocks.redstone_block, 1));
        ExtremeCraftingManager.getInstance().addSingularityRecipe(new ItemStack(LudicrousItems.singularity, 1, 4), new ItemStack(Blocks.quartz_block, 1));

        if(!OreDictionary.getOres("blockCopper").isEmpty()){
            ExtremeCraftingManager.getInstance().addOreSingularityRecipe(new ItemStack(LudicrousItems.singularity, 1, 5), "blockCopper");
            catalyst.getInput().add(new ItemStack(LudicrousItems.singularity, 1, 5));
        }
        if(!OreDictionary.getOres("blockTin").isEmpty()){
            ExtremeCraftingManager.getInstance().addOreSingularityRecipe(new ItemStack(LudicrousItems.singularity, 1, 6), "blockTin");
            catalyst.getInput().add(new ItemStack(LudicrousItems.singularity, 1, 6));
        }
        if(!OreDictionary.getOres("blockLead").isEmpty()){
            ExtremeCraftingManager.getInstance().addOreSingularityRecipe(new ItemStack(LudicrousItems.singularity, 1, 7), "blockLead");
            catalyst.getInput().add(new ItemStack(LudicrousItems.singularity, 1, 7));
        }
        if(!OreDictionary.getOres("blockSilver").isEmpty()){
            ExtremeCraftingManager.getInstance().addOreSingularityRecipe(new ItemStack(LudicrousItems.singularity, 1, 8), "blockSilver");
            catalyst.getInput().add(new ItemStack(LudicrousItems.singularity, 1, 8));
        }
        if(!OreDictionary.getOres("blockNickel").isEmpty()){
            ExtremeCraftingManager.getInstance().addOreSingularityRecipe(new ItemStack(LudicrousItems.singularity, 1, 9), "blockNickel");
            catalyst.getInput().add(new ItemStack(LudicrousItems.singularity, 1, 9));
        }
        if(!OreDictionary.getOres("blockEnderium").isEmpty()){
            catalyst.getInput().add(OreDictionary.getOres("blockEnderium"));
        }
        if(!OreDictionary.getOres("blockSteel").isEmpty()){
            catalyst.getInput().add(OreDictionary.getOres("blockSteel"));
        }
        if(!OreDictionary.getOres("ingotTartarite").isEmpty()){
            catalyst.getInput().add(OreDictionary.getOres("ingotTartarite"));
        }
        
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(new ItemStack(LudicrousItems.endest_pearl), new Object[]{
            "   EEE   ",
            " EEPPPEE ",
            " EPPPPPE ",
            "EPPPNPPPE",
            "EPPNSNPPE",
            "EPPPNPPPE",
            " EPPPPPE ",
            " EEPPPEE ",
            "   EEE   ",
            'E', new ItemStack(Blocks.end_stone),
            'P', new ItemStack(Items.ender_pearl),
            'S', new ItemStack(Items.nether_star),
            'N', new ItemStack(LudicrousItems.resource, 1, 4)});
    }
    
    public static void lastMinuteChanges(){
    	if (Compat.mfr) {
    		catalyst.getInput().add(OreDictionary.getOres("record"));
    	} else {
    		catalyst.getInput().add(new ItemStack(LudicrousItems.resource, 1, 7));
    		IRecipe smashysmashy;
    		//if (Compat.botan) {
    		//	smashysmashy = new ShapelessOreRecipe(new ItemStack(LudicrousItems.resource, 4, 7), "record");
    		//} else {
    			smashysmashy = new ShapelessOreRecipe(new ItemStack(LudicrousItems.resource, 8, 7), "record");
    		//}
    		GameRegistry.addRecipe(smashysmashy);
    	}
    }
}
