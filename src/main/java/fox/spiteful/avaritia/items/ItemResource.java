package fox.spiteful.avaritia.items;

import net.minecraft.item.Item;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

public class ItemResource extends Item {

    private static final String[] types = new String[]{"diamond_lattice", "crystal_matrix_ingot", "neutron_pile",
            "neutron_nugget", "neutronium_ingot", "infinity_catalyst"};

    @SideOnly(Side.CLIENT)
    public IIcon[] icons;
    @SideOnly(Side.CLIENT)
    public IIcon[] halo;

    public ItemResource(){
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setUnlocalizedName("avaritia_resource");
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        icons = new IIcon[types.length];

        for (int x = 0; x < types.length; x++) {
            icons[x] = ir.registerIcon("avaritia:" + "resource_" + types[x]);
        }

        halo = new IIcon[2];
        halo[0] = ir.registerIcon("avaritia:halo");
        halo[1] = ir.registerIcon("avaritia:halonoise");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int dam) {
        return this.icons[dam % icons.length];
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int i = MathHelper.clamp_int(stack.getItemDamage(), 0, types.length);
        return super.getUnlocalizedName() + "." + types[i];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int j = 0; j < types.length; ++j) {
            list.add(new ItemStack(item, 1, j));
        }
    }
}
