package morph.avaritia.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderHeavenArrow extends RenderArrow<EntityArrow> {

    private static final ResourceLocation tex = new ResourceLocation("avaritia", "textures/entity/heavenarrow.png");

    public RenderHeavenArrow(RenderManager manager) {
        super(manager);
    }

    @Override
    public void doRender(EntityArrow entityArrow, double x, double y, double z, float entityYaw, float partialTicks) {
        EntityRenderer er = Minecraft.getMinecraft().entityRenderer;
        er.disableLightmap();
        GlStateManager.disableLighting();

        bindEntityTexture(entityArrow);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.rotate(entityArrow.prevRotationYaw + (entityArrow.rotationYaw - entityArrow.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entityArrow.prevRotationPitch + (entityArrow.rotationPitch - entityArrow.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();

        byte b0 = 0;
        float f2 = 0.0F;
        float f3 = 0.5F;
        float f4 = (float) (b0 * 10) / 32.0F;
        float f5 = (float) (5 + b0 * 10) / 32.0F;
        float f6 = 0.0F;
        float f7 = 0.15625F;
        float f8 = (float) (5 + b0 * 10) / 32.0F;
        float f9 = (float) (10 + b0 * 10) / 32.0F;
        float f10 = 0.05625F;
        GlStateManager.enableRescaleNormal();
        float f11 = (float) entityArrow.arrowShake - partialTicks;

        if (f11 > 0.0F) {
            float f12 = -MathHelper.sin(f11 * 3.0F) * f11;
            GlStateManager.rotate(f12, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(f10, f10, f10);
        GlStateManager.translate(-4.0F, 0.0F, 0.0F);

        buffer.begin(0x07, DefaultVertexFormats.POSITION_TEX_NORMAL);
        buffer.pos(-7.0D, -2.0D, -2.0D).tex(f6, f8).normal(f10, 0.0F, 0.0F).endVertex();
        buffer.pos(-7.0D, -2.0D, 2.0D).tex(f7, f8).normal(f10, 0.0F, 0.0F).endVertex();
        buffer.pos(-7.0D, 2.0D, 2.0D).tex(f7, f9).normal(f10, 0.0F, 0.0F).endVertex();
        buffer.pos(-7.0D, 2.0D, -2.0D).tex(f6, f9).normal(f10, 0.0F, 0.0F).endVertex();
        tess.draw();

        buffer.begin(0x07, DefaultVertexFormats.POSITION_TEX_NORMAL);
        buffer.pos(-7.0D, 2.0D, -2.0D).tex(f6, f8).normal(-f10, 0.0F, 0.0F).endVertex();
        buffer.pos(-7.0D, 2.0D, 2.0D).tex(f7, f8).normal(-f10, 0.0F, 0.0F).endVertex();
        buffer.pos(-7.0D, -2.0D, 2.0D).tex(f7, f9).normal(-f10, 0.0F, 0.0F).endVertex();
        buffer.pos(-7.0D, -2.0D, -2.0D).tex(f6, f9).normal(-f10, 0.0F, 0.0F).endVertex();
        tess.draw();

        for (int i = 0; i < 4; ++i) {

            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);

            buffer.begin(0x07, DefaultVertexFormats.POSITION_TEX_NORMAL);
            buffer.pos(-8.0D, -2.0D, 0.0D).tex(f2, f4).normal(0.0F, 0.0F, f10).endVertex();
            buffer.pos(8.0D, -2.0D, 0.0D).tex(f3, f4).normal(0.0F, 0.0F, f10).endVertex();
            buffer.pos(8.0D, 2.0D, 0.0D).tex(f3, f5).normal(0.0F, 0.0F, f10).endVertex();
            buffer.pos(-8.0D, 2.0D, 0.0D).tex(f2, f5).normal(0.0F, 0.0F, f10).endVertex();
            tess.draw();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

        GlStateManager.enableLighting();
        er.enableLightmap();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityArrow entity) {
        return tex;
    }
}
