package fox.spiteful.avaritia.compat.forestry;

import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import net.minecraft.util.StatCollector;

public class Allele implements IAllele {

    public static Allele grindySpeed;
    public static Allele grindyLife;

    private String name;
    private String id;
    private boolean dom;

    public static void prepareGenes(){
        grindySpeed = new AlleleFloat("speedNerfed", true, 0.1f);
        grindyLife = new AlleleInteger("lifespanArtificial", false, 75);
    }

    public Allele(String moniker, boolean dominant){
        name = "avaritia.allele." + moniker;
        id = "avaritia." + moniker;
        dom = dominant;
        AlleleManager.alleleRegistry.registerAllele(this);
    }

    @Override
    public String getUID(){
        return id;
    }

    @Override
    public String getName(){
        return StatCollector.translateToLocal(name);
    }

    @Override
    public String getUnlocalizedName(){
        return name;
    }

    @Override
    public boolean isDominant(){
        return dom;
    }

    public static IAlleleBeeSpecies getBaseSpecies(String name) {
        return (IAlleleBeeSpecies) AlleleManager.alleleRegistry.getAllele((new StringBuilder()).append("forestry.species").append(name).toString());
    }

    public static IAlleleBeeSpecies getExtraSpecies(String name) {
        return (IAlleleBeeSpecies) AlleleManager.alleleRegistry.getAllele((new StringBuilder()).append("extrabees.species.").append(name.toLowerCase())
                .toString());
    }

    public static IAlleleBeeSpecies getMagicSpecies(String name) {
        return (IAlleleBeeSpecies) AlleleManager.alleleRegistry.getAllele((new StringBuilder()).append("magicbees.species").append(name)
                .toString());
    }

    public static IAllele getBaseAllele(String name) {
        return AlleleManager.alleleRegistry.getAllele("forestry." + name);
    }
}