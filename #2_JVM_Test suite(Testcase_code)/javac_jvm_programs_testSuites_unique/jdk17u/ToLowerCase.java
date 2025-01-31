import java.util.Locale;

public class ToLowerCase {

    public static void main(String[] args) {
        Locale turkish = new Locale("tr", "TR");
        Locale lt = new Locale("lt");
        Locale az = new Locale("az");
        test("\u03A3", Locale.US, "\u03C3");
        test("LAST\u03A3", Locale.US, "last\u03C2");
        test("MID\u03A3DLE", Locale.US, "mid\u03C3dle");
        test("WORD1 \u03A3 WORD3", Locale.US, "word1 \u03C3 word3");
        test("WORD1 LAST\u03A3 WORD3", Locale.US, "word1 last\u03C2 word3");
        test("WORD1 MID\u03A3DLE WORD3", Locale.US, "word1 mid\u03C3dle word3");
        test("\u0399\u0395\u03a3\u03a5\u03a3 \u03a7\u03a1\u0399\u03a3\u03a4\u039f\u03a3", Locale.US, "\u03b9\u03b5\u03c3\u03c5\u03c2 \u03c7\u03c1\u03b9\u03c3\u03c4\u03bf\u03c2");
        test("I", lt, "i");
        test("I\u0300", lt, "i\u0307\u0300");
        test("I\u0316", lt, "i\u0316");
        test("J", lt, "j");
        test("J\u0300", lt, "j\u0307\u0300");
        test("J\u0316", lt, "j\u0316");
        test("\u012E", lt, "\u012F");
        test("\u012E\u0300", lt, "\u012F\u0307\u0300");
        test("\u012E\u0316", lt, "\u012F\u0316");
        test("\u00CC", lt, "i\u0307\u0300");
        test("\u00CD", lt, "i\u0307\u0301");
        test("\u0128", lt, "i\u0307\u0303");
        test("I\u0300", Locale.US, "i\u0300");
        test("J\u0300", Locale.US, "j\u0300");
        test("\u012E\u0300", Locale.US, "\u012F\u0300");
        test("\u00CC", Locale.US, "\u00EC");
        test("\u00CD", Locale.US, "\u00ED");
        test("\u0128", Locale.US, "\u0129");
        test("\u0130", turkish, "i");
        test("\u0130", az, "i");
        test("\u0130", lt, "\u0069\u0307");
        test("\u0130", Locale.US, "\u0069\u0307");
        test("\u0130", Locale.JAPAN, "\u0069\u0307");
        test("\u0130", Locale.ROOT, "\u0069\u0307");
        test("I\u0307", turkish, "i");
        test("I\u0307", az, "i");
        test("J\u0307", turkish, "j\u0307");
        test("J\u0307", az, "j\u0307");
        test("I", turkish, "\u0131");
        test("I", az, "\u0131");
        test("I", Locale.US, "i");
        test("IABC", turkish, "\u0131abc");
        test("IABC", az, "\u0131abc");
        test("IABC", Locale.US, "iabc");
        test("\uD801\uDC00\uD801\uDC01\uD801\uDC02", Locale.US, "\uD801\uDC28\uD801\uDC29\uD801\uDC2A");
        test("\uD801\uDC00A\uD801\uDC01B\uD801\uDC02C", Locale.US, "\uD801\uDC28a\uD801\uDC29b\uD801\uDC2Ac");
        test("\uD800\uD800\uD801A\uDC00\uDC00\uDC00B", Locale.US, "\uD800\uD800\uD801a\uDC00\uDC00\uDC00b");
        test("a\uD801\uDC1c", Locale.ROOT, "a\uD801\uDC44");
        test("A\uD801\uDC1c", Locale.ROOT, "a\uD801\uDC44");
        test("a\uD801\uDC00\uD801\uDC01\uD801\uDC02", Locale.US, "a\uD801\uDC28\uD801\uDC29\uD801\uDC2A");
        test("A\uD801\uDC00\uD801\uDC01\uD801\uDC02", Locale.US, "a\uD801\uDC28\uD801\uDC29\uD801\uDC2A");
        StringBuilder src = new StringBuilder(0x20000);
        StringBuilder exp = new StringBuilder(0x20000);
        for (int cp = 0; cp < 0x20000; cp++) {
            if (cp >= Character.MIN_HIGH_SURROGATE && cp <= Character.MAX_HIGH_SURROGATE) {
                continue;
            }
            if (cp == 0x0130) {
                continue;
            }
            int lowerCase = Character.toLowerCase(cp);
            if (lowerCase == -1) {
                continue;
            }
            src.appendCodePoint(cp);
            exp.appendCodePoint(lowerCase);
        }
        test(src.toString(), Locale.US, exp.toString());
        src = new StringBuilder(0x100);
        exp = new StringBuilder(0x100);
        for (int cp = 0; cp < 0x100; cp++) {
            int lowerCase = Character.toLowerCase(cp);
            if (lowerCase == -1) {
                continue;
            }
            src.appendCodePoint(cp);
            exp.appendCodePoint(lowerCase);
        }
        test(src.toString(), Locale.US, exp.toString());
        src = new StringBuilder(0x100).append("abc");
        exp = new StringBuilder(0x100).append("abc");
        for (int cp = 0x100; cp < 0x10000; cp++) {
            int lowerCase = Character.toLowerCase(cp);
            if (lowerCase < 0x100 && cp != '\u0130') {
                src.appendCodePoint(cp);
                exp.appendCodePoint(lowerCase);
            }
        }
        test(src.toString(), Locale.US, exp.toString());
    }

    static void test(String in, Locale locale, String expected) {
        test0(in, locale, expected);
        for (String[] ss : new String[][] { new String[] { "abc", "abc" }, new String[] { "aBc", "abc" }, new String[] { "ABC", "abc" }, new String[] { "ab\u4e00", "ab\u4e00" }, new String[] { "aB\u4e00", "ab\u4e00" }, new String[] { "AB\u4e00", "ab\u4e00" }, new String[] { "ab\uD800\uDC00", "ab\uD800\uDC00" }, new String[] { "aB\uD800\uDC00", "ab\uD800\uDC00" }, new String[] { "AB\uD800\uDC00", "ab\uD800\uDC00" }, new String[] { "ab\uD801\uDC1C", "ab\uD801\uDC44" }, new String[] { "aB\uD801\uDC1C", "ab\uD801\uDC44" }, new String[] { "AB\uD801\uDC1C", "ab\uD801\uDC44" } }) {
            test0(ss[0] + " " + in, locale, ss[1] + " " + expected);
            test0(in + " " + ss[0], locale, expected + " " + ss[1]);
        }
    }

    static void test0(String in, Locale locale, String expected) {
        String result = in.toLowerCase(locale);
        if (!result.equals(expected)) {
            System.err.println("input: " + in + ", locale: " + locale + ", expected: " + expected + ", actual: " + result);
            throw new RuntimeException();
        }
    }
}
