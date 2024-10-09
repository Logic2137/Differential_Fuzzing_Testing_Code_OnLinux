
package gc.arguments;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;

public final class HeapRegionUsageTool {

    private static MemoryUsage getUsage(String name) {
        for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
            if (pool.getName().matches(name)) {
                return pool.getUsage();
            }
        }
        return null;
    }

    public static MemoryUsage getEdenUsage() {
        return getUsage(".*Eden.*");
    }

    public static MemoryUsage getSurvivorUsage() {
        return getUsage(".*Survivor.*");
    }

    public static MemoryUsage getOldUsage() {
        return getUsage(".*(Old|Tenured).*");
    }

    public static MemoryUsage getHeapUsage() {
        return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
    }

    public static long alignUp(long value, long alignment) {
        return (value + alignment - 1) & ~(alignment - 1);
    }

    public static long alignDown(long value, long alignment) {
        return value & ~(alignment - 1);
    }
}
