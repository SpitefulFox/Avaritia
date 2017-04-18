package morph.avaritia.item;

import codechicken.lib.model.ModelRegistryHelper;
import codechicken.lib.util.TransformUtils;
import morph.avaritia.Avaritia;
import morph.avaritia.api.IHaloRenderItem;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.client.render.item.HaloRenderItem;
import morph.avaritia.entity.EntityEndestPearl;
import morph.avaritia.init.AvaritiaTextures;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEndestPearl extends ItemEnderPearl implements IHaloRenderItem, IModelRegister {

    public ItemEndestPearl() {
        this.setUnlocalizedName("avaritia:endest_pearl");
        this.setRegistryName("endest_pearl");
        this.maxStackSize = 16;
        this.setCreativeTab(Avaritia.tab);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (!player.capabilities.isCreativeMode) {
            --stack.stackSize;
        }

        //world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote) {
            world.spawnEntityInWorld(new EntityEndestPearl(world, player));
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    @SideOnly (Side.CLIENT)
    public boolean shouldDrawHalo(ItemStack stack) {
        return true;
    }

    @Override
    @SideOnly (Side.CLIENT)
    public TextureAtlasSprite getHaloTexture(ItemStack stack) {
        return AvaritiaTextures.HALO;
    }

    @Override
    @SideOnly (Side.CLIENT)
    public int getHaloSize(ItemStack stack) {
        return 4;
    }

    @Override
    @SideOnly (Side.CLIENT)
    public boolean shouldDrawPulse(ItemStack stack) {
        return true;
    }

    @Override
    @SideOnly (Side.CLIENT)
    public int getHaloColour(ItemStack stack) {
        return 0xFF000000;
    }

    @Override
    @SideOnly (Side.CLIENT)
    public void registerModels() {
        ModelResourceLocation pearl = new ModelResourceLocation("avaritia:resource", "type=endest_pearl");
        ModelLoader.registerItemVariants(this, pearl);
        IBakedModel wrapped = new HaloRenderItem(TransformUtils.DEFAULT_TOOL, modelRegistry -> modelRegistry.getObject(pearl));
        ModelRegistryHelper.register(pearl, wrapped);
        ModelLoader.setCustomMeshDefinition(this, (ItemStack stack) -> pearl);
    }
}
