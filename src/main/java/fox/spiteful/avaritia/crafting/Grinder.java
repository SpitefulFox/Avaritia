package fox.spiteful.avaritia.crafting;

import cpw.mods.fml.common.registry.GameRegistry;
import fox.spiteful.avaritia.blocks.LudicrousBlocks;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class Grinder {

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

        ExtremeCraftingManager.getInstance().addShapelessRecipe(new ItemStack(LudicrousItems.resource, 1, 5), new Object[]{new ItemStack(LudicrousBlocks.crystal_matrix, 1), new ItemStack(LudicrousBlocks.resource_block, 1, 0), new ItemStack(Blocks.dragon_egg)});
    }
}
