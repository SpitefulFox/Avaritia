package fox.spiteful.avaritia.compat.forestry;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.apiculture.*;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.core.IIconProvider;
import forestry.api.genetics.*;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.awt.*;
import java.util.*;

public enum GreedyBeeSpecies implements IAlleleBeeSpecies, IIconProvider {


    ANNOYING("annoying", "incommodus", BeeBranch.BALANCED, "SpitefulFox", 0x662D01, 0x777777, false),
    TEDIOUS("tedious", "longus", BeeBranch.BALANCED, "SpitefulFox", 0x662D01, 0x662D01, false),
    INSUFFERABLE("insufferable", "intolerabilis", BeeBranch.BALANCED, "SpitefulFox", 0x662D01, 0xFFCAF0, true),
    TRIPPY("trippy", "laetus", BeeBranch.INFINITE, "SpitefulFox", 0xFFFFFF, -1, false),
    COSMIC("cosmic", "caelus", BeeBranch.INFINITE, "SpitefulFox", 0xFFFFFF, -1, true),
    NEUTRONIUM("neutronium", "sidereus", BeeBranch.INFINITE, "SpitefulFox", 0xA9F2FF, 0x2C2C2C, true),
    INFINITE("infinite", "infinitus", BeeBranch.INFINITE, "SpitefulFox", -1, -1, true, false, true);

    private String name;
    private String binomial;
    private IClassification branch;
    private boolean dominant;
    private String authority;
    private EnumTemperature temperature = EnumTemperature.NORMAL;
    private EnumHumidity humidity = EnumHumidity.NORMAL;
    private boolean hasEffect;
    private boolean isSecret;
    private boolean isCounted = true;
    private boolean fancy;
    private boolean isNocturnal = false;
    private IAllele[] genomeTemplate;
    private HashMap<ItemStack, Float> products = new HashMap<ItemStack, Float>();
    private HashMap<ItemStack, Float> specialties = new HashMap<ItemStack, Float>();
    private HashMap<ItemStack, Integer> legacyProducts = new HashMap<ItemStack, Integer>();
    private HashMap<ItemStack, Integer> legacySpecialties = new HashMap<ItemStack, Integer>();
    private int primaryColor;
    private int secondaryColor;

    GreedyBeeSpecies(String nombre, String genus, IClassification bran, String author, int mainColor, int otherColor, boolean dom, boolean secret, boolean shiny){
        name = nombre;
        binomial = genus;
        branch = bran;
        primaryColor = mainColor;
        secondaryColor = otherColor;
        authority = author;
        dominant = dom;
        AlleleManager.alleleRegistry.registerAllele(this);
        branch.addMemberSpecies(this);
        isSecret = secret;
        fancy = shiny;
    }

    GreedyBeeSpecies(String nombre, String genus, IClassification bran, String author, int mainColor, int otherColor, boolean dom){
        this(nombre, genus, bran, author, mainColor, otherColor, dom, false, false);
    }

    public static void buzz(){
        ANNOYING.registerGenomeTemplate(Genomes.getBalanced());
        TEDIOUS.registerGenomeTemplate(Genomes.getTedious());
        INSUFFERABLE.registerGenomeTemplate(Genomes.getInsufferable());
        TRIPPY.registerGenomeTemplate(Genomes.getInfinite());
        COSMIC.registerGenomeTemplate(Genomes.getCosmic());
        NEUTRONIUM.registerGenomeTemplate(Genomes.getNeutronium());
        INFINITE.registerGenomeTemplate(Genomes.getInfiniteBee());

        ANNOYING.addProduct(new ItemStack(LudicrousItems.combs, 1, 0), 0.4F);
        TEDIOUS.addProduct(new ItemStack(LudicrousItems.combs, 1, 0), 0.2F);
        INSUFFERABLE.addProduct(new ItemStack(LudicrousItems.combs, 1, 0), 0.1F);
        TRIPPY.addProduct(new ItemStack(LudicrousItems.combs, 1, 1), 0.15F);
        COSMIC.addProduct(new ItemStack(LudicrousItems.combs, 1, 1), 0.15F);
        COSMIC.addSpecialty(new ItemStack(LudicrousItems.beesource, 1, 0), 0.1F);
        NEUTRONIUM.addProduct(new ItemStack(LudicrousItems.combs, 1, 1), 0.15F);
        NEUTRONIUM.addSpecialty(new ItemStack(LudicrousItems.resource, 1, 2), 0.8F);
        INFINITE.addProduct(new ItemStack(LudicrousItems.combs, 1, 1), 0.15F);
    }

    @SideOnly(Side.CLIENT)
    private IIcon[][] icons;

    @Override
    public String getUID(){
        return "avaritia." + name;
    }

    @Override
    public String getName(){
        return StatCollector.translateToLocal("avaritia.bee." + name);
    }

    @Override
    public String getUnlocalizedName(){
        return "avaritia.bee." + name;
    }

    @Override
    public String getBinomial(){
        return binomial;
    }

    @Override
    public String getAuthority(){
        return authority;
    }

    @Override
    public String getDescription(){
        return StatCollector.translateToLocal("avaritia.bee." + name + ".desc");
    }

    @Override
    public IClassification getBranch() {
        return this.branch;
    }

    @Override
    public boolean isDominant() {
        return dominant;
    }

    @Override
    public boolean isCounted(){
        return isCounted;
    }

    @Override
    public boolean isSecret(){
        return isSecret;
    }

    @Override
    public boolean hasEffect(){
        return fancy;
    }

    @Override
    public EnumTemperature getTemperature(){
        return temperature;
    }

    @Override
    public EnumHumidity getHumidity(){
        return humidity;
    }

    @Override
    public boolean isNocturnal() {
        return this.isNocturnal;
    }

    public IAllele[] getGenome() {
        return genomeTemplate;
    }

    public void setGenome(IAllele[] genome){
        genomeTemplate = genome;
    }

    @Override
    public int getComplexity() {
        return 1 + getGeneticAdvancement(this, new ArrayList<IAllele>());
    }


    @Override
    public HashMap<ItemStack, Float> getProductChances() {
        return products;
    }

    @Override
    public HashMap<ItemStack, Float> getSpecialtyChances() {
        return specialties;
    }

    @Override
    public HashMap<ItemStack, Integer> getProducts() {
        return legacyProducts;
    }

    @Override
    public HashMap<ItemStack, Integer> getSpecialty() {
        return legacySpecialties;
    }

    @Override
    public IBeeRoot getRoot() {
        return BeeManager.beeRoot;
    }

    @Override
    public boolean isJubilant(IBeeGenome genome, IBeeHousing housing) {
        return true;
    }

    public void addProduct(ItemStack produce, float percentChance) {
        products.put(produce, percentChance);
        legacyProducts.put(produce, (int)(percentChance * 100));
    }

    public void addSpecialty(ItemStack produce, float percentChance) {
        specialties.put(produce, percentChance);
        legacySpecialties.put(produce, (int)(percentChance * 100));
    }

    public void registerGenomeTemplate(IAllele[] genome) {
        genomeTemplate = genome;
        BeeManager.beeRoot.registerTemplate(getUID(), genome);
    }

    private int getGeneticAdvancement(IAllele species, ArrayList<IAllele> exclude) {
        int own = 1;
        int highest = 0;
        exclude.add(species);
        for (IMutation mutation : getRoot().getPaths(species, EnumBeeChromosome.SPECIES)) {
            if (!exclude.contains(mutation.getAllele0())) {
                int otherAdvance = getGeneticAdvancement(mutation.getAllele0(), exclude);
                if (otherAdvance > highest) {
                    highest = otherAdvance;
                }
            }
            if (!exclude.contains(mutation.getAllele1())) {
                int otherAdvance = getGeneticAdvancement(mutation.getAllele1(), exclude);
                if (otherAdvance > highest) {
                    highest = otherAdvance;
                }
            }
        }
        return own + (highest < 0 ? 0 : highest);
    }

    @Override
    public float getResearchSuitability(ItemStack itemStack) {
        if (itemStack == null) {
            return 0f;
        }

        for (ItemStack product : this.products.keySet()) {
            if (itemStack.isItemEqual(product)) {
                return 1f;
            }
        }

        for (ItemStack specialty : this.specialties.keySet()) {
            if (specialty.isItemEqual(itemStack)) {
                return 1f;
            }
        }

        if (itemStack.getItem() == Ranger.honey) {
            return 0.5f;
        } else if (itemStack.getItem() == Ranger.honeydew) {
            return 0.7f;
        } else if (itemStack.getItem() == Ranger.comb) {
            return 0.4f;
        } else if (getRoot().isMember(itemStack)) {
            return 1.0f;
        } else {
            for (Map.Entry<ItemStack, Float> catalyst : BeeManager.beeRoot.getResearchCatalysts().entrySet()) {
                if (OreDictionary.itemMatches(itemStack, catalyst.getKey(), false)) {
                    return catalyst.getValue();
                }
            }
        }

        return 0f;
    }

    @Override
    public ItemStack[] getResearchBounty(World world, GameProfile researcher, IIndividual individual, int bountyLevel) {
        System.out.println("Bounty level: " + bountyLevel);
        ArrayList<ItemStack> bounty = new ArrayList<ItemStack>();

        if (world.rand.nextFloat() < ((10f / bountyLevel))) {
            Collection<? extends IMutation> resultantMutations = getRoot().getCombinations(this);
            if (resultantMutations.size() > 0) {
                IMutation[] candidates = resultantMutations.toArray(new IMutation[resultantMutations.size()]);
                bounty.add(AlleleManager.alleleRegistry.getMutationNoteStack(researcher, candidates[world.rand.nextInt(candidates.length)]));
            }
        }

        for (ItemStack product : this.products.keySet()) {
            ItemStack copy = product.copy();
            copy.stackSize = 1 + world.rand.nextInt(bountyLevel / 2);
            bounty.add(copy);
        }

        for (ItemStack specialty : this.specialties.keySet()) {
            ItemStack copy = specialty.copy();
            copy.stackSize = world.rand.nextInt(bountyLevel / 3);
            if (copy.stackSize > 0) {
                bounty.add(copy);
            }
        }

        return bounty.toArray(new ItemStack[bounty.size()]);
    }

    @Override
    public String getEntityTexture() {
        return "/gfx/forestry/entities/bees/honeyBee.png";
    }

    @Override
    public int getIconColour(int renderPass) {
        int value = 0xffffff;
        if (renderPass == 0) {
            if (this.primaryColor == -1) {
                int hue = (int) (System.currentTimeMillis() >> 2) % 360;
                value = Color.getHSBColor(hue / 360f, 0.75f, 0.80f).getRGB();
            } else {
                value = this.primaryColor;
            }
        } else if (renderPass == 1) {
            if (this.secondaryColor == -1) {
                int hue = (int) (System.currentTimeMillis() >> 3) % 360;
                hue += 60;
                hue = hue % 360;
                value = Color.getHSBColor(hue / 360f, 0.5f, 0.6f).getRGB();
            } else {
                value = this.secondaryColor;
            }
        }
        return value;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIconProvider getIconProvider() {
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(EnumBeeType type, int renderPass) {
        return icons[type.ordinal()][Math.min(renderPass, 2)];
    }

    @Override
    public void registerIcons(IIconRegister itemMap) {
        this.icons = new IIcon[EnumBeeType.values().length][3];

        IIcon body1 = itemMap.registerIcon("forestry:bees/default/body1");

        for (int i = 0; i < EnumBeeType.values().length; i++) {
            if (EnumBeeType.values()[i] == EnumBeeType.NONE)
                continue;

            icons[i][0] = itemMap.registerIcon("forestry:bees/default/" + EnumBeeType.values()[i].toString().toLowerCase(Locale.ENGLISH) + ".outline");
            icons[i][1] = (EnumBeeType.values()[i] != EnumBeeType.LARVAE) ? body1 : itemMap.registerIcon("forestry:bees/default/"
                    + EnumBeeType.values()[i].toString().toLowerCase(Locale.ENGLISH) + ".body");
            icons[i][2] = itemMap.registerIcon("forestry:bees/default/" + EnumBeeType.values()[i].toString().toLowerCase(Locale.ENGLISH) + ".body2");
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(short texUID) {
        return icons[0][0];
    }
}
