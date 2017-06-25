package edu.stanford.nlp.sempre;

/**
 * A feature computer.
 * <p>
 * Look at a derivation and add features to the feature vector.
 * A FeatureComputer should be stateless.
 * <p>
 * Before computing features, a FeatureComputer should call
 * <p>
 * if (!FeatureExtractor.containsDomain(...)) return;
 * <p>
 * to check the feature domain first.
 */
public interface FeatureComputer {

    /**
     * This function is called on every sub-Derivation.
     * <p>
     * It should extract only the features which depend in some way on |deriv|,
     * not just on its children.
     */
    void extractLocal(Example ex, Derivation deriv);

}
