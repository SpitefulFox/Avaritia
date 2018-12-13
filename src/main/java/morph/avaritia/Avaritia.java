package morph.avaritia;

import codechicken.lib.CodeChickenLib;
import codechicken.lib.gui.SimpleCreativeTab;
import morph.avaritia.init.FoodRecipes;
import morph.avaritia.init.ModBlocks;
import morph.avaritia.init.ModItems;
import morph.avaritia.proxy.Proxy;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.util.CompressorBalanceCalculator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import static morph.avaritia.Avaritia.*;

@Mod (modid = MOD_ID, name = MOD_NAME, version = MOD_VERSION, acceptedMinecraftVersions = CodeChickenLib.MC_VERSION_DEP, dependencies = DEPENDENCIES)
public class Avaritia {

    public static final String MOD_ID = "avaritia";
    public static final String MOD_NAME = "Avaritia";
    public static final String MOD_VERSION = "${mod_version}";
    public static final String DEPENDENCIES = "" + CodeChickenLib.MOD_VERSION_DEP;

    public static CreativeTabs tab = new SimpleCreativeTab(MOD_ID, "avaritia:resource", 5);

    @Mod.Instance (MOD_ID)
    public static Avaritia instance;

    @SidedProxy (clientSide = "morph.avaritia.proxy.ProxyClient", serverSide = "morph.avaritia.proxy.Proxy")
    public static Proxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        proxy.preInit(event);
        OreDictionary.registerOre("blockWool", new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("blockCrystalMatrix", new ItemStack(ModBlocks.resource, 1, 2));
        OreDictionary.registerOre("blockCosmicNeutronium", new ItemStack(ModBlocks.resource, 1, 0));
        OreDictionary.registerOre("blockInfinity", new ItemStack(ModBlocks.resource, 1, 1));
        OreDictionary.registerOre("ingotCrystalMatrix", ModItems.crystal_matrix_ingot);
        OreDictionary.registerOre("ingotCosmicNeutronium", ModItems.neutronium_ingot);
        OreDictionary.registerOre("ingotInfinity", ModItems.infinity_ingot);
        OreDictionary.registerOre("nuggetCosmicNeutronium", ModItems.neutron_nugget);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        CompressorBalanceCalculator.gatherBalanceModifier();
        AvaritiaRecipeManager.init();
        FoodRecipes.initFoodRecipes();
    }

}
