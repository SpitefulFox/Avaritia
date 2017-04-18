package morph.avaritia.item;

import morph.avaritia.Avaritia;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemMatrixIngot extends Item {

    public ItemMatrixIngot() {
        setMaxDamage(0);
        setCreativeTab(Avaritia.tab);
        setRegistryName("matrix_ingot");
        setUnlocalizedName("matrix_ingot");
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }
}
