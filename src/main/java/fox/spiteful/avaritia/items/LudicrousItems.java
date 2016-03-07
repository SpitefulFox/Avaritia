package fox.spiteful.avaritia.items;

import cpw.mods.fml.common.registry.GameRegistry;
import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.Config;
import fox.spiteful.avaritia.items.tools.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;

import static net.minecraft.item.Item.ToolMaterial;

public class LudicrousItems {

    /**
     * 0 = Diamond Lattice,
     * 1 = Crystal Matrix Ingot,
     * 2 = Pile of Neutrons,
     * 3 = Neutronium Nugget,
     * 4 = Neutronium Ingot,
     * 5 = Infinity Catalyst,
     * 6 = Infinity Ingot,
     * 7 = Record Fragment
     */
    public static Item resource;

    /**
     * 0 = Iron,
     * 1 = Gold,
     * 2 = Lapis,
     * 3 = Redstone,
     * 4 = Nether Quartz,
     * 5 = Copper,
     * 6 = Tin,
     * 7 = Lead,
     * 8 = Silver,
     * 9 = Nickel
     */
    public static Item singularity;

    public static Item skull_sword;
    public static Item ultimate_stew;
    public static Item cosmic_meatballs;
    public static Item endest_pearl;
    public static Item fractured_ore;
    public static Item matter_cluster;
    //public static Item morv_in_a_box;

    public static Item infinity_pickaxe;
    public static Item infinity_sword;
    public static Item infinity_shovel;
    public static Item infinity_axe;
    public static Item infinity_bow;

    public static Item infinity_helm;
    public static Item infinity_armor;
    public static Item infinity_pants;
    public static Item infinity_shoes;

    public static Item akashic_record;
    public static Item armok_orb;
    public static Item combs;
    public static Item beesource;
    public static Item bigPearl;
    
    

    public static EnumRarity cosmic = EnumHelper.addRarity("COSMIC", EnumChatFormatting.RED, "Cosmic");

    public static void grind(){
        resource = new ItemResource();
        GameRegistry.registerItem(resource, "Resource");

        if(Config.craftingOnly)
            return;

        singularity = register(new ItemSingularity(), "Singularity");
        infinity_pickaxe = register(new ItemPickaxeInfinity(), "Infinity_Pickaxe");
        infinity_sword = register(new ItemSwordInfinity(), "Infinity_Sword");
        skull_sword = register(new ItemSwordSkulls(), "Skull_Sword");
        ultimate_stew = register(new ItemFood(20,20.0f,false).setPotionEffect(Potion.regeneration.getId(), 300, 1, 1.0f).setTextureName("avaritia:stew").setUnlocalizedName("avaritia_stew").setCreativeTab(Avaritia.tab), "Ultimate_Stew");
        cosmic_meatballs = register(new ItemFood(20,20.0f,false).setPotionEffect(Potion.damageBoost.getId(), 300, 1, 1.0f).setTextureName("avaritia:meatballs").setUnlocalizedName("avaritia_meatballs").setCreativeTab(Avaritia.tab), "Cosmic_Meatballs");
        endest_pearl = register(new ItemEndestPearl(), "Endest_Pearl");
        infinity_helm = register(new ItemArmorInfinity(0), "Infinity_Helm");
        infinity_armor = register(new ItemArmorInfinity(1), "Infinity_Chest");
        infinity_pants = register(new ItemArmorInfinity(2), "Infinity_Pants");
        infinity_shoes = register(new ItemArmorInfinity(3), "Infinity_Shoes");
        if(Config.fractured)
            fractured_ore = register(new ItemFracturedOre(), "Fractured_Ore");
        matter_cluster = register(new ItemMatterCluster(), "Matter_Cluster");
        infinity_bow = register(new ItemBowInfinity(), "Infinity_Bow");
        infinity_shovel = register(new ItemShovelInfinity(), "Infinity_Shovel");
        infinity_axe = register(new ItemAxeInfinity(), "Infinity_Axe");
        //morv_in_a_box = register(new ItemMorvInABox(), "MorvInABox");
        
        MinecraftForge.EVENT_BUS.register(new ItemArmorInfinity.abilityHandler());
    }

    public static boolean isInfinite(EntityPlayer player){
        if(player.getEquipmentInSlot(1) == null || player.getEquipmentInSlot(2) == null
                || player.getEquipmentInSlot(3) == null || player.getEquipmentInSlot(4) == null)
            return false;
        if(player.getEquipmentInSlot(1).getItem() == infinity_shoes && player.getEquipmentInSlot(2).getItem() == infinity_pants
                && player.getEquipmentInSlot(3).getItem() == infinity_armor && player.getEquipmentInSlot(4).getItem() == infinity_helm)
            return true;
        else
            return false;
    }
    
    public static Item register(Item item, String name) {
    	GameRegistry.registerItem(item, name);
    	return item;
    }
}
