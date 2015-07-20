package fox.spiteful.avaritia;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import fox.spiteful.avaritia.compat.CompatClient;
import fox.spiteful.avaritia.entity.EntityEndestPearl;
import fox.spiteful.avaritia.entity.EntityGapingVoid;
import fox.spiteful.avaritia.items.LudicrousItems;
import fox.spiteful.avaritia.render.CosmicItemRenderer;
import fox.spiteful.avaritia.render.FancyHaloRenderer;
import fox.spiteful.avaritia.render.LudicrousRenderEvents;
import fox.spiteful.avaritia.render.RenderGapingVoid;
import fox.spiteful.avaritia.render.ShaderHelper;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

	public static final double toDeg = (1.0/(Math.PI*2))*360.0;
	
	@Override
	public void makeThingsPretty() {
		FancyHaloRenderer shiny = new FancyHaloRenderer();
		
		MinecraftForgeClient.registerItemRenderer(LudicrousItems.resource, shiny);
		MinecraftForgeClient.registerItemRenderer(LudicrousItems.singularity, shiny);
		MinecraftForgeClient.registerItemRenderer(LudicrousItems.endest_pearl, shiny);
		
		RenderingRegistry.registerEntityRenderingHandler(EntityEndestPearl.class, new RenderSnowball(LudicrousItems.endest_pearl));
		RenderingRegistry.registerEntityRenderingHandler(EntityGapingVoid.class, new RenderGapingVoid());
		
		CosmicItemRenderer sparkly = new CosmicItemRenderer();
		
		MinecraftForgeClient.registerItemRenderer(LudicrousItems.infinity_sword, sparkly);
		
		CompatClient.comprettify();
		
		LudicrousRenderEvents fancyevents = new LudicrousRenderEvents();
		MinecraftForge.EVENT_BUS.register(fancyevents);
		FMLCommonHandler.instance().bus().register(fancyevents);
		
		ShaderHelper.initShaders();
	}
}
