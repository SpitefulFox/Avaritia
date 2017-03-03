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
        IAllele first;
        IAllele second;
        if(Ranger.extra)
            first = Allele.getExtraSpecies("Relic");
        else
            first = Allele.getBaseSpecies("Austere");
        second = Allele.getBaseSpecies("Hermitic");
        new ExpensiveMutation(first, second, Genomes.getBalanced(), 0.8f);
        if(Ranger.magic)
            first = Allele.getMagicSpecies("Draconic");
        else
            first = Allele.getBaseSpecies("Heroic");
        new ExpensiveMutation(first, GreedyBeeSpecies.ANNOYING, Genomes.getTedious(), 0.7f);
        if(Ranger.extra)
            first = Allele.getExtraSpecies("Virulent");
        else if(Ranger.magic)
            first = Allele.getMagicSpecies("Withering");
        else
            first = Allele.getBaseSpecies("Demonic");
        new ExpensiveMutation(first, GreedyBeeSpecies.TEDIOUS, Genomes.getInsufferable(), 0.6f);

        new ExpensiveMutation(Allele.getBaseSpecies("Edenic"), GreedyBeeSpecies.INSUFFERABLE, Genomes.getInfinite(), 0.4f);
        if(Ranger.magic)
            first = Allele.getMagicSpecies("Doctoral");
        else if(Ranger.extra)
            first = Allele.getExtraSpecies("Diamond");
        else
            first = Allele.getBaseSpecies("Phantasmal");

        new ExpensiveMutation(first, GreedyBeeSpecies.TRIPPY, Genomes.getCosmic(), 0.8f);

        if(Ranger.magic)
            first = Allele.getMagicSpecies("Firey");
        else if(Ranger.extra)
            first = Allele.getExtraSpecies("Volcanic");
        else
            first = Allele.getBaseSpecies("Industrious");
        //new ExpensiveMutation(first, GreedyBeeSpecies.COSMIC, Genomes.getNeutronium(), 0.6f);
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
