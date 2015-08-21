package fox.spiteful.avaritia.compat.botania.alfheim;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import vazkii.botania.common.block.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderAlfheim implements IChunkProvider {
	public final long seed;
	public Random rand;
	public final World world;
	
	protected AlfheimNoise noise;
	protected MapGenAlfheimRavines cracks = new MapGenAlfheimRavines();
	
	public ChunkProviderAlfheim(World world, long seed) {
		this.seed = seed;
		this.rand = new Random(this.seed);
		this.world = world;
		this.noise = new AlfheimNoise(this.rand.nextLong());
	}
	
	@SuppressWarnings("rawtypes")
	public List spawnList = new ArrayList();
	
	@Override
	public boolean chunkExists(int chunkX, int chunkZ) {
		return true;
	}

	@Override
	public Chunk provideChunk(int chunkX, int chunkZ) {
		this.rand.setSeed((long)chunkX * 1497631652873L + (long)chunkZ * 902659997773L);
		Block[] blocks = new Block[65536];
		byte[] meta = new byte[65536];
		
		this.fillChunkArray(chunkX, chunkZ, blocks, meta);
		
		//this.cracks.func_151539_a(this, this.world, chunkX, chunkZ, blocks);
		
		Chunk chunk = new Chunk(this.world, blocks, meta, chunkX, chunkZ);
		byte[] biomes = chunk.getBiomeArray();
		
		for (int i=0; i<biomes.length; i++) {
			biomes[i] = (byte) BiomeGenBase.desert.biomeID;
		}
		
		chunk.generateSkylightMap();
		
		return chunk;
	}
	
	public void fillChunkArray(int chunkX, int chunkZ, Block[] blocks, byte[] meta) {
		
		for(int x = 0; x<=15; x++) {
			for(int z = 0; z<=15; z++) {
				double height = this.noise.getJordanDefault(chunkX * 16 + x, chunkZ * 16 + z);
				int threshold = MathHelper.floor_double(64 + height * 64);
				
				for(int y = 0; y<254; y++) {
					int pos = y | z << 8 | x << 12;
					
					if (y <= threshold) {
						
						blocks[pos] = ModBlocks.livingrock;
						meta[pos] = 0;
					}
				}
			}
		}
		
	}

	@Override
	public Chunk loadChunk(int chunkX, int chunkZ) {
		return this.provideChunk(chunkX, chunkZ);
	}

	@Override
	public void populate(IChunkProvider provider, int chunkX, int chunkZ) {
		// TODO Auto-generated method stub

		
	}

	@Override
	public boolean saveChunks(boolean mode, IProgressUpdate progress) {
		return true;
	}

	@Override
	public boolean unloadQueuedChunks() {
		return false;
	}

	@Override
	public boolean canSave() {
		return true;
	}

	@Override
	public String makeString() {
		return "AlfheimEternalSource";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getPossibleCreatures(EnumCreatureType type,	int x, int y, int z) {
		return this.spawnList;
	}

	// structure location check
	@Override
	public ChunkPosition func_147416_a(World world, String structureType, int x, int y, int z) {
		return null;
	}

	@Override
	public int getLoadedChunkCount() {
		return 0;
	}

	@Override
	public void recreateStructures(int chunkX, int chunkZ) {
		// TODO Auto-generated method stub
	}

	@Override
	public void saveExtraData() {}

}
