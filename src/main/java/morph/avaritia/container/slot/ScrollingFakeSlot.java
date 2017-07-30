package morph.avaritia.container.slot;

import morph.avaritia.Avaritia;
import morph.avaritia.util.TimeTracker;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created by covers1624 on 22/05/2017.
 */
public class ScrollingFakeSlot extends FakeSlot {

    private final Supplier<List<ItemStack>> stacksSupplier;

    private TimeTracker tracker = new TimeTracker();
    private int index = 0;

    public ScrollingFakeSlot(int xPosition, int yPosition, Supplier<List<ItemStack>> stacksSupplier) {
        super(xPosition, yPosition);
        this.stacksSupplier = stacksSupplier;
    }

    @Override
    public ItemStack getStack() {
        if (Avaritia.proxy.isClient() && stacksSupplier.get() != null && stacksSupplier.get().size() > 0) {
            World world = Avaritia.proxy.getClientWorld();
            List<ItemStack> stacks = stacksSupplier.get();
            if (tracker.hasDelayPassed(world, 25)) {
                index++;
                if (index > stacks.size() - 1) {
                    index = 0;
                }
                tracker.markTime(world);
            }
            return stacks.get(index);
        }
        return ItemStack.EMPTY;
    }
}
