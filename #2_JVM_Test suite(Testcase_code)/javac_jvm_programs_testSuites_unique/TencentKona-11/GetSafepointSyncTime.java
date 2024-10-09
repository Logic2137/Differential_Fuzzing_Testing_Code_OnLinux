





import sun.management.*;

public class GetSafepointSyncTime {

    private static HotspotRuntimeMBean mbean =
        (HotspotRuntimeMBean)ManagementFactoryHelper.getHotspotRuntimeMBean();

    private static final long NUM_THREAD_DUMPS = 300;

    static void checkPositive(long value, String label) {
        if (value < 0)
            throw new RuntimeException(label + " had a negative value of "
                                       + value);
    }

    static void validate(long count1, long count2, long time1, long time2,
                         String label) {
        checkPositive(count1, label + ":count1");
        checkPositive(count2, label + ":count2");
        checkPositive(time1, label + ":time1");
        checkPositive(time2, label + ":time2");

        long countDiff = count2 - count1;
        long timeDiff = time2 - time1;

        if (countDiff < NUM_THREAD_DUMPS) {
            throw new RuntimeException(label +
                                       ": Expected at least " + NUM_THREAD_DUMPS +
                                       " safepoints but only got " + countDiff);
        }

        
        
        
        if (timeDiff < 0) {
            throw new RuntimeException(label + ": Safepoint sync time " +
                                       "decreased unexpectedly " +
                                       "(time1 = " + time1 + "; " +
                                       "time2 = " + time2 + ")");
        }

        System.out.format("%s: Safepoint count=%d (diff=%d), sync time=%d ms (diff=%d)%n",
                          label, count2, countDiff, time2, timeDiff);

    }

    public static void main(String args[]) throws Exception {
        long count = mbean.getSafepointCount();
        long time = mbean.getSafepointSyncTime();

        checkPositive(count, "count");
        checkPositive(time, "time");

        

        for (int i = 0; i < NUM_THREAD_DUMPS; i++) {
            Thread.getAllStackTraces();
        }

        long count1 = mbean.getSafepointCount();
        long time1 = mbean.getSafepointSyncTime();

        validate(count, count1, time, time1, "Pass 1");

        

        for (int i = 0; i < NUM_THREAD_DUMPS; i++) {
            Thread.getAllStackTraces();
        }

        long count2 = mbean.getSafepointCount();
        long time2 = mbean.getSafepointSyncTime();

        validate(count1, count2, time1, time2, "Pass 2");

        System.out.println("Test passed.");
    }
}
