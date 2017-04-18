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

            if (this.stack.getItem().equals(other.stack.getItem()) && this.stack.getItemDamage() == other.stack.getItemDamage()) {

                if (this.stack.getTagCompound() == null && other.stack.getTagCompound() == null) {
                    return true;
                } else {
                    if (this.stack.getTagCompound() == null ^ other.stack.getTagCompound() == null) {
                        return false;
                    } else if (this.stack.getTagCompound().equals(other.stack.getTagCompound())) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h = this.stack.getItem().hashCode();
        if (this.stack.getTagCompound() != null) {
            h ^= this.stack.getTagCompound().hashCode();
        }
        return h ^ this.stack.getItemDamage();
    }

    @Override
    public String toString() {
        return this.stack.toString();
    }
}
