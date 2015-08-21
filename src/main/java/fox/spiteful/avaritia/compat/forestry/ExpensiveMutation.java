package fox.spiteful.avaritia.compat.forestry;

import forestry.api.apiculture.*;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IGenome;

import java.util.ArrayList;
import java.util.Collection;

public class ExpensiveMutation implements IBeeMutation {

    private IAllele mom;
    private IAllele dad;
    private IAllele[] template;
    private boolean secret = false;
    private float baseChance;

    public ExpensiveMutation(IAllele first, IAllele second, IAllele[] result, float chance){
        mom = first;
        dad = second;
        template = result;
        baseChance = chance;
        BeeManager.beeRoot.registerMutation(this);
    }

    public static void mutate(){
        new ExpensiveMutation(Allele.getBaseSpecies("Hermitic"), Allele.getBaseSpecies("Edenic"), Genomes.getBalanced(), 0.8f);
        new ExpensiveMutation(Allele.getBaseSpecies("Heroic"), GreedyBeeSpecies.ANNOYING, Genomes.getTedious(), 0.7f);
        new ExpensiveMutation(Allele.getBaseSpecies("Phantasmal"), GreedyBeeSpecies.TEDIOUS, Genomes.getTedious(), 0.6f);
    }

    @Override
    public IBeeRoot getRoot() {
        return BeeManager.beeRoot;
    }

    @Override
    public IAllele getAllele0() {
        return mom;
    }

    @Override
    public IAllele getAllele1() {
        return dad;
    }

    @Override
    public IAllele[] getTemplate() {
        return template;
    }

    @Override
    public float getBaseChance() {
        return baseChance;
    }

    @Override
    public boolean isSecret(){
        return secret;
    }

    @Override
    public boolean isPartner(IAllele allele) {
        return mom.getUID().equals(allele.getUID()) || dad.getUID().equals(allele.getUID());
    }

    @Override
    public IAllele getPartner(IAllele allele) {
        if(allele.getUID().equals(mom.getUID()))
            return dad;
        return mom;
    }

    @Override
    public Collection<String> getSpecialConditions() {
        return new ArrayList<String>();
    }

    @Override
    public float getChance(IBeeHousing housing, IAllele allele0, IAllele allele1, IGenome genome0, IGenome genome1) {
        float finalChance = 0f;
        float chance = this.baseChance * 1f;

        if(isPartner(allele0) && isPartner(allele1)){
            finalChance = Math.round(chance
                    * housing.getMutationModifier((IBeeGenome)genome0,
                    (IBeeGenome)genome1, chance)
                    * BeeManager.beeRoot.getBeekeepingMode(housing.getWorld())
                    .getMutationModifier((IBeeGenome) genome0,
                            (IBeeGenome) genome1, chance));
        }

        return finalChance;
    }

    @Override
    public float getChance(IBeeHousing housing, IAlleleBeeSpecies allele0, IAlleleBeeSpecies allele1, IBeeGenome genome0, IBeeGenome genome1) {
        float finalChance = 0f;
        float chance = this.baseChance * 1f;

        if(isPartner(allele0) && isPartner(allele1)){
            finalChance = Math.round(chance
                    * housing.getMutationModifier(genome0,
                    genome1, chance)
                    * BeeManager.beeRoot.getBeekeepingMode(housing.getWorld())
                    .getMutationModifier(genome0,
                            genome1, chance));
        }

        return finalChance;
    }

}
