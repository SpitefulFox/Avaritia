package morph.avaritia.container;

import morph.avaritia.container.slot.OutputSlot;
import morph.avaritia.tile.TileNeutronCollector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class ContainerNeutronCollector extends ContainerMachineBase<TileNeutronCollector> {

    public ContainerNeutronCollector(InventoryPlayer playerInventory, TileNeutronCollector machine) {
        super(machine);

        this.addSlotToContainer(new OutputSlot(machine, 2, 80, 35));

        bindPlayerInventory(playerInventory);
    }

    @Override
    protected Point getPlayerInvOffset() {
        return new Point(8, 84);
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
