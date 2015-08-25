package fox.spiteful.avaritia.compat.botania.alfheim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
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
		protected int CITYRADIUS;
		protected int ROADSIZE = 8;
		protected int BLOCKSIZE = 24;
		protected int BLOCKS;
		
		public Start() {}
		
		@SuppressWarnings("unchecked")
		public Start(World world, Random rand, int chunkX, int chunkZ) {
			super(chunkX, chunkZ);
			this.CITYRADIUS = ChunkProviderAlfheim.CITYRADIUS;
			this.BLOCKS = MathHelper.floor_double((CITYRADIUS*2 + ROADSIZE) / (double)(BLOCKSIZE + ROADSIZE));
			if (this.BLOCKS % 2 != 0) {
				this.BLOCKS--;
			}
			
			int blockwidth = BLOCKS * BLOCKSIZE + (BLOCKS-1) * ROADSIZE;
			int origin = -blockwidth/2;
			
			List<ChunkCoordinates> cells = new ArrayList<ChunkCoordinates>();
			int[] cellfill = new int[BLOCKS*BLOCKS];
			for (int i=0; i<cellfill.length; i++) {
				cellfill[i] = 0;
			}

			for (int bx = 0; bx < BLOCKS; bx++) {
				for (int bz = 0; bz < BLOCKS; bz++) {
					if (bx > BLOCKS/2-3 && bx < BLOCKS/2+2 && bz > BLOCKS/2-3 && bz < BLOCKS/2+2) {
						continue;
					}
					int x = origin + (BLOCKSIZE + ROADSIZE) * bx;
					int z = origin + (BLOCKSIZE + ROADSIZE) * bz;
					
					double cx = x + BLOCKSIZE * 0.5;
					double cz = z + BLOCKSIZE * 0.5;
					
					double rad = Math.sqrt(cx*cx + cz*cz) / (CITYRADIUS - (BLOCKSIZE*0.75));
					
					if (rad < 1.0) {
						if (rand.nextDouble() > rad - 0.4) {
							cells.add(new ChunkCoordinates(bx,bz,0));
							cellfill[bz*BLOCKS + bx] = 1;
						}
					}
				}
			}
			
			Collections.shuffle(cells);
			
			while (cells.size() > 0) {
				ChunkCoordinates cell = cells.remove(0);
				int bx = cell.posX;
				int bz = cell.posY;
				int index = bz * BLOCKS + bx;
				if (cellfill[index] != 1) {
					continue;
				}
				
				int dx = BLOCKS/2 - bx;
				int dz = BLOCKS/2 - bz;
				double dist = Math.sqrt(dx*dx + dz*dz) / (BLOCKS*0.5);
				
				int width = 1;
				int height = 1;
				
				if (rand.nextDouble() < 1.5 * (1-dist) - 0.05) {
					width += 1;
					height += 1;
					
					if (rand.nextDouble() < 0.1) {
						width += 1;
					}
					if (rand.nextDouble() < 0.1) {
						height += 1;
					}
				}
				
				while(true) {
					if (width == 1 && height == 1) {
						break;
					}
					
					boolean ok = true;
					for (int cx = 0; cx < width; cx++) {
						for (int cz = 0; cz < height; cz++) {
							if (bx+cx >= BLOCKS || bz+cz >= BLOCKS) {
								ok = false;
								break;
							}
							if ((bx < BLOCKS/2 && bx+cx >= BLOCKS/2) || (bz < BLOCKS/2 && bz+cz >= BLOCKS/2)) {
								ok = false;
								break;
							}
							int cindex = (bz+cz)*BLOCKS + (bx+cx);
							if (cellfill[cindex] != 1) {
								ok = false;
								break;
							}
						}
					}
					
					if (ok)  {
						break;
					} else {
						if (width > 1 && height > 1) {
							if (rand.nextBoolean()) {
								width--;
							} else {
								height--;
							}
						} else if (width > 1) {
							width--;
						} else {
							height--;
						}
					}
				}
				
				for (int cx = 0; cx < width; cx++) {
					for (int cz = 0; cz < height; cz++) {
						int cindex = (bz+cz)*BLOCKS + (bx+cx);
						cellfill[cindex] = 2;
					}
				}
				
				double size = Math.sqrt(width*height);
				int floors = (int)Math.round(0.5 + (size * 1.45 * ((1-dist) + 0.3) * (rand.nextDouble() * rand.nextDouble())) + (rand.nextDouble() * (1-dist) * 0.6) + rand.nextDouble() * 0.2);
				
				int x = origin + (BLOCKSIZE+ROADSIZE) * bx;
				int z = origin + (BLOCKSIZE+ROADSIZE) * bz;
				int structurewidth = width * BLOCKSIZE + (width-1) * ROADSIZE;
				int structureheight = height * BLOCKSIZE + (height-1) * ROADSIZE;
				
				/*ComponentScatteredFeaturePieces.SwampHut swamphut = new ComponentScatteredFeaturePieces.SwampHut(rand, x, z);
				this.components.add(swamphut);*/
				
				ComponentCityParts.CityRuin ruin = new ComponentCityParts.CityRuin(rand, x, z, structurewidth, structureheight, floors);
				this.components.add(ruin);
			}
			
			
			
			//ComponentScatteredFeaturePieces.SwampHut swamphut = new ComponentScatteredFeaturePieces.SwampHut(rand, (chunkX) * 16, (chunkZ) * 16);
			//this.components.add(swamphut);

            this.updateBoundingBox();
		}
	}
}
