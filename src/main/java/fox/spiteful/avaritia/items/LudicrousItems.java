package fox.spiteful.avaritia.items;

import cpw.mods.fml.common.registry.GameRegistry;
import fox.spiteful.avaritia.items.tools.ItemPickaxeInfinity;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

import static net.minecraft.item.Item.ToolMaterial;

public class LudicrousItems {

    public static Item resource;
    public static Item singularity;
    public static Item infinity_pickaxe;

    public static ToolMaterial opTool = EnumHelper.addToolMaterial("INFINITY", 32, 9999, 9999F, 9999F, 100);

    public static void grind(){
        resource = new ItemResource();
        GameRegistry.registerItem(resource, "Resource");
        singularity = new ItemSingularity();
        GameRegistry.registerItem(singularity, "Singularity");
        infinity_pickaxe = new ItemPickaxeInfinity();
        GameRegistry.registerItem(infinity_pickaxe, "Infinity_Pickaxe");
    }
}
