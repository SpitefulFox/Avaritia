/**
 * This class based on code by Vazkii in the Botania mod.
 *
 * Get the original source here:
 * https://github.com/Vazkii/Botania/blob/master/src/main/java/vazkii/botania/common/item/equipment/tool/ToolCommons.java
 */

package fox.spiteful.avaritia.items.tools;

import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import java.util.*;
import java.util.Map.Entry;
import fox.spiteful.avaritia.Lumberjack;
import fox.spiteful.avaritia.items.ItemMatterCluster;
import fox.spiteful.avaritia.items.ItemStackWrapper;


public class ToolHelper {

    public static Material[] materialsPick = new Material[]{ Material.ROCK, Material.IRON, Material.ICE, Material.GLASS, Material.PISTON, Material.ANVIL };
    public static Material[] materialsShovel = new Material[]{ Material.GRASS, Material.GROUND, Material.SAND, Material.SNOW, Material.CRAFTED_SNOW, Material.CLAY };
    public static Material[] materialsAxe = new Material[]{ Material.CORAL, Material.LEAVES, Material.PLANTS, Material.WOOD, Material.VINE };

    private static Random randy = new Random();
    
    public static Set<EntityPlayer> hammering = new HashSet<EntityPlayer>();
    public static Map<EntityPlayer, List<ItemStack>> hammerdrops = new WeakHashMap<EntityPlayer, List<ItemStack>>();

    public static void removeBlocksInIteration(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int xs, int ys, int zs, int xe, int ye, int ze, IBlockState state, Material[] materialsListing, boolean silk, int fortune, boolean dispose) {
        Block block = state.getBlock();
        float blockHardness = block == null ? 1F : state.getBlockHardness(world, new BlockPos(x, y, z));

        if (!hammerdrops.containsKey(player) || hammerdrops.get(player) == null) {
        	hammerdrops.put(player, new ArrayList<ItemStack>());
        }
        
        if (!hammering.contains(player)) {
        	hammering.add(player);
        }
        
        for(int x1 = xs; x1 < xe; x1++)
            for(int y1 = ys; y1 < ye; y1++)
                for(int z1 = zs; z1 < ze; z1++)
                    removeBlockWithDrops(player, stack, world, new BlockPos(x1 + x, y1 + y, z1 + z), state, materialsListing, silk, fortune, blockHardness, dispose);
        
        int meta = block.getMetaFromState(state);
        if(!world.isRemote /*&& ConfigHandler.blockBreakParticles && ConfigHandler.blockBreakParticlesTool*/)
                //world.playSound(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
        
        if (hammering.contains(player)) {
        	hammering.remove(player);
        }
        
        //List<ItemStack> drops = collateDropList(hammerdrops.get(player));
        //Lumberjack.info(drops);
        
        if(!world.isRemote) {
	        List<ItemStack> clusters = ItemMatterCluster.makeClusters(hammerdrops.get(player));
	        
	        for (ItemStack cluster : clusters) {
	        	EntityItem ent = new EntityItem(world, x,y,z, cluster);
	        	world.spawnEntityInWorld(ent);
	        }
        }
        
        hammerdrops.put(player, null);
    }

    public static boolean isRightMaterial(Material material, Material[] materialsListing) {
        for(Material mat : materialsListing)
            if(material == mat)
                return true;

        return false;
    }

    public static void removeBlockWithDrops(EntityPlayer player, ItemStack stack, World world, BlockPos pos, IBlockState state, Material[] materialsListing, boolean silk, int fortune, float blockHardness, boolean dispose) {
        Block block = state.getBlock();
        if(block==null) {
            return;
        }
        IBlockState state1=world.getBlockState(pos);
        Block blk = state1.getBlock();
        int meta = blk.getMetaFromState(state1);

        if(block != null && blk != block)
            return;

        Material mat = state1.getMaterial();
        if(!world.isRemote && blk != null && !blk.isAir(state1,world, pos)/* && blk.getPlayerRelativeBlockHardness(player, world, x, y, z) > 0*/) {
            if(blk == Blocks.GRASS && stack.getItem() == LudicrousItems.infinity_axe)
                world.setBlockState(pos, Blocks.DIRT.getDefaultState());
            if(!blk.canHarvestBlock(world,pos,player) || !isRightMaterial(mat, materialsListing))
                return;

            if(!player.capabilities.isCreativeMode) {
                blk.onBlockHarvested(world, pos, state1, player);

                if(blk.removedByPlayer(state1, world, pos, player, true)) {
                    blk.onBlockDestroyedByPlayer(world, pos, state1);

                    if(!dispose) {
                        if(state1.getPlayerRelativeBlockHardness(player, world, pos) < 0 && blk.quantityDropped(randy) == 0){
                            ItemStack drop = blk.getPickBlock(state1, raytraceFromEntity(world, player, true, 10), world, pos, player);
                            if(drop == null)
                                drop = new ItemStack(blk, 1, meta);
                            dropItem(drop, world, pos);
                        }
                        blk.harvestBlock(world, player, pos, state1, null, null);
                    }
                }

            } else world.setBlockToAir(pos);

            //if(!world.isRemote /*&& ConfigHandler.blockBreakParticles && ConfigHandler.blockBreakParticlesTool*/)
            //    world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(blk) + (meta << 12));
        }
    }

    /**
     * @author mDiyo
     */
    public static RayTraceResult raytraceFromEntity(World world, Entity player, boolean wut, double range) {
        float f = 1.0F;
        float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
        float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
        double d0 = player.prevPosX + (player.posX - player.prevPosX) * f;
        double d1 = player.prevPosY + (player.posY - player.prevPosY) * f;
        if (!world.isRemote && player instanceof EntityPlayer)
            d1 += 1.62D;
        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * f;
        Vec3d vec3 = new Vec3d(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = range;
        Vec3d vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3);
        return world.rayTraceBlocks(vec3, vec31, wut);
    }

    public static void dropItem(ItemStack drop, World world, BlockPos pos){
        int x=pos.getX();
        int y=pos.getY();
        int z=pos.getZ();
        float f = 0.7F;
        double d0 = (double)(randy.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        double d1 = (double)(randy.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        double d2 = (double)(randy.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        EntityItem entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, drop);
        entityitem.setDefaultPickupDelay();
        world.spawnEntityInWorld(entityitem);
    }
    
    public static List<ItemStack> collateDropList(List<ItemStack> input) {
    	return collateMatterClusterContents(collateMatterCluster(input));
    }
    
    public static List<ItemStack> collateMatterClusterContents(Map<ItemStackWrapper, Integer> input) {
    	List<ItemStack> collated = new ArrayList<ItemStack>();
    	
    	for (Entry<ItemStackWrapper, Integer> e : input.entrySet()) {
    		int count = e.getValue();
    		ItemStackWrapper wrap = e.getKey();
    		
    		int size = wrap.stack.getMaxStackSize();
    		int fullstacks = (int) Math.floor(count / size);
    		
    		for(int i=0; i<fullstacks; i++) {
    			count -= size;
    			ItemStack stack = wrap.stack.copy();
    			stack.stackSize = size;
    			collated.add(stack);
    		}
    		
    		if (count > 0) {
    			ItemStack stack = wrap.stack.copy();
    			stack.stackSize = count;
    			collated.add(stack);
    		}
    	}
    	
    	return collated;
    }
    
    public static Map<ItemStackWrapper, Integer> collateMatterCluster(List<ItemStack> input) {
    	Map<ItemStackWrapper, Integer> counts = new HashMap<ItemStackWrapper, Integer>();
    	
    	if (input != null) {
	    	for (ItemStack stack : input) {
	    		ItemStackWrapper wrap = new ItemStackWrapper(stack);
	    		if (!counts.containsKey(wrap)) {
	    			counts.put(wrap, 0);
	    		}
	    		
	    		counts.put(wrap, counts.get(wrap) + stack.stackSize);
	    	}
    	}
    	
    	return counts;
    }
}
