import java.util.*;

public class ComputeIfAbsentAccessOrder {

    public static void main(String[] args) throws Throwable {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>(2, 0.75f, true);
        map.put("first", null);
        map.put("second", null);
        map.computeIfAbsent("first", l -> null);
        String key = map.keySet().stream().findFirst().orElseThrow(() -> new RuntimeException("no value"));
        if (!"first".equals(key)) {
            throw new RuntimeException("not expected value " + "first" + "!=" + key);
        }
    }
}
