package morph.avaritia.init;

import morph.avaritia.Avaritia;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.block.*;
import morph.avaritia.tile.TileDireCraftingTable;
import morph.avaritia.tile.TileNeutronCollector;
import morph.avaritia.tile.TileNeutroniumCompressor;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

public class ModBlocks {

    public static BlockDoubleCraft double_craft;
    public static BlockTripleCraft triple_craft;
    public static BlockDireCrafting dire_craft;

    public static BlockResource resource;

    public static BlockNeutronCollector neutron_collector;
    public static BlockNeutroniumCompressor neutronium_compressor;

    public static void init() {
        double_craft = register(new BlockDoubleCraft());
        registerItemBlock(double_craft);

        triple_craft = register(new BlockTripleCraft());
        registerItemBlock(triple_craft);

        dire_craft = register(new BlockDireCrafting());
        registerItemBlock(dire_craft);
        GameRegistry.registerTileEntity(TileDireCraftingTable.class, "avaritia_dire_craft");

        resource = register(new BlockResource());
        register(new ItemBlockResource(resource));

        neutron_collector = register(new BlockNeutronCollector());
        registerItemBlock(neutron_collector);
        GameRegistry.registerTileEntity(TileNeutronCollector.class, "neutron_collector");

        neutronium_compressor = register(new BlockNeutroniumCompressor());
        registerItemBlock(neutronium_compressor);
        GameRegistry.registerTileEntity(TileNeutroniumCompressor.class, "neutronium_compressor");
    }

    public static <V extends IForgeRegistryEntry<?>> V register(V registryObject) {
        GameRegistry.register(registryObject);

        if (registryObject instanceof IModelRegister) {
            Avaritia.proxy.addModelRegister((IModelRegister) registryObject);
        }

        return registryObject;
    }

    public static ItemBlock registerItemBlock(Block block) {
        ItemBlock itemBlock = new ItemBlock(block);
        register(itemBlock.setRegistryName(block.getRegistryName()));
        return itemBlock;
    }

}
