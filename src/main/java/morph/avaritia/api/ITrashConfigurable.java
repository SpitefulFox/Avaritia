package morph.avaritia.api;

import net.minecraft.item.ItemStack;

/**
 * Implement on an item for AOE trash specific logic.
 */
public interface ITrashConfigurable {

    boolean isTrashStack(ItemStack suspect);

}
