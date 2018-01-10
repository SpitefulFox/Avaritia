package fox.spiteful.avaritia.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityAutoDireCrafting extends TileLudicrous implements IInventory, ISidedInventory{

    private ItemStack result;
    private ItemStack[] matrix = new ItemStack[81];
    private ItemStack[] storage = new ItemStack[81];

    private boolean recipeChanged = true;

    @Override
    public boolean canUpdate()
    {
        return false;
    }

    @Override
    public void readCustomNBT(NBTTagCompound tag)
    {
        this.result = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Result"));
        for(int x = 0;x < matrix.length;x++){
            matrix[x] = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Craft" + x));
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound tag)
    {
        if(result != null) {
            NBTTagCompound produce = new NBTTagCompound();
            result.writeToNBT(produce);
            tag.setTag("Result", produce);
        }
        else
            tag.removeTag("Result");

        for(int x = 0;x < matrix.length;x++){
            if(matrix[x] != null){
                NBTTagCompound craft = new NBTTagCompound();
                matrix[x].writeToNBT(craft);
                tag.setTag("Craft" + x, craft);
            }
            else
                tag.removeTag("Craft" + x);
        }
    }

    @Override
    public int getSizeInventory()
    {
        return 82;
    }

    @Override
    public ItemStack getStackInSlot(int slot){
        if(slot == 0)
            return result;
        else if(slot <= storage.length)
            return storage[slot - 1];
        else
            return null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int decrement){

        if(slot == 0){
            if(result != null){
                for(int x = 1;x <= matrix.length;x++){
                    decrStackSize(x, 1);
                }
                if(result.stackSize <= decrement) {
                    ItemStack craft = result;
                    result = null;
                    return craft;
                }
                ItemStack split = result.splitStack(decrement);
                if(result.stackSize <= 0)
                    result = null;
                return split;
            }
            else
                return null;
        }
        else if(slot <= matrix.length){
            if(matrix[slot - 1] != null){
                if(matrix[slot - 1].stackSize <= decrement){
                    ItemStack ingredient = matrix[slot - 1];
                    matrix[slot - 1] = null;
                    return ingredient;
                }
                ItemStack split = matrix[slot - 1].splitStack(decrement);
                if(matrix[slot - 1].stackSize <= 0)
                    matrix[slot - 1] = null;
                return split;
            }
        }
        return null;
    }

    @Override
    public void openInventory() {}
    @Override
    public void closeInventory() {}

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack){

        return false;
    }

    @Override
    public int getInventoryStackLimit(){
        return 64;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack){
        if(slot == 0){
            result = stack;
        }
        else if(slot <= matrix.length){
            matrix[slot - 1] = stack;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot){
        return null;
    }

    /**
     * Returns the name of the inventory
     */
    @Override
    public String getInventoryName()
    {
        return  "container.dire";
    }

    /**
     * Returns if the inventory is named
     */
    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    public int[] getAccessibleSlotsFromSide(int side){
        return new int[]{};
    }

    public boolean canInsertItem(int slot, ItemStack item, int side){
        return false;
    }

    public boolean canExtractItem(int slot, ItemStack item, int side){
        return false;
    }

    public class ExtremeCraftingMatrix extends InventoryCrafting
    {
        public ExtremeCraftingMatrix()
        {
            super(new Container()
            {
                @Override
                public boolean canInteractWith(final EntityPlayer entityPlayer)
                {
                    return false;
                }
            }, 9, 9);
        }

        @Override
        public ItemStack getStackInSlot(final int slot)
        {
            return matrix[slot];
        }

        @Override
        public void setInventorySlotContents(final int slot, final ItemStack itemStack)
        {
            recipeChanged = true;
            matrix[slot] = itemStack;
            markDirty();
        }

        @Override
        public ItemStack decrStackSize(int slot, final int howMuch)
        {
            if (matrix[slot] == null)
                return null;
            final ItemStack itemStack = matrix[slot].copy();
            if ((matrix[slot].stackSize -= howMuch) == 0)
                matrix[slot] = null;
            itemStack.stackSize = howMuch;
            markDirty();
            return itemStack;
        }
    }

}
