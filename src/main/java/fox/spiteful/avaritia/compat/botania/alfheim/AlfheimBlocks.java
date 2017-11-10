package fox.spiteful.avaritia.compat.botania.alfheim;

import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;
import fox.spiteful.avaritia.blocks.ItemBlockWithMetadataUnderp;

public class AlfheimBlocks {
	public static Block deadrock;
	
	public static void alfheimify() {
		deadrock = GameRegistry.registerBlock(new BlockDeadrock(), ItemBlockWithMetadataUnderp.class, "Alf_Deadrock");
	}
}
