// Original file by RWTema for Extra Utilities
// Even though the rest of the mod is open, please don't use this class without Tema's permission

package fox.spiteful.avaritia.compat.ticon;

import com.google.common.base.Throwables;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import fox.spiteful.avaritia.Lumberjack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.*;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
 
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Original class by RWTema for Extra Utilities
 */
public abstract class TConTextureResourcePackBase implements IResourcePack, IResourceManagerReloadListener {
    public static List<IResourcePack> packs;
    protected static DirectColorModel rgb = new DirectColorModel(32,
            0x00ff0000,       // Red
            0x0000ff00,       // Green
            0x000000ff,       // Blue
            0xff000000        // Alpha
    );
    protected final String name;
    public HashMap<ResourceLocation, byte[]> cachedImages = new HashMap<ResourceLocation, byte[]>();
    protected IResourcePack delegate;
    protected List<IResourcePack> resourcePackz = null;
 
    public TConTextureResourcePackBase(String name) {
        this.name = name.toLowerCase();
        this.delegate = FMLClientHandler.instance().getResourcePackFor("TConstruct");
    }
 
    public int brightness(int col) {
        return brightness(rgb.getRed(col), rgb.getGreen(col), rgb.getBlue(col));
    }
 
    public int brightness(int r, int g, int b) {
        return (int) (r * 0.2126F + g * 0.7152F + b * 0.0722F);
    }
 
    public void register() {
        List<IResourcePack> packs = getiResourcePacks();
        packs.add(this);
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
        Lumberjack.info("Registered TCon Resource Pack (" + name + ") - " + this.getClass().getSimpleName());
    }
 
    public List<IResourcePack> getiResourcePacks() {
        List<IResourcePack> packs1 = packs;
        if (packs1 == null)
            packs1 = ObfuscationReflectionHelper.getPrivateValue(FMLClientHandler.class, FMLClientHandler.instance(), "resourcePackList");
        return packs1;
    }
 
    public InputStream getStream(ResourceLocation location) {
        InputStream stream = null;
        for (IResourcePack iResourcePack : getPacks()) {
            if (iResourcePack.resourceExists(location)) {
                try {
                    stream = iResourcePack.getInputStream(location);
                } catch (IOException ignore) {
 
                }
            }
        }
        return stream;
    }
 
    @SuppressWarnings("unchecked")
	public List<IResourcePack> getPacks() {
        if (resourcePackz == null) {
            resourcePackz = new ArrayList<IResourcePack>();
            resourcePackz.add(delegate);
            List<ResourcePackRepository.Entry> t = Minecraft.getMinecraft().getResourcePackRepository().getRepositoryEntries();
            for (ResourcePackRepository.Entry entry : t) {
                IResourcePack resourcePack = entry.getResourcePack();
                if (resourcePack.getResourceDomains().contains("tinker")) {
                    resourcePackz.add(resourcePack);
                }
            }
        }
 
        return resourcePackz;
    }
 
    @Override
    public InputStream getInputStream(ResourceLocation p_110590_1_) throws IOException {
        byte[] bytes = cachedImages.get(p_110590_1_);
        if (bytes == null) {
            ResourceLocation location = new ResourceLocation("tinker", p_110590_1_.getResourcePath().replace(name, ""));
            InputStream inputStream = getStream(location);
 
            if (inputStream == null) {
                location = new ResourceLocation("tinker", p_110590_1_.getResourcePath().replace(name, "iron"));
                inputStream = getStream(location);
            }
 
            if (inputStream == null) {
                location = new ResourceLocation("tinker", p_110590_1_.getResourcePath().replace(name, "stone"));
                inputStream = getStream(location);
            }
 
            if (inputStream == null)
                return delegate.getInputStream(p_110590_1_);
 
            BufferedImage bufferedimage;
            try {
                bufferedimage = ImageIO.read(inputStream);
            } catch (IOException err) {
                throw Throwables.propagate(err);
            }
 
            BufferedImage image;
            try {
                image = modifyImage(bufferedimage);
            } catch (Throwable t) {
                t.printStackTrace();
                return delegate.getInputStream(location);
            }
 
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", stream);
            bytes = stream.toByteArray();
            cachedImages.put(location, bytes);
        }
 
        return new ByteArrayInputStream(bytes);
    }
 
    public abstract BufferedImage modifyImage(BufferedImage image);
 
    @Override
    public boolean resourceExists(ResourceLocation p_110589_1_) {
        if (!"tinker".equals(p_110589_1_.getResourceDomain()))
            return false;
 
 
        String resourcePath = p_110589_1_.getResourcePath();
        if (!(resourcePath.startsWith("textures/items/") && resourcePath.endsWith(".png")))
            return false;
 
        if (delegate.resourceExists(p_110589_1_))
            return false;
 
        if (!resourcePath.contains(name))
            return false;
 
        return delegate.resourceExists(new ResourceLocation("tinker", resourcePath.replace(name, "stone")))
                || delegate.resourceExists(new ResourceLocation("tinker", resourcePath.replace(name, "iron")))
                || delegate.resourceExists(new ResourceLocation("tinker", resourcePath.replace(name, "")));
 
    }
 
    @SuppressWarnings("rawtypes")
	@Override
    public Set getResourceDomains() {
        return delegate.getResourceDomains();
    }
 
    @Override
    public IMetadataSection getPackMetadata(IMetadataSerializer p_135058_1_, String p_135058_2_) throws IOException {
        return null;
    }
 
    @Override
    public BufferedImage getPackImage() throws IOException {
        return null;
    }
 
    @Override
    public String getPackName() {
        return "Avaritia_Delegate_Pack";
    }
 
    @Override
    public void onResourceManagerReload(IResourceManager p_110549_1_) {
        cachedImages.clear();
        resourcePackz = null;
    }
    
    private int[] colourarray = new int[4];
    protected int colour(int r, int g, int b, int a) {
    	colourarray[0] = r;
    	colourarray[1] = g;
    	colourarray[2] = b;
    	colourarray[3] = a;
    	return rgb.getDataElement(colourarray, 0);
    }
}
