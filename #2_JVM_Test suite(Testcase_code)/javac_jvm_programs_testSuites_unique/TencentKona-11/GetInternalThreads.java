import sun.management.*;
import java.util.*;
import java.lang.management.ThreadMXBean;
import java.lang.management.ManagementFactory;

public class GetInternalThreads {

    private final static HotspotThreadMBean mbean = ManagementFactoryHelper.getHotspotThreadMBean();

    private static final long MIN_VALUE_FOR_PASS = 4;

    private static final long MAX_VALUE_FOR_PASS = Long.MAX_VALUE;

    public static void main(String[] args) throws Exception {
        long value = mbean.getInternalThreadCount();
        if (value < MIN_VALUE_FOR_PASS || value > MAX_VALUE_FOR_PASS) {
            throw new RuntimeException("Internal thread count " + "illegal value: " + value + " " + "(MIN = " + MIN_VALUE_FOR_PASS + "; " + "MAX = " + MAX_VALUE_FOR_PASS + ")");
        }
        System.out.println("Internal Thread Count = " + value);
        ThreadMXBean thread = ManagementFactory.getThreadMXBean();
        if (!thread.isThreadCpuTimeSupported()) {
            System.out.println("Thread Cpu Time is not supported.");
            return;
        }
        while (!testCPUTime()) {
            Thread.sleep(100);
        }
    }

    private static boolean testCPUTime() {
        Map<String, Long> times = mbean.getInternalThreadCpuTimes();
        for (Map.Entry<String, Long> entry : times.entrySet()) {
            String threadName = entry.getKey();
            long cpuTime = entry.getValue();
            System.out.println("CPU time = " + cpuTime + " for " + threadName);
            if (cpuTime == -1) {
                System.out.println("Retry, proc structure might not be ready (-1)");
                return false;
            }
            if (cpuTime < 0) {
                throw new RuntimeException("Illegal CPU time: " + cpuTime);
            }
        }
        return true;
    }
}
