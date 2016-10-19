package fox.spiteful.avaritia.blocks;

import fox.spiteful.avaritia.tile.TileEntityDireCrafting;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class LudicrousBlocks {

    public static Block double_craft;
    public static Block triple_craft;
    public static Block dire_crafting;

    public static void voxelize(){
        double_craft = register(new BlockDoubleCraft(), "double_craft");
        triple_craft = register(new BlockTripleCraft(), "triple_craft");
        dire_crafting = register(new BlockDireCrafting(), "dire_crafting");
        GameRegistry.registerTileEntity(TileEntityDireCrafting.class, "avaritia_dire_craft");

    }

    private static Block register(Block block, String name){
        block.setRegistryName(name);
        GameRegistry.register(block);
        return block;
    }
}
