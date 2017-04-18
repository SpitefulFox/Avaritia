package morph.avaritia.client.render.item;

import codechicken.lib.colour.Colour;
import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.util.VertexDataUtils;
import codechicken.lib.vec.Vector3;
import codechicken.lib.vec.Vertex5;
import codechicken.lib.vec.uv.UV;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Generates a lattice model same as 1.7's item renderer.
 * THIS DOES NOT WORK AND NEEDS TO BE FIXED!
 * Created by covers1624 on 17/04/2017.
 */
public class LatticeModelCreator {

    public static List<BakedQuad> generateLatticeModel(Set<TextureAtlasSprite> sprites) {
        List<BakedQuad> quads = new LinkedList<>();
        VertexFormat format = DefaultVertexFormats.ITEM;
        Colour colour = new ColourRGBA(0xFFFFFFFF);
        double scale = 1D / 16D;

        int i = 0;
        for (TextureAtlasSprite sprite : sprites) {
            float minU = sprite.getMinU();
            float maxU = sprite.getMaxU();
            float minV = sprite.getMinV();
            float maxV = sprite.getMaxV();
            int iconWidth = sprite.getIconWidth();
            int iconHeight = sprite.getIconHeight();

            Vertex5 v1 = new Vertex5();
            Vertex5 v2 = new Vertex5();
            Vertex5 v3 = new Vertex5();
            Vertex5 v4 = new Vertex5();

            v1.set(0.0D, 0.0D, 0.0D, maxU, maxV);
            v2.set(1.0D, 0.0D, 0.0D, minU, maxV);
            v3.set(1.0D, 1.0D, 0.0D, minU, minV);
            v4.set(0.0D, 1.0D, 0.0D, maxU, minV);
            quads.add(buildQuad(format, sprite, EnumFacing.SOUTH, colour, i, v1, v2, v3, v4));

            v1.set(0.0D, 1.0D, 0.0D - scale, maxU, minV);
            v2.set(1.0D, 1.0D, 0.0D - scale, minU, minV);
            v3.set(1.0D, 0.0D, 0.0D - scale, minU, maxV);
            v4.set(0.0D, 0.0D, 0.0D - scale, maxU, maxV);
            quads.add(buildQuad(format, sprite, EnumFacing.NORTH, colour, i, v1, v2, v3, v4));

            double sU = 0.5D * (maxU - minU) / iconWidth;
            double sV = 0.5D * (maxV - minV) / iconWidth;

            for (int j = 0; j < iconWidth; j++) {
                double d1 = (double) j / (double) iconWidth;
                double d2 = maxU + (minU - maxU) * d1 - sU;

                v1.set(d1, 0.0D, 0.0D - scale, d2, maxV);
                v2.set(d1, 0.0D, 0.0D, d2, maxV);
                v3.set(d1, 1.0D, 0.0D, d2, minV);
                v4.set(d1, 1.0D, 0.0D - scale, d2, minV);
                quads.add(buildQuad(format, sprite, EnumFacing.WEST, colour, i, v1, v2, v3, v4));
            }

            for (int j = 0; j < iconWidth; ++j) {
                double d1 = (double) j / (double) iconWidth;
                double d2 = maxU + (minU - maxU) * d1 - sU;
                double d3 = d1 + 1.0F / (float) iconWidth;

                v1.set(d3, 1.0D, 0.0F - scale, d2, (double) minV);
                v2.set(d3, 1.0D, 0.0D, d2, (double) minV);
                v3.set(d3, 0.0D, 0.0D, d2, (double) maxV);
                v4.set(d3, 0.0D, 0.0F - scale, d2, (double) maxV);
                quads.add(buildQuad(format, sprite, EnumFacing.EAST, colour, i, v1, v2, v3, v4));
            }

            for (int j = 0; j < iconHeight; ++j) {
                double d1 = (float) j / (float) iconHeight;
                double d2 = maxV + (minV - maxV) * d1 - sV;
                double d3 = d1 + 1.0F / (float) iconHeight;

                v1.set(0.0D, d3, 0.0D, (double) maxU, d2);
                v2.set(1.0D, d3, 0.0D, (double) minU, d2);
                v3.set(1.0D, d3, 0.0F - scale, (double) minU, d2);
                v4.set(0.0D, d3, 0.0F - scale, (double) maxU, d2);
                quads.add(buildQuad(format, sprite, EnumFacing.UP, colour, i, v1, v2, v3, v4));
            }

            for (int j = 0; j < iconHeight; ++j) {
                double d1 = (float) j / (float) iconHeight;
                double d2 = maxV + (minV - maxV) * d1 - sV;

                v1.set(1.0D, d1, 0.0D, (double) minU, d2);
                v2.set(0.0D, d1, 0.0D, (double) maxU, d2);
                v3.set(0.0D, d1, 0.0F - scale, (double) maxU, d2);
                v4.set(1.0D, d1, 0.0F - scale, (double) minU, d2);
                quads.add(buildQuad(format, sprite, EnumFacing.DOWN, colour, i, v1, v2, v3, v4));
            }

            i++;
        }
        return quads;
    }

    private static BakedQuad buildQuad(VertexFormat format, TextureAtlasSprite sprite, EnumFacing face, Colour colour, int tint, Vertex5 v1, Vertex5 v2, Vertex5 v3, Vertex5 v4) {
        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        builder.setQuadTint(tint);
        builder.setQuadOrientation(face);
        builder.setTexture(sprite);

        putVertex(builder, format, face, v1, colour);
        putVertex(builder, format, face, v2, colour);
        putVertex(builder, format, face, v3, colour);
        putVertex(builder, format, face, v4, colour);

        return VertexDataUtils.copyQuad(builder.build());
    }

    private static void putVertex(UnpackedBakedQuad.Builder builder, VertexFormat format, EnumFacing face, Vertex5 vert, Colour colour) {
        for (int e = 0; e < format.getElementCount(); e++) {
            VertexFormatElement element = format.getElement(e);
            switch (element.getUsage()) {

                case POSITION:
                    Vector3 vec = vert.vec;
                    builder.put(e, (float) vec.x, (float) vec.y, (float) vec.z, 1);
                    break;
                case NORMAL:
                    builder.put(e, face.getFrontOffsetX(), face.getFrontOffsetY(), face.getFrontOffsetZ(), 0);
                    break;
                case COLOR:
                    builder.put(e, (colour.r & 0xFF) / 255F, (colour.g & 0xFF) / 255F, (colour.b & 0xFF) / 255F, (colour.a & 0xFF) / 255F);
                    break;
                case UV:
                    UV uv = vert.uv;
                    builder.put(e, (float) uv.u, (float) uv.v, 0, 1);
                    break;
                default:
                    builder.put(e);
                    break;
            }
        }
    }
}
