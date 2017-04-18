package morph.avaritia.entity;

import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import com.google.common.base.Predicate;
import morph.avaritia.init.ModSounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class EntityGapingVoid extends Entity {

    public static final DataParameter<Integer> AGE_PARAMETER = EntityDataManager.createKey(EntityGapingVoid.class, DataSerializers.VARINT);

    private static Random randy = new Random();
    public static final int maxLifetime = 186;
    public static double collapse = .95;
    public static double suckRange = 20.0;

    public static final Predicate<Entity> SUCK_PREDICATE = input -> {
        if (input instanceof EntityPlayer) {
            EntityPlayer p = (EntityPlayer) input;
            if (p.capabilities.isCreativeMode && p.capabilities.isFlying) {
                return false;
            }
        }

        return true;
    };

    public static final Predicate<Entity> OMNOM_PREDICATE = input -> {
        if (!(input instanceof EntityLivingBase)) {
            return false;
        }

        if (input instanceof EntityPlayer) {
            EntityPlayer p = (EntityPlayer) input;
            if (p.capabilities.isCreativeMode) {
                return false;
            }
        } else if (input instanceof EntityImmortalItem) {
            return false;
        }

        return true;
    };

    public EntityGapingVoid(World world) {
        super(world);
        this.isImmuneToFire = true;
        this.setSize(0.1F, 0.1F);
        this.ignoreFrustumCheck = true;
        //this.renderDistanceWeight = 100.0;//TODO Maybe something, dunno, HELP.
    }

    @Override
    protected void entityInit() {
        dataManager.register(AGE_PARAMETER, 0);
    }

    @SuppressWarnings ("unchecked")
    @Override
    public void onUpdate() {
        super.onUpdate();
        Vector3 pos = Vector3.fromEntity(this);

        // tick, tock
        int age = this.getAge();

        if (age >= maxLifetime) {
            this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, 6.0f, true);
            this.setDead();
        } else {
            if (age == 0) {
                this.worldObj.playSound(this.posX, this.posY, this.posZ, ModSounds.GAPING_VOID, SoundCategory.HOSTILE, 8.0F, 1.0F, true);
            }
            this.setAge(age + 1);
        }

        // poot poot
        double particlespeed = 4.5;

        double size = getVoidScale(age) * 0.5 - 0.2;
        for (int i = 0; i < 50; i++) {
            Vector3 particlePos = new Vector3(0, 0, size);
            particlePos.rotate(randy.nextFloat() * 180f, new Vector3(0, 1, 0));
            particlePos.rotate(randy.nextFloat() * 360f, new Vector3(1, 0, 0));

            Vector3 velocity = particlePos.copy().normalize();
            velocity.multiply(particlespeed);
            particlePos.add(pos);

            this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, particlePos.x, particlePos.y, particlePos.z, velocity.x, velocity.y, velocity.z);
        }

        // *slurping noises*
        Cuboid6 cuboid = new Cuboid6().add(pos);
        cuboid.expand(suckRange);
        List<Entity> sucked = this.worldObj.getEntitiesWithinAABB(Entity.class, cuboid.aabb(), SUCK_PREDICATE);

        double radius = getVoidScale(age) * 0.5;

        for (Entity suckee : sucked) {
            if (suckee != this) {
                double dx = this.posX - suckee.posX;
                double dy = this.posY - suckee.posY;
                double dz = this.posZ - suckee.posZ;

                double lensquared = dx * dx + dy * dy + dz * dz;
                double len = Math.sqrt(lensquared);
                double lenn = len / suckRange;

                if (len <= suckRange) {
                    double strength = (1 - lenn) * (1 - lenn);
                    double power = 0.075 * radius;

                    suckee.motionX += (dx / len) * strength * power;
                    suckee.motionY += (dy / len) * strength * power;
                    suckee.motionZ += (dz / len) * strength * power;
                }
            }
        }

        // om nom nom
        double nomrange = radius * 0.95;
        cuboid = new Cuboid6().add(pos);
        cuboid.expand(nomrange);
        List<Entity> nommed = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, cuboid.aabb(), OMNOM_PREDICATE);

        for (Entity nommee : nommed) {
            if (nommee != this) {
                Vector3 nomedPos = Vector3.fromEntity(nommee);
                Vector3 diff = pos.copy().subtract(nomedPos);

                double len = diff.mag();

                if (len <= nomrange) {
                    nommee.attackEntityFrom(DamageSource.outOfWorld, 3.0f);
                }
            }
        }

        // every half second, SMASH STUFF
        if (age % 10 == 0) {
            Vector3 posFloor = pos.copy().floor();

            int blockrange = (int) Math.round(nomrange);

            for (int y = -blockrange; y <= blockrange; y++) {
                for (int z = -blockrange; z <= blockrange; z++) {
                    for (int x = -blockrange; x <= blockrange; x++) {
                        Vector3 rPos = posFloor.copy().add(x, y, z);
                        BlockPos blockPos = rPos.pos();

                        if (blockPos.getY() < 0 || blockPos.getY() > 255) {
                            continue;
                        }

                        double dist = rPos.mag();
                        if (dist <= nomrange && !this.worldObj.isAirBlock(blockPos)) {
                            IBlockState state = worldObj.getBlockState(blockPos);
                            float resist = state.getBlock().getExplosionResistance(this);//TODO HELP state.getExplosionResistance(this, this.worldObj, lx, ly, lz, this.posX, this.posY, this.posZ);
                            if (resist <= 10.0) {
                                state.getBlock().dropBlockAsItemWithChance(worldObj, blockPos, state, 0.9F, 0);
                                this.worldObj.setBlockToAir(blockPos);
                            }
                        }
                    }
                }
            }
        }
    }

    private void setAge(int age) {
        dataManager.set(AGE_PARAMETER, age);
    }

    public int getAge() {
        return dataManager.get(AGE_PARAMETER);
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
        double life = age / (double) maxLifetime;

        double curve;
        if (life < collapse) {
            curve = 0.005 + ease(1 - ((collapse - life) / collapse)) * 0.995;
        } else {
            curve = ease(1 - ((life - collapse) / (1 - collapse)));
        }
        return 10.0 * curve;
    }

    private static double ease(double in) {
        double t = in - 1;
        return Math.sqrt(1 - t * t);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }
}
