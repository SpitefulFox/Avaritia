package fox.spiteful.avaritia.compat.botania.alfheim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fox.spiteful.avaritia.compat.botania.alfheim.Ruin.BlockRuinPalette.BlockPaletteInfo;

import net.minecraft.block.Block;

public class Ruin {
	public BlockArray blocks;
	public double ruinLevel;
	public Random rand;
	public BlockRuinPalette palette;
	
	public int xSize;
	public int ySize;
	public int zSize;
	public int xOffset;
	public int yOffset;
	public int zOffset;
	
	public Ruin(BlockArray blocks, double ruinLevel, Random rand) {
		this(blocks, ruinLevel, rand, new BlockRuinPalette());
	}
	public Ruin(BlockArray blocks, double ruinLevel, Random rand, BlockRuinPalette palette) {
		this (blocks, ruinLevel, rand, blocks.xSize, blocks.ySize, blocks.zSize, 0,0,0, palette);
	}
	public Ruin(BlockArray blocks, double ruinLevel, Random rand, int xSize, int ySize, int zSize, int xOffset, int yOffset, int zOffset) {
		this (blocks, ruinLevel, rand, xSize, ySize, zSize, xOffset,yOffset,zOffset, new BlockRuinPalette());
	}
	public Ruin(BlockArray blocks, double ruinLevel, Random rand, int xSize, int ySize, int zSize, int xOffset, int yOffset, int zOffset, BlockRuinPalette palette) {
		this.blocks = blocks;
		this.ruinLevel = ruinLevel;
		this.rand = rand;
		this.palette = palette;
		
		this.xSize = xSize;
		this.ySize = ySize;
		this.zSize = zSize;
		
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.zOffset = zOffset;
		
		int volume = this.xSize * this.ySize * this.zSize;
		int breaks = (int) Math.ceil((volume/16.0) * ruinLevel);
		
		for (int i=0; i<breaks; i++) {
			int x = rand.nextInt(this.xSize) - this.xOffset;
			int z = rand.nextInt(this.zSize) - this.zOffset;
			
			double ylevel = 1 - (rand.nextDouble() * rand.nextDouble());
			
			int y = (int) Math.round(ylevel * this.ySize) - this.yOffset;
			double rad = (6*rand.nextDouble() + 1)*ylevel*(0.1 + 0.9*ruinLevel) + 0.45 + (0.55*ruinLevel);
			
			this.ruinArea(x, y, z, rad);
		}
		
		this.disconnectionCheck();
		this.disconnectionCheck();
	}
	
	
	protected void dropBlock(int ox, int oy, int oz) {
		this.dropBlock(ox, oy, oz, 0);
	}
	protected void dropBlock(int ox, int oy, int oz, int initialdistance) {
		int x = ox;
		int y = oy;
		int z = oz;
		BlockMeta block = blocks.getBlock(x,y,z);
		if (block == null) {
			return;
		}
		blocks.setBlock(ox,oy,oz, null);
		
		int falldistance = initialdistance;
		
		while(true) {
			BlockMeta below = blocks.getBlock(x,y-1,z);
			
			if(blocks.outOfBounds(x,y-1,z)) {
				blocks.setBlock(x,y,z, block);
				break;
			}
			
			if(below == null) {
				y--;
				falldistance++;
			} else {
				boolean slide = false;
				int slidedir = 0;
				List<Integer> falldirs = new ArrayList<Integer>();
				if (!blocks.outOfBounds(x-1,y,z) && blocks.getBlock(x-1,y,z) == null && blocks.getBlock(x-1,y-1,z) == null) {
					falldirs.add(0);
				}
				if (!blocks.outOfBounds(x+1,y,z) && blocks.getBlock(x+1,y,z) == null && blocks.getBlock(x+1,y-1,z) == null) {
					falldirs.add(1);
				}
				if (!blocks.outOfBounds(x,y,z-1) && blocks.getBlock(x,y,z-1) == null && blocks.getBlock(x,y-1,z-1) == null) {
					falldirs.add(2);
				}
				if (!blocks.outOfBounds(x,y,z+1) && blocks.getBlock(x,y,z+1) == null && blocks.getBlock(x,y-1,z+1) == null) {
					falldirs.add(3);
				}
				
				if (!falldirs.isEmpty()) {
					slide = true;
					slidedir = falldirs.get(rand.nextInt(falldirs.size()));
				}
				
				if (slide) {
					impact(x,y-1,z, falldistance/4, block);
					if (slidedir == 0) {
						x--;
					} else if (slidedir == 1) {
						x++;
					} else if (slidedir == 2) {
						z--;
					} else {
						z++;
					}
				} else {
					BlockPaletteInfo blockinfo = this.palette.get(block);
					double breakchance = fallBreakChance(falldistance) * blockinfo.breakchance;
					if (rand.nextDouble() <= breakchance) {
						blocks.setBlock(x,y,z,blockinfo.broken);
					} else {
						if (blockinfo.hardness >= 1) {
							blocks.setBlock(x,y,z,block);
						}
					}
					impact(x,y-1,z, falldistance, block);
					break;
				}
			}
		}
	}
	
	protected void impact(int x, int y, int z, int falldistance, BlockMeta fallingblock) {
		BlockMeta block = blocks.getBlock(x,y,z);
		BlockPaletteInfo blockinfo = this.palette.get(block);
		double smashchance = fallBreakChance(falldistance) * blockinfo.smashchance;
		
		if (rand.nextDouble() <= smashchance) {
			blocks.setBlock(x,y,z, null);
			dropBlock(x,y+1,z, falldistance/3);
		}
		
		double breakchance = fallBreakChance(falldistance) * blockinfo.breakchance;
		if (blocks.getBlock(x,y-1,z) != null) {
			breakchance *= 0.75;
		}
		
		if (rand.nextDouble() <= breakchance) {
			if (blockinfo.broken != null) {
				blocks.setBlock(x,y,z, blockinfo.broken);
				impact(x,y,z, falldistance/2, fallingblock);
			} else {
				dropBlock(x,y,z);
				dropBlock(x,y+1,z, falldistance/3);
			}
		}
	}
	
	protected void ruinArea(int ox, int oy, int oz, double radius) {
		int range = (int)Math.ceil(radius*2);
		int offset = range/2;
		
		for (int y = 0; y<=range; y++) {
			List<Coord> coords = new ArrayList<Coord>();
			for (int x = 0; x<=range; x++) {
				if (x < 0 || x >= blocks.xSize) {
					continue;
				}
				for (int z = 0; z<=range; z++) {
	    			if (z < 0 || z >= blocks.zSize) {
	    				continue;
	    			}
	    			BlockMeta block = blocks.getBlock(ox+x-offset,oy+y-offset,oz+z-offset);
	    			if (block == null) {
	    				continue;
	    			}
	    			
	    			BlockPaletteInfo blockinfo = this.palette.get(block);
	    			
	    			double rx = (x - range/2);
	    			double ry = (y - range/2);
	    			double rz = (z - range/2);
	    			
	    			double dist = Math.sqrt(rx*rx+ry*ry+rz*rz) / radius;
	    			if(dist <= 1.0) {
	    				if (dist > .75) {
	    					double dchance = (dist - 0.75)*4;
	    					if (rand.nextDouble() > dchance) {
	    						coords.add(new Coord(x,y,z));
	    					} else if (blockinfo.broken != null || blockinfo.hardness < 1) {
	    						blocks.setBlock(ox+x-offset,oy+y-offset,oz+z-offset,blockinfo.broken);
	    					}
	    				} else {
	    					coords.add(new Coord(x,y,z));
	    				}
	    			}
	    		}
			}
			Collections.shuffle(coords, rand);
			for (int i=0; i<coords.size(); i++) {
				Coord c = coords.get(i);
				dropBlock(ox + c.x - range/2, oy + c.y - range/2, oz + c.z - range/2);
			}
		}
	}
	
	protected void disconnectionCheck() {
		int ox = blocks.xSize/2;
		int oz = blocks.zSize/2;
		
		boolean[] connected = new boolean[this.blocks.length];
		List<Coord> open = new ArrayList<Coord>();
		open.add(new Coord(ox,0,oz));
		
		while(!open.isEmpty()) {
			Coord c = open.remove(open.size()-1);
			if (!blocks.outOfBounds(c.x, c.y, c.z) && blocks.getBlock(c.x, c.y, c.z) != null) {
				connected[blocks.index(c.x,c.y,c.z)] = true;
				
				if(!blocks.outOfBounds(c.x-1, c.y, c.z) && blocks.getBlock(c.x-1, c.y, c.z) != null && connected[blocks.index(c.x-1,c.y,c.z)] == false) {
					open.add(new Coord(c.x-1,c.y,c.z));
				}
				if(!blocks.outOfBounds(c.x+1, c.y, c.z) && blocks.getBlock(c.x+1, c.y, c.z) != null && connected[blocks.index(c.x+1,c.y,c.z)] == false) {
					open.add(new Coord(c.x+1,c.y,c.z));
				}
				if(!blocks.outOfBounds(c.x, c.y, c.z-1) && blocks.getBlock(c.x, c.y, c.z-1) != null && connected[blocks.index(c.x,c.y,c.z-1)] == false) {
					open.add(new Coord(c.x,c.y,c.z-1));
				}
				if(!blocks.outOfBounds(c.x, c.y, c.z+1) && blocks.getBlock(c.x, c.y, c.z+1) != null && connected[blocks.index(c.x,c.y,c.z+1)] == false) {
					open.add(new Coord(c.x,c.y,c.z+1));
				}
				if(!blocks.outOfBounds(c.x, c.y-1, c.z) && blocks.getBlock(c.x, c.y-1, c.z) != null && connected[blocks.index(c.x,c.y-1,c.z)] == false) {
					open.add(new Coord(c.x,c.y-1,c.z));
				}
				if(!blocks.outOfBounds(c.x, c.y+1, c.z) && blocks.getBlock(c.x, c.y+1, c.z) != null && connected[blocks.index(c.x,c.y+1,c.z)] == false) {
					open.add(new Coord(c.x,c.y+1,c.z));
				}
			}
		}
		
		for (int y = 0; y<=blocks.ySize; y++) {
			List<Coord> coords = new ArrayList<Coord>();
			for (int x = 0; x<=blocks.xSize; x++) {
				for (int z = 0; z<=blocks.zSize; z++) {
					if (blocks.getBlock(x,y,z) != null && !connected[blocks.index(x,y,z)]) {
						coords.add(new Coord(x,y,z));
					}
				}
			}
			Collections.shuffle(coords, rand);
			for (int i=0; i<coords.size(); i++) {
				Coord c = coords.get(i);
				dropBlock(c.x,c.y,c.z);
			}
		}
	}
	
	protected double fallBreakChance(int distance) {
		double maxdist = 2;
		return Math.min(distance, maxdist) / maxdist;
	}
	
	// #################################################################################
	//	Utility Classes
	// #################################################################################
	public static class BlockArray {
		public BlockMeta[] blocks;
		public int xSize;
		public int ySize;
		public int zSize;
		public int length;
		
		public BlockArray(int xSize, int ySize, int zSize) {
			this.length = xSize*ySize*zSize;
			this.blocks = new BlockMeta[this.length];
			this.xSize = xSize;
			this.ySize = ySize;
			this.zSize = zSize;
		}
		
		public boolean outOfBounds(int x, int y, int z) {
			return (x<0||x>=xSize||y<0||y>=ySize||z<0||z>=zSize);
		}
		
		public void setBlock(int x, int y, int z, BlockMeta block) {
			if (outOfBounds(x,y,z)) {
				return;
			}
			
			blocks[index(x,y,z)] = block;
		}
		
		public BlockMeta getBlock(int x, int y, int z) {
			if (outOfBounds(x,y,z)) {
				return null;
			}
			
			return blocks[index(x,y,z)];
		}
		
		public void fillBlocks(int x, int y, int z, int x2, int y2, int z2, BlockMeta block) {
			for (int ix = x; ix<=x2; ix++) {
				for (int iy = y; iy<=y2; iy++) {
					for (int iz = z; iz<=z2; iz++) {
		            	setBlock(ix,iy,iz, block);
		        	}	
		    	}
			}
		}
		
		protected int index(int x, int y, int z) {
			return y * zSize * xSize + z * xSize + x;
		}
	}
	
	public static class BlockMeta {
		public Block block;
		public byte meta;
		
		public BlockMeta(Block block, int meta) {
			this.block = block;
			this.meta = (byte)(meta & 0xFF);
		}
		
		@Override
		public String toString() {
			return this.block.toString()+"#"+this.meta;
		}
		
		@Override
		public int hashCode() {
			return this.block.hashCode() + this.meta;
		}
	}
	
	public static class BlockRuinPalette {
		protected Map<BlockMeta, BlockPaletteInfo> infomap = new HashMap<BlockMeta, BlockPaletteInfo>();
		
		public BlockPaletteInfo get(BlockMeta block) {
			if (infomap.containsKey(block)) {
				return infomap.get(block);
			}
			return BlockPaletteInfo.DEFAULT;
		}
		
		public void set(BlockMeta block, double hardness, double breakchance, double smashchance) {
			this.set(block, hardness, breakchance, smashchance, null);
		}
		public void set(BlockMeta block, double hardness, double breakchance, double smashchance, BlockMeta broken) {
			infomap.put(block, new BlockPaletteInfo(hardness, breakchance, smashchance, broken));
		}
		
		public static class BlockPaletteInfo {
			public static final BlockPaletteInfo DEFAULT = new BlockPaletteInfo(1.0, 0.2, 0.2, null);
			
			public final double hardness;
			public final double breakchance;
			public final double smashchance;
			public final BlockMeta broken;
			
			public BlockPaletteInfo(double hardness, double breakchance, double smashchance, BlockMeta broken) {
				this.hardness = hardness;
				this.breakchance = breakchance;
				this.smashchance = smashchance;
				this.broken = broken;
			}
		}
	}
	
	// #################################################################################
	//	Internal Classes
	// #################################################################################
	protected static class Coord {
		int x;
		int y;
		int z;
		
		public Coord(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}
}
