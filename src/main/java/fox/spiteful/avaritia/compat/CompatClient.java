package fox.spiteful.avaritia.compat;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import fox.spiteful.avaritia.Config;
import fox.spiteful.avaritia.compat.botania.RenderInfinitato;
import fox.spiteful.avaritia.compat.botania.RenderTileInfinitato;
import fox.spiteful.avaritia.compat.botania.TileInfinitato;
import fox.spiteful.avaritia.compat.ticon.TonkersClient;
import fox.spiteful.avaritia.items.LudicrousItems;
import fox.spiteful.avaritia.render.FancyHaloRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

public class CompatClient {

	public static void earlyComprettify() {
		
	}
	
	public static void comprettify() {
		if (Compat.botan) {
			RenderingRegistry.registerBlockHandler(new RenderInfinitato(RenderingRegistry.getNextAvailableRenderId()));
			ClientRegistry.bindTileEntitySpecialRenderer(TileInfinitato.class, new RenderTileInfinitato());
		}

		if (Compat.ticon) {
			TonkersClient.dunkThosePaintbrushes();
		}
	}
	
	public static void lateComprettify() {
		if(Compat.forestry && Config.bees){
            MinecraftForgeClient.registerItemRenderer(LudicrousItems.beesource, new FancyHaloRenderer());
        }
	}
}
