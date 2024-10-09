





import sun.management.*;

public class GetSafepointSyncTime {

    private static HotspotRuntimeMBean mbean =
        (HotspotRuntimeMBean)ManagementFactoryHelper.getHotspotRuntimeMBean();

    private static final long NUM_THREAD_DUMPS = 300;

    
    private static final long MIN_VALUE_FOR_PASS = 1;
    private static final long MAX_VALUE_FOR_PASS = Long.MAX_VALUE;

    public static void main(String args[]) throws Exception {
        long count = mbean.getSafepointCount();
        long value = mbean.getSafepointSyncTime();

        
        
        
        for (int i = 0; i < NUM_THREAD_DUMPS; i++) {
            Thread.getAllStackTraces();
        }

        long count1 = mbean.getSafepointCount();
        long value1 = mbean.getSafepointSyncTime();

        System.out.format("Safepoint count=%d (diff=%d), sync time=%d ms (diff=%d)%n",
                          count1, count1-count, value1, value1-value);

        if (value1 < MIN_VALUE_FOR_PASS || value1 > MAX_VALUE_FOR_PASS) {
            throw new RuntimeException("Safepoint sync time " +
                                       "illegal value: " + value1 + " ms " +
                                       "(MIN = " + MIN_VALUE_FOR_PASS + "; " +
                                       "MAX = " + MAX_VALUE_FOR_PASS + ")");
        }

        for (int i = 0; i < NUM_THREAD_DUMPS; i++) {
            Thread.getAllStackTraces();
        }

        long count2 = mbean.getSafepointCount();
        long value2 = mbean.getSafepointSyncTime();

        System.out.format("Safepoint count=%d (diff=%d), sync time=%d ms (diff=%d)%n",
                          count2, count2-count1, value2, value2-value1);

        if (value2 <= value1) {
            throw new RuntimeException("Safepoint sync time " +
                                       "did not increase " +
                                       "(value1 = " + value1 + "; " +
                                       "value2 = " + value2 + ")");
        }

        System.out.println("Test passed.");
    }
}
