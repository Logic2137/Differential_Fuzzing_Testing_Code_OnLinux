import java.lang.reflect.Method;

public class XPrecisionDecimalToString {

    private static final String className = "com.sun.org.apache.xerces.internal.impl.dv.xs.PrecisionDecimalDV$XPrecisionDecimal";

    private static final String methodName = "canonicalToStringForHashCode";

    private static final Class<?>[] signature = { String.class, String.class, int.class, int.class };

    private static Method method;

    private static String canonicalToStringForHashCode(String ivalue, String fvalue, int sign, int pvalue) {
        try {
            if (method == null) {
                Class<?> type = Class.forName(className);
                method = type.getDeclaredMethod(methodName, signature);
                method.setAccessible(true);
            }
        } catch (Exception x) {
            throw new Error("Impossible to find '" + className + "." + methodName + "': " + x, x);
        }
        try {
            return (String) method.invoke(null, new Object[] { ivalue, fvalue, sign, pvalue });
        } catch (Exception x) {
            throw new Error("Failed to invoke " + className + "." + methodName + "(\"" + ivalue + "\", \"" + fvalue + "\", " + sign + ", " + pvalue + "): " + x, x);
        }
    }

    public static void main(String[] args) {
        test("123", "7890", -1, 0, "-1.23789E2");
        test("0", "007890", -1, 0, "-7.89E-3");
        test("123", "7890", 1, 0, "1.23789E2");
        test("0", "007890", 1, 0, "7.89E-3");
        test("123", "7890", 1, 10, "1.23789E12");
        test("0", "007890", 1, 33, "7.89E30");
        test("INF", "", 1, 0, "INF");
        test("INF", "", -1, 0, "-INF");
        test("NaN", "", 0, 0, "NaN");
        test("0", "", 1, 0, "0");
        test("00000", "00000", 1, 10, "0");
        test("00000", "00000", -1, 10, "0");
        test("00000", "000001", -1, -10, "-1E-16");
    }

    private static void test(String ival, String fval, int sign, int pvalue, String expected) {
        final String canonical = canonicalToStringForHashCode(ival, fval, sign, pvalue);
        System.out.println((sign == -1 ? "-" : "") + ival + ("INF".equals(ival) || "NaN".equals(ival) ? "" : ("." + fval + "E" + pvalue)) + " => " + canonical);
        if (!expected.equals(canonical)) {
            throw new Error("expected: " + expected + " got: " + canonical);
        }
    }
}
