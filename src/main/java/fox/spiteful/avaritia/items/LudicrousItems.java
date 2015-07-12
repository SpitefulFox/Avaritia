package fox.spiteful.avaritia.items;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class LudicrousItems {

    public static Item resource;
    public static Item singularity;

    public static void grind(){
        resource = new ItemResource();
        GameRegistry.registerItem(resource, "Resource");
        singularity = new ItemSingularity();
        GameRegistry.registerItem(singularity, "Singularity");
    }
}
