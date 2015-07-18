package fox.spiteful.avaritia.render;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

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
		Item itype = item.getItem();
		if (itype instanceof IHaloRenderItem) {
			IHaloRenderItem ihri = (IHaloRenderItem)itype;
			
			if (!(ihri.drawHalo(item) || ihri.drawPulseEffect(item))) {
				return false;
			}
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
		boolean renderHalo = false;
		boolean renderPulse = false;
		
		int spread = 0;
		IIcon halo = null;
		int haloColour = 0;
		
		Item itype = item.getItem();
		if (itype instanceof IHaloRenderItem) {
			IHaloRenderItem ihri = (IHaloRenderItem)itype;
			
			spread = ihri.getHaloSize(item);
			halo = ihri.getHaloTexture(item);
			haloColour = ihri.getHaloColour(item);
			
			renderHalo = ihri.drawHalo(item);
			renderPulse = ihri.drawPulseEffect(item);
		}
		
		RenderItem r = RenderItem.getInstance();
		Minecraft mc = Minecraft.getMinecraft();
		Tessellator t = Tessellator.instance;
		
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
			
			if (renderHalo) {
				float ca = (float)(haloColour >> 24 & 255) / 255.0F;
				float cr = (float)(haloColour >> 16 & 255) / 255.0F;
	            float cg = (float)(haloColour >> 8 & 255) / 255.0F;
	            float cb = (float)(haloColour & 255) / 255.0F;
	            GL11.glColor4f(cr, cg, cb, ca);
				
				t.startDrawingQuads();
				t.addVertexWithUV(0-spread, 0-spread, 0, halo.getMinU(), halo.getMinV());
				t.addVertexWithUV(0-spread, 16+spread, 0, halo.getMinU(), halo.getMaxV());
				t.addVertexWithUV(16+spread, 16+spread, 0, halo.getMaxU(), halo.getMaxV());
				t.addVertexWithUV(16+spread, 0-spread, 0, halo.getMaxU(), halo.getMinV());
				t.draw();
			}
			
			if (renderPulse) {
				GL11.glPushMatrix();
				double xs = (rand.nextGaussian() * 0.15) + 0.95;
				double ox = (1-xs)/2.0;
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glTranslated(ox*16.0, ox*16.0, 1.0);
				GL11.glScaled(xs, xs, 1.0);
				
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
