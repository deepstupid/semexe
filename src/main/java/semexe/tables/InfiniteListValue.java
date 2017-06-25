package semexe.tables;

import semexe.Value;
import semexe.Values;
import semexe.basic.LispTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a list of infinitely many values.
 * <p>
 * The list is represented by a List of Objects.
 *
 * @author ppasupat
 */
public class InfiniteListValue extends Value {

    final List<Object> representation;
    final int hashCode;

    public InfiniteListValue(List<Object> representation) {
        this.representation = representation;
        this.hashCode = representation.hashCode();
    }

    public InfiniteListValue(String s) {
        this(LispTree.proto.tree(s));
    }

    public InfiniteListValue(LispTree tree) {
        this.representation = new ArrayList<>();
        for (LispTree child : tree.children) {
            try {
                Value value = Values.fromLispTree(child);
                representation.add(value);
            } catch (Exception e) {
                representation.add(child.toString());
            }
        }
        this.hashCode = representation.hashCode();
    }

    @Override
    public LispTree tree() {
        LispTree tree = LispTree.proto.newList();
        for (Object x : representation) {
            if (x instanceof Value)
                tree.addChild(((Value) x).tree());
            else
                tree.addChild(x.toString());
        }
        return tree;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfiniteListValue that = (InfiniteListValue) o;
        return representation.equals(that.representation);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

}
