package fox.spiteful.avaritia.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import fox.spiteful.avaritia.tile.TileEntityCompressor;
import fox.spiteful.avaritia.tile.TileEntityDireCrafting;
import fox.spiteful.avaritia.tile.TileEntityNeutron;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GooeyHandler implements IGuiHandler {

    @Override
    public Object getClientGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z){
        if(ID == 0)
            return new GuiCrafting(player.inventory, world, x, y, z);
        else if(ID == 1)
            return new GUIExtremeCrafting(player.inventory, world, x, y, z, (TileEntityDireCrafting)world.getTileEntity(x, y, z));
        else if(ID == 2)
            return new GUINeutron(player.inventory, (TileEntityNeutron)world.getTileEntity(x, y, z));
        else if(ID == 3)
            return new GUICompressor(player.inventory, (TileEntityCompressor)world.getTileEntity(x, y, z));
        return null;
    }

    @Override
    public Object getServerGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z){
        if(ID == 0)
            return new ContainerCustomWorkbench(player.inventory, world, x, y, z);
        else if(ID == 1)
            return new ContainerExtremeCrafting(player.inventory, world, x, y, z, (TileEntityDireCrafting)world.getTileEntity(x, y, z));
        else if(ID == 2)
            return new ContainerNeutron(player.inventory, (TileEntityNeutron)world.getTileEntity(x, y, z));
        else if(ID == 3)
            return new ContainerCompressor(player.inventory, (TileEntityCompressor)world.getTileEntity(x, y, z));
        return null;
    }
}
