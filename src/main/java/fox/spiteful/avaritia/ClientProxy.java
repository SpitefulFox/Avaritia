package fox.spiteful.avaritia;

import cpw.mods.fml.common.FMLCommonHandler;
import fox.spiteful.avaritia.compat.CompatClient;
import fox.spiteful.avaritia.items.LudicrousItems;
import fox.spiteful.avaritia.render.CosmicItemRenderer;
import fox.spiteful.avaritia.render.FancyHaloRenderer;
import fox.spiteful.avaritia.render.LudicrousRenderEvents;
import fox.spiteful.avaritia.render.ShaderHelper;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

	@Override
	public void makeThingsPretty() {
		FancyHaloRenderer shiny = new FancyHaloRenderer();
		
		MinecraftForgeClient.registerItemRenderer(LudicrousItems.resource, shiny);
		MinecraftForgeClient.registerItemRenderer(LudicrousItems.singularity, shiny);
		
		CosmicItemRenderer sparkly = new CosmicItemRenderer();
		
		MinecraftForgeClient.registerItemRenderer(LudicrousItems.infinity_sword, sparkly);
		
		CompatClient.comprettify();
		
		LudicrousRenderEvents fancyevents = new LudicrousRenderEvents();
		MinecraftForge.EVENT_BUS.register(fancyevents);
		FMLCommonHandler.instance().bus().register(fancyevents);
		
		ShaderHelper.initShaders();
	}
}
