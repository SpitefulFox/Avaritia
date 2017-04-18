package morph.avaritia.client.render.item;

import codechicken.lib.render.item.IItemRenderer;
import codechicken.lib.texture.TextureUtils;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.IModelState;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by covers1624 on 13/04/2017.
 * TODO, 1.11 Move some implementation of this to CCL.
 */
public abstract class PerspectiveAwareItemRenderer implements IItemRenderer, IPerspectiveAwareModel, IEntityItemTickCallback {

    @Nonnull//If this is null, someone has seriously fucked with item rendering..
    protected TransformType transformType;

    //This can be null if rendered in a gui, ALWAYS null check this.
    //Havent traced all routes, but only hand rendering may fire this..
    @Nullable
    protected EntityLivingBase renderEntity;

    protected World world;

    protected BlockPos entityPos;

    private final IModelState state;

    protected PerspectiveAwareItemRenderer(IModelState state) {
        this.state = state;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return new ArrayList<>();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return TextureUtils.getMissingSprite();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return new EntityCachingOverrideList((entity, world) -> {
            renderEntity = entity;
            this.world = world;
            if (entity != null) {
                entityPos = entity.getPosition();
            }
        });
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
        this.transformType = cameraTransformType;
        return MapWrapper.handlePerspective(this, state, cameraTransformType);
    }

    @Override
    public void onEntityTick(EntityItem item) {
        entityPos = item.getPosition();
    }

    //This is so we can sniff the entity rendering the item.
    //I use "entity rendering the item" in a loose context.
    //The entity has no control over such rendering,
    //This simply represents the entity "holding" the item as such.
    private static class EntityCachingOverrideList extends ItemOverrideList {

        private IEntityCallback callback;

        public EntityCachingOverrideList(IEntityCallback callback) {
            super(ImmutableList.of());
            this.callback = callback;
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
            callback.onEntityStuffs(entity, world);
            return super.handleItemState(originalModel, stack, world, entity);
        }
    }

    private interface IEntityCallback {

        void onEntityStuffs(EntityLivingBase entity, World world);
    }
}
