package morph.avaritia.client.render.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class ModelRendererWing extends ModelRenderer {

    public ModelRendererWing(ModelBase model, String string) {
        super(model, string);
    }

    public ModelRendererWing(ModelBase model) {
        super(model);
    }

    public ModelRendererWing(ModelBase model, int x, int y) {
        super(model, x, y);
    }

    @Override
    public void render(float f) {
        if (!isHidden && showModel) {
            //GL11.glCullFace(GL11.GL_BACK);
            GlStateManager.enableCull();
        }
        super.render(f);
        if (!isHidden && showModel) {
            //GL11.glCullFace(GL11.GL_NONE);
            GlStateManager.disableCull();
        }
    }
}
