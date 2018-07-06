package morph.avaritia.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import morph.avaritia.Avaritia;
import morph.avaritia.init.ModItems;
import morph.avaritia.util.ItemStackWrapper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class ItemFracturedOre extends Item {
    //TODO, OreName needs to only be on NBT, so we can have safeguards against removed ores.

    public static final String OREKEY = "ore";
    protected static List<ItemStack> emulatedOres = new ArrayList<>();
    protected static Map<String, ItemStack> nameMapping = new HashMap<>();

    public ItemFracturedOre() {
        setCreativeTab(Avaritia.tab);
        setUnlocalizedName("avaritia:fractured_ore");
        setRegistryName("fractured_ore");
        setHasSubtypes(true);
    }

    //    @SuppressWarnings ({ "rawtypes" })
    //    @SideOnly (Side.CLIENT)
    //    @Override
    //    public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {
    //        // for debug purposes only - don't want these cluttering up the tab.
    //        /*for (ItemStack stack : emulatedOres) {
    //            list.add(getStackForOre(stack, 1));
    //        }*/
    //    }

    public ItemStack getStackForOre(ItemStack orestack, int stacksize) {
        NBTTagCompound oretag = NameStack.saveStackToNBT(orestack);

        ItemStack outstack = new ItemStack(this, stacksize, 0);
        NBTTagCompound stacktag = new NBTTagCompound();
        stacktag.setTag(OREKEY, oretag);
        outstack.setTagCompound(stacktag);
        outstack.setItemDamage(getDamage(outstack));

        return outstack;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey(OREKEY)) {
            NBTTagCompound tag = stack.getTagCompound().getCompoundTag(OREKEY);
            NameStack namestack = NameStack.loadFromNBT(tag);

            if (namestack != null) {
                ItemStack orestack = namestack.getStack();
                Item oreitem = orestack.getItem();
                return I18n.format("item.avaritia_fracturedore.prefix") + " " + oreitem.getItemStackDisplayName(orestack);
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

    // ########################################

    public static void parseOreDictionary() {
        String[] names = OreDictionary.getOreNames();

        ItemFracturedOre fracore = ModItems.fractured_ore;

        Multimap<String, ItemStack> toRegister = HashMultimap.create();
        Set<ItemStackWrapper> antiDupePool = new HashSet<>();

        for (String name : names) {
            if (name.startsWith("ore") && !name.startsWith("oreberry")) {
                //Lumberjack.info("ORE: "+name);

                List<ItemStack> ores = OreDictionary.getOres(name);

                for (ItemStack ore : ores) {
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
                for (int oreid : oreids) {
                    String oreidname = OreDictionary.getOreName(oreid);
                    //Lumberjack.info("Registering "+orestack.getItem().getItemStackDisplayName(orestack)+" ("+orestack+") ---> "+stack.getItem().getItemStackDisplayName(stack)+"#"+stack.getItemDamage()+" as "+oreidname);
                    OreDictionary.registerOre(oreidname, stack);
                }

                ItemStack smeltingResult = FurnaceRecipes.instance().getSmeltingResult(orestack);
                if (smeltingResult != null) {
                    float exp = FurnaceRecipes.instance().getSmeltingExperience(orestack);
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
            this(source.getItem().delegate.name().getResourcePath(), source.getItemDamage(), source.getCount(), source.getTagCompound());
        }

        public NameStack(String name, int damage, int size, NBTTagCompound nbt) {
            this.name = name;
            this.damage = damage;
            tag = nbt;
            this.size = size;
        }

        public NBTTagCompound saveToNBT() {
            NBTTagCompound savetag = new NBTTagCompound();
            savetag.setInteger("meta", damage);
            if (tag != null) {
                savetag.setTag("nbt", tag);
            }
            savetag.setString("name", name);
            savetag.setInteger("size", size);
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
            return Item.getByNameOrId(name);
        }

        public static NBTTagCompound saveStackToNBT(ItemStack stack) {
            return new NameStack(stack).saveToNBT();
        }

        public ItemStack getStack() {
            ItemStack stack = new ItemStack(getItem(), size, damage);
            if (tag != null) {
                stack.setTagCompound(tag.copy());
            }
            return stack;
        }

        @Override
        public String toString() {
            return "NameStack: " + size + "x " + name + "@" + damage + ", " + tag;
        }
    }
}
