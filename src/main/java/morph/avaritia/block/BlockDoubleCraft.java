package morph.avaritia.block;

import morph.avaritia.Avaritia;
import morph.avaritia.api.registration.IModelRegister;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDoubleCraft extends Block implements IModelRegister {

    public BlockDoubleCraft() {
        super(Material.WOOD);
        setHardness(4.0F);
        setUnlocalizedName("avaritia:compressed_workbench");
        setSoundType(SoundType.WOOD);
        setCreativeTab(Avaritia.tab);
        setRegistryName("compressed_workbench");
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        } else {
            player.openGui(Avaritia.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
    }

    @Override
    @SideOnly (Side.CLIENT)
    public void registerModels() {
        ResourceLocation craft = new ResourceLocation("avaritia:crafting");
        ModelLoader.setCustomStateMapper(this, new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return new ModelResourceLocation(craft, "type=double");
            }
        });
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(craft, "type=double"));
    }
}
