package fox.spiteful.avaritia.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.Lumberjack;
import fox.spiteful.avaritia.compat.botania.alfheim.ApocalypseCloudRenderer;
import fox.spiteful.avaritia.compat.botania.alfheim.ApocalypseSkyRenderer;
import fox.spiteful.avaritia.compat.botania.alfheim.TeleportHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;

public class ItemMorvInABox extends Item {

	public IIcon nameoverlay;
	
	public ItemMorvInABox() {
		this.setUnlocalizedName("morvinabox");
        this.setCreativeTab(Avaritia.tab);
        this.setTextureName("avaritia:morvinabox");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void addInformation(ItemStack item, EntityPlayer player, List tooltip, boolean wut) {
		tooltip.add(StatCollector.translateToLocal("tooltip.morvinabox.desc"));
    	tooltip.add(EnumChatFormatting.DARK_GRAY +""+ EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.morvinabox.subdesc"));
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
		super.registerIcons(ir);
		this.nameoverlay = ir.registerIcon("avaritia:morvinabox2");
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
		/*//if (world.isRemote) {
			Lumberjack.info("DESTROY SPACE");
			WorldProvider p = world.provider;//DimensionManager.getProvider(0);
			Lumberjack.info(p);
			p.setSkyRenderer(new ApocalypseSkyRenderer());
			p.setCloudRenderer(new ApocalypseCloudRenderer());
			Lumberjack.info(p.getCloudRenderer());
		//}*/
		/*if (DimensionManager.getWorld(0) == world) {
			player.travelToDimension(13);
		} else {
			player.travelToDimension(0);
		}*/
		
		TeleportHelper.travelToOrFromAlfheim(player);
        return stack;
    }
}
