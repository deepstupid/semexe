package semexe;

import semexe.basic.LispTree;
import semexe.basic.LogInfo;

/**
 * Represents a logical predicate.
 *
 * @author Percy Liang
 */
public class NameValue extends Value {
    public final String id;  // Identifier (e.g., "fb:en.barack_obama")
    public final String description;  // Readable description (e.g., "Barack Obama")

    public NameValue(LispTree tree) {
        this.id = tree.child(1).value;
        this.description = tree.children.size() > 2 ? tree.child(2).value : null;
        assert (this.id != null) : tree;
    }

    public NameValue(String id) {
        this(id, null);
    }

    public NameValue(String id, String description) {
        if (id == null) {
            LogInfo.errors("Got null id, description is %s", description);
            id = "fb:en.null";
        }
        this.id = id;
        this.description = description;
    }

    public LispTree tree() {
        LispTree tree = LispTree.proto.newList();
        tree.addChild("name");
        tree.addChild(id);
        if (description != null) tree.addChild(description);
        return tree;
    }

    @Override
    public String sortString() {
        return id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NameValue)) return false;
        NameValue that = (NameValue) o;
        // Note: only check id, not description
        return this.id.equals(that.id);
    }
}
