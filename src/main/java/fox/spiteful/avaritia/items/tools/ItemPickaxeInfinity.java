package fox.spiteful.avaritia.items.tools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.entity.EntityImmortalItem;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class ItemPickaxeInfinity extends ItemPickaxe {

    private static final ToolMaterial opPickaxe = EnumHelper.addToolMaterial("INFINITY_PICKAXE", 32, 9999, 9999F, 6.0F, 200);
    private IIcon hammer;

    public static final Material[] MATERIALS = new Material[] { Material.rock, Material.iron, Material.ice, Material.glass, Material.piston, Material.anvil, Material.grass, Material.ground, Material.sand, Material.snow, Material.craftedSnow, Material.clay };

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
    public float getDigSpeed(ItemStack stack, Block block, int meta){
        if(stack.getTagCompound() != null && stack.getTagCompound().getBoolean("hammer")){
            return 5.0F;
        }
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

    @Override
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

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase victim, EntityLivingBase player){
        if(stack.getTagCompound() != null){
            if(stack.getTagCompound().getBoolean("hammer")) {
                if(!(victim instanceof EntityPlayer && LudicrousItems.isInfinite((EntityPlayer)victim))) {
                    int i = 10;
                    victim.addVelocity((double) (-MathHelper.sin(player.rotationYaw * (float) Math.PI / 180.0F) * (float) i * 0.5F), 2.0D, (double) (MathHelper.cos(player.rotationYaw * (float) Math.PI / 180.0F) * (float) i * 0.5F));
                }
            }
        }
        return true;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player) {
        if(stack.getTagCompound() != null && stack.getTagCompound().getBoolean("hammer")) {
            MovingObjectPosition raycast = ToolHelper.raytraceFromEntity(player.worldObj, player, true, 10);
            if (raycast != null) {
                breakOtherBlock(player, stack, x, y, z, x, y, z, raycast.sideHit);
            }
        }
        return false;
    }

    public void breakOtherBlock(EntityPlayer player, ItemStack stack, int x, int y, int z, int originX, int originY, int originZ, int side) {

        World world = player.worldObj;
        Material mat = world.getBlock(x, y, z).getMaterial();
        if(!ToolHelper.isRightMaterial(mat, MATERIALS))
            return;

        if(world.isAirBlock(x, y, z))
            return;

        ForgeDirection direction = ForgeDirection.getOrientation(side);
        int fortune = EnchantmentHelper.getFortuneModifier(player);
        boolean silk = EnchantmentHelper.getSilkTouchModifier(player);
        boolean doY = direction.offsetY == 0;

        int range = 8;

        ToolHelper.removeBlocksInIteration(player, stack, world, x, y, z, -range, doY ? -1 : -range, -range, range, doY ? range * 2 - 2 : range, range, null, MATERIALS, silk, fortune, false);

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
