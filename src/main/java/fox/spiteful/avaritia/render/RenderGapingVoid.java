package fox.spiteful.avaritia.render;

import org.lwjgl.opengl.GL11;

import fox.spiteful.avaritia.ClientProxy;
import fox.spiteful.avaritia.Lumberjack;
import fox.spiteful.avaritia.entity.EntityGapingVoid;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class RenderGapingVoid extends Render {

	private ResourceLocation fill = new ResourceLocation("avaritia", "textures/entity/void.png");
	private ResourceLocation halo = new ResourceLocation("avaritia", "textures/entity/voidhalo.png");
	private IModelCustom model;
	
	public RenderGapingVoid() {
		this.model = AdvancedModelLoader.loadModel(new ResourceLocation("avaritia", "model/hemisphere.obj"));
	}
	
	@Override
	public void doRender(Entity ent, double x, double y, double z, float p_76986_8_, float partialTicks) {
		EntityGapingVoid egv = (EntityGapingVoid)ent;
		Minecraft mc =Minecraft.getMinecraft(); 
		mc.renderEngine.bindTexture(this.halo);
		Tessellator tess = Tessellator.instance;
		
		double age = egv.getAge() + partialTicks;
		
		setColour(age, 1.0);
		
		double scale = EntityGapingVoid.getVoidScale(age);
		
		double fullfadedist = 0.6 * scale;
		double fadedist = fullfadedist + 1.5;
		
		double halocoord = 0.58 * scale;
		double haloscaledist = 2.2 * scale;
		
		double dx = ent.posX - RenderManager.renderPosX;
		double dy = ent.posY - RenderManager.renderPosY;
		double dz = ent.posZ - RenderManager.renderPosZ;
		
		double xzlen = Math.sqrt(dx*dx + dz*dz);
		double len = Math.sqrt(dx*dx + dy*dy + dz*dz);
		
		if (len <= haloscaledist) {
			double close = (haloscaledist-len)/haloscaledist;
			halocoord *= 1.0 + close*close*close*close*1.5;
		}
		
		double yang = Math.atan2(xzlen, dy) * ClientProxy.toDeg;
		double xang = Math.atan2(dx, dz) * ClientProxy.toDeg;

		//Lumberjack.info("dx: "+dx+", dy: "+dy+", dz: "+dz+", xang: "+xang);
		//Lumberjack.info("x: "+x+", y: "+y+", z: "+z);
		
		GL11.glDisable(GL11.GL_LIGHTING);
		mc.entityRenderer.disableLightmap(0.0);
		
		GL11.glPushMatrix();
		{
			GL11.glTranslated(x, y, z);
			
			GL11.glRotated(xang, 0, 1, 0);
			GL11.glRotated(yang+90, 1, 0, 0);
			
			GL11.glPushMatrix();
			{			
				GL11.glRotated(90, 1, 0, 0);
		
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glDepthMask(false);
				
				tess.startDrawingQuads();
				tess.addVertexWithUV(-halocoord,0.0,-halocoord, 0.0, 0.0);
				tess.addVertexWithUV(-halocoord,0.0, halocoord, 0.0, 1.0);
				tess.addVertexWithUV( halocoord,0.0, halocoord, 1.0, 1.0);
				tess.addVertexWithUV( halocoord,0.0,-halocoord, 1.0, 0.0);
				tess.draw();
				
				GL11.glDepthMask(true);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			}
			GL11.glPopMatrix();
			
			Minecraft.getMinecraft().renderEngine.bindTexture(this.fill);
			
			GL11.glScaled(scale, scale, scale);
			
			model.renderAll();
			
			
		}
		GL11.glPopMatrix();
		
		if (len <= fadedist) {
			double alpha = 1.0;
			if (len >= fullfadedist) {
				alpha = 1.0 - ((len - fullfadedist) / (fadedist - fullfadedist));
				alpha = alpha*alpha*(3 - 2*alpha);
			}
			setColour(age, alpha);
			GL11.glPushMatrix();
			{
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_BLEND);
				
				GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
				
		        double d = 0;
		        
		        tess.startDrawingQuads();
				tess.addVertexWithUV(-100, 100, d, 0.0, 0.0);
				tess.addVertexWithUV(-100,-100, d, 0.0, 1.0);
				tess.addVertexWithUV( 100,-100, d, 1.0, 1.0);
				tess.addVertexWithUV( 100, 100, d, 1.0, 0.0);
				tess.draw();
		        
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
			}
			GL11.glPopMatrix();
		}
		
		mc.entityRenderer.enableLightmap(0.0);
		GL11.glEnable(GL11.GL_LIGHTING);
		
		GL11.glColor4d(1, 1, 1, 1);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity ent) {
		return this.fill;
	}

	private void setColour(double age, double alpha) {
		double life = (age / (double)EntityGapingVoid.maxLifetime);
		double f = Math.max(0, (life - EntityGapingVoid.collapse)/(1-EntityGapingVoid.collapse));
		f = Math.max(f, 1-(life*30));
		GL11.glColor4d(f, f, f, alpha);
	}
}
