package fox.spiteful.avaritia.blocks;

import java.util.Random;

import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.tile.TileEntityNeutron;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockNeutronCollector extends BlockContainerBase {

	public BlockNeutronCollector() {
		super(Material.IRON, "neutron_collector", 20.0F, -1.0F);
		setSoundType(SoundType.METAL);
		setHarvestLevel("pickaxe", 3);
		setCreativeTab(Avaritia.tab);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}
		else {
			player.openGui(Avaritia.instance, 2, world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityNeutron();
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileEntityNeutron) {
			TileEntityNeutron machine = (TileEntityNeutron) tile;
			int l = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

			if (l == 0) {
				machine.setFacing(2);
			}

			if (l == 1) {
				machine.setFacing(5);
			}

			if (l == 2) {
				machine.setFacing(3);
			}

			if (l == 3) {
				machine.setFacing(4);
			}
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntityNeutron collector = (TileEntityNeutron) world.getTileEntity(pos);

		if (collector != null) {
			Random rand = world.rand;
			ItemStack itemstack = collector.getStackInSlot(0);

			if (itemstack != null) {
				float f = rand.nextFloat() * 0.8F + 0.1F;
				float f1 = rand.nextFloat() * 0.8F + 0.1F;
				float f2 = rand.nextFloat() * 0.8F + 0.1F;

				while (itemstack.stackSize > 0) {
					int j1 = rand.nextInt(21) + 10;

					if (j1 > itemstack.stackSize) {
						j1 = itemstack.stackSize;
					}

					itemstack.stackSize -= j1;
					EntityItem entityitem = new EntityItem(world, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2, new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

					if (itemstack.hasTagCompound()) {
						entityitem.getEntityItem().setTagCompound(itemstack.getTagCompound().copy());
					}

					float f3 = 0.05F;
					entityitem.motionX = (float) rand.nextGaussian() * f3;
					entityitem.motionY = (float) rand.nextGaussian() * f3 + 0.2F;
					entityitem.motionZ = (float) rand.nextGaussian() * f3;
					world.spawnEntityInWorld(entityitem);
				}
			}

			//world.func_147453_f(x, y, z, block);
		}

		super.breakBlock(world, pos, state);
	}
}
