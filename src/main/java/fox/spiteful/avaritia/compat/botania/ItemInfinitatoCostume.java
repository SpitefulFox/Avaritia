package fox.spiteful.avaritia.compat.botania;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.Avaritia;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemInfinitatoCostume extends Item {

    private static final String[] types = new String[]{"armstrong", "moon", "egbert", "francis"};

    @SideOnly(Side.CLIENT)
    public IIcon[] icons;

    public ItemInfinitatoCostume(){
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        icons = new IIcon[types.length];

        for (int x = 0; x < types.length; x++) {
            icons[x] = ir.registerIcon("avaritia:" + "costume_" + types[x]);
        }

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {}

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int dam) {
        return this.icons[dam % icons.length];
    }
}
