package fox.spiteful.avaritia.gui;

import fox.spiteful.avaritia.tile.TileEntityCompressor;
import fox.spiteful.avaritia.tile.TileEntityNeutron;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class GUICompressor extends GuiContainer
{
    private static final ResourceLocation furnaceGuiTextures = new ResourceLocation("avaritia", "textures/gui/compressor.png");
    private TileEntityCompressor compressor;

    public GUICompressor(InventoryPlayer player, TileEntityCompressor machine)
    {
        super(new ContainerCompressor(player, machine));
        compressor = machine;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
    {
        String s = StatCollector.translateToLocal("container.neutronium_compressor");
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        if(compressor.getProgress() > 0) {
            s = compressor.getProgress() + " / " + compressor.getTarget();
            this.fontRendererObj.drawString(s, 41, 49, 4210752);
            this.fontRendererObj.drawString(compressor.getIngredient(), 41, 60, 4210752);
        }
        this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(furnaceGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        if(compressor.getProgress() > 0){
            int i1 = compressor.getProgress() * 24 / compressor.getTarget();
            this.drawTexturedModalRect(k + 79, l + 26, 176, 14, i1 + 1, 16);
        }

    }
}