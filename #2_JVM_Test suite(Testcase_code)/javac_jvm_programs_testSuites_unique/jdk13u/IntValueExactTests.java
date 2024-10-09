import java.math.*;
import java.util.List;
import java.util.Map;
import static java.util.Map.entry;

public class IntValueExactTests {

    public static void main(String... args) {
        int failures = 0;
        failures += intValueExactSuccessful();
        failures += intValueExactExceptional();
        if (failures > 0) {
            throw new RuntimeException("Incurred " + failures + " failures while testing intValueExact.");
        }
    }

    private static int simpleIntValueExact(BigDecimal bd) {
        return bd.toBigIntegerExact().intValue();
    }

    private static int intValueExactSuccessful() {
        int failures = 0;
        Map<BigDecimal, Integer> successCases = 
        Map.ofEntries(entry(new BigDecimal("2147483647"), Integer.MAX_VALUE), entry(new BigDecimal("2147483647.0"), Integer.MAX_VALUE), entry(new BigDecimal("2147483647.00"), Integer.MAX_VALUE), entry(new BigDecimal("-2147483648"), Integer.MIN_VALUE), entry(new BigDecimal("-2147483648.0"), Integer.MIN_VALUE), entry(new BigDecimal("-2147483648.00"), Integer.MIN_VALUE), entry(new BigDecimal("1e0"), 1), entry(new BigDecimal(BigInteger.ONE, -9), 1_000_000_000), entry(new BigDecimal("0e13"), 0), entry(new BigDecimal("0e32"), 0), entry(new BigDecimal("0e512"), 0), entry(new BigDecimal("10.000000000000000000000000000000000"), 10));
        for (var testCase : successCases.entrySet()) {
            BigDecimal bd = testCase.getKey();
            int expected = testCase.getValue();
            try {
                int intValueExact = bd.intValueExact();
                if (expected != intValueExact || intValueExact != simpleIntValueExact(bd)) {
                    failures++;
                    System.err.println("Unexpected intValueExact result " + intValueExact + " on " + bd);
                }
            } catch (Exception e) {
                failures++;
                System.err.println("Error on " + bd + "\tException message:" + e.getMessage());
            }
        }
        return failures;
    }

    private static int intValueExactExceptional() {
        int failures = 0;
        List<BigDecimal> exceptionalCases = 
        List.of(new BigDecimal("2147483648"), new BigDecimal("2147483648.0"), new BigDecimal("2147483648.00"), new BigDecimal("-2147483649"), new BigDecimal("-2147483649.1"), new BigDecimal("-2147483649.01"), new BigDecimal("9999999999999999999999999999999"), new BigDecimal("10000000000000000000000000000000"), new BigDecimal("0.99"), new BigDecimal("0.999999999999999999999"));
        for (BigDecimal bd : exceptionalCases) {
            try {
                int intValueExact = bd.intValueExact();
                failures++;
                System.err.println("Unexpected non-exceptional intValueExact on " + bd);
            } catch (ArithmeticException e) {
            }
        }
        return failures;
    }
}
