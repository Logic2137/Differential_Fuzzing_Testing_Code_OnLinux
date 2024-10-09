





import sun.management.*;

public class GetTotalSafepointTime {

    private static HotspotRuntimeMBean mbean =
        (HotspotRuntimeMBean)ManagementFactoryHelper.getHotspotRuntimeMBean();

    
    private static final long MIN_VALUE_FOR_PASS = 1;

    
    
    
    public static long executeThreadDumps(long initial_value) {
        long value;
        do {
            Thread.getAllStackTraces();
            value = mbean.getTotalSafepointTime();
        } while (value == initial_value);
        return value;
    }

    public static void main(String args[]) throws Exception {
        long value = executeThreadDumps(0);
        System.out.println("Total safepoint time (ms): " + value);

        if (value < MIN_VALUE_FOR_PASS) {
            throw new RuntimeException("Total safepoint time " +
                                       "illegal value: " + value + " ms " +
                                       "(MIN = " + MIN_VALUE_FOR_PASS + ")");
        }

        long value2 = executeThreadDumps(value);
        System.out.println("Total safepoint time (ms): " + value2);

        if (value2 <= value) {
            throw new RuntimeException("Total safepoint time " +
                                       "did not increase " +
                                       "(value = " + value + "; " +
                                       "value2 = " + value2 + ")");
        }

        System.out.println("Test passed.");
    }
}
