package fox.spiteful.avaritia.compat.botania;

import fox.spiteful.avaritia.tile.TileLudicrous;
import net.minecraft.nbt.NBTTagCompound;


public class TileInfinitato extends TileLudicrous {

	private static final String TAG_NAME = "name";

	public int jumpTicks = 0;
	public String name = "";
    public int nextDoIt = 0;

    public void interact() {
        jump();
        if(name.equalsIgnoreCase("shia labeouf") && !worldObj.isRemote && nextDoIt == 0) {
            nextDoIt = 40;
            worldObj.playSoundEffect(xCoord, yCoord, zCoord, "botania:doit", 2.5F, 0.7F);
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