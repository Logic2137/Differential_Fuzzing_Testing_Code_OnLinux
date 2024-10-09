import java.util.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;

public class FormatMicroBenchmark {

    private static final int NB_RUNS = 20;

    private static final int MAX_RANGE = 500000;

    private static boolean Verbose = false;

    private static boolean DoIt = false;

    private static void usage() {
        System.out.println("This is a set of micro-benchmarks testing throughput of " + "java.text.DecimalFormat.format(). It never fails.\n\n" + "Usage and arguments:\n" + " - Run with no argument skips the whole benchmark and exits.\n" + " - Run with \"-help\" as first argument prints this message and exits.\n" + " - Run with \"-doit\" runs the benchmark with summary details.\n" + " - Run with \"-verbose\" provides additional details on the run.\n\n" + "Example run :\n" + "   java -Xms500m -Xmx500m -XX:NewSize=400m FormatMicroBenchmark -doit -verbose\n\n" + "Note: \n" + " - Vm options -Xms, -Xmx, -XX:NewSize must be set correctly for \n" + "   getting reliable numbers. Otherwise GC activity may corrupt results.\n" + "   As of jdk80b48 using \"-Xms500m -Xmx500m -XX:NewSize=400m\" covers \n" + "   all cases.\n" + " - Optionally using \"-Xlog:gc\" option provides information that \n" + "   helps checking any GC activity while benches are run.\n\n" + "Look at the heading comments and description in source code for " + "detailed information.\n");
    }

    private static long stabilizeMemory(boolean reportConsumedMemory) {
        final long oneMegabyte = 1024L * 1024L;
        long refMemory = 0;
        long initialMemoryLeft = Runtime.getRuntime().freeMemory();
        long currMemoryLeft = initialMemoryLeft;
        int nbGCCalls = 0;
        do {
            nbGCCalls++;
            refMemory = currMemoryLeft;
            System.gc();
            currMemoryLeft = Runtime.getRuntime().freeMemory();
        } while ((Math.abs(currMemoryLeft - refMemory) > oneMegabyte) && (nbGCCalls < 10));
        if (Verbose && reportConsumedMemory)
            System.out.println("Memory consumed by previous run : " + (currMemoryLeft - initialMemoryLeft) / oneMegabyte + "Mbs.");
        return currMemoryLeft;
    }

    private static final String INTEGER_BENCH = "benchFormatInteger";

    private static String benchFormatInteger(NumberFormat nf) {
        String str = "";
        for (int j = -MAX_RANGE; j <= MAX_RANGE; j++) str = nf.format((double) j);
        return str;
    }

    static double integerThroughputLoad() {
        double d = 0.0d;
        for (int j = -MAX_RANGE; j <= MAX_RANGE; j++) {
            d = (double) j;
        }
        return d;
    }

    static void calculateIntegerThroughputLoad() {
        int nbRuns = NB_RUNS;
        long elapsedTime = 0;
        double foo;
        for (int i = 1; i <= nbRuns; i++) {
            long startTime = System.nanoTime();
            foo = integerThroughputLoad();
            long estimatedTime = System.nanoTime() - startTime;
            if (i > 3)
                elapsedTime += estimatedTime / 1000;
        }
        if (Verbose)
            System.out.println("calculated throughput load for " + INTEGER_BENCH + " bench is = " + (elapsedTime / (nbRuns - 3)) + " microseconds");
    }

    private static final String FRACTIONAL_BENCH = "benchFormatFractional";

    private static String benchFormatFractional(NumberFormat nf) {
        String str = "";
        double floatingN = 1.0d / (double) MAX_RANGE;
        for (int j = -MAX_RANGE; j <= MAX_RANGE; j++) str = nf.format(floatingN * (double) j);
        return str;
    }

    static double fractionalThroughputLoad() {
        double d = 0.0d;
        double floatingN = 1.0d / (double) MAX_RANGE;
        for (int j = -MAX_RANGE; j <= MAX_RANGE; j++) {
            d = floatingN * (double) j;
        }
        return d;
    }

    static void calculateFractionalThroughputLoad() {
        int nbRuns = NB_RUNS;
        long elapsedTime = 0;
        double foo;
        for (int i = 1; i <= nbRuns; i++) {
            long startTime = System.nanoTime();
            foo = fractionalThroughputLoad();
            long estimatedTime = System.nanoTime() - startTime;
            if (i > 3)
                elapsedTime += estimatedTime / 1000;
        }
        if (Verbose)
            System.out.println("calculated throughput load for " + FRACTIONAL_BENCH + " bench is = " + (elapsedTime / (nbRuns - 3)) + " microseconds");
    }

    private static final String SMALL_INTEGRAL_BENCH = "benchFormatSmallIntegral";

    private static String benchFormatSmallIntegral(NumberFormat nf) {
        String str = "";
        for (int j = -MAX_RANGE; j <= MAX_RANGE; j++) str = nf.format(((double) j) / 1000.0d);
        return str;
    }

    static double smallIntegralThroughputLoad() {
        double d = 0.0d;
        for (int j = -MAX_RANGE; j <= MAX_RANGE; j++) {
            d = (double) j / 1000.0d;
        }
        return d;
    }

    static void calculateSmallIntegralThroughputLoad() {
        int nbRuns = NB_RUNS;
        long elapsedTime = 0;
        double foo;
        for (int i = 1; i <= nbRuns; i++) {
            long startTime = System.nanoTime();
            foo = smallIntegralThroughputLoad();
            long estimatedTime = System.nanoTime() - startTime;
            if (i > 3)
                elapsedTime += estimatedTime / 1000;
        }
        if (Verbose)
            System.out.println("calculated throughput load for " + SMALL_INTEGRAL_BENCH + " bench is = " + (elapsedTime / (nbRuns - 3)) + " microseconds");
    }

    private static final String FAIR_SIMPLE_BENCH = "benchFormatFairSimple";

    private static String benchFormatFairSimple(NumberFormat nf, boolean isCurrency) {
        String str = "";
        double seed = isCurrency ? 0.0010203040506070809 : 0.00010203040506070809;
        double d = (double) -MAX_RANGE;
        for (int j = -MAX_RANGE; j <= MAX_RANGE; j++) {
            d = d + 1.0d + seed;
            str = nf.format(d);
        }
        return str;
    }

    static double fairSimpleThroughputLoad() {
        double seed = 0.00010203040506070809;
        double delta = 0.0d;
        double d = (double) -MAX_RANGE;
        for (int j = -MAX_RANGE; j <= MAX_RANGE; j++) {
            d = d + 1.0d + seed;
        }
        return d;
    }

    static void calculateFairSimpleThroughputLoad() {
        int nbRuns = NB_RUNS;
        long elapsedTime = 0;
        double foo;
        for (int i = 1; i <= nbRuns; i++) {
            long startTime = System.nanoTime();
            foo = fairSimpleThroughputLoad();
            long estimatedTime = System.nanoTime() - startTime;
            if (i > 3)
                elapsedTime += estimatedTime / 1000;
        }
        if (Verbose)
            System.out.println("calculated throughput load for " + FAIR_SIMPLE_BENCH + " bench is = " + (elapsedTime / (nbRuns - 3)) + " microseconds");
    }

    private static final String FRACTIONAL_ALL_NINES_BENCH = "benchFormatFractionalAllNines";

    private static String benchFormatFractionalAllNines(NumberFormat nf, boolean isCurrency) {
        String str = "";
        double fractionalEven = isCurrency ? 0.993000001 : 0.99930000001;
        double fractionalOdd = isCurrency ? 0.996000001 : 0.99960000001;
        double fractional;
        double d;
        for (int j = -MAX_RANGE; j <= MAX_RANGE; j++) {
            if ((j & 1) == 0)
                fractional = fractionalEven;
            else
                fractional = fractionalOdd;
            if (j >= 0)
                d = (double) j + fractional;
            else
                d = (double) j - fractional;
            str = nf.format(d);
        }
        return str;
    }

    static double fractionalAllNinesThroughputLoad() {
        double fractionalEven = 0.99930000001;
        double fractionalOdd = 0.99960000001;
        double fractional;
        double d = 0.0d;
        for (int j = -MAX_RANGE; j <= MAX_RANGE; j++) {
            if ((j & 1) == 0)
                fractional = fractionalEven;
            else
                fractional = fractionalOdd;
            if (j >= 0)
                d = (double) j + fractional;
            else
                d = (double) j - fractional;
        }
        return d;
    }

    static void calculateFractionalAllNinesThroughputLoad() {
        int nbRuns = NB_RUNS;
        long elapsedTime = 0;
        double foo;
        for (int i = 1; i <= nbRuns; i++) {
            long startTime = System.nanoTime();
            foo = fractionalAllNinesThroughputLoad();
            long estimatedTime = System.nanoTime() - startTime;
            if (i > 3)
                elapsedTime += estimatedTime / 1000;
        }
        if (Verbose)
            System.out.println("calculated throughput load for " + FRACTIONAL_ALL_NINES_BENCH + " bench is = " + (elapsedTime / (nbRuns - 3)) + " microseconds");
    }

    private static final String ALL_NINES_BENCH = "benchFormatAllNines";

    private static String benchFormatAllNines(NumberFormat nf, boolean isCurrency) {
        String str = "";
        double[] decimaAllNines = { 9.9993, 99.9993, 999.9993, 9999.9993, 99999.9993, 999999.9993, 9999999.9993, 99999999.9993, 999999999.9993 };
        double[] currencyAllNines = { 9.993, 99.993, 999.993, 9999.993, 99999.993, 999999.993, 9999999.993, 99999999.993, 999999999.993 };
        double[] valuesArray = (isCurrency) ? currencyAllNines : decimaAllNines;
        double seed = 1.0 / (double) MAX_RANGE;
        double d;
        int id;
        for (int j = -MAX_RANGE; j <= MAX_RANGE; j++) {
            id = (j >= 0) ? j % 9 : -j % 9;
            if ((j & 1) == 0)
                d = valuesArray[id] + id * seed;
            else
                d = valuesArray[id] - id * seed;
            str = nf.format(d);
        }
        return str;
    }

    static double allNinesThroughputLoad() {
        double[] decimaAllNines = { 9.9993, 99.9993, 999.9993, 9999.9993, 99999.9993, 999999.9993, 9999999.9993, 99999999.9993, 999999999.9993 };
        double[] valuesArray = decimaAllNines;
        double seed = 1.0 / (double) MAX_RANGE;
        double d = 0.0d;
        int id;
        for (int j = -MAX_RANGE; j <= MAX_RANGE; j++) {
            id = (j >= 0) ? j % 9 : -j % 9;
            if ((j & 1) == 0)
                d = valuesArray[id] + id * seed;
            else
                d = valuesArray[id] - id * seed;
        }
        return d;
    }

    static void calculateAllNinesThroughputLoad() {
        int nbRuns = NB_RUNS;
        long elapsedTime = 0;
        double foo;
        for (int i = 1; i <= nbRuns; i++) {
            long startTime = System.nanoTime();
            foo = allNinesThroughputLoad();
            long estimatedTime = System.nanoTime() - startTime;
            if (i > 3)
                elapsedTime += estimatedTime / 1000;
        }
        if (Verbose)
            System.out.println("calculated throughput load for " + ALL_NINES_BENCH + " bench is = " + (elapsedTime / (nbRuns - 3)) + " microseconds");
    }

    private static final String FAIR_BENCH = "benchFormatFair";

    private static String benchFormatFair(NumberFormat nf) {
        String str = "";
        double k = 1000.0d / (double) MAX_RANGE;
        k *= k;
        double d;
        double absj;
        double jPowerOf2;
        for (int j = -MAX_RANGE; j <= MAX_RANGE; j++) {
            absj = (double) j;
            jPowerOf2 = absj * absj;
            d = k * jPowerOf2;
            if (j < 0)
                d = -d;
            str = nf.format(d);
        }
        return str;
    }

    static double fairThroughputLoad() {
        double k = 1000.0d / (double) MAX_RANGE;
        k *= k;
        double d = 0.0d;
        double absj;
        double jPowerOf2;
        for (int j = -MAX_RANGE; j <= MAX_RANGE; j++) {
            absj = (double) j;
            jPowerOf2 = absj * absj;
            d = k * jPowerOf2;
            if (j < 0)
                d = -d;
        }
        return d;
    }

    static void calculateFairThroughputLoad() {
        int nbRuns = NB_RUNS;
        long elapsedTime = 0;
        double foo;
        for (int i = 1; i <= nbRuns; i++) {
            long startTime = System.nanoTime();
            foo = fairThroughputLoad();
            long estimatedTime = System.nanoTime() - startTime;
            if (i > 3)
                elapsedTime += estimatedTime / 1000;
        }
        if (Verbose)
            System.out.println("calculated throughput load for " + FAIR_BENCH + " bench is = " + (elapsedTime / (nbRuns - 3)) + " microseconds");
    }

    private static final String TIE_BENCH = "benchFormatTie";

    private static String benchFormatTie(NumberFormat nf, boolean isCurrency) {
        double d;
        String str = "";
        double fractionaScaling = (isCurrency) ? 1000.0d : 10000.0d;
        int fixedFractionalPart = (isCurrency) ? 125 : 1235;
        for (int j = -MAX_RANGE; j <= MAX_RANGE; j++) {
            d = (((double) j * fractionaScaling) + (double) fixedFractionalPart) / fractionaScaling;
            str = nf.format(d);
        }
        return str;
    }

    static double tieThroughputLoad(boolean isCurrency) {
        double d = 0.0d;
        double fractionaScaling = (isCurrency) ? 1000.0d : 10000.0d;
        int fixedFractionalPart = (isCurrency) ? 125 : 1235;
        for (int j = -MAX_RANGE; j <= MAX_RANGE; j++) {
            d = (((double) j * fractionaScaling) + (double) fixedFractionalPart) / fractionaScaling;
        }
        return d;
    }

    static void calculateTieThroughputLoad(boolean isCurrency) {
        int nbRuns = NB_RUNS;
        long elapsedTime = 0;
        double foo;
        for (int i = 1; i <= nbRuns; i++) {
            long startTime = System.nanoTime();
            foo = tieThroughputLoad(isCurrency);
            long estimatedTime = System.nanoTime() - startTime;
            if (i > 3)
                elapsedTime += estimatedTime / 1000;
        }
        if (Verbose)
            System.out.println("calculated throughput load for " + TIE_BENCH + " bench is = " + (elapsedTime / (nbRuns - 3)) + " microseconds");
    }

    static void printPerfResults(long[] times, String benchName) {
        int nbBenches = times.length;
        long totalTimeSpent = 0;
        long meanTimeSpent;
        double variance = 0;
        double standardDeviation = 0;
        for (int i = 1; i <= nbBenches; i++) totalTimeSpent += times[i - 1];
        meanTimeSpent = totalTimeSpent / nbBenches;
        for (int j = 1; j <= nbBenches; j++) variance += Math.pow(((double) times[j - 1] - (double) meanTimeSpent), 2);
        variance = variance / (double) times.length;
        standardDeviation = Math.sqrt(variance) / meanTimeSpent;
        System.out.println("Statistics (starting at 4th bench) for bench " + benchName + "\n for last " + nbBenches + " runs out of " + NB_RUNS + " , each with 2x" + MAX_RANGE + " format(double) calls : " + "\n  mean exec time = " + meanTimeSpent + " microseconds" + "\n  standard deviation = " + String.format("%.3f", standardDeviation) + "% \n");
    }

    public static void main(String[] args) {
        if (args.length >= 1) {
            if (args[0].equals("-help")) {
                usage();
                return;
            }
            for (String s : args) {
                if (s.equals("-doit"))
                    DoIt = true;
                else if (s.equals("-verbose"))
                    Verbose = true;
            }
        } else {
            System.out.println("Test skipped with success by default. See -help for details.");
            return;
        }
        if (!DoIt) {
            if (Verbose)
                usage();
            System.out.println("Test skipped and considered successful.");
            return;
        }
        System.out.println("Single Threaded micro benchmark evaluating " + "the throughput of java.text.DecimalFormat.format() call stack.\n");
        String fooString = "";
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        System.out.println("Running with a decimal instance of DecimalFormat.");
        calculateIntegerThroughputLoad();
        fooString = BenchType.INTEGER_BENCH.runBenchAndPrintStatistics(NB_RUNS, df, false);
        calculateFractionalThroughputLoad();
        fooString = BenchType.FRACTIONAL_BENCH.runBenchAndPrintStatistics(NB_RUNS, df, false);
        calculateSmallIntegralThroughputLoad();
        fooString = BenchType.SMALL_INTEGRAL_BENCH.runBenchAndPrintStatistics(NB_RUNS, df, false);
        calculateFractionalAllNinesThroughputLoad();
        fooString = BenchType.FRACTIONAL_ALL_NINES_BENCH.runBenchAndPrintStatistics(NB_RUNS, df, false);
        calculateAllNinesThroughputLoad();
        fooString = BenchType.ALL_NINES_BENCH.runBenchAndPrintStatistics(NB_RUNS, df, false);
        calculateFairSimpleThroughputLoad();
        fooString = BenchType.FAIR_SIMPLE_BENCH.runBenchAndPrintStatistics(NB_RUNS, df, false);
        calculateFairThroughputLoad();
        fooString = BenchType.FAIR_BENCH.runBenchAndPrintStatistics(NB_RUNS, df, false);
        calculateTieThroughputLoad(false);
        fooString = BenchType.TIE_BENCH.runBenchAndPrintStatistics(NB_RUNS, df, false);
        DecimalFormat cf = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        System.out.println("Running with a currency instance of DecimalFormat.");
        calculateIntegerThroughputLoad();
        fooString = BenchType.INTEGER_BENCH.runBenchAndPrintStatistics(NB_RUNS, cf, false);
        calculateFractionalThroughputLoad();
        fooString = BenchType.FRACTIONAL_BENCH.runBenchAndPrintStatistics(NB_RUNS, cf, false);
        calculateSmallIntegralThroughputLoad();
        fooString = BenchType.SMALL_INTEGRAL_BENCH.runBenchAndPrintStatistics(NB_RUNS, cf, false);
        calculateFractionalAllNinesThroughputLoad();
        fooString = BenchType.FRACTIONAL_ALL_NINES_BENCH.runBenchAndPrintStatistics(NB_RUNS, cf, false);
        calculateAllNinesThroughputLoad();
        fooString = BenchType.ALL_NINES_BENCH.runBenchAndPrintStatistics(NB_RUNS, cf, false);
        calculateFairSimpleThroughputLoad();
        fooString = BenchType.FAIR_SIMPLE_BENCH.runBenchAndPrintStatistics(NB_RUNS, cf, false);
        calculateFairThroughputLoad();
        fooString = BenchType.FAIR_BENCH.runBenchAndPrintStatistics(NB_RUNS, cf, false);
        calculateTieThroughputLoad(false);
        fooString = BenchType.TIE_BENCH.runBenchAndPrintStatistics(NB_RUNS, cf, false);
    }

    static enum BenchType {

        INTEGER_BENCH("benchFormatInteger"),
        FRACTIONAL_BENCH("benchFormatFractional"),
        SMALL_INTEGRAL_BENCH("benchFormatSmallIntegral"),
        FAIR_SIMPLE_BENCH("benchFormatFairSimple"),
        FRACTIONAL_ALL_NINES_BENCH("benchFormatFractionalAllNines"),
        ALL_NINES_BENCH("benchFormatAllNines"),
        FAIR_BENCH("benchFormatFair"),
        TIE_BENCH("benchFormatTie");

        private final String name;

        BenchType(String name) {
            this.name = name;
        }

        String runBenchAndPrintStatistics(int nbRuns, NumberFormat nf, boolean isCurrency) {
            long[] elapsedTimes = new long[nbRuns - 3];
            System.out.println("Now running " + nbRuns + " times bench " + name);
            String str = "";
            for (int i = 1; i <= nbRuns; i++) {
                stabilizeMemory(false);
                long startTime = System.nanoTime();
                switch(this) {
                    case INTEGER_BENCH:
                        str = benchFormatInteger(nf);
                        break;
                    case FRACTIONAL_BENCH:
                        str = benchFormatFractional(nf);
                        break;
                    case SMALL_INTEGRAL_BENCH:
                        str = benchFormatSmallIntegral(nf);
                        break;
                    case FRACTIONAL_ALL_NINES_BENCH:
                        str = benchFormatFractionalAllNines(nf, isCurrency);
                        break;
                    case ALL_NINES_BENCH:
                        str = benchFormatAllNines(nf, isCurrency);
                        break;
                    case FAIR_SIMPLE_BENCH:
                        str = benchFormatFairSimple(nf, isCurrency);
                        break;
                    case FAIR_BENCH:
                        str = benchFormatFair(nf);
                        break;
                    case TIE_BENCH:
                        str = benchFormatTie(nf, isCurrency);
                        break;
                    default:
                }
                long estimatedTime = System.nanoTime() - startTime;
                if (i > 3)
                    elapsedTimes[i - 4] = estimatedTime / 1000;
                if (Verbose)
                    System.out.println("calculated time for " + name + " bench " + i + " is = " + (estimatedTime / 1000) + " microseconds");
                else
                    System.out.print(".");
                stabilizeMemory(true);
            }
            System.out.println(name + " Done.");
            printPerfResults(elapsedTimes, name);
            return str;
        }
    }
}
