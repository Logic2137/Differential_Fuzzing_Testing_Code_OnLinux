

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static java.lang.Double.*;


public class TestDoubleSumAverage {
    public static void main(String... args) {
        int failures = 0;

        failures += testZeroAverageOfNonEmptyStream();
        failures += testForCompenstation();
        failures += testNonfiniteSum();

        if (failures > 0) {
            throw new RuntimeException("Found " + failures + " numerical failure(s).");
        }
    }

    
    private static int testZeroAverageOfNonEmptyStream() {
        Supplier<DoubleStream> ds = () -> DoubleStream.iterate(0.0, e -> 0.0).limit(10);

        return  compareUlpDifference(0.0, ds.get().average().getAsDouble(), 0);
    }

    
    private static int testForCompenstation() {
        int failures = 0;

        
        double base = 1.0;
        double increment = Math.ulp(base)/2.0;
        int count = 1_000_001;

        double expectedSum = base + (increment * (count - 1));
        double expectedAvg = expectedSum / count;

        
        Supplier<DoubleStream> ds = () -> DoubleStream.iterate(base, e -> increment).limit(count);

        DoubleSummaryStatistics stats = ds.get().collect(DoubleSummaryStatistics::new,
                                                         DoubleSummaryStatistics::accept,
                                                         DoubleSummaryStatistics::combine);

        failures += compareUlpDifference(expectedSum, stats.getSum(), 3);
        failures += compareUlpDifference(expectedAvg, stats.getAverage(), 3);

        failures += compareUlpDifference(expectedSum,
                                         ds.get().sum(), 3);
        failures += compareUlpDifference(expectedAvg,
                                         ds.get().average().getAsDouble(), 3);

        failures += compareUlpDifference(expectedSum,
                                         ds.get().boxed().collect(Collectors.summingDouble(d -> d)), 3);
        failures += compareUlpDifference(expectedAvg,
                                         ds.get().boxed().collect(Collectors.averagingDouble(d -> d)),3);
        return failures;
    }

    private static int testNonfiniteSum() {
        int failures = 0;

        Map<Supplier<DoubleStream>, Double> testCases = new LinkedHashMap<>();
        testCases.put(() -> DoubleStream.of(MAX_VALUE, MAX_VALUE),   POSITIVE_INFINITY);
        testCases.put(() -> DoubleStream.of(-MAX_VALUE, -MAX_VALUE), NEGATIVE_INFINITY);

        testCases.put(() -> DoubleStream.of(1.0d, POSITIVE_INFINITY, 1.0d), POSITIVE_INFINITY);
        testCases.put(() -> DoubleStream.of(POSITIVE_INFINITY),             POSITIVE_INFINITY);
        testCases.put(() -> DoubleStream.of(POSITIVE_INFINITY, POSITIVE_INFINITY), POSITIVE_INFINITY);
        testCases.put(() -> DoubleStream.of(POSITIVE_INFINITY, POSITIVE_INFINITY, 0.0), POSITIVE_INFINITY);

        testCases.put(() -> DoubleStream.of(1.0d, NEGATIVE_INFINITY, 1.0d), NEGATIVE_INFINITY);
        testCases.put(() -> DoubleStream.of(NEGATIVE_INFINITY),             NEGATIVE_INFINITY);
        testCases.put(() -> DoubleStream.of(NEGATIVE_INFINITY, NEGATIVE_INFINITY), NEGATIVE_INFINITY);
        testCases.put(() -> DoubleStream.of(NEGATIVE_INFINITY, NEGATIVE_INFINITY, 0.0), NEGATIVE_INFINITY);

        testCases.put(() -> DoubleStream.of(1.0d, NaN, 1.0d),               NaN);
        testCases.put(() -> DoubleStream.of(NaN),                           NaN);
        testCases.put(() -> DoubleStream.of(1.0d, NEGATIVE_INFINITY, POSITIVE_INFINITY, 1.0d), NaN);
        testCases.put(() -> DoubleStream.of(1.0d, POSITIVE_INFINITY, NEGATIVE_INFINITY, 1.0d), NaN);
        testCases.put(() -> DoubleStream.of(POSITIVE_INFINITY, NaN), NaN);
        testCases.put(() -> DoubleStream.of(NEGATIVE_INFINITY, NaN), NaN);
        testCases.put(() -> DoubleStream.of(NaN, POSITIVE_INFINITY), NaN);
        testCases.put(() -> DoubleStream.of(NaN, NEGATIVE_INFINITY), NaN);

        for(Map.Entry<Supplier<DoubleStream>, Double> testCase : testCases.entrySet()) {
            Supplier<DoubleStream> ds = testCase.getKey();
            double expected = testCase.getValue();

            DoubleSummaryStatistics stats = ds.get().collect(DoubleSummaryStatistics::new,
                                                             DoubleSummaryStatistics::accept,
                                                             DoubleSummaryStatistics::combine);

            failures += compareUlpDifference(expected, stats.getSum(), 0);
            failures += compareUlpDifference(expected, stats.getAverage(), 0);

            failures += compareUlpDifference(expected, ds.get().sum(), 0);
            failures += compareUlpDifference(expected, ds.get().average().getAsDouble(), 0);

            failures += compareUlpDifference(expected, ds.get().boxed().collect(Collectors.summingDouble(d -> d)), 0);
            failures += compareUlpDifference(expected, ds.get().boxed().collect(Collectors.averagingDouble(d -> d)), 0);
        }

        return failures;
    }

    
    private static int compareUlpDifference(double expected, double computed, double threshold) {
        if (!Double.isFinite(expected)) {
            
            if (Double.compare(expected, computed) == 0)
                return 0;
            else {
                System.err.printf("Unexpected sum, %g rather than %g.%n",
                                  computed, expected);
                return 1;
            }
        }

        double ulpDifference = Math.abs(expected - computed) / Math.ulp(expected);

        if (ulpDifference > threshold) {
            System.err.printf("Numerical summation error too large, %g ulps rather than %g.%n",
                              ulpDifference, threshold);
            return 1;
        } else
            return 0;
    }
}
