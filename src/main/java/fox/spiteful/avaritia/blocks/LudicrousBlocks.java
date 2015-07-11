package fox.spiteful.avaritia.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import fox.spiteful.avaritia.tile.TileEntityNeutron;
import net.minecraft.block.Block;

public class LudicrousBlocks {

    public static Block double_craft;
    public static Block triple_craft;
    public static Block crystal_matrix;
    public static Block resource_block;
    public static Block dire_crafting;
    public static Block neutron_collector;

    public static void voxelize(){
        double_craft = GameRegistry.registerBlock(new BlockDoubleCraft(), "Double_Craft");
        triple_craft = GameRegistry.registerBlock(new BlockTripleCraft(), "Triple_Craft");
        crystal_matrix = GameRegistry.registerBlock(new BlockCrystalMatrix(), "Crystal_Matrix");
        resource_block = GameRegistry.registerBlock(new BlockResource(), ItemBlockResource.class, "Resource_Block");
        dire_crafting = GameRegistry.registerBlock(new BlockDireCrafting(), "Dire_Crafting");
        neutron_collector = GameRegistry.registerBlock(new BlockNeutronCollector(), "Neutron_Collector");
        GameRegistry.registerTileEntity(TileEntityNeutron.class, "Avaritia_Neutron");
    }
}
