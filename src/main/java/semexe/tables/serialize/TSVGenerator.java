package semexe.tables.serialize;


import semexe.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Generate a TSV file for the dataset release.
 *
 * @author ppasupat
 */
public class TSVGenerator {
    protected PrintWriter out;

    protected static String serialize(String x) {
        if (x == null || x.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (char y : x.toCharArray()) {
            if (y == '\n') sb.append("\\n");
            else if (y == '\\') sb.append("\\\\");
            else if (y == '|') sb.append("\\p");
            else sb.append(y);
        }
        return sb.toString().replaceAll("\\s", " ").trim();
    }

    protected static String serialize(List<String> xs) {
        List<String> serialized = new ArrayList<>();
        for (String x : xs) serialized.add(serialize(x));
        return String.join("|", serialized);
    }

    protected static String serialize(Value value) {
        if (value instanceof ListValue) {
            List<String> xs = new ArrayList<>();
            for (Value v : ((ListValue) value).values) {
                xs.add(serialize(v));
            }
            return String.join("|", xs);
        } else if (value instanceof DescriptionValue) {
            return serialize(((DescriptionValue) value).value);
        } else if (value instanceof NameValue) {
            return serialize(((NameValue) value).description);
        } else if (value instanceof NumberValue) {
            return String.valueOf(((NumberValue) value).value);
        } else if (value instanceof DateValue) {
            return ((DateValue) value).isoString();
        } else {
            throw new RuntimeException("Unknown value type: " + value);
        }
    }

    protected static String serializeId(Value value) {
        if (value instanceof ListValue) {
            List<String> xs = new ArrayList<>();
            for (Value v : ((ListValue) value).values) {
                xs.add(serializeId(v));
            }
            return String.join("|", xs);
        } else if (value instanceof NameValue) {
            return serialize(((NameValue) value).id);
        } else {
            throw new RuntimeException("Unknown value type: " + value);
        }
    }

    protected void dump(String... stuff) {
        out.println(String.join("\t", stuff));
    }
}
