package morph.avaritia.container;

import morph.avaritia.container.slot.OutputSlot;
import morph.avaritia.container.slot.ScrollingFakeSlot;
import morph.avaritia.container.slot.StaticFakeSlot;
import morph.avaritia.recipe.AvaritiaRecipeManager;
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
        addSlotToContainer(new Slot(machine, 0, 39, 35));
        addSlotToContainer(outputSlot = new OutputSlot(machine, 1, 117, 35));
        bindPlayerInventory(playerInventory);
        addSlotToContainer(new StaticFakeSlot(147, 35, machineTile::getTargetStack));
        addSlotToContainer(new ScrollingFakeSlot(13, 35, machineTile::getInputItems));
    }

    @Override
    protected Point getPlayerInvOffset() {
        return new Point(8, 84);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(slotNumber);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotNumber == 1) {
                if (!mergeItemStack(itemstack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (slotNumber != 0) {
                if (AvaritiaRecipeManager.getCompressorRecipeFromInput(itemstack1) != null) {
                    if (!mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotNumber >= 2 && slotNumber < 29) {
                    if (!mergeItemStack(itemstack1, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotNumber >= 29 && slotNumber < 38 && !mergeItemStack(itemstack1, 2, 29, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(itemstack1, 2, 38, false)) {
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
