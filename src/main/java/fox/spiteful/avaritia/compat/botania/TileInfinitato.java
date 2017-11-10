package fox.spiteful.avaritia.compat.botania;

import java.util.List;

import fox.spiteful.avaritia.Lumberjack;
import fox.spiteful.avaritia.tile.TileLudicrous;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;


public class TileInfinitato extends TileLudicrous {

	private static final String TAG_NAME = "name";

	public int jumpTicks = 0;
	public String name = "";
    public int nextDoIt = 0;

    @SuppressWarnings("unchecked")
	public void interact() {
        jump();
        if(name.equalsIgnoreCase("shia labeouf") && !worldObj.isRemote && nextDoIt == 0) {
            nextDoIt = 40;
            worldObj.playSoundEffect(xCoord, yCoord, zCoord, "botania:doit", 2.5F, 0.7F);
        }
        
        double radius = 10.5;
        int time = 3600;
        List<EntityLivingBase> inspired = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(xCoord-radius, yCoord-radius, zCoord-radius, xCoord+radius, yCoord+radius, zCoord+radius));
        
        for (EntityLivingBase ent : inspired) {
        	Lumberjack.info(ent);
        	double dx = ent.posX - xCoord;
        	double dy = ent.posY - yCoord;
        	double dz = ent.posZ - zCoord;
        	double dist = Math.sqrt(dx*dx + dy*dy + dz*dz);
        	
        	if (dist <= radius) {
	        	ent.addPotionEffect(new PotionEffect(Potion.damageBoost.id, time, 1));
	        	ent.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, time, 0));
	        	ent.addPotionEffect(new PotionEffect(Potion.jump.id, time, 0));
	        	ent.addPotionEffect(new PotionEffect(Potion.regeneration.id, time, 1));
	        	ent.addPotionEffect(new PotionEffect(Potion.resistance.id, time, 1));
	        	ent.addPotionEffect(new PotionEffect(Potion.digSpeed.id, time, 1));
	        	ent.addPotionEffect(new PotionEffect(Potion.fireResistance.id, time, 0));
	        	ent.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, time, 0));
	        	ent.addPotionEffect(new PotionEffect(Potion.nightVision.id, time, 0));
	        	ent.addPotionEffect(new PotionEffect(Potion.field_76444_x.id, time, 4)); // absorb
                ent.addPotionEffect(new PotionEffect(Potion.field_76443_y.id, time, 4)); // saturation
        	}
        }
    }

    public void jump() {
		if(jumpTicks == 0)
			jumpTicks = 40;
	}

	@Override
	public void updateEntity() {
		/*if(worldObj.rand.nextInt(100) == 0)
			jump();*/

		if(jumpTicks > 0) {
			jumpTicks--;
			if (jumpTicks == 20 || jumpTicks == 0) {
				this.worldObj.createExplosion(null, this.xCoord+0.5, this.yCoord, this.zCoord+0.5, 0.0f, true);
			}
		}
        if(nextDoIt > 0)
            nextDoIt--;
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		cmp.setString(TAG_NAME, name);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		name = cmp.getString(TAG_NAME);
	}
}