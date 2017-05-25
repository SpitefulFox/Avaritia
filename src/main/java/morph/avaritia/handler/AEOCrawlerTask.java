package morph.avaritia.handler;

import morph.avaritia.util.ToolHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class AEOCrawlerTask {

    final World world;
    final EntityPlayer player;
    final ItemStack stack;
    final int steps;
    final BlockPos origin;
    final boolean leaves;
    final boolean force;
    final Set<BlockPos> posChecked;

    AEOCrawlerTask(World world, EntityPlayer player, ItemStack stack, BlockPos origin, int steps, boolean leaves, boolean force, Set<BlockPos> posChecked) {
        this.world = world;
        this.player = player;
        this.stack = stack;
        this.origin = origin;
        this.steps = steps;
        this.leaves = leaves;
        this.force = force;
        this.posChecked = posChecked;
    }

    void tick() {
        IBlockState originState = world.getBlockState(origin);
        Block originBlock = originState.getBlock();
        if (!force && originBlock.isAir(originState, world, origin)) {
            return;
        }
        ToolHelper.removeBlockWithDrops(player, stack, world, origin, null, ToolHelper.materialsAxe);
        if (steps == 0) {
            return;
        }
        for (EnumFacing dir : EnumFacing.VALUES) {
            BlockPos stepPos = origin.offset(dir);
            if (posChecked.contains(stepPos)) {
                continue;
            }
            IBlockState stepState = world.getBlockState(stepPos);
            Block stepBlock = stepState.getBlock();
            boolean log = stepBlock.isWood(world, stepPos);
            boolean leaf = stepBlock.isLeaves(stepState, world, stepPos);
            if (log || leaf) {
                int steps = this.steps - 1;
                steps = leaf ? leaves ? steps : 3 : steps;
                AvaritiaEventHandler.startCrawlerTask(world, player, stack, stepPos, steps, leaf, false, posChecked);
                posChecked.add(stepPos);
            }
        }
    }
}
