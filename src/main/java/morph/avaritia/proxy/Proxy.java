package morph.avaritia.proxy;

import morph.avaritia.Avaritia;
import morph.avaritia.achievements.AchievementTrigger;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.client.gui.GUIHandler;
import morph.avaritia.entity.EntityEndestPearl;
import morph.avaritia.entity.EntityGapingVoid;
import morph.avaritia.entity.EntityHeavenArrow;
import morph.avaritia.entity.EntityHeavenSubArrow;
import morph.avaritia.handler.AbilityHandler;
import morph.avaritia.handler.AvaritiaEventHandler;
import morph.avaritia.handler.ConfigHandler;
import morph.avaritia.init.FoodRecipes;
import morph.avaritia.init.ModBlocks;
import morph.avaritia.init.ModItems;
import morph.avaritia.init.Recipes;
import morph.avaritia.util.CompressorBalanceCalculator;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class Proxy {

    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        ModItems.init();
        ModBlocks.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(Avaritia.instance, new GUIHandler());
        MinecraftForge.EVENT_BUS.register(new AbilityHandler());
        MinecraftForge.EVENT_BUS.register(new AvaritiaEventHandler());
        MinecraftForge.EVENT_BUS.register(new AchievementTrigger());

        EntityRegistry.registerModEntity(EntityEndestPearl.class, "EndestPearl", 1, Avaritia.instance, 64, 10, true);
        EntityRegistry.registerModEntity(EntityGapingVoid.class, "GapingVoid", 2, Avaritia.instance, 256, 10, false);
        EntityRegistry.registerModEntity(EntityHeavenArrow.class, "HeavenArrow", 3, Avaritia.instance, 32, 1, true);
        EntityRegistry.registerModEntity(EntityHeavenSubArrow.class, "HeavenSubArrow", 4, Avaritia.instance, 32, 2, true);
    }

    public void init(FMLInitializationEvent event) {
        CompressorBalanceCalculator.gatherBalanceModifier();
        FoodRecipes.initFoodRecipes();
        Recipes.init();
        Recipes.initRecipeCompat();
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
