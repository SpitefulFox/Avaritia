package morph.avaritia.init;

import codechicken.lib.texture.TextureUtils.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

/**
 * Created by covers1624 on 17/04/2017.
 */
public class AvaritiaTextures implements IIconRegister {

    @Override
    public void registerIcons(TextureMap textureMap) {
        map = textureMap;

        //@formatter:off
        HALO = register(ITEMS_ + "halo");
        HALO_NOISE = register(ITEMS_ + "halo_noise");

        INFINITY_ARMOR_MASK = register(MODELS_ + "infinity_armor_mask");
        INFINITY_ARMOR_MASK_INV = register(MODELS_ + "infinity_armor_mask_inv");
        INFINITY_ARMOR_MASK_WINGS = register(MODELS_ + "infinity_armor_mask_wings");

        COSMIC_0 = register(SHADER_ + "cosmic_0");
        COSMIC_1 = register(SHADER_ + "cosmic_1");
        COSMIC_2 = register(SHADER_ + "cosmic_2");
        COSMIC_3 = register(SHADER_ + "cosmic_3");
        COSMIC_4 = register(SHADER_ + "cosmic_4");
        COSMIC_5 = register(SHADER_ + "cosmic_5");
        COSMIC_6 = register(SHADER_ + "cosmic_6");
        COSMIC_7 = register(SHADER_ + "cosmic_7");
        COSMIC_8 = register(SHADER_ + "cosmic_8");
        COSMIC_9 = register(SHADER_ + "cosmic_9");

        INFINITY_SWORD_MASK   = register(TOOLS_ + "infinity_sword/mask");

        INFINITY_BOW_IDLE = register(TOOLS_ + "infinity_bow/idle");
        INFINITY_BOW_PULL_0 = register(TOOLS_ + "infinity_bow/pull_0");
        INFINITY_BOW_PULL_1 = register(TOOLS_ + "infinity_bow/pull_1");
        INFINITY_BOW_PULL_2 = register(TOOLS_ + "infinity_bow/pull_2");

        INFINITY_BOW_IDLE_MASK = register(TOOLS_ + "infinity_bow/idle_mask");
        INFINITY_BOW_PULL_0_MASK = register(TOOLS_ + "infinity_bow/pull_0_mask");
        INFINITY_BOW_PULL_1_MASK = register(TOOLS_ + "infinity_bow/pull_1_mask");
        INFINITY_BOW_PULL_2_MASK = register(TOOLS_ + "infinity_bow/pull_2_mask");


        INFINITY_BOW_PULL = new TextureAtlasSprite[] {
                INFINITY_BOW_PULL_0,
                INFINITY_BOW_PULL_1,
                INFINITY_BOW_PULL_2
        };

        INFINITY_BOW_PULL_MASK = new TextureAtlasSprite[] {
                INFINITY_BOW_PULL_0_MASK,
                INFINITY_BOW_PULL_1_MASK,
                INFINITY_BOW_PULL_2_MASK
        };


        COSMIC = new TextureAtlasSprite[] {
                COSMIC_0,
                COSMIC_1,
                COSMIC_2,
                COSMIC_3,
                COSMIC_4,
                COSMIC_5,
                COSMIC_6,
                COSMIC_7,
                COSMIC_8,
                COSMIC_9
        };
        //@formatter:on

    }

    // Bouncer to make the class readable.
    private static TextureAtlasSprite register(String sprite) {

        return map.registerSprite(new ResourceLocation(sprite));
    }

    //Assign the TextureMap to a file to make things even more readable!.
    private static TextureMap map;

    private static final String ITEMS_ = "avaritia:items/";
    private static final String MODELS_ = "avaritia:models/";
    private static final String SHADER_ = "avaritia:shader/";
    private static final String TOOLS_ = ITEMS_ + "tools/";

    public static TextureAtlasSprite HALO;
    public static TextureAtlasSprite HALO_NOISE;

    public static TextureAtlasSprite INFINITY_ARMOR_MASK;
    public static TextureAtlasSprite INFINITY_ARMOR_MASK_INV;
    public static TextureAtlasSprite INFINITY_ARMOR_MASK_WINGS;

    public static TextureAtlasSprite[] COSMIC;
    public static TextureAtlasSprite COSMIC_0;
    public static TextureAtlasSprite COSMIC_1;
    public static TextureAtlasSprite COSMIC_2;
    public static TextureAtlasSprite COSMIC_3;
    public static TextureAtlasSprite COSMIC_4;
    public static TextureAtlasSprite COSMIC_5;
    public static TextureAtlasSprite COSMIC_6;
    public static TextureAtlasSprite COSMIC_7;
    public static TextureAtlasSprite COSMIC_8;
    public static TextureAtlasSprite COSMIC_9;

    public static TextureAtlasSprite INFINITY_SWORD_MASK;

    public static TextureAtlasSprite INFINITY_BOW_IDLE;
    public static TextureAtlasSprite[] INFINITY_BOW_PULL;
    public static TextureAtlasSprite INFINITY_BOW_PULL_0;
    public static TextureAtlasSprite INFINITY_BOW_PULL_1;
    public static TextureAtlasSprite INFINITY_BOW_PULL_2;

    public static TextureAtlasSprite INFINITY_BOW_IDLE_MASK;
    public static TextureAtlasSprite[] INFINITY_BOW_PULL_MASK;
    public static TextureAtlasSprite INFINITY_BOW_PULL_0_MASK;
    public static TextureAtlasSprite INFINITY_BOW_PULL_1_MASK;
    public static TextureAtlasSprite INFINITY_BOW_PULL_2_MASK;

}
