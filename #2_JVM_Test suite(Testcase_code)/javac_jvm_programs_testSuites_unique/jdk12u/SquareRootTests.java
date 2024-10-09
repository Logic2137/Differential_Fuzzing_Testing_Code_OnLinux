



import java.math.*;
import java.util.*;

public class SquareRootTests {

    public static void main(String... args) {
        int failures = 0;

        failures += negativeTests();
        failures += zeroTests();
        failures += evenPowersOfTenTests();
        failures += squareRootTwoTests();
        failures += lowPrecisionPerfectSquares();

        if (failures > 0 ) {
            throw new RuntimeException("Incurred " + failures + " failures" +
                                       " testing BigDecimal.sqrt().");
        }
    }

    private static int negativeTests() {
        int failures = 0;

        for (long i = -10; i < 0; i++) {
            for (int j = -5; j < 5; j++) {
                try {
                    BigDecimal input = BigDecimal.valueOf(i, j);
                    BigDecimal result = input.sqrt(MathContext.DECIMAL64);
                    System.err.println("Unexpected sqrt of negative: (" +
                                       input + ").sqrt()  = " + result );
                    failures += 1;
                } catch (ArithmeticException e) {
                    ; 
                }
            }
        }

        return failures;
    }

    private static int zeroTests() {
        int failures = 0;

        for (int i = -100; i < 100; i++) {
            BigDecimal expected = BigDecimal.valueOf(0L, i/2);
            
            failures += compare(BigDecimal.valueOf(0L, i).sqrt(MathContext.UNLIMITED),
                                expected, true, "zeros");

            failures += compare(BigDecimal.valueOf(0L, i).sqrt(MathContext.DECIMAL64),
                                expected, true, "zeros");
        }

        return failures;
    }

    
    private static int evenPowersOfTenTests() {
        int failures = 0;
        MathContext oneDigitExactly = new MathContext(1, RoundingMode.UNNECESSARY);

        for (int scale = -100; scale <= 100; scale++) {
            BigDecimal testValue       = BigDecimal.valueOf(1, 2*scale);
            BigDecimal expectedNumericalResult = BigDecimal.valueOf(1, scale);

            BigDecimal result;


            failures += equalNumerically(expectedNumericalResult,
                                           result = testValue.sqrt(MathContext.DECIMAL64),
                                           "Even powers of 10, DECIMAL64");

            
            failures += equalNumerically(expectedNumericalResult,
                                           result = testValue.sqrt(oneDigitExactly),
                                           "even powers of 10, 1 digit");
            if (result.precision() > 1) {
                failures += 1;
                System.err.println("Excess precision for " + result);
            }


            

        }

        return failures;
    }

    private static int squareRootTwoTests() {
        int failures = 0;
        BigDecimal TWO = new BigDecimal(2);

        
        BigDecimal highPrecisionRoot2 =
            new BigDecimal("1.41421356237309504880168872420969807856967187537694807317667973799");


        RoundingMode[] modes = {
            RoundingMode.UP,       RoundingMode.DOWN,
            RoundingMode.CEILING, RoundingMode.FLOOR,
            RoundingMode.HALF_UP, RoundingMode.HALF_DOWN, RoundingMode.HALF_EVEN
        };

        
        
        

        for (RoundingMode mode : modes) {
            for (int precision = 1; precision < 63; precision++) {
                MathContext mc = new MathContext(precision, mode);
                BigDecimal expected = highPrecisionRoot2.round(mc);
                BigDecimal computed = TWO.sqrt(mc);

                equalNumerically(expected, computed, "sqrt(2)");
            }
        }

        return failures;
    }

    private static int lowPrecisionPerfectSquares() {
        int failures = 0;

        
        
        
        
        long[][] squaresWithOneDigitRoot = {{ 4, 2},
                                            { 9, 3},
                                            {25, 5},
                                            {36, 6},
                                            {49, 7},
                                            {64, 8},
                                            {81, 9}};

        for (long[] squareAndRoot : squaresWithOneDigitRoot) {
            BigDecimal square     = new BigDecimal(squareAndRoot[0]);
            BigDecimal expected   = new BigDecimal(squareAndRoot[1]);

            for (int scale = 0; scale <= 4; scale++) {
                BigDecimal scaledSquare = square.setScale(scale, RoundingMode.UNNECESSARY);
                int expectedScale = scale/2;
                for (int precision = 0; precision <= 5; precision++) {
                    for (RoundingMode rm : RoundingMode.values()) {
                        MathContext mc = new MathContext(precision, rm);
                        BigDecimal computedRoot = scaledSquare.sqrt(mc);
                        failures += equalNumerically(expected, computedRoot, "simple squares");
                        int computedScale = computedRoot.scale();
                        if (precision >=  expectedScale + 1 &&
                            computedScale != expectedScale) {
                        System.err.printf("%s\tprecision=%d\trm=%s%n",
                                          computedRoot.toString(), precision, rm);
                            failures++;
                            System.err.printf("\t%s does not have expected scale of %d%n.",
                                              computedRoot, expectedScale);
                        }
                    }
                }
            }
        }

        return failures;
    }

    private static int compare(BigDecimal a, BigDecimal b, boolean expected, String prefix) {
        boolean result = a.equals(b);
        int failed = (result==expected) ? 0 : 1;
        if (failed == 1) {
            System.err.println("Testing " + prefix +
                               "(" + a + ").compareTo(" + b + ") => " + result +
                               "\n\tExpected " + expected);
        }
        return failed;
    }

    private static int equalNumerically(BigDecimal a, BigDecimal b,
                                        String prefix) {
        return compareNumerically(a, b, 0, prefix);
    }


    private static int compareNumerically(BigDecimal a, BigDecimal b,
                                          int expected, String prefix) {
        int result = a.compareTo(b);
        int failed = (result==expected) ? 0 : 1;
        if (failed == 1) {
            System.err.println("Testing " + prefix +
                               "(" + a + ").compareTo(" + b + ") => " + result +
                               "\n\tExpected " + expected);
        }
        return failed;
    }

}
