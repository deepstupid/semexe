package semexe.record;

import java.util.ArrayList;
import java.util.List;

/**
 * A record node with explicit list of children.  Nothing fancy.
 */
public class FullRecordNode extends AbstractRecordNode {
    private List<RecordNode> children;

    public FullRecordNode(String key, String value) {
        super(key, value);
        this.children = new ArrayList<>();
    }

    public List<RecordNode> getChildren() {
        return children;
    }

    protected void setChildren(List<RecordNode> children) {
        this.children = children;
    }

    public void addChild(RecordNode child) {
        children.add(child);
    }

    protected void clearChildren() {
        children.clear();
    }

    public RecordNode shallowCopy(String key, String value) {
        FullRecordNode node = new FullRecordNode(key, value);
        node.setChildren(getChildren());
        return node;
    }
}
