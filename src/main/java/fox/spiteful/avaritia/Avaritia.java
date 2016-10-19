package fox.spiteful.avaritia;

import fox.spiteful.avaritia.blocks.LudicrousBlocks;
import fox.spiteful.avaritia.gui.GooeyHandler;
import fox.spiteful.avaritia.items.ItemMatrixIngot;
import fox.spiteful.avaritia.minetweaker.Tweak;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Level;

@Mod(modid = "Avaritia", name = "Avaritia")
public class Avaritia {
    @Mod.Instance
    public static Avaritia instance;

    @SidedProxy(serverSide = "fox.spiteful.avaritia.CommonProxy", clientSide = "fox.spiteful.avaritia.ClientProxy")
    public static CommonProxy proxy;

    public static Item matrixIngot;

    public static CreativeTabs tab = new CreativeTabs("avaritia"){
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem(){
            return matrixIngot;
        }
    };

    @Mod.EventHandler
    public void earlyGame(FMLPreInitializationEvent event){
        instance = this;

        matrixIngot = new ItemMatrixIngot();
        matrixIngot.setRegistryName("matrix_ingot");
        GameRegistry.register(matrixIngot);

        LudicrousBlocks.voxelize();
    }

    @Mod.EventHandler
    public void midGame(FMLInitializationEvent event){
        addRecipes();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GooeyHandler());

        proxy.stupidJsonBullshit();
    }

    @Mod.EventHandler
    public void endGame(FMLPostInitializationEvent event){
        if(Loader.isModLoaded("MineTweaker3"))
            try {
                Tweak.registrate();
            }
            catch (Throwable e){
                Lumberjack.log(Level.ERROR, e, "Avaritia seems to be having trouble with CraftTweaker.");
            }
    }
    
    private void addRecipes(){
        OreDictionary.registerOre("ingotCrystalMatrix", new ItemStack(matrixIngot));

        GameRegistry.addShapedRecipe(new ItemStack(matrixIngot, 8), "DSD", 'D', new ItemStack(Blocks.DIAMOND_BLOCK), 'S', new ItemStack(Items.NETHER_STAR));
        GameRegistry.addShapedRecipe(new ItemStack(LudicrousBlocks.double_craft, 1), "CCC", "CCC", "CCC", 'C', new ItemStack(Blocks.CRAFTING_TABLE));
        GameRegistry.addShapedRecipe(new ItemStack(LudicrousBlocks.triple_craft, 1), "CCC", "CCC", "CCC", 'C', new ItemStack(LudicrousBlocks.double_craft));
        GameRegistry.addShapedRecipe(new ItemStack(Blocks.CRAFTING_TABLE, 9), "C", 'C', new ItemStack(LudicrousBlocks.double_craft));
        GameRegistry.addShapedRecipe(new ItemStack(LudicrousBlocks.double_craft, 9), "C", 'C', new ItemStack(LudicrousBlocks.triple_craft));
        GameRegistry.addShapedRecipe(new ItemStack(LudicrousBlocks.dire_crafting, 1), "CCC", "CXC", "CCC", 'C', new ItemStack(matrixIngot), 'X', new ItemStack(LudicrousBlocks.triple_craft));
    }
}