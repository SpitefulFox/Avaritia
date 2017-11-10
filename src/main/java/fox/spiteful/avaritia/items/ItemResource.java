package fox.spiteful.avaritia.items;

import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.entity.EntityImmortalItem;
import fox.spiteful.avaritia.render.IHaloRenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class ItemResource extends Item implements IHaloRenderItem {

    private static final String[] types = new String[]{"diamond_lattice", "crystal_matrix_ingot", "neutron_pile",
            "neutron_nugget", "neutronium_ingot", "infinity_catalyst", "infinity_ingot", "record_fragment", "starfuel"};

    @SideOnly(Side.CLIENT)
    public IIcon[] icons;
    @SideOnly(Side.CLIENT)
    public IIcon[] halo;

    public ItemResource(){
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setUnlocalizedName("avaritia_resource");
        this.setCreativeTab(Avaritia.tab);
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void addInformation(ItemStack item, EntityPlayer player, List tooltip, boolean wut) {

    	int meta = item.getItemDamage();
    	if (meta != 0 && meta != 8) {
    		tooltip.add(EnumChatFormatting.DARK_GRAY +""+ EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip."+types[meta]+".desc"));
    	}
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
            case 2:
            case 3:
                return EnumRarity.uncommon;
            case 1:
            case 4:
            case 8:
                return EnumRarity.rare;
            case 5:
                return EnumRarity.epic;
            case 6:
                return LudicrousItems.cosmic;
            default:
                return EnumRarity.common;
        }
    }

	@Override
	@SideOnly(Side.CLIENT)
	public boolean drawHalo(ItemStack stack) {
		int meta = stack.getItemDamage();
		return (meta >= 2 && meta <= 6) || meta == 8;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getHaloTexture(ItemStack stack) {
		int meta = stack.getItemDamage();
		if (meta == 2 || meta == 3 || meta == 4) {
			return halo[1];
		}
		return halo[0];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getHaloSize(ItemStack stack) {
		int meta = stack.getItemDamage();
		switch(meta) {
		case 5:
		case 6:
			return 10;
		}
		return 8;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean drawPulseEffect(ItemStack stack) {
		int meta = stack.getItemDamage();
		return meta == 5 || meta == 6;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getHaloColour(ItemStack stack) {
		int meta = stack.getItemDamage();
		if (meta == 2) {
			return 0x33FFFFFF;
		}
		if (meta == 3) {
			return 0x4DFFFFFF;
		}
		if (meta == 4) {
			return 0x99FFFFFF;
		}
		return 0xFF000000;
	}
	
	@Override
    public boolean hasCustomEntity (ItemStack stack)
    {
		int meta = stack.getItemDamage();
        return meta == 5 || meta == 6;
    }

    @Override
    public Entity createEntity (World world, Entity location, ItemStack itemstack)
    {
    	int meta = itemstack.getItemDamage();
        return (meta == 5 || meta == 6) ? new EntityImmortalItem(world, location, itemstack) : null;
    }
}
