package morph.avaritia.client.gui;

import codechicken.lib.texture.TextureUtils;
import morph.avaritia.container.ContainerNeutronCollector;
import morph.avaritia.tile.TileNeutronCollector;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly (Side.CLIENT)
public class GUINeutronCollector extends GuiContainer {

    private static final ResourceLocation GUI_TEX = new ResourceLocation("avaritia", "textures/gui/neutron_collector_gui.png");

    public GUINeutronCollector(InventoryPlayer player, TileNeutronCollector machine) {
        super(new ContainerNeutronCollector(player, machine));
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        String s = I18n.format("container.neutron_collector");
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        TextureUtils.changeTexture(GUI_TEX);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

    }
}
