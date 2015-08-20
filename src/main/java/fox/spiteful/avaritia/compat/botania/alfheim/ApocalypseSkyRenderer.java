package fox.spiteful.avaritia.compat.botania.alfheim;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ApocalypseSkyRenderer extends IRenderHandler {
	private static IModelCustom model;
	private static final ResourceLocation texture = new ResourceLocation("avaritia", "textures/models/sun.png");
	private static final ResourceLocation halo = new ResourceLocation("avaritia", "textures/entity/voidhalo.png");
	private static final ResourceLocation glow = new ResourceLocation("avaritia", "textures/items/halo128.png");

	public ApocalypseSkyRenderer() {
		if (model == null) {
			model = AdvancedModelLoader.loadModel(new ResourceLocation("avaritia", "model/sun.obj"));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        RenderHelper.disableStandardItemLighting();
        GL11.glDepthMask(false);
        
        Tessellator t = Tessellator.instance;
		GL11.glPushMatrix();
		
		GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotated(25, 1, 0, 0);
		
		GL11.glTranslated(0, 0, -350);
		GL11.glScaled(500, 500, 500);
		
		GL11.glRotated(90, -1, 0, 0);
		Minecraft.getMinecraft().renderEngine.bindTexture(glow);
		GL11.glColor4d(242/255.0, 61/255.0, 1/255.0, 0.3);
		double bound = 0.7;
		double depth = -0.4;
		t.startDrawingQuads();
		t.addVertexWithUV(-bound, depth, -bound, 0.0D, 0.0D);
        t.addVertexWithUV(bound, depth, -bound, 1.0D, 0.0D);
        t.addVertexWithUV(bound, depth, bound, 1.0D, 1.0D);
        t.addVertexWithUV(-bound, depth, bound, 0.0D, 1.0D);
        t.draw();
        
        Minecraft.getMinecraft().renderEngine.bindTexture(halo);
		GL11.glColor3d(242/255.0, 61/255.0, 1/255.0);
		bound = 0.562;
		depth = -0.2;
		t.startDrawingQuads();
		t.addVertexWithUV(-bound, depth, -bound, 0.0D, 0.0D);
        t.addVertexWithUV(bound, depth, -bound, 1.0D, 0.0D);
        t.addVertexWithUV(bound, depth, bound, 1.0D, 1.0D);
        t.addVertexWithUV(-bound, depth, bound, 0.0D, 1.0D);
        t.draw();
		
        GL11.glRotated(90, 1, 0, 0);
        GL11.glColor3d(1, 1, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		model.renderAll();

		GL11.glPopMatrix();
		
		GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
	}

}
