import java.util.TreeMap;

public class Clone {

    public static void main(String[] args) throws Exception {
        TreeMap<String, Object> m1 = new TreeMap<String, Object>();
        m1.put("one", 1);
        m1.keySet();
        TreeMap<String, Object> m2 = (TreeMap<String, Object>) m1.clone();
        m1.put("two", 2);
        m2.put("three", 3);
        for (final String key : m2.keySet()) {
            if (!"one".equals(key) && !"three".equals(key)) {
                throw new IllegalStateException("Unexpected key: " + key);
            }
        }
        for (final String key : m1.keySet()) {
            if (!"one".equals(key) && !"two".equals(key)) {
                throw new IllegalStateException("Unexpected key: " + key);
            }
        }
    }
}
