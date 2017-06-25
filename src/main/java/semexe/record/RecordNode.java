package semexe.record;

import java.util.List;

/**
 * The RecordNode is the basic building block of the entire framework.
 * Almost everything is a RecordNode.
 */
public interface RecordNode {
    // Basic functionality
    String getKey();

    String getValue();

    double getDoubleValue(); // For sorting

    String getDescription(DescriptionType type);

    // Functionality concerning children
    // Return collection of keys that children use
    List<RecordNode> getChildren();

    List<RecordNode> getChildren(String key);

    int numChildren();

    int numChildren(String key);

    void addChild(RecordNode child);

    // Return a copy of the current node, but replacing key and value,
    // but keeping a reference to the same children.
    RecordNode shallowCopy(String key, String value);

    // Return a copy of the record node, but without the children
    RecordNode withoutChildren();

    // strict is machine readable
    enum DescriptionType {
        human, machine
    }
}
