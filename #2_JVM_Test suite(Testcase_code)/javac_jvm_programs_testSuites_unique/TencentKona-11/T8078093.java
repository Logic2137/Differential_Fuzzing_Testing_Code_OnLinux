



import java.util.LinkedHashMap;
import java.util.Map;

class T8078093 {
    public static void test() {
        Map<Integer, String> a = x(x(x(x(x(x(x(x(x(x(x(x(
                                    new LinkedHashMap<Integer, String>(),
                                    1, "a"), 2, "b"), 3, "c"), 4, "d"),
                                    5, "e"), 6, "f"), 7, "g"), 8, "h"),
                                    9, "i"), 10, "j"), 11, "k"), 12, "l");
    }

    @SuppressWarnings("unused")
    public static <K, V> Map<K, V> x(Map<K, V> m, K k, V v) {
        
        return null;
    }
}
