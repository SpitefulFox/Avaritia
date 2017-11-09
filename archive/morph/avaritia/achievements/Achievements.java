package morph.avaritia.achievements;

import morph.avaritia.init.ModBlocks;
import morph.avaritia.init.ModItems;
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
    //public static Achievement armok;

    public static Achievement creative_kill;

    public static void load() {
        crystal_matrix = new AvaritiaAchievement("crystal_matrix", 0, 0, ModItems.crystal_matrix_ingot, null);
        dire_crafting = new AvaritiaAchievement("dire_crafting", 1, 1, new ItemStack(ModBlocks.dire_craft), crystal_matrix);
        collector = new AvaritiaAchievement("collector", 2, 2, new ItemStack(ModBlocks.neutron_collector), dire_crafting);
        neutronium = new AvaritiaAchievement("neutronium", 4, 1, ModItems.neutronium_ingot, collector);
        singularity = new AvaritiaAchievement("singularity", 6, 3, new ItemStack(ModItems.singularity, 1, 0), neutronium);
        catalyst = new AvaritiaAchievement("catalyst", 6, -3, ModItems.infinity_catalyst, singularity).setSpecial();
        infinity = new AvaritiaAchievement("infinity", 4, -6, ModItems.infinity_ingot, neutronium).setSpecial();
        creative_kill = new AvaritiaAchievement("creative_kill", -6, -6, new ItemStack(Items.SKULL, 1, 3), null).setSpecial();
        dire_uncrafting = new AvaritiaAchievement("dire_uncrafting", -1, 3, new ItemStack(Items.DIAMOND_PICKAXE), dire_crafting);

        //if(Compat.blood)
        //    armok = new LudicrousAchievement("armok", 2, -5, LudicrousItems.armok_orb, infinity);

        page = new AchievementPage("Avaritia", AvaritiaAchievement.achievements.toArray(new Achievement[AvaritiaAchievement.achievements.size()]));
        AchievementPage.registerAchievementPage(page);

        AchievementTrigger tigger = new AchievementTrigger();
        MinecraftForge.EVENT_BUS.register(tigger);
    }
}
