package fox.spiteful.avaritia.compat.botania.alfheim;

import fox.spiteful.avaritia.Lumberjack;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.MapGenRavine;

public class MapGenAlfheimRavines extends MapGenRavine {

	@Override
	protected void func_151538_a(World world, int originChunkX, int originChunkZ, int chunkX, int chunkZ, Block[] blocks)
    {
		if (this.rand.nextInt(6) == 0) {
			Lumberjack.info("crack!");
			double x = (double)(originChunkX * 16 + this.rand.nextInt(16));
            double y = 67;//(double)(this.rand.nextInt(20) + 50);
            double z = (double)(originChunkZ * 16 + this.rand.nextInt(16));

			int angles = this.rand.nextInt(4)+3;
			double angleinc = (Math.PI*2.0) / angles;
			double baseangle = this.rand.nextDouble() * Math.PI * 2.0;
			
			for (int i=0; i<angles; i++) {
				float f1 = 1f;//(this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
	            float f2 = 1f;//(this.rand.nextFloat() * 2.0F + this.rand.nextFloat()) * 2.0F;
	            float a = (float)(baseangle + angleinc * i);
	            this.func_151540_a(this.rand.nextLong(), chunkX, chunkZ, blocks, x, y, z, f2, a, f1, 0, 0, 3.0D);
			}
		}
    }
	
	@Override
	protected void digBlock(Block[] data, int index, int x, int y, int z, int chunkX, int chunkZ, boolean foundTop)
    {
        Block block  = data[index];

        if (block != null)
        {
            /*if (y < 10)
            {
                data[index] = Blocks.flowing_lava;
            }
            else
            {*/
                data[index] = null;
            //}
        }
    }
}
