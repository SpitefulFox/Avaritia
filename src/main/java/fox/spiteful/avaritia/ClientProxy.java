package fox.spiteful.avaritia;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import fox.spiteful.avaritia.compat.CompatClient;
import fox.spiteful.avaritia.entity.EntityEndestPearl;
import fox.spiteful.avaritia.entity.EntityGapingVoid;
import fox.spiteful.avaritia.entity.EntityHeavenArrow;
import fox.spiteful.avaritia.entity.EntityHeavenSubArrow;
import fox.spiteful.avaritia.items.LudicrousItems;
import fox.spiteful.avaritia.render.CosmicBowRenderer;
import fox.spiteful.avaritia.render.CosmicItemRenderer;
import fox.spiteful.avaritia.render.FancyHaloRenderer;
import fox.spiteful.avaritia.render.FracturedOreRenderer;
import fox.spiteful.avaritia.render.LudicrousRenderEvents;
//import fox.spiteful.avaritia.render.MorvInABoxRenderer;
import fox.spiteful.avaritia.render.RenderGapingVoid;
import fox.spiteful.avaritia.render.RenderHeavenArrow;
import fox.spiteful.avaritia.render.ShaderHelper;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

	public static final double toDeg = (1.0/(Math.PI*2))*360.0;
	
	@Override
	public void prepareForPretty() {
		CompatClient.earlyComprettify();
	}
	
	@Override
	public void makeThingsPretty() {
		FancyHaloRenderer shiny = new FancyHaloRenderer();
		
		MinecraftForgeClient.registerItemRenderer(LudicrousItems.resource, shiny);
		MinecraftForgeClient.registerItemRenderer(LudicrousItems.singularity, shiny);
		MinecraftForgeClient.registerItemRenderer(LudicrousItems.endest_pearl, shiny);
		
		RenderingRegistry.registerEntityRenderingHandler(EntityEndestPearl.class, new RenderSnowball(LudicrousItems.endest_pearl));
		RenderingRegistry.registerEntityRenderingHandler(EntityGapingVoid.class, new RenderGapingVoid());
		
		RenderHeavenArrow arrowrender = new RenderHeavenArrow();
		RenderingRegistry.registerEntityRenderingHandler(EntityHeavenArrow.class, arrowrender);
		RenderingRegistry.registerEntityRenderingHandler(EntityHeavenSubArrow.class, arrowrender);
		
		CosmicItemRenderer sparkly = new CosmicItemRenderer();
		
		MinecraftForgeClient.registerItemRenderer(LudicrousItems.infinity_sword, sparkly);
		MinecraftForgeClient.registerItemRenderer(LudicrousItems.infinity_helm, sparkly);
		MinecraftForgeClient.registerItemRenderer(LudicrousItems.infinity_armor, sparkly);
		MinecraftForgeClient.registerItemRenderer(LudicrousItems.infinity_pants, sparkly);
		MinecraftForgeClient.registerItemRenderer(LudicrousItems.infinity_shoes, sparkly);
		MinecraftForgeClient.registerItemRenderer(LudicrousItems.matter_cluster, sparkly);
		
		MinecraftForgeClient.registerItemRenderer(LudicrousItems.infinity_bow, new CosmicBowRenderer());
		
		MinecraftForgeClient.registerItemRenderer(LudicrousItems.fractured_ore, new FracturedOreRenderer());
		
		//MinecraftForgeClient.registerItemRenderer(LudicrousItems.morv_in_a_box, new MorvInABoxRenderer());
		
		CompatClient.comprettify();
		
		LudicrousRenderEvents fancyevents = new LudicrousRenderEvents();
		MinecraftForge.EVENT_BUS.register(fancyevents);
		FMLCommonHandler.instance().bus().register(fancyevents);
		
		ShaderHelper.initShaders();
	}
	
	@Override
	public void theAfterPretty() {
		CompatClient.lateComprettify();
	}
}
