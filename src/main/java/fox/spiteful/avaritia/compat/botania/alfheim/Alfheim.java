package fox.spiteful.avaritia.compat.botania.alfheim;

import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;

public class Alfheim {
	public static void vacationTime() {
		AlfheimBlocks.alfheimify();
		
		AlfheimEvents alfevents = new AlfheimEvents();
        FMLCommonHandler.instance().bus().register(alfevents);
    	MinecraftForge.EVENT_BUS.register(alfevents);
	}
	
	// this stuff needs to happen whether or not botania is active it seems D:
	public static void packYourBags() {
		int dim = WorldProviderAlfheim.dimensionID;
        DimensionManager.registerProviderType(dim, WorldProviderAlfheim.class, false);
        DimensionManager.registerDimension(dim, dim);
		
		MapGenStructureIO.registerStructure(MapGenCity.Start.class, "AlfCity");
		//MapGenStructureIO.func_143031_a(ATGComponentRiverSection.class, "ATGRiverSection");
	}
}
