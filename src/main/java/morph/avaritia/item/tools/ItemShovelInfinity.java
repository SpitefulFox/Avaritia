package morph.avaritia.item.tools;

import codechicken.lib.raytracer.RayTracer;
import morph.avaritia.Avaritia;
import morph.avaritia.entity.EntityImmortalItem;
import morph.avaritia.init.ModItems;
import morph.avaritia.util.ToolHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemShovelInfinity extends ItemSpade {

    private static final ToolMaterial TOOL_MATERIAL = EnumHelper.addToolMaterial("INFINITY_SHOVEL", 32, 9999, 9999F, 7.0F, 200);

    //private IIcon destroyer;

    public ItemShovelInfinity() {
        super(TOOL_MATERIAL);
        setUnlocalizedName("avaritia:infinity_shovel");
        setRegistryName("infinity_shovel");
        setCreativeTab(Avaritia.tab);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, 0);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return ModItems.COSMIC_RARITY;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        if (stack.getTagCompound() != null && stack.getTagCompound().getBoolean("destroyer")) {
            return 5.0F;
        }
        for (String type : getToolClasses(stack)) {
            if (state.getBlock().isToolEffective(type, state)) {
                return efficiency;
            }
        }
        return Math.max(super.getDestroySpeed(stack, state), 1.0F);
    }

    //@SideOnly(Side.CLIENT)
    //public void registerIcons(IIconRegister ir) {
    //    this.itemIcon = ir.registerIcon("avaritia:infinity_shovel");
    //    destroyer = ir.registerIcon("avaritia:infinity_destroyer");
    //}

    //@Override
    //public IIcon getIcon(ItemStack stack, int pass){
    //    NBTTagCompound tags = stack.getTagCompound();
    //    if(tags != null){
    //        if(tags.getBoolean("destroyer"))
    //            return destroyer;
    //    }
    //    return itemIcon;
    //}

    //@SideOnly(Side.CLIENT)
    //@Override
    //public IIcon getIconIndex(ItemStack stack){
    //    return getIcon(stack, 0);
    //}

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking()) {
            NBTTagCompound tags = stack.getTagCompound();
            if (tags == null) {
                tags = new NBTTagCompound();
                stack.setTagCompound(tags);
            }
            tags.setBoolean("destroyer", !tags.getBoolean("destroyer"));
            player.swingArm(hand);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
        if (stack.getTagCompound() != null && stack.getTagCompound().getBoolean("destroyer")) {
            RayTraceResult traceResult = RayTracer.retrace(player, 10, true);
            if (traceResult != null) {
                breakOtherBlock(player, stack, pos, traceResult.sideHit);
            }
        }
        return false;
    }

    public void breakOtherBlock(EntityPlayer player, ItemStack stack, BlockPos pos, EnumFacing sideHit) {

        World world = player.world;
        IBlockState state = world.getBlockState(pos);
        Material mat = state.getMaterial();
        if (!ItemPickaxeInfinity.MATERIALS.contains(mat)) {
            return;
        }

        if (state.getBlock().isAir(state, world, pos)) {
            return;
        }

        boolean doY = sideHit.getAxis() != Axis.Y;

        int range = 8;
        BlockPos min = new BlockPos(-range, doY ? -1 : -range, -range);
        BlockPos max = new BlockPos(range, doY ? range * 2 - 2 : range, range);

        ToolHelper.aoeBlocks(player, stack, world, pos, min, max, null, ItemPickaxeInfinity.MATERIALS, false);

    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return new EntityImmortalItem(world, location, itemstack);
    }

    @Override
    @SideOnly (Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack) {
        return false;
    }

}
