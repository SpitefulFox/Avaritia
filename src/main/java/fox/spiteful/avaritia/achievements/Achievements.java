package fox.spiteful.avaritia.achievements;

import cpw.mods.fml.common.FMLCommonHandler;
import fox.spiteful.avaritia.blocks.LudicrousBlocks;
import fox.spiteful.avaritia.compat.Compat;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class Achievements {

    public static AchievementPage page;

    public static Achievement crystal_matrix;
    public static Achievement dire_crafting;
    public static Achievement collector;
    public static Achievement neutronium;
    public static Achievement singularity;
    public static Achievement catalyst;
    public static Achievement infinity;
    public static Achievement armok;

    public static void achieve(){
        crystal_matrix = new LudicrousAchievement("crystal_matrix", 0, 0, new ItemStack(LudicrousItems.resource, 1, 1), null);
        dire_crafting = new LudicrousAchievement("dire_crafting", 1, 1, new ItemStack(LudicrousBlocks.dire_crafting), crystal_matrix);
        collector = new LudicrousAchievement("collector", 2, 2, new ItemStack(LudicrousBlocks.neutron_collector), dire_crafting);
        neutronium = new LudicrousAchievement("neutronium", 4, 1, new ItemStack(LudicrousItems.resource, 1, 4), collector);
        singularity = new LudicrousAchievement("singularity", 6, 3, new ItemStack(LudicrousItems.singularity, 1, 0), neutronium);
        catalyst = new LudicrousAchievement("catalyst", 6, -3, new ItemStack(LudicrousItems.resource, 1, 5), singularity).setSpecial();
        infinity = new LudicrousAchievement("infinity", 4, -6, new ItemStack(LudicrousItems.resource, 1, 6), neutronium).setSpecial();

        if(Compat.blood)
            armok = new LudicrousAchievement("armok", 2, -5, LudicrousItems.armok_orb, infinity);

        page = new AchievementPage("Avaritia", LudicrousAchievement.achievements.toArray(new Achievement[LudicrousAchievement.achievements.size()]));
        AchievementPage.registerAchievementPage(page);

        FMLCommonHandler.instance().bus().register(new AchievementTrigger());
    }
}
