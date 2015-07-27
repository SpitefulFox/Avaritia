/**
 * This class based on code by Vazkii in the Botania mod.
 *
 * Get the original source here:
 * https://github.com/Vazkii/Botania/blob/master/src/main/java/vazkii/botania/common/item/equipment/tool/ToolCommons.java
 */

package fox.spiteful.avaritia.items.tools;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.Random;

public class ToolHelper {

    public static Material[] materialsPick = new Material[]{ Material.rock, Material.iron, Material.ice, Material.glass, Material.piston, Material.anvil };
    public static Material[] materialsShovel = new Material[]{ Material.grass, Material.ground, Material.sand, Material.snow, Material.craftedSnow, Material.clay };
    public static Material[] materialsAxe = new Material[]{ Material.coral, Material.leaves, Material.plants, Material.wood };

    private static Random randy = new Random();

    public static void removeBlocksInIteration(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int xs, int ys, int zs, int xe, int ye, int ze, Block block, Material[] materialsListing, boolean silk, int fortune, boolean dispose) {
        float blockHardness = block == null ? 1F : block.getBlockHardness(world, x, y, z);

        for(int x1 = xs; x1 < xe; x1++)
            for(int y1 = ys; y1 < ye; y1++)
                for(int z1 = zs; z1 < ze; z1++)
                    removeBlockWithDrops(player, stack, world, x1 + x, y1 + y, z1 + z, block, materialsListing, silk, fortune, blockHardness, dispose);
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

            if(!world.isRemote /*&& ConfigHandler.blockBreakParticles && ConfigHandler.blockBreakParticlesTool*/)
                world.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(blk) + (meta << 12));
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

    private static void dropItem(ItemStack drop, World world, int x, int y, int z){
        float f = 0.7F;
        double d0 = (double)(randy.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        double d1 = (double)(randy.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        double d2 = (double)(randy.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        EntityItem entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, drop);
        entityitem.delayBeforeCanPickup = 10;
        world.spawnEntityInWorld(entityitem);
    }
}
