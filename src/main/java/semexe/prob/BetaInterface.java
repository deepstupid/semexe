package semexe.prob;

/**
 * See Beta and DegenerateBeta for implementations.
 */
public interface BetaInterface extends Distrib<Double> {
    double expectedLog(boolean b);

    double getAlpha();

    double getBeta();

    double getMean();

    double getMode();

    double totalCount();

    BetaInterface modeSpike();
}
