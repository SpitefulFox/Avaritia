package fox.spiteful.avaritia.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.LudicrousText;
import fox.spiteful.avaritia.render.IHaloRenderItem;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemSingularity extends Item implements IHaloRenderItem {

    public static final String[] types = new String[]{"iron", "gold", "lapis", "redstone", "quartz", "copper",
        "tin", "lead", "silver", "nickel", "clay"};
    public static final int[] colors = new int[]{0xBFBFBF, 0xE8EF23, 0x5a82e2, 0xDF0000, 0xeeebe6, 0xE47200,
        0xA5C7DE, 0x444072, 0xF9F9F9, 0xDEE187, 0x8890AD};
    public static final int[] colors2 = new int[]{0x7F7F7F, 0xdba213, 0x224baf, 0x900000, 0x94867d, 0x89511A,
        0x9BA9B2, 0x3E3D4E, 0xD5D5D5, 0xC4C698, 0x666B7F};
    public static IIcon background;
    public static IIcon foreground;

    public ItemSingularity(){
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setUnlocalizedName("avaritia_singularity");
        this.setTextureName("avaritia:singularity");
        this.setCreativeTab(Avaritia.tab);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getColorFromItemStack(ItemStack itemstack, int renderpass) {
    	//System.out.println(foreground);
        return renderpass == 0 ? colors2[itemstack.getItemDamage() % colors.length] : colors[itemstack.getItemDamage() % colors2.length];
    	//return 0xFFFFFF;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int i = MathHelper.clamp_int(stack.getItemDamage(), 0, types.length);
        return "item.singularity_" + types[i];
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int j = 0; j < types.length; ++j) {
            list.add(new ItemStack(item, 1, j));
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister ir) {
    	foreground = ir.registerIcon("avaritia:singularity");
    	background = ir.registerIcon("avaritia:singularity2");
    }
    
    @Override
    public IIcon getIcon(ItemStack stack, int pass)
    {
    	if (pass == 0) { return background; }
    	return foreground;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        if(stack.getItemDamage() == 10)
            return LudicrousItems.cosmic;
        return EnumRarity.uncommon;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List tooltip, boolean wut) {

        int meta = item.getItemDamage();
        if (meta == 10) {
            //tooltip.add(EnumChatFormatting.DARK_GRAY +""+ EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.claybalance.desc"));
            tooltip.add(LudicrousText.makeFabulous(StatCollector.translateToLocal("tooltip.claybalance.desc")));
        }
    }

	@Override
	@SideOnly(Side.CLIENT)
	public boolean drawHalo(ItemStack stack) {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getHaloTexture(ItemStack stack) {
		return ((ItemResource)LudicrousItems.resource).halo[0];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getHaloSize(ItemStack stack) {
		if(stack.getItemDamage() == 10)
            return 8;
        return 4;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean drawPulseEffect(ItemStack stack) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getHaloColour(ItemStack stack) {
		return 0xFF000000;
	}
}
