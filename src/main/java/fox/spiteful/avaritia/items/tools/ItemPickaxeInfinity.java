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

}
