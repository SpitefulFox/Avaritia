package fox.spiteful.avaritia.compat.botania.alfheim;

import java.util.Random;

import fox.spiteful.avaritia.Lumberjack;
import fox.spiteful.avaritia.compat.botania.alfheim.Ruin.BlockArray;
import fox.spiteful.avaritia.compat.botania.alfheim.Ruin.BlockMeta;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public class ComponentCityParts {

	public static void registerParts() {
		MapGenStructureIO.func_143031_a(CityRuin.class, "AlfRuin");
	}
	
	public static class CityRuin extends CityPart {
		public static final int outline = 5;

		public CityRuin() {}
		
		public CityRuin(Random rand, int x, int z, int xsize, int zsize, int floors) {
			super(rand, x-outline, 64, z-outline, xsize+outline*2, floors * 7 + 3 + 8, zsize+outline*2);
		}
		
		@Override
		public boolean addComponentParts(World world, Random rand, StructureBoundingBox bounds) {
			if (!this.processHeight(world, bounds, -3)) {
				return false;
			}
			this.genRand.setSeed(this.randSeed);
			
			int border = 16;
			
			int minx = Math.max(this.boundingBox.minX, bounds.minX - border);
			int maxx = Math.min(this.boundingBox.maxX, bounds.maxX + border);
			int minz = Math.max(this.boundingBox.minZ, bounds.minZ - border);
			int maxz = Math.min(this.boundingBox.maxZ, bounds.maxZ + border);
			
			int xsize = this.boundingBox.getXSize();
			int ysize = this.boundingBox.getYSize();
			int zsize = this.boundingBox.getZSize();
			
			int axsize = (maxx-minx)+1;
			int aysize = ysize;
			int azsize = (maxz-minz)+1;
			
			int xoffset = minx - this.boundingBox.minX;
			int zoffset = minz - this.boundingBox.minZ;
			
			Lumberjack.info("xyz: "+this.boundingBox+", chunk: "+bounds+", size: "+xsize+","+ysize+","+zsize+",  mxmy: "+minx+","+minz+"; "+maxx+","+maxz+", asize: "+axsize+","+aysize+","+azsize+",  offset: "+xoffset+","+zoffset);
			
			BlockArray blocks = new BlockArray(axsize, aysize, azsize);
			
			blocks.fillBlocks(outline-xoffset, 0, outline-zoffset, (xsize-outline-1)-xoffset, ysize, (zsize-outline-1)-zoffset, new BlockMeta(Blocks.cobblestone, 0));
			//blocks.fillBlocks(-xoffset, 0, -zoffset, (xsize-1)-xoffset, ysize, (zsize-1)-zoffset, new BlockMeta(Blocks.cobblestone, 0));
			
			new Ruin(blocks, 0.3 + this.genRand.nextDouble()*0.7, this.genRand, xsize,ysize,zsize, xoffset,0,zoffset);
			
			for (int x = 0; x<axsize; x++) {
				for (int y = 0; y<aysize; y++) {
					for (int z = 0; z<azsize; z++) {
						BlockMeta block = blocks.getBlock(x, y, z);
						if (block != null) {
							this.placeBlockAtCurrentPosition(world, block.block, block.meta, x + xoffset, y, z + zoffset, bounds);
						}
					}	
				}
			}
			
			//this.fillWithMetadataBlocks(world, bounds, 0, 0, 0, this.boundingBox.getXSize(), this.boundingBox.getYSize(), this.boundingBox.getZSize(), Blocks.planks, 1, Blocks.planks, 1, false);
			
			return true;
		}
	}
	
	public static abstract class CityPart extends StructureComponent {
		protected int partSizeX;
		protected int partSizeY;
		protected int partSizeZ;
		protected int partPosY = -1;
		
		protected Random genRand;
		protected long randSeed;
		
		public CityPart() {};
		
		protected CityPart(Random rand, int x, int y, int z, int xsize, int ysize, int zsize) {
			super(0);
			this.partSizeX = xsize;
			this.partSizeY = ysize;
			this.partSizeZ = zsize;
			this.coordBaseMode = 0;
			
			this.boundingBox = new StructureBoundingBox(x, y, z, x+xsize-1, y+ysize-1, z+zsize-1);
			
			this.randSeed = rand.nextLong();
			this.genRand = new Random(this.randSeed);
		}
		
		// save to NBT
		@Override
		protected void func_143012_a(NBTTagCompound tag)
        {
            tag.setInteger("Width", this.partSizeX);
            tag.setInteger("Height", this.partSizeY);
            tag.setInteger("Depth", this.partSizeZ);
            tag.setInteger("HPos", this.partPosY);
            tag.setLong("RSeed", this.randSeed);
        }

		// load from NBT
		@Override
        protected void func_143011_b(NBTTagCompound tag)
        {
            this.partSizeX = tag.getInteger("Width");
            this.partSizeY = tag.getInteger("Height");
            this.partSizeZ = tag.getInteger("Depth");
            this.partPosY = tag.getInteger("HPos");
            this.randSeed = tag.getLong("RSeed");
            this.genRand = new Random(this.randSeed);
        }
		
		protected boolean processHeight(World world, StructureBoundingBox bounds, int offset)
        {
            if (this.partPosY >= 0)
            {
                return true;
            }
            else
            {
                int j = 0;
                int k = 0;

                for (int l = this.boundingBox.minZ; l <= this.boundingBox.maxZ; ++l)
                {
                    for (int i1 = this.boundingBox.minX; i1 <= this.boundingBox.maxX; ++i1)
                    {
                        if (bounds.isVecInside(i1, 64, l))
                        {
                            j += Math.max(world.getTopSolidOrLiquidBlock(i1, l), world.provider.getAverageGroundLevel());
                            ++k;
                        }
                    }
                }

                if (k == 0)
                {
                    return false;
                }
                else
                {
                    this.partPosY = j / k;
                    this.boundingBox.offset(0, this.partPosY - this.boundingBox.minY + offset, 0);
                    return true;
                }
            }
        }
	}
}
