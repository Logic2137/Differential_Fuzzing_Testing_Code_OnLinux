import java.math.*;
import java.util.*;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;

public class SquareRootTests {

    private static BigDecimal TWO = new BigDecimal(2);

    private static final BigDecimal ONE_TENTH = valueOf(1L, 1);

    public static void main(String... args) {
        int failures = 0;
        failures += negativeTests();
        failures += zeroTests();
        failures += oneDigitTests();
        failures += twoDigitTests();
        failures += evenPowersOfTenTests();
        failures += squareRootTwoTests();
        failures += lowPrecisionPerfectSquares();
        failures += almostFourRoundingDown();
        failures += almostFourRoundingUp();
        failures += nearTen();
        failures += nearOne();
        failures += halfWay();
        if (failures > 0) {
            throw new RuntimeException("Incurred " + failures + " failures" + " testing BigDecimal.sqrt().");
        }
    }

    private static int negativeTests() {
        int failures = 0;
        for (long i = -10; i < 0; i++) {
            for (int j = -5; j < 5; j++) {
                try {
                    BigDecimal input = BigDecimal.valueOf(i, j);
                    BigDecimal result = input.sqrt(MathContext.DECIMAL64);
                    System.err.println("Unexpected sqrt of negative: (" + input + ").sqrt()  = " + result);
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
            BigDecimal expected = BigDecimal.valueOf(0L, i / 2);
            failures += compare(BigDecimal.valueOf(0L, i).sqrt(MathContext.UNLIMITED), expected, true, "zeros");
            failures += compare(BigDecimal.valueOf(0L, i).sqrt(MathContext.DECIMAL64), expected, true, "zeros");
        }
        return failures;
    }

    private static int oneDigitTests() {
        int failures = 0;
        List<BigDecimal> oneToNine = List.of(ONE, TWO, valueOf(3), valueOf(4), valueOf(5), valueOf(6), valueOf(7), valueOf(8), valueOf(9));
        List<RoundingMode> modes = List.of(RoundingMode.UP, RoundingMode.DOWN, RoundingMode.CEILING, RoundingMode.FLOOR, RoundingMode.HALF_UP, RoundingMode.HALF_DOWN, RoundingMode.HALF_EVEN);
        for (int i = 1; i < 20; i++) {
            for (RoundingMode rm : modes) {
                for (BigDecimal bd : oneToNine) {
                    MathContext mc = new MathContext(i, rm);
                    failures += compareSqrtImplementations(bd, mc);
                    bd = bd.multiply(ONE_TENTH);
                    failures += compareSqrtImplementations(bd, mc);
                }
            }
        }
        return failures;
    }

    private static int twoDigitTests() {
        int failures = 0;
        List<RoundingMode> modes = List.of(RoundingMode.UP, RoundingMode.DOWN, RoundingMode.CEILING, RoundingMode.FLOOR, RoundingMode.HALF_UP, RoundingMode.HALF_DOWN, RoundingMode.HALF_EVEN);
        for (int i = 10; i < 100; i++) {
            BigDecimal bd0 = BigDecimal.valueOf(i);
            BigDecimal bd1 = bd0.multiply(ONE_TENTH);
            BigDecimal bd2 = bd1.multiply(ONE_TENTH);
            for (BigDecimal bd : List.of(bd0, bd1, bd2)) {
                for (int precision = 1; i < 20; i++) {
                    for (RoundingMode rm : modes) {
                        MathContext mc = new MathContext(precision, rm);
                        failures += compareSqrtImplementations(bd, mc);
                    }
                }
            }
        }
        return failures;
    }

    private static int compareSqrtImplementations(BigDecimal bd, MathContext mc) {
        return equalNumerically(BigSquareRoot.sqrt(bd, mc), bd.sqrt(mc), "sqrt(" + bd + ") under " + mc);
    }

    private static int evenPowersOfTenTests() {
        int failures = 0;
        MathContext oneDigitExactly = new MathContext(1, RoundingMode.UNNECESSARY);
        for (int scale = -100; scale <= 100; scale++) {
            BigDecimal testValue = BigDecimal.valueOf(1, 2 * scale);
            BigDecimal expectedNumericalResult = BigDecimal.valueOf(1, scale);
            BigDecimal result;
            failures += equalNumerically(expectedNumericalResult, result = testValue.sqrt(MathContext.DECIMAL64), "Even powers of 10, DECIMAL64");
            failures += equalNumerically(expectedNumericalResult, result = testValue.sqrt(oneDigitExactly), "even powers of 10, 1 digit");
            if (result.precision() > 1) {
                failures += 1;
                System.err.println("Excess precision for " + result);
            }
        }
        return failures;
    }

    private static int squareRootTwoTests() {
        int failures = 0;
        BigDecimal highPrecisionRoot2 = new BigDecimal("1.41421356237309504880168872420969807856967187537694807317667973799");
        RoundingMode[] modes = { RoundingMode.UP, RoundingMode.DOWN, RoundingMode.CEILING, RoundingMode.FLOOR, RoundingMode.HALF_UP, RoundingMode.HALF_DOWN, RoundingMode.HALF_EVEN };
        for (RoundingMode mode : modes) {
            for (int precision = 1; precision < 63; precision++) {
                MathContext mc = new MathContext(precision, mode);
                BigDecimal expected = highPrecisionRoot2.round(mc);
                BigDecimal computed = TWO.sqrt(mc);
                BigDecimal altComputed = BigSquareRoot.sqrt(TWO, mc);
                failures += equalNumerically(expected, computed, "sqrt(2)");
                failures += equalNumerically(computed, altComputed, "computed & altComputed");
            }
        }
        return failures;
    }

    private static int lowPrecisionPerfectSquares() {
        int failures = 0;
        long[][] squaresWithOneDigitRoot = { { 4, 2 }, { 9, 3 }, { 25, 5 }, { 36, 6 }, { 49, 7 }, { 64, 8 }, { 81, 9 } };
        for (long[] squareAndRoot : squaresWithOneDigitRoot) {
            BigDecimal square = new BigDecimal(squareAndRoot[0]);
            BigDecimal expected = new BigDecimal(squareAndRoot[1]);
            for (int scale = 0; scale <= 4; scale++) {
                BigDecimal scaledSquare = square.setScale(scale, RoundingMode.UNNECESSARY);
                int expectedScale = scale / 2;
                for (int precision = 0; precision <= 5; precision++) {
                    for (RoundingMode rm : RoundingMode.values()) {
                        MathContext mc = new MathContext(precision, rm);
                        BigDecimal computedRoot = scaledSquare.sqrt(mc);
                        failures += equalNumerically(expected, computedRoot, "simple squares");
                        int computedScale = computedRoot.scale();
                        if (precision >= expectedScale + 1 && computedScale != expectedScale) {
                            System.err.printf("%s\tprecision=%d\trm=%s%n", computedRoot.toString(), precision, rm);
                            failures++;
                            System.err.printf("\t%s does not have expected scale of %d%n.", computedRoot, expectedScale);
                        }
                    }
                }
            }
        }
        return failures;
    }

    private static int almostFourRoundingDown() {
        int failures = 0;
        BigDecimal nearFour = new BigDecimal("3.999999999999999999999999999999");
        for (int i = 1; i < 64; i++) {
            MathContext mc = new MathContext(i, RoundingMode.FLOOR);
            BigDecimal result = nearFour.sqrt(mc);
            BigDecimal expected = BigSquareRoot.sqrt(nearFour, mc);
            failures += equalNumerically(expected, result, "near four rounding down");
            failures += (result.compareTo(TWO) < 0) ? 0 : 1;
        }
        return failures;
    }

    private static int almostFourRoundingUp() {
        int failures = 0;
        BigDecimal nearFour = new BigDecimal("4.000000000000000000000000000001");
        for (int i = 1; i < 64; i++) {
            MathContext mc = new MathContext(i, RoundingMode.CEILING);
            BigDecimal result = nearFour.sqrt(mc);
            BigDecimal expected = BigSquareRoot.sqrt(nearFour, mc);
            failures += equalNumerically(expected, result, "near four rounding up");
            failures += (result.compareTo(TWO) > 0) ? 0 : 1;
        }
        return failures;
    }

    private static int nearTen() {
        int failures = 0;
        BigDecimal near10 = new BigDecimal("9.99999999999999999999");
        BigDecimal near10sq = near10.multiply(near10);
        BigDecimal near10sq_ulp = near10sq.add(near10sq.ulp());
        for (int i = 10; i < 23; i++) {
            MathContext mc = new MathContext(i, RoundingMode.HALF_EVEN);
            failures += equalNumerically(BigSquareRoot.sqrt(near10sq_ulp, mc), near10sq_ulp.sqrt(mc), "near 10 rounding half even");
        }
        return failures;
    }

    private static int nearOne() {
        int failures = 0;
        BigDecimal near1 = new BigDecimal(".999999999999999999999");
        BigDecimal near1sq = near1.multiply(near1);
        BigDecimal near1sq_ulp = near1sq.add(near1sq.ulp());
        for (int i = 10; i < 23; i++) {
            for (RoundingMode rm : List.of(RoundingMode.HALF_EVEN, RoundingMode.UP, RoundingMode.DOWN)) {
                MathContext mc = new MathContext(i, rm);
                failures += equalNumerically(BigSquareRoot.sqrt(near1sq_ulp, mc), near1sq_ulp.sqrt(mc), mc.toString());
            }
        }
        return failures;
    }

    private static int halfWay() {
        int failures = 0;
        BigDecimal[] halfWayCases = { new BigDecimal("123456789123456789.5"), new BigDecimal("123456789123456788.5") };
        for (BigDecimal halfWayCase : halfWayCases) {
            int precision = halfWayCase.precision() - 1;
            BigDecimal square = halfWayCase.multiply(halfWayCase);
            for (RoundingMode rm : List.of(RoundingMode.HALF_EVEN, RoundingMode.HALF_UP, RoundingMode.HALF_DOWN)) {
                MathContext mc = new MathContext(precision, rm);
                System.out.println("\nRounding mode " + rm);
                System.out.println("\t" + halfWayCase.round(mc) + "\t" + halfWayCase);
                System.out.println("\t" + BigSquareRoot.sqrt(square, mc));
                failures += equalNumerically(BigSquareRoot.sqrt(square, mc), halfWayCase.round(mc), "Rounding halway " + rm);
            }
        }
        return failures;
    }

    private static int compare(BigDecimal a, BigDecimal b, boolean expected, String prefix) {
        boolean result = a.equals(b);
        int failed = (result == expected) ? 0 : 1;
        if (failed == 1) {
            System.err.println("Testing " + prefix + "(" + a + ").compareTo(" + b + ") => " + result + "\n\tExpected " + expected);
        }
        return failed;
    }

    private static int equalNumerically(BigDecimal a, BigDecimal b, String prefix) {
        return compareNumerically(a, b, 0, prefix);
    }

    private static int compareNumerically(BigDecimal a, BigDecimal b, int expected, String prefix) {
        int result = a.compareTo(b);
        int failed = (result == expected) ? 0 : 1;
        if (failed == 1) {
            System.err.println("Testing " + prefix + "(" + a + ").compareTo(" + b + ") => " + result + "\n\tExpected " + expected);
        }
        return failed;
    }

    private static class BigSquareRoot {

        private static final BigDecimal ONE_HALF = valueOf(5L, 1);

        public static boolean isPowerOfTen(BigDecimal bd) {
            return BigInteger.ONE.equals(bd.unscaledValue());
        }

        public static BigDecimal square(BigDecimal bd) {
            return bd.multiply(bd);
        }

        public static BigDecimal sqrt(BigDecimal bd, MathContext mc) {
            int signum = bd.signum();
            if (signum == 1) {
                int preferredScale = bd.scale() / 2;
                BigDecimal zeroWithFinalPreferredScale = BigDecimal.valueOf(0L, preferredScale);
                BigDecimal stripped = bd.stripTrailingZeros();
                int strippedScale = stripped.scale();
                if (isPowerOfTen(stripped) && strippedScale % 2 == 0) {
                    BigDecimal result = BigDecimal.valueOf(1L, strippedScale / 2);
                    if (result.scale() != preferredScale) {
                        result = result.add(zeroWithFinalPreferredScale, mc);
                    }
                    return result;
                }
                int scaleAdjust = 0;
                int scale = stripped.scale() - stripped.precision() + 1;
                if (scale % 2 == 0) {
                    scaleAdjust = scale;
                } else {
                    scaleAdjust = scale - 1;
                }
                BigDecimal working = stripped.scaleByPowerOfTen(scaleAdjust);
                assert ONE_TENTH.compareTo(working) <= 0 && working.compareTo(TEN) < 0;
                BigDecimal guess = new BigDecimal(Math.sqrt(working.doubleValue()));
                int guessPrecision = 15;
                int originalPrecision = mc.getPrecision();
                int targetPrecision;
                if (originalPrecision == 0) {
                    targetPrecision = stripped.precision() / 2 + 1;
                } else {
                    targetPrecision = originalPrecision;
                }
                BigDecimal approx = guess;
                int workingPrecision = working.precision();
                int loopPrecision = Math.max(2 * Math.max(targetPrecision, workingPrecision) + 2, 34);
                do {
                    MathContext mcTmp = new MathContext(loopPrecision, RoundingMode.HALF_EVEN);
                    approx = ONE_HALF.multiply(approx.add(working.divide(approx, mcTmp), mcTmp));
                    guessPrecision *= 2;
                } while (guessPrecision < loopPrecision);
                BigDecimal result;
                RoundingMode targetRm = mc.getRoundingMode();
                if (targetRm == RoundingMode.UNNECESSARY || originalPrecision == 0) {
                    RoundingMode tmpRm = (targetRm == RoundingMode.UNNECESSARY) ? RoundingMode.DOWN : targetRm;
                    MathContext mcTmp = new MathContext(targetPrecision, tmpRm);
                    result = approx.scaleByPowerOfTen(-scaleAdjust / 2).round(mcTmp);
                    if (bd.subtract(square(result)).compareTo(ZERO) != 0) {
                        throw new ArithmeticException("Computed square root not exact.");
                    }
                } else {
                    result = approx.scaleByPowerOfTen(-scaleAdjust / 2).round(mc);
                }
                assert squareRootResultAssertions(bd, result, mc);
                if (result.scale() != preferredScale) {
                    result = result.stripTrailingZeros().add(zeroWithFinalPreferredScale, new MathContext(originalPrecision, RoundingMode.UNNECESSARY));
                }
                return result;
            } else {
                switch(signum) {
                    case -1:
                        throw new ArithmeticException("Attempted square root " + "of negative BigDecimal");
                    case 0:
                        return valueOf(0L, bd.scale() / 2);
                    default:
                        throw new AssertionError("Bad value from signum");
                }
            }
        }

        private static boolean squareRootResultAssertions(BigDecimal input, BigDecimal result, MathContext mc) {
            if (result.signum() == 0) {
                return squareRootZeroResultAssertions(input, result, mc);
            } else {
                RoundingMode rm = mc.getRoundingMode();
                BigDecimal ulp = result.ulp();
                BigDecimal neighborUp = result.add(ulp);
                if (isPowerOfTen(result)) {
                    ulp = ulp.divide(TEN);
                }
                BigDecimal neighborDown = result.subtract(ulp);
                if (result.signum() != 1 || input.signum() != 1) {
                    return false;
                }
                switch(rm) {
                    case DOWN:
                    case FLOOR:
                        assert square(result).compareTo(input) <= 0 && square(neighborUp).compareTo(input) > 0 : "Square of result out for bounds rounding " + rm;
                        return true;
                    case UP:
                    case CEILING:
                        assert square(result).compareTo(input) >= 0 : "Square of result too small rounding " + rm;
                        assert square(neighborDown).compareTo(input) < 0 : "Square of down neighbor too large rounding  " + rm + "\n" + "\t input: " + input + "\t neighborDown: " + neighborDown + "\t sqrt: " + result + "\t" + mc;
                        return true;
                    case HALF_DOWN:
                    case HALF_EVEN:
                    case HALF_UP:
                        BigDecimal err = square(result).subtract(input).abs();
                        BigDecimal errUp = square(neighborUp).subtract(input);
                        BigDecimal errDown = input.subtract(square(neighborDown));
                        int err_comp_errUp = err.compareTo(errUp);
                        int err_comp_errDown = err.compareTo(errDown);
                        assert errUp.signum() == 1 && errDown.signum() == 1 : "Errors of neighbors squared don't have correct signs";
                        assert ((err_comp_errUp == 0) ? err_comp_errDown < 0 : true) && ((err_comp_errDown == 0) ? err_comp_errUp < 0 : true) : "Incorrect error relationships";
                        return true;
                    default:
                        return true;
                }
            }
        }

        private static boolean squareRootZeroResultAssertions(BigDecimal input, BigDecimal result, MathContext mc) {
            return input.compareTo(ZERO) == 0;
        }
    }
}
