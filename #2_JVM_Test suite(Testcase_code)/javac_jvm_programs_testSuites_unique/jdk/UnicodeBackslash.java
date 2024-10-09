



public class UnicodeBackslash {
    static boolean failed = false;

    public static void main(String... args) {
        
        test("1.1", "\\]",                       "\\]");
        test("1.2", "\u005C\]",                  "\\]");
        test("1.3", "\\u005C]",                  "\\u005C]");
        test("1.4", "\u005C\u005C]",             "\\]");

        test("2.1", "\\\\]",                     "\\\\]");
        test("2.2", "\u005C\\\]",                "\\\\]");
        test("2.3", "\\u005C\\]",                "\\u005C\\]");
        test("2.4", "\\\u005C\]",                "\\\\]");
        test("2.5", "\\\\u005C]",                "\\\\u005C]");

        test("3.1", "\u005C\u005C\\]",           "\\\\]");
        test("3.2", "\u005C\\u005C\]",           "\\\\]");
        test("3.3", "\u005C\\\u005C]",           "\\\\u005C]");
        test("3.4", "\\u005C\u005C\]",           "\\u005C\\]");
        test("3.5", "\\u005C\\u005C]",           "\\u005C\\u005C]");
        test("3.6", "\\\u005C\u005C]",           "\\\\]");

        test("4.1", "\u005C\u005C\u005C\]",      "\\\\]");
        test("4.2", "\u005C\\u005C\u005C]",      "\\\\]");
        test("4.3", "\u005C\u005C\\u005C]",      "\\\\u005C]");
        test("4.4", "\\u005C\u005C\u005C]",      "\\u005C\\]");

        test("5.1", "\u005C\u005C\u005C\u005C]", "\\\\]");

        if (failed) {
            throw new RuntimeException("Unicode escapes not handled correctly");
        }
    }

    static void test(String id, String source, String expected) {
        if (!source.equals(expected)) {
            System.err.println(id + ": expected: " +  expected + ", found: " + source);
            failed = true;
        }
    }
}
