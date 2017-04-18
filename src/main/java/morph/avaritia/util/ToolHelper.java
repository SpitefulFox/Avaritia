/**
 * This class based on code by Vazkii in the Botania mod.
 *
 * Get the original source here:
 * https://github.com/Vazkii/Botania/blob/master/src/main/java/vazkii/botania/common/item/equipment/tool/ToolCommons.java
 */

package morph.avaritia.util;

import codechicken.lib.util.ItemUtils;
import com.google.common.collect.Sets;
import morph.avaritia.handler.AvaritiaEventHandler;
import morph.avaritia.init.ModItems;
import morph.avaritia.item.ItemMatterCluster;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;
import java.util.Map.Entry;

public class ToolHelper {

    public static Material[] materialsPick = new Material[] { Material.ROCK, Material.IRON, Material.ICE, Material.GLASS, Material.PISTON, Material.ANVIL };
    public static Material[] materialsShovel = new Material[] { Material.GRASS, Material.GROUND, Material.SAND, Material.SNOW, Material.CRAFTED_SNOW, Material.CLAY };
    public static Set<Material> materialsAxe = Sets.newHashSet(Material.CORAL, Material.LEAVES, Material.PLANTS, Material.WOOD, Material.VINE);

    public static void aoeBlocks(EntityPlayer player, ItemStack stack, World world, BlockPos origin, BlockPos minOffset, BlockPos maxOffset, Block target, Set<Material> validMaterials, boolean isSilk, int fortuneLevel, boolean disposeDrops) {
        float targetHardness = 1F;
        if (target != null) {
            IBlockState state = world.getBlockState(origin);
            targetHardness = target.getBlockHardness(state, world, origin);
        }
        if (!AvaritiaEventHandler.playerHammerDrops.containsKey(player) || AvaritiaEventHandler.playerHammerDrops.get(player) == null) {
            AvaritiaEventHandler.playerHammerDrops.put(player, new ArrayList<>());
        }

        if (!AvaritiaEventHandler.playerHammerTracker.contains(player)) {
            AvaritiaEventHandler.playerHammerTracker.add(player);
        }

        BlockPos min = origin.subtract(minOffset);
        BlockPos max = origin.add(maxOffset);
        Iterable<BlockPos> positions = BlockPos.getAllInBox(min, max);
        for (BlockPos pos : positions) {
            removeBlockWithDrops(player, stack, world, pos, target, validMaterials, isSilk, fortuneLevel, targetHardness, disposeDrops);
        }

        if (AvaritiaEventHandler.playerHammerTracker.contains(player)) {
            AvaritiaEventHandler.playerHammerTracker.remove(player);
        }

        if (!world.isRemote) {
            List<ItemStack> clusters = ItemMatterCluster.makeClusters(AvaritiaEventHandler.playerHammerDrops.get(player));
            for (ItemStack cluster : clusters) {
                ItemUtils.dropItem(world, origin, cluster);
            }
        }
        AvaritiaEventHandler.playerHammerDrops.put(player, null);
    }

    public static void removeBlockWithDrops(EntityPlayer player, ItemStack stack, World world, BlockPos pos, Block target, Set<Material> validMaterials, boolean isSilk, int fortune, float targetHardness, boolean disposeDrops) {
        if (!world.isBlockLoaded(pos)) {
            return;
        }
        IBlockState state = world.getBlockState(pos);

        if (state == null || (target != null && target != state.getBlock())) {
            return;
        }
        Material mat = state.getMaterial();
        if (!world.isRemote && !state.getBlock().isAir(state, world, pos)) {
            if (state.getBlock() == Blocks.GRASS && stack.getItem() == ModItems.infinity_axe) {
                world.setBlockState(pos, Blocks.DIRT.getDefaultState());
            }
            if (!state.getBlock().canHarvestBlock(world, pos, player) || !validMaterials.contains(mat)) {
                return;
            }
            if (!player.capabilities.isCreativeMode) {
                state.getBlock().onBlockHarvested(world, pos, state, player);

                if (state.getBlock().removedByPlayer(state, world, pos, player, true)) {
                    state.getBlock().onBlockDestroyedByPlayer(world, pos, state);

                    if (!disposeDrops) {//TODO, This is all the cancer.
                        if (state.getBlock().getPlayerRelativeBlockHardness(state, player, world, pos) < 0) {
                            player.addStat(StatList.getBlockStats(state.getBlock()));
                            //player.addExhaustion(0.025F);

                            if (state.getBlock().canSilkHarvest(world, pos, state, player) && isSilk) {
                                List<ItemStack> items = new ArrayList<>();
                                ItemStack drop;

                                Item item = Item.getItemFromBlock(state.getBlock());
                                if (item.getHasSubtypes()) {
                                    drop = new ItemStack(item, 1, state.getBlock().getMetaFromState(state));
                                } else {
                                    drop = new ItemStack(item, 1, 0);
                                }

                                if (drop != null) {
                                    items.add(drop);
                                }

                                net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, world, pos, state, fortune, 1.0f, isSilk, player);
                                for (ItemStack dropStack : items) {
                                    ItemUtils.dropItem(world, pos, dropStack);
                                }
                            } else {
                                state.getBlock().dropBlockAsItem(world, pos, state, fortune);
                            }
                            world.playEvent(2001, pos, Block.getStateId(state));
                            world.setBlockToAir(pos);
                        }
                    }
                }
            } else {
                world.setBlockToAir(pos);
            }
        }
    }

    public static List<ItemStack> collateDropList(List<ItemStack> input) {
        return collateMatterClusterContents(collateMatterCluster(input));
    }

    public static List<ItemStack> collateMatterClusterContents(Map<ItemStackWrapper, Integer> input) {
        List<ItemStack> collated = new ArrayList<>();

        for (Entry<ItemStackWrapper, Integer> e : input.entrySet()) {
            int count = e.getValue();
            ItemStackWrapper wrap = e.getKey();

            int size = wrap.stack.getMaxStackSize();
            int fullstacks = (int) Math.floor(count / size);

            for (int i = 0; i < fullstacks; i++) {
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
        Map<ItemStackWrapper, Integer> counts = new HashMap<>();

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
