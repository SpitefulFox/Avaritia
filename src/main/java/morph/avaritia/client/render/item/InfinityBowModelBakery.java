package morph.avaritia.client.render.item;

import codechicken.lib.model.ItemQuadBakery;
import codechicken.lib.model.bakedmodels.ModelProperties.PerspectiveProperties;
import codechicken.lib.model.bakery.generation.IItemBakery;
import codechicken.lib.util.TransformUtils;
import morph.avaritia.init.AvaritiaTextures;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by covers1624 on 17/04/2017.
 */
public class InfinityBowModelBakery implements IItemBakery {

    public static final InfinityBowModelBakery INSTANCE = new InfinityBowModelBakery();

    private InfinityBowModelBakery() {
    }

    @Override
    public List<BakedQuad> bakeItemQuads(EnumFacing face, ItemStack stack) {
        List<BakedQuad> quads = new LinkedList<>();

        TextureAtlasSprite sprite = AvaritiaTextures.INFINITY_BOW_IDLE;

        //These flags are set on a copied stack specifically to be handled in here.
        int frame = -1;
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("frame")) {
            frame = stack.getTagCompound().getInteger("frame");
        }
        if (frame != -1) {
            sprite = AvaritiaTextures.INFINITY_BOW_PULL[frame];
        }
        quads.addAll(ItemQuadBakery.bakeItem(Collections.singletonList(sprite)));
        return quads;
    }

    @Override
    public PerspectiveProperties getModelProperties(ItemStack stack) {
        return new PerspectiveProperties(TransformUtils.DEFAULT_BOW, true, false);
    }
}
