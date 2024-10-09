import java.math.*;
import static java.math.BigInteger.*;

public class CompareToTests {

    private static int compareToTests() {
        int failures = 0;
        final BigInteger MINUS_ONE = BigInteger.ONE.negate();
        final BigInteger TWO_POW_126 = ONE.shiftLeft(126);
        final BigInteger TWO_POW_127 = ONE.shiftLeft(127);
        final BigInteger TWO_POW_128 = ONE.shiftLeft(128);
        BigInteger[][] testCases = { { valueOf(0), valueOf(0), ZERO }, { valueOf(0), valueOf(1), MINUS_ONE }, { valueOf(1), valueOf(2), MINUS_ONE }, { valueOf(2), valueOf(1), ONE }, { valueOf(10), valueOf(10), ZERO }, { TWO_POW_127, TWO_POW_127, ZERO }, { TWO_POW_127.negate(), TWO_POW_127, MINUS_ONE }, { TWO_POW_128.or(TWO_POW_126), TWO_POW_128, ONE }, { TWO_POW_128.or(TWO_POW_126), TWO_POW_128.negate(), ONE }, { TWO_POW_128, TWO_POW_128.or(TWO_POW_126), MINUS_ONE }, { TWO_POW_128.negate(), TWO_POW_128.or(TWO_POW_126), MINUS_ONE }, { TWO_POW_127, TWO_POW_128, MINUS_ONE }, { TWO_POW_127.negate(), TWO_POW_128, MINUS_ONE }, { TWO_POW_128, TWO_POW_127, ONE }, { TWO_POW_128.negate(), TWO_POW_127, MINUS_ONE }, { valueOf(Long.MAX_VALUE), valueOf(Long.MAX_VALUE), ZERO }, { valueOf(Long.MAX_VALUE).negate(), valueOf(Long.MAX_VALUE), MINUS_ONE }, { valueOf(Long.MAX_VALUE - 1), valueOf(Long.MAX_VALUE), MINUS_ONE }, { valueOf(Long.MAX_VALUE - 1).negate(), valueOf(Long.MAX_VALUE), MINUS_ONE }, { valueOf(Long.MIN_VALUE), valueOf(Long.MAX_VALUE), MINUS_ONE }, { valueOf(Long.MIN_VALUE).negate(), valueOf(Long.MAX_VALUE), ONE }, { valueOf(Long.MIN_VALUE + 1), valueOf(Long.MAX_VALUE), MINUS_ONE }, { valueOf(Long.MIN_VALUE + 1).negate(), valueOf(Long.MAX_VALUE), ZERO }, { valueOf(Long.MAX_VALUE), valueOf(Long.MIN_VALUE), ONE }, { valueOf(Long.MAX_VALUE).negate(), valueOf(Long.MIN_VALUE), ONE }, { valueOf(Long.MAX_VALUE - 1), valueOf(Long.MIN_VALUE), ONE }, { valueOf(Long.MAX_VALUE - 1).negate(), valueOf(Long.MIN_VALUE), ONE }, { valueOf(Long.MIN_VALUE), valueOf(Long.MIN_VALUE), ZERO }, { valueOf(Long.MIN_VALUE).negate(), valueOf(Long.MIN_VALUE), ONE }, { valueOf(Long.MIN_VALUE + 1), valueOf(Long.MIN_VALUE), ONE }, { valueOf(Long.MIN_VALUE + 1).negate(), valueOf(Long.MIN_VALUE), ONE } };
        for (BigInteger[] testCase : testCases) {
            BigInteger a = testCase[0];
            BigInteger a_negate = a.negate();
            BigInteger b = testCase[1];
            BigInteger b_negate = b.negate();
            int expected = testCase[2].intValue();
            failures += compareToTest(a, b, expected);
            failures += compareToTest(a_negate, b_negate, -expected);
        }
        return failures;
    }

    private static int compareToTest(BigInteger a, BigInteger b, int expected) {
        int result = a.compareTo(b);
        int failed = (result == expected) ? 0 : 1;
        if (failed == 1) {
            System.err.println("(" + a + ").compareTo(" + b + ") => " + result + "\n\tExpected " + expected);
        }
        return failed;
    }

    public static void main(String[] argv) {
        int failures = 0;
        failures += compareToTests();
        if (failures > 0) {
            throw new RuntimeException("Incurred " + failures + " failures while testing exact compareTo.");
        }
    }
}
