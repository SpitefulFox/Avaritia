package morph.avaritia.recipe.extreme;

import morph.avaritia.tile.TileDireCraftingTable;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.ItemStack;

public class InventoryExtremeCraftResult extends InventoryCraftResult {

    private TileDireCraftingTable craft;

    public InventoryExtremeCraftResult(TileDireCraftingTable table) {
        craft = table;
    }

    @Override
    public ItemStack getStackInSlot(int par1) {
        return craft.getStackInSlot(0);
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2) {
        ItemStack stack = craft.getStackInSlot(0);
        if (!stack.isEmpty()) {
            craft.setInventorySlotContents(0, ItemStack.EMPTY);
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
        craft.setInventorySlotContents(0, par2ItemStack);
    }

}
