package fox.spiteful.avaritia.compat.ticon;

import java.awt.image.BufferedImage;
import java.util.Random;

import codechicken.lib.math.MathHelper;

public class NeutroniumIcons extends TConTextureResourcePackBase {

	private double[] noise;
	
	public NeutroniumIcons() {
		super(Tonkers.neutroniumName);
		
		int len = 16*16;
		Random randy = new Random(12345);
		noise = new double[len];
		for (int i=0; i<len; i++) {
			noise[i] = randy.nextDouble();
		}
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
		
		int c, l, a;
		int r, g, b;
		double e, n;
		for (int y = 0; y<h; y++) {
			for (int x = 0; x<w; x++) {
				c = image.getRGB(x, y);
				l = MathHelper.floor_double(Math.min(255, (255-this.brightness(c))*1.05));
				a = rgb.getAlpha(c);
				
				if (isEdge(image, x, y, w, h, a)) {
					n = noise[(y*w+x) % noise.length];
					e = Math.min(1.0, (l / 255.0) * 0.5 + 0.5 + n*0.2);
					r = MathHelper.floor_double(e * 188);
					g = MathHelper.floor_double(e * 192);
					b = MathHelper.floor_double(e * 219);
					image.setRGB(x, y, colour(r,g,b,a));
				} else {
					image.setRGB(x, y, colour(l,l,l,a));
				}
			}
		}
		
		return image;
	}
	
	private boolean isEdge(BufferedImage image, int x, int y, int w, int h, int a) {
		if (a != 0) {
			if (x == 0 || x == w-1 || y == 0 || y == h-1) { return true; }
			
			if (rgb.getAlpha(image.getRGB(x-1, y)) != 255
				|| rgb.getAlpha(image.getRGB(x+1, y)) != 255
				|| rgb.getAlpha(image.getRGB(x, y-1)) != 255
				|| rgb.getAlpha(image.getRGB(x, y+1)) != 255
			) {
				return true;
			}
		}
		return false;
	}
}
