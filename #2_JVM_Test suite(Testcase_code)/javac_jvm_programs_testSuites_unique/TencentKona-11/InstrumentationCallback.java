

package jdk.jfr.event.io;
import java.util.Set;
import java.util.HashSet;



public class InstrumentationCallback {

    private static Set<String> keys = new HashSet<String>();

    public static synchronized void callback(String key) {
        if (!keys.contains(key)) {
            keys.add(key);
        }
    }

    public static synchronized void clear() {
        keys.clear();
    }

    public static synchronized Set<String> getKeysCopy() {
        return new HashSet<String>(keys);
    }
}
