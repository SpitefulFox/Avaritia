package fox.spiteful.avaritia.render;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public interface ICosmicRenderItem {

	public IIcon getMaskTexture(ItemStack stack);
}
