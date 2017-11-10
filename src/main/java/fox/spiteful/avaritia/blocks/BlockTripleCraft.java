package fox.spiteful.avaritia.blocks;

import fox.spiteful.avaritia.Avaritia;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockTripleCraft extends Block {

    public BlockTripleCraft(){
        super(Material.wood);
        setHardness(8.0F);
        setBlockName("very_compressed_workbench");
        setStepSound(Block.soundTypeWood);
        setBlockTextureName("avaritia:triple_craft");
        setCreativeTab(Avaritia.tab);

    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
            player.openGui(Avaritia.instance, 0, world, x, y, z);
            return true;
        }
    }

}
