package morph.avaritia.container;

import morph.avaritia.init.ModBlocks;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.extreme.InventoryExtremeCraftResult;
import morph.avaritia.recipe.extreme.InventoryExtremeCrafting;
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
    public InventoryExtremeCraftResult craftResult;
    protected World worldObj;
    protected BlockPos pos;

    public ContainerExtremeCrafting(InventoryPlayer player, World world, BlockPos pos, TileDireCraftingTable table) {
        worldObj = world;
        this.pos = pos;
        craftMatrix = new InventoryExtremeCrafting(this, table);
        craftResult = new InventoryExtremeCraftResult(table);
        addSlotToContainer(new SlotExtremeCrafting(player.player, craftMatrix, craftResult, 0, 210, 80));
        int wy;
        int ex;

        for (wy = 0; wy < 9; ++wy) {
            for (ex = 0; ex < 9; ++ex) {
                addSlotToContainer(new Slot(craftMatrix, ex + wy * 9, 12 + ex * 18, 8 + wy * 18));
            }
        }

        for (wy = 0; wy < 3; ++wy) {
            for (ex = 0; ex < 9; ++ex) {
                addSlotToContainer(new Slot(player, ex + wy * 9 + 9, 39 + ex * 18, 174 + wy * 18));
            }
        }

        for (ex = 0; ex < 9; ++ex) {
            addSlotToContainer(new Slot(player, ex, 39 + ex * 18, 232));
        }

        onCraftMatrixChanged(craftMatrix);
    }

    @Override
    public void onCraftMatrixChanged(IInventory matrix) {
        craftResult.setInventorySlotContents(0, AvaritiaRecipeManager.getExtremeCraftingResult(craftMatrix, worldObj));
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return worldObj.getBlockState(pos) == ModBlocks.extremeCraftingTable.getDefaultState() && player.getDistanceSq(pos) <= 64.0D;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(slotNumber);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotNumber == 0) {
                if (!mergeItemStack(itemstack1, 82, 118, true)) {//83 start??
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (slotNumber >= 82 && slotNumber < 109) {
                if (!mergeItemStack(itemstack1, 109, 118, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotNumber >= 109 && slotNumber < 118) {
                if (!mergeItemStack(itemstack1, 82, 109, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(itemstack1, 82, 118, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

}
