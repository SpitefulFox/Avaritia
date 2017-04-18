package morph.avaritia.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityHeavenSubArrow extends EntityArrow {

    public EntityHeavenSubArrow(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public EntityHeavenSubArrow(World world, EntityLivingBase entity) {
        super(world, entity);

    }

    public EntityHeavenSubArrow(World world) {
        super(world);
    }

    @Override
    public void onUpdate() {
        this.rotationPitch = 0;
        this.rotationYaw = 0;
        super.onUpdate();

        if (inGround && timeInGround >= 20) {
            this.setDead();
        }
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.ARROW);//TODO This needs to be null, but it can't be, because vanilla.
    }
}
