package fox.spiteful.avaritia;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.potion.Potion;
import org.apache.logging.log4j.Level;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.TreeSet;

public class PotionHelper {
    private static ArrayList<Potion> badPotions;

    public static void healthInspection(){
        badPotions = new ArrayList<Potion>();
        try {
            Field stupidMojangPrivateVariable = ReflectionHelper.findField(Potion.class, "isBadEffect", "field_76418_K");

            for(Potion potion : Potion.potionTypes){
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
