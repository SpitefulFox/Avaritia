package fox.spiteful.avaritia.compat.forestry;

import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.genetics.IAllele;

public class Genomes {

    public static IAllele[] getBase(){
        IAllele[] genome = new IAllele[EnumBeeChromosome.values().length];

        genome[EnumBeeChromosome.SPECIES.ordinal()] = GreedyBeeSpecies.ANNOYING;
        genome[EnumBeeChromosome.SPEED.ordinal()] = Allele.getBaseAllele("speedSlowest");
        genome[EnumBeeChromosome.LIFESPAN.ordinal()] = Allele.getBaseAllele("lifespanShorter");
        genome[EnumBeeChromosome.FERTILITY.ordinal()] = Allele.getBaseAllele("fertilityNormal");
        genome[EnumBeeChromosome.TEMPERATURE_TOLERANCE.ordinal()] = Allele.getBaseAllele("toleranceNone");
        genome[EnumBeeChromosome.NOCTURNAL.ordinal()] = Allele.getBaseAllele("boolFalse");
        genome[EnumBeeChromosome.HUMIDITY_TOLERANCE.ordinal()] = Allele.getBaseAllele("toleranceNone");
        genome[EnumBeeChromosome.TOLERANT_FLYER.ordinal()] = Allele.getBaseAllele("boolFalse");
        genome[EnumBeeChromosome.CAVE_DWELLING.ordinal()] = Allele.getBaseAllele("boolFalse");
        genome[EnumBeeChromosome.FLOWER_PROVIDER.ordinal()] = Allele.getBaseAllele("flowersVanilla");
        genome[EnumBeeChromosome.FLOWERING.ordinal()] = Allele.getBaseAllele("floweringSlowest");
        genome[EnumBeeChromosome.TERRITORY.ordinal()] = Allele.getBaseAllele("territoryDefault");
        genome[EnumBeeChromosome.EFFECT.ordinal()] = Allele.getBaseAllele("effectNone");

        return genome;
    }

    public static IAllele[] getBalanced(){
        IAllele[] genome = getBase();

        genome[EnumBeeChromosome.SPEED.ordinal()] = Allele.grindySpeed;
        genome[EnumBeeChromosome.LIFESPAN.ordinal()] = Allele.grindyLife;
        genome[EnumBeeChromosome.NOCTURNAL.ordinal()] = Allele.getBaseAllele("boolTrue");
        genome[EnumBeeChromosome.TOLERANT_FLYER.ordinal()] = Allele.getBaseAllele("boolTrue");
        //genome[EnumBeeChromosome.FLOWER_PROVIDER.ordinal()] = Allele.getBaseAllele("flowersVanilla");

        return genome;
    }

    public static IAllele[] getTedious(){
        IAllele[] genome = getBalanced();

        genome[EnumBeeChromosome.SPECIES.ordinal()] = GreedyBeeSpecies.TEDIOUS;

        return genome;
    }

    public static IAllele[] getInsufferable(){
        IAllele[] genome = getBalanced();

        genome[EnumBeeChromosome.SPECIES.ordinal()] = GreedyBeeSpecies.INSUFFERABLE;

        return genome;
    }

    public static IAllele[] getInfinite(){
        IAllele[] genome = getBase();

        genome[EnumBeeChromosome.SPECIES.ordinal()] = GreedyBeeSpecies.TRIPPY;
        genome[EnumBeeChromosome.NOCTURNAL.ordinal()] = Allele.getBaseAllele("boolTrue");
        genome[EnumBeeChromosome.CAVE_DWELLING.ordinal()] = Allele.getBaseAllele("boolTrue");
        //genome[EnumBeeChromosome.FLOWER_PROVIDER.ordinal()] = Allele.getBaseAllele("flowersVanilla");

        return genome;
    }

    public static IAllele[] getCosmic(){
        IAllele[] genome = getInfinite();

        genome[EnumBeeChromosome.SPECIES.ordinal()] = GreedyBeeSpecies.COSMIC;

        return genome;
    }

    public static IAllele[] getNeutronium(){
        IAllele[] genome = getInfinite();

        genome[EnumBeeChromosome.SPECIES.ordinal()] = GreedyBeeSpecies.NEUTRONIUM;

        return genome;
    }

    public static IAllele[] getInfiniteBee(){
        IAllele[] genome = getInfinite();

        genome[EnumBeeChromosome.SPECIES.ordinal()] = GreedyBeeSpecies.INFINITE;

        return genome;
    }

}
