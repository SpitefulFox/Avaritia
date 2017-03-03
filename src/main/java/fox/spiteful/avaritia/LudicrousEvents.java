package fox.spiteful.avaritia;

import fox.spiteful.avaritia.items.ItemFracturedOre;
import fox.spiteful.avaritia.items.ItemMatterCluster;
import fox.spiteful.avaritia.items.LudicrousItems;
import fox.spiteful.avaritia.items.tools.ItemSwordInfinity;
import fox.spiteful.avaritia.items.tools.ToolHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import static net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public class LudicrousEvents {

    private static Random randy = new Random();
    static final String[] trash = new String[]{"dirt", "sand", "gravel", "cobblestone", "netherrack"};

    @SubscribeEvent
    public void onPlayerMine(PlayerInteractEvent event) {
        if(!Config.bedrockBreaker || event.getFace() != null || event.getWorld().isRemote || !(event instanceof PlayerInteractEvent.LeftClickBlock) || event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND) == null || event.getEntityPlayer().capabilities.isCreativeMode)
            return;
        IBlockState state = event.getWorld().getBlockState(event.getPos());
        Block block = state.getBlock();
        int meta = block.getMetaFromState(state);
        if(state.getBlockHardness(event.getEntityPlayer().worldObj, event.getPos()) <= -1 &&
                event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND).getItem() == LudicrousItems.infinity_pickaxe &&
                        (state.getMaterial() == Material.ROCK || state.getMaterial() == Material.IRON)){

            if(event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND).getTagCompound() != null && event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND).getTagCompound().getBoolean("hammer")) {
                LudicrousItems.infinity_pickaxe.onBlockStartBreak(event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND), event.getPos(), event.getEntityPlayer());
            }
            else {

                if(block.quantityDropped(randy) == 0) {
                    ItemStack drop = block.getPickBlock(state, ToolHelper.raytraceFromEntity(event.getWorld(), event.getEntityPlayer(), true, 10),
                            event.getWorld(), event.getPos(), event.getEntityPlayer());
                    if(drop == null)
                        drop = new ItemStack(block, 1, meta);
                    ToolHelper.dropItem(drop, event.getEntityPlayer().worldObj, event.getPos());
                }
                else
                    block.harvestBlock(event.getWorld(), event.getEntityPlayer(), event.getPos(), state, null, null);
                event.getWorld().setBlockToAir(event.getPos());
                //event.world.playAuxSFX(2001, event.getPos(), Block.getIdFromBlock(block) + (meta << 12));
            }
        }
    }

    @SubscribeEvent
    public void handleExtraLuck(HarvestDropsEvent event) {
    	if(event.getHarvester() == null)
            return;
        if(event.getHarvester().getHeldItem(EnumHand.MAIN_HAND) == null)
            return;
        ItemStack held = event.getHarvester().getHeldItem(EnumHand.MAIN_HAND);
        if(held.getItem() == LudicrousItems.infinity_pickaxe) {
        	extraLuck(event, 4);

        	if (held.getTagCompound() != null && held.getTagCompound().getBoolean("hammer")
            	&& ToolHelper.hammering.contains(event.getHarvester())
            	&& ToolHelper.hammerdrops.containsKey(event.getHarvester())
            	&& ToolHelper.hammerdrops.get(event.getHarvester()) != null) {

            	ToolHelper.hammerdrops.get(event.getHarvester()).addAll(event.getDrops());
            	event.getDrops().clear();
            }
        }
        else if(held.getItem() == LudicrousItems.infinity_shovel) {

            if (held.getTagCompound() != null && held.getTagCompound().getBoolean("destroyer")
                    && ToolHelper.hammering.contains(event.getHarvester())
                    && ToolHelper.hammerdrops.containsKey(event.getHarvester())
                    && ToolHelper.hammerdrops.get(event.getHarvester()) != null) {

                ArrayList<ItemStack> garbage = new ArrayList<ItemStack>();
                for(ItemStack drop : event.getDrops()){
                    if(isGarbage(drop))
                        garbage.add(drop);
                }
                for(ItemStack junk : garbage){
                    event.getDrops().remove(junk);
                }
                ToolHelper.hammerdrops.get(event.getHarvester()).addAll(event.getDrops());
                event.getDrops().clear();
            }
        }
        else if(held.getItem() == LudicrousItems.infinity_axe) {

            if (ToolHelper.hammering.contains(event.getHarvester())
                    && ToolHelper.hammerdrops.containsKey(event.getHarvester())
                    && ToolHelper.hammerdrops.get(event.getHarvester()) != null) {

                ToolHelper.hammerdrops.get(event.getHarvester()).addAll(event.getDrops());
                event.getDrops().clear();
            }
        }
    }

    public static void extraLuck(HarvestDropsEvent event, int mult){
        if(event.getState().getMaterial() == Material.ROCK){
        	List<ItemStack> adds = new ArrayList<ItemStack>();
        	List<ItemStack> removals = new ArrayList<ItemStack>();
        	for(ItemStack drop : event.getDrops()){
                if(drop.getItem() != Item.getItemFromBlock(event.getState().getBlock()) && !(drop.getItem() instanceof ItemBlock)){
                    drop.stackSize = Math.min(drop.stackSize * mult, drop.getMaxStackSize());
                }
                else if(Config.fractured && drop.getItem() == Item.getItemFromBlock(event.getState().getBlock()))
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
        	event.getDrops().addAll(adds);
        	event.getDrops().removeAll(removals);
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
@SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() instanceof ItemSwordInfinity) {
            for(int x = 0;x < event.getToolTip().size();x++){
                if(event.getToolTip().get(x).contains(I18n.format("attribute.name.generic.attackDamage"))
                        || event.getToolTip().get(x).contains(I18n.format("Attack Damage"))){
                    event.getToolTip().set(x, TextFormatting.BLUE + "+" + LudicrousText.makeFabulous(I18n.format("tip.infinity")) + " " + TextFormatting.BLUE + I18n.format("attribute.name.generic.attackDamage"));
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public void onGetHurt(LivingHurtEvent event){
        if(!(event.getEntityLiving() instanceof EntityPlayer))
            return;
        EntityPlayer player = (EntityPlayer)event.getEntityLiving();
        if(player.getHeldItem(EnumHand.MAIN_HAND) != null && player.getHeldItem(EnumHand.MAIN_HAND).getItem() == LudicrousItems.infinity_sword && player.isHandActive())
            event.setCanceled(true);
        if(LudicrousItems.isInfinite(player) && !event.getSource().damageType.equals("infinity"))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onAttacked(LivingAttackEvent event) {
        if(!(event.getEntityLiving() instanceof EntityPlayer))
            return;
        if(event.getSource().getEntity() != null && event.getSource().getEntity() instanceof EntityPlayer)
            return;
        EntityPlayer player = (EntityPlayer)event.getEntityLiving();
        if(player.getHeldItem(EnumHand.MAIN_HAND) != null && player.getHeldItem(EnumHand.MAIN_HAND).getItem() == LudicrousItems.infinity_sword && player.isHandActive())
            event.setCanceled(true);
        if(LudicrousItems.isInfinite(player) && !event.getSource().damageType.equals("infinity"))
            event.setCanceled(true);
    }


    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        if(event.isRecentlyHit() && event.getEntityLiving() instanceof EntitySkeleton && event.getSource().getEntity() instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer)event.getSource().getEntity();
            if(player.getHeldItem(EnumHand.MAIN_HAND) != null && player.getHeldItem(EnumHand.MAIN_HAND).getItem() == LudicrousItems.skull_sword){
            	// ok, we need to drop a skull then.
            	if (event.getDrops().isEmpty()) {
            		addDrop(event, new ItemStack(Items.SKULL, 1, 1));
            	} else {
            		int skulls = 0;
            		
            		for (int i=0; i<event.getDrops().size(); i++) {
            			EntityItem drop = event.getDrops().get(i);
            			ItemStack stack = drop.getEntityItem();
            			if (stack.getItem() == Items.SKULL) {
            				if (stack.getItemDamage() == 1) {
            					skulls++;
            				} else if (stack.getItemDamage() == 0) {
            					skulls++;
            					stack.setItemDamage(1);
            				}
            			}
            		}
            		
            		if (skulls == 0) {
            			addDrop(event, new ItemStack(Items.SKULL, 1, 1));
            		}
            	}
                
            }
        }
    }

    @SubscribeEvent
    public void diggity(BreakSpeed event){
        if(event.getEntityLiving().getHeldItem(EnumHand.MAIN_HAND) != null){
            ItemStack held = event.getEntityLiving().getHeldItem(EnumHand.MAIN_HAND);
            if(held.getItem() == LudicrousItems.infinity_pickaxe || held.getItem() == LudicrousItems.infinity_shovel){
                if(!event.getEntityLiving().onGround)
                    event.setNewSpeed(event.getNewSpeed() *5);
                if(!event.getEntityLiving().isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(event.getEntityLiving()))
                    event.setNewSpeed(event.getNewSpeed() *5);
                if(held.getTagCompound() != null) {
                    if (held.getTagCompound().getBoolean("hammer") || held.getTagCompound().getBoolean("destroyer")) {
                        event.setNewSpeed(event.getNewSpeed() *0.5F);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void canHarvest(PlayerEvent.HarvestCheck event){
        if(event.getEntityLiving().getHeldItem(EnumHand.MAIN_HAND) != null){
            ItemStack held = event.getEntityLiving().getHeldItem(EnumHand.MAIN_HAND);
            if(held.getItem() == LudicrousItems.infinity_shovel && event.getTargetBlock().getMaterial() == Material.ROCK){
                if(held.getTagCompound() != null && held.getTagCompound().getBoolean("destroyer") && isGarbageBlock(event.getTargetBlock().getBlock()))
                    event.setResult(Event.Result.ALLOW);
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
        if(event.getEntityLiving() instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer)event.getEntityLiving();
            if(LudicrousItems.isInfinite(player) && !event.getSource().getDamageType().equals("infinity")){
                event.setCanceled(true);
                player.setHealth(player.getMaxHealth());
            }
        }
    }

    private void addDrop(LivingDropsEvent event, ItemStack drop) {
        EntityItem entityitem = new EntityItem(event.getEntityLiving().worldObj, event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, drop);
        entityitem.setDefaultPickupDelay();
        event.getDrops().add(entityitem);
    }

    @SubscribeEvent
    public void clusterClustererererer(EntityItemPickupEvent event) {
    	if(event.getEntityPlayer() != null && event.getItem().getEntityItem().getItem() == LudicrousItems.matter_cluster) {
    		ItemStack stack = event.getItem().getEntityItem();
    		EntityPlayer player = event.getEntityPlayer();
    		
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
