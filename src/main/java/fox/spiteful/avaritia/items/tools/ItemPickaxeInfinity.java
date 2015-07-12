package fox.spiteful.avaritia.items.tools;

import fox.spiteful.avaritia.Lumberjack;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;

public class ItemPickaxeInfinity extends ItemPickaxe {

    public ItemPickaxeInfinity(){
        super(LudicrousItems.opTool);
        setUnlocalizedName("infinity_pickaxe");
        setTextureName("avaritia:infinity_pickaxe");
    }

    @Override
    public void setDamage(ItemStack stack, int damage){
        super.setDamage(stack, 0);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player) {
        Block block = player.worldObj.getBlock(x, y, z);
        Lumberjack.log(Level.INFO, block.getLocalizedName());
        if(block.getBlockHardness(player.worldObj, x, y, z) <= -1.0F || block == Blocks.bedrock) {
            ItemStack drop = new ItemStack(block, 1, player.worldObj.getBlockMetadata(x, y, z));
            float f = 0.7F;
            double d0 = (double)(player.worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(player.worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(player.worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(player.worldObj, (double)x + d0, (double)y + d1, (double)z + d2, drop);
            entityitem.delayBeforeCanPickup = 10;
            player.worldObj.spawnEntityInWorld(entityitem);
            player.worldObj.setBlockToAir(x, y, z);
            return true;
        }
        return false;
    }

    @Override
    public float func_150893_a(ItemStack p_150893_1_, Block p_150893_2_) {
        return (p_150893_2_ == Blocks.bedrock) ? Float.MAX_VALUE : super.func_150893_a(p_150893_1_, p_150893_2_);
    }
    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta) {
        return (block == Blocks.bedrock) ? Float.MAX_VALUE : super.getDigSpeed(stack, block, meta);
    }
    @Override
    public boolean func_150897_b(Block p_150897_1_) {
        return (p_150897_1_ == Blocks.bedrock) || super.func_150897_b(p_150897_1_);
    }

}
