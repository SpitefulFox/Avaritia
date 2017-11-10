package fox.spiteful.avaritia.compat.botania;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderInfinitato implements ISimpleBlockRenderingHandler {
	public static int renderID;
	
	public RenderInfinitato(int id) {
		renderID = id;
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		GL11.glPushMatrix();
		GL11.glTranslatef(-0.5F, -0.35F, 0.5F);
		GL11.glRotated(90, 0, 1, 0);
		RenderTileInfinitato.drawHalo = false;
		TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileInfinitato(), 0.0D, 0.0D, 0.0D, 0.0F);
		RenderTileInfinitato.drawHalo = true;
		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return renderID;
	}

}
