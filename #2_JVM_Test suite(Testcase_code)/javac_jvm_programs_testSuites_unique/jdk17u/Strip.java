import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Strip {

    public static void main(String... arg) {
        testStrip();
        testWhitespace();
    }

    static void testStrip() {
        equal("   abc   ".strip(), "abc");
        equal("   abc   ".stripLeading(), "abc   ");
        equal("   abc   ".stripTrailing(), "   abc");
        equal("   abc\u2022   ".strip(), "abc\u2022");
        equal("   abc\u2022   ".stripLeading(), "abc\u2022   ");
        equal("   abc\u2022   ".stripTrailing(), "   abc\u2022");
        equal("".strip(), "");
        equal("".stripLeading(), "");
        equal("".stripTrailing(), "");
        equal("\b".strip(), "\b");
        equal("\b".stripLeading(), "\b");
        equal("\b".stripTrailing(), "\b");
    }

    static void testWhitespace() {
        StringBuilder sb = new StringBuilder(64);
        IntStream.range(1, 0xFFFF).filter(c -> Character.isWhitespace(c)).forEach(c -> sb.append((char) c));
        String whiteSpace = sb.toString();
        String testString = whiteSpace + "abc" + whiteSpace;
        equal(testString.strip(), "abc");
        equal(testString.stripLeading(), "abc" + whiteSpace);
        equal(testString.stripTrailing(), whiteSpace + "abc");
    }

    static void report(String message, String inputTag, String input, String outputTag, String output) {
        System.err.println(message);
        System.err.println();
        System.err.println(inputTag);
        System.err.println(input.codePoints().mapToObj(c -> (Integer) c).collect(Collectors.toList()));
        System.err.println();
        System.err.println(outputTag);
        System.err.println(output.codePoints().mapToObj(c -> (Integer) c).collect(Collectors.toList()));
        throw new RuntimeException();
    }

    static void equal(String input, String expected) {
        if (input == null || expected == null || !expected.equals(input)) {
            report("Failed equal", "Input:", input, "Expected:", expected);
        }
    }
}
