package fox.spiteful.avaritia.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public interface IHaloRenderItem {

	@SideOnly(Side.CLIENT)
	public boolean drawHalo(ItemStack stack);
	@SideOnly(Side.CLIENT)
	public IIcon getHaloTexture(ItemStack stack);
	@SideOnly(Side.CLIENT)
	public int getHaloSize(ItemStack stack);
	@SideOnly(Side.CLIENT)
	public boolean drawPulseEffect(ItemStack stack);
	@SideOnly(Side.CLIENT)
	public int getHaloColour(ItemStack stack);
}
