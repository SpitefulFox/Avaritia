package fox.spiteful.avaritia.render;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.ReflectionHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.IItemRenderer;

public class CosmicItemRenderer implements IItemRenderer {

	private final ShaderCallback shaderCallback;
	private float[] lightlevel = new float[3];
	
	private String[] lightmapobf = new String[] {"lightmapColors", "field_78504_Q", "U"};
	private static boolean inventoryRender = false;
	
	public CosmicItemRenderer() {
		shaderCallback = new ShaderCallback() {
			@Override
			public void call(int shader) {
				Minecraft mc = Minecraft.getMinecraft();

				float yaw = 0;
				float pitch = 0;
				float scale = 1.0f;
				
				if (!inventoryRender) {
					yaw = (float)((mc.thePlayer.rotationYaw * 2 * Math.PI) / 360.0);
					pitch = - (float)((mc.thePlayer.rotationPitch * 2 * Math.PI) / 360.0);
				} else {
					scale = 25.0f;
				}
				
				int x = ARBShaderObjects.glGetUniformLocationARB(shader, "yaw");
				ARBShaderObjects.glUniform1fARB(x, yaw);
				
				int z = ARBShaderObjects.glGetUniformLocationARB(shader, "pitch");
				ARBShaderObjects.glUniform1fARB(z, pitch);
				
				int l = ARBShaderObjects.glGetUniformLocationARB(shader, "lightlevel");
				ARBShaderObjects.glUniform3fARB(l, lightlevel[0], lightlevel[1], lightlevel[2]);
				
				int lightmix = ARBShaderObjects.glGetUniformLocationARB(shader, "lightmix");
				ARBShaderObjects.glUniform1fARB(lightmix, 0.2f);
				
				int uvs = ARBShaderObjects.glGetUniformLocationARB(shader, "cosmicuvs");
				ARBShaderObjects.glUniformMatrix2ARB(uvs, false, LudicrousRenderEvents.cosmicUVs);
				
				int s = ARBShaderObjects.glGetUniformLocationARB(shader, "externalScale");
				ARBShaderObjects.glUniform1fARB(s, scale);
			}
		};
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		//return type != ItemRenderType.INVENTORY;
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return helper == ItemRendererHelper.ENTITY_ROTATION || helper == ItemRendererHelper.ENTITY_BOBBING;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		Minecraft mc = Minecraft.getMinecraft();
		this.processLightLevel(type, item, data);
		//ShaderHelper.useShader(ShaderHelper.testShader, this.shaderCallback);
		switch(type) {
		case ENTITY : {
			GL11.glPushMatrix();
			GL11.glTranslatef(-0.5F, 0F, 0F);
			if(item.isOnItemFrame())
				GL11.glTranslatef(0F, -0.3F, 0.01F);
			render(item);
			GL11.glPopMatrix();
			
			break;
		}
		case EQUIPPED : {
			render(item);
			break;
		}
		case EQUIPPED_FIRST_PERSON : {
			render(item);
			break;
		}
		case INVENTORY: {
			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			RenderHelper.enableGUIStandardItemLighting();
			
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			
			RenderItem r = RenderItem.getInstance();
			r.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), item, 0, 0, true);
			
			if (item.getItem() instanceof ICosmicRenderItem) {
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				RenderHelper.enableGUIStandardItemLighting();
				
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				
				inventoryRender = true;
				ShaderHelper.useShader(ShaderHelper.testShader, this.shaderCallback);
				ICosmicRenderItem icri = (ICosmicRenderItem)(item.getItem());
				IIcon cosmicicon = icri.getMaskTexture(item);
				
				GL11.glColor4d(1, 1, 1, 1);
				
				float minu = cosmicicon.getMinU();
				float maxu = cosmicicon.getMaxU();
				float minv = cosmicicon.getMinV();
				float maxv = cosmicicon.getMaxV();
				
				Tessellator t = Tessellator.instance;
				
				t.startDrawingQuads();
				t.addVertexWithUV(0, 0, 0, minu, minv);
				t.addVertexWithUV(0, 16, 0, minu, maxv);
				t.addVertexWithUV(16, 16, 0, maxu, maxv);
				t.addVertexWithUV(16, 0, 0, maxu, minv);
				t.draw();
				
				ShaderHelper.releaseShader();
				inventoryRender = false;
			}
			
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			
			r.renderWithColor = true;
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
			break;
		}
		default : break;
		}
		//ShaderHelper.releaseShader();
		
		//Lumberjack.log(Level.INFO, light+"");
	}

	public void render(ItemStack item) {
		int passes = 1;
		if (item.getItem().requiresMultipleRenderPasses())
        {
			passes = item.getItem().getRenderPasses(item.getItemDamage());
        }
		
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		//ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), scale);

		float r,g,b;
		IIcon icon;
		float f,f1,f2,f3;
		float scale = 1F / 16F;

		//Lumberjack.log(Level.INFO, "passes: "+passes);
		
        for (int i = 0; i < passes; i++)
        {
        	icon = item.getItem().getIcon(item, i);
        	
        	//Lumberjack.log(Level.INFO, "icon "+i+": "+icon);

    		f = icon.getMinU();
    		f1 = icon.getMaxU();
    		f2 = icon.getMinV();
    		f3 = icon.getMaxV();
        	
            int colour = item.getItem().getColorFromItemStack(item, i);
            r = (float)(colour >> 16 & 255) / 255.0F;
            g = (float)(colour >> 8 & 255) / 255.0F;
            b = (float)(colour & 255) / 255.0F;
            GL11.glColor4f(r, g, b, 1.0F);
            ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), scale);
        }
		
		if (item.getItem() instanceof ICosmicRenderItem) {
			ShaderHelper.useShader(ShaderHelper.testShader, this.shaderCallback);
			ICosmicRenderItem icri = (ICosmicRenderItem)(item.getItem());
			IIcon cosmicicon = icri.getMaskTexture(item);
			
			float minu = cosmicicon.getMinU();
			float maxu = cosmicicon.getMaxU();
			float minv = cosmicicon.getMinV();
			float maxv = cosmicicon.getMaxV();
			ItemRenderer.renderItemIn2D(Tessellator.instance, maxu, minv, minu, maxv, cosmicicon.getIconWidth(), cosmicicon.getIconHeight(), scale);
			ShaderHelper.releaseShader();
		}
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();

		GL11.glColor4f(1F, 1F, 1F, 1F);
	}

	public void processLightLevel(ItemRenderType type, ItemStack item, Object... data) {
		int lightcoord = 0;
		
		switch(type) {
		case ENTITY : {
			EntityItem ent = (EntityItem)(data[1]);
			if (ent != null) {
				lightcoord = ent.worldObj.getLightBrightnessForSkyBlocks(MathHelper.floor_double(ent.posX), MathHelper.floor_double(ent.posY), MathHelper.floor_double(ent.posZ), 0);
			}
			break;
		}
		case EQUIPPED : {
			EntityLivingBase ent = (EntityLivingBase)(data[1]);
			if (ent != null) {
				lightcoord = ent.worldObj.getLightBrightnessForSkyBlocks(MathHelper.floor_double(ent.posX), MathHelper.floor_double(ent.posY), MathHelper.floor_double(ent.posZ), 0);
			}
			break;
		}
		case EQUIPPED_FIRST_PERSON : {
			EntityLivingBase ent = (EntityLivingBase)(data[1]);
			if (ent != null) {
				lightcoord = ent.worldObj.getLightBrightnessForSkyBlocks(MathHelper.floor_double(ent.posX), MathHelper.floor_double(ent.posY), MathHelper.floor_double(ent.posZ), 0);
			}
			break;
		}
		case INVENTORY: {
			lightcoord = 0;
			this.lightlevel[0] = 1.2f;
	        this.lightlevel[1] = 1.2f;
	        this.lightlevel[2] = 1.2f;
			return;
		}
		default : {
			lightcoord = 0;
			this.lightlevel[0] = 1.0f;
	        this.lightlevel[1] = 1.0f;
	        this.lightlevel[2] = 1.0f;
			return;
		}
		}
		
		int x = (lightcoord % 65536) / 16;
        int y = (lightcoord / 65536) / 16;
        
        int[] lightmap = ReflectionHelper.getPrivateValue(EntityRenderer.class, Minecraft.getMinecraft().entityRenderer, this.lightmapobf);
        
        int lightcolour = lightmap[y*16+x];
        
        this.lightlevel[0] = ((lightcolour >> 16) & 0xFF) / 256.0f;
        this.lightlevel[1] = ((lightcolour >> 8) & 0xFF) / 256.0f;
        this.lightlevel[2] = ((lightcolour) & 0xFF) / 256.0f;

	}
}
