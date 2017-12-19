package morph.avaritia.container.slot;

import codechicken.lib.inventory.InventorySimple;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

/**
 * Created by covers1624 on 22/05/2017.
 */
public class FakeSlot extends OutputSlot {

    public FakeSlot(int xPosition, int yPosition) {
        super(new InventorySimple(1), 0, xPosition, yPosition);

    }

    @Override
    public ItemStack getStack() {
        return new ItemStack(Blocks.NETHER_BRICK);
    }

    @Override
    public void putStack(ItemStack stack) {
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return false;
    }

    //    @Override
    //    public boolean canBeHovered() {
    //        return getHasStack();
    //    }
}
