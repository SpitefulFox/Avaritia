package morph.avaritia.item;

import codechicken.lib.item.ItemMultiType;
import codechicken.lib.model.ModelRegistryHelper;
import codechicken.lib.util.TransformUtils;
import com.google.common.collect.Sets;
import morph.avaritia.api.IHaloRenderItem;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.client.render.item.HaloRenderItem;
import morph.avaritia.entity.EntityImmortalItem;
import morph.avaritia.init.AvaritiaTextures;
import morph.avaritia.init.ModItems;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Set;

/**
 * Created by covers1624 on 11/04/2017.
 */
public class ItemResource extends ItemMultiType implements IHaloRenderItem, IModelRegister {

    public ItemResource(CreativeTabs tab, String registryName) {
        super(tab, registryName);
    }

    @Override
    public void addInformation(ItemStack item, EntityPlayer player, List<String> tooltip, boolean wut) {
        int meta = item.getItemDamage();
        if (meta != 0) {
            tooltip.add(TextFormatting.DARK_GRAY + "" + TextFormatting.ITALIC + I18n.translateToLocal("tooltip." + getUnlocalizedName(item) + ".desc"));
        }
    }

    @Override
    @SideOnly (Side.CLIENT)
    public boolean shouldDrawHalo(ItemStack stack) {
        int meta = stack.getItemDamage();
        return (meta >= 2 && meta <= 6);
    }

    @Override
    @SideOnly (Side.CLIENT)
    public TextureAtlasSprite getHaloTexture(ItemStack stack) {
        int meta = stack.getItemDamage();
        if (meta == 2 || meta == 3 || meta == 4) {
            return AvaritiaTextures.HALO_NOISE;
        }
        return AvaritiaTextures.HALO;
    }

    @Override
    @SideOnly (Side.CLIENT)
    public int getHaloSize(ItemStack stack) {
        int meta = stack.getItemDamage();
        switch (meta) {
            case 5:
            case 6:
                return 10;
        }
        return 8;
    }

    @Override
    @SideOnly (Side.CLIENT)
    public boolean shouldDrawPulse(ItemStack stack) {
        int meta = stack.getItemDamage();
        return meta == 5 || meta == 6;
    }

    @Override
    @SideOnly (Side.CLIENT)
    public int getHaloColour(ItemStack stack) {
        int meta = stack.getItemDamage();
        if (meta == 2) {
            return 0x33FFFFFF;
        }
        if (meta == 3) {
            return 0x4DFFFFFF;
        }
        if (meta == 4) {
            return 0x99FFFFFF;
        }
        return 0xFF000000;
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        int meta = stack.getItemDamage();
        return meta == 5 || meta == 6;
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        int meta = itemstack.getItemDamage();
        return (meta == 5 || meta == 6) ? new EntityImmortalItem(world, location, itemstack) : null;
    }

    @Override
    @SideOnly (Side.CLIENT)
    public void registerModels() {
        ModItems.resource.registerModelVariants();
        Set<Integer> toRegister = Sets.newHashSet(2, 3, 4, 5, 6);

        for (int meta : toRegister) {
            String name = names.get(meta);
            final ModelResourceLocation location = new ModelResourceLocation(getRegistryName(), "type=" + name);
            IBakedModel wrapped = new HaloRenderItem(TransformUtils.DEFAULT_ITEM, modelRegistry -> modelRegistry.getObject(location));
            ModelRegistryHelper.register(location, wrapped);
        }

    }
}
