package semexe.cache;

import java.util.Map;

/**
 * @author Roy Frostig
 */
public interface LruCallback<K, V> {
    void onEvict(Map.Entry<K, V> entry);
}
