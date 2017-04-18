package morph.avaritia;

import codechicken.lib.CodeChickenLib;
import codechicken.lib.gui.SimpleCreativeTab;
import morph.avaritia.compat.minetweaker.Tweak;
import morph.avaritia.proxy.Proxy;
import morph.avaritia.util.Lumberjack;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;

import static morph.avaritia.Avaritia.*;

@Mod (modid = MOD_ID, name = MOD_NAME, version = MOD_VERSION, acceptedMinecraftVersions = CodeChickenLib.mcVersion, dependencies = DEPENDENCIES)
public class Avaritia {

    public static final String MOD_ID = "avaritia";
    public static final String MOD_NAME = "Avaritia";
    public static final String MOD_VERSION = "${mod_version}";
    public static final String DEPENDENCIES = "required-after:CodeChickenLib@[" + CodeChickenLib.version + ",)";

    public static CreativeTabs tab = new SimpleCreativeTab(MOD_ID, "avaritia:resource", 5);

    @Mod.Instance (MOD_ID)
    public static Avaritia instance;

    @SidedProxy (clientSide = "morph.avaritia.proxy.ProxyClient", serverSide = "morph.avaritia.proxy.Proxy")
    public static Proxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
        if (Loader.isModLoaded("MineTweaker3")) {
            try {
                Tweak.registrate();
            } catch (Throwable e) {
                Lumberjack.errorError("Avaritia seems to be having trouble with CraftTweaker.", e);
            }
        }
    }

}
