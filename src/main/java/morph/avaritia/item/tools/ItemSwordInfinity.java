package morph.avaritia.item.tools;

import codechicken.lib.model.ModelRegistryHelper;
import codechicken.lib.util.TransformUtils;
import morph.avaritia.Avaritia;
import morph.avaritia.api.ICosmicRenderItem;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.client.render.item.CosmicItemRender;
import morph.avaritia.entity.EntityImmortalItem;
import morph.avaritia.handler.AvaritiaEventHandler;
import morph.avaritia.init.AvaritiaTextures;
import morph.avaritia.init.ModItems;
import morph.avaritia.util.DamageSourceInfinitySword;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSwordInfinity extends ItemSword implements ICosmicRenderItem, IModelRegister {

    private static final ToolMaterial TOOL_MATERIAL = EnumHelper.addToolMaterial("INFINITY_SWORD", 32, 9999, 9999F, -3.0F, 200);
    //private IIcon cosmicMask;
    //private IIcon pommel;

    public ItemSwordInfinity() {
        super(TOOL_MATERIAL);
        setUnlocalizedName("avaritia:infinity_sword");
        setRegistryName("infinity_sword");
        setCreativeTab(Avaritia.tab);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase victim, EntityLivingBase player) {
        if (player.world.isRemote) {
            return true;
        }
        if (victim instanceof EntityPlayer) {
            EntityPlayer pvp = (EntityPlayer) victim;
            if (AvaritiaEventHandler.isInfinite(pvp)) {
                victim.attackEntityFrom(new DamageSourceInfinitySword(player).setDamageBypassesArmor(), 4.0F);
                return true;
            }
            if (pvp.getHeldItem(EnumHand.MAIN_HAND) != null && pvp.getHeldItem(EnumHand.MAIN_HAND).getItem() == ModItems.infinity_sword && pvp.isHandActive()) {
                return true;
            }
        }

        victim.recentlyHit = 60;
        victim.getCombatTracker().trackDamage(new DamageSourceInfinitySword(player), victim.getHealth(), victim.getHealth());
        victim.setHealth(0);
        victim.onDeath(new EntityDamageSource("infinity", player));
        return true;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        if (!entity.world.isRemote && entity instanceof EntityPlayer) {
            EntityPlayer victim = (EntityPlayer) entity;
            if (victim.capabilities.isCreativeMode && !victim.isDead && victim.getHealth() > 0 && !AvaritiaEventHandler.isInfinite(victim)) {
                victim.getCombatTracker().trackDamage(new DamageSourceInfinitySword(player), victim.getHealth(), victim.getHealth());
                victim.setHealth(0);
                victim.onDeath(new EntityDamageSource("infinity", player));
                //TODO
                //player.addStat(Achievements.creative_kill, 1);
                return true;
            }
        }
        return false;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return ModItems.COSMIC_RARITY;
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, 0);
    }

    @Override
    @SideOnly (Side.CLIENT)
    public TextureAtlasSprite getMaskTexture(ItemStack stack, EntityLivingBase player) {
        return AvaritiaTextures.INFINITY_SWORD_MASK;
    }

    @Override
    @SideOnly (Side.CLIENT)
    public float getMaskOpacity(ItemStack stack, EntityLivingBase player) {
        return 1.0f;
    }

    //@SideOnly (Side.CLIENT)
    //@Override
    //public void registerIcons(IIconRegister ir) {
    //    super.registerIcons(ir);
    //    this.cosmicMask = ir.registerIcon("avaritia:infinity_sword_mask");
    //    this.pommel = ir.registerIcon("avaritia:infinity_sword_pommel");
    //}

    //@Override
    //public IIcon getIcon(ItemStack stack, int pass) {
    //    if (pass == 1) {
    //        return this.pommel;
    //    }
    //    return super.getIcon(stack, pass);
    // }

    //@SideOnly (Side.CLIENT)
    //@Override
    //public boolean requiresMultipleRenderPasses() {
    //    return true;
    //}

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return new EntityImmortalItem(world, location, itemstack);
    }

    @Override
    @SideOnly (Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack) {
        return false;
    }

    @Override
    @SideOnly (Side.CLIENT)
    public void registerModels() {
        ModelResourceLocation sword = new ModelResourceLocation("avaritia:tools", "type=infinity_sword");
        ModelLoader.registerItemVariants(ModItems.infinity_pickaxe, sword);
        IBakedModel wrapped = new CosmicItemRender(TransformUtils.DEFAULT_TOOL, modelRegistry -> modelRegistry.getObject(sword));
        ModelRegistryHelper.register(sword, wrapped);
        ModelLoader.setCustomMeshDefinition(ModItems.infinity_sword, (ItemStack stack) -> sword);
    }
}
