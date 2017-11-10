package fox.spiteful.avaritia.compat.botania.alfheim;

import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;

public class Alfheim {
	public static void vacationTime() {
		AlfheimBlocks.alfheimify();

        if(FMLCommonHandler.instance().getSide().isClient()) {
            AlfheimEvents alfevents = new AlfheimEvents();
            FMLCommonHandler.instance().bus().register(alfevents);
            MinecraftForge.EVENT_BUS.register(alfevents);
        }
	}
	
	// this stuff needs to happen whether or not botania is active it seems D:
    // DON'T LIE LIKE THAT, CUTS >:P
	public static void packYourBags() {
		int dim = WorldProviderAlfheim.dimensionID;
        DimensionManager.registerProviderType(dim, WorldProviderAlfheim.class, false);
        DimensionManager.registerDimension(dim, dim);
		
		MapGenStructureIO.registerStructure(MapGenCity.Start.class, "AlfCity");
		ComponentCityParts.registerParts();
	}
}
