package edu.stanford.nlp.sempre.tables.lambdadcs;

import edu.stanford.nlp.sempre.AggregateFormula;
import edu.stanford.nlp.sempre.PairListValue;
import edu.stanford.nlp.sempre.Value;
import fig.basic.LispTree;

public interface PairList {

    // ============================================================
    // Representation
    // ============================================================

    String toString();

    LispTree toLispTree();

    PairListValue toValue();

    // ============================================================
    // Getter
    // ============================================================

    UnaryDenotation domain();

    UnaryDenotation range();

    UnaryDenotation get(Value key);

    UnaryDenotation inverseGet(Value value);

    // ============================================================
    // Operations
    // ============================================================

    PairList aggregate(AggregateFormula.Mode mode);

    PairList filter(UnaryDenotation upperBound, UnaryDenotation domainUpperBound);

    PairList reverse();

    UnaryDenotation joinOnKey(UnaryDenotation keys);

    UnaryDenotation joinOnValue(UnaryDenotation values);

    ExplicitPairList explicitlyFilterOnKey(UnaryDenotation keys);

    ExplicitPairList explicitlyFilterOnValue(UnaryDenotation values);

}
