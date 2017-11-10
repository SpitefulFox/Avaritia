package fox.spiteful.avaritia.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockResource extends ItemBlock {

    public ItemBlockResource(Block block) {

        super(block);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int meta) {

        return meta;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {

        return "tile.block_" + BlockResource.types[itemStack.getItemDamage() % BlockResource.types.length];
    }
}