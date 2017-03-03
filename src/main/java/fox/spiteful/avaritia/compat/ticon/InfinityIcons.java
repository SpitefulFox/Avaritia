package fox.spiteful.avaritia.compat.ticon;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.Level;

import codechicken.lib.math.MathHelper;

import fox.spiteful.avaritia.Lumberjack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class InfinityIcons extends TConTextureResourcePackBase {

	protected BufferedImage infinityImage;
	
	public InfinityIcons() {
		super(Tonkers.infinityMetalName);
	}

	protected void loadInfinityImage() {
		if (infinityImage == null) {
			ResourceLocation res = new ResourceLocation("avaritia", "textures/items/ticoninfinity.png");
	        try {
	        	InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(res).getInputStream();
	            infinityImage = ImageIO.read(stream);
	        } catch (Exception err) {
	            Lumberjack.log(Level.WARN, "Unable to load overlay image for Infinity tool parts");
	        }
		}
	}
	protected InputStream loadInfinityAnimation() {
		InputStream stream = null;
		ResourceLocation res = new ResourceLocation("avaritia", "textures/items/ticoninfinitymeta.png.mcmeta");
		try {
			stream = Minecraft.getMinecraft().getResourceManager().getResource(res).getInputStream();
		} catch (Exception err) {
            Lumberjack.log(Level.WARN, "Unable to load animation data for Infinity tool parts");
        }
		return stream;
	}
	
	@Override
	public BufferedImage modifyImage(BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();
		
		if (image.getType() != BufferedImage.TYPE_INT_ARGB) {
			BufferedImage temp = new BufferedImage(w,h, BufferedImage.TYPE_INT_ARGB);
			int[] data = new int[w*h];
			image.getRGB(0, 0, w, h, data, 0, w);
			temp.setRGB(0, 0, w, h, data, 0, w);
			image = temp;
		}
		
		int c, a, i, inf;
		int r, g, b;
		int fr,fg,fb;
		double l, frac;
		
		int left = w-1;
		int right = 0;
		int top = h-1;
		int bottom = 0;
		boolean anyOpaque = false;
		int opaquecount = 0;
		
		// get the extents of the opaque part of the image
		for (int y = 0; y<h; y++) {
			for (int x = 0; x<w; x++) {
				c = image.getRGB(x, y);
				a = rgb.getAlpha(c);
				
				if (a != 0) {
					left = Math.min(x, left);
					right = Math.max(x, right);
					top = Math.min(y, top);
					bottom = Math.max(y, bottom);
					anyOpaque = true;
					opaquecount++;
				}
			}
		}
		
		int opaqueWidth = (right-left)+1;
		int opaqueHeight = (bottom-top)+1;
		int opaqueSquare = Math.max(w/2, Math.max(opaqueHeight, opaqueWidth));
		if (opaqueWidth < opaqueSquare) {
			int diff = opaqueSquare - opaqueWidth;
			left -= diff/2;
		} else if (opaqueHeight < opaqueSquare) {
			int diff = opaqueSquare - opaqueHeight;
			top -= diff/2;
		}
		
		// scale the infinity image
		BufferedImage infinity = new BufferedImage(w,h, BufferedImage.TYPE_INT_ARGB);
		this.loadInfinityImage();
		if (anyOpaque && infinityImage != null) {
			int iw = infinityImage.getWidth();
			int ih = infinityImage.getHeight();
			int sx1 = 0;
			int sx2 = iw;
			int sy1 = 0;
			int sy2 = ih;
			if (opaquecount > w*h*0.3) {
				sx1 = MathHelper.floor_double(iw*0.25);
				sx2 = MathHelper.floor_double(iw*0.75);
				sy1 = MathHelper.floor_double(ih*0.25);
				sy2 = MathHelper.floor_double(ih*0.75);
			}
			
			Graphics2D graphics = infinity.createGraphics();
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			//graphics.drawImage(infinityImage, left, top, opaqueSquare, opaqueSquare, null);
			
			graphics.drawImage(infinityImage, left, top, left+opaqueSquare, top+opaqueSquare, sx1,sy1,sx2,sy2, null);
		}
		
		//ok, now do stuff with it
		
		/*int[] data = new int[w*h];
		infinity.getRGB(0, 0, w, h, data, 0, w);
		image.setRGB(0, 0, w, h, data, 0, w);*/
		
		int frames = 9;
		
		BufferedImage framesimage = new BufferedImage(w,h*frames, BufferedImage.TYPE_INT_ARGB);
		
		double brighten = 1.3;
		for (int y = 0; y<h; y++) {
			for (int x = 0; x<w; x++) {
				c = image.getRGB(x, y);
				l = Math.min(1.0, Math.max(0.4,(this.brightness(c)/255.0)*1.4 - 0.4));
				a = rgb.getAlpha(c);
				
				if (a != 0) {
					inf = infinity.getRGB(x, y);
					r = Math.min(255, MathHelper.floor_double(rgb.getRed(inf) * l * brighten));
					g = Math.min(255, MathHelper.floor_double(rgb.getGreen(inf) * l * brighten));
					b = Math.min(255, MathHelper.floor_double(rgb.getBlue(inf) * l * brighten));
					for (i=0; i<frames; i++) {
						frac = 0.125 * i;
						fr = MathHelper.floor_double((1-frac)*r + frac*255);
						fg = MathHelper.floor_double((1-frac)*g + frac*255);
						fb = MathHelper.floor_double((1-frac)*b + frac*255);
						framesimage.setRGB(x, y + h*i, colour(fr,fg,fb,a));
					}
				}
			}
		}
		
		return framesimage;
	}
	
	@Override
    public void onResourceManagerReload(IResourceManager manager) {
        super.onResourceManagerReload(manager);
        this.infinityImage = null;
    }

	@Override
    public boolean resourceExists(ResourceLocation resource) {
        String resourcePath = resource.getResourcePath();

        if (resourcePath.endsWith(".mcmeta")) {
        	String testpath = resourcePath.substring(0, resourcePath.length()-7);
        	if (resourceExists(new ResourceLocation(resource.getResourceDomain(), testpath))) {
        		return true;
        	}
        }
        return super.resourceExists(resource);
    }
	
	@Override
	public InputStream getInputStream(ResourceLocation resource) throws IOException {
		if (resource.getResourcePath().endsWith(".mcmeta")) {
			return this.loadInfinityAnimation();
		}
		return super.getInputStream(resource);
	}
}
