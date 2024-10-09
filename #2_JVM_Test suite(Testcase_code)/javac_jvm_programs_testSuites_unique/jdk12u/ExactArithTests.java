

import java.math.BigInteger;


public class ExactArithTests {

    
    private static int errors = 0;

    
    public static void main(String[] args) {
        testIntegerExact();
        testLongExact();

        if (errors > 0) {
            throw new RuntimeException(errors + " errors found in ExactArithTests.");
        }
    }

    static void fail(String message) {
        errors++;
        System.err.println(message);
    }

    
    static void testIntegerExact() {
        testIntegerExact(0, 0);
        testIntegerExact(1, 1);
        testIntegerExact(1, -1);
        testIntegerExact(-1, 1);
        testIntegerExact(1000, 2000);

        testIntegerExact(Integer.MIN_VALUE, Integer.MIN_VALUE);
        testIntegerExact(Integer.MAX_VALUE, Integer.MAX_VALUE);
        testIntegerExact(Integer.MIN_VALUE, 1);
        testIntegerExact(Integer.MAX_VALUE, 1);
        testIntegerExact(Integer.MIN_VALUE, 2);
        testIntegerExact(Integer.MAX_VALUE, 2);
        testIntegerExact(Integer.MIN_VALUE, -1);
        testIntegerExact(Integer.MAX_VALUE, -1);
        testIntegerExact(Integer.MIN_VALUE, -2);
        testIntegerExact(Integer.MAX_VALUE, -2);

    }

    
    static void testIntegerExact(int x, int y) {
        try {
            
            int sum = StrictMath.addExact(x, y);
            long sum2 = (long) x + (long) y;
            if ((int) sum2 != sum2) {
                fail("FAIL: int StrictMath.addExact(" + x + " + " + y + ") = " + sum + "; expected Arithmetic exception");
            } else if (sum != sum2) {
                fail("FAIL: long StrictMath.addExact(" + x + " + " + y + ") = " + sum + "; expected: " + sum2);
            }
        } catch (ArithmeticException ex) {
            long sum2 = (long) x + (long) y;
            if ((int) sum2 == sum2) {
                fail("FAIL: int StrictMath.addExact(" + x + " + " + y + ")" + "; Unexpected exception: " + ex);

            }
        }

        try {
            
            int diff = StrictMath.subtractExact(x, y);
            long diff2 = (long) x - (long) y;
            if ((int) diff2 != diff2) {
                fail("FAIL: int StrictMath.subtractExact(" + x + " - " + y + ") = " + diff + "; expected: " + diff2);
            }

        } catch (ArithmeticException ex) {
            long diff2 = (long) x - (long) y;
            if ((int) diff2 == diff2) {
                fail("FAIL: int StrictMath.subtractExact(" + x + " - " + y + ")" + "; Unexpected exception: " + ex);
            }
        }

        try {
            
            int product = StrictMath.multiplyExact(x, y);
            long m2 = (long) x * (long) y;
            if ((int) m2 != m2) {
                fail("FAIL: int StrictMath.multiplyExact(" + x + " * " + y + ") = " + product + "; expected: " + m2);
            }
        } catch (ArithmeticException ex) {
            long m2 = (long) x * (long) y;
            if ((int) m2 == m2) {
                fail("FAIL: int StrictMath.multiplyExact(" + x + " * " + y + ")" + "; Unexpected exception: " + ex);
            }
        }

    }

    
    static void testLongExact() {
        testLongExactTwice(0, 0);
        testLongExactTwice(1, 1);
        testLongExactTwice(1, -1);
        testLongExactTwice(1000, 2000);

        testLongExactTwice(Long.MIN_VALUE, Long.MIN_VALUE);
        testLongExactTwice(Long.MAX_VALUE, Long.MAX_VALUE);
        testLongExactTwice(Long.MIN_VALUE, 1);
        testLongExactTwice(Long.MAX_VALUE, 1);
        testLongExactTwice(Long.MIN_VALUE, 2);
        testLongExactTwice(Long.MAX_VALUE, 2);
        testLongExactTwice(Long.MIN_VALUE, -1);
        testLongExactTwice(Long.MAX_VALUE, -1);
        testLongExactTwice(Long.MIN_VALUE, -2);
        testLongExactTwice(Long.MAX_VALUE, -2);
        testLongExactTwice(Long.MIN_VALUE/2, 2);
        testLongExactTwice(Long.MAX_VALUE, 2);
        testLongExactTwice(Integer.MAX_VALUE, Integer.MAX_VALUE);
        testLongExactTwice(Integer.MAX_VALUE, -Integer.MAX_VALUE);
        testLongExactTwice(Integer.MAX_VALUE+1, Integer.MAX_VALUE+1);
        testLongExactTwice(Integer.MAX_VALUE+1, -Integer.MAX_VALUE+1);
        testLongExactTwice(Integer.MIN_VALUE-1, Integer.MIN_VALUE-1);
        testLongExactTwice(Integer.MIN_VALUE-1, -Integer.MIN_VALUE-1);
        testLongExactTwice(Integer.MIN_VALUE/2, 2);

    }

    
    static void testLongExactTwice(long x, long y) {
        testLongExact(x, y);
        testLongExact(y, x);
    }


    
    static void testLongExact(long x, long y) {
        BigInteger resultBig = null;
        final BigInteger xBig = BigInteger.valueOf(x);
        final BigInteger yBig = BigInteger.valueOf(y);
        try {
            
            resultBig = xBig.add(yBig);
            long sum = StrictMath.addExact(x, y);
            checkResult("long StrictMath.addExact", x, y, sum, resultBig);
        } catch (ArithmeticException ex) {
            if (inLongRange(resultBig)) {
                fail("FAIL: long StrictMath.addExact(" + x + " + " + y + "); Unexpected exception: " + ex);
            }
        }

        try {
            
            resultBig = xBig.subtract(yBig);
            long diff = StrictMath.subtractExact(x, y);
            checkResult("long StrictMath.subtractExact", x, y, diff, resultBig);
        } catch (ArithmeticException ex) {
            if (inLongRange(resultBig)) {
                fail("FAIL: long StrictMath.subtractExact(" + x + " - " + y + ")" + "; Unexpected exception: " + ex);
            }
        }

        try {
            
            resultBig = xBig.multiply(yBig);
            long product = StrictMath.multiplyExact(x, y);
            checkResult("long StrictMath.multiplyExact", x, y, product, resultBig);
        } catch (ArithmeticException ex) {
            if (inLongRange(resultBig)) {
                fail("FAIL: long StrictMath.multiplyExact(" + x + " * " + y + ")" + "; Unexpected exception: " + ex);
            }
        }

        try {
            
            int value = StrictMath.toIntExact(x);
            if ((long)value != x) {
                fail("FAIL: " + "long StrictMath.toIntExact" + "(" + x + ") = " + value + "; expected an arithmetic exception: ");
            }
        } catch (ArithmeticException ex) {
            if (resultBig.bitLength() <= 32) {
                fail("FAIL: long StrictMath.toIntExact(" + x + ")" + "; Unexpected exception: " + ex);
            }
        }

    }

    
    static void checkResult(String message, long x, long y, long result, BigInteger expected) {
        BigInteger resultBig = BigInteger.valueOf(result);
        if (!inLongRange(expected)) {
            fail("FAIL: " + message + "(" + x + ", " + y + ") = " + result + "; expected an arithmetic exception: ");
        } else if (!resultBig.equals(expected)) {
            fail("FAIL: " + message + "(" + x + ", " + y + ") = " + result + "; expected " + expected);
        }
    }

    
    static boolean inLongRange(BigInteger value) {
        return value.bitLength() <= 63;
    }
}
