package fox.spiteful.avaritia.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public interface ICosmicRenderItem {

	@SideOnly(Side.CLIENT)
	public IIcon getMaskTexture(ItemStack stack);
}
