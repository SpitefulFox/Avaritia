package morph.avaritia.client.gui;

import morph.avaritia.container.ContainerCustomWorkbench;
import morph.avaritia.container.ContainerExtremeCrafting;
import morph.avaritia.container.ContainerNeutronCollector;
import morph.avaritia.container.ContainerNeutroniumCompressor;
import morph.avaritia.tile.TileDireCraftingTable;
import morph.avaritia.tile.TileNeutronCollector;
import morph.avaritia.tile.TileNeutroniumCompressor;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

//TODO, on Packet rewrite, switch to CCL Gui opening system.
public class GUIHandler implements IGuiHandler {

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tile = world.getTileEntity(pos);
        switch (ID) {
            case 0:
                return new GuiCrafting(player.inventory, world, pos);
            case 1:
                return new GUIExtremeCrafting(player.inventory, world, pos, ((TileDireCraftingTable) tile));
            case 2:
                return new GUINeutronCollector(player.inventory, ((TileNeutronCollector) tile));
            case 3:
                return new GUINeutroniumCompressor(player.inventory, ((TileNeutroniumCompressor) tile));
            default:
                return null;
        }
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tile = world.getTileEntity(pos);
        switch (ID) {
            case 0:
                return new ContainerCustomWorkbench(player.inventory, world, pos);
            case 1:
                return new ContainerExtremeCrafting(player.inventory, world, pos, (TileDireCraftingTable) tile);
            case 2:
                return new ContainerNeutronCollector(player.inventory, ((TileNeutronCollector) tile));
            case 3:
                return new ContainerNeutroniumCompressor(player.inventory, (TileNeutroniumCompressor) tile);
        }
        return null;
    }
}
