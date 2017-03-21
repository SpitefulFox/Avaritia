/*
 *
 * Code blatantly jacked from Vazkii
 * Get the original here: https://github.com/Vazkii/Botania/blob/master/src/main/java/vazkii/botania/common/item/equipment/tool/terrasteel/ItemTerraAxe.java
 */

package fox.spiteful.avaritia.items.tools;

import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.Lumberjack;
import fox.spiteful.avaritia.entity.EntityImmortalItem;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemAxeInfinity extends ItemAxe {

    private static final ToolMaterial opAxe = EnumHelper.addToolMaterial("INFINITY_PICKAXE", 32, 9999, 9999F, 20.0F, 200);
    private static Map<Integer, List<BlockSwapper>> blockSwappers = new HashMap();

    public ItemAxeInfinity(){
        super(opAxe);
        setCreativeTab(Avaritia.tab);
        MinecraftForge.EVENT_BUS.register(this);
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
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        if (super.getStrVsBlock(stack, state) > 1.0F || state.getMaterial() == Material.LEAVES) {
            return efficiencyOnProperMaterial;
        }
        return Math.max(super.getStrVsBlock(stack, state), 6.0F);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if(player.isSneaking()) {
            player.swingArm(hand);
            int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
            boolean silk = EnchantmentHelper.getEnchantments(stack).containsKey(Enchantments.SILK_TOUCH);

            int range = 13;

            ToolHelper.removeBlocksInIteration(player, stack, world, (int)player.posX, (int)player.posY, (int)player.posZ, -range, -3, -range, range, range * 2 - 3, range, null, ToolHelper.materialsAxe, silk, fortune, false);
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        RayTraceResult raycast = ToolHelper.raytraceFromEntity(player.worldObj, player, true, 10);
        if (raycast != null) {
            breakOtherBlock(player, stack, x, y, z, x, y, z, raycast.sideHit);
        }
        return false;
    }

    public void breakOtherBlock(EntityPlayer player, ItemStack stack, int x, int y, int z, int originX, int originY, int originZ, EnumFacing side) {
        if(player.isSneaking())
            return;
        BlockPos coords = new BlockPos(x, y, z);
        addBlockSwapper(player.worldObj, player, stack, coords, coords, 32, false, true, new ArrayList<String>());
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
    public boolean hasEffect(ItemStack par1ItemStack)
    {
        return false;
    }

    @SubscribeEvent
    public void onTickEnd(TickEvent.WorldTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            int dim = event.world.provider.getDimension();
            if(blockSwappers.containsKey(dim)) {
                List<BlockSwapper> swappers = blockSwappers.get(dim);
                List<BlockSwapper> swappersSafe = new ArrayList(swappers);
                swappers.clear();
                for(BlockSwapper s : swappersSafe)
                    if(s != null)
                        s.tick();
            }
        }
    }

    private static BlockSwapper addBlockSwapper(World world, EntityPlayer player, ItemStack stack, BlockPos origCoords, BlockPos coords, int steps, boolean leaves, boolean force, List<String> posChecked) {
        BlockSwapper swapper = new BlockSwapper(world, player, stack, origCoords, coords, steps, leaves, force, posChecked);
        int dim = world.provider.getDimension();
        if(!blockSwappers.containsKey(dim))
            blockSwappers.put(dim, new ArrayList());
        blockSwappers.get(dim).add(swapper);
        return swapper;
    }
    private static class BlockSwapper {
        final World world;
        final EntityPlayer player;
        final ItemStack stack;
        final BlockPos origCoords;
        final int steps;
        final BlockPos coords;
        final boolean leaves;
        final boolean force;
        final List<String> posChecked;
        BlockSwapper(World world, EntityPlayer player, ItemStack stack, BlockPos origCoords, BlockPos coords, int steps, boolean leaves, boolean force, List<String> posChecked) {
            this.world = world;
            this.player = player;
            this.stack = stack;
            this.origCoords = origCoords;
            this.coords = coords;
            this.steps = steps;
            this.leaves = leaves;
            this.force = force;
            this.posChecked = posChecked;
        }
        void tick() {
            IBlockState state = world.getBlockState(coords);
            Block blockat = state.getBlock();
            if (!force && blockat.isAir(state, world, coords)) return;
              ToolHelper.removeBlockWithDrops(player, stack, world, coords, null, ToolHelper.materialsAxe, EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack), 0F, false);
            if(steps == 0)
                return;
            for(EnumFacing dir : EnumFacing.VALUES) {
                int x = coords.getX() + dir.getFrontOffsetX();
                int y = coords.getY() + dir.getFrontOffsetY();
                int z = coords.getZ() + dir.getFrontOffsetZ();
                String pstr = posStr(x, y, z);
                if(posChecked.contains(pstr))
                    continue;
                BlockPos pos = new BlockPos(x, y, z);
                IBlockState state1 = world.getBlockState(pos);
                Block block = state1.getBlock();
                boolean log = block.isWood(world, pos);
                boolean leaf = block.isLeaves(state1, world, pos);
                if(log || leaf) {
                    int steps = this.steps - 1;
                    steps = leaf ? leaves ? steps : 3 : steps;
                    addBlockSwapper(world, player, stack, origCoords, new BlockPos(x, y, z), steps, leaf, false, posChecked);
                    posChecked.add(pstr);
                 }
            }
        }
        String posStr(int x, int y, int z) {
            return x + ":" + y + ":" + z;
        }
    }

}
