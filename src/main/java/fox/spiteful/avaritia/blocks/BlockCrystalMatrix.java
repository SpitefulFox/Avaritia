package fox.spiteful.avaritia.blocks;

import fox.spiteful.avaritia.Avaritia;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;

public class BlockCrystalMatrix extends Block {

    public BlockCrystalMatrix(){
        super(Material.iron);
        setStepSound(Block.soundTypeGlass);
        setHardness(50.0F);
        setResistance(2000.0F);
        setBlockName("block_crystal_matrix");
        setHarvestLevel("pickaxe", 3);
        setBlockTextureName("avaritia:block_crystal_matrix");
        setCreativeTab(Avaritia.tab);
    }

    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ){
        return true;
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity){
        return false;
    }

}
