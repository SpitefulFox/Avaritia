package morph.avaritia.recipe.extreme;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Basically IRecipe but not IRecipe.
 * //TODO, Some form of serialization and deserialization here, for easier recipe syncing.
 */
public interface IExtremeRecipe extends IForgeRegistryEntry<IExtremeRecipe> {

    boolean matches(InventoryCrafting inventory, World world);

    default ItemStack getCraftingResult(InventoryCrafting inventory) {
        return getRecipeOutput();
    }

    boolean canFit(int width, int height);

    ItemStack getRecipeOutput();

    default NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }

    default NonNullList<Ingredient> getIngredients() {
        return NonNullList.create();
    }
}
