package fox.spiteful.avaritia.achievements;

import cpw.mods.fml.common.FMLCommonHandler;
import fox.spiteful.avaritia.blocks.LudicrousBlocks;
import fox.spiteful.avaritia.compat.Compat;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;

public class Achievements {

    public static AchievementPage page;

    public static Achievement crystal_matrix;
    public static Achievement dire_crafting;
    public static Achievement dire_uncrafting;
    public static Achievement collector;
    public static Achievement neutronium;
    public static Achievement singularity;
    public static Achievement catalyst;
    public static Achievement infinity;
    public static Achievement armok;

    public static Achievement creative_kill;

    public static void achieve(){
        crystal_matrix = new LudicrousAchievement("crystal_matrix", 0, 0, new ItemStack(LudicrousItems.resource, 1, 1), null);
        dire_crafting = new LudicrousAchievement("dire_crafting", 1, 1, new ItemStack(LudicrousBlocks.dire_crafting), crystal_matrix);
        collector = new LudicrousAchievement("collector", 2, 2, new ItemStack(LudicrousBlocks.neutron_collector), dire_crafting);
        neutronium = new LudicrousAchievement("neutronium", 4, 1, new ItemStack(LudicrousItems.resource, 1, 4), collector);
        singularity = new LudicrousAchievement("singularity", 6, 3, new ItemStack(LudicrousItems.singularity, 1, 0), neutronium);
        catalyst = new LudicrousAchievement("catalyst", 6, -3, new ItemStack(LudicrousItems.resource, 1, 5), singularity).setSpecial();
        infinity = new LudicrousAchievement("infinity", 4, -6, new ItemStack(LudicrousItems.resource, 1, 6), neutronium).setSpecial();
        creative_kill = new LudicrousAchievement("creative_kill", -6, -6, new ItemStack(Items.skull, 1, 3), null).setSpecial();
        dire_uncrafting = new LudicrousAchievement("dire_uncrafting", -1, 3, new ItemStack(Items.diamond_pickaxe), dire_crafting);

        if(Compat.blood)
            armok = new LudicrousAchievement("armok", 2, -5, LudicrousItems.armok_orb, infinity);

        page = new AchievementPage("Avaritia", LudicrousAchievement.achievements.toArray(new Achievement[LudicrousAchievement.achievements.size()]));
        AchievementPage.registerAchievementPage(page);

        AchievementTrigger tigger = new AchievementTrigger();
        FMLCommonHandler.instance().bus().register(tigger);
        MinecraftForge.EVENT_BUS.register(tigger);
    }
}
