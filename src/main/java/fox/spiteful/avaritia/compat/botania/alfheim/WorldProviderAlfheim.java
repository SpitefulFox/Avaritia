package fox.spiteful.avaritia.compat.botania.alfheim;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.Lumberjack;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraftforge.client.IRenderHandler;

public class WorldProviderAlfheim extends WorldProvider {

	public static int dimensionID = 13;
	
	@Override
	public void registerWorldChunkManager()
    {
        this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.hell, 0.0F);
        this.isHellWorld = true;
        this.hasNoSky = false;
        this.setCloudRenderer(new ApocalypseCloudRenderer());
        this.setSkyRenderer(new ApocalypseSkyRenderer());
    }

    /**
     * Return Vec3D with biome specific fog color
     */
	@Override
    @SideOnly(Side.CLIENT)
    public Vec3 getFogColor(float p_76562_1_, float p_76562_2_)
    {
        return Vec3.createVectorHelper(0.1, 0.04, 0.00);
    }

    /**
     * Creates the light to brightness table
     */
	/*@Override
    protected void generateLightBrightnessTable()
    {
        float f = 0.1F;

        for (int i = 0; i <= 15; ++i)
        {
            float f1 = 1.0F - (float)i / 15.0F;
            this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
        }
    }*/

    /**
     * Returns a new chunk provider which generates chunks for this world
     */
	@Override
    public IChunkProvider createChunkGenerator()
    {
        return new ChunkProviderGenerate(this.worldObj, this.worldObj.getSeed(), false);
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
}
