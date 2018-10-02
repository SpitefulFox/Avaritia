package morph.avaritia.tile;

import codechicken.lib.packet.PacketCustom;
import codechicken.lib.util.BlockUtils;
import codechicken.lib.util.CommonUtils;
import morph.avaritia.network.NetworkDispatcher;
import morph.avaritia.util.TimeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

/**
 * Created by covers1624 on 20/05/2017.
 */
public abstract class TileMachineBase extends TileBase implements ITickable {

    //The machine is currently doing stuff.
    protected boolean isActive;
    //The machine was doing stuff and a client update is pending.
    private boolean wasActive;
    //Tracks how long the machine has been off for, and then triggers an update after x time.
    protected TimeTracker offTracker = new TimeTracker();

    private boolean sendUpdatePacket;
    public boolean fullContainerSync;

    protected EnumFacing facing = EnumFacing.NORTH;

    @Override
    public final void update() {
        if (CommonUtils.isClientWorld(world)) {
            return;
        }
        if (canWork()) {
            if (!isActive && !wasActive) {
                sendUpdatePacket = true;
            }
            isActive = true;
            wasActive = false;
            doWork();
        } else {
            if (isActive) {
                onWorkStopped();
                wasActive = true;
                if (world != null) {
                    offTracker.markTime(world);
                }
            }
            isActive = false;
        }
        updateCheck();
    }

    /**
     * Does checks to see if a delay has passed since the machine was turned off for triggering client updates.
     */
    private void updateCheck() {
        if (wasActive && offTracker.hasDelayPassed(world, 100)) {
            wasActive = false;
            sendUpdatePacket = true;
        }
        if (sendUpdatePacket) {
            sendUpdatePacket = false;
            NetworkDispatcher.dispatchMachineUpdate(this);
        }
    }

    /**
     * Called on the server to write data to the update packet.
     *
     * @param packet The packet to write data to.
     */
    public void writeUpdateData(PacketCustom packet) {
        packet.writeBoolean(isActive);
        packet.writeByte(facing.ordinal());
    }

    /**
     * Called on the client to read the update data from the server.
     *
     * @param packet The packet to read data from.
     */
    public void readUpdatePacket(PacketCustom packet) {
        isActive = packet.readBoolean();
        facing = EnumFacing.VALUES[packet.readUByte()];
        BlockUtils.fireBlockUpdate(world, getPos());
    }

    public abstract void writeGuiData(PacketCustom packet, boolean isFullSync);

    public abstract void readGuiData(PacketCustom packet, boolean isFullSync);

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        facing = EnumFacing.VALUES[compound.getByte("facing")];
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setByte("facing", (byte) facing.ordinal());
        return super.writeToNBT(compound);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tag = super.getUpdateTag();
        tag.setBoolean("active", isActive);
        return tag;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        super.handleUpdateTag(tag);
        isActive = tag.getBoolean("active");
    }

    /**
     * Called Server side to check if work can happen.
     *
     * @return If work can happen.
     */
    protected abstract boolean canWork();

    /**
     * Called server side to do work.
     */
    protected abstract void doWork();

    protected abstract void onWorkStopped();

    public EnumFacing getFacing() {
        return facing;
    }

    public void setFacing(EnumFacing facing) {
        this.facing = facing;
    }

    public boolean isActive() {
        return isActive;
    }
}
