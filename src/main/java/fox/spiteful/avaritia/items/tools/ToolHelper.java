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
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.*;
import java.util.Map.Entry;

import fox.spiteful.avaritia.Lumberjack;
import fox.spiteful.avaritia.items.ItemMatterCluster;
import fox.spiteful.avaritia.items.ItemStackWrapper;
import org.apache.logging.log4j.Level;

public class ToolHelper {

    public static Material[] materialsPick = new Material[]{ Material.rock, Material.iron, Material.ice, Material.glass, Material.piston, Material.anvil };
    public static Material[] materialsShovel = new Material[]{ Material.grass, Material.ground, Material.sand, Material.snow, Material.craftedSnow, Material.clay };
    public static Material[] materialsAxe = new Material[]{ Material.coral, Material.leaves, Material.plants, Material.wood, Material.vine };

    private static Random randy = new Random();
    
    public static Set<EntityPlayer> hammering = new HashSet<EntityPlayer>();
    public static Map<EntityPlayer, List<ItemStack>> hammerdrops = new WeakHashMap<EntityPlayer, List<ItemStack>>();

    public static void removeBlocksInIteration(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int xs, int ys, int zs, int xe, int ye, int ze, Block block, Material[] materialsListing, boolean silk, int fortune, boolean dispose) {
        float blockHardness = block == null ? 1F : block.getBlockHardness(world, x, y, z);

        if (!hammerdrops.containsKey(player) || hammerdrops.get(player) == null) {
        	hammerdrops.put(player, new ArrayList<ItemStack>());
        }
        
        if (!hammering.contains(player)) {
        	hammering.add(player);
        }
        
        for(int x1 = xs; x1 < xe; x1++)
            for(int y1 = ys; y1 < ye; y1++)
                for(int z1 = zs; z1 < ze; z1++)
                    removeBlockWithDrops(player, stack, world, x1 + x, y1 + y, z1 + z, block, materialsListing, silk, fortune, blockHardness, dispose);
        
        int meta = world.getBlockMetadata(x, y, z);
        if(!world.isRemote /*&& ConfigHandler.blockBreakParticles && ConfigHandler.blockBreakParticlesTool*/)
                world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));
        
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

    public static void removeBlockWithDrops(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, Block block, Material[] materialsListing, boolean silk, int fortune, float blockHardness, boolean dispose) {
        if(!world.blockExists(x, y, z))
            return;

        Block blk = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);

        if(block != null && blk != block)
            return;

        Material mat = world.getBlock(x, y, z).getMaterial();
        if(!world.isRemote && blk != null && !blk.isAir(world, x, y, z)/* && blk.getPlayerRelativeBlockHardness(player, world, x, y, z) > 0*/) {
            if(blk == Blocks.grass && stack.getItem() == LudicrousItems.infinity_axe)
                world.setBlock(x, y, z, Blocks.dirt);
            if(!blk.canHarvestBlock(player, meta) || !isRightMaterial(mat, materialsListing))
                return;

            if(!player.capabilities.isCreativeMode) {
                int localMeta = world.getBlockMetadata(x, y, z);
                blk.onBlockHarvested(world, x, y, z, localMeta, player);

                if(blk.removedByPlayer(world, player, x, y, z, true)) {
                    blk.onBlockDestroyedByPlayer(world, x, y, z, localMeta);

                    if(!dispose) {
                        if(blk.getPlayerRelativeBlockHardness(player, world, x, y, z) < 0 && blk.quantityDropped(randy) == 0){
                            ItemStack drop = blk.getPickBlock(raytraceFromEntity(world, player, true, 10), world, x, y, z, player);
                            if(drop == null)
                                drop = new ItemStack(blk, 1, meta);
                            dropItem(drop, world, x, y, z);
                        }
                        blk.harvestBlock(world, player, x, y, z, localMeta);
                    }
                }

            } else world.setBlockToAir(x, y, z);

            //if(!world.isRemote /*&& ConfigHandler.blockBreakParticles && ConfigHandler.blockBreakParticlesTool*/)
            //    world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(blk) + (meta << 12));
        }
    }

    /**
     * @author mDiyo
     */
    public static MovingObjectPosition raytraceFromEntity(World world, Entity player, boolean wut, double range) {
        float f = 1.0F;
        float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
        float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
        double d0 = player.prevPosX + (player.posX - player.prevPosX) * f;
        double d1 = player.prevPosY + (player.posY - player.prevPosY) * f;
        if (!world.isRemote && player instanceof EntityPlayer)
            d1 += 1.62D;
        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * f;
        Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = range;
        Vec3 vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3);
        return world.rayTraceBlocks(vec3, vec31, wut);
    }

    public static void dropItem(ItemStack drop, World world, int x, int y, int z){
        float f = 0.7F;
        double d0 = (double)(randy.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        double d1 = (double)(randy.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        double d2 = (double)(randy.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        EntityItem entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, drop);
        entityitem.delayBeforeCanPickup = 10;
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
