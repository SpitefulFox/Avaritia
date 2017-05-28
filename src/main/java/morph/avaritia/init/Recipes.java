package morph.avaritia.init;

import codechicken.lib.util.ItemUtils;
import morph.avaritia.handler.ConfigHandler;
import morph.avaritia.recipe.compressor.CompressorManager;
import morph.avaritia.recipe.extreme.ExtremeCraftingManager;
import morph.avaritia.recipe.extreme.ExtremeShapelessOreRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * Created by covers1624 on 11/04/2017.
 */
public class Recipes {

    public static ExtremeShapelessOreRecipe catalyst;

    public static void init() {
        OreDictionary.registerOre("blockCrystalMatrix", new ItemStack(ModBlocks.resource, 1, 2));
        OreDictionary.registerOre("blockCosmicNeutronium", new ItemStack(ModBlocks.resource, 1, 0));
        OreDictionary.registerOre("blockInfinity", new ItemStack(ModBlocks.resource, 1, 1));
        OreDictionary.registerOre("ingotCrystalMatrix", ModItems.crystal_matrix_ingot);
        OreDictionary.registerOre("ingotCosmicNeutronium", ModItems.neutronium_ingot);
        OreDictionary.registerOre("ingotInfinity", ModItems.infinity_ingot);

        //@formatter:off
        GameRegistry.addShapedRecipe(
                ModItems.diamond_lattice,
                "X X",
                " X ",
                "X X",
                'X', new ItemStack(Items.DIAMOND)
        );
        GameRegistry.addShapedRecipe(
                ModItems.crystal_matrix_ingot,
                "DSD",
                "DSD",
                'D', ModItems.diamond_lattice,
                'S', new ItemStack(Items.NETHER_STAR)
        );
        GameRegistry.addShapedRecipe(
                new ItemStack(ModBlocks.double_craft, 1),
                "CCC",
                "CCC",
                "CCC",
                'C', new ItemStack(Blocks.CRAFTING_TABLE)
        );
        GameRegistry.addShapedRecipe(
                new ItemStack(ModBlocks.triple_craft, 1),
                "CCC",
                "CCC",
                "CCC",
                'C', new ItemStack(ModBlocks.double_craft)
        );
        GameRegistry.addShapedRecipe(
                new ItemStack(Blocks.CRAFTING_TABLE, 9),
                "C",
                'C', new ItemStack(ModBlocks.double_craft)
        );
        GameRegistry.addShapedRecipe(
                new ItemStack(ModBlocks.double_craft, 9),
                "C",
                'C', new ItemStack(ModBlocks.triple_craft)
        );
        GameRegistry.addShapedRecipe(
                new ItemStack(ModBlocks.dire_craft, 1),
                "CCC",
                "CXC",
                "CCC",
                'C', ModItems.crystal_matrix_ingot,
                'X', new ItemStack(ModBlocks.triple_craft)
        );

        if (ConfigHandler.craftingOnly) {
            return;
        }

        GameRegistry.addShapedRecipe(
                new ItemStack(ModBlocks.resource, 1, 2),
                "CCC",
                "CCC",
                "CCC",
                'C', ModItems.crystal_matrix_ingot
        );
        GameRegistry.addShapedRecipe(
                ItemUtils.copyStack(ModItems.crystal_matrix_ingot, 9),
                "C",
                'C', new ItemStack(ModBlocks.resource, 1, 2)
        );
        GameRegistry.addShapedRecipe(
                ModItems.neutron_nugget,
                "CCC",
                "CCC",
                "CCC",
                'C', ModItems.neutron_pile
        );
        GameRegistry.addShapedRecipe(
                ModItems.neutronium_ingot,
                "CCC",
                "CCC",
                "CCC",
                'C', ModItems.neutron_nugget
        );
        GameRegistry.addShapedRecipe(
                ItemUtils.copyStack(ModItems.neutron_nugget, 9),
                "C",
                'C', ModItems.neutronium_ingot
        );
        GameRegistry.addShapedRecipe(
                ItemUtils.copyStack(ModItems.neutron_pile, 9),
                "C",
                'C', ModItems.neutron_nugget
        );
        GameRegistry.addShapedRecipe(
                new ItemStack(ModBlocks.resource, 1, 0),
                "CCC",
                "CCC",
                "CCC",
                'C', ModItems.neutronium_ingot
        );
        GameRegistry.addShapedRecipe(
                ItemUtils.copyStack(ModItems.neutronium_ingot, 9),
                "C",
                'C', new ItemStack(ModBlocks.resource, 1, 0)
        );
        GameRegistry.addShapedRecipe(
                new ItemStack(ModBlocks.resource, 1, 1),
                "CCC",
                "CCC",
                "CCC",
                'C', ModItems.infinity_ingot
        );
        GameRegistry.addShapedRecipe(
                ItemUtils.copyStack(ModItems.infinity_ingot, 9),
                "C",
                'C', new ItemStack(ModBlocks.resource, 1, 1)
        );

        ExtremeCraftingManager.getInstance().addRecipe(
                new ItemStack(ModBlocks.neutron_collector, 1),
                "IIQQQQQII",
                "I QQQQQ I",
                "I  RRR  I",
                "X RRRRR X",
                "I RRXRR I",
                "X RRRRR X",
                "I  RRR  I",
                "I       I",
                "IIIXIXIII",
                'X', ModItems.crystal_matrix_ingot,
                'I', new ItemStack(Blocks.IRON_BLOCK, 1),
                'Q', new ItemStack(Blocks.QUARTZ_BLOCK, 1),
                'R', new ItemStack(Blocks.REDSTONE_BLOCK, 1)
        );

        ExtremeCraftingManager.getInstance().addRecipe(
                ModItems.infinity_ingot,
                "NNNNNNNNN",
                "NCXXCXXCN",
                "NXCCXCCXN",
                "NCXXCXXCN",
                "NNNNNNNNN",
                'C', ModItems.crystal_matrix_ingot,
                'N', ModItems.neutronium_ingot,
                'X', ModItems.infinity_catalyst
        );

        catalyst = ExtremeCraftingManager.getInstance().addShapelessOreRecipe(
                ModItems.infinity_catalyst,
                new ItemStack(Blocks.EMERALD_BLOCK, 1),
                ModItems.diamond_lattice,
                ModItems.crystal_matrix_ingot,
                ModItems.neutron_pile,
                ModItems.neutron_nugget,
                ModItems.neutronium_ingot,
                ModItems.ultimate_stew,
                ModItems.cosmic_meatballs,
                ModItems.endest_pearl,
                ModItems.ironSingularity,
                ModItems.goldSingularity,
                ModItems.lapisSingularity,
                ModItems.redstoneSingularity,
                ModItems.quartzSingularity
        );

        ItemStack result = new ItemStack(ModItems.infinity_pickaxe, 1);
        result.addEnchantment(Enchantments.FORTUNE, 10);

        ExtremeCraftingManager.getInstance().addRecipe(
                result,
                " IIIIIII ",
                "IIIICIIII",
                "II  N  II",
                "    N    ",
                "    N    ",
                "    N    ",
                "    N    ",
                "    N    ",
                "    N    ",
                'I', ModItems.infinity_ingot,
                'C', new ItemStack(ModBlocks.resource, 1, 2),
                'N', ModItems.neutronium_ingot
        );

        ExtremeCraftingManager.getInstance().addRecipe(
                new ItemStack(ModItems.infinity_sword),
                "       II",
                "      III",
                "     III ",
                "    III  ",
                " C III   ",
                "  CII    ",
                "  NC     ",
                " N  C    ",
                "X        ",
                'I', ModItems.infinity_ingot,
                'X', ModItems.infinity_catalyst,
                'C', ModItems.crystal_matrix_ingot,
                'N', ModItems.neutronium_ingot
        );

        ExtremeCraftingManager.getInstance().addRecipe(
                new ItemStack(ModItems.infinity_shovel),
                "      III",
                "     IIXI",
                "      III",
                "     N I ",
                "    N    ",
                "   N     ",
                "  N      ",
                " N       ",
                "N        ",
                'I', ModItems.infinity_ingot,
                'X', new ItemStack(ModBlocks.resource, 1, 1),
                'N', ModItems.neutronium_ingot
        );

        ExtremeCraftingManager.getInstance().addRecipe(
                new ItemStack(ModItems.infinity_axe),
                "   I     ",
                "  IIIII  ",
                "   IIII  ",
                "     IN  ",
                "      N  ",
                "      N  ",
                "      N  ",
                "      N  ",
                "      N  ",
                'I', ModItems.infinity_ingot,
                'N', ModItems.neutronium_ingot
        );

        ExtremeCraftingManager.getInstance().addRecipe(
                new ItemStack(ModItems.infinity_bow),
                "   II    ",
                "  I W    ",
                " I  W    ",
                "I   W    ",
                "X   W    ",
                "I   W    ",
                " I  W    ",
                "  I W    ",
                "   II    ",
                'I', ModItems.infinity_ingot,
                'X', new ItemStack(ModBlocks.resource, 1, 2),
                'W', new ItemStack(Blocks.WOOL, 1, 0)
        );

        ExtremeCraftingManager.getInstance().addRecipe(
                new ItemStack(ModItems.infinity_chestplate),
                " NN   NN ",
                "NNN   NNN",
                "NNN   NNN",
                " NIIIIIN ",
                " NIIXIIN ",
                " NIIIIIN ",
                " NIIIIIN ",
                " NIIIIIN ",
                "  NNNNN  ",
                'I', ModItems.infinity_ingot,
                'X', new ItemStack(ModBlocks.resource, 1, 2),
                'N', ModItems.neutronium_ingot
        );

        ExtremeCraftingManager.getInstance().addRecipe(
                new ItemStack(ModItems.infinity_helmet),
                "  NNNNN  ",
                " NIIIIIN ",
                " N XIX N ",
                " NIIIIIN ",
                " NIIIIIN ",
                " NI I IN ",
                'I', ModItems.infinity_ingot,
                'X', ModItems.infinity_catalyst,
                'N', ModItems.neutronium_ingot
        );

        ExtremeCraftingManager.getInstance().addRecipe(
                new ItemStack(ModItems.infinity_pants),
                "NNNNNNNNN",
                "NIIIXIIIN",
                "NINNXNNIN",
                "NIN   NIN",
                "NCN   NCN",
                "NIN   NIN",
                "NIN   NIN",
                "NIN   NIN",
                "NNN   NNN",
                'I', ModItems.infinity_ingot,
                'X', ModItems.infinity_catalyst,
                'C', new ItemStack(ModBlocks.resource, 1, 2),
                'N', ModItems.neutronium_ingot
        );

        ExtremeCraftingManager.getInstance().addRecipe(
                new ItemStack(ModItems.infinity_boots),
                " NNN NNN ",
                " NIN NIN ",
                " NIN NIN ",
                "NNIN NINN",
                "NIIN NIIN",
                "NNNN NNNN",
                'I', ModItems.infinity_ingot,
                'N', ModItems.neutronium_ingot
        );

        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(
                new ItemStack(ModItems.skull_sword),
                "       IX",
                "      IXI",
                "     IXI ",
                "    IXI  ",
                " B IXI   ",
                "  BXI    ",
                "  WB     ",
                " W  B    ",
                "D        ",
                'I', ModItems.crystal_matrix_ingot,
                'X', new ItemStack(Items.BLAZE_POWDER),
                'B', new ItemStack(Items.BONE),
                'D', new ItemStack(Items.NETHER_STAR),
                'W', "logWood"
        );

        CompressorManager.addRecipe(ModItems.ironSingularity, 400, new ItemStack(Blocks.IRON_BLOCK));
        CompressorManager.addRecipe(ModItems.goldSingularity, 200, new ItemStack(Blocks.GOLD_BLOCK, 1));
        CompressorManager.addRecipe(ModItems.lapisSingularity, 400, new ItemStack(Blocks.LAPIS_BLOCK, 1));
        CompressorManager.addRecipe(ModItems.redstoneSingularity, 500, new ItemStack(Blocks.REDSTONE_BLOCK, 1));
        CompressorManager.addRecipe(ModItems.diamondSingularity, 300, new ItemStack(Blocks.DIAMOND_BLOCK, 1));
        CompressorManager.addRecipe(ModItems.emeraldSingularity, 200, new ItemStack(Blocks.EMERALD_BLOCK, 1));

        if (ConfigHandler.endStone) {
            ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(
                    new ItemStack(ModItems.endest_pearl),
                    "   EEE   ",
                    " EEPPPEE ",
                    " EPPPPPE ",
                    "EPPPNPPPE",
                    "EPPNSNPPE",
                    "EPPPNPPPE",
                    " EPPPPPE ",
                    " EEPPPEE ",
                    "   EEE   ",
                    'E', new ItemStack(Blocks.END_STONE),
                    'P', new ItemStack(Items.ENDER_PEARL),
                    'S', new ItemStack(Items.NETHER_STAR),
                    'N', ModItems.neutronium_ingot
            );
        } else {
            ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(
                    new ItemStack(ModItems.endest_pearl),
                    "   EEE   ",
                    " EEPPPEE ",
                    " EPPPPPE ",
                    "EPPPNPPPE",
                    "EPPNSNPPE",
                    "EPPPNPPPE",
                    " EPPPPPE ",
                    " EEPPPEE ",
                    "   EEE   ",
                    'P', new ItemStack(Items.ENDER_PEARL),
                    'S', new ItemStack(Items.NETHER_STAR),
                    'N', ModItems.neutronium_ingot
            );
        }

        ExtremeCraftingManager.getInstance().addRecipe(
                new ItemStack(ModBlocks.neutronium_compressor),
                "IIIHHHIII",
                "X N   N X",
                "I N   N I",
                "X N   N X",
                "RNN O NNR",
                "X N   N X",
                "I N   N I",
                "X N   N X",
                "IIIXIXIII",
                'X', ModItems.crystal_matrix_ingot,
                'N', ModItems.neutronium_ingot,
                'I', new ItemStack(Blocks.IRON_BLOCK, 1),
                'H', new ItemStack(Blocks.HOPPER, 1),
                'R', new ItemStack(Blocks.REDSTONE_BLOCK, 1),
                'O', new ItemStack(ModBlocks.resource, 1, 0)
        );
        //@formatter:on
    }

    public static void initRecipeCompat() {
        //if (Compat.mfr) {
        //    catalyst.getInput().add(OreDictionary.getOres("record"));
        //} else {
        catalyst.getInput().add(ModItems.record_fragment);
        IRecipe smashysmashy;
        //if (Compat.botan) {
        //	smashysmashy = new ShapelessOreRecipe(new ItemStack(LudicrousItems.resource, 4, 7), "record");
        //} else {
        smashysmashy = new ShapelessOreRecipe(ItemUtils.copyStack(ModItems.record_fragment, 8), "record");
        //}
        GameRegistry.addRecipe(smashysmashy);
        //}

        if (ConfigHandler.copper && !OreDictionary.getOres("blockCopper").isEmpty()) {
            CompressorManager.addOreRecipe(ModItems.copperSingularity, 400, "blockCopper");
            catalyst.getInput().add(ModItems.copperSingularity);
        }
        if (ConfigHandler.tin && !OreDictionary.getOres("blockTin").isEmpty()) {
            CompressorManager.addOreRecipe(ModItems.tinSingularity, 400, "blockTin");
            catalyst.getInput().add(ModItems.tinSingularity);
        }
        if (ConfigHandler.lead && !OreDictionary.getOres("blockLead").isEmpty()) {
            CompressorManager.addOreRecipe(ModItems.leadSingularity, 300, "blockLead");
            catalyst.getInput().add(ModItems.leadSingularity);
        }
        if (ConfigHandler.silver && !OreDictionary.getOres("blockSilver").isEmpty()) {
            CompressorManager.addOreRecipe(ModItems.silverSingularity, 300, "blockSilver");
            catalyst.getInput().add(ModItems.silverSingularity);
        }
        if (ConfigHandler.nickel && !OreDictionary.getOres("blockNickel").isEmpty()) {
            CompressorManager.addOreRecipe(ModItems.nickelSingularity, 400, "blockNickel");
            catalyst.getInput().add(ModItems.nickelSingularity);
        }
        //if (ConfigHandler.diamond && !OreDictionary.getOres("blockDiamond").isEmpty()) {
        //    CompressorManager.addOreRecipe(ModItems.diamondSingularity, 200, "blockDiamond");
        //    catalyst.getInput().add(ModItems.diamondSingularity);
        //}
        //if (ConfigHandler.emerald && !OreDictionary.getOres("blockEmerald").isEmpty()) {
        //    CompressorManager.addOreRecipe(ModItems.emeraldSingularity, 400, "blockEmerald");
        //    catalyst.getInput().add(ModItems.emeraldSingularity);
        //}
        //if (ConfigHandler.te && !OreDictionary.getOres("blockEnderium").isEmpty()) {
        //    catalyst.getInput().add(OreDictionary.getOres("blockEnderium"));
        //}
        //if (ConfigHandler.steel && !OreDictionary.getOres("blockSteel").isEmpty()) {
        //    catalyst.getInput().add(OreDictionary.getOres("blockSteel"));
        //}
        //if (ConfigHandler.metallurgy && !OreDictionary.getOres("ingotTartarite").isEmpty()) {
        //    catalyst.getInput().add(OreDictionary.getOres("ingotTartarite"));
        //}
        //if (ConfigHandler.numanuma && !OreDictionary.getOres("blockIronCompressed").isEmpty()) {
        //    catalyst.getInput().add(OreDictionary.getOres("blockIronCompressed"));
        //}
        //if (ConfigHandler.enderio && !OreDictionary.getOres("blockDarkSteel").isEmpty()) {
        //    catalyst.getInput().add(OreDictionary.getOres("blockDarkSteel"));
        //}
    }

}
