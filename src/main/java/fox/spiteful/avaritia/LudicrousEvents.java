package fox.spiteful.avaritia;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fox.spiteful.avaritia.items.LudicrousItems;
import fox.spiteful.avaritia.items.tools.ItemSwordInfinity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Random;

import static net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

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

    @SubscribeEvent
    public void extraLuck(HarvestDropsEvent event){
        if(event.harvester == null)
            return;
        if(event.harvester.getHeldItem() == null)
            return;
        ItemStack held = event.harvester.getHeldItem();
        if(held.getItem() == LudicrousItems.infinity_pickaxe){
            if(event.block.getMaterial() == Material.rock){
                ArrayList<ItemStack> adds = new ArrayList<ItemStack>();
                ArrayList<ItemStack> removals = new ArrayList<ItemStack>();
                for(ItemStack drop : event.drops){
                    if(drop.getItem() != Item.getItemFromBlock(event.block) && !(drop.getItem() instanceof ItemBlock)){
                        drop.stackSize = Math.min(drop.stackSize * 4, drop.getMaxStackSize());
                    }
                    else if(drop.getItem() == Item.getItemFromBlock(event.block) && FurnaceRecipes.smelting().getSmeltingResult(drop) != null){
                        ItemStack smelt = FurnaceRecipes.smelting().getSmeltingResult(drop).copy();
                        smelt.stackSize = Math.min(drop.stackSize * 8, drop.getMaxStackSize());
                        adds.add(smelt);
                        removals.add(drop);
                    }
                }
                for(ItemStack add : adds)
                    event.drops.add(add);
                for(ItemStack rem : removals)
                    event.drops.remove(rem);
                event.dropChance = 1.0F;
            }
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

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if (event.itemStack.getItem() instanceof ItemSwordInfinity) {
            for(int x = 0;x < event.toolTip.size();x++){
                if(event.toolTip.get(x).contains(StatCollector.translateToLocal("attribute.name.generic.attackDamage"))
                        || event.toolTip.get(x).contains(StatCollector.translateToLocal("Attack Damage"))){
                    event.toolTip.set(x, EnumChatFormatting.BLUE + "+" + LudicrousText.makeFabulous(StatCollector.translateToLocal("tip.infinity")) + " " + EnumChatFormatting.BLUE + StatCollector.translateToLocal("attribute.name.generic.attackDamage"));
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public void onGetHurt(LivingHurtEvent event){
        if(!(event.entityLiving instanceof EntityPlayer))
            return;
        EntityPlayer player = (EntityPlayer)event.entityLiving;
        if(player.getHeldItem() != null && player.getHeldItem().getItem() == LudicrousItems.infinity_sword && player.isUsingItem())
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        if(event.recentlyHit && event.entityLiving instanceof EntitySkeleton && event.source.getEntity() instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer)event.source.getEntity();
            if(player.getHeldItem() != null && player.getHeldItem().getItem() == LudicrousItems.skull_sword){
                addDrop(event, new ItemStack(Items.skull, 1, 1));
            }
        }
    }

    private void addDrop(LivingDropsEvent event, ItemStack drop) {
        EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, drop);
        entityitem.delayBeforeCanPickup = 10;
        event.drops.add(entityitem);
    }

}
