
package utils;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;

public class Pools {

    private static final String EDEN_SPACE_POOL = "Eden Space";

    private static final String OLD_GEN_POOL = "Old Gen";

    private static final String METASPACE_POOL = "Metaspace";

    private static final String SURVIVOR_SPACE = "Survivor Space";

    public static long getNGMaxSize() {
        return getPoolMaxSize(EDEN_SPACE_POOL) + 2 * getPoolMaxSize(SURVIVOR_SPACE);
    }

    public static long getHeapCommittedSize() {
        return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getCommitted() / 1024;
    }

    public static long getEdenCommittedSize() {
        return getPoolCommittedSize(EDEN_SPACE_POOL);
    }

    public static long getOldGenCommittedSize() {
        return getPoolCommittedSize(OLD_GEN_POOL);
    }

    public static long getMetaspaceCommittedSize() {
        return getPoolCommittedSize(METASPACE_POOL);
    }

    private static long getPoolMaxSize(String poolName) {
        long result;
        MemoryPoolMXBean pool = findPool(poolName);
        if (pool != null) {
            if (pool.getUsage().getMax() == -1) {
                result = -1;
            } else {
                result = pool.getUsage().getCommitted() / 1024;
            }
        } else {
            throw new RuntimeException("Pool '" + poolName + "' wasn't found");
        }
        log("Max size of the pool '" + poolName + "' is " + result);
        return result;
    }

    private static long getPoolCommittedSize(String poolName) {
        long result;
        MemoryPoolMXBean pool = findPool(poolName);
        if (pool != null) {
            if (pool.getUsage().getCommitted() == -1) {
                result = -1;
            } else {
                result = pool.getUsage().getCommitted() / 1024;
            }
        } else {
            throw new RuntimeException("Pool '" + poolName + "' wasn't found");
        }
        log("Committed size of the pool '" + poolName + "' is " + result);
        return result;
    }

    private static MemoryPoolMXBean findPool(String poolName) {
        for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
            if (pool.getName().contains(poolName)) {
                return pool;
            }
        }
        return null;
    }

    private static void log(String s) {
        System.out.println(s);
    }
}
