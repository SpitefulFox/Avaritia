package fox.spiteful.avaritia.compat.ticon;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.Level;

import fox.spiteful.avaritia.Lumberjack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class InfinityIcons extends TConTextureResourcePackBase {

	protected BufferedImage infinityImage;
	
	public InfinityIcons() {
		super(Tonkers.infinityMetalName);
	}

	protected BufferedImage loadInfinityImage() {
		ResourceLocation res = new ResourceLocation("avaritia", "textures/items/ticoninfinity.png");
        try {
        	InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(res).getInputStream();
            infinityImage = ImageIO.read(stream);
        } catch (Exception err) {
            Lumberjack.log(Level.WARN, "Unable to load overlay image for Infinity tool parts");
        }
		return infinityImage;
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
		
		// scale the infinity image
		BufferedImage infinity = new BufferedImage(w,h, BufferedImage.TYPE_INT_ARGB);
		if (infinityImage != null) {
			Graphics2D g = infinity.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.drawImage(infinityImage, 0, 0, w, h, null);
		}
		
		//ok, now do stuff with it
		
		int[] data = new int[w*h];
		infinity.getRGB(0, 0, w, h, data, 0, w);
		image.setRGB(0, 0, w, h, data, 0, w);
		
		return image;
	}
	
	@Override
    public void onResourceManagerReload(IResourceManager manager) {
        super.onResourceManagerReload(manager);
        this.infinityImage = loadInfinityImage();
    }

}
