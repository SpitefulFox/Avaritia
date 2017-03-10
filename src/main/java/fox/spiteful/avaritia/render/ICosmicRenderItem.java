package fox.spiteful.avaritia.render;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ICosmicRenderItem {

	@SideOnly(Side.CLIENT)
	public Models getMaskTexture(ItemStack stack, EntityPlayer player);
	
	@SideOnly(Side.CLIENT)
	public float getMaskMultiplier(ItemStack stack, EntityPlayer player);
}
