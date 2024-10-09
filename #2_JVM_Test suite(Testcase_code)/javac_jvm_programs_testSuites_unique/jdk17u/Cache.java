import java.util.LinkedHashMap;
import java.util.Map;

public class Cache {

    private static final int MAP_SIZE = 10;

    private static final int NUM_KEYS = 100;

    public static void main(String[] args) throws Exception {
        Map m = new LinkedHashMap() {

            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > MAP_SIZE;
            }
        };
        for (int i = 0; i < NUM_KEYS; i++) {
            m.put(new Integer(i), "");
            int eldest = ((Integer) m.keySet().iterator().next()).intValue();
            if (eldest != Math.max(i - 9, 0))
                throw new RuntimeException("i = " + i + ", eldest = " + eldest);
        }
    }
}
