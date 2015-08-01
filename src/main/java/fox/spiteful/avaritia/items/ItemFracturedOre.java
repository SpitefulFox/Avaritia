package fox.spiteful.avaritia.items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import fox.spiteful.avaritia.Avaritia;
import fox.spiteful.avaritia.Lumberjack;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

public class ItemFracturedOre extends Item {

	public static final String OREKEY = "ore";
	protected static List<ItemStack> emulatedOres = new ArrayList<ItemStack>();
	
	public ItemFracturedOre() {
		this.setCreativeTab(Avaritia.tab);
		this.setUnlocalizedName("avaritia_fracturedore");
		this.setTextureName("avaritia:fracturedore");
		this.setHasSubtypes(true);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	@Override
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for (ItemStack stack : emulatedOres) {
        	list.add(getStackForOre(stack, 1));
        }
    }
	
	public ItemStack getStackForOre(ItemStack orestack, int stacksize) {
		NBTTagCompound oretag = orestack.writeToNBT(new NBTTagCompound());
		
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
			ItemStack orestack = ItemStack.loadItemStackFromNBT(stack.getTagCompound().getCompoundTag(OREKEY));
			return StatCollector.translateToLocal("item.avaritia_fracturedore.prefix") +" "+ orestack.getItem().getItemStackDisplayName(orestack);
		}
		return super.getItemStackDisplayName(stack);
    }
	
	@Override
	public int getDamage(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey(OREKEY)) {
;			NBTTagCompound tag = stack.getTagCompound().getCompoundTag(OREKEY);
			int id = tag.getInteger("id");
			int meta = tag.getInteger("Damage");
			return meta + (id << 4);
		}
		return 0;
	}
	
	// ########################################
	
	public static void brushUpUncomfortablyAgainstTheOreDictionary() {
		String[] names = OreDictionary.getOreNames();
		
		ItemFracturedOre fracore = (ItemFracturedOre)LudicrousItems.fractured_ore;
		
		Multimap<String, ItemStack> toRegister = HashMultimap.create();
		Set<ItemStackWrapper> antiDupePool = new HashSet<ItemStackWrapper>();
		
		for (String name : names) {
			if (name.startsWith("ore")) {
				Lumberjack.info("ORE: "+name);
				
				List<ItemStack> ores = OreDictionary.getOres(name);
				
				for(ItemStack ore : ores) {
					ItemStackWrapper compare = new ItemStackWrapper(ore);
					if (!antiDupePool.contains(compare)) {
						Lumberjack.info(ore);
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
				ItemStack orestack = ItemStack.loadItemStackFromNBT(stack.getTagCompound().getCompoundTag(OREKEY)); 
				int[] oreids = OreDictionary.getOreIDs(orestack);
				for (int i=0; i<oreids.length; i++) {
					String oreidname = OreDictionary.getOreName(oreids[i]);
					Lumberjack.info("Registering "+orestack.getItem().getItemStackDisplayName(orestack)+" ("+orestack+") ---> "+stack.getItem().getItemStackDisplayName(stack)+"#"+stack.getItemDamage()+" as "+oreidname);
					OreDictionary.registerOre(oreidname, stack);
				}
				
				ItemStack smeltingResult = FurnaceRecipes.smelting().getSmeltingResult(orestack);
				if (smeltingResult != null) {
					float exp = FurnaceRecipes.smelting().func_151398_b(orestack);
					Lumberjack.info("Registering "+stack+" to smelt to "+smeltingResult+" for "+exp+" experience");
					GameRegistry.addSmelting(stack, smeltingResult, exp);
				}
			}
		}
	}
	
	private static class ItemStackWrapper {
		public final ItemStack stack;
		
		public ItemStackWrapper(ItemStack stack) {
			this.stack = stack;
		}
		
		@Override
		public boolean equals(Object otherobj) {
			if (otherobj instanceof ItemStackWrapper) {
				ItemStackWrapper other = (ItemStackWrapper)otherobj;
				
				if (this.stack.getItem().equals(other.stack.getItem()) 
					&& this.stack.getItemDamage() == other.stack.getItemDamage()
				) {
					
					if (this.stack.stackTagCompound == null && other.stack.stackTagCompound == null) {
						return true;
					} else {
						if (this.stack.stackTagCompound == null ^ other.stack.stackTagCompound == null) {
							return false;
						}
						else if (this.stack.stackTagCompound.equals(other.stack.stackTagCompound)) {
							return true;
						}
					}
					
				}
			}
			return false;
		}
		
		@Override 
		public int hashCode() {
			int h = this.stack.getItem().hashCode();
			if (this.stack.stackTagCompound != null) {
				h ^= this.stack.stackTagCompound.hashCode();
			}
			return h ^ this.stack.getItemDamage();
		}
		
		@Override
		public String toString() {
			return this.stack.toString();
		}
	}
}
