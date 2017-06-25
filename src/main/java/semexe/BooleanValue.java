package semexe;

import semexe.basic.LispTree;

/**
 * Represents a boolean.
 *
 * @author Percy Liang
 **/
public class BooleanValue extends Value {
    public final boolean value;

    public BooleanValue(boolean value) {
        this.value = value;
    }

    public BooleanValue(LispTree tree) {
        this.value = Boolean.parseBoolean(tree.child(1).value);
    }

    public LispTree tree() {
        LispTree tree = LispTree.proto.newList();
        tree.addChild("boolean");
        tree.addChild(String.valueOf(value));
        return tree;
    }

    @Override
    public int hashCode() {
        return Boolean.valueOf(value).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BooleanValue that = (BooleanValue) o;
        return this.value == that.value;
    }
}
