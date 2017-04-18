package morph.avaritia.tile;

import morph.avaritia.recipe.compressor.CompressorManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileNeutroniumCompressor extends TileLudicrous implements ISidedInventory {

    private ItemStack input;
    private ItemStack processing;
    private ItemStack output;
    private EnumFacing facing = EnumFacing.NORTH;
    private int progress = 0;
    private int target = 0;
    private String ingredient;

    private int packetCount;
    private boolean packet;

    private static final int[] top = new int[] { 0 };
    private static final int[] sides = new int[] { 1 };

    @Override
    public void update() {
        if (packetCount > 0) {
            packetCount--;
        }
        if (input != null && (processing == null || progress < target)) {
            if (CompressorManager.getOutput(input) != null && (output == null || CompressorManager.getOutput(input).isItemEqual(output))) {
                if (processing == null) {
                    processing = CompressorManager.getOutput(input);
                    target = CompressorManager.getCost(input);
                    ingredient = CompressorManager.getName(input);
                }
                if (CompressorManager.getOutput(input).isItemEqual(processing)) {
                    int needed = target - progress;
                    if (needed >= input.stackSize) {
                        progress += input.stackSize;
                        input = null;
                    } else {
                        progress = target;
                        input.stackSize -= needed;
                    }
                }
                markDirty();
                packet = true;
            }
        }
        if (progress >= target && processing != null && (output == null || (output.isItemEqual(processing) && output.stackSize + processing.stackSize <= output.getMaxStackSize()))) {
            if (output == null) {
                output = processing.copy();
            } else if (output.isItemEqual(processing)) {
                output.stackSize += processing.stackSize;
            }

            progress -= target;
            if (progress == 0) {
                processing = null;
                ingredient = null;
            }
            markDirty();
            packet = true;
        }
        if (packet && packetCount <= 0) {
            markDirty();
            packetCount = 10;
            packet = false;
        }
    }

    public EnumFacing getFacing() {
        return facing;
    }

    public void setFacing(EnumFacing dir) {
        facing = dir;
    }

    public int getProgress() {
        return progress;
    }

    public int getTarget() {
        return target;
    }

    public String getIngredient() {
        return ingredient;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        input = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Input"));
        processing = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Processing"));
        output = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Output"));
        if (processing != null) {
            target = CompressorManager.getPrice(processing);
            if (target != 0) {
                progress = tag.getInteger("Progress");
                if (tag.hasKey("Ingredient")) {
                    ingredient = tag.getString("Ingredient");
                }
            } else {
                processing = null;
            }
        } else {
            progress = 0;
            target = 0;
            ingredient = null;
        }
        facing = EnumFacing.VALUES[tag.getByte("facing")];
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setByte("facing", (byte) facing.ordinal());
        if (input != null) {
            NBTTagCompound produce = new NBTTagCompound();
            input.writeToNBT(produce);
            tag.setTag("Input", produce);
        } else {
            tag.removeTag("Input");
        }
        if (processing != null) {
            NBTTagCompound produce = new NBTTagCompound();
            processing.writeToNBT(produce);
            tag.setTag("Processing", produce);
            tag.setInteger("Progress", progress);
            if (ingredient != null) {
                tag.setString("Ingredient", ingredient);
            } else {
                tag.removeTag("Ingredient");
            }
        } else {
            tag.removeTag("Processing");
            tag.removeTag("Progress");
            tag.removeTag("Target");
            tag.removeTag("Ingredient");
        }
        if (output != null) {
            NBTTagCompound produce = new NBTTagCompound();
            output.writeToNBT(produce);
            tag.setTag("Output", produce);
        } else {
            tag.removeTag("Output");
        }
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
            if (processing == null) {
                return true;
            }
            if (CompressorManager.getOutput(stack) == null) {
                return false;
            }
            if (CompressorManager.getOutput(stack).isItemEqual(processing)) {
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
