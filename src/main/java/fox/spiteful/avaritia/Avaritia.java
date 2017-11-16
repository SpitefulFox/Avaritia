package fox.spiteful.avaritia;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.achievements.Achievements;
import fox.spiteful.avaritia.blocks.LudicrousBlocks;
import fox.spiteful.avaritia.compat.Compat;
//import fox.spiteful.avaritia.compat.botania.alfheim.Alfheim;
import fox.spiteful.avaritia.crafting.Gregorizer;
import fox.spiteful.avaritia.crafting.Grinder;
import fox.spiteful.avaritia.crafting.Mincer;
import fox.spiteful.avaritia.entity.LudicrousEntities;
import fox.spiteful.avaritia.gui.GooeyHandler;
import fox.spiteful.avaritia.items.ItemFracturedOre;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = "Avaritia", name = "Avaritia", dependencies = "after:Thaumcraft;after:AWWayofTime;after:Botania")
public class Avaritia {
    @Instance
    public static Avaritia instance;

    @SidedProxy(serverSide = "fox.spiteful.avaritia.CommonProxy", clientSide = "fox.spiteful.avaritia.ClientProxy")
    public static CommonProxy proxy;

    public static CreativeTabs tab = new CreativeTabs("avaritia"){
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem(){
            return LudicrousItems.resource;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public int func_151243_f()
        {
            return 5;
        }
    };

    @EventHandler
    public void earlyGame(FMLPreInitializationEvent event){
        instance = this;
        Config.configurate(event.getSuggestedConfigurationFile());
        LudicrousItems.grind();
        LudicrousBlocks.voxelize();
        Compat.census();
        if(Config.craftingOnly)
            return;

        LudicrousEntities.letLooseTheDogsOfWar();
        proxy.prepareForPretty();
        //if(Compat.botan && Config.alfheim)
            //Alfheim.packYourBags();
    }

    @EventHandler
    public void midGame(FMLInitializationEvent event){
        Grinder.artsAndCrafts();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GooeyHandler());
        if(Config.craftingOnly)
            return;
        proxy.makeThingsPretty();
        MinecraftForge.EVENT_BUS.register(new LudicrousEvents());
        if(Config.fractured)
            ItemFracturedOre.brushUpUncomfortablyAgainstTheOreDictionary();
    }

    @EventHandler
    public void endGame(FMLPostInitializationEvent event){
        Compat.compatify();
        Gregorizer.balance();
        if(Config.craftingOnly)
            return;
        Mincer.countThoseCalories();
        Grinder.lastMinuteChanges();
        Achievements.achieve();
        PotionHelper.healthInspection();
        proxy.theAfterPretty();
    }
}