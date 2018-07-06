package morph.avaritia.compat.jei.extreme;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import morph.avaritia.Avaritia;
import morph.avaritia.compat.jei.AvaritiaJEIPlugin;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Collections;
import java.util.List;

import static morph.avaritia.compat.jei.AvaritiaJEIPlugin.EXTREME_CRAFTING;

/**
 * Created by brandon3055 on 17/02/2017.
 */
public class ExtremeCraftingCategory implements IRecipeCategory<ExtremeRecipeWrapper> {

    private static final int craftOutputSlot = 0;
    private static final int craftInputSlot1 = 1;

    private final String localizedName;
    private final ICraftingGridHelper gridHelper;

    public ExtremeCraftingCategory(IGuiHelper guiHelper) {
        localizedName = I18n.format("crafting.extreme");
        gridHelper = guiHelper.createCraftingGridHelper(craftInputSlot1, craftOutputSlot);
    }

    @Override
    public String getUid() {
        return EXTREME_CRAFTING;
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public String getModName() {
        return Avaritia.MOD_NAME;
    }

    @Override
    public IDrawable getBackground() {
        return AvaritiaJEIPlugin.extreme_crafting;
    }

    @Override
    public void setRecipe(IRecipeLayout layout, ExtremeRecipeWrapper wrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = layout.getItemStacks();

        guiItemStacks.init(craftOutputSlot, false, 167, 73);

        for (int y = 0; y < 9; ++y) {
            for (int x = 0; x < 9; ++x) {
                int index = craftInputSlot1 + x + (y * 9);
                guiItemStacks.init(index, true, (x * 18) + 1, (y * 18) + 1);
            }
        }
        List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
        if (wrapper.recipe.isShapedRecipe()) {
            try {
                int width = wrapper.recipe.getWidth();
                int height = wrapper.recipe.getHeight();
                if (width != 9) {
                    List<List<ItemStack>> newInputs = NonNullList.withSize(9 * height, Collections.emptyList());
                    for (int i = 0; i < height; i++) {
                        for (int j = 0; j < 9; j++) {
                            int index = i + j * 9;
                            int oldidx = i + j * width;
                            if (j < width) {
                                newInputs.set(index, inputs.get(oldidx));
                            }
                        }
                    }
                    ingredients.setInputLists(ItemStack.class, newInputs);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            layout.setShapeless();
        }
        guiItemStacks.set(ingredients);
    }
}
