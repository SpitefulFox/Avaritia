package fox.spiteful.avaritia.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.entity.EntityEndestPearl;
import fox.spiteful.avaritia.render.IHaloRenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemEndestPearl extends ItemEnderPearl implements IHaloRenderItem {

	public ItemEndestPearl() {
		this.setUnlocalizedName("avaritia_endest_pearl");
		this.setTextureName("avaritia:endestpearl");
		this.maxStackSize = 16;
		this.setCreativeTab(Avaritia.tab);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (!player.capabilities.isCreativeMode)
        {
        	--stack.stackSize;
        }

        world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote)
        {
            world.spawnEntityInWorld(new EntityEndestPearl(world, player));
        }

        return stack;
    }
	
	@Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.rare;
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
		return 4;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean drawPulseEffect(ItemStack stack) {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getHaloColour(ItemStack stack) {
		return 0xFF000000;
	}
}
