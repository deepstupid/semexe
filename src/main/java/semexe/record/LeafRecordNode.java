package semexe.record;

import java.util.Collections;
import java.util.List;

/**
 * A record node without children.
 */
public class LeafRecordNode extends AbstractRecordNode {
    public static final LeafRecordNode nullNode = new LeafRecordNode(null, null);

    public LeafRecordNode(String key, String value) {
        super(key, value);
    }

    public List<RecordNode> getChildren() {
        return Collections.emptyList();
    }

    public void addChild(RecordNode node) {
    }

    public RecordNode shallowCopy(String key, String value) {
        return new LeafRecordNode(key, value);
    }
}
