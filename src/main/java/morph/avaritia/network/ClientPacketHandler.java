package morph.avaritia.network;

import codechicken.lib.packet.ICustomPacketHandler.IClientPacketHandler;
import codechicken.lib.packet.PacketCustom;
import morph.avaritia.tile.TileMachineBase;
import morph.avaritia.util.Lumberjack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.Level;

/**
 * Created by covers1624 on 20/05/2017.
 */
public class ClientPacketHandler implements IClientPacketHandler {

    @Override
    public void handlePacket(PacketCustom packet, Minecraft mc, INetHandlerPlayClient handler) {
        switch (packet.getType()) {
            case 1: {
                BlockPos pos = packet.readPos();
                TileEntity tile = mc.world.getTileEntity(pos);
                if (tile instanceof TileMachineBase) {
                    ((TileMachineBase) tile).readUpdatePacket(packet);
                } else {
                    Lumberjack.log(Level.ERROR, "Received Machine update packet for tile that is not a Machine... Pos: " + pos.toString());
                }
                break;
            }
            case 2: {
                BlockPos pos = packet.readPos();
                TileEntity tile = mc.world.getTileEntity(pos);
                if (tile instanceof TileMachineBase) {
                    ((TileMachineBase) tile).readGuiData(packet, packet.readBoolean());
                } else {
                    Lumberjack.log(Level.ERROR, "Received Gui update packet for tile that is not a Machine... Pos: " + pos.toString());
                }
                break;
            }
        }
    }
}
