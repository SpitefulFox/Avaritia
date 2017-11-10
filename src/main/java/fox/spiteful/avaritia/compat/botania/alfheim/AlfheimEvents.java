package fox.spiteful.avaritia.compat.botania.alfheim;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.logging.log4j.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.Timer;
import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.ReflectionHelper;
import fox.spiteful.avaritia.FieldHelper;
import fox.spiteful.avaritia.Lumberjack;

public class AlfheimEvents {
	public static Field lightmapUpdateNeededField;
	public static Field mcTimerField;
	public static Field torchFlickerField;
	public static Field lightmapColorsField;
	public static Field lightmapTextureField;
	public static Field bossColorModifierField;
	public static Field bossColorModifierPrevField;
	public static Method getNightVisionBrightnessMethod = ReflectionHelper.findMethod(EntityRenderer.class, Minecraft.getMinecraft().entityRenderer, new String[]{"getNightVisionBrightness", "func_82830_a"}, EntityPlayer.class, float.class);
	
	static {
		try {
			mcTimerField = ReflectionHelper.findField(Minecraft.class, "timer", "field_71428_T");
			lightmapUpdateNeededField = ReflectionHelper.findField(EntityRenderer.class, "lightmapUpdateNeeded", "field_78536_aa");
			torchFlickerField = ReflectionHelper.findField(EntityRenderer.class, "torchFlickerX", "field_78514_e");
			lightmapColorsField = ReflectionHelper.findField(EntityRenderer.class, "lightmapColors", "field_78504_Q");
			lightmapTextureField = ReflectionHelper.findField(EntityRenderer.class, "lightmapTexture", "field_78513_d");
			bossColorModifierField = ReflectionHelper.findField(EntityRenderer.class, "bossColorModifier", "field_82831_U");
			bossColorModifierPrevField = ReflectionHelper.findField(EntityRenderer.class, "bossColorModifierPrev", "field_82832_V");
			//getNightVisionBrightnessMethod = ReflectionHelper.findMethod(EntityRenderer.class, Minecraft.getMinecraft().entityRenderer, new String[]{"getNightVisionBrightness", "func_82830_a"}, Float.class);
		} catch(Exception e) {
			Lumberjack.log(Level.ERROR, e);
		}
	}
	
	@SubscribeEvent
	public void onRenderStart(TickEvent.RenderTickEvent event) {
		if (event.phase == Phase.START) {
			Minecraft mc = Minecraft.getMinecraft();
			
			if (mc.theWorld == null) {
				return;
			}
			World world = mc.theWorld;
			if (world.provider == null || !(world.provider instanceof WorldProviderAlfheim)) {
				return;
			}
			EntityRenderer er = mc.entityRenderer;

			boolean needsupdate = FieldHelper.get(lightmapUpdateNeededField, er);
			
			if (needsupdate) {
				updateLightmapRedGiant();
			}
		}
	}
	
	public void updateLightmapRedGiant() {
		//Lumberjack.info("Lightmap update");
		Minecraft mc = Minecraft.getMinecraft();
		EntityRenderer er = mc.entityRenderer;
		Timer timer = FieldHelper.get(mcTimerField, mc);
		float partialTicks = timer.renderPartialTicks;
		float flicker = FieldHelper.get(torchFlickerField, er);
		float bossColorModifier = FieldHelper.get(bossColorModifierField, er);
		float bossColorModifierPrev = FieldHelper.get(bossColorModifierPrevField, er);
		int[] lightmapColors = FieldHelper.get(lightmapColorsField, er);
		DynamicTexture lightmapTexture = FieldHelper.get(lightmapTextureField, er);
		
		WorldClient worldclient = mc.theWorld;

        if (worldclient != null)
        {
            for (int i = 0; i < 256; ++i)
            {
                float brightness = worldclient.getSunBrightness(1.0F) * 0.95F + 0.05F;
                float sun = worldclient.provider.lightBrightnessTable[i / 16] * brightness;
                float torch = worldclient.provider.lightBrightnessTable[i % 16] * (flicker * 0.1F + 1.5F);

                if (worldclient.lastLightningBolt > 0)
                {
                    sun = worldclient.provider.lightBrightnessTable[i / 16];
                }

                float sunr = sun * (worldclient.getSunBrightness(1.0F) * 0.65F + 0.35F);
                float sung = sun * (worldclient.getSunBrightness(1.0F) * 0.65F + 0.35F);
                float torchg = torch * ((torch * 0.6F + 0.4F) * 0.6F + 0.4F);
                float torchb = torch * (torch * torch * 0.6F + 0.4F);
                float fr = sunr + torch;
                float fg = sung * 0.3f + torchg;
                float fb = sun * 0.2f + torchb;
                fr = fr * 0.96F + 0.03F;
                fg = fg * 0.96F + 0.03F;
                fb = fb * 0.96F + 0.03F;
                float modifier;

                if (bossColorModifier > 0.0F)
                {
                    modifier = bossColorModifierPrev + (bossColorModifier - bossColorModifierPrev) * partialTicks;
                    fr = fr * (1.0F - modifier) + fr * 0.7F * modifier;
                    fg = fg * (1.0F - modifier) + fg * 0.6F * modifier;
                    fb = fb * (1.0F - modifier) + fb * 0.6F * modifier;
                }

                if (worldclient.provider.dimensionId == 1)
                {
                    fr = 0.22F + torch * 0.75F;
                    fg = 0.28F + torchg * 0.75F;
                    fb = 0.25F + torchb * 0.75F;
                }

                if (mc.thePlayer.isPotionActive(Potion.nightVision))
                {
                	modifier = FieldHelper.invoke(getNightVisionBrightnessMethod, er, mc.thePlayer, partialTicks);
                    
                	float nightvision = 1.0F / fr;

                    if (nightvision > 1.0F / fg)
                    {
                        nightvision = 1.0F / fg;
                    }

                    if (nightvision > 1.0F / fb)
                    {
                        nightvision = 1.0F / fb;
                    }

                    fr = fr * (1.0F - modifier) + fr * nightvision * modifier;
                    fg = fg * (1.0F - modifier) + fg * nightvision * modifier;
                    fb = fb * (1.0F - modifier) + fb * nightvision * modifier;
                }

                if (fr > 1.0F) { fr = 1.0F; }
                if (fg > 1.0F) { fg = 1.0F; }
                if (fb > 1.0F) { fb = 1.0F; }

                modifier = mc.gameSettings.gammaSetting;
                float gr = 1.0F - fr;
                float gg = 1.0F - fg + 0.25f*sun;
                float gb = 1.0F - fb + 0.2f*sun;
                gr = 1.0F - gr * gr * gr * gr;
                gg = 1.0F - gg * gg * gg * gg;
                gb = 1.0F - gb * gb * gb * gb;
                fr = fr * (1.0F - modifier) + gr * modifier;
                fg = fg * (1.0F - modifier) + gg * modifier;
                fb = fb * (1.0F - modifier) + gb * modifier;
                fr = fr * 0.96F + 0.03F;
                fg = fg * 0.96F + 0.03F;
                fb = fb * 0.96F + 0.03F;

                if (fr > 1.0F) { fr = 1.0F; }
                if (fg > 1.0F) { fg = 1.0F; }
                if (fb > 1.0F) { fb = 1.0F; }

                if (fr < 0.0F) { fr = 0.0F; }
                if (fg < 0.0F) { fg = 0.0F; }
                if (fb < 0.0F) { fb = 0.0F; }

                short alpha = 255;
                int r = (int)(fr * 255.0F);
                int g = (int)(fg * 255.0F);
                int b = (int)(fb * 255.0F);
                lightmapColors[i] = alpha << 24 | r << 16 | g << 8 | b;
            }

            lightmapTexture.updateDynamicTexture();
            FieldHelper.set(lightmapUpdateNeededField, er, false);
        }
	}
}
