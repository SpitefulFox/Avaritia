package fox.spiteful.avaritia.compat.botania;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelInfinitato extends ModelBase {

	ModelRenderer potato;

	public ModelInfinitato() {
		textureWidth = 64;
		textureHeight = 32;

		potato = new ModelRenderer(this, 0, 0);
		potato.addBox(0F, 0F, 0F, 8, 12, 8);
		potato.setRotationPoint(-4F, 12F, -4F);
		potato.setTextureSize(64, 32);
	}

	public void render() {
		potato.render(1F / 16F);
	}

}
