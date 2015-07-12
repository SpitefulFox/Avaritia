package fox.spiteful.avaritia;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.apache.logging.log4j.Level;

import java.util.Random;

public class LudicrousEvents {

    private Random randy = new Random();

    @SubscribeEvent
    public void onPlayerMine(PlayerInteractEvent event) {
        //Lumberjack.log(Level.INFO, event.block.getLocalizedName());
        if(event.face == -1 || event.world.isRemote || event.action != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK || event.entityPlayer.getHeldItem() == null)
            return;
        Block block = event.world.getBlock(event.x, event.y, event.z);
        if(block.getBlockHardness(event.entityPlayer.worldObj, event.x, event.y, event.z) <= -1 &&
                event.entityPlayer.getHeldItem().getItem() == LudicrousItems.infinity_pickaxe &&
                        (block.getMaterial() == Material.rock || block.getMaterial() == Material.iron)){
            dropItem(new ItemStack(block, 1, event.world.getBlockMetadata(event.x, event.y, event.z)), event.entityPlayer.worldObj, event.x, event.y, event.z);
            event.entityPlayer.worldObj.setBlockToAir(event.x, event.y, event.z);
            event.world.playSoundEffect(event.x, event.y, event.z, block.stepSound.getBreakSound(), block.stepSound.getVolume(), block.stepSound.getPitch());
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
