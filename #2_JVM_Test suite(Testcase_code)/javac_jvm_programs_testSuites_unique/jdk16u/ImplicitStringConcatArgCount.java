


public class ImplicitStringConcatArgCount {
    static final String s = "f";
    static final String s1 = "o";
    static String s2 = "o";
    static int i = 7;

    public static void main(String[] args) throws Exception {
        test("fo",          s + s1);
        test("foo",         s + s1 + s2);
        test("foo7",        s + s1 + s2 + i);
        test("foo77",       s + s1 + s2 + i + i);
        test("foo777",      s + s1 + s2 + i + i + i);
        test("foo7777",     s + s1 + s2 + i + i + i + i);
        test("foo77777",    s + s1 + s2 + i + i + i + i + i);
        test("foo777777",   s + s1 + s2 + i + i + i + i + i + i);
        test("foo7777777",  s + s1 + s2 + i + i + i + i + i + i + i);
        test("foo77777777", s + s1 + s2 + i + i + i + i + i + i + i + i);
    }

    public static void test(String expected, String actual) {
       if (!expected.equals(actual)) {
           StringBuilder sb = new StringBuilder();
           sb.append("Expected = ");
           sb.append(expected);
           sb.append(", actual = ");
           sb.append(actual);
           throw new IllegalStateException(sb.toString());
       }
    }
}
