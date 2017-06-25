package semexe.record;

import java.util.Collections;
import java.util.List;

/**
 * A record node with exactly at most one child.
 */
public class TwigRecordNode extends AbstractRecordNode {
    private RecordNode child;

    public TwigRecordNode(String key, String value) {
        super(key, value);
    }

    public TwigRecordNode(String key, String value, RecordNode child) {
        super(key, value);
        this.child = child;
    }

    public List<RecordNode> getChildren() {
        if (child == null) return Collections.emptyList();
        return Collections.singletonList(child);
    }

    public void addChild(RecordNode node) {
        child = node;
    }

    public RecordNode shallowCopy(String key, String value) {
        return new TwigRecordNode(key, value, child);
    }

    protected RecordNode getChild() {
        return child;
    }
}
