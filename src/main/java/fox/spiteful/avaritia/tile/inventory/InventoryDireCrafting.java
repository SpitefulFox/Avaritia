package fox.spiteful.avaritia.tile.inventory;

import fox.spiteful.avaritia.tile.TileEntityDireCrafting;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class InventoryDireCrafting extends InventoryCrafting {

    private TileEntityDireCrafting craft;
    private Container container;

    public InventoryDireCrafting(Container cont, TileEntityDireCrafting table){
        super(cont, 9, 9);
        craft = table;
        container = cont;
    }

    @Override
    public ItemStack getStackInSlot (int slot)
    {
        return slot >= this.getSizeInventory() ? null : craft.getStackInSlot(slot+1);
    }

    @Override
    public ItemStack getStackInRowAndColumn (int row, int column)
    {
        if (row >= 0 && row < 9)
        {
            int x = row + column * 9;
            return this.getStackInSlot(x);
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing (int par1)
    {
        return null;
    }

    @Override
    public ItemStack decrStackSize (int slot, int decrement)
    {
        ItemStack stack = craft.getStackInSlot(slot + 1);
        if (stack != null)
        {
            ItemStack itemstack;
            if (stack.stackSize <= decrement)
            {
                itemstack = stack.copy();
                stack = null;
                craft.setInventorySlotContents(slot + 1, null);
                this.container.onCraftMatrixChanged(this);
                return itemstack;
            }
            else
            {
                itemstack = stack.splitStack(decrement);
                if (stack.stackSize == 0)
                {
                    stack = null;
                }
                this.container.onCraftMatrixChanged(this);
                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents (int slot, ItemStack itemstack)
    {
        craft.setInventorySlotContents(slot + 1, itemstack);
        this.container.onCraftMatrixChanged(this);
    }

}
