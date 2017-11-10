package fox.spiteful.avaritia.compat.forestry;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import forestry.api.recipes.RecipeManagers;
import fox.spiteful.avaritia.Config;
import fox.spiteful.avaritia.compat.Compat;
import fox.spiteful.avaritia.crafting.Grinder;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.EnumHelper;

public class Ranger {

    public static Item honey;
    public static Item honeydew;
    public static Item comb;

    public static boolean magic = false;
    public static boolean extra = false;

    public static EnumRarity trash = EnumHelper.addRarity("TRASH", EnumChatFormatting.DARK_GRAY, "Trash");

    public static void stopForestFires() throws Compat.ItemNotFoundException {
        magic = Loader.isModLoaded("MagicBees");
        extra = Loader.isModLoaded("ExtraBees");

        comb = Compat.getItem("Forestry", "beeCombs");
        honey = Compat.getItem("Forestry", "honeyDrop");
        honeydew = Compat.getItem("Forestry", "honeydew");
        Item panel = Compat.getItem("Forestry", "craftingMaterial");

        if(!Config.bees)
            return;

        LudicrousItems.combs = new ItemComb();
        GameRegistry.registerItem(LudicrousItems.combs, "Combs");
        LudicrousItems.beesource = new ItemBeesource();
        GameRegistry.registerItem(LudicrousItems.beesource, "Beesource");

        Allele.prepareGenes();
        GreedyBeeSpecies.buzz();
        ExpensiveMutation.mutate();

        RecipeManagers.centrifugeManager.addRecipe(20, new ItemStack(LudicrousItems.combs, 1, 1),
                new ItemStack[]{new ItemStack(Items.dye, 1, 1), new ItemStack(Items.dye, 1, 2),
                        new ItemStack(Items.dye, 1, 4), new ItemStack(Items.dye, 1, 5),
                        new ItemStack(Items.dye, 1, 11), new ItemStack(Items.dye, 1, 14)},
                new int[]{16, 16, 16, 16, 16, 16});

        RecipeManagers.centrifugeManager.addRecipe(20, new ItemStack(LudicrousItems.combs, 1, 0),
                new ItemStack(LudicrousItems.beesource, 1, 1));

        Grinder.catalyst.getInput().add(new ItemStack(panel, 1, 6));
        Grinder.catalyst.getInput().add(new ItemStack(LudicrousItems.beesource, 1, 0));
    }

}
