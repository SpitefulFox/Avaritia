package fox.spiteful.avaritia.compat.botania;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.model.Models;
import vazkii.botania.api.subtile.signature.SubTileSignature;

public class Signature extends SubTileSignature {
    String name;
    Models icon;
    public Signature(String nombre){
        name = nombre;
    }
    @Override
    public void registerIcons(IIconRegister reg){
        icon = reg.registerIcon("avaritia:" + name);
    }
    @Override
    public Models getIconForStack(ItemStack item){
        return icon;
    }
    @Override
    public String getUnlocalizedNameForStack(ItemStack item){
        return "avaritia.flower." + name;
    }
    @Override
    public String getUnlocalizedLoreTextForStack(ItemStack item){
        return "tile.avaritia.flower." + name + ".lore";
    }
}