package morph.avaritia.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Created by covers1624 on 16/04/2017.
 */
public class ItemBlockResource extends ItemBlock {

    public ItemBlockResource(Block block) {
        super(block);
        setHasSubtypes(true);
        setMaxDamage(0);
        setNoRepair();
        setRegistryName("block_resource");
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "tile.avaritia:block_resource." + BlockResource.Type.byMetadata(stack.getItemDamage()).getName() + ".name";
    }
}
