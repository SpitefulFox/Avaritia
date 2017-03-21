package fox.spiteful.avaritia.items;

import java.util.List;
import fox.spiteful.avaritia.Avaritia;
import net.minecraft.client.resources.I18n;
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
		this.setCreativeTab(Avaritia.tab);

	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void addInformation(ItemStack item, EntityPlayer player, List tooltip, boolean wut) {
		tooltip.add(I18n.format("tooltip.morvinabox.desc"));
    	tooltip.add(TextFormatting.DARK_GRAY +""+ TextFormatting.ITALIC + I18n.format("tooltip.morvinabox.subdesc"));
    }
	

}
