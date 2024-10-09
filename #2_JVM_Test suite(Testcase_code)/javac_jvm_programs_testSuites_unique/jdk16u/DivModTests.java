

import java.math.BigDecimal;
import java.math.RoundingMode;


public class DivModTests {

    
    private static int errors = 0;

    
    public static void main(String[] args) {
        errors = 0;
        testIntFloorDivMod();
        testLongFloorDivMod();

        if (errors > 0) {
            throw new RuntimeException(errors + " errors found in DivMod methods.");
        }
    }

    
    static void fail(String message, Object... args) {
        errors++;
        System.out.printf(message, args);
    }

    
    static void testIntFloorDivMod() {
        testIntFloorDivMod(4, 0, new ArithmeticException(), new ArithmeticException()); 
        testIntFloorDivMod(4, 3, 1, 1);
        testIntFloorDivMod(3, 3, 1, 0);
        testIntFloorDivMod(2, 3, 0, 2);
        testIntFloorDivMod(1, 3, 0, 1);
        testIntFloorDivMod(0, 3, 0, 0);
        testIntFloorDivMod(4, -3, -2, -2);
        testIntFloorDivMod(3, -3, -1, 0);
        testIntFloorDivMod(2, -3, -1, -1);
        testIntFloorDivMod(1, -3, -1, -2);
        testIntFloorDivMod(0, -3, 0, 0);
        testIntFloorDivMod(-1, 3, -1, 2);
        testIntFloorDivMod(-2, 3, -1, 1);
        testIntFloorDivMod(-3, 3, -1, 0);
        testIntFloorDivMod(-4, 3, -2, 2);
        testIntFloorDivMod(-1, -3, 0, -1);
        testIntFloorDivMod(-2, -3, 0, -2);
        testIntFloorDivMod(-3, -3, 1, 0);
        testIntFloorDivMod(-4, -3, 1, -1);
        testIntFloorDivMod(Integer.MAX_VALUE, 1, Integer.MAX_VALUE, 0);
        testIntFloorDivMod(Integer.MAX_VALUE, -1, -Integer.MAX_VALUE, 0);
        testIntFloorDivMod(Integer.MAX_VALUE, 3, 715827882, 1);
        testIntFloorDivMod(Integer.MAX_VALUE - 1, 3, 715827882, 0);
        testIntFloorDivMod(Integer.MIN_VALUE, 3, -715827883, 1);
        testIntFloorDivMod(Integer.MIN_VALUE + 1, 3, -715827883, 2);
        testIntFloorDivMod(Integer.MIN_VALUE + 1, -1, Integer.MAX_VALUE, 0);
        testIntFloorDivMod(Integer.MAX_VALUE, Integer.MAX_VALUE, 1, 0);
        testIntFloorDivMod(Integer.MAX_VALUE, Integer.MIN_VALUE, -1, -1);
        testIntFloorDivMod(Integer.MIN_VALUE, Integer.MIN_VALUE, 1, 0);
        testIntFloorDivMod(Integer.MIN_VALUE, Integer.MAX_VALUE, -2, 2147483646);
        
        testIntFloorDivMod(Integer.MIN_VALUE, -1, Integer.MIN_VALUE, 0);
    }

    
    static void testIntFloorDivMod(int x, int y, Object divExpected, Object modExpected) {
        testIntFloorDiv(x, y, divExpected);
        testIntFloorMod(x, y, modExpected);
    }

    
    static void testIntFloorDiv(int x, int y, Object expected) {
        Object result = doFloorDiv(x, y);
        if (!resultEquals(result, expected)) {
            fail("FAIL: Math.floorDiv(%d, %d) = %s; expected %s%n", x, y, result, expected);
        }

        Object strict_result = doStrictFloorDiv(x, y);
        if (!resultEquals(strict_result, expected)) {
            fail("FAIL: StrictMath.floorDiv(%d, %d) = %s; expected %s%n", x, y, strict_result, expected);
        }
    }

    
    static void testIntFloorMod(int x, int y, Object expected) {
        Object result = doFloorMod(x, y);
        if (!resultEquals(result, expected)) {
            fail("FAIL: Math.floorMod(%d, %d) = %s; expected %s%n", x, y, result, expected);
        }

        Object strict_result = doStrictFloorMod(x, y);
        if (!resultEquals(strict_result, expected)) {
            fail("FAIL: StrictMath.floorMod(%d, %d) = %s; expected %s%n", x, y, strict_result, expected);
        }

        try {
            
            int tmp = x / y;     
            double ff = x - Math.floor((double)x / (double)y) * y;
            int fr = (int)ff;
            boolean t = (fr == ((Integer)result));
            if (!result.equals(fr)) {
                fail("FAIL: Math.floorMod(%d, %d) = %s differs from Math.floor(x, y): %d%n", x, y, result, fr);
            }
        } catch (ArithmeticException ae) {
            if (y != 0) {
                fail("FAIL: Math.floorMod(%d, %d); unexpected %s%n", x, y, ae);
            }
        }
    }

    
    static void testLongFloorDivMod() {
        testLongFloorDivMod(4L, 0L, new ArithmeticException(), new ArithmeticException()); 
        testLongFloorDivMod(4L, 3L, 1L, 1L);
        testLongFloorDivMod(3L, 3L, 1L, 0L);
        testLongFloorDivMod(2L, 3L, 0L, 2L);
        testLongFloorDivMod(1L, 3L, 0L, 1L);
        testLongFloorDivMod(0L, 3L, 0L, 0L);
        testLongFloorDivMod(4L, -3L, -2L, -2L);
        testLongFloorDivMod(3L, -3L, -1L, 0l);
        testLongFloorDivMod(2L, -3L, -1L, -1L);
        testLongFloorDivMod(1L, -3L, -1L, -2L);
        testLongFloorDivMod(0L, -3L, 0L, 0L);
        testLongFloorDivMod(-1L, 3L, -1L, 2L);
        testLongFloorDivMod(-2L, 3L, -1L, 1L);
        testLongFloorDivMod(-3L, 3L, -1L, 0L);
        testLongFloorDivMod(-4L, 3L, -2L, 2L);
        testLongFloorDivMod(-1L, -3L, 0L, -1L);
        testLongFloorDivMod(-2L, -3L, 0L, -2L);
        testLongFloorDivMod(-3L, -3L, 1L, 0L);
        testLongFloorDivMod(-4L, -3L, 1L, -1L);

        testLongFloorDivMod(Long.MAX_VALUE, 1, Long.MAX_VALUE, 0L);
        testLongFloorDivMod(Long.MAX_VALUE, -1, -Long.MAX_VALUE, 0L);
        testLongFloorDivMod(Long.MAX_VALUE, 3L, Long.MAX_VALUE / 3L, 1L);
        testLongFloorDivMod(Long.MAX_VALUE - 1L, 3L, (Long.MAX_VALUE - 1L) / 3L, 0L);
        testLongFloorDivMod(Long.MIN_VALUE, 3L, Long.MIN_VALUE / 3L - 1L, 1L);
        testLongFloorDivMod(Long.MIN_VALUE + 1L, 3L, Long.MIN_VALUE / 3L - 1L, 2L);
        testLongFloorDivMod(Long.MIN_VALUE + 1, -1, Long.MAX_VALUE, 0L);
        testLongFloorDivMod(Long.MAX_VALUE, Long.MAX_VALUE, 1L, 0L);
        testLongFloorDivMod(Long.MAX_VALUE, Long.MIN_VALUE, -1L, -1L);
        testLongFloorDivMod(Long.MIN_VALUE, Long.MIN_VALUE, 1L, 0L);
        testLongFloorDivMod(Long.MIN_VALUE, Long.MAX_VALUE, -2L, 9223372036854775806L);
        
        testLongFloorDivMod(Long.MIN_VALUE, -1, Long.MIN_VALUE, 0L);
    }

    
    static void testLongFloorDivMod(long x, long y, Object divExpected, Object modExpected) {
        testLongFloorDiv(x, y, divExpected);
        testLongFloorMod(x, y, modExpected);
    }

    
    static void testLongFloorDiv(long x, long y, Object expected) {
        Object result = doFloorDiv(x, y);
        if (!resultEquals(result, expected)) {
            fail("FAIL: long Math.floorDiv(%d, %d) = %s; expected %s%n", x, y, result, expected);
        }

        Object strict_result = doStrictFloorDiv(x, y);
        if (!resultEquals(strict_result, expected)) {
            fail("FAIL: long StrictMath.floorDiv(%d, %d) = %s; expected %s%n", x, y, strict_result, expected);
        }
    }

    
    static void testLongFloorMod(long x, long y, Object expected) {
        Object result = doFloorMod(x, y);
        if (!resultEquals(result, expected)) {
            fail("FAIL: long Math.floorMod(%d, %d) = %s; expected %s%n", x, y, result, expected);
        }

        Object strict_result = doStrictFloorMod(x, y);
        if (!resultEquals(strict_result, expected)) {
            fail("FAIL: long StrictMath.floorMod(%d, %d) = %s; expected %s%n", x, y, strict_result, expected);
        }

        try {
            
            BigDecimal xD = new BigDecimal(x);
            BigDecimal yD = new BigDecimal(y);
            BigDecimal resultD = xD.divide(yD, RoundingMode.FLOOR);
            resultD = resultD.multiply(yD);
            resultD = xD.subtract(resultD);
            long fr = resultD.longValue();
            if (!result.equals(fr)) {
                fail("FAIL: Long.floorMod(%d, %d) = %d is different than BigDecimal result: %d%n", x, y, result, fr);

            }
        } catch (ArithmeticException ae) {
            if (y != 0) {
                fail("FAIL: long Math.floorMod(%d, %d); unexpected ArithmeticException from bigdecimal");
            }
        }
    }

    
    static void testLongIntFloorDivMod() {
        testLongIntFloorDivMod(4L, 0, new ArithmeticException(), new ArithmeticException()); 
        testLongIntFloorDivMod(4L, 3, 1L, 1);
        testLongIntFloorDivMod(3L, 3, 1L, 0);
        testLongIntFloorDivMod(2L, 3, 0L, 2);
        testLongIntFloorDivMod(1L, 3, 0L, 1);
        testLongIntFloorDivMod(0L, 3, 0L, 0);
        testLongIntFloorDivMod(4L, -3, -2L, -2);
        testLongIntFloorDivMod(3L, -3, -1L, 0);
        testLongIntFloorDivMod(2L, -3, -1L, -1);
        testLongIntFloorDivMod(1L, -3, -1L, -2);
        testLongIntFloorDivMod(0L, -3, 0L, 0);
        testLongIntFloorDivMod(-1L, 3, -1L, 2);
        testLongIntFloorDivMod(-2L, 3, -1L, 1);
        testLongIntFloorDivMod(-3L, 3, -1L, 0);
        testLongIntFloorDivMod(-4L, 3, -2L, 2);
        testLongIntFloorDivMod(-1L, -3, 0L, -1);
        testLongIntFloorDivMod(-2L, -3, 0L, -2);
        testLongIntFloorDivMod(-3L, -3, 1L, 0);
        testLongIntFloorDivMod(-4L, -3, 1L, -1);

        testLongIntFloorDivMod(Long.MAX_VALUE, 1, Long.MAX_VALUE, 0L);
        testLongIntFloorDivMod(Long.MAX_VALUE, -1, -Long.MAX_VALUE, 0L);
        testLongIntFloorDivMod(Long.MAX_VALUE, 3, Long.MAX_VALUE / 3L, 1L);
        testLongIntFloorDivMod(Long.MAX_VALUE - 1L, 3, (Long.MAX_VALUE - 1L) / 3L, 0L);
        testLongIntFloorDivMod(Long.MIN_VALUE, 3, Long.MIN_VALUE / 3L - 1L, 1L);
        testLongIntFloorDivMod(Long.MIN_VALUE + 1L, 3, Long.MIN_VALUE / 3L - 1L, 2L);
        testLongIntFloorDivMod(Long.MIN_VALUE + 1, -1, Long.MAX_VALUE, 0L);
        testLongIntFloorDivMod(Long.MAX_VALUE, Integer.MAX_VALUE, 4294967298L, 1);
        testLongIntFloorDivMod(Long.MAX_VALUE, Integer.MIN_VALUE, -4294967296L, -1);
        testLongIntFloorDivMod(Long.MIN_VALUE, Integer.MIN_VALUE, 4294967296L, 0);
        testLongIntFloorDivMod(Long.MIN_VALUE, Integer.MAX_VALUE, -4294967299L, 2147483645);
        
        testLongIntFloorDivMod(Long.MIN_VALUE, -1, Long.MIN_VALUE, 0L);
    }

    
    static void testLongIntFloorDivMod(long x, int y, Object divExpected, Object modExpected) {
        testLongIntFloorDiv(x, y, divExpected);
        testLongIntFloorMod(x, y, modExpected);
    }

    
    static void testLongIntFloorDiv(long x, int y, Object expected) {
        Object result = doFloorDiv(x, y);
        if (!resultEquals(result, expected)) {
            fail("FAIL: long Math.floorDiv(%d, %d) = %s; expected %s%n", x, y, result, expected);
        }

        Object strict_result = doStrictFloorDiv(x, y);
        if (!resultEquals(strict_result, expected)) {
            fail("FAIL: long StrictMath.floorDiv(%d, %d) = %s; expected %s%n", x, y, strict_result, expected);
        }
    }

    
    static void testLongIntFloorMod(long x, int y, Object expected) {
        Object result = doFloorMod(x, y);
        if (!resultEquals(result, expected)) {
            fail("FAIL: long Math.floorMod(%d, %d) = %s; expected %s%n", x, y, result, expected);
        }

        Object strict_result = doStrictFloorMod(x, y);
        if (!resultEquals(strict_result, expected)) {
            fail("FAIL: long StrictMath.floorMod(%d, %d) = %s; expected %s%n", x, y, strict_result, expected);
        }

        try {
            
            BigDecimal xD = new BigDecimal(x);
            BigDecimal yD = new BigDecimal(y);
            BigDecimal resultD = xD.divide(yD, RoundingMode.FLOOR);
            resultD = resultD.multiply(yD);
            resultD = xD.subtract(resultD);
            long fr = resultD.longValue();
            if (!result.equals(fr)) {
                fail("FAIL: Long.floorMod(%d, %d) = %d is different than BigDecimal result: %d%n", x, y, result, fr);

            }
        } catch (ArithmeticException ae) {
            if (y != 0) {
                fail("FAIL: long Math.floorMod(%d, %d); unexpected ArithmeticException from bigdecimal");
            }
        }
    }

    
    static Object doFloorDiv(int x, int y) {
        try {
            return Math.floorDiv(x, y);
        } catch (ArithmeticException ae) {
            return ae;
        }
    }

    
    static Object doFloorDiv(long x, int y) {
        try {
            return Math.floorDiv(x, y);
        } catch (ArithmeticException ae) {
            return ae;
        }
    }

    
    static Object doFloorDiv(long x, long y) {
        try {
            return Math.floorDiv(x, y);
        } catch (ArithmeticException ae) {
            return ae;
        }
    }

    
    static Object doFloorMod(int x, int y) {
        try {
            return Math.floorMod(x, y);
        } catch (ArithmeticException ae) {
            return ae;
        }
    }

    
    static Object doFloorMod(long x, int y) {
        try {
            return Math.floorMod(x, y);
        } catch (ArithmeticException ae) {
            return ae;
        }
    }

    
    static Object doFloorMod(long x, long y) {
        try {
            return Math.floorMod(x, y);
        } catch (ArithmeticException ae) {
            return ae;
        }
    }

    
    static Object doStrictFloorDiv(int x, int y) {
        try {
            return StrictMath.floorDiv(x, y);
        } catch (ArithmeticException ae) {
            return ae;
        }
    }

    
    static Object doStrictFloorDiv(long x, int y) {
        try {
            return StrictMath.floorDiv(x, y);
        } catch (ArithmeticException ae) {
            return ae;
        }
    }

    
    static Object doStrictFloorDiv(long x, long y) {
        try {
            return StrictMath.floorDiv(x, y);
        } catch (ArithmeticException ae) {
            return ae;
        }
    }

    
    static Object doStrictFloorMod(int x, int y) {
        try {
            return StrictMath.floorMod(x, y);
        } catch (ArithmeticException ae) {
            return ae;
        }
    }

    
    static Object doStrictFloorMod(long x, int y) {
        try {
            return StrictMath.floorMod(x, y);
        } catch (ArithmeticException ae) {
            return ae;
        }
    }

    
    static Object doStrictFloorMod(long x, long y) {
        try {
            return StrictMath.floorMod(x, y);
        } catch (ArithmeticException ae) {
            return ae;
        }
    }

    
    static boolean resultEquals(Object result, Object expected) {
        if (result.getClass() != expected.getClass()) {
            fail("FAIL: Result type mismatch, %s; expected: %s%n",
                    result.getClass().getName(), expected.getClass().getName());
            return false;
        }

        if (result.equals(expected)) {
            return true;
        }
        
        if (result instanceof ArithmeticException && expected instanceof ArithmeticException) {
            return true;
        }
        return false;
    }

}
