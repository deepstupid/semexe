package semexe.prob;

import semexe.basic.TDoubleMap;

/**
 * See SparseDirichlet and DegenerateSparseDirichlet.
 */
public interface SparseDirichletInterface extends Distrib<TDoubleMap> {
    int dim();

    double getConcentration(Object key);

    double totalCount();

    double getMean(Object key);

    double getMode(Object key);

    double expectedLog(Object key);

    SparseDirichletInterface modeSpike();
}
