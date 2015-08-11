package fox.spiteful.avaritia.tile;

import fox.spiteful.avaritia.crafting.CompressorManager;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityCompressor extends TileLudicrous implements ISidedInventory {

    private ItemStack input, processing, output;
    private int facing = 2;
    private int progress = 0;
    private int target = 0;
    private String ingredient;

    @Override
    public void updateEntity(){
        if(input != null){
            if(CompressorManager.getOutput(input) != null && (output == null || CompressorManager.getOutput(input).isItemEqual(output))) {
                if (processing == null) {
                    processing = CompressorManager.getOutput(input);
                    target = CompressorManager.getCost(input);
                    ingredient = CompressorManager.getName(input);
                }
                if (CompressorManager.getOutput(input).isItemEqual(processing)) {
                    progress += input.stackSize;
                    input = null;
                }
                markDirty();
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
        }
        if (progress >= target && processing != null && (output == null || output.isItemEqual(processing))) {
            if(output == null)
                output = processing.copy();
            else if(output.isItemEqual(processing))
                output.stackSize++;

            progress -= target;
            if(progress == 0) {
                processing = null;
                progress = 0;
                ingredient = null;
            }
            markDirty();
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    public int getFacing(){
        return facing;
    }

    public void setFacing(int dir){
        facing = dir;
    }

    public int getProgress(){
        return progress;
    }

    public int getTarget(){
        return target;
    }

    public String getIngredient(){
        return ingredient;
    }

    @Override
    public void readCustomNBT(NBTTagCompound tag)
    {
        this.input = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Input"));
        this.processing = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Processing"));
        this.output = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Output"));
        this.progress = tag.getInteger("Progress");
        this.target = tag.getInteger("Target");
        if(tag.hasKey("Ingredient"))
            this.ingredient = tag.getString("Ingredient");
        this.facing = tag.getShort("Facing");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound tag)
    {
        tag.setInteger("Progress", this.progress);
        tag.setInteger("Target", this.target);
        if(ingredient != null)
            tag.setString("Ingredient", this.ingredient);
        else
            tag.removeTag("Ingredient");
        tag.setShort("Facing", (short) this.facing);
        if(input != null) {
            NBTTagCompound produce = new NBTTagCompound();
            input.writeToNBT(produce);
            tag.setTag("Input", produce);
        }
        else
            tag.removeTag("Input");
        if(processing != null) {
            NBTTagCompound produce = new NBTTagCompound();
            processing.writeToNBT(produce);
            tag.setTag("Processing", produce);
        }
        else
            tag.removeTag("Processing");
        if(output != null) {
            NBTTagCompound produce = new NBTTagCompound();
            output.writeToNBT(produce);
            tag.setTag("Output", produce);
        }
        else
            tag.removeTag("Output");
    }

    @Override
    public int getSizeInventory()
    {
        return 3;
    }

    @Override
    public ItemStack getStackInSlot(int slot){
        if(slot == 0)
            return input;
        else if(slot == 2)
            return processing;
        else
            return output;
    }

    @Override
    public ItemStack decrStackSize(int slot, int decrement){
        if(slot == 0) {
            if (input == null)
                return null;
            else {
                if (decrement < input.stackSize) {
                    ItemStack take = input.splitStack(decrement);
                    if (input.stackSize <= 0)
                        input = null;
                    return take;
                } else {
                    ItemStack take = input;
                    input = null;
                    return take;
                }
            }
        }
        else if (slot == 1){
            if (output == null)
                return null;
            else {
                if (decrement < output.stackSize) {
                    ItemStack take = output.splitStack(decrement);
                    if (output.stackSize <= 0)
                        output = null;
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
        if(slot == 0){
            if(processing == null)
                return true;
            if(CompressorManager.getOutput(stack).isItemEqual(processing))
                return true;
        }
        return false;
    }

    @Override
    public int getInventoryStackLimit(){
        return 64;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack){
        if(slot == 0)
            input = stack;
        else if(slot == 1)
            output = stack;
        else
            processing = stack;
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
        return  "container.neutronium_compressor";
    }

    /**
     * Returns if the inventory is named
     */
    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side){
        if(side == 0)
            return new int[]{0};
        else
            return new int[]{1};
    }

    public boolean canInsertItem(int slot, ItemStack stack, int side){
        if(slot == 0 && side == 0){
            if(processing == null)
                return true;
            if(CompressorManager.getOutput(stack).isItemEqual(processing))
                return true;
        }
        return false;
    }

    public boolean canExtractItem(int slot, ItemStack stack, int side){
        if(slot == 1 && side != 0)
            return true;
        return false;
    }

}
