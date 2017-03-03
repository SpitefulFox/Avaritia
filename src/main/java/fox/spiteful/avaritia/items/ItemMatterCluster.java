package fox.spiteful.avaritia.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import codechicken.lib.math.MathHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fox.spiteful.avaritia.Lumberjack;
import fox.spiteful.avaritia.items.tools.ToolHelper;
import fox.spiteful.avaritia.render.ICosmicRenderItem;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemMatterCluster extends Item implements ICosmicRenderItem {

	protected static Random randy = new Random();
	
	public static final String MAINTAG = "clusteritems";
	public static final String LISTTAG = "items";
	public static final String ITEMTAG = "item";
	public static final String COUNTTAG = "count";
	public static final String MAINCOUNTTAG = "total";
	
	public static int capacity = 64*64;
	
	public IIcon iconFull;
	public IIcon cosmicIcon;
	public IIcon cosmicIconFull;
	
	public ItemMatterCluster() {
		this.setMaxStackSize(1);
		this.setUnlocalizedName("avaritia_mattercluster");
		this.setTextureName("avaritia:mattercluster");
	}
	
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
		super.registerIcons(ir);
		
		this.cosmicIcon = ir.registerIcon("avaritia:mattercluster_mask");
		
		this.iconFull = ir.registerIcon("avaritia:mattercluster_full");
		this.cosmicIconFull = ir.registerIcon("avaritia:mattercluster_full_mask");
	}
	
	@Override
    public EnumRarity getRarity(ItemStack stack)
    {
		return LudicrousItems.cosmic;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean debug) {
		if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey(MAINTAG)) {
			return;
		}
		NBTTagCompound clustertag = stack.getTagCompound().getCompoundTag(MAINTAG);
		
		tooltip.add(clustertag.getInteger(MAINCOUNTTAG) + "/" + capacity + " " + StatCollector.translateToLocal("tooltip.matter_cluster.counter"));
		tooltip.add("");
		
		
		if (GuiScreen.isShiftKeyDown()) {
			NBTTagList list = clustertag.getTagList(LISTTAG, 10);
			for (int i=0; i<list.tagCount(); i++) {
				NBTTagCompound tag = list.getCompoundTagAt(i);
				ItemStack countstack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag(ITEMTAG));
				int count = tag.getInteger(COUNTTAG);
				
				tooltip.add(countstack.getItem().getRarity(countstack).rarityColor + countstack.getDisplayName() +EnumChatFormatting.GRAY+" x " + count);
			}
		} else {
			tooltip.add(EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("tooltip.matter_cluster.desc"));
			tooltip.add(EnumChatFormatting.DARK_GRAY.toString() + EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.matter_cluster.desc2"));
		}
	}
	
	public static List<ItemStack> makeClusters(List<ItemStack> input) {
		Map<ItemStackWrapper, Integer> items = ToolHelper.collateMatterCluster(input);
		List<ItemStack> clusters = new ArrayList<ItemStack>();
		List<Entry<ItemStackWrapper, Integer>> itemlist = new ArrayList<Entry<ItemStackWrapper, Integer>>();
		itemlist.addAll(items.entrySet());
		
		int currentTotal = 0;
		Map<ItemStackWrapper, Integer> currentItems = new HashMap<ItemStackWrapper, Integer>();
		
		while(!itemlist.isEmpty()) {
			Entry<ItemStackWrapper, Integer> e = itemlist.get(0);
			ItemStackWrapper wrap = e.getKey();
			int wrapcount = e.getValue();
			
			int count = Math.min(capacity - currentTotal, wrapcount);
			
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
			
			if (currentTotal == capacity) {
				ItemStack cluster = makeCluster(currentItems);
				
				clusters.add(cluster);
				
				currentTotal = 0;
				currentItems = new HashMap<ItemStackWrapper, Integer>();
			}
		}
		
		if (currentTotal > 0) {
			ItemStack cluster = makeCluster(currentItems);
			
			clusters.add(cluster);
		}
		
		return clusters;
	}
	
	public static ItemStack makeCluster(Map<ItemStackWrapper, Integer> input) {
		ItemStack cluster = new ItemStack(LudicrousItems.matter_cluster);
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
		Map<ItemStackWrapper, Integer> data = new HashMap<ItemStackWrapper, Integer>();
		
		for (int i=0; i<list.tagCount(); i++) {
			NBTTagCompound entry = list.getCompoundTagAt(i);
			ItemStackWrapper wrap = new ItemStackWrapper(ItemStack.loadItemStackFromNBT(entry.getCompoundTag(ITEMTAG)));
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
		
		Lumberjack.info(donorcount +", "+ recipientcount);
		if (donorcount == 0 || donorcount == capacity || recipientcount == capacity) {
			return;
		}
		
		Map<ItemStackWrapper, Integer> donordata = getClusterData(donor);
		Map<ItemStackWrapper, Integer> recipientdata = getClusterData(recipient);
		List<Entry<ItemStackWrapper, Integer>> datalist = new ArrayList<Entry<ItemStackWrapper, Integer>>();
		datalist.addAll(donordata.entrySet());
		
		while (recipientcount < capacity && donorcount > 0) {
			Entry<ItemStackWrapper, Integer> e = datalist.get(0);
			ItemStackWrapper wrap = e.getKey();
			int wrapcount = e.getValue();
			
			int count = Math.min(capacity - recipientcount, wrapcount);
			
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
			donor.stackSize = 0;
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
		if(!world.isRemote) {
			List<ItemStack> drops = ToolHelper.collateMatterClusterContents(ItemMatterCluster.getClusterData(stack));
			
			for (ItemStack drop : drops) {
				ToolHelper.dropItem(drop, world, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ));
			}
		}
		
        stack.stackSize = 0;
        return stack;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getMaskTexture(ItemStack stack, EntityPlayer player) {
		int count = getClusterSize(stack);
		if (count == capacity) {
			return cosmicIconFull;
		}
		return cosmicIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getMaskMultiplier(ItemStack stack, EntityPlayer player) {
		int count = getClusterSize(stack);
		return count / (float)capacity;
	}
	
	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		int count = getClusterSize(stack);
		if (count == capacity) {
			return iconFull;
		}
		return super.getIcon(stack, pass);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIconIndex(ItemStack stack)
    {
        return this.getIcon(stack, 0);
    }
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
    {
		int count = getClusterSize(stack);
		if (count == capacity) {
			return super.getUnlocalizedName(stack) + ".full";
		}
        return super.getUnlocalizedName(stack);
    }
}
