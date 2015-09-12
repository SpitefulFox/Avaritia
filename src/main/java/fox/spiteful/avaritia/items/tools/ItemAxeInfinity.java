/*
 *
 * Code blatantly jacked from Vazkii
 * Get the original here: https://github.com/Vazkii/Botania/blob/master/src/main/java/vazkii/botania/common/item/equipment/tool/terrasteel/ItemTerraAxe.java
 */

package fox.spiteful.avaritia.items.tools;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.Lumberjack;
import fox.spiteful.avaritia.entity.EntityImmortalItem;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemAxeInfinity extends ItemAxe {

    private static final ToolMaterial opAxe = EnumHelper.addToolMaterial("INFINITY_PICKAXE", 32, 9999, 9999F, 20.0F, 200);
    private static Map<Integer, List<BlockSwapper>> blockSwappers = new HashMap();

    public ItemAxeInfinity(){
        super(opAxe);
        setUnlocalizedName("infinity_axe");
        setTextureName("avaritia:infinity_axe");
        setCreativeTab(Avaritia.tab);
        FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public void setDamage(ItemStack stack, int damage){
        super.setDamage(stack, 0);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return LudicrousItems.cosmic;
    }

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta){
        if (ForgeHooks.isToolEffective(stack, block, meta) || block.getMaterial() == Material.leaves)
        {
            return efficiencyOnProperMaterial;
        }
        return Math.max(func_150893_a(stack, block), 6.0F);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if(player.isSneaking()) {
            player.swingItem();
            int fortune = EnchantmentHelper.getFortuneModifier(player);
            boolean silk = EnchantmentHelper.getSilkTouchModifier(player);

            int range = 13;

            ToolHelper.removeBlocksInIteration(player, stack, world, (int)player.posX, (int)player.posY, (int)player.posZ, -range, -3, -range, range, range * 2 - 3, range, null, ToolHelper.materialsAxe, silk, fortune, false);
        }
        return stack;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player) {
        MovingObjectPosition raycast = ToolHelper.raytraceFromEntity(player.worldObj, player, true, 10);
        if (raycast != null) {
            breakOtherBlock(player, stack, x, y, z, x, y, z, raycast.sideHit);
        }
        return false;
    }

    public void breakOtherBlock(EntityPlayer player, ItemStack stack, int x, int y, int z, int originX, int originY, int originZ, int side) {
        if(player.isSneaking())
            return;
        ChunkCoordinates coords = new ChunkCoordinates(x, y, z);
        addBlockSwapper(player.worldObj, player, stack, coords, coords, 32, false, true, new ArrayList());
    }

    @Override
    public boolean hasCustomEntity (ItemStack stack)
    {
        return true;
    }

    @Override
    public Entity createEntity (World world, Entity location, ItemStack itemstack)
    {
        return new EntityImmortalItem(world, location, itemstack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack, int pass)
    {
        return false;
    }

    @SubscribeEvent
    public void onTickEnd(TickEvent.WorldTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            int dim = event.world.provider.dimensionId;
            if(blockSwappers.containsKey(dim)) {
                List<BlockSwapper> swappers = blockSwappers.get(dim);
                List<BlockSwapper> swappersSafe = new ArrayList(swappers);
                swappers.clear();
                for(BlockSwapper s : swappersSafe)
                    if(s != null)
                        s.tick();
            }
        }
    }

    private static BlockSwapper addBlockSwapper(World world, EntityPlayer player, ItemStack stack, ChunkCoordinates origCoords, ChunkCoordinates coords, int steps, boolean leaves, boolean force, List<String> posChecked) {
        BlockSwapper swapper = new BlockSwapper(world, player, stack, origCoords, coords, steps, leaves, force, posChecked);
        int dim = world.provider.dimensionId;
        if(!blockSwappers.containsKey(dim))
            blockSwappers.put(dim, new ArrayList());
        blockSwappers.get(dim).add(swapper);
        return swapper;
    }
    private static class BlockSwapper {
        final World world;
        final EntityPlayer player;
        final ItemStack stack;
        final ChunkCoordinates origCoords;
        final int steps;
        final ChunkCoordinates coords;
        final boolean leaves;
        final boolean force;
        final List<String> posChecked;
        BlockSwapper(World world, EntityPlayer player, ItemStack stack, ChunkCoordinates origCoords, ChunkCoordinates coords, int steps, boolean leaves, boolean force, List<String> posChecked) {
            this.world = world;
            this.player = player;
            this.stack = stack;
            this.origCoords = origCoords;
            this.coords = coords;
            this.steps = steps;
            this.leaves = leaves;
            this.force = force;
            this.posChecked = posChecked;
        }
        void tick() {
            Block blockat = world.getBlock(coords.posX, coords.posY, coords.posZ);
            if(!force && blockat.isAir(world, coords.posX, coords.posY, coords.posZ))
                return;
            ToolHelper.removeBlockWithDrops(player, stack, world, coords.posX, coords.posY, coords.posZ, null, ToolHelper.materialsAxe, EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, stack) > 0, EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack), 0F, false);
            if(steps == 0)
                return;
            for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                int x = coords.posX + dir.offsetX;
                int y = coords.posY + dir.offsetY;
                int z = coords.posZ + dir.offsetZ;
                String pstr = posStr(x, y, z);
                if(posChecked.contains(pstr))
                    continue;
                Block block = world.getBlock(x, y, z);
                boolean log = block.isWood(world, x, y, z);
                boolean leaf = block.isLeaves(world, x, y, z);
                if(log || leaf) {
                    int steps = this.steps - 1;
                    steps = leaf ? leaves ? steps : 3 : steps;
                    addBlockSwapper(world, player, stack, origCoords, new ChunkCoordinates(x, y, z), steps, leaf, false, posChecked);
                    posChecked.add(pstr);
                }
            }
        }
        String posStr(int x, int y, int z) {
            return x + ":" + y + ":" + z;
        }
    }

}
