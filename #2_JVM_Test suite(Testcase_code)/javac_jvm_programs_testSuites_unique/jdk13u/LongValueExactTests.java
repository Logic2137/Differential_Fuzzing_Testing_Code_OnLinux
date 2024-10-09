import java.math.*;
import java.util.List;
import java.util.Map;
import static java.util.Map.entry;

public class LongValueExactTests {

    public static void main(String... args) {
        int failures = 0;
        failures += longValueExactSuccessful();
        failures += longValueExactExceptional();
        if (failures > 0) {
            throw new RuntimeException("Incurred " + failures + " failures while testing longValueExact.");
        }
    }

    private static long simpleLongValueExact(BigDecimal bd) {
        return bd.toBigIntegerExact().longValue();
    }

    private static int longValueExactSuccessful() {
        int failures = 0;
        Map<BigDecimal, Long> successCases = 
        Map.ofEntries(entry(new BigDecimal("9223372036854775807"), Long.MAX_VALUE), entry(new BigDecimal("9223372036854775807.0"), Long.MAX_VALUE), entry(new BigDecimal("9223372036854775807.00"), Long.MAX_VALUE), entry(new BigDecimal("-9223372036854775808"), Long.MIN_VALUE), entry(new BigDecimal("-9223372036854775808.0"), Long.MIN_VALUE), entry(new BigDecimal("-9223372036854775808.00"), Long.MIN_VALUE), entry(new BigDecimal("1e0"), 1L), entry(new BigDecimal(BigInteger.ONE, -18), 1_000_000_000_000_000_000L), entry(new BigDecimal("0e13"), 0L), entry(new BigDecimal("0e64"), 0L), entry(new BigDecimal("0e1024"), 0L), entry(new BigDecimal("10.000000000000000000000000000000000"), 10L));
        for (var testCase : successCases.entrySet()) {
            BigDecimal bd = testCase.getKey();
            long expected = testCase.getValue();
            try {
                long longValueExact = bd.longValueExact();
                if (expected != longValueExact || longValueExact != simpleLongValueExact(bd)) {
                    failures++;
                    System.err.println("Unexpected longValueExact result " + longValueExact + " on " + bd);
                }
            } catch (Exception e) {
                failures++;
                System.err.println("Error on " + bd + "\tException message:" + e.getMessage());
            }
        }
        return failures;
    }

    private static int longValueExactExceptional() {
        int failures = 0;
        List<BigDecimal> exceptionalCases = 
        List.of(new BigDecimal("9223372036854775808"), new BigDecimal("9223372036854775808.0"), new BigDecimal("9223372036854775808.00"), new BigDecimal("-9223372036854775809"), new BigDecimal("-9223372036854775808.1"), new BigDecimal("-9223372036854775808.01"), new BigDecimal("9999999999999999999"), new BigDecimal("10000000000000000000"), new BigDecimal("0.99"), new BigDecimal("0.999999999999999999999"));
        for (BigDecimal bd : exceptionalCases) {
            try {
                long longValueExact = bd.longValueExact();
                failures++;
                System.err.println("Unexpected non-exceptional longValueExact on " + bd);
            } catch (ArithmeticException e) {
            }
        }
        return failures;
    }
}
