package semexe.tables.lambdadcs;

import semexe.AggregateFormula;
import semexe.Value;
import semexe.basic.LispTree;

/**
 * Represents a UnaryDenotation or a MappingDenotation.
 * <p>
 * The following operations must be handled:
 * - Map
 * - count(UL)
 * - Aggregate: sum(UL), ...
 * - Combine
 * - Merge: and(UL1, UL2), ...
 * - Arithmetic: sub(UL1, UL2), ...
 * <p>
 * Compose operations (join, superlative) are handled in BL.
 *
 * @author ppasupat
 */
public interface Unarylike {

    LispTree toLispTree();

    Value toValue();

    /**
     * Return the name of the free variable.
     */
    String getDomainVar();

    /**
     * List of possible variable assignments
     */
    UnaryDenotation domain();

    /**
     * List of possible values.
     */
    UnaryDenotation range();

    /**
     * |key| => ???
     */
    UnaryDenotation get(Value key);

    /**
     * ??? => |value|
     */
    UnaryDenotation inverseGet(Value value);

    /**
     * count and other aggregate operations
     */
    Unarylike aggregate(AggregateFormula.Mode mode);

    /**
     * Return a new Unarylike where only the values found in |upperBound|
     * and domain values found in |domainUpperBound| are kept
     */
    Unarylike filter(UnaryDenotation upperBound, UnaryDenotation domainUpperBound);
}
