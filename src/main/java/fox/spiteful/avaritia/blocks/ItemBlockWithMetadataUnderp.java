package fox.spiteful.avaritia.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class ItemBlockWithMetadataUnderp extends ItemBlockWithMetadata {
	public ItemBlockWithMetadataUnderp(Block block) {
		super(block, block);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
    {
		int meta = stack.getItemDamage();
        return this.field_150939_a.getUnlocalizedName() + meta;
    }
}
