package morph.avaritia.client.render.item;

import codechicken.lib.model.ModelRegistryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.common.model.IModelState;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by covers1624 on 16/04/2017.
 */
public abstract class WrappedItemRenderer extends PerspectiveAwareItemRenderer {

    protected IBakedModel wrapped;

    public WrappedItemRenderer(IModelState state, IBakedModel model) {
        super(state);
        wrapped = model;
    }

    public WrappedItemRenderer(IModelState state, IWrappedModelGetter getter) {
        super(state);
        ModelRegistryHelper.registerPreBakeCallback(modelRegistry -> wrapped = getter.getWrappedModel(modelRegistry));
    }

    public static interface IWrappedModelGetter {

        /**
         * A callback from the model load event to grab the wrapped model.
         *
         * @param modelRegistry Registry
         * @return The wrapped model
         */
        IBakedModel getWrappedModel(IRegistry<ModelResourceLocation, IBakedModel> modelRegistry);
    }

    /**
     * Renders a model basically the same as RenderItem does.
     *
     * @param model The model to render.
     * @param stack The stack being rendered. Used for quad tinting.
     */
    public static void renderModel(IBakedModel model, ItemStack stack) {
        renderModel(model, stack, 1.0F);
    }

    /**
     * Renders a model basically the same as RenderItem does, except allows overriding the alpha.
     *
     * @param model         The model to render.
     * @param stack         The stack being renderer. Used for quad tinting.
     * @param alphaOverride Th alpha override 1.0 -> 0.0
     */
    public static void renderModel(IBakedModel model, ItemStack stack, float alphaOverride) {

        ItemColors itemColorProvider = Minecraft.getMinecraft().getItemColors();
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();
        buffer.begin(0x07, DefaultVertexFormats.ITEM);
        List<BakedQuad> quads = new LinkedList<>();

        for (EnumFacing face : EnumFacing.VALUES) {
            quads.addAll(model.getQuads(null, face, 0));
        }
        quads.addAll(model.getQuads(null, null, 0));

        int alpha = (int) (alphaOverride * 255F) & 0xFF;
        for (BakedQuad quad : quads) {
            int colour = -1;

            if (quad.hasTintIndex()) {
                colour = itemColorProvider.colorMultiplier(stack, quad.getTintIndex());

                if (EntityRenderer.anaglyphEnable) {
                    colour = TextureUtil.anaglyphColor(colour);
                }
            }

            colour |= (alpha << 24);
            LightUtil.renderQuadColor(buffer, quad, colour);
        }

        tess.draw();
    }

}
