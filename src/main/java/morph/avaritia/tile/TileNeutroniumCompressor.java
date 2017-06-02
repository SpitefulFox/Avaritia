package morph.avaritia.tile;

import codechicken.lib.packet.PacketCustom;
import codechicken.lib.util.ItemUtils;
import morph.avaritia.recipe.compressor.CompressorManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedList;
import java.util.List;

public class TileNeutroniumCompressor extends TileMachineBase implements ISidedInventory {

    //Number of ticks needed to consume an item.
    public static int CONSUME_TICKS = 1;//TODO Config.

    //The consumption progress.
    private int consumption_progress;
    //The production progress.
    private int compression_progress;
    //What we are creating.
    private ItemStack target_stack;
    private int compression_target;

    private ItemStack input;
    private ItemStack output;

    private List<ItemStack> c_InputItems;

    private static final int[] top = new int[] { 0 };
    private static final int[] sides = new int[] { 1 };

    @Override
    public void doWork() {

        boolean dirty = false;

        if (target_stack == null) {
            fullContainerSync = true;
            target_stack = CompressorManager.getOutput(input);
            compression_target = CompressorManager.getPrice(target_stack);
        }

        consumption_progress++;
        if (consumption_progress == CONSUME_TICKS) {
            consumption_progress = 0;

            input.stackSize--;
            if (input.stackSize == 0) {
                input = null;
            }

            compression_progress++;
            dirty = true;
        }

        if (compression_progress >= compression_target) {
            compression_progress = 0;
            if (output == null) {
                output = ItemUtils.copyStack(target_stack, 1);
            } else {
                output.stackSize++;
            }
            dirty = true;
            target_stack = null;
            fullContainerSync = true;
        }

        if (dirty) {
            markDirty();
        }
    }

    @Override
    protected void onWorkStopped() {
        consumption_progress = 0;
    }

    @Override
    protected boolean canWork() {
        return CompressorManager.isValidInputForOutput(input, target_stack) && (output == null || output.stackSize < Math.min(output.getMaxStackSize(), getInventoryStackLimit()));
    }

    @Override
    public void writeGuiData(PacketCustom packet, boolean isFullSync) {
        packet.writeVarInt(consumption_progress);
        packet.writeVarInt(compression_progress);

        if (isFullSync) {
            packet.writeBoolean(target_stack != null);
            if (target_stack != null) {
                packet.writeVarInt(compression_target);
                packet.writeItemStack(target_stack);
            }

            List<ItemStack> inputs = CompressorManager.getInputs(target_stack);

            packet.writeInt(inputs.size());
            for (ItemStack input : inputs) {
                packet.writeItemStack(input);
            }
        }
    }

    @Override
    public void readGuiData(PacketCustom packet, boolean isFullSync) {
        consumption_progress = packet.readVarInt();
        compression_progress = packet.readVarInt();
        if (isFullSync) {
            if (packet.readBoolean()) {
                compression_target = packet.readVarInt();
                target_stack = packet.readItemStack();
            } else {
                target_stack = null;
                compression_target = 0;
            }

            List<ItemStack> inputs = new LinkedList<>();
            int items = packet.readInt();
            for (int i = 0; i < items; i++) {
                inputs.add(packet.readItemStack());
            }

            c_InputItems = inputs;
        }
    }

    public int getCompressionProgress() {
        return compression_progress;
    }

    public int getCompressionTarget() {
        return compression_target;
    }

    public int getConsumptionProgress() {
        return consumption_progress;
    }

    public int getConsumptionTarget() {
        return CONSUME_TICKS;
    }

    public ItemStack getTargetStack() {
        return target_stack;
    }

    @SideOnly(Side.CLIENT)
    public List<ItemStack> getInputItems() {
        return c_InputItems;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        input = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("input"));
        output = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("output"));

        consumption_progress = tag.getInteger("consumption_progress");
        compression_progress = tag.getInteger("compression_progress");

        target_stack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("target"));
        //Calc compression target.
        compression_target = CompressorManager.getPrice(target_stack);
        fullContainerSync = true;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if (input != null) {
            NBTTagCompound inputTag = new NBTTagCompound();
            input.writeToNBT(inputTag);
            tag.setTag("input", inputTag);
        }
        if (output != null) {
            NBTTagCompound outputTag = new NBTTagCompound();
            output.writeToNBT(outputTag);
            tag.setTag("output", outputTag);
        }
        if (target_stack != null) {
            NBTTagCompound targetTag = new NBTTagCompound();
            target_stack.writeToNBT(targetTag);
            tag.setTag("target", targetTag);
        }
        tag.setInteger("consumption_progress", consumption_progress);
        tag.setInteger("compression_progress", compression_progress);
        return super.writeToNBT(tag);
    }

    @Override
    public int getSizeInventory() {
        return 2;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot == 0) {
            return input;
        } else {
            return output;
        }
    }

    @Override
    public ItemStack decrStackSize(int slot, int decrement) {
        if (slot == 0) {
            if (input == null) {
                return null;
            } else {
                if (decrement < input.stackSize) {
                    ItemStack take = input.splitStack(decrement);
                    if (input.stackSize <= 0) {
                        input = null;
                    }
                    return take;
                } else {
                    ItemStack take = input;
                    input = null;
                    return take;
                }
            }
        } else if (slot == 1) {
            if (output == null) {
                return null;
            } else {
                if (decrement < output.stackSize) {
                    ItemStack take = output.splitStack(decrement);
                    if (output.stackSize <= 0) {
                        output = null;
                    }
                    return take;
                } else {
                    ItemStack take = output;
                    output = null;
                    return take;
                }
            }
        }
        return null;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(getPos()) == this && player.getDistanceSq(getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (slot == 0) {
            if (target_stack == null) {
                return true;
            }
            if (CompressorManager.getOutput(stack) == null) {
                return false;
            }
            if (CompressorManager.getOutput(stack).isItemEqual(target_stack)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (slot == 0) {
            input = stack;
        } else if (slot == 1) {
            output = stack;
        }
    }

    /**
     * Returns the name of the inventory
     */
    @Override
    public String getName() {
        return "container.neutronium_compressor";
    }

    /**
     * Returns if the inventory is named
     */
    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if (side == EnumFacing.UP) {
            return top;
        } else {
            return sides;
        }
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
        return isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
        if (slot == 1 && side != EnumFacing.UP) {
            return true;
        }
        return false;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
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
        return 0;
    }

    @Override
    public void clear() {
    }

}
