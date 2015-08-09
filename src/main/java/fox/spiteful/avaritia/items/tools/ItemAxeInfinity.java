package fox.spiteful.avaritia.items.tools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.entity.EntityImmortalItem;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemAxeInfinity extends ItemAxe {

    private static final ToolMaterial opAxe = EnumHelper.addToolMaterial("INFINITY_PICKAXE", 32, 9999, 9999F, 20.0F, 200);

    public ItemAxeInfinity(){
        super(opAxe);
        setUnlocalizedName("infinity_axe");
        setTextureName("avaritia:infinity_axe");
        setCreativeTab(Avaritia.tab);
    }

    @Override
    public void setDamage(ItemStack stack, int damage){
        super.setDamage(stack, 0);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return LudicrousItems.cosmic;
    }

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta){
        if (ForgeHooks.isToolEffective(stack, block, meta) || block.getMaterial() == Material.leaves)
        {
            return efficiencyOnProperMaterial;
        }
        return Math.max(func_150893_a(stack, block), 6.0F);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        /*if(player.isSneaking()) {

        }*/
        return stack;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player) {
        /*if(stack.getTagCompound() != null && stack.getTagCompound().getBoolean("destroyer")) {
            MovingObjectPosition raycast = ToolHelper.raytraceFromEntity(player.worldObj, player, true, 10);
            if (raycast != null) {
                breakOtherBlock(player, stack, x, y, z, x, y, z, raycast.sideHit);
            }
        }*/
        return false;
    }

    public void breakOtherBlock(EntityPlayer player, ItemStack stack, int x, int y, int z, int originX, int originY, int originZ, int side) {

        /*World world = player.worldObj;
        Material mat = world.getBlock(x, y, z).getMaterial();
        if(!ToolHelper.isRightMaterial(mat, ItemPickaxeInfinity.MATERIALS))
            return;

        if(world.isAirBlock(x, y, z))
            return;

        ForgeDirection direction = ForgeDirection.getOrientation(side);
        int fortune = EnchantmentHelper.getFortuneModifier(player);
        boolean silk = EnchantmentHelper.getSilkTouchModifier(player);
        boolean doY = direction.offsetY == 0;

        int range = 8;

        ToolHelper.removeBlocksInIteration(player, stack, world, x, y, z, -range, doY ? -1 : -range, -range, range, doY ? range * 2 - 2 : range, range, null, ItemPickaxeInfinity.MATERIALS, silk, fortune, false);*/

    }

    @Override
    public boolean hasCustomEntity (ItemStack stack)
    {
        return true;
    }

    @Override
    public Entity createEntity (World world, Entity location, ItemStack itemstack)
    {
        return new EntityImmortalItem(world, location, itemstack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack, int pass)
    {
        return false;
    }

}
