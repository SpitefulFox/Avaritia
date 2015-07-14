package fox.spiteful.avaritia.compat;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import fox.spiteful.avaritia.compat.botania.RenderInfinitato;
import fox.spiteful.avaritia.compat.botania.RenderTileInfinitato;
import fox.spiteful.avaritia.compat.botania.TileInfinitato;

public class CompatClient {

	public static void comprettify() {
		if (Compat.botan) {
			RenderingRegistry.registerBlockHandler(new RenderInfinitato(RenderingRegistry.getNextAvailableRenderId()));
			ClientRegistry.bindTileEntitySpecialRenderer(TileInfinitato.class, new RenderTileInfinitato());
		}
	}
}
