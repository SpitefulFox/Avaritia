package fox.spiteful.avaritia.entity;

import fox.spiteful.avaritia.Lumberjack;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityGapingVoid extends Entity {
	
	public EntityGapingVoid(World world) {
		super(world);
		this.isImmuneToFire = true;
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObject(12, 0);
		dataWatcher.setObjectWatched(12);
		Lumberjack.info("SPAWN");
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		
		/*int age = this.getAge();
		
		if (age >= 100) {
			this.setDead();
		} else {
			this.setAge(age+1);
		}*/
	}

	
	private void setAge(int age) {
		dataWatcher.updateObject(12, age);
	}
	
	private int getAge() {
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
}
