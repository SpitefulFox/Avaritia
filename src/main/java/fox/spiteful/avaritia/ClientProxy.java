package fox.spiteful.avaritia;

import fox.spiteful.avaritia.blocks.LudicrousBlocks;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(Avaritia.matrixIngot, 0, new ModelResourceLocation("avaritia:matrix_ingot"));
	}


}
