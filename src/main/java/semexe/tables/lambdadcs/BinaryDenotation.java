package semexe.tables.lambdadcs;

import semexe.PairListValue;
import semexe.basic.LispTree;

/**
 * Binary denotation: a mapping from value to values.
 * <p>
 * Share the implementation with MappingDenotation by using PairList.
 *
 * @author ppasupat
 */
public class BinaryDenotation<PL extends PairList> implements Binarylike {

    protected final PL pairList;

    public BinaryDenotation(PL pairList) {
        this.pairList = pairList;
    }

    @Override
    public String toString() {
        return toLispTree().toString();
    }

    @Override
    public LispTree toLispTree() {
        return LispTree.proto.newList("binary", pairList.toLispTree());
    }

    @Override
    public PairListValue toValue() {
        return pairList.toValue();
    }

    public MappingDenotation<PL> asMapping(String domainVar) {
        return new MappingDenotation<>(domainVar, pairList);
    }

    @Override
    public Binarylike reverse() {
        return new BinaryDenotation<>(pairList.reverse());
    }

    @Override
    public UnaryDenotation joinOnKey(UnaryDenotation keys) {
        return pairList.joinOnKey(keys);
    }

    @Override
    public UnaryDenotation joinOnValue(UnaryDenotation values) {
        return pairList.joinOnValue(values);
    }

    @Override
    public BinaryDenotation<ExplicitPairList> explicitlyFilterOnKey(UnaryDenotation keys) {
        return new BinaryDenotation<>(pairList.explicitlyFilterOnKey(keys));
    }

    @Override
    public BinaryDenotation<ExplicitPairList> explicitlyFilterOnValue(UnaryDenotation values) {
        return new BinaryDenotation<>(pairList.explicitlyFilterOnValue(values));
    }

}
