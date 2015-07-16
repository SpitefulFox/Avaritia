package fox.spiteful.avaritia.compat.thaumcraft;

import fox.spiteful.avaritia.Avaritia;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketAspectPool;

public class ItemAkashicRecord extends Item {

    public ItemAkashicRecord(){
        setUnlocalizedName("akashic_record");
        setTextureName("avaritia:akashic_record");
        setCreativeTab(Avaritia.tab);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if(world.isRemote)
            return stack;

        for(Aspect aspect : Aspect.aspects.values()) {
            Thaumcraft.proxy.playerKnowledge.addAspectPool(player.getCommandSenderName(), aspect, (short) 999);
            PacketHandler.INSTANCE.sendTo(new PacketAspectPool(aspect.getTag(), (short)999, Short.valueOf(Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(player.getCommandSenderName(), aspect))), (EntityPlayerMP) player);
        }
        if(--stack.stackSize <= 0)
            return null;
        else
            return stack;
    }

}
