package fox.spiteful.avaritia.entity;

import fox.spiteful.avaritia.Lumberjack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityEndestPearl extends EntityThrowable {

	public EntityEndestPearl(World world, double x,	double y, double z) {
		super(world, x, y, z);
	}

	public EntityEndestPearl(World world, EntityLivingBase ent) {
		super(world, ent);
	}

	public EntityEndestPearl(World world) {
		super(world);
	}

	@Override
	protected void onImpact(MovingObjectPosition pos) {
		if (pos.entityHit != null)
        {
            pos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
        }

		for (int i = 0; i < 32; ++i)
        {
            this.worldObj.spawnParticle("portal", this.posX, this.posY + this.rand.nextDouble() * 2.0D, this.posZ, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian());
        }

        if (!this.worldObj.isRemote)
        {
        	//this.worldObj.createExplosion(this, pos.hitVec.xCoord, pos.hitVec.yCoord, pos.hitVec.zCoord, 4.0f, true);

        	Entity ent = new EntityGapingVoid(this.worldObj);
        	//Entity ent = new EntityChicken(this.worldObj);
            ent.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            this.worldObj.spawnEntityInWorld(ent);
            
            this.setDead();
            
            Lumberjack.info("ded");
        }
	}

}
