package morph.avaritia.recipe.extreme;

import morph.avaritia.tile.TileDireCraftingTable;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class InventoryExtremeCrafting extends InventoryCrafting {

    private TileDireCraftingTable craft;
    private Container container;

    public InventoryExtremeCrafting(Container cont, TileDireCraftingTable table) {
        super(cont, 9, 9);
        craft = table;
        container = cont;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot >= getSizeInventory() ? ItemStack.EMPTY : craft.getStackInSlot(slot + 1);
    }

    @Override
    public ItemStack getStackInRowAndColumn(int row, int column) {
        if (row >= 0 && row < 9) {
            int x = row + column * 9;
            return getStackInSlot(x);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack decrStackSize(int slot, int decrement) {
        ItemStack stack = craft.getStackInSlot(slot + 1);
        if (!stack.isEmpty()) {
            ItemStack itemstack;
            if (stack.getCount() <= decrement) {
                itemstack = stack.copy();
                craft.setInventorySlotContents(slot + 1, ItemStack.EMPTY);
                container.onCraftMatrixChanged(this);
                return itemstack;
            } else {
                itemstack = stack.splitStack(decrement);
                if (stack.getCount() == 0) {
                    craft.setInventorySlotContents(slot + 1, ItemStack.EMPTY);
                }
                container.onCraftMatrixChanged(this);
                return itemstack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        craft.setInventorySlotContents(slot + 1, itemstack);
        container.onCraftMatrixChanged(this);
    }

}
