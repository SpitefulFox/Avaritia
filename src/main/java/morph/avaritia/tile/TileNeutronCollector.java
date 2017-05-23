package morph.avaritia.tile;

import codechicken.lib.packet.PacketCustom;
import codechicken.lib.util.BlockUtils;
import codechicken.lib.util.ItemUtils;
import morph.avaritia.init.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileNeutronCollector extends TileMachineBase implements IInventory {

    public static final int PRODUCTION_TICKS = 7111;//TODO config.

    private ItemStack neutrons;
    private int progress;

    @Override
    public void doWork() {
        if (++progress >= PRODUCTION_TICKS) {
            if (neutrons == null) {
                neutrons = ItemUtils.copyStack(ModItems.neutron_pile, 1);
            } else if (ItemUtils.areStacksSameType(neutrons, ModItems.neutron_pile)) {
                if (neutrons.stackSize < 64) {
                    neutrons.stackSize++;
                }
            }
            progress = 0;
            markDirty();
        }
    }

    @Override
    protected void onWorkStopped() {
        progress = 0;
    }

    @Override
    protected boolean canWork() {
        return neutrons == null || neutrons.stackSize < 64;
    }

    public int getProgress() {
        return progress;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("Neutrons")) {
            this.neutrons = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Neutrons"));
        }
        this.progress = tag.getInteger("Progress");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setInteger("Progress", this.progress);
        if (neutrons != null) {
            NBTTagCompound produce = new NBTTagCompound();
            neutrons.writeToNBT(produce);
            tag.setTag("Neutrons", produce);
        } else {
            tag.removeTag("Neutrons");
        }
        return super.writeToNBT(tag);
    }

    @Override
    public void writeGuiData(PacketCustom packet, boolean isFullSync) {
        packet.writeVarInt(progress);
    }

    @Override
    public void readGuiData(PacketCustom packet, boolean isFullSync) {
        progress = packet.readVarInt();
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return neutrons;
    }

    @Override
    public ItemStack decrStackSize(int slot, int decrement) {
        if (neutrons == null) {
            return null;
        } else {
            if (decrement < neutrons.stackSize) {
                ItemStack take = neutrons.splitStack(decrement);
                if (neutrons.stackSize <= 0) {
                    neutrons = null;
                }
                return take;
            } else {
                ItemStack take = neutrons;
                neutrons = null;
                return take;
            }
        }
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(getPos()) == this && BlockUtils.isEntityInRange(getPos(), player, 64);
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        neutrons = stack;
    }

    @Override
    public String getName() {
        return "container.neutron";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        return null;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 2;
    }

    @Override
    public void clear() {
    }

}
