package fox.spiteful.avaritia.render;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import fox.spiteful.avaritia.Lumberjack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class ModelArmorInfinity extends ModelBiped {

	public static IIcon overlayIcon = null;
	public static IIcon invulnOverlayIcon = null;
	public static IIcon eyesIcon = null;
	public static ResourceLocation eyeTex = new ResourceLocation("avaritia","textures/models/infinity_armor_eyes.png");
	public static int itempagewidth = 0;
	public static int itempageheight = 0;
	public boolean legs = false;
	
	private Random randy = new Random();
	
	private Overlay overlay;
	private Overlay invulnOverlay;
	private boolean invulnRender = true;
	
	private float expand;
	
	public ModelArmorInfinity(float expand) {
		super(expand, 0, 64,64);
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
		
		this.bipedBody = new ModelRenderer(this, 16, 16 +legoffset);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, this.expand);
        this.bipedBody.setRotationPoint(0.0F, 0.0F + heightoffset, 0.0F);
        this.bipedRightLeg = new ModelRenderer(this, 0, 16 +legoffset);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, this.expand);
        this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + heightoffset, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 0, 16 +legoffset);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, this.expand);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + heightoffset, 0.0F);
		
		return this;
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		Minecraft mc = Minecraft.getMinecraft();
		
		super.render(entity, f, f1, f2, f3, f4, f5);
		
		CosmicRenderShenanigans.useShader();
		CosmicRenderShenanigans.bindItemTexture();
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthMask(false);
		if(this.invulnRender) {
			GL11.glColor4d(1, 1, 1, 0.2);
			this.invulnOverlay.render(entity, f, f1, f2, f3, f4, f5);
		}
		this.overlay.render(entity, f, f1, f2, f3, f4, f5);
		

		CosmicRenderShenanigans.releaseShader();
		
		mc.renderEngine.bindTexture(eyeTex);
		GL11.glDisable(GL11.GL_LIGHTING);
		mc.entityRenderer.disableLightmap(0.0);
		
		long time = mc.thePlayer.worldObj.getWorldTime();
		
		this.setGems();
		
		double pulse = Math.sin(time / 10.0)*0.5 + 0.5;
		GL11.glColor4d(0.84, 1, 0.95, pulse*pulse*pulse*pulse*pulse*pulse*0.5);
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		super.render(entity, f, f1, f2, f3, f4, f5);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		
		if (this.invulnRender) {
			long frame = time/3;
			randy.setSeed(frame*1723609l);
			float o = randy.nextFloat()*6.0f;
			float[] col = RainbowHelper.HSVtoRGB(o, 1.0f, 1.0f);
			
			GL11.glColor4d(col[0], col[1], col[2], 1);
			this.setEyes();
			super.render(entity, f, f1, f2, f3, f4, f5);
		}
		
		mc.entityRenderer.enableLightmap(0.0);
		GL11.glEnable(GL11.GL_LIGHTING);
	}
	
	public void update(EntityLivingBase entityLiving, ItemStack itemstack, int armorSlot) {
		this.bipedHead.showModel = armorSlot == 0;
        this.bipedHeadwear.showModel = armorSlot == 0;
        this.bipedBody.showModel = armorSlot == 1 || armorSlot == 2;
        this.bipedRightArm.showModel = armorSlot == 1;
        this.bipedLeftArm.showModel = armorSlot == 1;
        this.bipedRightLeg.showModel = armorSlot == 2 || armorSlot == 3;
        this.bipedLeftLeg.showModel = armorSlot == 2 || armorSlot == 3;
        
        this.overlay.bipedHead.showModel = armorSlot == 0;
        this.overlay.bipedHeadwear.showModel = armorSlot == 0;
        this.overlay.bipedBody.showModel = armorSlot == 1 || armorSlot == 2;
        this.overlay.bipedRightArm.showModel = armorSlot == 1;
        this.overlay.bipedLeftArm.showModel = armorSlot == 1;
        this.overlay.bipedRightLeg.showModel = armorSlot == 2 || armorSlot == 3;
        this.overlay.bipedLeftLeg.showModel = armorSlot == 2 || armorSlot == 3;
        
        this.isSneak = entityLiving.isSneaking();
        this.isRiding = entityLiving.isRiding();
        this.isChild = entityLiving.isChild();
        
        this.overlay.isSneak = entityLiving.isSneaking();
        this.overlay.isRiding = entityLiving.isRiding();
        this.overlay.isChild = entityLiving.isChild();
        
        this.invulnOverlay.isSneak = entityLiving.isSneaking();
        this.invulnOverlay.isRiding = entityLiving.isRiding();
        this.invulnOverlay.isChild = entityLiving.isChild();

        this.heldItemRight = 0;
        this.aimedBow = false;
        
        this.overlay.heldItemRight = 0;
        this.overlay.aimedBow = false;
        
        this.invulnOverlay.heldItemRight = 0;
        this.invulnOverlay.aimedBow = false;

        EntityPlayer player = (EntityPlayer)entityLiving;
        
        ItemStack held_item = player.getEquipmentInSlot(0);

        if (held_item != null){
            this.heldItemRight = 1;
            this.overlay.heldItemRight = 1;
            this.invulnOverlay.heldItemRight = 1;

            if (player.getItemInUseCount() > 0){

                EnumAction enumaction = held_item.getItemUseAction();

                if (enumaction == EnumAction.bow){
                    this.aimedBow = true;
                    this.overlay.aimedBow = true;
                    this.invulnOverlay.aimedBow = true;
                }else if (enumaction == EnumAction.block){
                    this.heldItemRight = 3;
                    this.overlay.heldItemRight = 3;
                    this.invulnOverlay.heldItemRight = 3;
                }


            }

        }
        
        this.invulnRender = armorSlot == 0;
	}
	
	public void setEyes() {
		this.bipedHead.showModel = false;
        this.bipedBody.showModel = false;
        this.bipedRightArm.showModel = false;
        this.bipedLeftArm.showModel = false;
        this.bipedRightLeg.showModel = false;
        this.bipedLeftLeg.showModel = false;
        this.bipedHeadwear.showModel = true;
	}
	
	public void setGems() {
		this.bipedHead.showModel = false;
		this.bipedHeadwear.showModel = false;
        this.bipedBody.showModel = this.legs ? false : true;
        this.bipedRightArm.showModel = this.legs ? false : true;
        this.bipedLeftArm.showModel = this.legs ? false : true;
        this.bipedRightLeg.showModel = this.legs ? true : false;
        this.bipedLeftLeg.showModel = this.legs ? true : false;
	}
	
	public void rebuildOverlay() {
		this.overlay.rebuild(overlayIcon);
		this.invulnOverlay.rebuild(invulnOverlayIcon);
	}
	
	public class Overlay extends ModelBiped {
		
		public ModelArmorInfinity parent;
		public float expand;
		
		public Overlay(ModelArmorInfinity parent, float expand) {
			this.parent = parent;
			this.expand = expand;
		}
		
		public void rebuild(IIcon icon) {
			int ox = MathHelper.floor_float(icon.getMinU() * itempagewidth);
			int oy = MathHelper.floor_float(icon.getMinV() * itempageheight);
			
			float heightoffset = 0.0f;
			int legoffset = this.parent.legs ? 32 : 0;
			
			this.textureWidth = itempagewidth;
	        this.textureHeight = itempageheight;
	        this.bipedCloak = new ModelRenderer(this, 0 +ox, 0 +oy);
	        this.bipedCloak.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, this.expand);
	        this.bipedEars = new ModelRenderer(this, 24 +ox, 0 +legoffset +oy);
	        this.bipedEars.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, this.expand);
	        this.bipedHead = new ModelRenderer(this, 0 +ox, 0 +legoffset +oy);
	        this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, this.expand);
	        this.bipedHead.setRotationPoint(0.0F, 0.0F + heightoffset, 0.0F);
	        this.bipedHeadwear = new ModelRenderer(this, 32 +ox, 0 +legoffset +oy);
	        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, this.expand * 0.5F);
	        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + heightoffset, 0.0F);
	        this.bipedBody = new ModelRenderer(this, 16 +ox, 16 +legoffset +oy);
	        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, this.expand);
	        this.bipedBody.setRotationPoint(0.0F, 0.0F + heightoffset, 0.0F);
	        this.bipedRightArm = new ModelRenderer(this, 40 +ox, 16 +legoffset +oy);
	        this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, this.expand);
	        this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + heightoffset, 0.0F);
	        this.bipedLeftArm = new ModelRenderer(this, 40 +ox, 16 +legoffset +oy);
	        this.bipedLeftArm.mirror = true;
	        this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, this.expand);
	        this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + heightoffset, 0.0F);
	        this.bipedRightLeg = new ModelRenderer(this, 0 +ox, 16 +legoffset +oy);
	        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, this.expand);
	        this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + heightoffset, 0.0F);
	        this.bipedLeftLeg = new ModelRenderer(this, 0 +ox, 16 +legoffset +oy);
	        this.bipedLeftLeg.mirror = true;
	        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, this.expand);
	        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + heightoffset, 0.0F);
	    }
		
		@Override
		public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
			super.render(entity, f, f1, f2, f3, f4, f5);
		}
	}
}
