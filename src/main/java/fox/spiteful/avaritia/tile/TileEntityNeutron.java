package fox.spiteful.avaritia.tile;

import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityNeutron extends TileLudicrous implements IInventory, ITickable {

    private ItemStack neutrons;
    private int facing = 2;
    private int progress;

    @Override
    public void update(){
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
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        this.neutrons = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Neutrons"));
        this.progress = tag.getInteger("Progress");
        this.facing = tag.getShort("Facing");
    }

    @Override
        public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag.setInteger("Progress", this.progress);
        tag.setShort("Facing", (short) this.facing);
        if(neutrons != null) {
            NBTTagCompound produce = new NBTTagCompound();
            neutrons.writeToNBT(produce);
            tag.setTag("Neutrons", produce);
        }
        else {
            tag.removeTag("Neutrons");
        }
        return super.writeToNBT(tag);
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
    public void openInventory(EntityPlayer player) {}
    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY(),getPos().getZ())) == this && player.getDistanceSq((double)getPos().getX() + 0.5D, (double)getPos().getY() + 0.5D, (double)getPos().getZ() + 0.5D) <= 64.0D;
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

    /**
     * Returns the name of the inventory
     */
    @Override
    public String getName()
    {
        return  "container.neutron";
    }

    /**
     * Returns if the inventory is named
     */
    @Override
    public boolean hasCustomName()
    {
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
        return 0;
    }

    @Override
    public void clear() {
    }

}
