package morph.avaritia.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Created by covers1624 on 23/04/2017.
 */
public class ModHelper {

    public static final boolean isTinkersLoaded = Loader.isModLoaded("tconstruct");
    private static Item tinkersCleaver;

    public static boolean isHoldingCleaver(EntityLivingBase entity) {
        if (!isTinkersLoaded) {
            return false;
        }
        if (tinkersCleaver == null) {
            tinkersCleaver = ForgeRegistries.ITEMS.getValue(new ResourceLocation("tconstruct", "cleaver"));
        }
        return isPlayerHolding(entity, item -> Objects.equals(item, tinkersCleaver));
    }

    //TODO, move to ccl -covers
    public static boolean isPlayerHolding(EntityLivingBase entity, Predicate<Item> predicate) {
        for (EnumHand hand : EnumHand.values()) {
            ItemStack stack = entity.getHeldItem(hand);
            if (stack != null) {
                if (predicate.test(stack.getItem())) {
                    return true;
                }
            }
        }
        return false;
    }

}
