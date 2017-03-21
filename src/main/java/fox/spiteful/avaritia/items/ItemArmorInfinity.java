package fox.spiteful.avaritia.items;

import com.google.common.collect.Multimap;
import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.LudicrousText;
import fox.spiteful.avaritia.ModGlobals;
import fox.spiteful.avaritia.compat.Compat;
import fox.spiteful.avaritia.entity.EntityImmortalItem;
import fox.spiteful.avaritia.render.ICosmicRenderItem;
import fox.spiteful.avaritia.render.ModelArmorInfinity;
import net.minecraft.block.material.Material;
import net.minecraft.client.model.ModelBiped;
import fox.spiteful.avaritia.PotionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaDiscountArmor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



@Optional.InterfaceList({
        @Optional.Interface(iface = "vazkii.botania.api.mana.IManaDiscountArmor", modid = "Botania"),
        @Optional.Interface(iface = "vazkii.botania.api.item.IManaProficiencyArmor", modid = "Botania")
})
public class ItemArmorInfinity extends ItemArmor implements IManaDiscountArmor, IManaProficiencyArmor {

    public static final ArmorMaterial infinite_armor = EnumHelper.addArmorMaterial(ModGlobals.MODID+":infinity", "", 9999, new int[]{6, 16, 12, 6}, 1000, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F);
    public final EntityEquipmentSlot slot;

    public ItemArmorInfinity(EntityEquipmentSlot slot){
        super(infinite_armor, 0, slot);
        this.slot = slot;
        setCreativeTab(Avaritia.tab);
         }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
    {
        return "avaritia:textures/models/infinity_armor.png";
    }

    @Override
    public void setDamage(ItemStack stack, int damage){
        super.setDamage(stack, 0);
    }

    @SuppressWarnings("rawtypes")
	@Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
    {
        if(armorType == EntityEquipmentSlot.HEAD){
            player.setAir(300);
            player.getFoodStats().addStats(20, 20F);
        }
        else if (armorType == EntityEquipmentSlot.CHEST) {
            player.capabilities.allowFlying = true;
            Collection effects = player.getActivePotionEffects();
            if (effects.size() > 0) {
                for (Object effect : effects) {
                    if (effect instanceof PotionEffect) {
                        PotionEffect potion = (PotionEffect) effect;
                        if (potion.getPotion().isBadEffect()) {
                            player.removePotionEffect(potion.getPotion());
                        }
                    }
                }
            }
        }
        else if(armorType == EntityEquipmentSlot.LEGS){
            if(player.isBurning())
                player.extinguish();
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return LudicrousItems.cosmic;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemstack, EntityEquipmentSlot armorSlot, ModelBiped _deafult){
        ModelArmorInfinity model = armorSlot == EntityEquipmentSlot.LEGS ? ModelArmorInfinity.legModel : ModelArmorInfinity.armorModel;

        model.update(entityLiving, itemstack, armorSlot);

        return model;
    }


    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean par4) {
        if (this.slot == EntityEquipmentSlot.FEET) {
        	list.add("");
        	list.add(TextFormatting.BLUE+"+"+TextFormatting.ITALIC+LudicrousText.makeSANIC("SANIC")+TextFormatting.RESET+""+TextFormatting.BLUE+"% Speed");
        }
        super.addInformation(stack, player, list, par4);
    }


    @Optional.Method(modid = "Botania")
    @Override
    public float getDiscount(ItemStack stack, int slot, EntityPlayer player){
        return 0.25F;
    }

    @Optional.Method(modid = "Botania")
    @Override
    public boolean shouldGiveProficiency(ItemStack itemStack, int i, EntityPlayer player){
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack)
    {
        return false;
    }

	public static class abilityHandler {
		public static List<String> playersWithHat = new ArrayList<String>();
		public static List<String> playersWithChest = new ArrayList<String>();
		public static List<String> playersWithLeg = new ArrayList<String>();
		public static List<String> playersWithFoot = new ArrayList<String>();
		
		public static boolean playerHasHat(EntityPlayer player) {
			ItemStack armour = player.inventory.armorItemInSlot(3);
			return armour != null && armour.getItem() == LudicrousItems.infinity_helm;
		}
		
		public static boolean playerHasChest(EntityPlayer player) {
			ItemStack armour = player.inventory.armorItemInSlot(2);
			return armour != null && armour.getItem() == LudicrousItems.infinity_armor;
		}
		
		public static boolean playerHasLeg(EntityPlayer player) {
			ItemStack armour = player.inventory.armorItemInSlot(1);
			return armour != null && armour.getItem() == LudicrousItems.infinity_pants;
		}
		
		public static boolean playerHasFoot(EntityPlayer player) {
			ItemStack armour = player.inventory.armorItemInSlot(0);
			return armour != null && armour.getItem() == LudicrousItems.infinity_shoes;
		}
		
		public static String playerKey(EntityPlayer player) {
			return player.getGameProfile().getName() +":"+ player.worldObj.isRemote;
		}
		
		@SubscribeEvent
		public void updatePlayerAbilityStatus(LivingUpdateEvent event) {
			if (event.getEntityLiving() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)event.getEntityLiving();
				String key = playerKey(player);
				
				// hat
				Boolean hasHat = playerHasHat(player);
				if (playersWithHat.contains(key)) {
					if (hasHat) {
						
					} else {
						playersWithHat.remove(key);
					}
				} else if (hasHat) {
					playersWithHat.add(key);
				}
				
				// chest
				Boolean hasChest = playerHasChest(player);
				if (playersWithChest.contains(key)) {
					if (hasChest) {
						player.capabilities.allowFlying = true;
					} else {
						if (!player.capabilities.isCreativeMode) {
							player.capabilities.allowFlying = false;
							player.capabilities.isFlying = false;
						}
						playersWithChest.remove(key);
					}
				} else if (hasChest) {
					playersWithChest.add(key);
				}
				
				// legs
				Boolean hasLeg = playerHasLeg(player);
				if (playersWithLeg.contains(key)) {
					if (hasLeg) {
						
					} else {
						playersWithLeg.remove(key);
					}
				} else if (hasLeg) {
					playersWithLeg.add(key);
				}
				
				// shoes
				Boolean hasFoot = playerHasFoot(player);
				if (playersWithFoot.contains(key)) {
					if (hasFoot) {
						boolean flying = player.capabilities.isFlying;
						boolean swimming = player.isInsideOfMaterial(Material.WATER) || player.isInWater();
						if (player.onGround || flying || swimming) {
							boolean sneaking = player.isSneaking();
							
							float speed = 0.15f 
								* (flying ? 1.1f : 1.0f) 
								//* (swimming ? 1.2f : 1.0f) 
								* (sneaking ? 0.1f : 1.0f); 
							
							if (player.moveForward > 0f) {
								player.moveRelative(0f, 1f, speed);
							} else if (player.moveForward < 0f) {
								player.moveRelative(0f, 1f, -speed * 0.3f);
							}
							
							if (player.moveStrafing != 0f) {
								player.moveRelative(1f, 0f, speed * 0.5f * Math.signum(player.moveStrafing));
							}
						}
					} else {
						playersWithFoot.remove(key);
					}
				} else if (hasFoot) {
					playersWithFoot.add(key);
				}
			}
		}
		
		@SubscribeEvent
		public void jumpBoost(LivingJumpEvent event) {
			if (event.getEntityLiving() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)event.getEntityLiving();
				String key = playerKey(player);
				
				if (playersWithFoot.contains(key)) {
					player.motionY += 0.4f;
				}
			}
		}
	}
}
