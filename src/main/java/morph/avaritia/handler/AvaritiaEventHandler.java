package morph.avaritia.handler;

import morph.avaritia.init.ModItems;
import morph.avaritia.item.ItemArmorInfinity;
import morph.avaritia.item.ItemFracturedOre;
import morph.avaritia.item.ItemMatterCluster;
import morph.avaritia.item.tools.ItemSwordInfinity;
import morph.avaritia.util.TextUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.EntityEquipmentSlot.Type;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class AvaritiaEventHandler {

    private static Map<Integer, List<AEOCrawlerTask>> crawlerTasks = new HashMap<>();

    private static Set<ItemStack> capturedDrops = new LinkedHashSet<>();
    private static boolean doItemCapture = false;

    //These are defaults, loaded from config.
    public static final Set<String> defaultTrashOres = new HashSet<>();

    public static boolean isInfinite(EntityPlayer player) {
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            if (slot.getSlotType() != Type.ARMOR) {
                continue;
            }
            ItemStack stack = player.getItemStackFromSlot(slot);
            if (stack.isEmpty() || !(stack.getItem() instanceof ItemArmorInfinity)) {
                return false;
            }
        }
        return true;
    }

    //region EntityItem capture.
    public static void enableItemCapture() {
        doItemCapture = true;
    }

    public static void stopItemCapture() {
        doItemCapture = false;
    }

    public static boolean isItemCaptureEnabled() {
        return doItemCapture;
    }

    public static Set<ItemStack> getCapturedDrops() {
        Set<ItemStack> dropsCopy = new LinkedHashSet<>(capturedDrops);
        capturedDrops.clear();
        return dropsCopy;
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (doItemCapture) {
            if (event.getEntity() instanceof EntityItem) {
                ItemStack stack = ((EntityItem) event.getEntity()).getItem();
                capturedDrops.add(stack);
                event.setCanceled(true);
            }
        }
    }
    //endregion

    public static AEOCrawlerTask startCrawlerTask(World world, EntityPlayer player, ItemStack stack, BlockPos coords, int steps, boolean leaves, boolean force, Set<BlockPos> posChecked) {
        AEOCrawlerTask swapper = new AEOCrawlerTask(world, player, stack, coords, steps, leaves, force, posChecked);
        int dim = world.provider.getDimension();
        if (!crawlerTasks.containsKey(dim)) {
            crawlerTasks.put(dim, new ArrayList<>());
        }
        crawlerTasks.get(dim).add(swapper);
        return swapper;
    }

    @SubscribeEvent
    public void onTickEnd(TickEvent.WorldTickEvent event) {//TODO, clamp at specific num ops per tick.
        if (event.phase == TickEvent.Phase.END) {
            int dim = event.world.provider.getDimension();
            if (crawlerTasks.containsKey(dim)) {
                List<AEOCrawlerTask> swappers = crawlerTasks.get(dim);
                List<AEOCrawlerTask> swappersSafe = new ArrayList<>(swappers);
                swappers.clear();
                for (AEOCrawlerTask s : swappersSafe) {
                    if (s != null) {
                        s.tick();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerMine(PlayerInteractEvent.LeftClickBlock event) {
        if (!ConfigHandler.bedrockBreaker || event.getFace() == null || event.getWorld().isRemote || event.getItemStack().isEmpty() || event.getEntityPlayer().capabilities.isCreativeMode) {
            return;
        }
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        int meta = block.getMetaFromState(state);
        if (state.getBlockHardness(world, event.getPos()) <= -1 && event.getItemStack().getItem() == ModItems.infinity_pickaxe && (state.getMaterial() == Material.ROCK || state.getMaterial() == Material.IRON)) {

            if (event.getItemStack().getTagCompound() != null && event.getItemStack().getTagCompound().getBoolean("hammer")) {
                ModItems.infinity_pickaxe.onBlockStartBreak(event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND), event.getPos(), event.getEntityPlayer());
            } else {//TODO, FIXME, HELP!
                //if (block.quantityDropped(randy) == 0) {
                //    ItemStack drop = block.getPickBlock(state, ToolHelper.raytraceFromEntity(event.getWorld(), event.getEntityPlayer(), true, 10), event.getWorld(), event.getPos(), event.getEntityPlayer());
                //    if (drop == null) {
                //        drop = new ItemStack(block, 1, meta);
                //    }
                //    ToolHelper.dropItem(drop, event.getEntityPlayer().worldObj, event.getPos());
                //} else {
                //    block.harvestBlock(event.getWorld(), event.getEntityPlayer(), event.getPos(), state, null, null);
                ///}
                //event.getWorld().setBlockToAir(event.getPos());
                //event.world.playAuxSFX(2001, event.getPos(), Block.getIdFromBlock(block) + (meta << 12));
            }
        }
    }

    @SubscribeEvent
    public void handleExtraLuck(HarvestDropsEvent event) {
        if (event.getHarvester() == null) {
            return;
        }
        ItemStack mainHand = event.getHarvester().getHeldItem(EnumHand.MAIN_HAND);

        if (!mainHand.isEmpty() && mainHand.getItem() == ModItems.infinity_pickaxe) {
            applyLuck(event, 4);
        }
    }

    public static void applyLuck(HarvestDropsEvent event, int multiplier) {
        //Only do stuff on rock.
        if (event.getState().getMaterial() == Material.ROCK) {
            List<ItemStack> adds = new ArrayList<>();
            List<ItemStack> removals = new ArrayList<>();

            for (ItemStack drop : event.getDrops()) {
                //We are a drop that is not the same as the Blocks ItemBlock and the drop itself is not an ItemBlock, AKA, Redstone, Lapis.
                if (drop.getItem() != Item.getItemFromBlock(event.getState().getBlock()) && !(drop.getItem() instanceof ItemBlock)) {
                    //Apply standard Luck modifier
                    drop.setCount(Math.min(drop.getCount() * multiplier, drop.getMaxStackSize()));
                } else if (ConfigHandler.fracturedOres && drop.getItem() == Item.getItemFromBlock(event.getState().getBlock())) {
                    //kk, we are an ore block, Lets test for oreDict and add fractured ores.
                    ItemFracturedOre fracturedOre = ModItems.fractured_ore;
                    int[] iDs = OreDictionary.getOreIDs(drop);
                    for (int id : iDs) {
                        String oreName = OreDictionary.getOreName(id);
                        if (oreName.startsWith("ore")) {
                            // add the fractured ores
                            adds.add(fracturedOre.getStackForOre(drop, Math.min(drop.getCount() * (multiplier + 1), drop.getMaxStackSize())));
                            removals.add(drop);
                            break;
                        }
                    }
                }
            }
            event.getDrops().addAll(adds);
            event.getDrops().removeAll(removals);
        }
    }

    @SideOnly (Side.CLIENT)
    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() instanceof ItemSwordInfinity) {
            for (int x = 0; x < event.getToolTip().size(); x++) {
                if (event.getToolTip().get(x).contains(I18n.translateToLocal("attribute.name.generic.attackDamage")) || event.getToolTip().get(x).contains(I18n.translateToLocal("Attack Damage"))) {
                    event.getToolTip().set(x, TextFormatting.BLUE + "+" + TextUtils.makeFabulous(I18n.translateToLocal("tip.infinity")) + " " + TextFormatting.BLUE + I18n.translateToLocal("attribute.name.generic.attackDamage"));
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public void onGetHurt(LivingHurtEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        if (!player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() == ModItems.infinity_sword && player.isHandActive()) {//TODO Blocking? Maybe add a shield?
            event.setCanceled(true);
        }
        if (isInfinite(player) && !event.getSource().damageType.equals("infinity")) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onAttacked(LivingAttackEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer)) {
            return;
        }
        if (event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityPlayer) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        if (isInfinite(player) && !event.getSource().damageType.equals("infinity")) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        if (event.isRecentlyHit() && event.getEntityLiving() instanceof EntitySkeleton && event.getSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
            if (!player.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && player.getHeldItem(EnumHand.MAIN_HAND).getItem() == ModItems.skull_sword) {
                // ok, we need to drop a skull then.
                if (event.getDrops().isEmpty()) {
                    addDrop(event, new ItemStack(Items.SKULL, 1, 1));
                } else {
                    int skulls = 0;

                    for (int i = 0; i < event.getDrops().size(); i++) {
                        EntityItem drop = event.getDrops().get(i);
                        ItemStack stack = drop.getItem();
                        if (stack.getItem() == Items.SKULL) {
                            if (stack.getItemDamage() == 1) {
                                skulls++;
                            } else if (stack.getItemDamage() == 0) {
                                skulls++;
                                stack.setItemDamage(1);
                            }
                        }
                    }

                    if (skulls == 0) {
                        addDrop(event, new ItemStack(Items.SKULL, 1, 1));
                    }
                }

            }
        }
    }

    @SubscribeEvent
    public void diggity(BreakSpeed event) {
        if (!event.getEntityLiving().getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
            ItemStack held = event.getEntityLiving().getHeldItem(EnumHand.MAIN_HAND);
            if (held.getItem() == ModItems.infinity_pickaxe || held.getItem() == ModItems.infinity_shovel) {
                if (!event.getEntityLiving().onGround) {
                    event.setNewSpeed(event.getNewSpeed() * 5);
                }
                if (!event.getEntityLiving().isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(event.getEntityLiving())) {
                    event.setNewSpeed(event.getNewSpeed() * 5);
                }
                if (held.getTagCompound() != null) {
                    if (held.getTagCompound().getBoolean("hammer") || held.getTagCompound().getBoolean("destroyer")) {
                        event.setNewSpeed(event.getNewSpeed() * 0.5F);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void canHarvest(PlayerEvent.HarvestCheck event) {
        if (!event.getEntityLiving().getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
            ItemStack held = event.getEntityLiving().getHeldItem(EnumHand.MAIN_HAND);
            if (held.getItem() == ModItems.infinity_shovel && event.getTargetBlock().getMaterial() == Material.ROCK) {
                if (held.getTagCompound() != null && held.getTagCompound().getBoolean("destroyer") && isGarbageBlock(event.getTargetBlock().getBlock())) {
                    event.setResult(Event.Result.ALLOW);
                }
            }
        }
    }

    private static boolean isGarbageBlock(Block block) {
        for (int id : OreDictionary.getOreIDs(new ItemStack(block, 1))) {
            String ore = OreDictionary.getOreName(id);
            if (ore.equals("cobblestone") || ore.equals("stone") || ore.equals("netherrack")) {
                return true;
            }
        }

        return false;
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (isInfinite(player) && !event.getSource().getDamageType().equals("infinity")) {
                event.setCanceled(true);
                player.setHealth(player.getMaxHealth());
            }
        }
    }

    private void addDrop(LivingDropsEvent event, ItemStack drop) {
        EntityItem entityitem = new EntityItem(event.getEntityLiving().world, event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, drop);
        entityitem.setDefaultPickupDelay();
        event.getDrops().add(entityitem);
    }

    @SubscribeEvent
    public void clusterClustererererer(EntityItemPickupEvent event) {
        if (event.getEntityPlayer() != null && event.getItem().getItem().getItem() == ModItems.matter_cluster) {
            ItemStack stack = event.getItem().getItem();
            EntityPlayer player = event.getEntityPlayer();

            for (ItemStack slot : player.inventory.mainInventory) {
                if (stack.isEmpty()) {
                    break;
                }
                if (slot.getItem() == ModItems.matter_cluster) {
                    ItemMatterCluster.mergeClusters(stack, slot);
                }
            }
        }
    }

}
