package fox.spiteful.avaritia.items;

import com.google.common.collect.Multimap;
import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.Lumberjack;
import fox.spiteful.avaritia.compat.Compat;
import fox.spiteful.avaritia.render.ModelArmorInfinity;
import net.minecraft.client.model.ModelBiped;
import fox.spiteful.avaritia.PotionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import org.apache.logging.log4j.Level;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.common.Optional;
import thaumcraft.api.IGoggles;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.nodes.IRevealer;
import vazkii.botania.api.item.IPhantomInkable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Optional.InterfaceList({
        @Optional.Interface(iface = "thaumcraft.api.IGoggles", modid = "Thaumcraft"),
        @Optional.Interface(iface = "thaumcraft.api.nodes.IRevealer", modid = "Thaumcraft"),
        @Optional.Interface(iface = "vazkii.botania.api.item.IPhantomInkable", modid = "Botania")
})
public class ItemArmorInfinity extends ItemArmor implements IGoggles, IRevealer, IVisDiscountGear, IPhantomInkable {

    public static final ArmorMaterial infinite_armor = EnumHelper.addArmorMaterial("infinity", 9999, new int[]{6, 16, 12, 6}, 1000);
    @SideOnly(Side.CLIENT)
    public static final ModelArmorInfinity armorModel = new ModelArmorInfinity(1.0f);
    @SideOnly(Side.CLIENT)
    public static final ModelArmorInfinity legModel = new ModelArmorInfinity(0.5f).setLegs(true);

    public ItemArmorInfinity(int slot){
        super(infinite_armor, 0, slot);
        setCreativeTab(Avaritia.tab);
        setUnlocalizedName("infinity_armor_" + slot);
        setTextureName("avaritia:infinity_armor_" + slot);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        //if(slot == 2)
        //    return "avaritia:textures/models/infinity_pants.png";
        //else
        if(Compat.botan && hasPhantomInk(stack))
            return "botania:textures/model/invisibleArmor.png";
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

        model.update(entityLiving, itemstack, armorSlot);

        return model;
    }

    @Override
    public Multimap getAttributeModifiers(ItemStack stack)
    {
        Multimap multimap = super.getAttributeModifiers(stack);
        if(armorType == 3)
            multimap.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Armor modifier", 0.7, 1));
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

    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        if(Compat.thaumic)
            list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + ": " + this.getVisDiscount(stack, player, (Aspect)null) + "%");
        if(Compat.botan) {
            if (hasPhantomInk(stack))
                list.add(StatCollector.translateToLocal("botaniamisc.hasPhantomInk").replaceAll("&", "\u00a7"));
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

}
