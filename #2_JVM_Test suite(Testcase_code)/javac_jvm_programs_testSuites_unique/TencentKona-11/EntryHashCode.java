




import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class EntryHashCode {
    private static final int TEST_SIZE = 100;

    static final Object[][] entryData = {
        new Object[TEST_SIZE],
        new Object[TEST_SIZE]
    };

    @SuppressWarnings("unchecked")
    static final Map<Object,Object>[] maps = (Map<Object,Object>[])new Map[] {
        new HashMap<>(),
        new Hashtable<>(),
        new IdentityHashMap<>(),
        new LinkedHashMap<>(),
        new TreeMap<>(),
        new WeakHashMap<>(),
        new ConcurrentHashMap<>(),
        new ConcurrentSkipListMap<>()
    };

    static {
        for (int i = 0; i < entryData[0].length; i++) {
            
            entryData[0][i] = new Comparable<Object>() {
                public int compareTo(Object o) {
                    return (hashCode() - o.hashCode());
                }
            };
            entryData[1][i] = new Object();
        }
    }

    private static void addTestData(Map<Object,Object> map) {
        for (int i = 0; i < entryData[0].length; i++) {
            map.put(entryData[0][i], entryData[1][i]);
        }
    }

    public static void main(String[] args) throws Exception {
        Exception failure = null;
        for (Map<Object,Object> map: maps) {
            addTestData(map);

            try {
                for (Map.Entry<Object,Object> e: map.entrySet()) {
                    Object key = e.getKey();
                    Object value = e.getValue();
                    int expectedEntryHashCode =
                        (Objects.hashCode(key) ^ Objects.hashCode(value));

                    if (e.hashCode() != expectedEntryHashCode) {
                        throw new Exception("FAILURE: " +
                                e.getClass().getName() +
                                ".hashCode() does not conform to defined" +
                                " behaviour of java.util.Map.Entry.hashCode()");
                    }
                }
            } catch (Exception e) {
                if (failure == null) {
                    failure = e;
                } else {
                    failure.addSuppressed(e);
                }
            } finally {
                map.clear();
            }
        }
        if (failure != null) {
            throw failure;
        }
    }
}
