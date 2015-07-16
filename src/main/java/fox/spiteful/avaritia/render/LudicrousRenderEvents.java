package fox.spiteful.avaritia.render;

import java.nio.FloatBuffer;

import org.apache.logging.log4j.Level;
import org.lwjgl.BufferUtils;

import scala.actors.threadpool.Arrays;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fox.spiteful.avaritia.Lumberjack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

public class LudicrousRenderEvents {

	private static final int cosmicCount = 10;
	public static String[] cosmicTextures = new String[cosmicCount];
	
	static {
		for (int i=0; i<cosmicCount; i++) {
			cosmicTextures[i] = "avaritia:cosmic"+i;
		}
	}
	
	public static FloatBuffer cosmicUVs = null;
	public static IIcon[] cosmicIcons = new IIcon[cosmicTextures.length];
	
	@SubscribeEvent
	public void letsMakeAQuilt(TextureStitchEvent.Pre event) {
		if (event.map.getTextureType() == 0) { return; }
				
		for (int i=0; i<cosmicTextures.length; i++) {
			IIcon icon = event.map.registerIcon(cosmicTextures[i]);
			cosmicIcons[i] = icon;
		}
		
		//Lumberjack.log(Level.INFO, "COSMIC QUILTING: "+Arrays.toString(cosmicIcons));
	}
}
