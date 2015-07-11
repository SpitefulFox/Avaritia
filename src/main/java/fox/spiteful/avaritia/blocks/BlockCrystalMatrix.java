package fox.spiteful.avaritia.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockCrystalMatrix extends Block {

    public BlockCrystalMatrix(){
        super(Material.iron);
        setStepSound(Block.soundTypeGlass);
        setHardness(50.0F);
        setResistance(2000.0F);
        setBlockName("block_crystal_matrix");
        setHarvestLevel("pickaxe", 3);
        setBlockTextureName("avaritia:block_crystal_matrix");
    }

}
