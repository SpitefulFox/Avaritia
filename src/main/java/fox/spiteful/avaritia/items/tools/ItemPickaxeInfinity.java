package fox.spiteful.avaritia.items.tools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.EnumHelper;

import java.util.List;

public class ItemPickaxeInfinity extends ItemPickaxe {

    private static final ToolMaterial opPickaxe = EnumHelper.addToolMaterial("INFINITY_PICKAXE", 32, 9999, 9999F, 5.0F, 200);
    private IIcon hammer;

    public ItemPickaxeInfinity(){
        super(opPickaxe);
        setUnlocalizedName("infinity_pickaxe");
        setCreativeTab(Avaritia.tab);
    }

    @Override
    public void setDamage(ItemStack stack, int damage){
        super.setDamage(stack, 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        ItemStack pick = new ItemStack(this);
        pick.addEnchantment(Enchantment.fortune, 10);
        list.add(pick);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return LudicrousItems.cosmic;
    }

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta)
    {
        if (ForgeHooks.isToolEffective(stack, block, meta))
        {
            return efficiencyOnProperMaterial;
        }
        return Math.max(func_150893_a(stack, block), 6.0F);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {

        this.itemIcon = ir.registerIcon("avaritia:infinity_pickaxe");
        hammer = ir.registerIcon("avaritia:infinity_hammer");
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass){

        NBTTagCompound tags = stack.getTagCompound();
        if(tags != null){
            if(tags.getBoolean("hammer"))
                return hammer;
        }
        return itemIcon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconIndex(ItemStack stack){
        return getIcon(stack, 0);
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if(player.isSneaking()) {
            NBTTagCompound tags = stack.getTagCompound();
            if (tags == null) {
                tags = new NBTTagCompound();
                stack.setTagCompound(tags);
            }
            if(EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack) < 10)
                stack.addEnchantment(Enchantment.fortune, 10);
            tags.setBoolean("hammer", !tags.getBoolean("hammer"));
            player.swingItem();
        }
        return stack;
    }

}
