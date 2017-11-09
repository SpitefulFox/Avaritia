package morph.avaritia.init;

import morph.avaritia.Avaritia;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.block.*;
import morph.avaritia.tile.TileDireCraftingTable;
import morph.avaritia.tile.TileNeutronCollector;
import morph.avaritia.tile.TileNeutroniumCompressor;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Consumer;

public class ModBlocks {

    public static BlockCompressedCraftingTable compressedCraftingTable;
    public static BlockDoubleCompressedCraftingTable doubleCompressedCraftingTable;
    public static BlockExtremeCraftingTable extremeCraftingTable;

    public static BlockResource resource;

    public static BlockNeutronCollector neutron_collector;
    public static BlockNeutroniumCompressor neutronium_compressor;

    public static void init() {
        compressedCraftingTable = registerBlock(new BlockCompressedCraftingTable());
        registerItemBlock(compressedCraftingTable);

        doubleCompressedCraftingTable = registerBlock(new BlockDoubleCompressedCraftingTable());
        registerItemBlock(doubleCompressedCraftingTable);

        extremeCraftingTable = registerBlock(new BlockExtremeCraftingTable());
        registerItemBlock(extremeCraftingTable);
        GameRegistry.registerTileEntity(TileDireCraftingTable.class, "avaritia_extreme_crafting_table");

        resource = registerBlock(new BlockResource());
        registerItem(new ItemBlockResource(resource));

        neutron_collector = registerBlock(new BlockNeutronCollector());
        registerItemBlock(neutron_collector);
        GameRegistry.registerTileEntity(TileNeutronCollector.class, "neutron_collector");

        neutronium_compressor = registerBlock(new BlockNeutroniumCompressor());
        registerItemBlock(neutronium_compressor);
        GameRegistry.registerTileEntity(TileNeutroniumCompressor.class, "neutronium_compressor");
    }

    public static <V extends Block> V registerBlock(V block) {
        registerImpl(block, ForgeRegistries.BLOCKS::register);
        return block;
    }

    public static <V extends Item> V registerItem(V item) {
        registerImpl(item, ForgeRegistries.ITEMS::register);
        return item;
    }

    public static <V extends IForgeRegistryEntry<V>> V registerImpl(V registryObject, Consumer<V> registerCallback) {
        registerCallback.accept(registryObject);

        if (registryObject instanceof IModelRegister) {
            Avaritia.proxy.addModelRegister((IModelRegister) registryObject);
        }

        return registryObject;
    }

    public static ItemBlock registerItemBlock(Block block) {
        ItemBlock itemBlock = new ItemBlock(block);
        registerItem(itemBlock.setRegistryName(block.getRegistryName()));
        return itemBlock;
    }

}
