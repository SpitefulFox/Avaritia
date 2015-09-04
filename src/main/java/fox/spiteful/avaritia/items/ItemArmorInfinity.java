package fox.spiteful.avaritia.items;

import com.google.common.collect.Multimap;
import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.LudicrousText;
import fox.spiteful.avaritia.Lumberjack;
import fox.spiteful.avaritia.compat.Compat;
import fox.spiteful.avaritia.entity.EntityImmortalItem;
import fox.spiteful.avaritia.render.ICosmicRenderItem;
import fox.spiteful.avaritia.render.ModelArmorInfinity;
import net.minecraft.block.material.Material;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import fox.spiteful.avaritia.PotionHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.api.IGoggles;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.nodes.IRevealer;
import vazkii.botania.api.item.IPhantomInkable;
import vazkii.botania.api.mana.IManaDiscountArmor;
import vazkii.botania.api.item.IManaProficiencyArmor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Optional.InterfaceList({
        @Optional.Interface(iface = "thaumcraft.api.IGoggles", modid = "Thaumcraft"),
        @Optional.Interface(iface = "thaumcraft.api.nodes.IRevealer", modid = "Thaumcraft"),
        @Optional.Interface(iface = "thaumcraft.api.IVisDiscountGear", modid = "Thaumcraft"),
        @Optional.Interface(iface = "vazkii.botania.api.item.IPhantomInkable", modid = "Botania"),
        @Optional.Interface(iface = "vazkii.botania.api.mana.IManaDiscountArmor", modid = "Botania"),
        @Optional.Interface(iface = "vazkii.botania.api.item.IManaProficiencyArmor", modid = "Botania")
})
public class ItemArmorInfinity extends ItemArmor implements ICosmicRenderItem, IGoggles, IRevealer, IVisDiscountGear, IPhantomInkable, IManaDiscountArmor, IManaProficiencyArmor {

    public static final ArmorMaterial infinite_armor = EnumHelper.addArmorMaterial("infinity", 9999, new int[]{6, 16, 12, 6}, 1000);
   	static ItemStack[] armorset;
    public IIcon cosmicMask;
    public final int slot;

    public ItemArmorInfinity(int slot){
        super(infinite_armor, 0, slot);
        this.slot = slot;
        setCreativeTab(Avaritia.tab);
        setUnlocalizedName("infinity_armor_" + slot);
        setTextureName("avaritia:infinity_armor_" + slot);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        return "avaritia:textures/models/infinity_armor.png";
    }

    // Makes infinity armor basically never despawn
    @Override
	public int getEntityLifespan(ItemStack itemStack, World world) {
		return Integer.MAX_VALUE;
	}

    @Override
    public void setDamage(ItemStack stack, int damage){
        super.setDamage(stack, 0);
    }



    @SuppressWarnings("rawtypes")
	@Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
    {
        if(armorType == 0){
            player.setAir(300);
            player.getFoodStats().addStats(20, 20F);
            PotionEffect effect = player.getActivePotionEffect(Potion.nightVision);
    		if (effect == null || effect.getDuration() < 900000) player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 1000000, 1, true));

        }
        else if(armorType == 1){
            //player.capabilities.allowFlying = true;
            Collection effects = player.getActivePotionEffects();
            if(effects.size() > 0){
                ArrayList<Potion> bad = new ArrayList<Potion>();
                for(Object effect : effects){
                    if(effect instanceof PotionEffect){
                        PotionEffect potion = (PotionEffect)effect;
                        if(PotionHelper.badPotion(Potion.potionTypes[potion.getPotionID()]))
                            bad.add(Potion.potionTypes[potion.getPotionID()]);
                    }
                }
                if(bad.size() > 0){
                    for(Potion potion : bad){
                        player.removePotionEffect(potion.id);
                    }
                }
            }
        }
        else if(armorType == 2){
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
    public ModelBiped getArmorModel (EntityLivingBase entityLiving, ItemStack itemstack, int armorSlot){
        ModelArmorInfinity model = armorSlot == 2 ? ModelArmorInfinity.legModel : ModelArmorInfinity.armorModel;

        model.update(entityLiving, itemstack, armorSlot);

        return model;
    }

    // 100% knockback resistance with full set
    public static final AttributeModifier knockbackModifier = (new AttributeModifier( "knockbackResistance", 0.25D, 0)).setSaved(false);

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public Multimap getAttributeModifiers(ItemStack stack)
    {
        Multimap multimap = super.getAttributeModifiers(stack);
        //if(armorType == 3)
        //    multimap.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Armor modifier", 0.7, 1));
		if (armorType == 0)
        	multimap.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(), knockbackModifier);
        if (armorType == 1)
        	multimap.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(), knockbackModifier);
        if (armorType == 2)
        	multimap.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(), knockbackModifier);
        if (armorType == 3)
        	multimap.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(), knockbackModifier);
        return multimap;
    }

    @Optional.Method(modid = "Thaumcraft")
    @Override
    public boolean showIngamePopups(ItemStack itemStack, EntityLivingBase entityLivingBase){
        if(armorType == 0)
            return true;
        return false;
    }

    @Optional.Method(modid = "Thaumcraft")
    @Override
    public boolean showNodes(ItemStack itemStack, EntityLivingBase entityLivingBase){
        if(armorType == 0)
            return true;
        return false;
    }

    @Optional.Method(modid = "Thaumcraft")
    @Override
    public int getVisDiscount(ItemStack itemStack, EntityPlayer entityPlayer, Aspect aspect){
        return 20;
    }

    @Optional.Method(modid = "Botania")
	public String getArmorSetTitle(EntityPlayer player) {
		return StatCollector.translateToLocal("botaniamisc.armorset") + " " + getArmorSetName() + " (" + getSetPiecesEquipped(player) + "/" + getArmorSetStacks().length + ")";
	}

	@Optional.Method(modid = "Botania")
	public String getArmorSetName() {
		return StatCollector.translateToLocal("avaritia.armorset.infinity.name");
	}

	@Optional.Method(modid = "Botania")
	public void addArmorSetDescription(ItemStack stack, List<String> list) {
		addStringToTooltip(StatCollector.translateToLocal("avaritia.armorset.infinity.desc0"), list);
		addStringToTooltip(StatCollector.translateToLocal("avaritia.armorset.infinity.desc1"), list);
	}

	@Optional.Method(modid = "Botania")
	public ItemStack[] getArmorSetStacks() {
		if(armorset == null)
			armorset = new ItemStack[] {
				new ItemStack(LudicrousItems.infinity_armor),
				new ItemStack(LudicrousItems.infinity_helm),
				new ItemStack(LudicrousItems.infinity_pants),
				new ItemStack(LudicrousItems.infinity_shoes)
		};

		return armorset;
	}

	@Optional.Method(modid = "Botania")
	public void addStringToTooltip(String s, List<String> tooltip) {
		tooltip.add(s.replaceAll("&", "\u00a7"));
	}

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        if(Compat.thaumic)
            list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + ": " + this.getVisDiscount(stack, player, (Aspect)null) + "%");
        if(Compat.botan) {
            if(GuiScreen.isShiftKeyDown()) {
				addStringToTooltip(getArmorSetTitle(player), list);
				addArmorSetDescription(stack, list);
				ItemStack[] stacks = getArmorSetStacks();
				for(int i = 0; i < stacks.length; i++)
					addStringToTooltip((hasArmorSetItem(player, i) ? EnumChatFormatting.GREEN : "") + " - " + stacks[i].getDisplayName(), list);
				if(hasPhantomInk(stack))
					addStringToTooltip(StatCollector.translateToLocal("botaniamisc.hasPhantomInk"), list);
			} else addStringToTooltip(StatCollector.translateToLocal("botaniamisc.shiftinfo"), list);
        }
        if (this.slot == 3) {
        	list.add("");
        	list.add(EnumChatFormatting.BLUE+"+"+EnumChatFormatting.ITALIC+LudicrousText.makeSANIC("SANIC")+EnumChatFormatting.RESET+""+EnumChatFormatting.BLUE+"% Speed");
        }
        super.addInformation(stack, player, list, par4);
    }

    public boolean hasPhantomInk(ItemStack stack) {
        if(stack.getTagCompound() == null)
            return false;
        return stack.getTagCompound().getBoolean("phantomInk");
    }

    public void setPhantomInk(ItemStack stack, boolean ink) {

        NBTTagCompound tag = stack.getTagCompound();
        if(tag == null){
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        tag.setBoolean("phantomInk", ink);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister ir) {
        super.registerIcons(ir);

        this.cosmicMask = ir.registerIcon("avaritia:infinity_armor_" + slot + "_mask");
    }
    
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getMaskTexture(ItemStack stack, EntityPlayer player) {
		return this.cosmicMask;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public float getMaskMultiplier(ItemStack stack, EntityPlayer player) {
		return 1.0f;
	}
	
	@Override
	public boolean hasCustomEntity (ItemStack stack)
    {
        return true;
    }

	@Override
    public Entity createEntity (World world, Entity location, ItemStack itemstack)
    {
        return new EntityImmortalItem(world, location, itemstack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack, int pass)
    {
        return false;
    }

    public boolean hasArmorSet(EntityPlayer player) {
		return hasArmorSetItem(player, 0) && hasArmorSetItem(player, 1) && hasArmorSetItem(player, 2) && hasArmorSetItem(player, 3);
	}

    public boolean hasArmorSetItem(EntityPlayer player, int i) {
		ItemStack stack = player.inventory.armorInventory[3 - i];
		if(stack == null)
			return false;

		switch(i) {
			case 0: return stack.getItem() == LudicrousItems.infinity_helm;
			case 1: return stack.getItem() == LudicrousItems.infinity_armor;
			case 2: return stack.getItem() == LudicrousItems.infinity_pants;
			case 3: return stack.getItem() == LudicrousItems.infinity_shoes;
		}

		return false;
	}

	public int getSetPiecesEquipped(EntityPlayer player) {
		int pieces = 0;
		for(int i = 0; i < 4; i++)
			if(hasArmorSetItem(player, i))
				pieces++;

		return pieces;
	}

	@Override
	public float getDiscount(ItemStack stack, int slot, EntityPlayer player) {
		return hasArmorSet(player) ? 0.40F : 0F;
	}

	@Override
	public boolean shouldGiveProficiency(ItemStack stack, int slot, EntityPlayer player) {
		return hasArmorSet(player);
	}

	public static class abilityHandler {
		public static List<String> playersWithHat = new ArrayList<String>();
		public static List<String> playersWithChest = new ArrayList<String>();
		public static List<String> playersWithLeg = new ArrayList<String>();
		public static List<String> playersWithFoot = new ArrayList<String>();
		
		public static boolean playerHasHat(EntityPlayer player) {
			ItemStack armour = player.getCurrentArmor(3);
			return armour != null && armour.getItem() == LudicrousItems.infinity_helm;
		}
		
		public static boolean playerHasChest(EntityPlayer player) {
			ItemStack armour = player.getCurrentArmor(2);
			return armour != null && armour.getItem() == LudicrousItems.infinity_armor;
		}
		
		public static boolean playerHasLeg(EntityPlayer player) {
			ItemStack armour = player.getCurrentArmor(1);
			return armour != null && armour.getItem() == LudicrousItems.infinity_pants;
		}
		
		public static boolean playerHasFoot(EntityPlayer player) {
			ItemStack armour = player.getCurrentArmor(0);
			return armour != null && armour.getItem() == LudicrousItems.infinity_shoes;
		}
		
		public static String playerKey(EntityPlayer player) {
			return player.getGameProfile().getName() +":"+ player.worldObj.isRemote;
		}
		
		@SubscribeEvent
		public void updatePlayerAbilityStatus(LivingUpdateEvent event) {
			if (event.entityLiving instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)event.entityLiving;
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
						boolean swimming = player.isInsideOfMaterial(Material.water) || player.isInWater();
						World world = player.worldObj;
						boolean sneaking = player.isSneaking();
						float speed = 0.15f 
							* (flying ? 1.1f : 1.0f) 
							//* (swimming ? 1.2f : 1.0f) 
							* (sneaking ? 0.1f : 1.0f);
    					int x = MathHelper.floor_double(player.posX);
    					int y = MathHelper.floor_double(player.boundingBox.minY - 0.11f);
    					int yPaddle = MathHelper.floor_double(player.boundingBox.minY);
    					int z = MathHelper.floor_double(player.posZ);
    					Material mWater = world.getBlock(x, y, z).getMaterial();
    					Material mPaddle = world.getBlock(x, yPaddle, z).getMaterial();
    					boolean waterBelow = (mWater == Material.water);
    					boolean paddlingInWater = (mPaddle == Material.water);
    					// Water walking effect
    					if (waterBelow && player.motionY < 0.0D && !player.isSneaking()) {
    						if (player instanceof EntityPlayerMP) {
      							player.posY -= player.yOffset;
    						} else {
      							player.posY -= player.motionY;
      						}
   							player.motionY = 0.0D;
    						player.fallDistance = 0.0F;
    						if (player.moveForward > 0f) {
								player.moveFlying(0f, 1f, speed);
							} else if (player.moveForward < 0f) {
								player.moveFlying(0f, 1f, -speed * 0.3f);
							}
							if (player.moveStrafing != 0f) {
								player.moveFlying(1f, 0f, speed * 0.5f * Math.signum(player.moveStrafing));
							}
    					}
    					if ((player.isInWater() || paddlingInWater) && !player.isSneaking()) {
      						player.motionY = 0.1f;
    					}
						if (player.onGround || flying || swimming) {
							//step height 
							if ((player.onGround) && player.moveForward > 0F){
        						player.stepHeight = 1F;
      						}

							if (player.moveForward > 0f) {
								player.moveFlying(0f, 1f, speed);
							} else if (player.moveForward < 0f) {
								player.moveFlying(0f, 1f, -speed * 0.3f);
							}
							
							if (player.moveStrafing != 0f) {
								player.moveFlying(1f, 0f, speed * 0.5f * Math.signum(player.moveStrafing));
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
			if (event.entityLiving instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer)event.entityLiving;
				String key = playerKey(player);
				
				if (playersWithFoot.contains(key)) {
					player.motionY += 0.4f;
				}
			}
		}
	}
}
