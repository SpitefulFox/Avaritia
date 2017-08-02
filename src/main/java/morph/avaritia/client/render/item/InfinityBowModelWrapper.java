package morph.avaritia.client.render.item;

import codechicken.lib.math.MathHelper;
import codechicken.lib.model.bakery.CCBakeryModel;
import codechicken.lib.util.ItemNBTUtils;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by covers1624 on 17/04/2017.
 */
public class InfinityBowModelWrapper extends CCBakeryModel {

    private final ItemOverrideList superList = super.getOverrides();
    private final ItemOverrideList wrappedList = new WrappedBowOverrideList();

    public InfinityBowModelWrapper() {
        super();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return wrappedList;
    }

    private class WrappedBowOverrideList extends ItemOverrideList {

        public WrappedBowOverrideList() {
            super(ImmutableList.of());
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
            if (entity != null) {
                stack = stack.copy();
                ItemNBTUtils.setInteger(stack, "frame", getBowFrame(entity));
            }
            return superList.handleItemState(originalModel, stack, world, entity);
        }

    }

    public static int getBowFrame(EntityLivingBase entity) {
        ItemStack inuse = entity.getActiveItemStack();
        int time = entity.getItemInUseCount();

        if (!inuse.isEmpty()) {
            int max = inuse.getMaxItemUseDuration();
            double pull = (max - time) / (double) max;
            int frame = Math.max(0, (int) Math.ceil(pull * 3.0) - 1);
            frame = MathHelper.clip(frame, 0, 2);
            return frame;
        }
        return -1;
    }
}
