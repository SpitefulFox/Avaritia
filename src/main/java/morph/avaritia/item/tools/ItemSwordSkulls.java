package morph.avaritia.item.tools;

import morph.avaritia.Avaritia;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import java.util.List;

public class ItemSwordSkulls extends ItemSword {

    public ItemSwordSkulls() {
        super(ToolMaterial.DIAMOND);
        setUnlocalizedName("avaritia:skullfire_sword");
        setRegistryName("skullfire_sword");
        setCreativeTab(Avaritia.tab);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @SuppressWarnings ({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.DARK_GRAY + "" + TextFormatting.ITALIC + I18n.translateToLocal("tooltip.skullfire_sword.desc"));
    }
}
