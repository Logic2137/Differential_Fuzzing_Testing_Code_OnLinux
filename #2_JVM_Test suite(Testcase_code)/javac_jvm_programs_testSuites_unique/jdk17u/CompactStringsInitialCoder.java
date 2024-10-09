import java.lang.StringBuilder;

public class CompactStringsInitialCoder {

    static String strEmpty = "";

    static String strLatin1 = "\u0042";

    static String strUTF16 = "\u4242";

    static char charLatin1 = '\u0042';

    static char charUTF16 = '\u4242';

    public static void main(String[] args) throws Exception {
        test("\u0042", "" + '\u0042');
        test("\u4242", "" + '\u4242');
        test("\u0042", "" + charLatin1);
        test("\u4242", "" + charUTF16);
        test("\u0042", strEmpty + '\u0042');
        test("\u4242", strEmpty + '\u4242');
        test("\u0042\u0042", strLatin1 + '\u0042');
        test("\u0042\u4242", strLatin1 + '\u4242');
        test("\u4242\u0042", strUTF16 + '\u0042');
        test("\u4242\u4242", strUTF16 + '\u4242');
        test("\u0042\u0042", "\u0042" + charLatin1);
        test("\u0042\u4242", "\u0042" + charUTF16);
        test("\u4242\u0042", "\u4242" + charLatin1);
        test("\u4242\u4242", "\u4242" + charUTF16);
        test("\u0042\u0042", "" + charLatin1 + charLatin1);
        test("\u0042\u4242", "" + charLatin1 + charUTF16);
        test("\u4242\u0042", "" + charUTF16 + charLatin1);
        test("\u4242\u4242", "" + charUTF16 + charUTF16);
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
