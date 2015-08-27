package fox.spiteful.avaritia.compat.botania.alfheim;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.FieldHelper;
import fox.spiteful.avaritia.Lumberjack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderAlfheim extends WorldProvider {

	public static int dimensionID = 13;
	
	@Override
	public void registerWorldChunkManager()
    {
        this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.hell, 0.0F);
        this.isHellWorld = true;
        this.hasNoSky = false;
        if(FMLCommonHandler.instance().getSide().isClient()) {
            this.setCloudRenderer(new ApocalypseCloudRenderer());
            this.setSkyRenderer(new ApocalypseSkyRenderer());
        }
    }

    /**
     * Return Vec3D with biome specific fog color
     */
	@Override
    @SideOnly(Side.CLIENT)
    public Vec3 getFogColor(float p_76562_1_, float p_76562_2_)
    {
		/*double r = 0.1;
		double g = 0.04;
		double b = 0;
		
		double nr = 1.0;
		double ng = 0.5;
		double nb = 0.0;
		
		Minecraft mc = Minecraft.getMinecraft();
		Timer t = FieldHelper.get(AlfheimEvents.mcTimerField, mc);
		float partialTicks = t.renderPartialTicks;
		
		float n = FieldHelper.invoke(AlfheimEvents.getNightVisionBrightnessMethod, mc.entityRenderer, mc.thePlayer, partialTicks);
		double notn = 1.0 - n;
		
        return Vec3.createVectorHelper(n*nr + notn*r, n*ng + notn*g, n*nb + notn*b);*/
		return Vec3.createVectorHelper(0.1, 0.04, 0.0);
    }

    /**
     * Returns a new chunk provider which generates chunks for this world
     */
	@Override
    public IChunkProvider createChunkGenerator()
    {
        return new ChunkProviderAlfheim(this.worldObj, this.worldObj.getSeed());
    }

    /**
     * Returns 'true' if in the "main surface world", but 'false' if in the Nether or End dimensions.
     */
	@Override
    public boolean isSurfaceWorld()
    {
        return false;
    }

    /**
     * Will check if the x, z position specified is alright to be set as the map spawn point
     */
	@Override
    public boolean canCoordinateBeSpawn(int x, int z)
    {
        return true; // need to improve
    }

    /**
     * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
     */
	@Override
    public float calculateCelestialAngle(long tick, float partialTick)
    {
        return 1f;//0.425F;
    }

    /**
     * True if the player can respawn in this dimension (true = overworld, false = nether).
     */
	@Override
    public boolean canRespawnHere()
    {
        return true;
    }

    /**
     * Returns true if the given X,Z coordinate should show environmental fog.
     */
	@Override
    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int x, int z)
    {
		return false;
        //return true;
    }
	
	@Override
	public String getDimensionName() {
		return "Alfheim Eternal";
	}

	@Override
	public float getSunBrightnessFactor(float par1)
    {
        return 0.6f;
    }
	
	@Override
	public ChunkCoordinates getEntrancePortalLocation()
    {
        return new ChunkCoordinates(0, 85, 0);
    }
}
