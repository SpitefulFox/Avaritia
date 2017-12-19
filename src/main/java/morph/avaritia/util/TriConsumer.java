package morph.avaritia.util;

/**
 * Created by covers1624 on 11/10/2017.
 */
@FunctionalInterface
public interface TriConsumer<L, M, R> {

    void accept(L l, M m, R r);

}
