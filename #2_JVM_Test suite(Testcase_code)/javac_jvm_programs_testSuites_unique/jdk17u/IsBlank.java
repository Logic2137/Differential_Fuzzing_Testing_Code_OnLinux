import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IsBlank {

    public static void main(String... arg) {
        testIsBlank();
        testWhitespace();
    }

    static void testIsBlank() {
        test("", true);
        test(" ", true);
        test(" \t", true);
        test("  \u1680", true);
        test("   abc   ", false);
        test("   abc\u2022", false);
    }

    static void testWhitespace() {
        StringBuilder sb = new StringBuilder(64);
        IntStream.range(1, 0xFFFF).filter(c -> Character.isWhitespace(c)).forEach(c -> sb.append((char) c));
        String whiteSpace = sb.toString();
        test(whiteSpace, true);
        test(whiteSpace + "abc" + whiteSpace, false);
    }

    static void test(String input, boolean expected) {
        if (input.isBlank() != expected) {
            System.err.format("Failed test, Input: %s, Expected: %b%n", input, expected);
            throw new RuntimeException();
        }
    }
}
