package morph.avaritia.client.gui;

import codechicken.lib.texture.TextureUtils;
import morph.avaritia.container.ContainerNeutroniumCompressor;
import morph.avaritia.tile.TileNeutroniumCompressor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GUINeutroniumCompressor extends GuiContainer {

    private static final ResourceLocation GUI_TEX = new ResourceLocation("avaritia", "textures/gui/compressor.png");
    private TileNeutroniumCompressor compressor;

    public GUINeutroniumCompressor(InventoryPlayer player, TileNeutroniumCompressor machine) {
        super(new ContainerNeutroniumCompressor(player, machine));
        compressor = machine;
    }

    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        String s = I18n.format("container.neutronium_compressor");
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        if (compressor.getProgress() > 0) {
            s = compressor.getProgress() + " / " + compressor.getTarget();
            this.fontRendererObj.drawString(s, 41, 49, 4210752);
            this.fontRendererObj.drawString(compressor.getIngredient(), 41, 60, 4210752);
        }
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        TextureUtils.changeTexture(GUI_TEX);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        if (compressor.getProgress() > 0) {
            int i1 = compressor.getProgress() * 24 / compressor.getTarget();
            this.drawTexturedModalRect(k + 79, l + 26, 176, 14, i1 + 1, 16);
        }

    }
}
