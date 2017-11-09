package morph.avaritia.util;

import net.minecraft.item.ItemStack;

public class ItemStackWrapper {

    public final ItemStack stack;

    public ItemStackWrapper(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public boolean equals(Object otherobj) {
        if (otherobj instanceof ItemStackWrapper) {
            ItemStackWrapper other = (ItemStackWrapper) otherobj;

            if (stack.getItem().equals(other.stack.getItem()) && stack.getItemDamage() == other.stack.getItemDamage()) {

                if (stack.getTagCompound() == null && other.stack.getTagCompound() == null) {
                    return true;
                } else {
                    if (stack.getTagCompound() == null ^ other.stack.getTagCompound() == null) {
                        return false;
                    } else if (stack.getTagCompound().equals(other.stack.getTagCompound())) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = stack.getItem().hashCode();
        if (stack.getTagCompound() != null) {
            h ^= stack.getTagCompound().hashCode();
        }
        return h ^ stack.getItemDamage();
    }

    @Override
    public String toString() {
        return stack.toString();
    }
}
