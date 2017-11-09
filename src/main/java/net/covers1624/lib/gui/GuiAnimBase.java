package net.covers1624.lib.gui;

import codechicken.lib.texture.TextureUtils;
import codechicken.lib.vec.Rectangle4i;
import net.covers1624.lib.gui.DrawableGuiElement.AnimationDirection;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * Created by covers1624 on 21/05/2017.
 */
public abstract class GuiAnimBase extends GuiContainer {

    private Set<DrawableGuiElement> drawableElements = new HashSet<>();
    private ResourceLocation BACKGROUND_TEX;

    public GuiAnimBase(Container container) {
        super(container);
    }

    protected void setBackgroundTexture(ResourceLocation location) {
        BACKGROUND_TEX = location;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(-guiLeft, -guiTop, 0);
        for (DrawableGuiElement drawableGuiElement : drawableElements) {
            Point guiPos = getGuiPos();
            Rectangle4i bounds = drawableGuiElement.getBounds().offset(guiPos.x, guiPos.y);
            if (bounds.contains(mouseX, mouseY)) {
                drawableGuiElement.renderTooltip(new Point(mouseX, mouseY));
                break;
            }
        }
        GlStateManager.popMatrix();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Point guiPos = getGuiPos();
        GlStateManager.pushMatrix();
        GlStateManager.translate(guiPos.x, guiPos.y, 0);
        for (DrawableGuiElement drawableElement : drawableElements) {
            try {
                drawableElement.draw();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        GlStateManager.popMatrix();
    }

    protected void addDrawable(DrawableGuiElement element) {
        drawableElements.add(element);
    }

    protected DrawableBuilder drawableBuilder() {
        return new DrawableBuilder().setParent(this).setSpriteLocation(BACKGROUND_TEX).setAnimationDirection(AnimationDirection.STATIC);
    }

    protected Point getGuiPos() {
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        return new Point(x, y);
    }

    protected void drawBackground() {
        Point guiPos = getGuiPos();
        TextureUtils.changeTexture(BACKGROUND_TEX);
        drawTexturedModalRect(guiPos.x, guiPos.y, 0, 0, xSize, ySize);
    }

    protected static class DrawableBuilder {

        private Gui parent;
        private ResourceLocation spriteLocation;
        private Rectangle4i sprite;
        private Point location;
        private BooleanSupplier renderPredicate;
        private Supplier<Pair<Integer, Integer>> animationSupplier;
        private AnimationDirection animationDirection;
        private Supplier<String> tooltipSupplier;

        public DrawableBuilder() {
        }

        public DrawableBuilder setParent(Gui parent) {
            this.parent = parent;
            return this;
        }

        public DrawableBuilder setSpriteLocation(ResourceLocation spriteLocation) {
            this.spriteLocation = spriteLocation;
            return this;
        }

        public DrawableBuilder setSpriteSize(Rectangle4i sprite) {
            this.sprite = sprite;
            return this;
        }

        public DrawableBuilder setGuiLocation(Point location) {
            this.location = location;
            return this;
        }

        public DrawableBuilder setSpriteSize(int x, int y, int w, int h) {
            return setSpriteSize(new Rectangle4i(x, y, w, h));
        }

        public DrawableBuilder setGuiLocation(int x, int y) {
            return setGuiLocation(new Point(x, y));
        }

        public DrawableBuilder setAnimationSupplier(Supplier<Pair<Integer, Integer>> animationSupplier) {
            this.animationSupplier = animationSupplier;
            return this;
        }

        public DrawableBuilder setAnimationDirection(AnimationDirection animationDirection) {
            this.animationDirection = animationDirection;
            return this;
        }

        public DrawableBuilder setRenderPredicate(BooleanSupplier renderPredicate) {
            this.renderPredicate = renderPredicate;
            return this;
        }

        public DrawableBuilder setTooltipSupplier(Supplier<String> tooltipSupplier) {
            this.tooltipSupplier = tooltipSupplier;
            return this;
        }

        public DrawableGuiElement build() {
            return new DrawableGuiElement(parent, spriteLocation, sprite, location, animationDirection, animationSupplier, renderPredicate, tooltipSupplier);
        }
    }
}
