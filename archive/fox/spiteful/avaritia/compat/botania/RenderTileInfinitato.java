package fox.spiteful.avaritia.compat.botania;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import fox.spiteful.avaritia.compat.tails.InfiniteFoxes;
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
	public void renderTileEntityAt(TileEntity var1, double x, double y, double z, float partialTicks) {
		TileInfinitato potato = (TileInfinitato) var1;
		Tessellator tessellator = Tessellator.instance;
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glTranslated(x, y, z);

		Minecraft mc = Minecraft.getMinecraft();
		

		GL11.glTranslatef(0.5F, 1.5F, 0.5F);
		GL11.glScalef(1F, -1F, -1F);
		int meta = potato.getWorldObj() == null ? 3 : potato.getBlockMetadata();
		
		if (drawHalo){
			mc.renderEngine.bindTexture(halo);
			GL11.glPushMatrix();
			
			double xdiff = (potato.xCoord + 0.5) - RenderManager.instance.viewerPosX;//.renderPosX;
			double ydiff = (potato.yCoord + 0.4) - RenderManager.instance.viewerPosY;//.renderPosY;
			double zdiff = (potato.zCoord + 0.5) - RenderManager.instance.viewerPosZ;//.renderPosZ;
			
			double len = Math.sqrt(xdiff*xdiff + ydiff*ydiff + zdiff*zdiff);
						
			xdiff /= len;
			ydiff /= len;
			zdiff /= len;
			
			GL11.glTranslated(-xdiff, ydiff, zdiff);
			
			//GL11.glRotatef(-rotZ, 0F, 0F, 1F);
			//GL11.glRotatef(-rotY, 0F, 1F, 0F);
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
		
		
		float rotY = meta * 90F - 180F;
		GL11.glRotatef(rotY, 0F, 1F, 0F);

		float jump = potato.jumpTicks*0.5f;
		if(jump > 0)
			jump += partialTicks*0.5f;

		float up = (float) -Math.abs(Math.sin(jump / 10 * Math.PI)) * 0.2F;
		float rotZ = (float) Math.sin(jump / 10 * Math.PI) * 2;

		
		
		GL11.glTranslatef(0F, up, 0F);
		GL11.glRotatef(rotZ, 0F, 0F, 1F);

		mc.renderEngine.bindTexture(texture);
		model.render();

        GL11.glPushMatrix();
        String name = potato.name.toLowerCase();
        mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
        float scale = 1F / 4F;
        GL11.glTranslatef(0F, 1F, 0F);
        GL11.glScalef(scale, scale, scale);
        if(name.equals("armstrong")) {
            GL11.glScalef(1.75F, 1.75F, 1.25F);
            GL11.glRotatef(180F, 0F, 0F, 1F);
            GL11.glTranslatef(-0.5F, -0.55F, -0.8F);
            renderIcon(Tsundere.costumes.getIconFromDamage(0));
        }
        else if(name.startsWith("moo") && name.endsWith("oon")) {
            GL11.glScalef(2.5F, 2.5F, 1.25F);
            GL11.glRotatef(180F, 0F, 0F, 1F);
            GL11.glTranslatef(-0.5F, -0.6F, -0.8F);
            renderIcon(Tsundere.costumes.getIconFromDamage(1));
        }
        else if(name.equals("egbert")) {
            GL11.glScalef(1.25F, 1.25F, 1.25F);
            GL11.glRotatef(180F, 0F, 0F, 1F);
            GL11.glTranslatef(-0.5F, -1.4F, -0.8F);
            renderIcon(Tsundere.costumes.getIconFromDamage(2));
        }
        else if(name.equals("popetato")) {
            GL11.glScalef(1.75F, 1.75F, 1.25F);
            GL11.glRotatef(180F, 0F, 0F, 1F);
            GL11.glTranslatef(-0.5F, -0, -0.8F);
            renderIcon(Tsundere.costumes.getIconFromDamage(3));
        }
        else if(name.startsWith("foxtato")) {
        	InfiniteFoxes.renderInfinitatoFluff(partialTicks);
        }

        GL11.glPopMatrix();


		GL11.glRotatef(-rotZ, 0F, 0F, 1F);
		GL11.glRotatef(-rotY, 0F, 1F, 0F);
		GL11.glColor3f(1F, 1F, 1F);
		GL11.glScalef(1F, -1F, -1F);

        MovingObjectPosition pos = mc.objectMouseOver;
        if(!potato.name.isEmpty() && pos != null && pos.blockX == potato.xCoord && pos.blockY == potato.yCoord && pos.blockZ == potato.zCoord) {
			GL11.glPushMatrix();
			GL11.glTranslatef(0F, -0.4F, 0F);
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

    public void renderIcon(IIcon icon) {
        float f = icon.getMinU();
        float f1 = icon.getMaxU();
        float f2 = icon.getMinV();
        float f3 = icon.getMaxV();
        ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 16F);
    }
}
