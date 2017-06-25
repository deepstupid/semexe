package semexe.prob;

/**
 * Represents all the information about a set of data points that the
 * likelihood model needs in order to evaluate the likelihood of the data.
 */
public interface SuffStats {
    void add(SuffStats suffStats);

    void sub(SuffStats suffStats);

    SuffStats reweight(double scale);
}
