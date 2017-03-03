package fox.spiteful.avaritia.blocks;

import fox.spiteful.avaritia.tile.TileEntityDireCrafting;
import fox.spiteful.avaritia.tile.TileEntityNeutron;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class LudicrousBlocks {

    public static Block double_craft;
    public static Block triple_craft;
    public static Block dire_crafting;
    public static Block neutron_collector;

    public static void voxelize(){
        double_craft = register(new BlockDoubleCraft(), "double_craft");
        triple_craft = register(new BlockTripleCraft(), "triple_craft");
        dire_crafting = register(new BlockDireCrafting(), "dire_crafting");
        neutron_collector = register(new BlockNeutronCollector(), "neutron_collector");
        GameRegistry.registerTileEntity(TileEntityDireCrafting.class, "avaritia_dire_craft");
        GameRegistry.registerTileEntity(TileEntityNeutron.class, "neutron_collector");

    }

    private static Block register(Block block, String name){
        block.setRegistryName(name);
        GameRegistry.register(block);
        ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(name);
        GameRegistry.register(itemBlock);
        return block;
    }
}
