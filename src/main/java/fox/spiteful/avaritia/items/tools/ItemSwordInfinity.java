package fox.spiteful.avaritia.items.tools;

import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.items.LudicrousItems;
import fox.spiteful.avaritia.render.ICosmicRenderItem;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;

public class ItemSwordInfinity extends ItemSword implements ICosmicRenderItem {

    private static final ToolMaterial opSword = EnumHelper.addToolMaterial("INFINITY_SWORD", 32, 9999, 9999F, -3.0F, 200);
    private IIcon cosmicMask;
    
    public ItemSwordInfinity(){
        super(opSword);
        setUnlocalizedName("infinity_sword");
        //setTextureName("avaritia:infinity_sword");
        setTextureName("minecraft:diamond_sword");
        setCreativeTab(Avaritia.tab);
    }

    /*public Multimap getItemAttributeModifiers()
    {
        Multimap multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", (double)this.field_150934_a, 0));
        return multimap;
    }*/

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase victim, EntityLivingBase player)
    {
        if(player.worldObj.isRemote)
            return true;
        victim.func_110142_aN().func_94547_a(new EntityDamageSource("infinity", player), victim.getHealth(), victim.getHealth());
        victim.setHealth(0);
        victim.onDeath(new EntityDamageSource("infinity", player));
        return true;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return LudicrousItems.cosmic;
    }

    @Override
    public void setDamage(ItemStack stack, int damage){
        super.setDamage(stack, 0);
    }

	@Override
	public IIcon getMaskTexture(ItemStack stack) {
		return cosmicMask;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void registerIcons(IIconRegister ir) {
		super.registerIcons(ir);
		
		this.cosmicMask = ir.registerIcon("avaritia:infinity_sword_mask");
	}
}
