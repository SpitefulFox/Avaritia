package fox.spiteful.avaritia.compat.forestry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.Avaritia;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

import java.awt.*;
import java.util.*;

public class ItemComb extends Item {

    private static final String[] types = new String[]{"nerfed", "cosmic"};
    private static final int[] mainColors = new int[]{0xB6B6B6, 0xEF57FF};
    private static final int[] otherColors = new int[]{0x757575, 0x06005C};

    @SideOnly(Side.CLIENT)
    private IIcon secondIcon;

    public ItemComb(){
        setCreativeTab(Avaritia.tab);
        setHasSubtypes(true);
        setMaxDamage(0);
        setUnlocalizedName("avaritia.comb");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, java.util.List list) {
        for (int j = 0; j < types.length; ++j) {
            list.add(new ItemStack(item, 1, j));
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon("forestry:beeCombs.0");
        this.secondIcon = par1IconRegister.registerIcon("forestry:beeCombs.1");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass)
    {
        return (pass == 0) ? itemIcon : secondIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    public int getRenderPasses(int meta)
    {
        return 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass)
    {

        if (pass == 0)
        {
            if(stack.getItemDamage() == 1){
                int hue = (int) (System.currentTimeMillis() >> 2) % 360;
                return Color.getHSBColor(hue / 360f, 0.75f, 0.80f).getRGB();
            }
            return mainColors[stack.getItemDamage() % mainColors.length];
        }

        return otherColors[stack.getItemDamage() % otherColors.length];
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int i = MathHelper.clamp_int(stack.getItemDamage(), 0, types.length);
        return super.getUnlocalizedName() + "." + types[i];
    }

}
