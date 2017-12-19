package morph.avaritia.item;

import codechicken.lib.util.ItemUtils;
import morph.avaritia.init.ModItems;
import morph.avaritia.util.ItemStackWrapper;
import morph.avaritia.util.ToolHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.*;
import java.util.Map.Entry;

public class ItemMatterCluster extends Item {

    protected static Random randy = new Random();

    public static final String MAINTAG = "clusteritems";
    public static final String LISTTAG = "items";
    public static final String ITEMTAG = "item";
    public static final String COUNTTAG = "count";
    public static final String MAINCOUNTTAG = "total";

    public static int CAPACITY = 64 * 64;

    public ItemMatterCluster() {
        setMaxStackSize(1);
        setUnlocalizedName("avaritia:matter_cluster");
        setRegistryName("matter_cluster");
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return ModItems.COSMIC_RARITY;
    }

    @SuppressWarnings ({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey(MAINTAG)) {
            return;
        }
        NBTTagCompound clustertag = stack.getTagCompound().getCompoundTag(MAINTAG);

        tooltip.add(clustertag.getInteger(MAINCOUNTTAG) + "/" + CAPACITY + " " + I18n.format("tooltip.matter_cluster.counter"));
        tooltip.add("");

        if (GuiScreen.isShiftKeyDown()) {
            NBTTagList list = clustertag.getTagList(LISTTAG, 10);
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                ItemStack countstack = new ItemStack(tag.getCompoundTag(ITEMTAG));
                int count = tag.getInteger(COUNTTAG);

                tooltip.add(countstack.getItem().getRarity(countstack).rarityColor + countstack.getDisplayName() + TextFormatting.GRAY + " x " + count);
            }
        } else {
            tooltip.add(TextFormatting.DARK_GRAY + I18n.format("tooltip.matter_cluster.desc"));
            tooltip.add(TextFormatting.DARK_GRAY.toString() + TextFormatting.ITALIC + I18n.format("tooltip.matter_cluster.desc2"));
        }
    }

    public static List<ItemStack> makeClusters(Set<ItemStack> input) {
        Map<ItemStackWrapper, Integer> items = ToolHelper.collateMatterCluster(input);
        List<ItemStack> clusters = new ArrayList<>();
        List<Entry<ItemStackWrapper, Integer>> itemlist = new ArrayList<>();
        itemlist.addAll(items.entrySet());

        int currentTotal = 0;
        Map<ItemStackWrapper, Integer> currentItems = new HashMap<>();

        while (!itemlist.isEmpty()) {
            Entry<ItemStackWrapper, Integer> e = itemlist.get(0);
            ItemStackWrapper wrap = e.getKey();
            int wrapcount = e.getValue();

            int count = Math.min(CAPACITY - currentTotal, wrapcount);

            if (!currentItems.containsKey(e.getKey())) {
                currentItems.put(wrap, count);
            } else {
                currentItems.put(wrap, currentItems.get(wrap) + count);
            }
            currentTotal += count;

            e.setValue(wrapcount - count);
            if (e.getValue() == 0) {
                itemlist.remove(0);
            }

            if (currentTotal == CAPACITY) {
                ItemStack cluster = makeCluster(currentItems);

                clusters.add(cluster);

                currentTotal = 0;
                currentItems = new HashMap<>();
            }
        }

        if (currentTotal > 0) {
            ItemStack cluster = makeCluster(currentItems);

            clusters.add(cluster);
        }

        return clusters;
    }

    public static ItemStack makeCluster(Map<ItemStackWrapper, Integer> input) {
        ItemStack cluster = new ItemStack(ModItems.matter_cluster);
        int total = 0;
        for (int num : input.values()) {
            total += num;
        }
        setClusterData(cluster, input, total);
        return cluster;
    }

    public static Map<ItemStackWrapper, Integer> getClusterData(ItemStack cluster) {
        if (!cluster.hasTagCompound() || !cluster.getTagCompound().hasKey(MAINTAG)) {
            return null;
        }
        NBTTagCompound tag = cluster.getTagCompound().getCompoundTag(MAINTAG);
        NBTTagList list = tag.getTagList(LISTTAG, 10);
        Map<ItemStackWrapper, Integer> data = new HashMap<>();

        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound entry = list.getCompoundTagAt(i);
            ItemStackWrapper wrap = new ItemStackWrapper(new ItemStack(entry.getCompoundTag(ITEMTAG)));
            int count = entry.getInteger(COUNTTAG);
            data.put(wrap, count);
        }
        return data;
    }

    public static int getClusterSize(ItemStack cluster) {
        if (!cluster.hasTagCompound() || !cluster.getTagCompound().hasKey(MAINTAG)) {
            return 0;
        }
        return cluster.getTagCompound().getCompoundTag(MAINTAG).getInteger(MAINCOUNTTAG);
    }

    public static void setClusterData(ItemStack stack, Map<ItemStackWrapper, Integer> data, int count) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound clustertag = new NBTTagCompound();
        NBTTagList list = new NBTTagList();

        for (Entry<ItemStackWrapper, Integer> entry : data.entrySet()) {
            NBTTagCompound itemtag = new NBTTagCompound();
            itemtag.setTag(ITEMTAG, entry.getKey().stack.writeToNBT(new NBTTagCompound()));
            itemtag.setInteger(COUNTTAG, entry.getValue());
            list.appendTag(itemtag);
        }
        clustertag.setTag(LISTTAG, list);
        clustertag.setInteger(MAINCOUNTTAG, count);
        stack.getTagCompound().setTag(MAINTAG, clustertag);
    }

    public static void mergeClusters(ItemStack donor, ItemStack recipient) {
        int donorcount = getClusterSize(donor);
        int recipientcount = getClusterSize(recipient);

        //Lumberjack.log(Level.INFO, donorcount + ", " + recipientcount);
        if (donorcount == 0 || donorcount == CAPACITY || recipientcount == CAPACITY) {
            return;
        }

        Map<ItemStackWrapper, Integer> donordata = getClusterData(donor);
        Map<ItemStackWrapper, Integer> recipientdata = getClusterData(recipient);
        List<Entry<ItemStackWrapper, Integer>> datalist = new ArrayList<>();
        datalist.addAll(donordata.entrySet());

        while (recipientcount < CAPACITY && donorcount > 0) {
            Entry<ItemStackWrapper, Integer> e = datalist.get(0);
            ItemStackWrapper wrap = e.getKey();
            int wrapcount = e.getValue();

            int count = Math.min(CAPACITY - recipientcount, wrapcount);

            if (!recipientdata.containsKey(wrap)) {
                recipientdata.put(wrap, count);
            } else {
                recipientdata.put(wrap, recipientdata.get(wrap) + count);
            }

            donorcount -= count;
            recipientcount += count;

            if (wrapcount - count > 0) {
                e.setValue(wrapcount - count);
            } else {
                donordata.remove(wrap);
                datalist.remove(0);
            }
        }
        setClusterData(recipient, recipientdata, recipientcount);

        if (donorcount > 0) {
            setClusterData(donor, donordata, donorcount);
        } else {
            donor.setTagCompound(null);
            donor.setCount(0);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            List<ItemStack> drops = ToolHelper.collateMatterClusterContents(ItemMatterCluster.getClusterData(stack));

            for (ItemStack drop : drops) {
                ItemUtils.dropItem(world, player.getPosition(), drop);
            }
        }

        stack.setCount(0);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

}
