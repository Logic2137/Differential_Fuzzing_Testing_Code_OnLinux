



import java.util.function.BiFunction;

public class T8188144 {
    public static void main(String[] args) {
        BiFunction<String, String, String> format = String::format;
        if (!format.apply("foo %s", "bar").endsWith("foo bar")) {
            throw new AssertionError("Unexpected output!");
        }
    }
}
