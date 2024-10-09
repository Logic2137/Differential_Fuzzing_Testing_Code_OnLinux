import java.util.EnumMap;
import java.util.Map;

public class UniqueNullValue {

    static enum TestEnum {

        e00, e01
    }

    public static void main(String[] args) {
        Map<TestEnum, Integer> map = new EnumMap<>(TestEnum.class);
        map.put(TestEnum.e00, 0);
        if (false == map.containsValue(0)) {
            throw new RuntimeException("EnumMap unexpectedly missing 0 value");
        }
        if (map.containsValue(null)) {
            throw new RuntimeException("EnumMap unexpectedly holds null value");
        }
        map.put(TestEnum.e00, null);
        if (map.containsValue(0)) {
            throw new RuntimeException("EnumMap unexpectedly holds 0 value");
        }
        if (false == map.containsValue(null)) {
            throw new RuntimeException("EnumMap unexpectedly missing null value");
        }
    }
}
