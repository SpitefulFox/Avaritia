package morph.avaritia.container.slot;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Created by covers1624 on 22/05/2017.
 */
public class StaticFakeSlot extends FakeSlot {

    private final Supplier<ItemStack> stackSupplier;

    public StaticFakeSlot(int xPosition, int yPosition, Supplier<ItemStack> stackSupplier) {
        super(xPosition, yPosition);
        this.stackSupplier = stackSupplier;
    }

    @Nullable
    @Override
    public ItemStack getStack() {
        return stackSupplier.get();
    }
}
