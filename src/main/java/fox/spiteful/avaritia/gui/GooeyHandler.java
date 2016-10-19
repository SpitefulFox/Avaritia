package fox.spiteful.avaritia.gui;

import fox.spiteful.avaritia.tile.TileEntityDireCrafting;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GooeyHandler implements IGuiHandler {

    @Override
    public Object getClientGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z){
        if(ID == 0)
            return new GuiCrafting(player.inventory, world, new BlockPos(x, y, z));
        else if(ID == 1)
            return new GUIExtremeCrafting(player.inventory, world, new BlockPos(x, y, z), (TileEntityDireCrafting)world.getTileEntity(new BlockPos(x, y, z)));
        return null;
    }

    @Override
    public Object getServerGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z){
        if(ID == 0)
            return new ContainerCustomWorkbench(player.inventory, world, new BlockPos(x, y, z));
        else if(ID == 1)
            return new ContainerExtremeCrafting(player.inventory, world, new BlockPos(x, y, z), (TileEntityDireCrafting)world.getTileEntity(new BlockPos(x, y, z)));
        return null;
    }
}
