package fox.spiteful.avaritia.compat.bloodmagic;

import cpw.mods.fml.common.registry.GameRegistry;
import fox.spiteful.avaritia.compat.Compat;
import fox.spiteful.avaritia.crafting.ExtremeCraftingManager;
import fox.spiteful.avaritia.crafting.Grinder;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Bloody {

    public static void bloodlett() throws Compat.ItemNotFoundException {
        Block crystal = Compat.getBlock("AWWayofTime", "blockCrystal");
        ItemStack cluster = new ItemStack(crystal, 1, 0);
        Grinder.catalyst.getInput().add(cluster);

        LudicrousItems.armok_orb = new ItemOrbArmok();
        GameRegistry.registerItem(LudicrousItems.armok_orb, "Orb_Armok");

        Item transorb = Compat.getItem("AWWayofTime", "transcendentBloodOrb");
        ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(LudicrousItems.armok_orb, 1), new Object[]{
                "   III   ",
                "  IOIOI  ",
                "  IIXII  ",
                " NIOIOIN ",
                "NNNIIINNN",
                " NNNNNNN ",
                "   NNN   ",
                'I', new ItemStack(LudicrousItems.resource, 1, 6),
                'X', new ItemStack(LudicrousItems.resource, 1, 5),
                'N', new ItemStack(LudicrousItems.resource, 1, 4),
                'O', new ItemStack(transorb)
        });
    }

}
