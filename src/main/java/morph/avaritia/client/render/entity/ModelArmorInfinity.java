package morph.avaritia.client.render.entity;

import codechicken.lib.math.MathHelper;
import codechicken.lib.texture.TextureUtils;
import morph.avaritia.Avaritia;
import morph.avaritia.client.render.shader.CosmicShaderHelper;
import morph.avaritia.client.ColourHelper;
import morph.avaritia.init.AvaritiaTextures;
import morph.avaritia.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Random;

@SideOnly (Side.CLIENT)
public class ModelArmorInfinity extends ModelBiped {

    public static final ModelArmorInfinity armorModel = new ModelArmorInfinity(1.0f);
    public static final ModelArmorInfinity legModel = new ModelArmorInfinity(0.5f).setLegs(true);

    public static ResourceLocation eyeTex = new ResourceLocation("avaritia", "textures/models/infinity_armor_eyes.png");
    public static ResourceLocation wingTex = new ResourceLocation("avaritia", "textures/models/infinity_armor_wing.png");
    public static ResourceLocation wingGlowTex = new ResourceLocation("avaritia", "textures/models/infinity_armor_wingglow.png");
    public static int itempagewidth = 0;
    public static int itempageheight = 0;
    public boolean legs = false;

    public EntityEquipmentSlot currentSlot = EntityEquipmentSlot.HEAD;

    private Random randy = new Random();

    private Overlay overlay;
    private Overlay invulnOverlay;
    private boolean invulnRender = true;
    private boolean showHat;
    private boolean showChest;
    private boolean showLeg;
    private boolean showFoot;

    private float expand;

    public ModelRenderer bipedLeftWing;
    public ModelRenderer bipedRightWing;

    public ModelArmorInfinity(float expand) {
        super(expand, 0, 64, 64);
        this.expand = expand;
        this.overlay = new Overlay(this, expand);
        this.invulnOverlay = new Overlay(this, 0);

        this.bipedHeadwear = new ModelRenderer(this, 32, 0);
        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, expand * 0.5F);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + 0, 0.0F);
    }

    public ModelArmorInfinity setLegs(boolean islegs) {
        this.legs = islegs;

        int heightoffset = 0;
        int legoffset = islegs ? 32 : 0;

        this.bipedBody = new ModelRenderer(this, 16, 16 + legoffset);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, this.expand);
        this.bipedBody.setRotationPoint(0.0F, 0.0F + heightoffset, 0.0F);
        this.bipedRightLeg = new ModelRenderer(this, 0, 16 + legoffset);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, this.expand);
        this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + heightoffset, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 0, 16 + legoffset);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, this.expand);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + heightoffset, 0.0F);

        return this;
    }

    @SuppressWarnings ("rawtypes")
    public void rebuildWings() {

        // remove the old items from the list so that the new ones don't just stack up
        if (this.bipedBody.childModels == null) {
            this.bipedBody.childModels = new ArrayList();
        }
        if (this.bipedLeftWing != null) {
            this.bipedBody.childModels.remove(this.bipedLeftWing);
        }
        if (this.bipedRightWing != null) {
            this.bipedBody.childModels.remove(this.bipedRightWing);
        }

        // define new
        this.bipedLeftWing = new ModelRendererWing(this, 0, 0);
        this.bipedLeftWing.mirror = true;
        this.bipedLeftWing.addBox(0f, -11.6f, 0f, 0, 32, 32);
        this.bipedLeftWing.setRotationPoint(-1.5f, 0.0f, 2.0f);
        this.bipedLeftWing.rotateAngleY = (float) (Math.PI * 0.4);
        this.bipedBody.addChild(this.bipedLeftWing);

        this.bipedRightWing = new ModelRendererWing(this, 0, 0);
        this.bipedRightWing.addBox(0f, -11.6f, 0f, 0, 32, 32);
        this.bipedRightWing.setRotationPoint(1.5f, 0.0f, 2.0f);
        this.bipedRightWing.rotateAngleY = (float) (-Math.PI * 0.4);
        this.bipedBody.addChild(this.bipedRightWing);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        Minecraft mc = Minecraft.getMinecraft();
        boolean isFlying = entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isFlying && entity.isAirBorne;

        //copyBipedAngles(this, this.overlay);
        //copyBipedAngles(this, this.invulnOverlay);

        super.render(entity, f, f1, f2, f3, f4, f5);

        GlStateManager.color(1, 1, 1, 1);
        CosmicShaderHelper.useShader();
        TextureUtils.bindBlockTexture();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        if (this.invulnRender) {
            GlStateManager.color(1, 1, 1, 0.2F);
            this.invulnOverlay.render(entity, f, f1, f2, f3, f4, f5);
        }
        this.overlay.render(entity, f, f1, f2, f3, f4, f5);

        CosmicShaderHelper.releaseShader();

        mc.renderEngine.bindTexture(eyeTex);
        GlStateManager.disableLighting();
        mc.entityRenderer.disableLightmap();

        long time = mc.thePlayer.worldObj.getWorldTime();

        this.setGems();

        double pulse = Math.sin(time / 10.0) * 0.5 + 0.5;
        double pulse_mag_sqr = pulse * pulse * pulse * pulse * pulse * pulse;
        GlStateManager.color(0.84F, 1F, 0.95F, (float) (pulse_mag_sqr * 0.5F));

        GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
        super.render(entity, f, f1, f2, f3, f4, f5);
        GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();

        if (this.invulnRender) {
            long frame = time / 3;
            randy.setSeed(frame * 1723609L);
            float o = randy.nextFloat() * 6.0f;
            float[] col = ColourHelper.HSVtoRGB(o, 1.0f, 1.0f);

            GlStateManager.color(col[0], col[1], col[2], 1);
            this.setEyes();
            super.render(entity, f, f1, f2, f3, f4, f5);
        }

        if (!CosmicShaderHelper.inventoryRender) {
            mc.entityRenderer.enableLightmap();
        }
        GlStateManager.enableLighting();
        GlStateManager.color(1, 1, 1, 1);

        // WINGS
        if (isFlying && !CosmicShaderHelper.inventoryRender) {
            this.setWings();
            mc.renderEngine.bindTexture(wingTex);
            super.render(entity, f, f1, f2, f3, f4, f5);

            CosmicShaderHelper.useShader();
            TextureUtils.bindBlockTexture();
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.depthMask(false);
            this.overlay.render(entity, f, f1, f2, f3, f4, f5);

            CosmicShaderHelper.releaseShader();

            mc.renderEngine.bindTexture(wingGlowTex);
            GlStateManager.disableLighting();
            mc.entityRenderer.disableLightmap();

            GlStateManager.color(0.84F, 1F, 0.95F, (float) (pulse_mag_sqr * 0.5));

            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
            super.render(entity, f, f1, f2, f3, f4, f5);
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

            GlStateManager.depthMask(true);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            if (!CosmicShaderHelper.inventoryRender) {
                mc.entityRenderer.enableLightmap();
            }
            GlStateManager.enableLighting();
            GlStateManager.color(1, 1, 1, 1);

        }

    }

    public void update(EntityLivingBase entityLiving, ItemStack itemstack, EntityEquipmentSlot armorSlot) {
        this.currentSlot = armorSlot;

        this.invulnRender = false;

        ItemStack hat = entityLiving.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        ItemStack chest = entityLiving.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        ItemStack leg = entityLiving.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        ItemStack foot = entityLiving.getItemStackFromSlot(EntityEquipmentSlot.FEET);

        boolean hasHat = hat != null && hat.getItem() == ModItems.infinity_helmet; //&& !((ItemArmorInfinity) (ModItems.infinity_helmet)).hasPhantomInk(hat);
        boolean hasChest = chest != null && chest.getItem() == ModItems.infinity_chestplate; // && !((ItemArmorInfinity) (ModItems.infinity_chestplate)).hasPhantomInk(chest);
        boolean hasLeg = leg != null && leg.getItem() == ModItems.infinity_pants; // && !((ItemArmorInfinity) (ModItems.infinity_pants)).hasPhantomInk(leg);
        boolean hasFoot = foot != null && foot.getItem() == ModItems.infinity_boots; // && !((ItemArmorInfinity) (ModItems.infinity_boots)).hasPhantomInk(foot);

        if (armorSlot == EntityEquipmentSlot.HEAD) {//TODO, Wot.
            if (hasHat && hasChest && hasLeg && hasFoot) {
                this.invulnRender = true;
            }
        }

        this.showHat = hasHat && armorSlot == EntityEquipmentSlot.HEAD;
        this.showChest = hasChest && armorSlot == EntityEquipmentSlot.CHEST;
        this.showLeg = hasLeg && armorSlot == EntityEquipmentSlot.LEGS;
        this.showFoot = hasFoot && armorSlot == EntityEquipmentSlot.FEET;

        this.bipedHead.showModel = showHat;
        this.bipedHeadwear.showModel = showHat;
        this.bipedBody.showModel = showChest || showLeg;
        this.bipedRightArm.showModel = showChest;
        this.bipedLeftArm.showModel = showChest;
        this.bipedRightLeg.showModel = showLeg || showFoot;
        this.bipedLeftLeg.showModel = showLeg || showFoot;

        this.overlay.bipedHead.showModel = showHat;
        this.overlay.bipedHeadwear.showModel = showHat;
        this.overlay.bipedBody.showModel = showChest || showLeg;
        this.overlay.bipedRightArm.showModel = showChest;
        this.overlay.bipedLeftArm.showModel = showChest;
        this.overlay.bipedRightLeg.showModel = showLeg || showFoot;
        this.overlay.bipedLeftLeg.showModel = showLeg || showFoot;

        this.bipedLeftWing.showModel = false;
        this.bipedRightWing.showModel = false;
        this.overlay.bipedLeftWing.showModel = false;
        this.overlay.bipedRightWing.showModel = false;

        this.isSneak = entityLiving.isSneaking();
        this.isRiding = entityLiving.isRiding();
        this.isChild = entityLiving.isChild();

        this.overlay.isSneak = entityLiving.isSneaking();
        this.overlay.isRiding = entityLiving.isRiding();
        this.overlay.isChild = entityLiving.isChild();

        this.invulnOverlay.isSneak = entityLiving.isSneaking();
        this.invulnOverlay.isRiding = entityLiving.isRiding();
        this.invulnOverlay.isChild = entityLiving.isChild();

        this.overlay.swingProgress = this.swingProgress;
        this.invulnOverlay.swingProgress = this.swingProgress;

        this.leftArmPose = ArmPose.EMPTY;
        this.rightArmPose = ArmPose.EMPTY;

        this.overlay.leftArmPose = ArmPose.EMPTY;
        this.overlay.rightArmPose = ArmPose.EMPTY;

        this.invulnOverlay.leftArmPose = ArmPose.EMPTY;
        this.invulnOverlay.rightArmPose = ArmPose.EMPTY;

        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;

            ItemStack main_hand = player.getHeldItem(EnumHand.MAIN_HAND);

            if (main_hand != null) {
                this.rightArmPose = ArmPose.ITEM;
                this.overlay.rightArmPose = ArmPose.ITEM;
                this.invulnOverlay.rightArmPose = ArmPose.ITEM;

                if (player.getItemInUseCount() > 0) {

                    EnumAction enumaction = main_hand.getItemUseAction();

                    if (enumaction == EnumAction.BOW) {
                        this.rightArmPose = ArmPose.BOW_AND_ARROW;
                        this.overlay.rightArmPose = ArmPose.BOW_AND_ARROW;
                        this.invulnOverlay.rightArmPose = ArmPose.BOW_AND_ARROW;
                    } else if (enumaction == EnumAction.BLOCK) {
                        this.rightArmPose = ArmPose.BLOCK;
                        this.overlay.rightArmPose = ArmPose.BLOCK;
                        this.invulnOverlay.rightArmPose = ArmPose.BLOCK;
                    }

                }

            }

            ItemStack off_hand = player.getHeldItem(EnumHand.OFF_HAND);
            if (off_hand != null) {
                this.leftArmPose = ArmPose.ITEM;
                this.overlay.leftArmPose = ArmPose.ITEM;
                this.invulnOverlay.leftArmPose = ArmPose.ITEM;

                if (player.getItemInUseCount() > 0) {

                    EnumAction enumaction = off_hand.getItemUseAction();

                    if (enumaction == EnumAction.BOW) {
                        this.leftArmPose = ArmPose.BOW_AND_ARROW;
                        this.overlay.leftArmPose = ArmPose.BOW_AND_ARROW;
                        this.invulnOverlay.leftArmPose = ArmPose.BOW_AND_ARROW;
                    } else if (enumaction == EnumAction.BLOCK) {
                        this.leftArmPose = ArmPose.BLOCK;
                        this.overlay.leftArmPose = ArmPose.BLOCK;
                        this.invulnOverlay.leftArmPose = ArmPose.BLOCK;
                    }

                }

            }
        }

    }

    @Override
    public void setRotationAngles(float f1, float speed, float ticks, float headYaw, float headPitch, float f6, Entity entity) {
        super.setRotationAngles(f1, speed, ticks, headYaw, headPitch, f6, entity);
        this.overlay.setRotationAngles(f1, speed, ticks, headYaw, headPitch, f6, entity);
        this.invulnOverlay.setRotationAngles(f1, speed, ticks, headYaw, headPitch, f6, entity);
        RenderManager manager = Minecraft.getMinecraft().getRenderManager();
        if (manager.entityRenderMap.containsKey(entity.getClass())) {
            Render r = manager.entityRenderMap.get(entity.getClass());

            if (r instanceof RenderBiped) {
                ModelBiped m = ((RenderBiped) r).modelBipedMain;

                copyBipedAngles(m, this);
            }
        }
    }

    public void setEyes() {
        this.bipedHead.showModel = false;
        this.bipedBody.showModel = false;
        this.bipedRightArm.showModel = false;
        this.bipedLeftArm.showModel = false;
        this.bipedRightLeg.showModel = false;
        this.bipedLeftLeg.showModel = false;
        this.bipedHeadwear.showModel = this.showHat ? true : false;
    }

    public void setGems() {
        this.bipedHead.showModel = false;
        this.bipedHeadwear.showModel = false;
        this.bipedBody.showModel = this.legs ? false : (this.showChest ? true : false);
        this.bipedRightArm.showModel = this.legs ? false : (this.showChest ? true : false);
        this.bipedLeftArm.showModel = this.legs ? false : (this.showChest ? true : false);
        this.bipedRightLeg.showModel = this.legs ? (this.showLeg ? true : false) : false;
        this.bipedLeftLeg.showModel = this.legs ? (this.showLeg ? true : false) : false;
    }

    public void setWings() {
        this.bipedBody.showModel = this.legs ? false : (this.showChest ? true : false);
        this.bipedLeftWing.showModel = true;
        this.bipedRightWing.showModel = true;
        this.bipedHeadwear.showModel = false;
        this.bipedRightArm.showModel = false;
        this.bipedLeftArm.showModel = false;
        this.bipedRightLeg.showModel = false;
        this.bipedLeftLeg.showModel = false;
        this.bipedHeadwear.showModel = false;
        this.bipedHead.showModel = false;

        this.overlay.bipedBody.showModel = this.legs ? false : (this.showChest ? true : false);
        this.overlay.bipedLeftWing.showModel = true;
        this.overlay.bipedRightWing.showModel = true;
        this.overlay.bipedHead.showModel = false;
        this.overlay.bipedHeadwear.showModel = false;
    }

    public void rebuildOverlay() {
        this.rebuildWings();
        this.overlay.rebuild(AvaritiaTextures.INFINITY_ARMOR_MASK, AvaritiaTextures.INFINITY_ARMOR_MASK_WINGS);
        this.invulnOverlay.rebuild(AvaritiaTextures.INFINITY_ARMOR_MASK_INV, null);
    }

    public static void copyPartAngles(ModelRenderer source, ModelRenderer dest) {
        dest.rotateAngleX = source.rotateAngleX;
        dest.rotateAngleY = source.rotateAngleY;
        dest.rotateAngleZ = source.rotateAngleZ;
    }

    public static void copyBipedAngles(ModelBiped source, ModelBiped dest) {
        copyPartAngles(source.bipedBody, dest.bipedBody);
        copyPartAngles(source.bipedHead, dest.bipedHead);
        copyPartAngles(source.bipedHeadwear, dest.bipedHeadwear);
        copyPartAngles(source.bipedLeftArm, dest.bipedLeftArm);
        copyPartAngles(source.bipedLeftLeg, dest.bipedLeftLeg);
        copyPartAngles(source.bipedRightArm, dest.bipedRightArm);
        copyPartAngles(source.bipedRightLeg, dest.bipedRightLeg);
    }

    public class Overlay extends ModelBiped {

        public ModelArmorInfinity parent;
        public float expand;

        public ModelRenderer bipedLeftWing;
        public ModelRenderer bipedRightWing;

        public Overlay(ModelArmorInfinity parent, float expand) {
            this.parent = parent;
            this.expand = expand;
        }

        @SuppressWarnings ("rawtypes")
        public void rebuild(TextureAtlasSprite icon, TextureAtlasSprite wingicon) {
            int ox = MathHelper.floor(icon.getMinU() * itempagewidth);
            int oy = MathHelper.floor(icon.getMinV() * itempageheight);

            float heightoffset = 0.0f;
            int legoffset = this.parent.legs ? 32 : 0;

            this.textureWidth = itempagewidth;
            this.textureHeight = itempageheight;
            this.bipedHead = new ModelRenderer(this, 0 + ox, 0 + legoffset + oy);
            this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, this.expand);
            this.bipedHead.setRotationPoint(0.0F, 0.0F + heightoffset, 0.0F);
            this.bipedHeadwear = new ModelRenderer(this, 32 + ox, 0 + legoffset + oy);
            this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, this.expand * 0.5F);
            this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + heightoffset, 0.0F);
            this.bipedBody = new ModelRenderer(this, 16 + ox, 16 + legoffset + oy);
            this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, this.expand);
            this.bipedBody.setRotationPoint(0.0F, 0.0F + heightoffset, 0.0F);
            this.bipedRightArm = new ModelRenderer(this, 40 + ox, 16 + legoffset + oy);
            this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, this.expand);
            this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + heightoffset, 0.0F);
            this.bipedLeftArm = new ModelRenderer(this, 40 + ox, 16 + legoffset + oy);
            this.bipedLeftArm.mirror = true;
            this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, this.expand);
            this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + heightoffset, 0.0F);
            this.bipedRightLeg = new ModelRenderer(this, 0 + ox, 16 + legoffset + oy);
            this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, this.expand);
            this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + heightoffset, 0.0F);
            this.bipedLeftLeg = new ModelRenderer(this, 0 + ox, 16 + legoffset + oy);
            this.bipedLeftLeg.mirror = true;
            this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, this.expand);
            this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + heightoffset, 0.0F);

            // rebuild wings!
            if (wingicon != null) {
                int oxw = MathHelper.floor(wingicon.getMinU() * itempagewidth);
                int oyw = MathHelper.floor(wingicon.getMinV() * itempageheight);

                if (this.bipedBody.childModels == null) {
                    this.bipedBody.childModels = new ArrayList();
                }
                if (this.bipedLeftWing != null) {
                    this.bipedBody.childModels.remove(this.bipedLeftWing);
                }
                if (this.bipedRightWing != null) {
                    this.bipedBody.childModels.remove(this.bipedRightWing);
                }

                this.bipedLeftWing = new ModelRendererWing(this, oxw, oyw);
                this.bipedLeftWing.mirror = true;
                this.bipedLeftWing.addBox(0f, -11.6f, 0f, 0, 32, 32);
                this.bipedLeftWing.setRotationPoint(-1.5f, 0.0f, 2.0f);
                this.bipedLeftWing.rotateAngleY = (float) (Math.PI * 0.4);
                this.bipedBody.addChild(this.bipedLeftWing);

                this.bipedRightWing = new ModelRendererWing(this, oxw, oyw);
                this.bipedRightWing.addBox(0f, -11.6f, 0f, 0, 32, 32);
                this.bipedRightWing.setRotationPoint(1.5f, 0.0f, 2.0f);
                this.bipedRightWing.rotateAngleY = (float) (-Math.PI * 0.4);
                this.bipedBody.addChild(this.bipedRightWing);
            }
        }

        @Override
        public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
            copyBipedAngles(this.parent, this);

            super.render(entity, f, f1, f2, f3, f4, f5);
        }

        @Override
        public void setRotationAngles(float f1, float f2, float f3, float f4, float f5, float f6, Entity entity) {
            super.setRotationAngles(f1, f2, f3, f4, f5, f6, entity);
            RenderManager manager = Minecraft.getMinecraft().getRenderManager();
            if (manager.entityRenderMap.containsKey(entity.getClass())) {
                Render r = manager.entityRenderMap.get(entity.getClass());

                if (r instanceof RenderBiped) {
                    ModelBiped m = ((RenderBiped) r).modelBipedMain;

                    copyBipedAngles(m, this);
                }
            }
        }
    }
}
