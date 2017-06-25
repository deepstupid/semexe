package semexe;

import semexe.FuzzyMatchFn.FuzzyMatchFnMode;

import java.util.Collection;
import java.util.List;

/**
 * Interface for knowledge sources that, given a phrase, can retrieve all its
 * predicates that (fuzzily) match the phrase.
 *
 * @author ppasupat
 */
public interface FuzzyMatchable {

    /**
     * Return all entities / unaries / binaries that approximately match the
     * string formed by joining sentence[startIndex], ..., sentence[endIndex-1]
     * with spaces.
     * <p>
     * This allows the algorithm to consider the context of the term being matched.
     * <p>
     * One possible implementation, which ignores the context, is calling
     * getFuzzyMatchedFormulas(term, mode) where
     * term = String.join(" ", sentence.subList(startIndex, endIndex))
     */
    Collection<Formula> getFuzzyMatchedFormulas(
            List<String> sentence, int startIndex, int endIndex, FuzzyMatchFnMode mode);

    /**
     * Return all entities / unaries / binaries that approximately match the term
     */
    Collection<Formula> getFuzzyMatchedFormulas(String term, FuzzyMatchFnMode mode);

    /**
     * Return all possible entities / unaries / binaries
     */
    Collection<Formula> getAllFormulas(FuzzyMatchFnMode mode);

}
