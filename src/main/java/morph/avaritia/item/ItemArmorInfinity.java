package morph.avaritia.item;

import morph.avaritia.client.render.entity.ModelArmorInfinity;
import morph.avaritia.Avaritia;
import morph.avaritia.init.ModItems;
import morph.avaritia.util.TextUtils;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaDiscountArmor;

import java.util.Collection;
import java.util.List;

//@formatter:off
@Optional.InterfaceList({
        @Optional.Interface(iface = "vazkii.botania.api.mana.IManaDiscountArmor", modid = "Botania"),
        @Optional.Interface(iface = "vazkii.botania.api.item.IManaProficiencyArmor", modid = "Botania")
})
//@formatter:on
public class ItemArmorInfinity extends ItemArmor implements IManaDiscountArmor, IManaProficiencyArmor {

    public static final ArmorMaterial infinite_armor = EnumHelper.addArmorMaterial("avaritia_infinity", "", 9999, new int[] { 6, 16, 12, 6 }, 1000, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0F);
    public final EntityEquipmentSlot slot;

    public ItemArmorInfinity(EntityEquipmentSlot slot) {
        super(infinite_armor, 0, slot);
        this.slot = slot;
        setCreativeTab(Avaritia.tab);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "avaritia:textures/models/infinity_armor.png";
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, 0);
    }

    @SuppressWarnings ("rawtypes")
    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (armorType == EntityEquipmentSlot.HEAD) {
            player.setAir(300);
            player.getFoodStats().addStats(20, 20F);
        } else if (armorType == EntityEquipmentSlot.CHEST) {
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
        } else if (armorType == EntityEquipmentSlot.LEGS) {
            if (player.isBurning()) {
                player.extinguish();
            }
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return ModItems.COSMIC_RARITY;
    }

    @Override
    @SideOnly (Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemstack, EntityEquipmentSlot armorSlot, ModelBiped _deafult) {
        ModelArmorInfinity model = armorSlot == EntityEquipmentSlot.LEGS ? ModelArmorInfinity.legModel : ModelArmorInfinity.armorModel;

        model.update(entityLiving, itemstack, armorSlot);

        return model;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean par4) {
        if (this.slot == EntityEquipmentSlot.FEET) {
            list.add("");
            list.add(TextFormatting.BLUE + "+" + TextFormatting.ITALIC + TextUtils.makeSANIC("SANIC") + TextFormatting.RESET + "" + TextFormatting.BLUE + "% Speed");
        }
        super.addInformation(stack, player, list, par4);
    }

    @Optional.Method (modid = "Botania")
    @Override
    public float getDiscount(ItemStack stack, int slot, EntityPlayer player) {
        return 0.25F;
    }

    @Optional.Method (modid = "Botania")
    @Override
    public boolean shouldGiveProficiency(ItemStack itemStack, int i, EntityPlayer player) {
        return true;
    }

    @Override
    @SideOnly (Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack) {
        return false;
    }

}
