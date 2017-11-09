package morph.avaritia.item.tools;

import codechicken.lib.model.ModelRegistryHelper;
import codechicken.lib.model.bakery.IBakeryProvider;
import codechicken.lib.model.bakery.ModelBakery;
import codechicken.lib.model.bakery.generation.IBakery;
import codechicken.lib.util.TransformUtils;
import morph.avaritia.Avaritia;
import morph.avaritia.api.ICosmicRenderItem;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.client.render.item.CosmicItemRender;
import morph.avaritia.client.render.item.InfinityBowModelBakery;
import morph.avaritia.client.render.item.InfinityBowModelWrapper;
import morph.avaritia.entity.EntityHeavenArrow;
import morph.avaritia.init.AvaritiaTextures;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityArrow.PickupStatus;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBowInfinity extends Item implements ICosmicRenderItem, IModelRegister, IBakeryProvider {

    //private IIcon[] iconArray;
    //private IIcon[] maskArray;
    //private IIcon idleMask;

    public ItemBowInfinity() {
        maxStackSize = 1;
        setMaxDamage(9999);
        setCreativeTab(Avaritia.tab);
        setUnlocalizedName("avaritia:infinity_bow");
        setRegistryName("infinity_bow");
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, 0);
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        if (count == 1) {
            fire(stack, player.world, player, 0);
        }
    }

    public void fire(ItemStack stack, World world, EntityLivingBase player, int useCount) {
        int max = getMaxItemUseDuration(stack);
        float maxf = (float) max;
        int j = max - useCount;

        float f = j / maxf;
        f = (f * f + f * 2.0F) / 3.0F;

        if (f < 0.1) {
            return;
        }

        if (f > 1.0) {
            f = 1.0F;
        }

        EntityArrow arrow = new EntityHeavenArrow(world, player);
        arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0, f * 3.0F, 1.0F);//TODO, no inaccuracy?
        arrow.setDamage(20.0);

        if (f == 1.0F) {
            arrow.setIsCritical(true);
        }

        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

        if (k > 0) {
            arrow.setDamage(arrow.getDamage() + k + 1);
        }

        int l = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

        if (l > 0) {
            arrow.setKnockbackStrength(l);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
            arrow.setFire(100);
        }

        stack.damageItem(1, player);
        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

        arrow.pickupStatus = PickupStatus.CREATIVE_ONLY;

        if (!world.isRemote) {
            world.spawnEntity(arrow);
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 13;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

        ItemStack stack = player.getHeldItem(hand);
        ActionResult<ItemStack> event = ForgeEventFactory.onArrowNock(stack, world, player, hand, true);
        if (event != null) {
            return event;
        }

        player.setActiveHand(hand);

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public int getItemEnchantability() {
        return 1;
    }

    //@Override
    //@SideOnly (Side.CLIENT)
    //public void registerIcons(IIconRegister ir) {
    //    int pullframes = 3;
    //    this.itemIcon = ir.registerIcon(this.getIconString() + "_standby");
    //    this.idleMask = ir.registerIcon(this.getIconString() + "_standby_mask");
    //    this.iconArray = new IIcon[pullframes];
    //    this.maskArray = new IIcon[pullframes];
    //    for (int i = 0; i < pullframes; ++i) {
    //        this.iconArray[i] = ir.registerIcon(this.getIconString() + "_pulling_" + i);
    //        this.maskArray[i] = ir.registerIcon(this.getIconString() + "_pulling_mask_" + i);
    //    }
    //}

    //@Override
    //@SideOnly (Side.CLIENT)
    //public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
    //    if (usingItem != null) {
    //        int max = stack.getMaxItemUseDuration();
    //        int pull = max - useRemaining;
    //        if (pull >= (max * 2) / 3.0) {
    //            return this.iconArray[2];
    //        }
    //        if (pull > max / 3.0) {
    //            return this.iconArray[1];
    //        }
    //        if (pull > 0) {
    //            return this.iconArray[0];
    //        }
    //    }
    //    return getIcon(stack, renderPass);
    //}

    //@Override
    //@SideOnly (Side.CLIENT)
    //public IIcon getIcon(ItemStack stack, int pass) {
    //    return super.getIcon(stack, pass);
    //}

    @Override
    @SideOnly (Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

    @Override
    @SideOnly (Side.CLIENT)
    public TextureAtlasSprite getMaskTexture(ItemStack stack, EntityLivingBase player) {
        int frame = -1;
        if (player != null) {
            int bframe = InfinityBowModelWrapper.getBowFrame(player);
            frame = bframe != 0 ? bframe : -1;
        }
        //Lumberjack.info(frame);
        if (frame == -1) {
            return AvaritiaTextures.INFINITY_BOW_IDLE_MASK;
        }
        return AvaritiaTextures.INFINITY_BOW_PULL_MASK[frame];
    }

    @Override
    @SideOnly (Side.CLIENT)
    public float getMaskOpacity(ItemStack stack, EntityLivingBase player) {
        return 1.0f;
    }

    @Override
    @SideOnly (Side.CLIENT)
    public void registerModels() {
        ModelBakery.registerItemKeyGenerator(this, stack -> {
            String key = ModelBakery.defaultItemKeyGenerator.generateKey(stack);
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("frame")) {
                key += "@pull=" + stack.getTagCompound().getInteger("frame");
            }
            return key;
        });
        ModelResourceLocation location = new ModelResourceLocation("avaritia:bow", "bow");
        IBakedModel actualModel = new InfinityBowModelWrapper();
        IBakedModel wrapped = new CosmicItemRender(TransformUtils.DEFAULT_BOW, modelRegistry -> actualModel);
        ModelRegistryHelper.register(location, wrapped);
        ModelLoader.setCustomMeshDefinition(this, stack -> location);

    }

    @Override
    public IBakery getBakery() {
        return InfinityBowModelBakery.INSTANCE;
    }
}
