package fox.spiteful.avaritia.compat.ticon;

import java.util.Random;

import fox.spiteful.avaritia.Config;
import fox.spiteful.avaritia.LudicrousEvents;
import morph.avaritia.util.ToolHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.events.TinkerEvent;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.tools.ToolCore;

public class TonkersEvents {
	private Random randy = new Random();

	@SubscribeEvent
	public void craftTool(TinkerEvent.OnItemBuilding event) {
		NBTTagCompound toolTag = event.tag.getCompoundTag("InfiTool");
		handleInfinityMods(toolTag, (ToolCore) event.tool);
	}

	private void handleInfinityMods(NBTTagCompound toolTag, ToolCore tool) {
		int plusmod = 5;
		int modifiers = toolTag.getInteger("Modifiers");
		if (toolTag.getInteger("Head") == Tonkers.infinityMetalId) {
			modifiers += plusmod;
		}
		if (toolTag.getInteger("Handle") == Tonkers.infinityMetalId) {
			modifiers += plusmod;
		}
		if (toolTag.getInteger("Accessory") == Tonkers.infinityMetalId) {
			modifiers += plusmod;
		}
		if (toolTag.getInteger("Extra") == Tonkers.infinityMetalId) {
			modifiers += plusmod;
		}

		// 2 part tools gain 2 modifiers for the head
		if (tool.getRequiredComponents().size() == 2 && toolTag.getInteger("Head") == Tonkers.infinityMetalId) {
			modifiers += plusmod;
		}

		toolTag.setInteger("Modifiers", modifiers);
	}

	@SubscribeEvent
	public void handleExtraLuck(HarvestDropsEvent event) {
		if (event.getHarvester() == null) {
			return;
		}
		if (event.getHarvester().getHeldItem(EnumHand.MAIN_HAND) == null) {
			return;
		}
		ItemStack held = event.getHarvester().getHeldItem(EnumHand.MAIN_HAND);
		if (held != null && held.hasTagCompound() && held.getItem() instanceof ToolCore) {
			ToolCore tool = (ToolCore) held.getItem();
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
				if (parts == tool.getRequiredComponents().size()) {
					luck++;
					if (tool.getRequiredComponents().size() == 2) {
						luck++;
					}
				}

				LudicrousEvents.extraLuck(event, luck);
			}
		}
	}

	@SubscribeEvent
	public void onHurt(LivingHurtEvent event) {
		Entity damaged = event.getEntity();
		Entity damager = event.getSource().getSourceOfDamage();
		if (damager != null && damager instanceof EntityLivingBase && damaged instanceof EntityLivingBase) {
			EntityLivingBase attacker = (EntityLivingBase) damager;
			EntityLivingBase attacked = (EntityLivingBase) damaged;

			ItemStack held = attacker.getHeldItem(EnumHand.MAIN_HAND);
			if (held != null && held.hasTagCompound() && held.getItem() instanceof ToolCore) {
				//ToolCore tool = (ToolCore) held.getItem();
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

				//float knockback = (AbilityHelper.calcKnockback(attacker, attacked, held, tool, toolTag, 0) + 1) * puntpower;
				float knockback = (ModifierNBT.readInteger(toolTag).current + 1) * puntpower;
				attacked.addVelocity(-MathHelper.sin(attacker.rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F, 0.1D, MathHelper.cos(attacker.rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F);
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
		if (!Config.bedrockBreaker || event.getFace() != null || event.getWorld().isRemote || !(event instanceof PlayerInteractEvent.LeftClickBlock) || event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND) == null || event.getEntityPlayer().capabilities.isCreativeMode) {
			return;
		}
		IBlockState state = event.getWorld().getBlockState(event.getPos());
		Block block = state.getBlock();
		int meta = block.getMetaFromState(state);
		ItemStack held = event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND);
		if (state.getBlockHardness(event.getEntityPlayer().worldObj, event.getPos()) <= -1 && held.hasTagCompound() && held.getItem() instanceof ToolCore && (state.getMaterial() == Material.ROCK || state.getMaterial() == Material.IRON)) {

			NBTTagCompound toolTag = held.getTagCompound().getCompoundTag("InfiTool");
			ToolCore tool = (ToolCore) held.getItem();

			if (toolTag != null && toolTag.getInteger("Head") == Tonkers.infinityMetalId && tool.HarvestBlock(Blocks.STONE.getDefaultState(), held)) {
				if (block.quantityDropped(randy) == 0) {
					ItemStack drop = block.getPickBlock(state, ToolHelper.raytraceFromEntity(event.getWorld(), event.getEntityPlayer(), true, 10), event.getWorld(), event.getPos(), event.getEntityPlayer());
					if (drop == null) {
						drop = new ItemStack(block, 1, meta);
					}
					ToolHelper.dropItem(drop, event.getEntityPlayer().worldObj, event.getPos());
				}
				else {
					block.harvestBlock(event.getWorld(), event.getEntityPlayer(), event.getPos(), state, null, null);
				}
				event.getEntityPlayer().worldObj.setBlockToAir(event.getPos());
				//event.getWorld().playAuxSFX(2001, event.x, event.y, event.z, Block.getIdFromBlock(block) + (meta << 12));
			}
		}
	}
}
