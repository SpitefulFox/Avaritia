package fox.spiteful.avaritia.items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.Lumberjack;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

public class ItemFracturedOre extends Item {

	public static final String OREKEY = "ore";
	protected static List<ItemStack> emulatedOres = new ArrayList<ItemStack>();
	protected static Map<String, ItemStack> nameMapping = new HashMap<String, ItemStack>();
	public static IIcon unknownIcon;
		
	public ItemFracturedOre() {
		this.setCreativeTab(Avaritia.tab);
		this.setUnlocalizedName("avaritia_fracturedore");
		this.setTextureName("avaritia:fracturedore");
		this.setHasSubtypes(true);
	}

	@SuppressWarnings({ "rawtypes"})
	@SideOnly(Side.CLIENT)
	@Override
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
		// for debug purposes only - don't want these cluttering up the tab.
        /*for (ItemStack stack : emulatedOres) {
        	list.add(getStackForOre(stack, 1));
        }*/
    }
	
	public ItemStack getStackForOre(ItemStack orestack, int stacksize) {
		NBTTagCompound oretag = NameStack.saveStackToNBT(orestack);
		
		ItemStack outstack = new ItemStack(this, stacksize, 0);
		NBTTagCompound stacktag = new NBTTagCompound();
		stacktag.setTag(OREKEY, oretag);
		outstack.setTagCompound(stacktag);
		outstack.setItemDamage(this.getDamage(outstack));
		
		return outstack;
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack)
    {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey(OREKEY)) {
			NBTTagCompound tag = stack.getTagCompound().getCompoundTag(OREKEY);
			NameStack namestack = NameStack.loadFromNBT(tag);
			
			if (namestack != null) {
				ItemStack orestack = namestack.getStack();
				Item oreitem = orestack.getItem();
				return StatCollector.translateToLocal("item.avaritia_fracturedore.prefix") +" "+ oreitem.getItemStackDisplayName(orestack);
			}
		}
		return super.getItemStackDisplayName(stack);
    }
	
	@Override
	public int getDamage(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey(OREKEY)) {
			NameStack nstack = NameStack.loadFromNBT(stack.getTagCompound().getCompoundTag(OREKEY));
			int id = Item.getIdFromItem(nstack.getItem());
			int meta = nstack.damage;
			int out = meta + (id << 4);
			stack.setItemDamage(out);
			return out;
		}
		return 0;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
		super.registerIcons(ir);
		
		unknownIcon = ir.registerIcon("avaritia:unknown");
	}
	
	// ########################################
	
	public static void brushUpUncomfortablyAgainstTheOreDictionary() {
		String[] names = OreDictionary.getOreNames();
		
		ItemFracturedOre fracore = (ItemFracturedOre)LudicrousItems.fractured_ore;
		
		Multimap<String, ItemStack> toRegister = HashMultimap.create();
		Set<ItemStackWrapper> antiDupePool = new HashSet<ItemStackWrapper>();
		
		for (String name : names) {
			if (name.startsWith("ore") && !name.startsWith("oreberry")) {
				//Lumberjack.info("ORE: "+name);
				
				List<ItemStack> ores = OreDictionary.getOres(name);
				
				for(ItemStack ore : ores) {
					ItemStackWrapper compare = new ItemStackWrapper(ore);
					if (!antiDupePool.contains(compare)) {
						//Lumberjack.info(ore);
						antiDupePool.add(compare);
						emulatedOres.add(ore.copy());
						
						ItemStack frac = fracore.getStackForOre(ore, 1);
						toRegister.put(name, frac);
					}
				}
			}
		}
		
		for (String name : toRegister.keySet()) {
			Collection<ItemStack> stacks = toRegister.get(name);
			for (ItemStack stack : stacks) {
				ItemStack orestack = NameStack.loadFromNBT(stack.getTagCompound().getCompoundTag(OREKEY)).getStack();
				int[] oreids = OreDictionary.getOreIDs(orestack);
				for (int i=0; i<oreids.length; i++) {
					String oreidname = OreDictionary.getOreName(oreids[i]);
					//Lumberjack.info("Registering "+orestack.getItem().getItemStackDisplayName(orestack)+" ("+orestack+") ---> "+stack.getItem().getItemStackDisplayName(stack)+"#"+stack.getItemDamage()+" as "+oreidname);
					OreDictionary.registerOre(oreidname, stack);
				}
				
				ItemStack smeltingResult = FurnaceRecipes.smelting().getSmeltingResult(orestack);
				if (smeltingResult != null) {
					float exp = FurnaceRecipes.smelting().func_151398_b(orestack);
					//Lumberjack.info("Registering "+stack+" to smelt to "+smeltingResult+" for "+exp+" experience");
					GameRegistry.addSmelting(stack, smeltingResult, exp);
				}
			}
		}
	}
	
	public static class NameStack {
		String name;
		int damage;
		NBTTagCompound tag;
		int size;
		
		public NameStack(ItemStack source) {
			this(source.getItem().delegate.name(), source.getItemDamage(), source.stackSize, source.getTagCompound());
		}
		
		public NameStack(String name, int damage, int size, NBTTagCompound nbt) {
			this.name = name;
			this.damage = damage;
			this.tag = nbt;
			this.size = size;
		}
		
		public NBTTagCompound saveToNBT() {
			NBTTagCompound savetag = new NBTTagCompound();
			savetag.setInteger("meta", this.damage);
			if (this.tag != null) {
				savetag.setTag("nbt", this.tag);
			}
			savetag.setString("name", this.name);
			savetag.setInteger("size", this.size);
			return savetag;
		}
		
		public static NameStack loadFromNBT(NBTTagCompound tag) {
			NBTTagCompound stacktag = null;
			if (tag.hasKey("nbt")) {
				stacktag = tag.getCompoundTag("nbt");
			}
			return new NameStack(tag.getString("name"), tag.getInteger("meta"), tag.getInteger("size"), stacktag);
		}
		
		public Item getItem() {
			return (Item) Item.itemRegistry.getObject(this.name);
		}
		
		public static NBTTagCompound saveStackToNBT(ItemStack stack) {
			return new NameStack(stack).saveToNBT();
		}
		
		public ItemStack getStack() {
			ItemStack stack = new ItemStack(this.getItem(), this.size, this.damage);
			if(this.tag != null) {
				stack.setTagCompound((NBTTagCompound) this.tag.copy());
			}
			return stack;
		}
		
		@Override
		public String toString() {
			return "NameStack: "+this.size+"x "+this.name+"@"+this.damage+", "+this.tag;
		}
	}
}
