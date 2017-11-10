package fox.spiteful.avaritia.render;

import org.lwjgl.opengl.GL11;

import fox.spiteful.avaritia.Lumberjack;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelRendererWing extends ModelRenderer {

	public ModelRendererWing(ModelBase model, String string) {
		super(model, string);
	}

	public ModelRendererWing(ModelBase model) {
		super(model);
	}

	public ModelRendererWing(ModelBase model, int x, int y) {
		super(model,x,y);
	}

	@Override
	public void render(float f) {
		/*if (!this.isHidden && this.showModel) {
			//GL11.glCullFace(GL11.GL_BACK);
			GL11.glEnable(GL11.GL_CULL_FACE);
		}*/
		super.render(f);
		/*if (!this.isHidden && this.showModel) {
			//GL11.glCullFace(GL11.GL_NONE);
			GL11.glDisable(GL11.GL_CULL_FACE);
		}*/
	}
}
