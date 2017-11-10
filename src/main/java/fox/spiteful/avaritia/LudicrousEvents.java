package fox.spiteful.avaritia;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fox.spiteful.avaritia.items.ItemArmorInfinity;
import fox.spiteful.avaritia.items.ItemFracturedOre;
import fox.spiteful.avaritia.items.ItemMatterCluster;
import fox.spiteful.avaritia.items.LudicrousItems;
import fox.spiteful.avaritia.items.tools.ItemPickaxeInfinity;
import fox.spiteful.avaritia.items.tools.ItemSwordInfinity;
import fox.spiteful.avaritia.items.tools.ToolHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
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
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import static net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public class LudicrousEvents {

    private static Random randy = new Random();
    static final String[] trash = new String[]{"dirt", "sand", "gravel", "cobblestone", "netherrack"};

    @SubscribeEvent
    public void onPlayerMine(PlayerInteractEvent event) {
        if(!Config.bedrockBreaker || event.face == -1 || event.world.isRemote || event.action != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK || event.entityPlayer.getHeldItem() == null || event.entityPlayer.capabilities.isCreativeMode)
            return;
        Block block = event.world.getBlock(event.x, event.y, event.z);
        int meta = event.world.getBlockMetadata(event.x, event.y, event.z);
        if(block.getBlockHardness(event.entityPlayer.worldObj, event.x, event.y, event.z) <= -1 &&
                event.entityPlayer.getHeldItem().getItem() == LudicrousItems.infinity_pickaxe &&
                        (block.getMaterial() == Material.rock || block.getMaterial() == Material.iron)){

            if(event.entityPlayer.getHeldItem().getTagCompound() != null && event.entityPlayer.getHeldItem().getTagCompound().getBoolean("hammer")) {
                LudicrousItems.infinity_pickaxe.onBlockStartBreak(event.entityPlayer.getHeldItem(), event.x, event.y, event.z, event.entityPlayer);
            }
            else {

                if(block.quantityDropped(randy) == 0) {
                    ItemStack drop = block.getPickBlock(ToolHelper.raytraceFromEntity(event.world, event.entityPlayer, true, 10),
                            event.world, event.x, event.y, event.z, event.entityPlayer);
                    if(drop == null)
                        drop = new ItemStack(block, 1, meta);
                    dropItem(drop, event.entityPlayer.worldObj, event.x, event.y, event.z);
                }
                else
                    block.harvestBlock(event.world, event.entityPlayer, event.x, event.y, event.z, meta);
                event.entityPlayer.worldObj.setBlockToAir(event.x, event.y, event.z);
                event.world.playAuxSFX(2001, event.x, event.y, event.z, Block.getIdFromBlock(block) + (meta << 12));
            }
        }
    }

    @SubscribeEvent
    public void handleExtraLuck(HarvestDropsEvent event) {
    	if(event.harvester == null)
            return;
        if(event.harvester.getHeldItem() == null)
            return;
        ItemStack held = event.harvester.getHeldItem();
        if(held.getItem() == LudicrousItems.infinity_pickaxe) {
        	extraLuck(event, 4);

        	if (held.getTagCompound() != null && held.getTagCompound().getBoolean("hammer")
            	&& ToolHelper.hammering.contains(event.harvester)
            	&& ToolHelper.hammerdrops.containsKey(event.harvester)
            	&& ToolHelper.hammerdrops.get(event.harvester) != null) {

            	ToolHelper.hammerdrops.get(event.harvester).addAll(event.drops);
            	event.drops.clear();
            }
        }
        else if(held.getItem() == LudicrousItems.infinity_shovel) {

            if (held.getTagCompound() != null && held.getTagCompound().getBoolean("destroyer")
                    && ToolHelper.hammering.contains(event.harvester)
                    && ToolHelper.hammerdrops.containsKey(event.harvester)
                    && ToolHelper.hammerdrops.get(event.harvester) != null) {

                ArrayList<ItemStack> garbage = new ArrayList<ItemStack>();
                for(ItemStack drop : event.drops){
                    if(isGarbage(drop))
                        garbage.add(drop);
                }
                for(ItemStack junk : garbage){
                    event.drops.remove(junk);
                }
                ToolHelper.hammerdrops.get(event.harvester).addAll(event.drops);
                event.drops.clear();
            }
        }
        else if(held.getItem() == LudicrousItems.infinity_axe) {

            if (ToolHelper.hammering.contains(event.harvester)
                    && ToolHelper.hammerdrops.containsKey(event.harvester)
                    && ToolHelper.hammerdrops.get(event.harvester) != null) {

                ToolHelper.hammerdrops.get(event.harvester).addAll(event.drops);
                event.drops.clear();
            }
        }
    }

    public static void extraLuck(HarvestDropsEvent event, int mult){
        if(event.block.getMaterial() == Material.rock){
        	List<ItemStack> adds = new ArrayList<ItemStack>();
        	List<ItemStack> removals = new ArrayList<ItemStack>();
        	for(ItemStack drop : event.drops){
                if(drop.getItem() != Item.getItemFromBlock(event.block) && !(drop.getItem() instanceof ItemBlock)){
                    drop.stackSize = Math.min(drop.stackSize * mult, drop.getMaxStackSize());
                }
                else if(Config.fractured && drop.getItem() == Item.getItemFromBlock(event.block))
                {
                	ItemFracturedOre ifo = (ItemFracturedOre)LudicrousItems.fractured_ore;
                    int[] oreids = OreDictionary.getOreIDs(drop);
                    for (int i=0; i<oreids.length; i++) {
                    	String orename = OreDictionary.getOreName(oreids[i]);
                    	if (orename.startsWith("ore")) {
                    		// add the fractured ores
                    		adds.add(ifo.getStackForOre(drop, Math.min(drop.stackSize * (mult+1), drop.getMaxStackSize())));
                    		removals.add(drop);
                    		break;
                    	}
                    }
                }
            }
        	event.drops.addAll(adds);
        	event.drops.removeAll(removals);
        }
    }

    private static boolean isGarbage(ItemStack drop) {
        for(int id : OreDictionary.getOreIDs(drop)) {
            for(String ore : trash) {
                if(OreDictionary.getOreName(id).equals(ore))
                    return true;
            }
        }

        return false;
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
        if(LudicrousItems.isInfinite(player) && !event.source.damageType.equals("infinity"))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onAttacked(LivingAttackEvent event) {
        if(!(event.entityLiving instanceof EntityPlayer))
            return;
        if(event.source.getEntity() != null && event.source.getEntity() instanceof EntityPlayer)
            return;
        EntityPlayer player = (EntityPlayer)event.entityLiving;
        if(player.getHeldItem() != null && player.getHeldItem().getItem() == LudicrousItems.infinity_sword && player.isUsingItem())
            event.setCanceled(true);
        if(LudicrousItems.isInfinite(player) && !event.source.damageType.equals("infinity"))
            event.setCanceled(true);
    }


    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        if(event.recentlyHit && event.entityLiving instanceof EntitySkeleton && event.source.getEntity() instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer)event.source.getEntity();
            if(player.getHeldItem() != null && player.getHeldItem().getItem() == LudicrousItems.skull_sword){
            	// ok, we need to drop a skull then.
            	if (event.drops.isEmpty()) {
            		addDrop(event, new ItemStack(Items.skull, 1, 1));
            	} else {
            		int skulls = 0;
            		
            		for (int i=0; i<event.drops.size(); i++) {
            			EntityItem drop = event.drops.get(i);
            			ItemStack stack = drop.getEntityItem();
            			if (stack.getItem() == Items.skull) {
            				if (stack.getItemDamage() == 1) {
            					skulls++;
            				} else if (stack.getItemDamage() == 0) {
            					skulls++;
            					stack.setItemDamage(1);
            				}
            			}
            		}
            		
            		if (skulls == 0) {
            			addDrop(event, new ItemStack(Items.skull, 1, 1));
            		}
            	}
                
            }
        }
    }

    @SubscribeEvent
    public void diggity(BreakSpeed event){
        if(event.entityPlayer.getHeldItem() != null){
            ItemStack held = event.entityPlayer.getHeldItem();
            if(held.getItem() == LudicrousItems.infinity_pickaxe || held.getItem() == LudicrousItems.infinity_shovel){
                if(!event.entityPlayer.onGround)
                    event.newSpeed *= 5;
                if(!event.entityPlayer.isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(event.entityPlayer))
                    event.newSpeed *= 5;
                if(held.getTagCompound() != null) {
                    if (held.getTagCompound().getBoolean("hammer") || held.getTagCompound().getBoolean("destroyer")) {
                        event.newSpeed *= 0.5;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void canHarvest(PlayerEvent.HarvestCheck event){
        if(event.entityPlayer.getHeldItem() != null){
            ItemStack held = event.entityPlayer.getHeldItem();
            if(held.getItem() == LudicrousItems.infinity_shovel && event.block.getMaterial() == Material.rock){
                if(held.getTagCompound() != null && held.getTagCompound().getBoolean("destroyer") && isGarbageBlock(event.block))
                    event.success = true;
            }
        }
    }

    private static boolean isGarbageBlock(Block block) {
        for(int id : OreDictionary.getOreIDs(new ItemStack(block, 1))) {
            String ore = OreDictionary.getOreName(id);
            if(ore.equals("cobblestone") || ore.equals("stone") || ore.equals("netherrack"))
                return true;
        }

        return false;
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event){
        if(event.entityLiving instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer)event.entityLiving;
            if(LudicrousItems.isInfinite(player) && !event.source.getDamageType().equals("infinity")){
                event.setCanceled(true);
                player.setHealth(player.getMaxHealth());
            }
        }
    }

    private void addDrop(LivingDropsEvent event, ItemStack drop) {
        EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, drop);
        entityitem.delayBeforeCanPickup = 10;
        event.drops.add(entityitem);
    }

    @SubscribeEvent
    public void clusterClustererererer(EntityItemPickupEvent event) {
    	if(event.entityPlayer != null && event.item.getEntityItem().getItem() == LudicrousItems.matter_cluster) {
    		ItemStack stack = event.item.getEntityItem();
    		EntityPlayer player = event.entityPlayer;
    		
    		for (int i=0; i<player.inventory.mainInventory.length; i++) {
    			if (stack.stackSize == 0) {
    				break;
    			}
    			ItemStack slot = player.inventory.mainInventory[i];
    			if (slot != null && slot.getItem() != null && slot.getItem() == LudicrousItems.matter_cluster) {
    				ItemMatterCluster.mergeClusters(stack, slot);
    			}
    		}
    	}
    }
}
