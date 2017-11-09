package morph.avaritia.container;

import morph.avaritia.container.slot.OutputSlot;
import morph.avaritia.tile.TileNeutronCollector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class ContainerNeutronCollector extends ContainerMachineBase<TileNeutronCollector> {

    public ContainerNeutronCollector(InventoryPlayer playerInventory, TileNeutronCollector machine) {
        super(machine);

        addSlotToContainer(new OutputSlot(machine, 2, 80, 35));

        bindPlayerInventory(playerInventory);
    }

    @Override
    protected Point getPlayerInvOffset() {
        return new Point(8, 84);
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(slotNumber);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotNumber == 0) {
                if (!mergeItemStack(itemstack1, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else {
                if (slotNumber >= 1 && slotNumber < 28) {
                    if (!mergeItemStack(itemstack1, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotNumber >= 28 && slotNumber < 37 && !mergeItemStack(itemstack1, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
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
