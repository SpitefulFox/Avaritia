package fox.spiteful.avaritia.compat.botania.alfheim;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.ComponentScatteredFeaturePieces;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;

public class MapGenCity extends MapGenStructure {

	public MapGenCity() {
		this.range = (int) Math.ceil(ChunkProviderAlfheim.CITYRADIUS / 16);
	}
	
	@Override
	public String func_143025_a() {
		return "AlfCity";
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
		return chunkX == 0 && chunkZ == 0;
	}

	@Override
	protected StructureStart getStructureStart(int chunkX, int chunkZ) {
		return new Start(this.worldObj, this.rand, chunkX, chunkZ);
	}

	
	public static class Start extends StructureStart {
		public Start() {}
		
		@SuppressWarnings("unchecked")
		public Start(World world, Random rand, int chunkX, int chunkZ) {
			super(chunkX, chunkZ);
			
			int range = (int) Math.ceil(ChunkProviderAlfheim.CITYRADIUS / 16);
			
			/*for (int z = -range; z <= range; z++) {
				for (int x = -range; x <= range; x++) {
					double r = Math.sqrt(x*x+z*z);
					if (r <= range) {
						ComponentScatteredFeaturePieces.SwampHut swamphut = new ComponentScatteredFeaturePieces.SwampHut(rand, (chunkX+x) * 16, (chunkZ+z) * 16);
			            this.components.add(swamphut);
					}
				}
			}*/

			ComponentScatteredFeaturePieces.SwampHut swamphut = new ComponentScatteredFeaturePieces.SwampHut(rand, (chunkX) * 16, (chunkZ) * 16);
			this.components.add(swamphut);

            this.updateBoundingBox();
		}
	}
}
