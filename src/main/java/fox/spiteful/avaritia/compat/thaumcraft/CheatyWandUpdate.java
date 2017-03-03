package fox.spiteful.avaritia.compat.thaumcraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.IWandRodOnUpdate;
import thaumcraft.common.items.wands.ItemWandCasting;

public class CheatyWandUpdate implements IWandRodOnUpdate {
    Aspect primals[] = Aspect.getPrimalAspects().toArray(new Aspect[0]);
    public void onUpdate(ItemStack itemstack, EntityPlayer player) {
        for (int x = 0; x < primals.length; x++) {
            if (((ItemWandCasting) itemstack.getItem()).getVis(itemstack, primals[x]) < ((ItemWandCasting) itemstack.getItem()).getMaxVis(itemstack)) {
                ((ItemWandCasting) itemstack.getItem()).addVis(itemstack, primals[x], ((ItemWandCasting) itemstack.getItem()).getMaxVis(itemstack) - ((ItemWandCasting) itemstack.getItem()).getVis(itemstack, primals[x]), true);
            }
        }
    }
}