package fox.spiteful.avaritia.compat.ticon;

import java.util.Random;

import fox.spiteful.avaritia.Config;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import tconstruct.library.event.ToolCraftEvent;
import tconstruct.library.tools.AbilityHelper;
import tconstruct.library.tools.ToolCore;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fox.spiteful.avaritia.LudicrousEvents;
import fox.spiteful.avaritia.Lumberjack;
import fox.spiteful.avaritia.items.LudicrousItems;
import fox.spiteful.avaritia.items.tools.ToolHelper;

public class TonkersEvents {
	private Random randy = new Random();
	
	@SubscribeEvent
    public void craftTool(ToolCraftEvent.NormalTool event)
    {
		NBTTagCompound toolTag = event.toolTag.getCompoundTag("InfiTool");
		handleInfinityMods(toolTag, event.tool);
    }
	
	private void handleInfinityMods (NBTTagCompound toolTag, ToolCore tool)
    {
		int plusmod = 5;
        int modifiers = toolTag.getInteger("Modifiers");
        if (toolTag.getInteger("Head") == Tonkers.infinityMetalId)
            modifiers += plusmod;
        if (toolTag.getInteger("Handle") == Tonkers.infinityMetalId)
            modifiers += plusmod;
        if (toolTag.getInteger("Accessory") == Tonkers.infinityMetalId)
            modifiers += plusmod;
        if (toolTag.getInteger("Extra") == Tonkers.infinityMetalId)
            modifiers += plusmod;

        // 2 part tools gain 2 modifiers for the head
        if (tool.getPartAmount() == 2 && toolTag.getInteger("Head") == Tonkers.infinityMetalId)
            modifiers += plusmod;

        toolTag.setInteger("Modifiers", modifiers);
    }
	
	@SubscribeEvent
    public void handleExtraLuck(HarvestDropsEvent event) {
		if(event.harvester == null)
            return;
        if(event.harvester.getHeldItem() == null)
            return;
        ItemStack held = event.harvester.getHeldItem();
        if (held != null && held.hasTagCompound() && held.getItem() instanceof ToolCore)
        {
        	ToolCore tool = (ToolCore)held.getItem();
        	NBTTagCompound toolTag = held.getTagCompound().getCompoundTag("InfiTool");
        	
        	if (toolTag.getInteger("Head") == Tonkers.infinityMetalId) {
        		int parts = 1;
        		if (toolTag.getInteger("Handle") == Tonkers.infinityMetalId) {
        			parts++;
        		}
                if (toolTag.getInteger("Accessory") == Tonkers.infinityMetalId) {
                	parts++;
                }
                if (toolTag.getInteger("Extra") == Tonkers.infinityMetalId) {
                	parts++;
                }
                
                int luck = Math.min(3, parts);
                if (parts == tool.getPartAmount()) {
                	luck++;
                	if (tool.getPartAmount() == 2) {
                		luck++;
                	}
                }
                
                LudicrousEvents.extraLuck(event, luck);
        	}
        }
	}
	
	@SubscribeEvent
	public void onHurt(LivingHurtEvent event) {
		Entity damaged = event.entity;
		Entity damager = event.source.getSourceOfDamage();
		if (damager != null && damager instanceof EntityLivingBase && damaged instanceof EntityLivingBase) {
			EntityLivingBase attacker = (EntityLivingBase)damager;
			EntityLivingBase attacked = (EntityLivingBase)damaged;
			
			ItemStack held = attacker.getHeldItem();
			if (held != null && held.hasTagCompound() && held.getItem() instanceof ToolCore) {
				ToolCore tool = (ToolCore)held.getItem();
	        	NBTTagCompound toolTag = held.getTagCompound().getCompoundTag("InfiTool");
	        	
	        	float puntpower = 0.0f;
	        	float puntboost = 1.5f;
	        	if (toolTag.getInteger("Head") == Tonkers.neutroniumId) {
	        		puntpower += puntboost;
	        	}
				if (toolTag.getInteger("Handle") == Tonkers.neutroniumId) {
					puntpower += puntboost;     		
				}
				if (toolTag.getInteger("Accessory") == Tonkers.neutroniumId) {
					puntpower += puntboost;
				}
				if (toolTag.getInteger("Extra") == Tonkers.neutroniumId) {
					puntpower += puntboost;
				}
				
				float knockback = (AbilityHelper.calcKnockback(attacker, attacked, held, tool, toolTag, 0) + 1) * puntpower;
				attacked.addVelocity((double) (-MathHelper.sin(attacker.rotationYaw * (float) Math.PI / 180.0F) * (float) knockback * 0.5F), 0.1D, (double) (MathHelper.cos(attacker.rotationYaw * (float) Math.PI / 180.0F) * (float) knockback * 0.5F));
			}
		}
	}

	/*@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		ItemStack held = event.entityLiving.getHeldItem();
		if (held != null && held.hasTagCompound() && held.getItem() instanceof ToolCore) {
			ToolCore tool = (ToolCore)held.getItem();
        	NBTTagCompound toolTag = held.getTagCompound().getCompoundTag("InfiTool");
        	
        	int chance = 0;
        	int per = 1;
        	if (toolTag.getInteger("Head") == Tonkers.infinityMetalId) {
        		chance += per;
        	}
			if (toolTag.getInteger("Handle") == Tonkers.infinityMetalId) {
				chance += per;
			}
			if (toolTag.getInteger("Accessory") == Tonkers.infinityMetalId) {
				chance += per;
			}
			if (toolTag.getInteger("Extra") == Tonkers.infinityMetalId) {
				chance += per;
			}
			
			if (randy.nextInt(60) < chance) {
				AbilityHelper.healTool(held, MathHelper.floor_float(tool.getDurabilityModifier()), event.entityLiving, true);
			}
		}
	}*/
	
	// bedrock SMASH!
	@SubscribeEvent
    public void onPlayerMine(PlayerInteractEvent event) {
        if(!Config.bedrockBreaker || event.face == -1 || event.world.isRemote || event.action != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK || event.entityPlayer.getHeldItem() == null || event.entityPlayer.capabilities.isCreativeMode)
            return;
        Block block = event.world.getBlock(event.x, event.y, event.z);
        int meta = event.world.getBlockMetadata(event.x, event.y, event.z);
        ItemStack held = event.entityPlayer.getHeldItem();
        if(block.getBlockHardness(event.entityPlayer.worldObj, event.x, event.y, event.z) <= -1 &&
                held.hasTagCompound() && held.getItem() instanceof ToolCore &&
                        (block.getMaterial() == Material.rock || block.getMaterial() == Material.iron)){

        	NBTTagCompound toolTag = held.getTagCompound().getCompoundTag("InfiTool");
        	ToolCore tool = (ToolCore)held.getItem();
        	
        	if (toolTag != null && toolTag.getInteger("Head") == Tonkers.infinityMetalId && tool.canHarvestBlock(Blocks.stone, held)) {
	            if(block.quantityDropped(randy) == 0) {
	                ItemStack drop = block.getPickBlock(ToolHelper.raytraceFromEntity(event.world, event.entityPlayer, true, 10),
	                        event.world, event.x, event.y, event.z, event.entityPlayer);
	                if(drop == null)
	                    drop = new ItemStack(block, 1, meta);
	                LudicrousEvents.dropItem(drop, event.entityPlayer.worldObj, event.x, event.y, event.z);
	            }
	            else {
	            	block.harvestBlock(event.world, event.entityPlayer, event.x, event.y, event.z, meta);
	            }
	            event.entityPlayer.worldObj.setBlockToAir(event.x, event.y, event.z);
	            event.world.playAuxSFX(2001, event.x, event.y, event.z, Block.getIdFromBlock(block) + (meta << 12));
        	}
        }
    }
}
