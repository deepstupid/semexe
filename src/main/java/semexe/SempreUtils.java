package semexe;

import semexe.basic.LogInfo;
import semexe.basic.MapUtils;

import java.util.Map;

/**
 * Created by joberant on 10/18/14.
 */
public final class SempreUtils {
    private SempreUtils() {
    }

    // "java.util.ArrayList" => "java.util.ArrayList"
    // "TypeLookup" => "TypeLookup"
    public static String resolveClassName(String name) {
        if (name.startsWith("edu.") || name.startsWith("org.") ||
                name.startsWith("com.") || name.startsWith("net."))
            return name;
        return "semexe." + name;
    }

    public static <K, V> void logMap(Map<K, V> map, String desc) {
        LogInfo.begin_track("Logging %s map", desc);
        for (Map.Entry<K, V> kvEntry : map.entrySet())
            LogInfo.log(kvEntry.getKey() + "\t" + kvEntry.getValue());
        LogInfo.end_track();
    }

    public static void addToDoubleMap(Map<String, Double> mutatedMap, Map<String, Double> addedMap) {
        for (Map.Entry<String, Double> stringDoubleEntry : addedMap.entrySet())
            MapUtils.incr(mutatedMap, (String) stringDoubleEntry.getKey(), stringDoubleEntry.getValue());
    }
}
