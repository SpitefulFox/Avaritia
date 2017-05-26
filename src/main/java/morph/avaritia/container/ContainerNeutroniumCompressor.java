package morph.avaritia.container;

import morph.avaritia.container.slot.OutputSlot;
import morph.avaritia.container.slot.ScrollingFakeSlot;
import morph.avaritia.container.slot.StaticFakeSlot;
import morph.avaritia.recipe.compressor.CompressorManager;
import morph.avaritia.tile.TileNeutroniumCompressor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class ContainerNeutroniumCompressor extends ContainerMachineBase<TileNeutroniumCompressor> {

    public OutputSlot outputSlot;

    public ContainerNeutroniumCompressor(InventoryPlayer playerInventory, TileNeutroniumCompressor machine) {
        super(machine);
        this.addSlotToContainer(new Slot(machine, 0, 39, 35));
        this.addSlotToContainer(outputSlot = new OutputSlot(machine, 1, 117, 35));
        bindPlayerInventory(playerInventory);
        this.addSlotToContainer(new StaticFakeSlot(147, 35, machineTile::getTargetStack));
        this.addSlotToContainer(new ScrollingFakeSlot(13, 35, machineTile::getInputItems));
    }

    @Override
    protected Point getPlayerInvOffset() {
        return new Point(8, 84);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
        ItemStack itemstack = null;
        Slot slot = this.inventorySlots.get(slotNumber);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotNumber == 1) {
                if (!this.mergeItemStack(itemstack1, 2, 38, true)) {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (slotNumber != 0) {
                if (CompressorManager.getOutput(itemstack1) != null) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return null;
                    }
                } else if (slotNumber >= 2 && slotNumber < 29) {
                    if (!this.mergeItemStack(itemstack1, 29, 38, false)) {
                        return null;
                    }
                } else if (slotNumber >= 29 && slotNumber < 38 && !this.mergeItemStack(itemstack1, 2, 29, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 2, 38, false)) {
                return null;
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
