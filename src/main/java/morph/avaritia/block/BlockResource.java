package morph.avaritia.block;

import morph.avaritia.Avaritia;
import morph.avaritia.api.registration.IModelRegister;
import morph.avaritia.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by covers1624 on 11/04/2017.
 */
public class BlockResource extends Block implements IModelRegister {

    public static final PropertyEnum<BlockResource.Type> VARIANT = PropertyEnum.create("type", BlockResource.Type.class);

    public BlockResource() {
        super(Material.IRON);
        setUnlocalizedName("avaritia:block_resource");
        setRegistryName("block_resource");
        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 3);
        setCreativeTab(Avaritia.tab);
        setHardness(50);
        setResistance(2000);
        setDefaultState(blockState.getBaseState().withProperty(VARIANT, Type.NEUTRONIUM));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    @SideOnly (Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
        //TODO
        for (int i = 0; i < Type.METADATA_LOOKUP.length; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, Type.byMetadata(meta));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }

    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
        return true;
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return false;
    }

    @Override
    @SideOnly (Side.CLIENT)
    public void registerModels() {
        ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(ModBlocks.resource), stack -> new ModelResourceLocation("avaritia:block_resource", "type=" + BlockResource.Type.byMetadata(stack.getMetadata()).getName()));
    }

    public static enum Type implements IStringSerializable {

        NEUTRONIUM(0, "neutronium"),
        INFINITY(1, "infinity"),
        CRYSTAL_MATRIX(2, "crystal_matrix");

        private static final BlockResource.Type[] METADATA_LOOKUP = new BlockResource.Type[values().length];
        private final int metadata;
        private final String name;

        Type(int metadata, String name) {

            this.metadata = metadata;
            this.name = name;
        }

        public int getMetadata() {

            return metadata;
        }

        @Override
        public String getName() {

            return name;
        }

        public static Type byMetadata(int metadata) {

            if (metadata < 0 || metadata >= METADATA_LOOKUP.length) {
                metadata = 0;
            }
            return METADATA_LOOKUP[metadata];
        }

        static {
            for (Type type : values()) {
                METADATA_LOOKUP[type.getMetadata()] = type;
            }
        }
    }

}
