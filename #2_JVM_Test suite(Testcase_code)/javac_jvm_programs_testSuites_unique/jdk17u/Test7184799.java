import java.beans.Introspector;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Test7184799 {

    private static final Class[] TYPES = { Class.class, String.class, Character.class, Boolean.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Collection.class, Set.class, HashSet.class, TreeSet.class, LinkedHashSet.class, Map.class, HashMap.class, TreeMap.class, LinkedHashMap.class, WeakHashMap.class, ConcurrentHashMap.class, Dictionary.class, Exception.class };

    public static void main(String[] args) throws Exception {
        long time = System.nanoTime();
        for (Class type : TYPES) {
            Introspector.getBeanInfo(type);
        }
        time -= System.nanoTime();
        System.out.println("Time (ms): " + (-time / 1000000));
    }
}
