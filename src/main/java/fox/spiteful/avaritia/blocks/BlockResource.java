package fox.spiteful.avaritia.blocks;

import java.util.List;

import fox.spiteful.avaritia.Avaritia;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockResource extends BlockBase {

	public static final String[] types = new String[] {
			"neutronium",
			"infinity"
	};

	public BlockResource(int type) {
		super(Material.IRON, "avaritia_resource_" + types[type], 50.0F, 2000.0F);
		setSoundType(SoundType.METAL);
		setHarvestLevel("pickaxe", 3);
		setCreativeTab(Avaritia.tab);
	}

	public int damageDropped(int metadata) {
		return metadata;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
		for (int x = 0; x < types.length; x++) {
			list.add(new ItemStack(item, 1, x));
		}
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
