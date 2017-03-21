package fox.spiteful.avaritia.items;

import fox.spiteful.avaritia.Avaritia;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemMatrixIngot extends Item {


    public ItemMatrixIngot(){
        this.setMaxDamage(0);
        this.setCreativeTab(Avaritia.tab);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.EPIC;
    }
}
