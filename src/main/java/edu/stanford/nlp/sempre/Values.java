package edu.stanford.nlp.sempre;

import fig.basic.LispTree;

/**
 * Utilities for Value.
 *
 * @author Percy Liang
 */
public final class Values {
    private Values() {
    }

    // Try to parse the LispTree into a value.
    // If it fails, just return null.
    public static Value fromLispTreeOrNull(LispTree tree) {
        if (tree.isLeaf())
            return null;
        String type = tree.child(0).value;
        if (type == null)
            return null;

        switch (type) {
            case "name":
                return new NameValue(tree);
            case "boolean":
                return new BooleanValue(tree);
            case "number":
                return new NumberValue(tree);
            case "string":
                return new StringValue(tree);
            case "list":
                return new ListValue(tree);
            case "table":
                return new TableValue(tree);
            case "description":
                return new DescriptionValue(tree);
            case "url":
                return new UriValue(tree);
            case "context":
                return new ContextValue(tree);
            case "date":
                return new DateValue(tree);
            case "error":
                return new ErrorValue(tree);
            case "time":
                return new TimeValue(tree);
        }
        return null;
    }

    // Try to parse.  If it fails, throw an exception.
    public static Value fromLispTree(LispTree tree) {
        Value value = fromLispTreeOrNull(tree);
        if (value == null)
            throw new RuntimeException("Invalid value: " + tree);
        return value;
    }

    public static Value fromString(String s) {
        return fromLispTree(LispTree.proto.parseFromString(s));
    }
}
