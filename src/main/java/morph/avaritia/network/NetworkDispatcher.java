package morph.avaritia.network;

import codechicken.lib.packet.PacketCustom;
import morph.avaritia.tile.TileMachineBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by covers1624 on 20/05/2017.
 */
public class NetworkDispatcher {

    public static final String NET_CHANNEL = "avaritia";

    public static void dispatchMachineUpdate(TileMachineBase machineTile) {
        PacketCustom packet = new PacketCustom(NET_CHANNEL, 1);
        packet.writePos(machineTile.getPos());
        machineTile.writeUpdateData(packet);
        packet.sendToChunk(machineTile);
    }

    public static void dispatchGuiChanges(EntityPlayer player, TileMachineBase machineTile, boolean isFullSync) {
        PacketCustom packet = new PacketCustom(NET_CHANNEL, 2);
        packet.writePos(machineTile.getPos());
        packet.writeBoolean(isFullSync);
        machineTile.writeGuiData(packet, isFullSync);
        packet.sendToPlayer(player);
    }

}
