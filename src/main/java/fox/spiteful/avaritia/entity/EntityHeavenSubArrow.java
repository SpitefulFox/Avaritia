package fox.spiteful.avaritia.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.World;

public class EntityHeavenSubArrow extends EntityArrow {
	public EntityHeavenSubArrow(World world, double x,	double y, double z) {
		super(world, x, y, z);
	}

	public EntityHeavenSubArrow(World world, EntityLivingBase entity, EntityLivingBase entity2, float something, float otherthing) {
		super(world, entity, entity2, something, otherthing);
	}

	public EntityHeavenSubArrow(World world, EntityLivingBase entity, float something) {
		super(world, entity, something);
		
	}

	public EntityHeavenSubArrow(World world) {
		super(world);
	}
	
	@Override
	public void onUpdate() {
		this.rotationPitch = 0;
		this.rotationYaw = 0;
		super.onUpdate();
		
		if (EntityHeavenArrow.getInGround(this) && EntityHeavenArrow.getTicksInGround(this) >= 20) {
			this.setDead();
		}
	}
}
