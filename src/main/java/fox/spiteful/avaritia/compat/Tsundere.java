package fox.spiteful.avaritia.compat;

import cpw.mods.fml.common.registry.GameRegistry;
import fox.spiteful.avaritia.blocks.LudicrousBlocks;
import fox.spiteful.avaritia.compat.tsundere.BlockInfinitato;
import fox.spiteful.avaritia.compat.tsundere.TileInfinitato;
import fox.spiteful.avaritia.crafting.ExtremeCraftingManager;
import fox.spiteful.avaritia.crafting.Grinder;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.botania.api.BotaniaAPI;

public class Tsundere {

	public static Block potato;
	public static Block infinitato;
	
    public static void baka() throws Compat.ItemNotFoundException {
        Item resource = Compat.getItem("Botania", "manaResource");
        ItemStack gaia = new ItemStack(resource, 1, 14);
        GameRegistry.addShapedRecipe(new ItemStack(LudicrousBlocks.resource_block, 1, 2), new Object[]{
                "XXX",
                "XXX",
                "XXX",
                'X', gaia
        });
        GameRegistry.addShapedRecipe(new ItemStack(resource, 9, 14), new Object[]{
                "X", 'X', new ItemStack(LudicrousBlocks.resource_block, 1, 2)
        });
        Grinder.catalyst.recipeItems.add(new ItemStack(LudicrousBlocks.resource_block, 1, 2));

        BotaniaAPI.registerSubTile("asgardandelion", SubTileCheaty.class);
        BotaniaAPI.registerSubTileSignature(SubTileCheaty.class, new Signature("asgardandelion"));
        BotaniaAPI.addSubTileToCreativeMenu("asgardandelion");

        ItemStack cheaty = getFlower("asgardandelion");

        SubTileCheaty.lexicon = new LudicrousLexicon("asgardandelion", BotaniaAPI.categoryGenerationFlowers);
        SubTileCheaty.lexicon.addPage(BotaniaAPI.internalHandler.textPage("avaritia.lexicon.asgardandelion.0"));
        SubTileCheaty.lexicon.setIcon(cheaty);

        ExtremeCraftingManager.getInstance().addRecipe(cheaty, new Object[]{
                "   III   ",
                "  IIIII  ",
                "  IIXII  ",
                "  IIIII  ",
                "   III   ",
                " nn N nn ",
                "nnnnNnnnn",
                " nn N nn ",
                "    N    ",
                'I', new ItemStack(LudicrousItems.resource, 1, 6),
                'X', new ItemStack(LudicrousItems.resource, 1, 5),
                'N', new ItemStack(LudicrousItems.resource, 1, 4),
                'n', new ItemStack(LudicrousItems.resource, 1, 3),
        });
        
        infinitato = new BlockInfinitato();
        GameRegistry.registerBlock(infinitato, "infinitato");
        GameRegistry.registerTileEntity(TileInfinitato.class, "Avaritia_Infinitato");
        
        potato = Compat.getBlock("Botania", "tinyPotato");
        
        ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(infinitato), new Object[]{
                "IIIIIIIII",
                "IIIIIIIII",
                "IIISISIII",
                "IIIIIIIII",
                "IISIXISII",
                "IIISSSIII",
                "IIIIIIIII",
                "IIIIIIIII",
                "IIIIIIIII",
                'I', new ItemStack(potato),
                'X', new ItemStack(LudicrousItems.resource, 1, 5),
                'S', new ItemStack(Items.diamond)});
    }

    private static ItemStack getFlower(String type) throws Compat.ItemNotFoundException {
        Item specialFlower = Compat.getItem("Botania", "specialFlower");
        ItemStack flower = new ItemStack(specialFlower, 1, 0);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", type);
        flower.setTagCompound(tag);
        return flower;
    }

}
