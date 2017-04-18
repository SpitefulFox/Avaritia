package morph.avaritia.container;

import morph.avaritia.tile.TileNeutronCollector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.ItemStack;

public class ContainerNeutronCollector extends Container {

    private TileNeutronCollector tileNeutron;

    public ContainerNeutronCollector(InventoryPlayer player, TileNeutronCollector machine) {
        this.tileNeutron = machine;
        this.addSlotToContainer(new SlotFurnaceFuel(machine, 2, 80, 35));
        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(player, i, 8 + i * 18, 142));
        }
    }

    public boolean canInteractWith(EntityPlayer player) {
        return this.tileNeutron.isUseableByPlayer(player);
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
        ItemStack itemstack = null;
        Slot slot = this.inventorySlots.get(slotNumber);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotNumber == 0) {
                if (!this.mergeItemStack(itemstack1, 1, 37, true)) {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else {
                if (slotNumber >= 1 && slotNumber < 28) {
                    if (!this.mergeItemStack(itemstack1, 28, 37, false)) {
                        return null;
                    }
                } else if (slotNumber >= 28 && slotNumber < 37 && !this.mergeItemStack(itemstack1, 1, 28, false)) {
                    return null;
                }
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
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
