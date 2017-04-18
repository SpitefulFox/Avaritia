package morph.avaritia.container;

import morph.avaritia.recipe.extreme.InventoryDireCraftResult;
import morph.avaritia.recipe.extreme.InventoryDireCrafting;
import morph.avaritia.init.ModBlocks;
import morph.avaritia.recipe.extreme.ExtremeCraftingManager;
import morph.avaritia.tile.TileDireCraftingTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerExtremeCrafting extends Container {

    /**
     * The crafting matrix inventory (9x9).
     */
    public InventoryCrafting craftMatrix;
    public IInventory craftResult;
    protected World worldObj;
    protected BlockPos pos;

    public ContainerExtremeCrafting(InventoryPlayer player, World world, BlockPos pos, TileDireCraftingTable table) {
        this.worldObj = world;
        this.pos = pos;
        craftMatrix = new InventoryDireCrafting(this, table);
        craftResult = new InventoryDireCraftResult(table);
        this.addSlotToContainer(new SlotExtremeCrafting(player.player, this.craftMatrix, this.craftResult, 0, 210, 80));
        int wy;
        int ex;

        for (wy = 0; wy < 9; ++wy) {
            for (ex = 0; ex < 9; ++ex) {
                this.addSlotToContainer(new Slot(this.craftMatrix, ex + wy * 9, 12 + ex * 18, 8 + wy * 18));
            }
        }

        for (wy = 0; wy < 3; ++wy) {
            for (ex = 0; ex < 9; ++ex) {
                this.addSlotToContainer(new Slot(player, ex + wy * 9 + 9, 39 + ex * 18, 174 + wy * 18));
            }
        }

        for (ex = 0; ex < 9; ++ex) {
            this.addSlotToContainer(new Slot(player, ex, 39 + ex * 18, 232));
        }

        this.onCraftMatrixChanged(this.craftMatrix);
    }

    @Override
    public void onCraftMatrixChanged(IInventory matrix) {
        this.craftResult.setInventorySlotContents(0, ExtremeCraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj));
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.worldObj.getBlockState(pos) == ModBlocks.dire_craft.getDefaultState() && player.getDistanceSq(pos) <= 64.0D;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotNumber);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotNumber == 0) {
                if (!this.mergeItemStack(itemstack1, 82, 118, true)) {//83 start??
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (slotNumber >= 82 && slotNumber < 109) {
                if (!this.mergeItemStack(itemstack1, 109, 118, false)) {
                    return null;
                }
            } else if (slotNumber >= 109 && slotNumber < 118) {
                if (!this.mergeItemStack(itemstack1, 82, 109, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 82, 118, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }

}
