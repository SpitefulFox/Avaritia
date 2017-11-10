package fox.spiteful.avaritia.items;

import com.google.common.collect.Multimap;
import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.Config;
import fox.spiteful.avaritia.LudicrousText;
import fox.spiteful.avaritia.compat.Belmont;
import fox.spiteful.avaritia.compat.Compat;
import fox.spiteful.avaritia.entity.EntityImmortalItem;
import fox.spiteful.avaritia.render.ICosmicRenderItem;
import fox.spiteful.avaritia.render.ModelArmorInfinity;
import net.minecraft.block.material.Material;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import fox.spiteful.avaritia.PotionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
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
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.item.IPhantomInkable;
import vazkii.botania.api.mana.IManaDiscountArmor;

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
public class ItemArmorInfinity extends ItemArmor implements ICosmicRenderItem, IGoggles, IRevealer, IVisDiscountGear, IPhantomInkable,
        IManaDiscountArmor, IManaProficiencyArmor {

    public static final ArmorMaterial infinite_armor = EnumHelper.addArmorMaterial("infinity", 9999, new int[]{6, 16, 12, 6}, 1000);
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public Multimap getAttributeModifiers(ItemStack stack)
    {
        Multimap multimap = super.getAttributeModifiers(stack);
        //if(armorType == 3)
        //    multimap.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Armor modifier", 0.7, 1));
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        if(Compat.thaumic)
            list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + ": " + this.getVisDiscount(stack, player, (Aspect)null) + "%");
        if(Compat.botan) {
            if (hasPhantomInk(stack))
                list.add(StatCollector.translateToLocal("botaniamisc.hasPhantomInk").replaceAll("&", "\u00a7"));
        }
        if (this.slot == 3 && Config.fast) {
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
				Boolean hasFoot = playerHasFoot(player) && Config.fast;
				if (playersWithFoot.contains(key)) {
					if (hasFoot) {
						boolean flying = player.capabilities.isFlying;
						boolean swimming = player.isInsideOfMaterial(Material.water) || player.isInWater();
						if (player.onGround || flying || swimming) {
							boolean sneaking = player.isSneaking();
							
							float speed = 0.15f 
								* (flying ? 1.1f : 1.0f) 
								//* (swimming ? 1.2f : 1.0f) 
								* (sneaking ? 0.1f : 1.0f); 
							
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
