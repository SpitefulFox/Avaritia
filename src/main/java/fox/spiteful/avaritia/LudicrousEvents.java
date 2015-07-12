package fox.spiteful.avaritia;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.apache.logging.log4j.Level;

import java.util.Random;

public class LudicrousEvents {

    private Random randy = new Random();

    @SubscribeEvent
    public void breakDamnit(PlayerEvent.HarvestCheck event){
        if(event.block == Blocks.bedrock)
            event.success = true;
    }

    @SubscribeEvent
    public void onPlayerBreaking(PlayerEvent.BreakSpeed event) {
        Lumberjack.log(Level.INFO, event.block.getLocalizedName());
        if(event.x == -1 || event.y == -1 || event.z == -1 || event.entityPlayer.worldObj.isRemote)
            return;
        if(event.block.getBlockHardness(event.entityPlayer.worldObj, event.x, event.y, event.z) <= -1 &&
                (event.block.getMaterial() == Material.rock || event.block.getMaterial() == Material.iron)){
            dropItem(new ItemStack(event.block, 1, event.metadata), event.entityPlayer.worldObj, event.x, event.y, event.z);
            event.entityPlayer.worldObj.setBlockToAir(event.x, event.y, event.z);
            //event.setCanceled(true);
        }
    }

    private void dropItem(ItemStack drop, World world, int x, int y, int z){
        float f = 0.7F;
        double d0 = (double)(randy.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        double d1 = (double)(randy.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        double d2 = (double)(randy.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
        EntityItem entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, drop);
        entityitem.delayBeforeCanPickup = 10;
        world.spawnEntityInWorld(entityitem);
    }
}
