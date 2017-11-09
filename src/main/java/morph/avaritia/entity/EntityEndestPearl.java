package morph.avaritia.entity;

import codechicken.lib.vec.Vector3;
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
            pos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), 0.0F);
        }

        for (int i = 0; i < 100; ++i) {
            world.spawnParticle(EnumParticleTypes.PORTAL, posX, posY, posZ, rand.nextGaussian() * 3, rand.nextGaussian() * 3, rand.nextGaussian() * 3);
        }

        if (!world.isRemote) {
            //this.worldObj.createExplosion(this, pos.hitVec.xCoord, pos.hitVec.yCoord, pos.hitVec.zCoord, 4.0f, true);

            Entity ent = new EntityGapingVoid(world);
            EnumFacing dir = pos.sideHit;
            Vector3 offset = Vector3.zero.copy();
            if (pos.sideHit != null) {
                offset = new Vector3(dir.getFrontOffsetX(), dir.getFrontOffsetY(), dir.getFrontOffsetZ());
            }
            ent.setLocationAndAngles(posX + offset.x * 0.25, posY + offset.y * 0.25, posZ + offset.z * 0.25, rotationYaw, 0.0F);
            world.spawnEntity(ent);

            setDead();
        }
    }

}
