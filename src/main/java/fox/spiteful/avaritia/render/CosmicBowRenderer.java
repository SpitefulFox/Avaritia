package fox.spiteful.avaritia.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.ReflectionHelper;
import fox.spiteful.avaritia.Lumberjack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

public class CosmicBowRenderer extends CosmicItemRenderer implements IItemRenderer {
	
	private static String[] iiuObf = new String[] { "itemInUse", "field_71074_e", "f" };
	private static String[] iiucObf = new String[] { "itemInUseCount", "field_71072_f", "g" };
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
		if (type == ItemRenderType.EQUIPPED) {
			GL11.glPushMatrix();
			GL11.glTranslatef(0.2F, -0.3F, 0.15F);
			super.renderItem(type, stack, data);
			GL11.glPopMatrix();
		} else {
			super.renderItem(type, stack, data);
		}
	}

	public static int getBowFrame(EntityPlayer player) {
		ItemStack inuse = ReflectionHelper.getPrivateValue(EntityPlayer.class, player, iiuObf);
		int time = ReflectionHelper.getPrivateValue(EntityPlayer.class, player, iiucObf);
		
		if (inuse != null) {
			int max = inuse.getMaxItemUseDuration();
			double pull = (max - time) / (double)max;
			return Math.max(0, (int)Math.ceil(pull*3.0)-1);
		}
		return 0;
	}
	
	@Override
	public IIcon getStackIcon(ItemStack stack, int pass, EntityPlayer player) {
		Item item = stack.getItem();
        ItemStack inuse;
        if(player != null)
		    inuse = ReflectionHelper.getPrivateValue(EntityPlayer.class, player, iiuObf);
        else
            inuse = stack;
        int time;
        if(player != null)
		    time = ReflectionHelper.getPrivateValue(EntityPlayer.class, player, iiucObf);
        else
            time = 0;
		
		return item.getIcon(stack, pass, player, inuse, time);
	}
}
