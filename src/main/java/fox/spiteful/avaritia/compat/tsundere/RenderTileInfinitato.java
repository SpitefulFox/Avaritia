package fox.spiteful.avaritia.compat.tsundere;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderTileInfinitato extends TileEntitySpecialRenderer {

	public static boolean drawHalo = true;
	private static final ResourceLocation texture = new ResourceLocation("avaritia","textures/blocks/infinitato.png");
	public static final ResourceLocation halo = new ResourceLocation("avaritia", "textures/items/halo128.png");
	private static final ModelInfinitato model = new ModelInfinitato();

	@Override
	public void renderTileEntityAt(TileEntity var1, double d0, double d1, double d2, float var8) {
		TileInfinitato potato = (TileInfinitato) var1;
		Tessellator tessellator = Tessellator.instance;
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glTranslated(d0, d1, d2);

		Minecraft mc = Minecraft.getMinecraft();
		

		GL11.glTranslatef(0.5F, 1.5F, 0.5F);
		GL11.glScalef(1F, -1F, -1F);
		int meta = potato.getWorldObj() == null ? 3 : potato.getBlockMetadata();
		float rotY = meta * 90F - 180F;
		GL11.glRotatef(rotY, 0F, 1F, 0F);

		float jump = potato.jumpTicks*0.5f;
		if(jump > 0)
			jump += var8*0.5f;

		float up = (float) -Math.abs(Math.sin(jump / 10 * Math.PI)) * 0.2F;
		float rotZ = (float) Math.sin(jump / 10 * Math.PI) * 2;

		if (drawHalo){
			mc.renderEngine.bindTexture(halo);
			GL11.glPushMatrix();
			
			double xdiff = potato.xCoord + 0.5 - mc.thePlayer.posX;
			double ydiff = potato.yCoord + 0.4 - (mc.thePlayer.posY + mc.thePlayer.eyeHeight);
			double zdiff = potato.zCoord + 0.5 - mc.thePlayer.posZ;
			
			double len = Math.sqrt(xdiff*xdiff + ydiff*ydiff + zdiff*zdiff);
			
			xdiff /= len;
			ydiff /= len;
			zdiff /= len;
			
			GL11.glTranslated(xdiff, ydiff, -zdiff);
			
			GL11.glRotatef(-rotZ, 0F, 0F, 1F);
			GL11.glRotatef(-rotY, 0F, 1F, 0F);
			GL11.glScalef(1F, -1F, -1F);
			
			GL11.glTranslatef(0F, -1.15F, 0F);
			GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);

			//GL11.glTranslatef(0F, 0F, -1F);
			float f = 1.6F;
			float f1 = 0.016666668F * f;
			GL11.glScalef(f1, f1, f1);
			GL11.glDisable(GL11.GL_LIGHTING);
			//GL11.glTranslatef(0.0F, 0F / f1, 0.0F);
			GL11.glDepthMask(false);
			//GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			tessellator.startDrawingQuads();
			int i=60;
			tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 1.0F);
			tessellator.addVertexWithUV(-i, -i, 0.0D, 0,0);
			tessellator.addVertexWithUV(-i, i, 0.0D, 1,0);
			tessellator.addVertexWithUV(i, i, 0.0D, 1,1);
			tessellator.addVertexWithUV(i, -i, 0.0D, 0,1);
			tessellator.draw();
			GL11.glDepthMask(true);
			//GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glColor4f(1F, 1F, 1F, 1F);
			//GL11.glScalef(1F / -f1, 1F / -f1, 1F / f1);
			
			GL11.glPopMatrix();
		}
		
		GL11.glTranslatef(0F, up, 0F);
		GL11.glRotatef(rotZ, 0F, 0F, 1F);

		mc.renderEngine.bindTexture(texture);
		model.render();

		GL11.glRotatef(-rotZ, 0F, 0F, 1F);
		GL11.glRotatef(-rotY, 0F, 1F, 0F);
		GL11.glColor3f(1F, 1F, 1F);
		GL11.glScalef(1F, -1F, -1F);

		if(!potato.name.isEmpty()) {
			GL11.glPushMatrix();
			GL11.glTranslatef(0F, -0.6F, 0F);
			GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
			float f = 1.6F;
			float f1 = 0.016666668F * f;
			GL11.glScalef(-f1, -f1, f1);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glTranslatef(0.0F, 0F / f1, 0.0F);
			GL11.glDepthMask(false);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			tessellator.startDrawingQuads();
			int i = mc.fontRenderer.getStringWidth(potato.name) / 2;
			tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
			tessellator.addVertex(-i - 1, -1.0D, 0.0D);
			tessellator.addVertex(-i - 1, 8.0D, 0.0D);
			tessellator.addVertex(i + 1, 8.0D, 0.0D);
			tessellator.addVertex(i + 1, -1.0D, 0.0D);
			tessellator.draw();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(true);
			mc.fontRenderer.drawString(potato.name, -mc.fontRenderer.getStringWidth(potato.name) / 2, 0, 0xFFFFFF);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glColor4f(1F, 1F, 1F, 1F);
			GL11.glScalef(1F / -f1, 1F / -f1, 1F / f1);
			GL11.glPopMatrix();
		}

		GL11.glPopMatrix();
	}
}
