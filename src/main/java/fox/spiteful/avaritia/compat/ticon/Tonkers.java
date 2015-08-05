package fox.spiteful.avaritia.compat.ticon;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import fox.spiteful.avaritia.items.LudicrousItems;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.tools.ToolMaterial;

public class Tonkers {

	public static ToolMaterial neutronium;
	public static ToolMaterial infinityMetal;
	public static final int neutroniumId = 500;
	public static final int infinityMetalId = 501;
	public static final String neutroniumName = "avaritia_neutronium";
	public static final String infinityMetalName = "avaritia_infinitymetal";
	
	public static void buildstruct() {
		
		neutronium = new ToolMaterial(neutroniumName, "material."+neutroniumName, 5, 2400, 900, 6, 2.5f, 0, 0.0f, EnumChatFormatting.DARK_GRAY.toString(), 0x303030);
		infinityMetal = new ToolMaterial(infinityMetalName, "material."+infinityMetalName, 5, 10000, 2000, 50, 10.0f, 5, 0.0f, LudicrousItems.cosmic.rarityColor.toString(), 0xFFFFFF);

		TConstructRegistry.addtoolMaterial(neutroniumId, neutronium);
		TConstructRegistry.addtoolMaterial(infinityMetalId, infinityMetal);
		
		TConstructRegistry.addDefaultToolPartMaterial(neutroniumId);
		TConstructRegistry.addDefaultToolPartMaterial(infinityMetalId);
		
		TConstructRegistry.addBowMaterial(neutroniumId, 50, 5.0f);
		TConstructRegistry.addBowMaterial(infinityMetalId, 30, 10.0f);
		
		TConstructRegistry.addArrowMaterial(neutroniumId, 5.0f, 0.0f);
		TConstructRegistry.addArrowMaterial(infinityMetalId, 2.0f, 0.0f);
		
		TonkersEvents events = new TonkersEvents();
		MinecraftForge.EVENT_BUS.register(events);
        FMLCommonHandler.instance().bus().register(events);
	}
}
