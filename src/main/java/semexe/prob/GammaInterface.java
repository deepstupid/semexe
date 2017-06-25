package semexe.prob;

public interface GammaInterface extends Distrib<Double> {
    double getShape();

    double getRate();

    double getMean();

    double getMode();

    double expectedLog();

    GammaInterface modeSpike();
}
