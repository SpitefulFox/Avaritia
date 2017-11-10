package fox.spiteful.avaritia.entity;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import fox.spiteful.avaritia.Lumberjack;
import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityGapingVoid extends Entity {
	
	private static Random randy = new Random();
	public static final int maxLifetime = 186;
	public static double collapse = .95;
	public static double suckrange = 20.0;
	
	public static final IEntitySelector sucklector = new IEntitySelector() {

		@Override
		public boolean isEntityApplicable(Entity ent) {
			if (ent instanceof EntityPlayer) {
				EntityPlayer p = (EntityPlayer)ent;
				if (p.capabilities.isCreativeMode && p.capabilities.isFlying) {
					return false;
				}
			}
			
			return true;
		}
		
	};
	
	public static final IEntitySelector nomlector = new IEntitySelector() {

		@Override
		public boolean isEntityApplicable(Entity ent) {
			if(!(ent instanceof EntityLivingBase)) { return false; }
			
			if (ent instanceof EntityPlayer) {
				EntityPlayer p = (EntityPlayer)ent;
				if (p.capabilities.isCreativeMode) {
					return false;
				}
			} else if (ent instanceof EntityImmortalItem) {
				return false;
			}
			
			return true;
		}
		
	};
	
	public EntityGapingVoid(World world) {
		super(world);
		this.isImmuneToFire = true;
		this.setSize(0.1F, 0.1F);
		this.ignoreFrustumCheck = true;
		this.renderDistanceWeight = 100.0;
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObject(12, 0);
		dataWatcher.setObjectWatched(12);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		// tick, tock
		int age = this.getAge();
		
		if (age >= maxLifetime) {
			this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 6.0f, true);
			this.setDead();
		} else {
			if (age == 0) {
				this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "Avaritia:gapingVoid", 8.0F, 1.0F);
			}
			this.setAge(age+1);
		}
		
		// poot poot
		double particlespeed = 4.5;
		
		double size = getVoidScale(age)*0.5 - 0.2;
		for(int i=0; i<50; i++) {
			Vec3 pootdir = Vec3.createVectorHelper(0, 0, size);
			pootdir.rotateAroundY(randy.nextFloat()*180f);
			pootdir.rotateAroundX(randy.nextFloat()*360f);
			
			Vec3 pootspeed = pootdir.normalize();
			pootspeed.xCoord *= particlespeed;
			pootspeed.yCoord *= particlespeed;
			pootspeed.zCoord *= particlespeed;
			
			this.worldObj.spawnParticle("portal", this.posX + pootdir.xCoord, this.posY + pootdir.yCoord, this.posZ + pootdir.zCoord, pootspeed.xCoord, pootspeed.yCoord, pootspeed.zCoord);
		}
		
		// *slurping noises*
		AxisAlignedBB suckzone = AxisAlignedBB.getBoundingBox(this.posX - suckrange, this.posY - suckrange, this.posZ - suckrange, this.posX + suckrange, this.posY + suckrange, this.posZ + suckrange);
		List<Entity> sucked = this.worldObj.selectEntitiesWithinAABB(Entity.class, suckzone, sucklector);
		
		double radius = getVoidScale(age)*0.5;
		
		for (Entity suckee : sucked) {
			if (suckee != this) {
				double dx = this.posX - suckee.posX;
				double dy = this.posY - suckee.posY;
				double dz = this.posZ - suckee.posZ;
				
				double lensquared = dx*dx + dy*dy + dz*dz;
				double len = Math.sqrt(lensquared);
				double lenn = len/suckrange;
				
				if (len <= suckrange) {
					double strength = (1-lenn)*(1-lenn);
					double power = 0.075 * radius;
					
					suckee.motionX += (dx/len)*strength*power;
					suckee.motionY += (dy/len)*strength*power;
					suckee.motionZ += (dz/len)*strength*power;
				}
			}
		}
		
		// om nom nom
		double nomrange = radius*0.95;
		AxisAlignedBB nomzone = AxisAlignedBB.getBoundingBox(this.posX - nomrange, this.posY - nomrange, this.posZ - nomrange, this.posX + nomrange, this.posY + nomrange, this.posZ + nomrange);
		List<Entity> nommed = this.worldObj.selectEntitiesWithinAABB(EntityLivingBase.class, nomzone, nomlector);
		
		for (Entity nommee : nommed) {
			if (nommee != this) {
				double dx = this.posX - nommee.posX;
				double dy = this.posY - nommee.posY;
				double dz = this.posZ - nommee.posZ;
				
				double lensquared = dx*dx + dy*dy + dz*dz;
				double len = Math.sqrt(lensquared);
				
				if (len <= nomrange) {
					nommee.attackEntityFrom(DamageSource.outOfWorld, 3.0f);
				}
			}
		}
		
		// every half second, SMASH STUFF
		if (age % 10 == 0) {
			int bx = (int) Math.floor(this.posX);
			int by = (int) Math.floor(this.posY);
			int bz = (int) Math.floor(this.posZ);
			
			int blockrange = (int) Math.round(nomrange);
			int lx,ly,lz;
			
			for (int y = -blockrange; y <= blockrange; y++) {
				for (int z = -blockrange; z <= blockrange; z++) {
					for (int x = -blockrange; x <= blockrange; x++) {
						lx = bx+x;
						ly = by+y;
						lz = bz+z;
						
						if (ly < 0 || ly > 255) {
							continue;
						}
						
						double dist = Math.sqrt(x*x+y*y+z*z);
						if (dist <= nomrange && !this.worldObj.isAirBlock(lx, ly, lz)) {
							Block b = this.worldObj.getBlock(lx, ly, lz);
							int meta = this.worldObj.getBlockMetadata(lx, ly, lz);
							float resist = b.getExplosionResistance(this, this.worldObj, lx, ly, lz, this.posX, this.posY, this.posZ);
							if (resist <= 10.0) {
								b.dropBlockAsItemWithChance(worldObj, lx, ly, lz, meta, 0.9f, 0);
								this.worldObj.setBlockToAir(lx, ly, lz);
							}
						}
					}
				}
			}
		}
	}

	
	private void setAge(int age) {
		dataWatcher.updateObject(12, age);
	}
	
	public int getAge() {
		return dataWatcher.getWatchableObjectInt(12);
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound tag) {
		this.setAge(tag.getInteger("age"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tag) {
		tag.setInteger("age", this.getAge());
	}
	
	public static double getVoidScale(double age) {
		double life = age / (double)maxLifetime;
		
		double curve;
		if (life < collapse) {
			curve = 0.005 + ease(1-((collapse-life)/collapse))* 0.995;
		} else {
			curve = ease(1-((life-collapse)/(1-collapse)));
		}
		return 10.0*curve;
	}
	
	private static double ease(double in) {
		double t = in-1;
		return Math.sqrt(1 - t*t);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

	@Override
    public boolean canBeCollidedWith()
    {
        return false;
    }
}
