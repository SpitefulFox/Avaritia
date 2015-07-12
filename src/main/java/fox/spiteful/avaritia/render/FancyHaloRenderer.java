package fox.spiteful.avaritia.render;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import fox.spiteful.avaritia.items.ItemResource;
import fox.spiteful.avaritia.items.LudicrousItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

public class FancyHaloRenderer implements IItemRenderer {
	
	public Random rand = new Random();
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		int meta = item.getItemDamage();
		Item itype = item.getItem();
		if (itype == LudicrousItems.resource) {
			if (meta < 2) { return false; }
		} else if (itype == LudicrousItems.singularity) {
			
		} else {
			return false;
		}
		
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
		Item itype = item.getItem();
		
		if (itype == LudicrousItems.resource) {
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
			case 5:
			case 6:
				GL11.glColor4d(0, 0, 0, 1.0);
				spread = 10;
				break;
			default:
				GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
				spread = 5;
			}
		} else if (itype == LudicrousItems.singularity) {
			GL11.glColor4d(0, 0, 0, 1.0);
			spread = 4;
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
			
			if (itype == LudicrousItems.resource && (meta == 5 || meta == 6)) {
				GL11.glPushMatrix();
				double xs = (rand.nextGaussian() * 0.15) + 0.95;
				double ox = (1-xs)/2.0;
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glTranslated(ox*16.0, ox*16.0, 1.0);
				GL11.glScaled(xs, xs, 1.0);
				
				//GL11.glColor4d(1.0, 1.0, 1.0, 0.3);
				
				IIcon icon = item.getItem().getIcon(item, 0);
				
				t.startDrawingQuads();
				t.setColorRGBA_F(1.0f, 1.0f, 1.0f, 0.6f);
				t.addVertexWithUV(0-ox, 0-ox, 0, icon.getMinU(), icon.getMinV());
				t.addVertexWithUV(0-ox, 16+ox, 0, icon.getMinU(), icon.getMaxV());
				t.addVertexWithUV(16+ox, 16+ox, 0, icon.getMaxU(), icon.getMaxV());
				t.addVertexWithUV(16+ox, 0-ox, 0, icon.getMaxU(), icon.getMinV());
				t.draw();
				
				GL11.glPopMatrix();
			}
			
			r.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), item, 0, 0, true);
			
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			
			r.renderWithColor = true;
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
			break;
		default:
			break;
		}
	}
}
