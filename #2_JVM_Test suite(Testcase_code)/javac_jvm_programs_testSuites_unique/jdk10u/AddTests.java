



import java.math.*;
import static java.math.BigDecimal.*;
import java.util.Set;
import java.util.EnumSet;

public class AddTests {

    private static Set<RoundingMode> nonExactRoundingModes =
        EnumSet.complementOf(EnumSet.of(RoundingMode.UNNECESSARY));

    
    private static int simpleTests() {
        int failures = 0;

        BigDecimal[] bd1 = {
            new BigDecimal(new BigInteger("7812404666936930160"), 11),
            new BigDecimal(new BigInteger("7812404666936930160"), 12),
            new BigDecimal(new BigInteger("7812404666936930160"), 13),
        };
        BigDecimal bd2 = new BigDecimal(new BigInteger("2790000"), 1);
        BigDecimal[] expectedResult = {
            new BigDecimal("78403046.66936930160"),
            new BigDecimal("8091404.666936930160"),
            new BigDecimal("1060240.4666936930160"),
        };
        for (int i = 0; i < bd1.length; i++) {
            if (!bd1[i].add(bd2).equals(expectedResult[i]))
                failures++;
        }
        return failures;
    }

    
    private static int extremaTests() {
        int failures = 0;

        failures += addWithoutException(valueOf(1, -Integer.MAX_VALUE),
                                        valueOf(2, Integer.MAX_VALUE), null);
        failures += addWithoutException(valueOf(1, -Integer.MAX_VALUE),
                                        valueOf(-2, Integer.MAX_VALUE), null);
        return failures;
    }

    
    private static int addWithoutException(BigDecimal b1, BigDecimal b2, MathContext mc) {
        if (mc == null)
            mc = new MathContext(2, RoundingMode.DOWN);

        try {
            BigDecimal sum = b1.add(b2, mc);
            printAddition(b1, b2, sum.toString());
            return 0;
        } catch(ArithmeticException ae) {
            printAddition(b1, b2, "Exception!");
            return 1;
        }
    }

    
    private static int roundingGradationTests() {
        int failures = 0;

        failures += roundAway(new BigDecimal("1234e100"),
                              new BigDecimal(   "1234e97"));

        failures += roundAway(new BigDecimal("1234e100"),
                              new BigDecimal(    "1234e96"));

        failures += roundAway(new BigDecimal("1234e100"),
                              new BigDecimal(     "1234e95"));

        failures += roundAway(new BigDecimal("1234e100"),
                              new BigDecimal(      "1234e94"));

        failures += roundAway(new BigDecimal("1234e100"),
                              new BigDecimal(       "1234e93"));

        failures += roundAway(new BigDecimal("1234e100"),
                              new BigDecimal(        "1234e92"));

        failures += roundAway(new BigDecimal("1234e100"),
                              new BigDecimal("1234e50"));


        failures += roundAway(new BigDecimal("1000e100"),
                              new BigDecimal(   "1234e97"));

        failures += roundAway(new BigDecimal("1000e100"),
                              new BigDecimal(    "1234e96"));

        failures += roundAway(new BigDecimal("1000e100"),
                              new BigDecimal(     "1234e95"));

        failures += roundAway(new BigDecimal("1000e100"),
                              new BigDecimal(      "1234e94"));

        failures += roundAway(new BigDecimal("1000e100"),
                              new BigDecimal(       "1234e93"));

        failures += roundAway(new BigDecimal("1000e100"),
                              new BigDecimal(        "1234e92"));

        failures += roundAway(new BigDecimal("1000e100"),
                              new BigDecimal("1234e50"));



        failures += roundAway(new BigDecimal("1999e100"),
                              new BigDecimal(   "1234e97"));

        failures += roundAway(new BigDecimal("1999e100"),
                              new BigDecimal(    "1234e96"));

        failures += roundAway(new BigDecimal("1999e100"),
                              new BigDecimal(     "1234e95"));

        failures += roundAway(new BigDecimal("1999e100"),
                              new BigDecimal(      "1234e94"));

        failures += roundAway(new BigDecimal("1999e100"),
                              new BigDecimal(       "1234e93"));

        failures += roundAway(new BigDecimal("1999e100"),
                              new BigDecimal(        "1234e92"));

        failures += roundAway(new BigDecimal("1999e100"),
                              new BigDecimal("1234e50"));



        failures += roundAway(new BigDecimal("9999e100"),
                              new BigDecimal(   "1234e97"));

        failures += roundAway(new BigDecimal("9999e100"),
                              new BigDecimal(    "1234e96"));

        failures += roundAway(new BigDecimal("9999e100"),
                              new BigDecimal(     "1234e95"));

        failures += roundAway(new BigDecimal("9999e100"),
                              new BigDecimal(      "1234e94"));

        failures += roundAway(new BigDecimal("9999e100"),
                              new BigDecimal(       "1234e93"));

        failures += roundAway(new BigDecimal("9999e100"),
                              new BigDecimal(        "1234e92"));

        failures += roundAway(new BigDecimal("9999e100"),
                              new BigDecimal("1234e50"));

        return failures;
    }

    private static void printAddition(BigDecimal b1, BigDecimal b2, String s) {
        System.out.println("" + b1+ "\t+\t" + b2 + "\t=\t" + s);
    }

    private static int roundAway(BigDecimal b1, BigDecimal b2) {
        int failures = 0;

        b1.precision();
        b2.precision();

        BigDecimal b1_negate = b1.negate();
        BigDecimal b2_negate = b2.negate();

        b1_negate.precision();
        b2_negate.precision();

        failures += roundAway1(b1,        b2);
        failures += roundAway1(b1,        b2_negate);
        failures += roundAway1(b1_negate, b2);
        failures += roundAway1(b1_negate, b2_negate);

        return failures;
    }

    private static int roundAway1(BigDecimal b1, BigDecimal b2) {
        int failures = 0;
        failures += roundAway0(b1, b2);
        failures += roundAway0(b2, b1);
        return failures;
    }

    
    private static int roundAway0(BigDecimal b1, BigDecimal b2) {
        int failures = 0;
        BigDecimal exactSum = b1.add(b2);

        for(int precision = 1 ; precision < exactSum.precision()+2; precision++) {
            for(RoundingMode rm : nonExactRoundingModes) {
                MathContext mc = new MathContext(precision, rm);
                BigDecimal roundedExactSum = exactSum.round(mc);

                try {
                    BigDecimal sum = b1.add(b2, mc);

                    if (!roundedExactSum.equals(sum) ) {
                        failures++;
                        System.out.println("Exact sum " + exactSum +
                                           "\trounded by " + mc +
                                           "\texpected: " + roundedExactSum + " got: ");
                        printAddition(b1, b2, sum.toString());
                    }





                } catch (ArithmeticException ae) {
                    printAddition(b1, b2, "Exception!");
                    failures++;
                }
            }
        }

        return failures;
    }

    
    private static int precisionConsistencyTest() {
        int failures = 0;
        MathContext mc = new MathContext(1,RoundingMode.DOWN);
        BigDecimal a = BigDecimal.valueOf(1999, -1); 

        BigDecimal sum1 = a.add(BigDecimal.ONE, mc);
        a.precision();
        BigDecimal sum2 = a.add(BigDecimal.ONE, mc);

        if (!sum1.equals(sum2)) {
            failures ++;
            System.out.println("Unequal sums after calling precision!");
            System.out.print("Before:\t");
            printAddition(a, BigDecimal.ONE, sum1.toString());

            System.out.print("After:\t");
            printAddition(a, BigDecimal.ONE, sum2.toString());
        }

        return failures;
    }

    public static void main(String argv[]) {
        int failures = 0;

        failures += extremaTests();
        failures += roundingGradationTests();
        failures += precisionConsistencyTest();

        if (failures > 0) {
            throw new RuntimeException("Incurred " + failures +
                                       " failures while testing rounding add.");
        }
    }
}
