package fox.spiteful.avaritia.render;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IHaloRenderItem {

	@SideOnly(Side.CLIENT)
	public boolean drawHalo(ItemStack stack);
	@SideOnly(Side.CLIENT)
	public Models getHaloTexture(ItemStack stack);
	@SideOnly(Side.CLIENT)
	public int getHaloSize(ItemStack stack);
	@SideOnly(Side.CLIENT)
	public boolean drawPulseEffect(ItemStack stack);
	@SideOnly(Side.CLIENT)
	public int getHaloColour(ItemStack stack);
}
