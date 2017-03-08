package fox.spiteful.avaritia.blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fox.spiteful.avaritia.api.IModelHolder;
import fox.spiteful.avaritia.tile.TileEntityDireCrafting;
import fox.spiteful.avaritia.tile.TileEntityNeutron;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LudicrousBlocks {

	private static final List<Block> BLOCK_LIST = new ArrayList<Block>();

	public static final Block DOUBLE_CRAFT = new BlockDoubleCraft();
	public static final Block TRIPLE_CRAFT = new BlockTripleCraft();
	public static final Block DIRE_CRAFTING = new BlockDireCrafting();
	public static final Block NEUTRON_COLLECTOR = new BlockNeutronCollector();
	public static final Block CRYSTAL_MATRIX = new BlockCrystalMatrix();
	public static final Block RESOURCE_NEUTRONIUM = new BlockResource(0);
	public static final Block RESOURCE_INFINITY = new BlockResource(1);
	public static final Block NEUTRONIUM_COMPRESSOR = new BlockCompressor();

	public static void init() {
		GameRegistry.registerTileEntity(TileEntityDireCrafting.class, "avaritia_dire_craft");
		GameRegistry.registerTileEntity(TileEntityNeutron.class, "neutron_collector");
		BLOCK_LIST.addAll(Arrays.asList(DOUBLE_CRAFT, TRIPLE_CRAFT, DIRE_CRAFTING, NEUTRON_COLLECTOR, CRYSTAL_MATRIX, RESOURCE_NEUTRONIUM, RESOURCE_INFINITY, NEUTRONIUM_COMPRESSOR));
	}

	@SideOnly(Side.CLIENT)
	public static void preInitModels() {
		for (Block block : BLOCK_LIST) {
			if (block instanceof IModelHolder) {
				((IModelHolder) block).initModel();
			}
		}
	}

}
