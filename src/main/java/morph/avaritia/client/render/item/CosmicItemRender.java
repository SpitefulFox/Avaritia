package morph.avaritia.client.render.item;

import codechicken.lib.model.ItemQuadBakery;
import codechicken.lib.model.bakedmodels.ModelProperties;
import codechicken.lib.model.bakedmodels.PerspectiveAwareBakedModel;
import codechicken.lib.util.ResourceUtils;
import codechicken.lib.util.TransformUtils;
import com.google.common.collect.ImmutableList;
import morph.avaritia.api.ICosmicRenderItem;
import morph.avaritia.client.render.shader.CosmicShaderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.model.IModelState;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.List;

/**
 * Created by covers1624 on 13/04/2017.
 */
public class CosmicItemRender extends WrappedItemRenderer {

    private static final HashMap<TextureAtlasSprite, IBakedModel> spriteQuadCache = new HashMap<>();

    static {
        ResourceUtils.registerReloadListener(resourceManager -> spriteQuadCache.clear());
    }

    public CosmicItemRender(IModelState state, IBakedModel wrapped) {
        super(state, wrapped);

    }

    public CosmicItemRender(IModelState state, IWrappedModelGetter getter) {
        super(state, getter);
    }

    @Override
    public void renderItem(ItemStack item, TransformType transformType) {
        processLightLevel(transformType);
        if (transformType == TransformType.GUI) {
            renderInventory(item, renderEntity);
        } else {
            renderSimple(item, renderEntity);
        }
    }

    protected void renderSimple(ItemStack stack, EntityLivingBase player) {

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1F, 1F, 1F, 1F);

        World world = player != null ? player.world : null;
        IBakedModel model = wrapped.getOverrides().handleItemState(wrapped, stack, world, player);

        renderModel(model, stack);

        if (stack.getItem() instanceof ICosmicRenderItem) {
            ICosmicRenderItem cri = ((ICosmicRenderItem) stack.getItem());

            GlStateManager.disableAlpha();
            GlStateManager.depthFunc(GL11.GL_EQUAL);

            TextureAtlasSprite cosmicSprite = cri.getMaskTexture(stack, player);

            IBakedModel cosmicModel = spriteQuadCache.computeIfAbsent(cosmicSprite, CosmicItemRender::computeModel);

            CosmicShaderHelper.cosmicOpacity = cri.getMaskOpacity(stack, player);
            CosmicShaderHelper.useShader();

            renderModel(cosmicModel, stack);

            CosmicShaderHelper.releaseShader();

            GlStateManager.depthFunc(GL11.GL_LEQUAL);
            GlStateManager.enableAlpha();
        }
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    protected void renderInventory(ItemStack stack, EntityLivingBase player) {

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();

        World world = player != null ? player.world : null;
        IBakedModel model = wrapped.getOverrides().handleItemState(wrapped, stack, world, player);
        renderModel(model, stack);

        if (stack.getItem() instanceof ICosmicRenderItem) {
            ICosmicRenderItem cri = (ICosmicRenderItem) stack.getItem();

            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

            GlStateManager.disableAlpha();
            GlStateManager.disableDepth();

            TextureAtlasSprite sprite = cri.getMaskTexture(stack, player);
            IBakedModel cosmicModel = spriteQuadCache.computeIfAbsent(sprite, CosmicItemRender::computeModel);

            GlStateManager.color(1F, 1F, 1F, 1F);
            CosmicShaderHelper.cosmicOpacity = cri.getMaskOpacity(stack, player);
            CosmicShaderHelper.inventoryRender = true;
            CosmicShaderHelper.useShader();

            renderModel(cosmicModel, stack);

            CosmicShaderHelper.releaseShader();
            CosmicShaderHelper.inventoryRender = false;
            GlStateManager.popMatrix();
        }

        GlStateManager.enableAlpha();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableDepth();

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private static IBakedModel computeModel(TextureAtlasSprite sprite) {
        List<BakedQuad> quads = ItemQuadBakery.bakeItem(ImmutableList.of(sprite));
        return new PerspectiveAwareBakedModel(quads, TransformUtils.DEFAULT_ITEM, new ModelProperties(true, false));
    }

    protected void processLightLevel(TransformType transformType) {
        switch (transformType) {
            case GROUND:
                if (entityPos != null) {
                    CosmicShaderHelper.setLightFromLocation(world, entityPos);
                    return;
                }
                break;
            case THIRD_PERSON_LEFT_HAND:
            case THIRD_PERSON_RIGHT_HAND:
            case FIRST_PERSON_LEFT_HAND:
            case FIRST_PERSON_RIGHT_HAND:
            case HEAD:
                if (renderEntity != null) {
                    CosmicShaderHelper.setLightFromLocation(world, entityPos);
                    return;
                }
                break;
            case GUI:
                CosmicShaderHelper.setLightLevel(1.2F);
                return;
            default:
                break;
        }
        CosmicShaderHelper.setLightLevel(1.0F);
    }
}
