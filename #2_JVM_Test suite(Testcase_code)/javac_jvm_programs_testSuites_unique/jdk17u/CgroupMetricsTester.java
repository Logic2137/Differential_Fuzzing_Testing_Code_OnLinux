
package jdk.test.lib.containers.cgroup;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

interface CgroupMetricsTester {

    public static final double ERROR_MARGIN = 0.25;

    public static final String EMPTY_STR = "";

    public void testMemorySubsystem();

    public void testCpuAccounting();

    public void testCpuSchedulingMetrics();

    public void testCpuSets();

    public void testCpuConsumption() throws IOException, InterruptedException;

    public void testMemoryUsage() throws Exception;

    public void testMisc();

    public static long convertStringToLong(String strval, long initialVal, long overflowRetval) {
        long retval = initialVal;
        if (strval == null)
            return retval;
        try {
            retval = Long.parseLong(strval);
        } catch (NumberFormatException e) {
            BigInteger b = new BigInteger(strval);
            if (b.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
                return overflowRetval;
            }
        }
        return retval;
    }

    public static boolean compareWithErrorMargin(long oldVal, long newVal) {
        return Math.abs(oldVal - newVal) <= Math.abs(oldVal * ERROR_MARGIN);
    }

    public static boolean compareWithErrorMargin(double oldVal, double newVal) {
        return Math.abs(oldVal - newVal) <= Math.abs(oldVal * ERROR_MARGIN);
    }

    public static void fail(String controller, String metric, long oldVal, long testVal) {
        throw new RuntimeException("Test failed for - " + controller + ":" + metric + ", expected [" + oldVal + "], got [" + testVal + "]");
    }

    public static void fail(String controller, String metric, String oldVal, String testVal) {
        throw new RuntimeException("Test failed for - " + controller + ":" + metric + ", expected [" + oldVal + "], got [" + testVal + "]");
    }

    public static void fail(String controller, String metric, double oldVal, double testVal) {
        throw new RuntimeException("Test failed for - " + controller + ":" + metric + ", expected [" + oldVal + "], got [" + testVal + "]");
    }

    public static void fail(String controller, String metric, boolean oldVal, boolean testVal) {
        throw new RuntimeException("Test failed for - " + controller + ":" + metric + ", expected [" + oldVal + "], got [" + testVal + "]");
    }

    public static void warn(String controller, String metric, long oldVal, long testVal) {
        System.err.println("Warning - " + controller + ":" + metric + ", expected [" + oldVal + "], got [" + testVal + "]");
    }

    public static Integer[] convertCpuSetsToArray(String cpusstr) {
        if (cpusstr == null || EMPTY_STR.equals(cpusstr)) {
            return null;
        }
        Integer[] cpuSets = Stream.of(cpusstr.split(",")).flatMap(a -> {
            if (a.contains("-")) {
                String[] range = a.split("-");
                return IntStream.rangeClosed(Integer.parseInt(range[0]), Integer.parseInt(range[1])).boxed();
            } else {
                return Stream.of(Integer.parseInt(a));
            }
        }).toArray(Integer[]::new);
        return cpuSets;
    }

    public static Integer[] boxedArrayOrNull(int[] primitiveArray) {
        if (primitiveArray == null) {
            return null;
        }
        return Arrays.stream(primitiveArray).boxed().toArray(Integer[]::new);
    }

    public static Integer[] sortAllowNull(Integer[] array) {
        if (array == null) {
            return null;
        }
        Arrays.sort(array);
        return array;
    }
}
