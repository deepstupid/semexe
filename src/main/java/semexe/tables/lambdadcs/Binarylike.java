package semexe.tables.lambdadcs;

import semexe.Value;
import semexe.basic.LispTree;

/**
 * Represents a BinaryDenotation.
 * <p>
 * By LambdaDCS convention, (v <= k) means that k maps to v.
 * <p>
 * The following operations must be handled:
 * - Reverse
 * - reverse(BL)
 * - Compose
 * - Join: BL.UL
 * - Superlative: argmax(UL, BL), ...
 *
 * @author ppasupat
 */
public interface Binarylike {

    LispTree toLispTree();

    Value toValue();

    /**
     * Return all (y <= x) such that (x <= y) is in this binary
     */
    Binarylike reverse();

    /**
     * Return all v such that for some k in keys, (v <= k) is in this binary
     */
    UnaryDenotation joinOnKey(UnaryDenotation keys);

    /**
     * Return all k such that for some v in values, (v <= k) is in this binary
     */
    UnaryDenotation joinOnValue(UnaryDenotation values);

    /**
     * Return all (v <= k) such that k is in keys and (v <= k) is in this binary
     */
    BinaryDenotation<ExplicitPairList> explicitlyFilterOnKey(UnaryDenotation keys);

    /**
     * Return all (v <= k) such that v in values and (v <= k) is in this binary
     */
    BinaryDenotation<ExplicitPairList> explicitlyFilterOnValue(UnaryDenotation values);

}
