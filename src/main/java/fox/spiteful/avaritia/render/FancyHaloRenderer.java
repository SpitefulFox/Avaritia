package fox.spiteful.avaritia.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import fox.spiteful.avaritia.items.ItemResource;
import fox.spiteful.avaritia.items.LudicrousItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

public class FancyHaloRenderer implements IItemRenderer {
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		int meta = item.getItemDamage();
		if (meta < 2) { return false; }
		
		switch(type) {
		case INVENTORY:
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		RenderItem r = RenderItem.getInstance();
		Minecraft mc = Minecraft.getMinecraft();
		Tessellator t = Tessellator.instance;
		IIcon[] halos = ((ItemResource)LudicrousItems.resource).halo;
		IIcon halo = halos[1];
		int spread = 8;
		int meta = item.getItemDamage();
		
		switch(meta) {
		case 2:
			GL11.glColor4d(1.0, 1.0, 1.0, 0.2);
			spread = 8;
			break;
		case 3:
			GL11.glColor4d(1.0, 1.0, 1.0, 0.3);
			spread = 8;
			break;
		case 4:
			GL11.glColor4d(1.0, 1.0, 1.0, 0.6);
			spread = 8;
			break;
		default:
			GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
			spread = 5;
		}
		
		switch(type) {
		case ENTITY:
			break;
		case INVENTORY:
			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			RenderHelper.enableGUIStandardItemLighting();
			
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			
			t.startDrawingQuads();
			t.addVertexWithUV(0-spread, 0-spread, 0, halo.getMinU(), halo.getMinV());
			t.addVertexWithUV(0-spread, 16+spread, 0, halo.getMinU(), halo.getMaxV());
			t.addVertexWithUV(16+spread, 16+spread, 0, halo.getMaxU(), halo.getMaxV());
			t.addVertexWithUV(16+spread, 0-spread, 0, halo.getMaxU(), halo.getMinV());
			t.draw();
			
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			r.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), item, 0, 0, true);
			r.renderWithColor = true;
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
			break;
		default:
			break;
		}
	}
}
