package morph.avaritia.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Random;

public class EntityHeavenArrow extends EntityArrow {

    public boolean impacted = false;

    public EntityHeavenArrow(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public EntityHeavenArrow(World world, EntityLivingBase entity) {
        super(world, entity);

    }

    public EntityHeavenArrow(World world) {
        super(world);
    }

    @Override
    public void onUpdate() {
        rotationPitch = 0;
        rotationYaw = 0;
        super.onUpdate();
        if (!impacted) {
            try {
                if (inGround) {
                    impacted = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (impacted) {
                if (!world.isRemote) {
                    barrage();
                }
            }
        }

        if (inGround && timeInGround >= 100) {
            setDead();
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setBoolean("impacted", impacted);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        impacted = tag.getBoolean("impacted");
    }

    public void barrage() {//TODO, this logic may be borked.
        Random randy = getEntityWorld().rand;
        for (int i = 0; i < 10; i++) {
            double angle = randy.nextDouble() * 2 * Math.PI;
            double dist = randy.nextGaussian() * 0.5;

            double x = Math.sin(angle) * dist + posX;
            double z = Math.cos(angle) * dist + posZ;
            double y = posY + 25.0;

            double dangle = randy.nextDouble() * 2 * Math.PI;
            double ddist = randy.nextDouble() * 0.35;
            double dx = Math.sin(dangle) * ddist;
            double dz = Math.cos(dangle) * ddist;

            EntityArrow arrow = new EntityHeavenSubArrow(world, x, y, z);
            arrow.shootingEntity = shootingEntity;
            arrow.addVelocity(dx, -(randy.nextDouble() * 1.85 + 0.15), dz);
            arrow.setDamage(getDamage());
            arrow.setIsCritical(true);
            arrow.pickupStatus = pickupStatus;

            world.spawnEntity(arrow);
        }
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.ARROW);//TODO This needs to be null but can't be, Because vanulla.
    }
}
