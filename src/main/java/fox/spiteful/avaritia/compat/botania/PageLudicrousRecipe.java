package fox.spiteful.avaritia.compat.botania;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.crafting.ExtremeShapedOreRecipe;
import fox.spiteful.avaritia.crafting.ExtremeShapedRecipe;
import fox.spiteful.avaritia.crafting.ExtremeShapelessRecipe;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.lexicon.page.PageRecipe;

public class PageLudicrousRecipe extends PageRecipe {

	private static final ResourceLocation craftingOverlay = new ResourceLocation("avaritia", "textures/gui/lexiconCraftingOverlay.png");
	
	IRecipe recipe;
	int ticksElapsed = 0;
	
	boolean oreDictRecipe, shapelessRecipe;
	
	public PageLudicrousRecipe(String unlocalizedName, IRecipe recipe) {
		super(unlocalizedName);
		this.recipe = recipe;
	}

	@Override
	public void onPageAdded(LexiconEntry entry, int index) {
		LexiconRecipeMappings.map(this.recipe.getRecipeOutput(), entry, index);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void renderRecipe(IGuiLexiconEntry gui, int mx, int my) {
		oreDictRecipe = shapelessRecipe = false;

		renderCraftingRecipe(gui, recipe);

		TextureManager render = Minecraft.getMinecraft().renderEngine;
		render.bindTexture(craftingOverlay);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		((GuiScreen) gui).drawTexturedModalRect(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());

		int iconX = gui.getLeft() + 115;
		int iconY = gui.getTop() + 12;

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		if(shapelessRecipe) {
			((GuiScreen) gui).drawTexturedModalRect(iconX, iconY, 240, 0, 16, 16);

			if(mx >= iconX && my >= iconY && mx < iconX + 16 && my < iconY + 16)
				RenderHelper.renderTooltip(mx, my, Arrays.asList(StatCollector.translateToLocal("botaniamisc.shapeless")));

			iconY += 20;
		}

		render.bindTexture(craftingOverlay);
		GL11.glEnable(GL11.GL_BLEND);

		if(oreDictRecipe) {
			((GuiScreen) gui).drawTexturedModalRect(iconX, iconY, 240, 16, 16, 16);

			if(mx >= iconX && my >= iconY && mx < iconX + 16 && my < iconY + 16)
				RenderHelper.renderTooltip(mx, my, Arrays.asList(StatCollector.translateToLocal("botaniamisc.oredict")));
		}
		GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateScreen() {
		/*if(ticksElapsed % 20 == 0) {
			recipeAt++;

			if(recipeAt == recipes.size())
				recipeAt = 0;
		}*/
		++ticksElapsed;
	}

	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	public void renderCraftingRecipe(IGuiLexiconEntry gui, IRecipe recipe) {
		if (recipe instanceof ExtremeShapedRecipe) {
			ExtremeShapedRecipe shaped = (ExtremeShapedRecipe) recipe;
			
			for(int y = 0; y < shaped.recipeHeight; y++)
				for(int x = 0; x < shaped.recipeWidth; x++)
					renderItemAtLudicrousGridPos(gui, x, y, shaped.recipeItems[y * shaped.recipeWidth + x], true);
		} else if (recipe instanceof ExtremeShapedOreRecipe) {
			oreDictRecipe = true;
		} else if (recipe instanceof ExtremeShapelessRecipe) {
			shapelessRecipe = true;
		} else if(recipe instanceof ShapelessOreRecipe) {
			ShapelessOreRecipe shapeless = (ShapelessOreRecipe) recipe;

			drawGrid : {
				for(int y = 0; y < 3; y++)
					for(int x = 0; x < 3; x++) {
						int index = y * 3 + x;

						if(index >= shapeless.getRecipeSize())
							break drawGrid;

						Object input = shapeless.getInput().get(index);
						if(input != null)
							renderItemAtLudicrousGridPos(gui, x, y, input instanceof ItemStack ? (ItemStack) input : ((ArrayList<ItemStack>) input).get(0), true);
					}
			}

			shapelessRecipe = true;
			oreDictRecipe = true;
		}
		/*if(recipe instanceof ShapedRecipes) {
			ShapedRecipes shaped = (ShapedRecipes)recipe;

			for(int y = 0; y < shaped.recipeHeight; y++)
				for(int x = 0; x < shaped.recipeWidth; x++)
					renderItemAtGridPos(gui, 1 + x, 1 + y, shaped.recipeItems[y * shaped.recipeWidth + x], true);
		} else if(recipe instanceof ShapedOreRecipe) {
			ShapedOreRecipe shaped = (ShapedOreRecipe) recipe;
			int width = (Integer) ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, shaped, 4);
			int height = (Integer) ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, shaped, 5);

			for(int y = 0; y < height; y++)
				for(int x = 0; x < width; x++) {
					Object input = shaped.getInput()[y * width + x];
					if(input != null)
						renderItemAtGridPos(gui, 1 + x, 1 + y, input instanceof ItemStack ? (ItemStack) input : ((ArrayList<ItemStack>) input).get(0), true);
				}

			oreDictRecipe = true;
		} else if(recipe instanceof ShapelessRecipes) {
			ShapelessRecipes shapeless = (ShapelessRecipes) recipe;

			drawGrid : {
				for(int y = 0; y < 3; y++)
					for(int x = 0; x < 3; x++) {
						int index = y * 3 + x;

						if(index >= shapeless.recipeItems.size())
							break drawGrid;

						renderItemAtGridPos(gui, 1 + x, 1 + y, (ItemStack) shapeless.recipeItems.get(index), true);
					}
			}

			shapelessRecipe = true;
		} else if(recipe instanceof ShapelessOreRecipe) {
			ShapelessOreRecipe shapeless = (ShapelessOreRecipe) recipe;

			drawGrid : {
				for(int y = 0; y < 3; y++)
					for(int x = 0; x < 3; x++) {
						int index = y * 3 + x;

						if(index >= shapeless.getRecipeSize())
							break drawGrid;

						Object input = shapeless.getInput().get(index);
						if(input != null)
							renderItemAtGridPos(gui, 1 + x, 1 + y, input instanceof ItemStack ? (ItemStack) input : ((ArrayList<ItemStack>) input).get(0), true);
					}
			}

			shapelessRecipe = true;
			oreDictRecipe = true;
		}*/

		renderItemAtLudicrousGridPos(gui, 4, -1, recipe.getRecipeOutput(), false);
	}

	@SideOnly(Side.CLIENT)
	public void renderItemAtLudicrousGridPos(IGuiLexiconEntry gui, int x, int y, ItemStack stack, boolean accountForContainer) {
		if(stack == null || stack.getItem() == null)
			return;
		stack = stack.copy();

		if(stack.getItemDamage() == Short.MAX_VALUE)
			stack.setItemDamage(0);

		int xPos = gui.getLeft() + x * 13 + 13;
		int yPos = gui.getTop() + y * 13 + 30 - (y==-1 ? 5 : 0);
		ItemStack stack1 = stack.copy();
		if(stack1.getItemDamage() == -1)
			stack1.setItemDamage(0);

		renderItem(gui, xPos, yPos, stack1, accountForContainer);
	}
}
