package fox.spiteful.avaritia.render;

import java.nio.FloatBuffer;

import net.minecraftforge.common.model.Models;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import fox.spiteful.avaritia.Lumberjack;
import fox.spiteful.avaritia.items.ItemArmorInfinity;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.TextureStitchEvent;

public class LudicrousRenderEvents {

	private static final int cosmicCount = 10;
	public static String[] cosmicTextures = new String[cosmicCount];
	
	static {
		for (int i=0; i<cosmicCount; i++) {
			cosmicTextures[i] = "avaritia:cosmic"+i;
		}
	}
	
	public static FloatBuffer cosmicUVs = BufferUtils.createFloatBuffer(4 * cosmicTextures.length);
	public static Models[] cosmicIcons = new Models[cosmicTextures.length];
	
	@SubscribeEvent
	public void letsMakeAQuilt(TextureStitchEvent.Pre event) {
		if (event.map.getTextureType() != 1) { return; }
				
		for (int i=0; i<cosmicTextures.length; i++) {
			Models icon = event.map.registerIcon(cosmicTextures[i]);
			cosmicIcons[i] = icon;
		}
		
		ModelArmorInfinity.overlayIcon = event.map.registerIcon("avaritia:infinity_armor_mask");
		ModelArmorInfinity.invulnOverlayIcon = event.map.registerIcon("avaritia:infinity_armor_mask2");
		ModelArmorInfinity.wingOverlayIcon = event.map.registerIcon("avaritia:infinity_armor_wingmask");
	}
	
	@SubscribeEvent
	public void weMadeAQuilt(TextureStitchEvent.Post event) {
		if (event.map.getTextureType() != 1) { return; }
		
		CosmicRenderShenanigans.bindItemTexture();
		ModelArmorInfinity.itempagewidth = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
		ModelArmorInfinity.itempageheight = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
		
		ModelArmorInfinity.armorModel.rebuildOverlay();
		ModelArmorInfinity.legModel.rebuildOverlay();
	}
	
	@SubscribeEvent
	public void pushTheCosmicFancinessToTheLimit(TickEvent.RenderTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			cosmicUVs = BufferUtils.createFloatBuffer(4 * cosmicIcons.length);
			Models icon;
			for (int i=0; i<cosmicIcons.length; i++) {
				icon = cosmicIcons[i];

				cosmicUVs.put(icon.getMinU());
				cosmicUVs.put(icon.getMinV());
				cosmicUVs.put(icon.getMaxU());
				cosmicUVs.put(icon.getMaxV());
			}
			cosmicUVs.flip();
		}
	}
	
	@SubscribeEvent
	public void makeCosmicStuffLessDumbInGUIs(GuiScreenEvent.DrawScreenEvent.Pre event) {
		CosmicRenderShenanigans.inventoryRender = true;
	}
	
	@SubscribeEvent
	public void finishMakingCosmicStuffLessDumbInGUIs(GuiScreenEvent.DrawScreenEvent.Post event) {
		CosmicRenderShenanigans.inventoryRender = false;
	}
}
