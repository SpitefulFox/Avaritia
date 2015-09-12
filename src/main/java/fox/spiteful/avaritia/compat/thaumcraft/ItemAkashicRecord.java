package fox.spiteful.avaritia.compat.thaumcraft;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.compat.Compat;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketAspectPool;

import java.util.List;

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

        if (!player.capabilities.isCreativeMode)
        {
            --stack.stackSize;
        }
        return stack;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        super.getSubItems(item, tab, list);
        try {
            Item wand = Compat.getItem("Thaumcraft", "WandCasting");
            ItemStack cosmic = new ItemStack(wand, 1, 9000);
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("cap", "matrix");
            tag.setString("rod", "infinity");
            cosmic.setTagCompound(tag);
            list.add(cosmic);
        }
        catch (Throwable e){}
    }

}
