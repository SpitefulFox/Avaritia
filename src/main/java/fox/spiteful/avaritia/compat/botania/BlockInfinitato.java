package fox.spiteful.avaritia.compat.botania;

import net.minecraft.block.BlockContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;

public class BlockInfinitato extends BlockContainer implements ILexiconable {
	public static LexiconEntry lexiconEntry;
	
	public BlockInfinitato() {
		super(Material.cloth);
		setHardness(0.25F);
		setBlockName("infinitato");
		float f = 1F / 16F * 4F;
		setBlockBounds(f, 0, f, 1F - f, f*3, 1F - f);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		// NO-OP
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return Blocks.hardened_clay.getIcon(0, 11);
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		TileEntity tile = par1World.getTileEntity(par2, par3, par4);
		if(tile instanceof TileInfinitato) {
			((TileInfinitato) tile).interact();
			par1World.spawnParticle("heart", par2 + minX + Math.random() * (maxX - minX), par3 + maxY, par4 + minZ + Math.random() * (maxZ - minZ), 0, 0 ,0);
			par1World.spawnParticle("heart", par2 + minX + Math.random() * (maxX - minX), par3 + maxY, par4 + minZ + Math.random() * (maxZ - minZ), 0, 0 ,0);
			par1World.spawnParticle("heart", par2 + minX + Math.random() * (maxX - minX), par3 + maxY, par4 + minZ + Math.random() * (maxZ - minZ), 0, 0 ,0);
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack par6ItemStack) {
		int l1 = MathHelper.floor_double(par5EntityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

		par1World.setBlockMetadataWithNotify(par2, par3, par4, l1, 2);
		if (par6ItemStack.hasDisplayName())
			((TileInfinitato) par1World.getTileEntity(par2, par3, par4)).name = par6ItemStack.getDisplayName();
	}

	@Override
	public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer) {
		if(!par6EntityPlayer.capabilities.isCreativeMode)
			dropBlockAsItem(par1World, par2, par3, par4, par5, 0);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> list = new ArrayList();
		TileEntity tile = world.getTileEntity(x, y, z);

		if(tile != null) {
			ItemStack stack = new ItemStack(this);
			String name = ((TileInfinitato) tile).name;
			if(!name.isEmpty())
				stack.setStackDisplayName(name);
			list.add(stack);
		}

		return list;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return RenderInfinitato.renderID;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileInfinitato();
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return lexiconEntry;
	}

}
