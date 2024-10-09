import java.lang.IndexOutOfBoundsException;
import java.lang.NullPointerException;
import java.lang.RuntimeException;

public class ParsingTest {

    public static void main(String... argv) {
        check(+100, "+100");
        check(-100, "-100");
        check(0, "+0");
        check(0, "-0");
        check(0, "+00000");
        check(0, "-00000");
        check(0, "0");
        check(1, "1");
        check(9, "9");
        checkFailure("");
        checkFailure("\u0000");
        checkFailure("\u002f");
        checkFailure("+");
        checkFailure("-");
        checkFailure("++");
        checkFailure("+-");
        checkFailure("-+");
        checkFailure("--");
        checkFailure("++100");
        checkFailure("--100");
        checkFailure("+-6");
        checkFailure("-+6");
        checkFailure("*100");
        check(0, "+00000", 0, 6, 10);
        check(0, "-00000", 0, 6, 10);
        check(0, "test-00000", 4, 10, 10);
        check(-12345, "test-12345", 4, 10, 10);
        check(12345, "xx12345yy", 2, 7, 10);
        check(15, "xxFyy", 2, 3, 16);
        checkNumberFormatException("", 0, 0, 10);
        checkNumberFormatException("+-6", 0, 3, 10);
        checkNumberFormatException("1000000", 7, 7, 10);
        checkNumberFormatException("1000000", 0, 2, Character.MAX_RADIX + 1);
        checkNumberFormatException("1000000", 0, 2, Character.MIN_RADIX - 1);
        checkIndexOutOfBoundsException("1000000", 10, 4, 10);
        checkIndexOutOfBoundsException("1000000", -1, 2, Character.MAX_RADIX + 1);
        checkIndexOutOfBoundsException("1000000", -1, 2, Character.MIN_RADIX - 1);
        checkIndexOutOfBoundsException("1000000", 10, 2, Character.MAX_RADIX + 1);
        checkIndexOutOfBoundsException("1000000", 10, 2, Character.MIN_RADIX - 1);
        checkIndexOutOfBoundsException("-1", 0, 3, 10);
        checkIndexOutOfBoundsException("-1", 2, 3, 10);
        checkIndexOutOfBoundsException("-1", -1, 2, 10);
        checkNull(0, 1, 10);
        checkNull(-1, 0, 10);
        checkNull(0, 0, 10);
        checkNull(0, -1, 10);
        checkNull(-1, -1, -1);
    }

    private static void check(int expected, String val) {
        int n = Integer.parseInt(val);
        if (n != expected)
            throw new RuntimeException("Integer.parseInt failed. String:" + val + " Result:" + n);
    }

    private static void checkFailure(String val) {
        int n = 0;
        try {
            n = Integer.parseInt(val);
            System.err.println("parseInt(" + val + ") incorrectly returned " + n);
            throw new RuntimeException();
        } catch (NumberFormatException nfe) {
            ;
        }
    }

    private static void checkNumberFormatException(String val, int start, int end, int radix) {
        int n = 0;
        try {
            n = Integer.parseInt(val, start, end, radix);
            System.err.println("parseInt(" + val + ", " + start + ", " + end + ", " + radix + ") incorrectly returned " + n);
            throw new RuntimeException();
        } catch (NumberFormatException nfe) {
            ;
        }
    }

    private static void checkIndexOutOfBoundsException(String val, int start, int end, int radix) {
        int n = 0;
        try {
            n = Integer.parseInt(val, start, end, radix);
            System.err.println("parseInt(" + val + ", " + start + ", " + end + ", " + radix + ") incorrectly returned " + n);
            throw new RuntimeException();
        } catch (IndexOutOfBoundsException ioob) {
            ;
        }
    }

    private static void checkNull(int start, int end, int radix) {
        int n = 0;
        try {
            n = Integer.parseInt(null, start, end, radix);
            System.err.println("parseInt(null, " + start + ", " + end + ", " + radix + ") incorrectly returned " + n);
            throw new RuntimeException();
        } catch (NullPointerException npe) {
            ;
        }
    }

    private static void check(int expected, String val, int start, int end, int radix) {
        int n = Integer.parseInt(val, start, end, radix);
        if (n != expected)
            throw new RuntimeException("Integer.parsedInt failed. Expected: " + expected + " String: \"" + val + "\", start: " + start + ", end: " + end + ", radix: " + radix + " Result:" + n);
    }
}
