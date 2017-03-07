package fox.spiteful.avaritia.blocks;

import fox.spiteful.avaritia.Avaritia;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockCrystalMatrix extends BlockBase {

	public BlockCrystalMatrix() {
		super(Material.IRON, "block_crystal_matrix", 50.0F, 2000.0F);
		setSoundType(SoundType.GLASS);
		setHarvestLevel("pickaxe", 3);
		setCreativeTab(Avaritia.tab);
	}

	@Override
	public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
		return true;
	}

	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
		return false;
	}

}
