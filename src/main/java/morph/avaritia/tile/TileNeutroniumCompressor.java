package morph.avaritia.tile;

import codechicken.lib.packet.PacketCustom;
import codechicken.lib.util.ItemUtils;
import morph.avaritia.recipe.AvaritiaRecipeManager;
import morph.avaritia.recipe.compressor.ICompressorRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TileNeutroniumCompressor extends TileMachineBase implements ISidedInventory {

    //Number of ticks needed to consume an item.
    public static int CONSUME_TICKS = 1;//TODO Config.

    //The consumption progress.
    private int consumption_progress;
    //The production progress.
    private int compression_progress;
    //What we are creating.
    private ItemStack target_stack = ItemStack.EMPTY;
    private int compression_target;

    private ItemStack input = ItemStack.EMPTY;
    private ItemStack output = ItemStack.EMPTY;

    private List<ItemStack> c_InputItems;

    private static final int[] top = new int[] { 0 };
    private static final int[] sides = new int[] { 1 };

    @Override
    public void doWork() {

        boolean dirty = false;

        if (target_stack.isEmpty()) {
            fullContainerSync = true;
            ICompressorRecipe recipe = AvaritiaRecipeManager.getCompressorRecipeFromInput(input);
            target_stack = recipe.getResult();
            compression_target = recipe.getCost();
        }

        consumption_progress++;
        if (consumption_progress == CONSUME_TICKS) {
            consumption_progress = 0;

            input.shrink(1);
            if (input.getCount() == 0) {
                input = ItemStack.EMPTY;
            }

            compression_progress++;
            dirty = true;
        }

        if (compression_progress >= compression_target) {
            compression_progress = 0;
            if (output.isEmpty()) {
                output = ItemUtils.copyStack(target_stack, 1);
            } else {
                output.grow(1);
            }
            dirty = true;
            target_stack = ItemStack.EMPTY;
            fullContainerSync = true;
        }

        if (dirty) {
            markDirty();
        }
    }

    @Override
    protected void onWorkStopped() {
        consumption_progress = 0;
    }

    @Override
    protected boolean canWork() {
        if (input.isEmpty()) {
            return false;
        }
        if(!target_stack.isEmpty()) {
            ICompressorRecipe recipe = AvaritiaRecipeManager.getCompressorRecipeFromResult(target_stack);
            return recipe.matches(input);
        }
        ICompressorRecipe recipe = AvaritiaRecipeManager.getCompressorRecipeFromInput(input);
        return recipe != null && (output.isEmpty() || (recipe.getResult().isItemEqual(output) && output.getCount() < Math.min(output.getMaxStackSize(), getInventoryStackLimit())));
    }

    @Override
    public void writeGuiData(PacketCustom packet, boolean isFullSync) {
        packet.writeVarInt(consumption_progress);
        packet.writeVarInt(compression_progress);

        if (isFullSync) {
            packet.writeBoolean(!target_stack.isEmpty());
            if (!target_stack.isEmpty()) {
                packet.writeVarInt(compression_target);
                packet.writeItemStack(target_stack);
            }

            List<Ingredient> ings = Collections.emptyList();
            if (!target_stack.isEmpty()) {
                ings = AvaritiaRecipeManager.getCompressorRecipeFromResult(target_stack).getIngredients();
            }
            List<ItemStack> inputs = ings.stream().flatMap(l -> Arrays.stream(l.getMatchingStacks())).collect(Collectors.toList());

            packet.writeInt(inputs.size());
            for (ItemStack input : inputs) {
                packet.writeItemStack(input);
            }
        }
    }

    @Override
    public void readGuiData(PacketCustom packet, boolean isFullSync) {
        consumption_progress = packet.readVarInt();
        compression_progress = packet.readVarInt();
        if (isFullSync) {
            if (packet.readBoolean()) {
                compression_target = packet.readVarInt();
                target_stack = packet.readItemStack();
            } else {
                target_stack = ItemStack.EMPTY;
                compression_target = 0;
            }

            List<ItemStack> inputs = new LinkedList<>();
            int items = packet.readInt();
            for (int i = 0; i < items; i++) {
                inputs.add(packet.readItemStack());
            }

            c_InputItems = inputs;
        }
    }

    public int getCompressionProgress() {
        return compression_progress;
    }

    public int getCompressionTarget() {
        return compression_target;
    }

    public int getConsumptionProgress() {
        return consumption_progress;
    }

    public int getConsumptionTarget() {
        return CONSUME_TICKS;
    }

    public ItemStack getTargetStack() {
        return target_stack;
    }

    public List<ItemStack> getInputItems() {
        return c_InputItems;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        input = new ItemStack(tag.getCompoundTag("input"));
        output = new ItemStack(tag.getCompoundTag("output"));

        consumption_progress = tag.getInteger("consumption_progress");
        compression_progress = tag.getInteger("compression_progress");

        target_stack = new ItemStack(tag.getCompoundTag("target"));
        //Calc compression target.
        if (!target_stack.isEmpty()) {
            compression_target = AvaritiaRecipeManager.getCompressorRecipeFromResult(target_stack).getCost();
        }
        fullContainerSync = true;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if (!input.isEmpty()) {
            NBTTagCompound inputTag = new NBTTagCompound();
            input.writeToNBT(inputTag);
            tag.setTag("input", inputTag);
        }
        if (!output.isEmpty()) {
            NBTTagCompound outputTag = new NBTTagCompound();
            output.writeToNBT(outputTag);
            tag.setTag("output", outputTag);
        }
        if (!target_stack.isEmpty()) {
            NBTTagCompound targetTag = new NBTTagCompound();
            target_stack.writeToNBT(targetTag);
            tag.setTag("target", targetTag);
        }
        tag.setInteger("consumption_progress", consumption_progress);
        tag.setInteger("compression_progress", compression_progress);
        return super.writeToNBT(tag);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side != null) {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new SidedInvWrapper(this, side));
            } else {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new InvWrapper(this));
            }
        }
        return super.getCapability(capability, side);
    }

    @Override
    public int getSizeInventory() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return input.isEmpty() && output.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot == 0) {
            return input;
        } else {
            return output;
        }
    }

    @Override
    public ItemStack decrStackSize(int slot, int decrement) {
        if (slot == 0) {
            if (input.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                if (decrement < input.getCount()) {
                    ItemStack take = input.splitStack(decrement);
                    if (input.getCount() <= 0) {
                        input = ItemStack.EMPTY;
                    }
                    return take;
                } else {
                    ItemStack take = input;
                    input = ItemStack.EMPTY;
                    return take;
                }
            }
        } else if (slot == 1) {
            if (output.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                if (decrement < output.getCount()) {
                    ItemStack take = output.splitStack(decrement);
                    if (output.getCount() <= 0) {
                        output = ItemStack.EMPTY;
                    }
                    return take;
                } else {
                    ItemStack take = output;
                    output = ItemStack.EMPTY;
                    return take;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return world.getTileEntity(getPos()) == this && player.getDistanceSq(getPos().getX() + 0.5D, getPos().getY() + 0.5D, getPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (slot == 0) {
            if (target_stack.isEmpty()) {
                return true;
            }
            if (AvaritiaRecipeManager.hasCompressorRecipe(stack)) {
                if (AvaritiaRecipeManager.getCompressorRecipeFromInput(stack).getResult().isItemEqual(target_stack)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (slot == 0) {
            input = stack;
        } else if (slot == 1) {
            output = stack;
        }
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if (side == EnumFacing.UP) {
            return top;
        } else {
            return sides;
        }
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side) {
        return isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
        if (slot == 1 && side != EnumFacing.UP) {
            return true;
        }
        return false;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStack.EMPTY;
    }

    //@formatter:off
    @Override public int getInventoryStackLimit() { return 64; }
    @Override public String getName() { return "container.neutronium_compressor"; }
    @Override public boolean hasCustomName() { return false; }
    @Override public void openInventory(EntityPlayer player) { }
    @Override public void closeInventory(EntityPlayer player) { }
    @Override public int getField(int id) { return 0; }
    @Override public void setField(int id, int value) { }
    @Override public int getFieldCount() { return 0; }
    @Override public void clear() { }
    //@formatter:on
}
