package fox.spiteful.avaritia.compat;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import fox.spiteful.avaritia.compat.tsundere.RenderInfinitato;
import fox.spiteful.avaritia.compat.tsundere.RenderTileInfinitato;
import fox.spiteful.avaritia.compat.tsundere.TileInfinitato;

public class CompatClient {

	public static void comprettify() {
		if (Compat.botan) {
			RenderingRegistry.registerBlockHandler(new RenderInfinitato(RenderingRegistry.getNextAvailableRenderId()));
			ClientRegistry.bindTileEntitySpecialRenderer(TileInfinitato.class, new RenderTileInfinitato());
		}
	}
}
