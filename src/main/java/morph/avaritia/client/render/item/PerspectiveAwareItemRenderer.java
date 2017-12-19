package morph.avaritia.client.render.item;

import codechicken.lib.render.item.IItemRenderer;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.model.IModelState;

import javax.annotation.Nullable;

/**
 * Created by covers1624 on 13/04/2017.
 * TODO, 1.11 Move some implementation of this to CCL.
 */
public abstract class PerspectiveAwareItemRenderer implements IItemRenderer, IEntityItemTickCallback {

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
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
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
    public IModelState getTransforms() {
        return state;
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
