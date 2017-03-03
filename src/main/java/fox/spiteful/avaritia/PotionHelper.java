package fox.spiteful.avaritia;


import net.minecraft.potion.Potion;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.Level;
import java.lang.reflect.Field;
import java.util.ArrayList;


public class PotionHelper {
    private static ArrayList<Potion> badPotions;

    public static void healthInspection(){
        badPotions = new ArrayList<Potion>();
        try {
            Field stupidMojangPrivateVariable = ReflectionHelper.findField(Potion.class, "isBadEffect", "field_76418_K");

            for(Potion potion : Potion.REGISTRY){
                if(potion != null && stupidMojangPrivateVariable.getBoolean(potion))
                    badPotions.add(potion);
            }
        }
        catch(Exception e){
            Lumberjack.log(Level.ERROR, e, "Failure to get potions");
            e.printStackTrace();
        }
    }

    public static boolean badPotion(Potion effect){
        return badPotions.contains(effect);
    }
}
