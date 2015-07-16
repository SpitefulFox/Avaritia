package fox.spiteful.avaritia.items.tools;

import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemSwordSkulls extends ItemSword {

    public ItemSwordSkulls(){
        super(ToolMaterial.EMERALD);
        setUnlocalizedName("skullfire_sword");
        setTextureName("avaritia:skull_sword");
        setCreativeTab(Avaritia.tab);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.epic;
    }
}
