package fox.spiteful.avaritia.compat.forestry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.items.LudicrousItems;
import fox.spiteful.avaritia.render.IHaloRenderItem;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

import java.util.List;

public class ItemBeesource extends Item implements IHaloRenderItem {

    private static final String[] types = new String[]{"infinity_drop", "dust"};

    @SideOnly(Side.CLIENT)
    public IIcon[] icons;

    @SideOnly(Side.CLIENT)
    public IIcon halo;

    public ItemBeesource(){
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setUnlocalizedName("avaritia_beesource");
        this.setCreativeTab(Avaritia.tab);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        icons = new IIcon[types.length];

        for (int x = 0; x < types.length; x++) {
            icons[x] = ir.registerIcon("avaritia:" + "resource_" + types[x]);
        }

        halo = ir.registerIcon("avaritia:halo");
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int j = 0; j < types.length; ++j) {
            list.add(new ItemStack(item, 1, j));
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        switch(stack.getItemDamage()){
            case 0:
                return LudicrousItems.cosmic;
            case 1:
                return Ranger.trash;
            default:
                return EnumRarity.common;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean drawHalo(ItemStack stack) {
        int meta = stack.getItemDamage();
        return (meta == 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getHaloTexture(ItemStack stack) {
        return halo;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getHaloSize(ItemStack stack) {
        return 10;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean drawPulseEffect(ItemStack stack) {
        int meta = stack.getItemDamage();
        return meta == 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getHaloColour(ItemStack stack) {
        return 0xFF000000;
    }
}
