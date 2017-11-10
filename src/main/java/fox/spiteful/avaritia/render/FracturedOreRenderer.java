package fox.spiteful.avaritia.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import fox.spiteful.avaritia.items.ItemFracturedOre;
import fox.spiteful.avaritia.items.ItemFracturedOre.NameStack;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

public class FracturedOreRenderer implements IItemRenderer {

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
			RenderHelper.enableGUIStandardItemLighting();
			
			//GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			RenderItem r = RenderItem.getInstance();
			r.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), item, 0, 0, true);
			
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			
			boolean unknown = false;
			
			if (item.hasTagCompound() && item.getTagCompound().hasKey(ItemFracturedOre.OREKEY)) {
				ItemStack orestack = NameStack.loadFromNBT(item.getTagCompound().getCompoundTag(ItemFracturedOre.OREKEY)).getStack();
				
				mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
				
				Item oreitem = orestack.getItem();
				if (oreitem instanceof ItemBlock) {
					/*ItemBlock ib = (ItemBlock)oreitem;
					Block b = ib.field_150939_a;
					IIcon oreicon = b.getIcon(0, orestack.getItemDamage());
					
					float minu = oreicon.getMinU();
					float maxu = oreicon.getMaxU();
					float minv = oreicon.getMinV();
					float maxv = oreicon.getMaxV();
	
					Tessellator t = Tessellator.instance;
					
					t.startDrawingQuads();
					t.addVertexWithUV(0, 0, 0, minu, minv);
					t.addVertexWithUV(0, 16, 0, minu, maxv);
					t.addVertexWithUV(16, 16, 0, maxu, maxv);
					t.addVertexWithUV(16, 0, 0, maxu, minv);
					
					GL11.glDepthFunc(GL11.GL_EQUAL);
					t.draw();
					GL11.glDepthFunc(GL11.GL_LEQUAL);*/
					
					GL11.glDisable(GL11.GL_LIGHTING);
					GL11.glPushMatrix();
					
					RenderItem ri = RenderItem.getInstance();
					
					GL11.glTranslated(33.125, 10, 32);
					GL11.glScaled(16, 16, 16);
					GL11.glRotatef(-90.0F, 0.0F, -1.0F, 0.0F);
		            GL11.glRotatef(45.0F, 0.0F, -1.0F, 0.0F);
		            GL11.glRotatef(210.0F, -1.0F, 0.0F, 0.0F);
					GL11.glScalef(1.0F, 1.0F, -1.0F);
		            GL11.glTranslatef(-1.0F, -0.5F, -1.0F);
		            GL11.glScalef(0.1F, 0.1F, 0.1F);
		            GL11.glRotated(180, 1, 0, 0);
		            
		            GL11.glTranslated(2, -3, 3);
		            
		            
		            GL11.glDepthFunc(GL11.GL_GEQUAL);
					ri.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), orestack, 0, 0);
					GL11.glDepthFunc(GL11.GL_LEQUAL);
					
					GL11.glPopMatrix();
					GL11.glEnable(GL11.GL_LIGHTING);
					
					mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
				} else {
					unknown = true;
				}
			} else {
				unknown = true;
			}
			
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			GL11.glColor4d(1, 1, 1, 0.2);
			
			IIcon fracicon = item.getItem().getIcon(item, 0);

			float minu = fracicon.getMinU();
			float maxu = fracicon.getMaxU();
			float minv = fracicon.getMinV();
			float maxv = fracicon.getMaxV();

			Tessellator t = Tessellator.instance;
			
			t.startDrawingQuads();
			t.addVertexWithUV(0, 0, 0, minu, minv);
			t.addVertexWithUV(0, 16, 0, minu, maxv);
			t.addVertexWithUV(16, 16, 0, maxu, maxv);
			t.addVertexWithUV(16, 0, 0, maxu, minv);
			
			t.draw();
			
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4d(1, 1, 1, 1);
			
			t.startDrawingQuads();
			t.addVertexWithUV(0, 0, 0, minu, minv);
			t.addVertexWithUV(0, 16, 0, minu, maxv);
			t.addVertexWithUV(16, 16, 0, maxu, maxv);
			t.addVertexWithUV(16, 0, 0, maxu, minv);
			
			t.draw();
			
			GL11.glDisable(GL11.GL_BLEND);
			
			if (unknown) {
				IIcon uicon = ItemFracturedOre.unknownIcon;
				
				minu = uicon.getMinU();
				maxu = uicon.getMaxU();
				minv = uicon.getMinV();
				maxv = uicon.getMaxV();
				
				t.startDrawingQuads();
				t.addVertexWithUV(0, 0, 0, minu, minv);
				t.addVertexWithUV(0, 16, 0, minu, maxv);
				t.addVertexWithUV(16, 16, 0, maxu, maxv);
				t.addVertexWithUV(16, 0, 0, maxu, minv);
				
				t.draw();
			}
			
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			
			r.renderWithColor = true;
			
			
			GL11.glPopMatrix();
			break;
		}
		default : break;
		}
	}

	public void render(ItemStack item) {
		Minecraft mc = Minecraft.getMinecraft();
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);

		IIcon icon;
		float f,f1,f2,f3;
		float scale = 1F / 16F;

		GL11.glColor4f(1F, 1F, 1F, 1F);
    	icon = item.getItem().getIcon(item, 0);

		f = icon.getMinU();
		f1 = icon.getMaxU();
		f2 = icon.getMinV();
		f3 = icon.getMaxV();
    	
        ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), scale);
        
        boolean unknown = false;
        
        if (item.hasTagCompound() && item.getTagCompound().hasKey(ItemFracturedOre.OREKEY)) {
        	ItemStack orestack = NameStack.loadFromNBT(item.getTagCompound().getCompoundTag(ItemFracturedOre.OREKEY)).getStack();
			
			mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
			
			Item oreitem = orestack.getItem();
			if (oreitem instanceof ItemBlock) {				
				ItemBlock ib = (ItemBlock)oreitem;
				Block b = ib.field_150939_a;
				
				if (b instanceof ITileEntityProvider) {
					unknown = true;
				}
				
				IIcon oreicon = b.getIcon(0, orestack.getItemDamage());
				//IIcon oreicon = item.getIconIndex();
				
				float minu = oreicon.getMinU();
				float maxu = oreicon.getMaxU();
				float minv = oreicon.getMinV();
				float maxv = oreicon.getMaxV();

				
				GL11.glDepthFunc(GL11.GL_EQUAL);
				ItemRenderer.renderItemIn2D(Tessellator.instance, maxu, minv, minu, maxv, icon.getIconWidth(), icon.getIconHeight(), scale);
				GL11.glDepthFunc(GL11.GL_LEQUAL);
				
				mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
			}
		} else {
			unknown = true;
		}
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glColor4d(1, 1, 1, 0.2);
		
		IIcon fracicon = item.getItem().getIcon(item, 0);

		float minu = fracicon.getMinU();
		float maxu = fracicon.getMaxU();
		float minv = fracicon.getMinV();
		float maxv = fracicon.getMaxV();
		
		ItemRenderer.renderItemIn2D(Tessellator.instance, maxu, minv, minu, maxv, icon.getIconWidth(), icon.getIconHeight(), scale);
		
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4d(1, 1, 1, 1);
		mc.entityRenderer.disableLightmap(0.0);

		ItemRenderer.renderItemIn2D(Tessellator.instance, maxu, minv, minu, maxv, icon.getIconWidth(), icon.getIconHeight(), scale);
		
		GL11.glDisable(GL11.GL_BLEND);

		if (!CosmicRenderShenanigans.inventoryRender) {
			mc.entityRenderer.enableLightmap(0.0);
			GL11.glEnable(GL11.GL_LIGHTING);
		}
		
		if (unknown) {
			IIcon uicon = ItemFracturedOre.unknownIcon;
			
			minu = uicon.getMinU();
			maxu = uicon.getMaxU();
			minv = uicon.getMinV();
			maxv = uicon.getMaxV();
			
			ItemRenderer.renderItemIn2D(Tessellator.instance, maxu, minv, minu, maxv, uicon.getIconWidth(), uicon.getIconHeight(), scale);
		}
		
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		GL11.glPopMatrix();

		GL11.glColor4f(1F, 1F, 1F, 1F);
		
	}
}
