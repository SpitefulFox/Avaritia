package morph.avaritia.client.render.entity;

import codechicken.lib.math.MathHelper;
import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.OBJParser;
import codechicken.lib.texture.TextureUtils;
import morph.avaritia.entity.EntityGapingVoid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class RenderGapingVoid extends Render<EntityGapingVoid> {

    private ResourceLocation fill = new ResourceLocation("avaritia", "textures/entity/void.png");
    private ResourceLocation halo = new ResourceLocation("avaritia", "textures/entity/voidhalo.png");

    private CCModel model;

    public RenderGapingVoid(RenderManager manager) {
        super(manager);
        model = OBJParser.parseModels(new ResourceLocation("avaritia", "model/hemisphere.obj")).get("model");
    }

    @Override
    public void doRender(EntityGapingVoid ent, double x, double y, double z, float entityYaw, float partialTicks) {

        Minecraft mc = Minecraft.getMinecraft();
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();
        CCRenderState ccrs = CCRenderState.instance();
        TextureUtils.changeTexture(halo);

        double age = ent.getAge() + partialTicks;

        setColour(age, 1.0);

        double scale = EntityGapingVoid.getVoidScale(age);

        double fullfadedist = 0.6 * scale;
        double fadedist = fullfadedist + 1.5;

        double halocoord = 0.58 * scale;
        double haloscaledist = 2.2 * scale;

        double dx = ent.posX - renderManager.renderPosX;
        double dy = ent.posY - renderManager.renderPosY;
        double dz = ent.posZ - renderManager.renderPosZ;

        double xzlen = Math.sqrt(dx * dx + dz * dz);
        double len = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (len <= haloscaledist) {
            double close = (haloscaledist - len) / haloscaledist;
            halocoord *= 1.0 + close * close * close * close * 1.5;
        }

        double yang = Math.atan2(xzlen, dy) * MathHelper.todeg;
        double xang = Math.atan2(dx, dz) * MathHelper.todeg;

        //Lumberjack.info("dx: "+dx+", dy: "+dy+", dz: "+dz+", xang: "+xang);
        //Lumberjack.info("x: "+x+", y: "+y+", z: "+z);

        GlStateManager.disableLighting();
        mc.entityRenderer.disableLightmap();

        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(x, y, z);

            GlStateManager.rotate((float) xang, 0, 1, 0);
            GlStateManager.rotate((float) (yang + 90), 1, 0, 0);

            GlStateManager.pushMatrix();
            {
                GlStateManager.rotate(90, 1, 0, 0);

                GlStateManager.disableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.depthMask(false);

                buffer.begin(0x07, DefaultVertexFormats.POSITION_TEX);
                buffer.pos(-halocoord, 0.0, -halocoord).tex(0.0, 0.0).endVertex();
                buffer.pos(-halocoord, 0.0, halocoord).tex(0.0, 1.0).endVertex();
                buffer.pos(halocoord, 0.0, halocoord).tex(1.0, 1.0).endVertex();
                buffer.pos(halocoord, 0.0, -halocoord).tex(1.0, 0.0).endVertex();
                tess.draw();

                GlStateManager.depthMask(true);
                GlStateManager.disableBlend();
                GlStateManager.enableAlpha();
            }
            GlStateManager.popMatrix();

            TextureUtils.changeTexture(fill);

            GlStateManager.scale(scale, scale, scale);

            GlStateManager.disableCull();
            ccrs.startDrawing(0x07, DefaultVertexFormats.POSITION_TEX_NORMAL);
            model.render(ccrs);
            ccrs.draw();
            GlStateManager.enableCull();

        }
        GlStateManager.popMatrix();

        if (len <= fadedist) {
            double alpha = 1.0;
            if (len >= fullfadedist) {
                alpha = 1.0 - ((len - fullfadedist) / (fadedist - fullfadedist));
                alpha = alpha * alpha * (3 - 2 * alpha);
            }
            setColour(age, alpha);
            GlStateManager.pushMatrix();
            {
                GlStateManager.disableAlpha();
                GlStateManager.enableBlend();

                GlStateManager.rotate(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

                double d = 0;

                buffer.begin(0x07, DefaultVertexFormats.POSITION_TEX);
                buffer.pos(-100, 100, d).tex(0.0, 0.0).endVertex();
                buffer.pos(-100, -100, d).tex(0.0, 1.0).endVertex();
                buffer.pos(100, -100, d).tex(1.0, 1.0).endVertex();
                buffer.pos(100, 100, d).tex(1.0, 0.0).endVertex();
                tess.draw();

                GlStateManager.disableBlend();
                GlStateManager.enableAlpha();
            }
            GlStateManager.popMatrix();
        }

        mc.entityRenderer.enableLightmap();
        GlStateManager.enableLighting();

        GlStateManager.color(1, 1, 1, 1);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityGapingVoid ent) {
        return fill;
    }

    private void setColour(double age, double alpha) {
        double life = (age / EntityGapingVoid.maxLifetime);
        double f = Math.max(0, (life - EntityGapingVoid.collapse) / (1 - EntityGapingVoid.collapse));
        f = Math.max(f, 1 - (life * 30));
        GlStateManager.color((float) f, (float) f, (float) f, (float) alpha);
    }
}
