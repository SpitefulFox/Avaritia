package fox.spiteful.avaritia.render;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.world.World;

import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.ARBShaderObjects;

import cpw.mods.fml.relauncher.ReflectionHelper;
import fox.spiteful.avaritia.Lumberjack;

public class CosmicRenderShenanigans {
	public static final ShaderCallback shaderCallback;
	
	public static float[] lightlevel = new float[3];
	
	public static String[] lightmapobf = new String[] {"lightmapColors", "field_78504_Q", "U"};
	public static boolean inventoryRender = false;
	public static float cosmicOpacity = 1.0f;
	
	static {
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
				
				int o = ARBShaderObjects.glGetUniformLocationARB(shader, "opacity");
				ARBShaderObjects.glUniform1fARB(o, cosmicOpacity);
			}
		};
	}
	
	public static void useShader() {
		ShaderHelper.useShader(ShaderHelper.cosmicShader, shaderCallback);
	}
	public static void releaseShader() {
		ShaderHelper.releaseShader();
	}
	
	private static Field mapfield = ReflectionHelper.findField(EntityRenderer.class, lightmapobf);
	public static void setLightFromLocation(World world, int x, int y, int z) {
		if(world == null){
            setLightLevel(1.0f);
            return;
        }

        int coord = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
		
		int[] map = null;
		try {
			map = (int[]) mapfield.get(Minecraft.getMinecraft().entityRenderer);
		} catch (Exception e) {
			Lumberjack.log(Level.ERROR, e, "Failure to get light map");
		}
		if (map == null) {
			setLightLevel(1.0f);
			return;
		}
		
		int mx = (coord % 65536) / 16;
        int my = (coord / 65536) / 16;
        
        int lightcolour = map[my*16+mx];
        
        setLightLevel(
    		((lightcolour >> 16) & 0xFF) / 256.0f,
    		((lightcolour >> 8) & 0xFF) / 256.0f,
    		((lightcolour) & 0xFF) / 256.0f
        );
	}
	
	public static void setLightLevel(float level) {
		setLightLevel(level, level, level);
	}
	public static void setLightLevel(float r, float g, float b) {
		lightlevel[0] = Math.max(0.0f, Math.min(1.0f, r));
		lightlevel[1] = Math.max(0.0f, Math.min(1.0f, g));
		lightlevel[2] = Math.max(0.0f, Math.min(1.0f, b));
	}
	
	public static void bindItemTexture() {
		Minecraft mc = Minecraft.getMinecraft();
		mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
	}
}
