package semexe.prob;

/**
 * See Dirichlet and DegenerateDirichlet for implementations.
 */
public interface DirichletInterface extends Distrib<double[]> {
    int dim();

    double[] getMean();

    double[] getMode();

    double[] expectedLog();

    double expectedLog(int i);

    double getAlpha(int i);

    double totalCount();

    DirichletInterface modeSpike();
}
