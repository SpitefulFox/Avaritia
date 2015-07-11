package fox.spiteful.avaritia.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.Avaritia;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockDireCrafting extends Block {
    private static IIcon top, sides, bottom;

    public BlockDireCrafting(){
        super(Material.iron);
        setStepSound(Block.soundTypeGlass);
        setHardness(50.0F);
        setResistance(2000.0F);
        setBlockName("dire_crafting");
        setHarvestLevel("pickaxe", 3);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons (IIconRegister iconRegister)
    {
        this.top = iconRegister.registerIcon("avaritia:dire_crafting_top");
        this.sides = iconRegister.registerIcon("avaritia:dire_crafting_side");
        this.bottom = iconRegister.registerIcon("avaritia:crystal_matrix_block");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon (int side, int metadata)
    {
        if (side == 0)
            return bottom;
        if (side == 1)
            return top;
        return sides;
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
            player.openGui(Avaritia.instance, 1, world, x, y, z);
            return true;
        }
    }
}
