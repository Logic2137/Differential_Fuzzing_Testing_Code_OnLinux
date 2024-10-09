





import com.sun.management.OperatingSystemMXBean;
import java.lang.management.*;

public class GetCommittedVirtualMemorySize {

    private static OperatingSystemMXBean mbean =
        (com.sun.management.OperatingSystemMXBean)
        ManagementFactory.getOperatingSystemMXBean();

    
    private static final long MIN_SIZE_FOR_PASS = 1;
    private static long       MAX_SIZE_FOR_PASS = Long.MAX_VALUE;

    private static boolean trace = false;

    public static void main(String args[]) throws Exception {
        if (args.length > 0 && args[0].equals("trace")) {
            trace = true;
        }

        long size = mbean.getCommittedVirtualMemorySize();
        if (size == -1) {
            System.out.println("getCommittedVirtualMemorySize() is not supported");
            return;
        }

        if (trace) {
            System.out.println("Committed virtual memory size in bytes: " +
                               size);
        }

        if (size < MIN_SIZE_FOR_PASS || size > MAX_SIZE_FOR_PASS) {
            throw new RuntimeException("Committed virtual memory size " +
                                       "illegal value: " + size + " bytes " +
                                       "(MIN = " + MIN_SIZE_FOR_PASS + "; " +
                                       "MAX = " + MAX_SIZE_FOR_PASS + ")");
        }

        System.out.println("Test passed.");
    }
}
