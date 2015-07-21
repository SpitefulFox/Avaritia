package fox.spiteful.avaritia.items;

import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.Lumberjack;
import fox.spiteful.avaritia.render.ModelArmorInfinity;
import net.minecraft.client.model.ModelBiped;
import fox.spiteful.avaritia.PotionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.util.EnumHelper;
import org.apache.logging.log4j.Level;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;

public class ItemArmorInfinity extends ItemArmor {

    public static final ArmorMaterial infinite_armor = EnumHelper.addArmorMaterial("infinity", 9999, new int[]{6, 16, 12, 6}, 1000);
    public static final ModelArmorInfinity armorModel = new ModelArmorInfinity(1.0f);
    public static final ModelArmorInfinity legModel = new ModelArmorInfinity(0.5f);

    public ItemArmorInfinity(int slot){
        super(infinite_armor, 0, slot);
        setCreativeTab(Avaritia.tab);
        setUnlocalizedName("infinity_armor_" + slot);
        setTextureName("avaritia:infinity_armor_" + slot);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        if(slot == 2)
            return "avaritia:textures/models/infinity_pants.png";
        else
            return "avaritia:textures/models/infinity_armor.png";
    }

    @Override
    public void setDamage(ItemStack stack, int damage){
        super.setDamage(stack, 0);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
    {
        if(armorType == 0){
            player.setAir(300);
            player.getFoodStats().setFoodLevel(20);
            player.getFoodStats().setFoodSaturationLevel(20F);
        }
        else if(armorType == 1){
            player.capabilities.allowFlying = true;
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
        ModelArmorInfinity model = armorSlot == 2 ? legModel : armorModel;

        model.bipedHead.showModel = armorSlot == 0;
        model.bipedHeadwear.showModel = armorSlot == 0;
        model.bipedBody.showModel = armorSlot == 1;
        model.bipedRightArm.showModel = armorSlot == 1;
        model.bipedLeftArm.showModel = armorSlot == 1;
        model.bipedRightLeg.showModel = armorSlot == 2 || armorSlot == 3;
        model.bipedLeftLeg.showModel = armorSlot == 2 || armorSlot == 3;

        model.isSneak = entityLiving.isSneaking();
        model.isRiding = entityLiving.isRiding();
        model.isChild = entityLiving.isChild();

        model.heldItemRight = 0;
        model.aimedBow = false;

        EntityPlayer player = (EntityPlayer)entityLiving;

        ItemStack held_item = player.getEquipmentInSlot(0);

        if (held_item != null){
            model.heldItemRight = 1;

            if (player.getItemInUseCount() > 0){

                EnumAction enumaction = held_item.getItemUseAction();

                if (enumaction == EnumAction.bow){
                    model.aimedBow = true;
                }else if (enumaction == EnumAction.block){
                    model.heldItemRight = 3;
                }


            }

        }

        return model;
    }

}
