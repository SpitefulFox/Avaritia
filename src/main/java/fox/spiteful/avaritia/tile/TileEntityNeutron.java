package fox.spiteful.avaritia.tile;

import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityNeutron extends TileLudicrous implements IInventory {

    private ItemStack neutrons;
    private int facing = 2;
    private int progress;

    @Override
    public void updateEntity(){
        if(++progress >= 7111){
        //if(++progress >= 300){
            if(neutrons == null)
                neutrons = new ItemStack(LudicrousItems.resource, 1, 2);
            else if(neutrons.getItem() == LudicrousItems.resource && neutrons.getItemDamage() == 2 && neutrons.stackSize < 64)
                neutrons.stackSize++;
            progress = 0;
            markDirty();
        }
    }

    public int getFacing(){
        return facing;
    }

    public void setFacing(int dir){
        facing = dir;
    }

    @Override
    public void readCustomNBT(NBTTagCompound tag)
    {
        this.neutrons = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Neutrons"));
        this.progress = tag.getInteger("Progress");
        this.facing = tag.getShort("Facing");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound tag)
    {
        tag.setInteger("Progress", this.progress);
        tag.setShort("Facing", (short) this.facing);
        if(neutrons != null) {
            NBTTagCompound produce = new NBTTagCompound();
            neutrons.writeToNBT(produce);
            tag.setTag("Neutrons", produce);
        }
        else
            tag.removeTag("Neutrons");
    }

    @Override
    public int getSizeInventory()
    {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot){
        return neutrons;
    }

    @Override
    public ItemStack decrStackSize(int slot, int decrement){
        if(neutrons == null)
            return null;
        else {
            if(decrement < neutrons.stackSize){
                ItemStack take = neutrons.splitStack(decrement);
                if(neutrons.stackSize <= 0)
                    neutrons = null;
                return take;
            }
            else {
                ItemStack take = neutrons;
                neutrons = null;
                return take;
            }
        }
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
        neutrons = stack;
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
        return  "container.neutron";
    }

    /**
     * Returns if the inventory is named
     */
    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

}
