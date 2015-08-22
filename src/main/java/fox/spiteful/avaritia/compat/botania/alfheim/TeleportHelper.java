package fox.spiteful.avaritia.compat.botania.alfheim;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class TeleportHelper {
	public static void travelToDimension(Entity ent, int x, int y, int z, int dim){
		if(ent instanceof EntityPlayerMP){
			MinecraftServer minecraftserver = MinecraftServer.getServer();
			WorldServer newWorldServer = minecraftserver.worldServerForDimension(dim);
			minecraftserver.getConfigurationManager().transferPlayerToDimension((EntityPlayerMP)ent, dim, new BasicTeleporter(newWorldServer));
			((EntityPlayer)ent).setPositionAndUpdate(x+0.5, y+0.5, z+0.5);
			return;
		}


		if (!ent.worldObj.isRemote && !ent.isDead)
		{
			ent.worldObj.theProfiler.startSection("changeDimension");
			MinecraftServer minecraftserver = MinecraftServer.getServer();
			int j = ent.dimension;
			WorldServer worldserver = minecraftserver.worldServerForDimension(j);
			WorldServer worldserver1 = minecraftserver.worldServerForDimension(dim);
			ent.dimension = dim;

			if (j == 1 && dim == 1)
			{
				worldserver1 = minecraftserver.worldServerForDimension(0);
				ent.dimension = 0;
			}

			ent.worldObj.removeEntity(ent);
			ent.isDead = false;
			ent.worldObj.theProfiler.startSection("reposition");
			minecraftserver.getConfigurationManager().transferEntityToWorld(ent, j, worldserver, worldserver1);
			ent.worldObj.theProfiler.endStartSection("reloading");
			Entity entity = EntityList.createEntityByName(EntityList.getEntityString(ent), worldserver1);

			if (entity != null)
			{
				entity.copyDataFrom(ent, true);

				if (j == 1 && dim == 1)
				{
					entity.setLocationAndAngles(x+0.5, y+0.5, z+0.5, entity.rotationYaw, entity.rotationPitch);
				}

				worldserver1.spawnEntityInWorld(entity);
				entity.setPosition(x+0.5, y+0.5, z+0.5);
			}

			ent.isDead = true;
			ent.worldObj.theProfiler.endSection();
			worldserver.resetUpdateEntityTick();
			worldserver1.resetUpdateEntityTick();
			ent.worldObj.theProfiler.endSection();
		}
	}
	
	public static void travelToOrFromAlfheim(EntityPlayer player) {
		if (DimensionManager.getWorld(0) == player.worldObj) {
			// overworld to alfheim
			travelToDimension(player, 0, 85, 0, WorldProviderAlfheim.dimensionID);
		} else {
			// alfheim to overworld
			sendPlayerHome(player);
		}
	}
	
	public static void sendPlayerHome(EntityPlayer player) {
		World world = DimensionManager.getWorld(0);
		ChunkCoordinates coord = player.getBedLocation(0);
		
		if (coord == null) {
			coord = world.getSpawnPoint();
		}
		
		coord = forceLocation(world, coord);
		
		if (coord == null) {
			coord = world.getSpawnPoint();
		}

		travelToDimension(player, coord.posX, coord.posY, coord.posZ, 0);
		
		while (!world.getCollidingBoundingBoxes(player, player.boundingBox).isEmpty())
        {
            player.setPositionAndUpdate(player.posX, player.posY + 1.0D, player.posZ);
        }
	}
	
	public static ChunkCoordinates forceLocation(World world, ChunkCoordinates coord) {
		return null;
	}
}
