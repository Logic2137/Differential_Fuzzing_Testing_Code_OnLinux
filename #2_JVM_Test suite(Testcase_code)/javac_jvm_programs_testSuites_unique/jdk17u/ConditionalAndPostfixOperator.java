import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ConditionalAndPostfixOperator {

    public static void main(String... args) {
        Map<String, Integer> m = new HashMap<>();
        String key = "a";
        m.put(key, val());
        assertEquals(2, m.compute(key, (k, v) -> (v > 5) ? v-- : v++));
        Integer v = val();
        assertEquals(2, (v > 5) ? v-- : v++);
        assertEquals(3, v);
    }

    static void assertEquals(Integer expected, Integer actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected: " + expected + ", " + "actual: " + actual);
        }
    }

    static int val() {
        return 2;
    }
}
