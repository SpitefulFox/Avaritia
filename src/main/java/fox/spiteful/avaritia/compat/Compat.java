package fox.spiteful.avaritia.compat;

import cpw.mods.fml.common.Loader;

public class Compat {

    public static boolean nei = false;
    public static boolean thaumic = false;

    public static void census(){
        nei = Loader.isModLoaded("NotEnoughItems");
        thaumic = Loader.isModLoaded("Thaumcraft");
    }

    public static void compatify(){
        if(nei){
            try
            {
                Class<?> handler = Class.forName("fox.spiteful.avaritia.compat.ExtremeShapedRecipeHandler");
                Class<?> handler2 = Class.forName("fox.spiteful.avaritia.compat.ExtremeShapelessRecipeHandler");
                Class<?> api = Class.forName("codechicken.nei.api.API");
                api.getMethod("registerRecipeHandler", Class.forName("codechicken.nei.recipe.ICraftingHandler")).invoke(null, handler.newInstance());
                api.getMethod("registerUsageHandler", Class.forName("codechicken.nei.recipe.IUsageHandler")).invoke(null, handler.newInstance());
                api.getMethod("registerRecipeHandler", Class.forName("codechicken.nei.recipe.ICraftingHandler")).invoke(null, handler2.newInstance());
                api.getMethod("registerUsageHandler", Class.forName("codechicken.nei.recipe.IUsageHandler")).invoke(null, handler2.newInstance());
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        if(thaumic){
            try
            {
                Lucrum.abracadabra();
            }
            catch(Throwable e){
                e.printStackTrace();
            }
        }
    }
}
