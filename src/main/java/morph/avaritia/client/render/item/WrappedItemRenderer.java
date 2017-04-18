package morph.avaritia.client.render.item;

import codechicken.lib.model.ModelRegistryHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.common.model.IModelState;

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

}
