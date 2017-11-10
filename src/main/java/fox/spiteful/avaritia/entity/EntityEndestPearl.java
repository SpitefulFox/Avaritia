package fox.spiteful.avaritia.entity;

import fox.spiteful.avaritia.Lumberjack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

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

		for (int i = 0; i < 100; ++i)
        {
            this.worldObj.spawnParticle("portal", this.posX, this.posY, this.posZ, this.rand.nextGaussian()*3, this.rand.nextGaussian()*3, this.rand.nextGaussian()*3);
        }

        if (!this.worldObj.isRemote)
        {
        	//this.worldObj.createExplosion(this, pos.hitVec.xCoord, pos.hitVec.yCoord, pos.hitVec.zCoord, 4.0f, true);

        	Entity ent = new EntityGapingVoid(this.worldObj);
        	//Entity ent = new EntityChicken(this.worldObj);
        	ForgeDirection dir = ForgeDirection.getOrientation(pos.sideHit);
            ent.setLocationAndAngles(this.posX + dir.offsetX*0.25, this.posY + dir.offsetY*0.25, this.posZ + dir.offsetZ*0.25, this.rotationYaw, 0.0F);
            this.worldObj.spawnEntityInWorld(ent);
            
            this.setDead();
        }
	}

}
