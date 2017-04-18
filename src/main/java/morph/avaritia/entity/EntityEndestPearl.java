package morph.avaritia.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityEndestPearl extends EntityThrowable {

    public EntityEndestPearl(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public EntityEndestPearl(World world, EntityLivingBase ent) {
        super(world, ent);
    }

    public EntityEndestPearl(World world) {
        super(world);
    }

    @Override
    protected void onImpact(RayTraceResult pos) {
        if (pos.entityHit != null) {
            pos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
        }

        for (int i = 0; i < 100; ++i) {
            this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY, this.posZ, this.rand.nextGaussian() * 3, this.rand.nextGaussian() * 3, this.rand.nextGaussian() * 3);
        }

        if (!this.worldObj.isRemote) {
            //this.worldObj.createExplosion(this, pos.hitVec.xCoord, pos.hitVec.yCoord, pos.hitVec.zCoord, 4.0f, true);

            Entity ent = new EntityGapingVoid(this.worldObj);
            EnumFacing dir = pos.sideHit;
            ent.setLocationAndAngles(this.posX + dir.getFrontOffsetX() * 0.25, this.posY + dir.getFrontOffsetY() * 0.25, this.posZ + dir.getFrontOffsetZ() * 0.25, this.rotationYaw, 0.0F);
            this.worldObj.spawnEntityInWorld(ent);

            this.setDead();
        }
    }

}
