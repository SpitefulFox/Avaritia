package fox.spiteful.avaritia.blocks;

import fox.spiteful.avaritia.api.IModelHolder;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author p455w0rd
 *
 */
public class BlockContainerBase extends BlockContainer implements IModelHolder {

	public BlockContainerBase(Material materialIn, MapColor color, String name, float hardness, float resistance) {
		super(materialIn, color);
		setUnlocalizedName(name);
		setRegistryName(name);
		if (resistance != -1.0F) {
			setResistance(resistance);
		}
		setHardness(hardness);
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());
	}

	public BlockContainerBase(Material materialIn, String name, float hardness, float resistance) {
		super(materialIn);
		setUnlocalizedName(name);
		setRegistryName(name);
		if (resistance != -1.0F) {
			setResistance(resistance);
		}
		setHardness(hardness);
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlock(this), getRegistryName());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return null;
	}

}
