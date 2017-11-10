package fox.spiteful.avaritia.compat.forestry;

import forestry.api.genetics.IAlleleFloat;

public class AlleleFloat extends Allele implements IAlleleFloat {

    private float value;

    public AlleleFloat(String moniker, boolean dom, float val){
        super(moniker, dom);
        value = val;
    }

    @Override
    public float getValue(){
        return value;
    }
}