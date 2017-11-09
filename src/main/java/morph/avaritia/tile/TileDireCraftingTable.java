package morph.avaritia.tile;

import codechicken.lib.util.ArrayUtils;
import codechicken.lib.util.BlockUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class TileDireCraftingTable extends TileBase implements IInventory, ISidedInventory {

    private ItemStack result = ItemStack.EMPTY;
    private ItemStack[] matrix = new ItemStack[81];

    public TileDireCraftingTable() {
        ArrayUtils.fillArray(matrix, ItemStack.EMPTY);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        result = new ItemStack(tag.getCompoundTag("Result"));
        for (int x = 0; x < matrix.length; x++) {
            if (tag.hasKey("Craft" + x)) {
                matrix[x] = new ItemStack(tag.getCompoundTag("Craft" + x));
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if (!result.isEmpty()) {
            NBTTagCompound produce = new NBTTagCompound();
            result.writeToNBT(produce);
            tag.setTag("Result", produce);
        } else {
            tag.removeTag("Result");
        }

        for (int x = 0; x < matrix.length; x++) {
            if (!matrix[x].isEmpty()) {
                NBTTagCompound craft = new NBTTagCompound();
                matrix[x].writeToNBT(craft);
                tag.setTag("Craft" + x, craft);
            } else {
                tag.removeTag("Craft" + x);
            }
        }
        return super.writeToNBT(tag);
    }

    @Override
    public int getSizeInventory() {
        return 82;
    }

    @Override
    public boolean isEmpty() {
        return ArrayUtils.count(matrix, stack -> !stack.isEmpty()) <= 0 && result.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot == 0) {
            return result;
        } else if (slot <= matrix.length) {
            return matrix[slot - 1];
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack decrStackSize(int slot, int decrement) {

        if (slot == 0) {
            if (!result.isEmpty()) {
                for (int x = 1; x <= matrix.length; x++) {
                    decrStackSize(x, 1);
                }
                if (result.getCount() <= decrement) {
                    ItemStack craft = result;
                    result = ItemStack.EMPTY;
                    return craft;
                }
                ItemStack split = result.splitStack(decrement);
                if (result.getCount() <= 0) {
                    result = ItemStack.EMPTY;
                }
                return split;
            } else {
                return ItemStack.EMPTY;
            }
        } else if (slot <= matrix.length) {
            if (matrix[slot - 1] != ItemStack.EMPTY) {
                if (matrix[slot - 1].getCount() <= decrement) {
                    ItemStack ingredient = matrix[slot - 1];
                    matrix[slot - 1] = ItemStack.EMPTY;
                    return ingredient;
                }
                ItemStack split = matrix[slot - 1].splitStack(decrement);
                if (matrix[slot - 1].getCount() <= 0) {
                    matrix[slot - 1] = ItemStack.EMPTY;
                }
                return split;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        if (slot == 0) {
            if (!result.isEmpty()) {
                for (int x = 1; x <= matrix.length; x++) {
                    decrStackSize(x, 1);
                }

                ItemStack craft = result;
                result = ItemStack.EMPTY;
                return craft;

            } else {
                return ItemStack.EMPTY;
            }
        } else if (slot <= matrix.length) {
            if (!matrix[slot - 1].isEmpty()) {
                ItemStack ingredient = matrix[slot - 1];
                matrix[slot - 1] = ItemStack.EMPTY;
                return ingredient;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return world.getTileEntity(pos) == this && BlockUtils.isEntityInRange(pos, player, 64);
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
        if (slot == 0) {
            result = stack;
        } else if (slot <= matrix.length) {
            matrix[slot - 1] = stack;
        }
    }

    @Override
    public String getName() {
        return "container.dire";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        if (hasCustomName()) {
            return new TextComponentString(getName());
        }
        return new TextComponentTranslation(getName());
    }

    @Override
    public int[] getSlotsForFace(EnumFacing face) {
        return new int[] {};
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, EnumFacing face) {
        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, EnumFacing face) {
        return false;
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void clear() {
        result = ItemStack.EMPTY;
        for (int x = 0; x < matrix.length; x++) {
            matrix[x] = ItemStack.EMPTY;
        }
    }

}
