



import java.lang.management.*;
import java.util.*;

public class TestMemoryMXBeans {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalStateException("Should provide expected heap sizes");
        }

        long initSize = 1L * Integer.parseInt(args[0]) * 1024 * 1024;
        long maxSize  = 1L * Integer.parseInt(args[1]) * 1024 * 1024;

        
        Thread.sleep(1000);

        testMemoryBean(initSize, maxSize);
    }

    public static void testMemoryBean(long initSize, long maxSize) {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        long heapInit = memoryMXBean.getHeapMemoryUsage().getInit();
        long heapCommitted = memoryMXBean.getHeapMemoryUsage().getCommitted();
        long heapMax = memoryMXBean.getHeapMemoryUsage().getMax();
        long nonHeapInit = memoryMXBean.getNonHeapMemoryUsage().getInit();
        long nonHeapCommitted = memoryMXBean.getNonHeapMemoryUsage().getCommitted();
        long nonHeapMax = memoryMXBean.getNonHeapMemoryUsage().getMax();

        if (initSize > 0 && heapInit != initSize) {
            throw new IllegalStateException("Init heap size is wrong: " + heapInit + " vs " + initSize);
        }
        if (maxSize > 0 && heapMax != maxSize) {
            throw new IllegalStateException("Max heap size is wrong: " + heapMax + " vs " + maxSize);
        }
        if (initSize > 0 && maxSize > 0 && initSize != maxSize && heapCommitted == heapMax) {
            throw new IllegalStateException("Committed heap size is max: " + heapCommitted +
                                            " (init: " + initSize + ", max: " + maxSize + ")");
        }
        if (initSize > 0 && maxSize > 0 && initSize == maxSize && heapCommitted != heapMax) {
            throw new IllegalStateException("Committed heap size is not max: " + heapCommitted +
                                            " (init: " + initSize + ", max: " + maxSize + ")");
        }
        if (initSize > 0 && heapCommitted < initSize) {
            throw new IllegalStateException("Committed heap size is below min: " + heapCommitted +
                                            " (init: " + initSize + ", max: " + maxSize + ")");
        }
    }
}
