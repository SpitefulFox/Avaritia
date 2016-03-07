package fox.spiteful.avaritia.compat.bloodmagic;

import WayofTime.alchemicalWizardry.api.altarRecipeRegistry.AltarRecipeRegistry;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBindable;
import WayofTime.alchemicalWizardry.api.items.interfaces.IBloodOrb;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemOrbArmok extends Item implements IBloodOrb, IBindable {

    public ItemOrbArmok(){
        setMaxStackSize(1);
        this.setUnlocalizedName("orb_armok");
        this.setTextureName("avaritia:orb_armok");
        setCreativeTab(Avaritia.tab);
        AltarRecipeRegistry.registerAltarOrbRecipe(new ItemStack(this), 1, 140);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
        if (!world.isRemote)
            SoulNetworkHandler.checkAndSetItemOwner(itemstack, player);

        return itemstack;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if(!world.isRemote && entity instanceof EntityPlayer){
            NBTTagCompound itemTag = stack.stackTagCompound;
            if (itemTag == null || itemTag.getString("ownerName").equals(""))
                return;

            SoulNetworkHandler.setCurrentEssence(itemTag.getString("ownerName"), getMaxEssence());
        }
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List tooltip, boolean wut)
    {
        tooltip.add(StatCollector.translateToLocal("tooltip.armok.desc"));
        if (item.getTagCompound() != null)
        {
            tooltip.add(StatCollector.translateToLocal("tooltip.owner.currentowner") + " " + item.getTagCompound().getString("ownerName"));
        }
    }

    @Override
    public int getMaxEssence() {
        return 1000000000;
    }

    @Override
    public int getOrbLevel() {
        return 6;
    }

    @Override
    public EnumRarity getRarity(ItemStack itemstack) {
        return LudicrousItems.cosmic;
    }

    @Override
    public boolean hasContainerItem()
    {
        return true;
    }

    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemStack)
    {
        return false;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        return itemStack;
    }

}
