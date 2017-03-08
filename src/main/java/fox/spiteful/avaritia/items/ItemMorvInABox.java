package fox.spiteful.avaritia.items;

import java.util.List;
import fox.spiteful.avaritia.Avaritia;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMorvInABox extends Item {

	public Models nameoverlay;
	
	public ItemMorvInABox() {
		this.setUnlocalizedName("morvinabox");
        this.setCreativeTab(Avaritia.tab);
        this.setTextureName("avaritia:morvinabox");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void addInformation(ItemStack item, EntityPlayer player, List tooltip, boolean wut) {
		tooltip.add(StatCollector.translateToLocal("tooltip.morvinabox.desc"));
    	tooltip.add(TextFormatting.DARK_GRAY +""+ TextFormatting.ITALIC + StatCollector.translateToLocal("tooltip.morvinabox.subdesc"));
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
		super.registerIcons(ir);
		this.nameoverlay = ir.registerIcon("avaritia:morvinabox2");
	}
	
}
