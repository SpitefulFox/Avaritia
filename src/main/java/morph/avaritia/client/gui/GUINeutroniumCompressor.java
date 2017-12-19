package morph.avaritia.client.gui;

import morph.avaritia.container.ContainerNeutroniumCompressor;
import morph.avaritia.tile.TileNeutroniumCompressor;
import net.covers1624.lib.gui.DrawableGuiElement.AnimationDirection;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

public class GUINeutroniumCompressor extends GuiMachineBase<TileNeutroniumCompressor, ContainerNeutroniumCompressor> {

    private static final ResourceLocation GUI_TEX = new ResourceLocation("avaritia", "textures/gui/compressor.png");

    public GUINeutroniumCompressor(InventoryPlayer player, TileNeutroniumCompressor machine) {
        super(new ContainerNeutroniumCompressor(player, machine));
        setBackgroundTexture(GUI_TEX);

        DrawableBuilder builder = drawableBuilder();
        builder.setGuiLocation(62, 35).setSpriteSize(176, 0, 22, 16);
        builder.setAnimationDirection(AnimationDirection.LEFT_RIGHT);
        builder.setRenderPredicate(() -> machineTile.getConsumptionProgress() > 0);
        builder.setAnimationSupplier(() -> Pair.of(kick(machineTile.getConsumptionProgress()), machineTile.getConsumptionTarget()));
        addDrawable(builder.build());

        builder = drawableBuilder();
        builder.setGuiLocation(90, 35).setSpriteSize(176, 16, 16, 16);
        builder.setAnimationDirection(AnimationDirection.BOTTOM_UP);
        builder.setRenderPredicate(() -> machineTile.getCompressionProgress() > 0);
        builder.setAnimationSupplier(() -> Pair.of(kick(machineTile.getCompressionProgress()), machineTile.getCompressionTarget()));
        builder.setTooltipSupplier(() -> String.format("%.2f%%", scaleF(machineTile.getCompressionProgress(), machine.getCompressionTarget(), 100)));
        addDrawable(builder.build());
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = I18n.format("container.neutronium_compressor");
        fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 0x404040);
        fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 0x404040);
        if (machineTile.getCompressionProgress() > 0) {
            s = machineTile.getCompressionProgress() + " / " + machineTile.getCompressionTarget();
            int x = xSize / 2 - fontRenderer.getStringWidth(s) / 2;
            fontRenderer.drawString(s, x, 60, 0x404040);
        }
        if (!machineTile.getTargetStack().isEmpty()) {
            s = "Output";
            int x = (xSize + 147 - 8) / 2 - fontRenderer.getStringWidth(s) / 2;
            fontRenderer.drawString(s, x, 25, 0x404040);
        }
        if (!machineTile.getTargetStack().isEmpty()) {
            s = "Input";
            if (machineTile.getInputItems() != null && machineTile.getInputItems().size() > 1) {
                s += "s";
            }
            int x = 20 - fontRenderer.getStringWidth(s) / 2;
            fontRenderer.drawString(s, x, 25, 0x404040);
        }

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    private int kick(int num) {
        if (num > 0) {
            return num + 1;
        }
        return num;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawBackground();
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }
}
