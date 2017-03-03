package fox.spiteful.avaritia.compat.thaumcraft;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LudicrousResearchItem extends ResearchItem {

    public LudicrousResearchItem(String par1, String x) {
        super(par1, x);
    }

    public LudicrousResearchItem(String par1, String x, AspectList tags, int y, int z, int par5, ResourceLocation icon) {
        super(par1, x, tags, y, z, par5, icon);
    }

    public LudicrousResearchItem(String par1, String x, AspectList tags, int y, int z, int par5, ItemStack icon) {
        super(par1, x, tags, y, z, par5, icon);
    }

    @SideOnly(Side.CLIENT)
    public String getName() {
        return StatCollector.translateToLocal("avaritia.research_name." + key);
    }

    @SideOnly(Side.CLIENT)
    public String getText() {
        return "[AV] " + StatCollector.translateToLocal("avaritia.research_text." + key);
    }
}