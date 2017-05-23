package morph.avaritia.client.gui;

import codechicken.lib.math.MathHelper;
import morph.avaritia.container.ContainerNeutronCollector;
import morph.avaritia.tile.TileNeutronCollector;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly (Side.CLIENT)
public class GUINeutronCollector extends GuiMachineBase<TileNeutronCollector, ContainerNeutronCollector> {

    private static final ResourceLocation GUI_TEX = new ResourceLocation("avaritia", "textures/gui/neutron_collector_gui.png");

    public GUINeutronCollector(InventoryPlayer player, TileNeutronCollector machine) {
        super(new ContainerNeutronCollector(player, machine));
        setBackgroundTexture(GUI_TEX);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        String s = I18n.format("container.neutron_collector");
        float scaled_progress = scaleF(machineTile.getProgress(), TileNeutronCollector.PRODUCTION_TICKS, 100);
        String progress = "Progress: " + MathHelper.round(scaled_progress, 10) + "%";

        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 0x404040);
        this.fontRendererObj.drawString(progress, xSize / 2 - fontRendererObj.getStringWidth(progress) / 2, 60, 0x404040);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawBackground();
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }
}
