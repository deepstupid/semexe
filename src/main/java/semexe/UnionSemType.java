package semexe;

import semexe.basic.LispTree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Represents the union of a set of base types.
public class UnionSemType extends SemType {
    public final List<SemType> baseTypes;

    // Constructors
    public UnionSemType() {
        this.baseTypes = new ArrayList<>();
    }

    public UnionSemType(SemType... baseTypes) {
        this.baseTypes = new ArrayList<>();
        for (SemType baseType : baseTypes)
            if (baseType.isValid())
                this.baseTypes.add(baseType);
    }

    public UnionSemType(Collection<SemType> baseTypes) {
        this.baseTypes = new ArrayList<>();
        for (SemType baseType : baseTypes)
            if (baseType.isValid())
                this.baseTypes.add(baseType);
    }

    public boolean isValid() {
        return baseTypes.size() > 0;
    }

    public SemType meet(SemType that) {
        if (that instanceof TopSemType) return this;
        List<SemType> result = new ArrayList<>();
        for (SemType baseType : baseTypes)
            result.add(baseType.meet(that));
        return new UnionSemType(result).simplify();
    }

    public SemType apply(SemType that) {
        List<SemType> result = new ArrayList<>();
        for (SemType baseType : baseTypes)
            result.add(baseType.apply(that));
        return new UnionSemType(result).simplify();
    }

    public SemType reverse() {
        List<SemType> result = new ArrayList<>();
        for (SemType baseType : baseTypes)
            result.add(baseType.reverse());
        return new UnionSemType(result).simplify();
    }

    public LispTree toLispTree() {
        LispTree result = LispTree.proto.newList();
        result.addChild("union");
        for (SemType baseType : baseTypes)
            result.addChild(baseType.toLispTree());
        return result;
    }

    public SemType simplify() {
        if (baseTypes.size() == 0) return bottomType;
        if (baseTypes.size() == 1) return baseTypes.get(0);
        if (baseTypes.contains(topType)) return topType;
        return this;
    }
}
