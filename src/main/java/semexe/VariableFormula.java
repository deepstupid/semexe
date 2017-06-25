package semexe;

import semexe.basic.LispTree;

/**
 * Corresponds to a variable reference.
 *
 * @author Percy Liang
 */
public class VariableFormula extends PrimitiveFormula {
    public final String name;  // Name of variable.

    public VariableFormula(String name) {
        this.name = name;
    }

    public LispTree toLispTree() {
        return LispTree.proto.newList("var", name);
    }

    @SuppressWarnings({"equalshashcode"})
    @Override
    public boolean equals(Object thatObj) {
        if (this == thatObj) return true;
        if (!(thatObj instanceof VariableFormula)) return false;
        VariableFormula that = (VariableFormula) thatObj;
        return this.name.equals(that.name);
    }

    public int computeHashCode() {
        return name.hashCode();
    }
}
