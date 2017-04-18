package morph.avaritia.container;

import morph.avaritia.recipe.extreme.ExtremeCraftingManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.stats.AchievementList;

import javax.annotation.Nullable;

/**
 * Created by brandon3055 on 18/02/2017.
 */
public class SlotExtremeCrafting extends Slot {

    private final InventoryCrafting craftMatrix;
    private final EntityPlayer thePlayer;
    private int amountCrafted;

    public SlotExtremeCrafting(EntityPlayer player, InventoryCrafting craftingInventory, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
        this.thePlayer = player;
        this.craftMatrix = craftingInventory;
    }

    @Override
    public boolean isItemValid(@Nullable ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        if (this.getHasStack()) {
            this.amountCrafted += Math.min(amount, this.getStack().stackSize);
        }

        return super.decrStackSize(amount);
    }

    @Override
    protected void onCrafting(ItemStack stack, int amount) {
        this.amountCrafted += amount;
        this.onCrafting(stack);
    }

    @Override
    protected void onCrafting(ItemStack stack) {
        if (this.amountCrafted > 0) {
            stack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.amountCrafted);
        }

        this.amountCrafted = 0;

        if (stack.getItem() == Item.getItemFromBlock(Blocks.CRAFTING_TABLE)) {
            this.thePlayer.addStat(AchievementList.BUILD_WORK_BENCH);
        }

        if (stack.getItem() instanceof ItemPickaxe) {
            this.thePlayer.addStat(AchievementList.BUILD_PICKAXE);
        }

        if (stack.getItem() == Item.getItemFromBlock(Blocks.FURNACE)) {
            this.thePlayer.addStat(AchievementList.BUILD_FURNACE);
        }

        if (stack.getItem() instanceof ItemHoe) {
            this.thePlayer.addStat(AchievementList.BUILD_HOE);
        }

        if (stack.getItem() == Items.BREAD) {
            this.thePlayer.addStat(AchievementList.MAKE_BREAD);
        }

        if (stack.getItem() == Items.CAKE) {
            this.thePlayer.addStat(AchievementList.BAKE_CAKE);
        }

        if (stack.getItem() instanceof ItemPickaxe && ((ItemPickaxe) stack.getItem()).getToolMaterial() != Item.ToolMaterial.WOOD) {
            this.thePlayer.addStat(AchievementList.BUILD_BETTER_PICKAXE);
        }

        if (stack.getItem() instanceof ItemSword) {
            this.thePlayer.addStat(AchievementList.BUILD_SWORD);
        }

        if (stack.getItem() == Item.getItemFromBlock(Blocks.ENCHANTING_TABLE)) {
            this.thePlayer.addStat(AchievementList.ENCHANTMENTS);
        }

        if (stack.getItem() == Item.getItemFromBlock(Blocks.BOOKSHELF)) {
            this.thePlayer.addStat(AchievementList.BOOKCASE);
        }
    }

    @Override
    public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
        net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerCraftingEvent(playerIn, stack, craftMatrix);
        this.onCrafting(stack);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(playerIn);
        ItemStack[] aitemstack = ExtremeCraftingManager.getInstance().getRemainingItems(this.craftMatrix, playerIn.worldObj);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);

        for (int i = 0; i < aitemstack.length; ++i) {
            ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
            ItemStack itemstack1 = aitemstack[i];

            if (itemstack != null) {
                this.craftMatrix.decrStackSize(i, 1);
                itemstack = this.craftMatrix.getStackInSlot(i);
            }

            if (itemstack1 != null) {
                if (itemstack == null) {
                    this.craftMatrix.setInventorySlotContents(i, itemstack1);
                } else if (ItemStack.areItemsEqual(itemstack, itemstack1) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1)) {
                    itemstack1.stackSize += itemstack.stackSize;
                    this.craftMatrix.setInventorySlotContents(i, itemstack1);
                } else if (!this.thePlayer.inventory.addItemStackToInventory(itemstack1)) {
                    this.thePlayer.dropItem(itemstack1, false);
                }
            }
        }
    }
}
