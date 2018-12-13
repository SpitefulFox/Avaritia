package morph.avaritia.proxy;

import com.mojang.authlib.GameProfile;
import morph.avaritia.Avaritia;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.client.gui.GUIHandler;
import morph.avaritia.entity.EntityEndestPearl;
import morph.avaritia.entity.EntityGapingVoid;
import morph.avaritia.entity.EntityHeavenArrow;
import morph.avaritia.entity.EntityHeavenSubArrow;
import morph.avaritia.handler.AbilityHandler;
import morph.avaritia.handler.AvaritiaEventHandler;
import morph.avaritia.handler.ConfigHandler;
import morph.avaritia.init.ModBlocks;
import morph.avaritia.init.ModItems;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.UUID;

public class Proxy {

    public static final GameProfile avaritiaFakePlayer = new GameProfile(UUID.fromString("32283731-bbef-487c-bb69-c7e32f84ed27"), "[Avaritia]");

    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        ModItems.init();
        ModBlocks.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(Avaritia.instance, new GUIHandler());
        MinecraftForge.EVENT_BUS.register(new AbilityHandler());
        MinecraftForge.EVENT_BUS.register(new AvaritiaEventHandler());

        EntityRegistry.registerModEntity(new ResourceLocation("avaritia:endest_pearl"), EntityEndestPearl.class, "EndestPearl", 1, Avaritia.instance, 64, 10, true);
        EntityRegistry.registerModEntity(new ResourceLocation("avaritia:gaping_void"), EntityGapingVoid.class, "GapingVoid", 2, Avaritia.instance, 256, 10, false);
        EntityRegistry.registerModEntity(new ResourceLocation("avaritia:heaven_arrow"), EntityHeavenArrow.class, "HeavenArrow", 3, Avaritia.instance, 32, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation("avaritia:heaven_sub_arrow"), EntityHeavenSubArrow.class, "HeavenSubArrow", 4, Avaritia.instance, 32, 2, true);
    }

    public void init(FMLInitializationEvent event) {
    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    public void addModelRegister(IModelRegister register) {

    }

    public boolean isClient() {
        return false;
    }

    public boolean isServer() {
        return true;
    }

    public World getClientWorld() {
        return null;
    }

}
