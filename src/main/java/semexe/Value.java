package semexe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import semexe.basic.LispTree;
import semexe.basic.LogInfo;

import java.util.Comparator;

/**
 * Values represent denotations (or partial denotations).
 *
 * @author Percy Liang
 */
public abstract class Value {
    @JsonCreator
    public static Value fromString(String str) {
        return Values.fromLispTree(LispTree.proto.tree(str));
    }

    public abstract LispTree tree();

    // Print using LogInfo.
    public void log() {
        LogInfo.logs("%s", toString());
    }

    @JsonValue
    public String toString() {
        return tree().toString();
    }

    // (optional) String used for sorting Values. The default is to call toString()
    public String sortString() {
        return toString();
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    public static class ValueComparator implements Comparator<Value> {
        @Override
        public int compare(Value o1, Value o2) {
            return o1.toString().compareTo(o2.toString());
        }
    }
}
