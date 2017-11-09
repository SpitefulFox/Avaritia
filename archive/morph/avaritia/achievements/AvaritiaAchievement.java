package morph.avaritia.achievements;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

import java.util.ArrayList;
import java.util.List;

public class AvaritiaAchievement extends Achievement {

    public static List<Achievement> achievements = new ArrayList<>();

    public AvaritiaAchievement(String name, int x, int y, ItemStack icon, Achievement parent) {
        super("achievement.avaritia:" + name, "avaritia:" + name, x, y, icon, parent);
        achievements.add(this);
        registerStat();
    }

    public AvaritiaAchievement(String name, int x, int y, Item icon, Achievement parent) {
        this(name, x, y, new ItemStack(icon), parent);
    }

    public AvaritiaAchievement(String name, int x, int y, Block icon, Achievement parent) {
        this(name, x, y, new ItemStack(icon), parent);
    }
}
