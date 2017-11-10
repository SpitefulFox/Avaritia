package fox.spiteful.avaritia.compat.forestry;

import forestry.api.genetics.IAlleleFloat;
import forestry.api.genetics.IAlleleInteger;

public class AlleleInteger extends Allele implements IAlleleInteger {

    private int value;

    public AlleleInteger(String moniker, boolean dom, int val){
        super(moniker, dom);
        value = val;
    }

    @Override
    public int getValue(){
        return value;
    }
}