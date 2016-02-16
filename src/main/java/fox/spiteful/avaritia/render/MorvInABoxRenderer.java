package fox.spiteful.avaritia.render;

/*import java.util.Map;
import java.util.UUID;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.authlib.properties.Property;

import fox.spiteful.avaritia.Lumberjack;
import fox.spiteful.avaritia.items.ItemMorvInABox;
import fox.spiteful.avaritia.items.LudicrousItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.resources.SkinManager.SkinAvailableCallback;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;

public class MorvInABoxRenderer implements IItemRenderer {
	private static GameProfile morvy;// = new GameProfile(UUID.fromString(), "Morvelaira");
	private static ResourceLocation skin = null;

	public MorvInABoxRenderer() {
		NBTTagCompound morvytag = new NBTTagCompound();
		morvytag.setString("Id", "67b0b9d7-c003-4b12-8ee1-f233424a219c");
		morvy = new GameProfile(null, "Morvelaira");
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return helper == ItemRendererHelper.ENTITY_ROTATION || helper == ItemRendererHelper.ENTITY_BOBBING;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		this.getMorvy();
		Minecraft mc = Minecraft.getMinecraft();
		switch(type) {
		case ENTITY : {
			GL11.glPushMatrix();
			GL11.glTranslatef(-0.5F, 0F, 0F);
			if(item.isOnItemFrame())
				GL11.glTranslatef(0F, -0.3F, 0.01F);
			render(item, null);
			GL11.glPopMatrix();
			
			break;
		}
		case EQUIPPED : {
			render(item, data[1] instanceof EntityPlayer ? (EntityPlayer) data[1] : null);
			break;
		}
		case EQUIPPED_FIRST_PERSON : {
			render(item, data[1] instanceof EntityPlayer ? (EntityPlayer) data[1] : null);
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
			
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			
			r.renderWithColor = true;
			
			Tessellator t = Tessellator.instance;
			
	        if (skin != null) {
	        	GL11.glPushMatrix();
	        	GL11.glScaled(0.4, 0.4, 1.0);
	        	GL11.glTranslated(16, 10, 0);
	        	GL11.glRotated(20, 0, 0, 1);
	        	
	        	mc.getTextureManager().bindTexture(skin);
	        	
	        	float minu = 8/64.0f;
				float maxu = 16/64.0f;
				float minv = 8/32.0f;
				float maxv = 16/32.0f;
	        	
	        	t.startDrawingQuads();
				t.addVertexWithUV(0, 0, 0, minu, minv);
				t.addVertexWithUV(0, 16, 0, minu, maxv);
				t.addVertexWithUV(16, 16, 0, maxu, maxv);
				t.addVertexWithUV(16, 0, 0, maxu, minv);
				t.draw();
				
				minu = 40/64.0f;
				maxu = 48/64.0f;
				minv = 8/32.0f;
				maxv = 16/32.0f;
	        	
	        	t.startDrawingQuads();
				t.addVertexWithUV(-1, -1, 0, minu, minv);
				t.addVertexWithUV(-1, 17, 0, minu, maxv);
				t.addVertexWithUV(17, 17, 0, maxu, maxv);
				t.addVertexWithUV(17, -1, 0, maxu, minv);
				t.draw();
	        	
	        	mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
	        	
	        	GL11.glPopMatrix();
	        }
			
			IIcon icon = ((ItemMorvInABox)(LudicrousItems.morv_in_a_box)).nameoverlay;
			
			GL11.glColor4d(1, 1, 1, 1);
			
			float minu = icon.getMinU();
			float maxu = icon.getMaxU();
			float minv = icon.getMinV();
			float maxv = icon.getMaxV();
			
			
			
			t.startDrawingQuads();
			t.addVertexWithUV(0, 0, 0, minu, minv);
			t.addVertexWithUV(0, 16, 0, minu, maxv);
			t.addVertexWithUV(16, 16, 0, maxu, maxv);
			t.addVertexWithUV(16, 0, 0, maxu, minv);
			t.draw();
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
			break;
		}
		default : break;
		}
	}

	public void render(ItemStack item, EntityPlayer player) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		//ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), scale);

		IIcon icon = item.getItem().getIcon(item, 0);

		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
    	
        ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1.0f/16.0f);
        
        if (skin != null) {
        	Minecraft mc = Minecraft.getMinecraft();
        	GL11.glPushMatrix();
        	GL11.glScaled(0.4, 0.4, 1.0);
        	GL11.glTranslated(15.0/16.0, 10.0/16.0, 0.0001);
        	GL11.glRotated(20, 0, 0, 1);
        	
        	//GL11.glDisable(GL11.GL_DEPTH_TEST);
        	//GL11.glDepthFunc(GL11.GL_EQUAL);
        	
        	mc.getTextureManager().bindTexture(skin);
        	
        	f = 8/64.0f;
			f1 = 16/64.0f;
			f2 = 8/32.0f;
			f3 = 16/32.0f;
        	
			ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1.0025f/16.0f);
			
			f = 40/64.0f;
			f1 = 48/64.0f;
			f2 = 8/32.0f;
			f3 = 16/32.0f;
        	
			GL11.glTranslated(-.35/16.0, -.35/16.0, 0.0002);
			GL11.glScaled(17/16.0, 17/16.0, 1.0);
			
			
			ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1.006f/16.0f);
        	
        	mc.getTextureManager().bindTexture(TextureMap.locationItemsTexture);
        	
        	//GL11.glDepthFunc(GL11.GL_LEQUAL);
        	//GL11.glEnable(GL11.GL_DEPTH_TEST);
        	
        	GL11.glPopMatrix();
        }
        
        icon = ((ItemMorvInABox)(LudicrousItems.morv_in_a_box)).nameoverlay;

		f = icon.getMinU();
		f1 = icon.getMaxU();
		f2 = icon.getMinV();
		f3 = icon.getMaxV();
    	
		GL11.glPushMatrix();
		
		GL11.glTranslated(0, 0, 0.0004);
		
        ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1.008f/16.0f);
        
        GL11.glPopMatrix();
        
        GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();

		GL11.glColor4f(1F, 1F, 1F, 1F);
	}
	
	@SuppressWarnings("rawtypes")
	public void getMorvy() {
		if (!morvy.isComplete() || !morvy.getProperties().containsKey("textures"))
        {
            GameProfile gameprofile = null;

            //Which one is returning null?  Who knooooooooows
            if(MinecraftServer.getServer() != null && MinecraftServer.getServer().func_152358_ax() != null
                    && morvy != null)
                gameprofile = MinecraftServer.getServer().func_152358_ax().func_152655_a(morvy.getName());

            if (gameprofile != null)
            {
                Property property = (Property)Iterables.getFirst(gameprofile.getProperties().get("textures"), (Object)null);

                if (property == null)
                {
                    gameprofile = MinecraftServer.getServer().func_147130_as().fillProfileProperties(gameprofile, true);
                }
            }
            morvy = gameprofile;
        }
		
		if (skin == null) {
			//Lumberjack.info("2");
			Minecraft minecraft = Minecraft.getMinecraft();
            Map map = minecraft.func_152342_ad().func_152788_a(morvy);
			//Map map = minecraft.func_152342_ad().func_152788_a(Minecraft.getMinecraft().thePlayer.getGameProfile());
            //Lumberjack.info(map);
            
            if (map.containsKey(Type.SKIN))
            {
            	//Lumberjack.info("3");
                skin = minecraft.func_152342_ad().func_152792_a((MinecraftProfileTexture)map.get(Type.SKIN), Type.SKIN);
            }
		}
	}
}
*/