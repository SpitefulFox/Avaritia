package morph.avaritia.item.tools;

import codechicken.lib.util.ItemUtils;
import morph.avaritia.Avaritia;
import morph.avaritia.entity.EntityImmortalItem;
import morph.avaritia.handler.AvaritiaEventHandler;
import morph.avaritia.init.ModItems;
import morph.avaritia.item.ItemMatterCluster;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Set;

/**
 * Created by covers1624 on 31/07/2017.
 * Credits mostly to brandon3055, this is his AOE code.
 */
public class ItemHoeInfinity extends ItemHoe {

    private static final ToolMaterial TOOL_MATERIAL = EnumHelper.addToolMaterial("INFINITY_HOE", 32, 9999, 9999F, 20.0F, 200);

    public ItemHoeInfinity() {
        super(TOOL_MATERIAL);
        setUnlocalizedName("avaritia:infinity_hoe");
        setRegistryName("infinity_hoe");
        setCreativeTab(Avaritia.tab);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos origin, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (!attemptHoe(stack, player, world, origin, facing)) {
            if (world.getBlockState(origin).getBlock() != Blocks.FARMLAND) {
                return EnumActionResult.FAIL;
            }
        }

        if (player.isSneaking()) {
            return EnumActionResult.FAIL;
        }

        int aoe_range = 4;
        AvaritiaEventHandler.enableItemCapture();
        for (BlockPos aoePos : BlockPos.getAllInBox(origin.add(-aoe_range, 0, -aoe_range), origin.add(aoe_range, 0, aoe_range))) {
            if (aoePos.equals(origin)) {
                continue;
            }

            boolean airOrReplaceable = world.isAirBlock(aoePos) || world.getBlockState(aoePos).getBlock().isReplaceable(world, aoePos);
            boolean lowerBlockOk = world.isSideSolid(aoePos.down(), EnumFacing.UP) || world.getBlockState(aoePos.down()).getBlock() == Blocks.FARMLAND;

            if (airOrReplaceable && lowerBlockOk && (player.capabilities.isCreativeMode || player.inventory.hasItemStack(new ItemStack(Blocks.DIRT)))) {
                BlockEvent.PlaceEvent event = ForgeEventFactory.onPlayerBlockPlace(player, new BlockSnapshot(world, aoePos, Blocks.DIRT.getDefaultState()), EnumFacing.UP, player.getActiveHand());

                if (!event.isCanceled() && (player.capabilities.isCreativeMode || consumeStack(new ItemStack(Blocks.DIRT), player.inventory))) {
                    world.setBlockState(aoePos, Blocks.DIRT.getDefaultState());
                }
            }

            boolean canDropAbove = world.getBlockState(aoePos.up()).getBlock() == Blocks.DIRT || world.getBlockState(aoePos.up()).getBlock() == Blocks.GRASS || world.getBlockState(aoePos.up()).getBlock() == Blocks.FARMLAND;
            boolean canRemoveAbove = canDropAbove || world.getBlockState(aoePos.up()).getBlock().isReplaceable(world, aoePos.up());
            boolean up2OK = world.isAirBlock(aoePos.up().up()) || world.getBlockState(aoePos.up().up()).getBlock().isReplaceable(world, aoePos.up().up());

            if (!world.isAirBlock(aoePos.up()) && canRemoveAbove && up2OK) {
                if (!world.isRemote && canDropAbove) {
                    world.spawnEntity(new EntityItem(world, player.posX, player.posY, player.posZ, new ItemStack(Blocks.DIRT)));
                }
                world.setBlockToAir(aoePos.up());
            }
            attemptHoe(stack, player, world, aoePos, facing);
        }

        AvaritiaEventHandler.stopItemCapture();
        Set<ItemStack> drops = AvaritiaEventHandler.getCapturedDrops();
        if (!world.isRemote) {
            List<ItemStack> clusters = ItemMatterCluster.makeClusters(drops);
            for (ItemStack cluster : clusters) {
                ItemUtils.dropItem(world, origin.up(), cluster);
            }
        }

        return EnumActionResult.SUCCESS;
    }

    public boolean consumeStack(ItemStack stack, IInventory inventory) {
        if (stack.isEmpty()) {
            return false;
        }

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack s = inventory.getStackInSlot(i);
            if (s.isEmpty()) {
                continue;
            }

            if (ItemStack.areItemsEqual(stack, s) && stack.getItemDamage() == s.getItemDamage() && s.getCount() >= stack.getCount()) {
                s.shrink(stack.getCount());
                inventory.markDirty();
                return true;
            }
        }

        return false;
    }

    /**
     * Attempts to hoe a block.
     * Basically carbon copy of vanilla but not really.
     *
     * @param hoeStack The hoe stack.
     * @param player   The player.
     * @param world    The world.
     * @param pos      The position to hoe.
     * @param face     The face of the block we face clicked on.
     * @return If the hoe operation was successful.
     */
    private boolean attemptHoe(ItemStack hoeStack, EntityPlayer player, World world, BlockPos pos, EnumFacing face) {
        if (!player.canPlayerEdit(pos, face, hoeStack)) {
            return false;
        }

        int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(hoeStack, player, world, pos);
        if (hook != 0) {
            return hook > 0;
        }

        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (face != EnumFacing.DOWN && world.isAirBlock(pos.up())) {
            if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
                setBlock(hoeStack, player, world, pos, Blocks.FARMLAND.getDefaultState());
                return true;
            }

            if (block == Blocks.DIRT) {
                switch (state.getValue(BlockDirt.VARIANT)) {
                    case DIRT:
                        setBlock(hoeStack, player, world, pos, Blocks.FARMLAND.getDefaultState());
                        return true;
                    case COARSE_DIRT:
                        setBlock(hoeStack, player, world, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void setBlock(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, IBlockState state) {
        worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

        if (!worldIn.isRemote) {
            worldIn.setBlockState(pos, state, 0b1011);
        }
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
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return new EntityImmortalItem(world, location, itemstack);
    }

    @Override
    @SideOnly (Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return false;
    }
}
