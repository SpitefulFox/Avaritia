package fox.spiteful.avaritia;

import fox.spiteful.avaritia.items.LudicrousItems;
import fox.spiteful.avaritia.render.FancyHaloRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {

	@Override
	public void makeThingsPretty() {
		FancyHaloRenderer shiny = new FancyHaloRenderer();
		
		MinecraftForgeClient.registerItemRenderer(LudicrousItems.resource, shiny);
	}
}
