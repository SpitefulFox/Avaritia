package fox.spiteful.avaritia.gui;

import fox.spiteful.avaritia.tile.TileEntityAutoDireCrafting;
import fox.spiteful.avaritia.tile.TileEntityDireCrafting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class GUIAutoExtremeCrafting extends GuiContainer {

    private static final ResourceLocation tex = new ResourceLocation("avaritia:textures/gui/dire_autocrafting_gui.png");

    public GUIAutoExtremeCrafting(InventoryPlayer par1InventoryPlayer, World par2World, int x, int y, int z, TileEntityAutoDireCrafting table)
    {
        super(new ContainerAutoExtremeCrafting(par1InventoryPlayer, par2World, x, y, z, table));
        this.ySize = 256;
        this.xSize = 238;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int i, int j)
    {
        //this.fontRendererObj.drawString(StatCollector.translateToLocal("container.extreme_crafting"), 28, 6, 4210752);
        //this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        /*this.mc.renderEngine.bindTexture(tex);
        int foo = (this.width - this.xSize) / 2;
        int bar = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(foo, bar, 0, 0, this.ySize, this.ySize);*/
        mc.getTextureManager().bindTexture(tex);
        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(guiLeft, guiTop, 0, 0.0, 0.0);
        tessellator.addVertexWithUV(guiLeft, guiTop + xSize, 0, 0.0, 1.0);
        tessellator.addVertexWithUV(guiLeft + xSize, guiTop + xSize, 0, 1.0, 1.0);
        tessellator.addVertexWithUV(guiLeft + xSize, guiTop, 0, 1.0, 0.0);
        tessellator.draw();
    }
}
