import java.lang.management.*;
import javax.management.*;
import static java.lang.management.ManagementFactory.*;
import java.util.List;
import java.util.Map;
import com.sun.management.GcInfo;

public class ProxyTypeMapping {

    private static MBeanServer server = ManagementFactory.getPlatformMBeanServer();

    private static RuntimeMXBean runtime;

    private static ThreadMXBean thread;

    private static MemoryMXBean memory;

    private static MemoryPoolMXBean heapPool = null;

    private static MemoryPoolMXBean nonHeapPool = null;

    public static void main(String[] argv) throws Exception {
        runtime = newPlatformMXBeanProxy(server, RUNTIME_MXBEAN_NAME, RuntimeMXBean.class);
        thread = newPlatformMXBeanProxy(server, THREAD_MXBEAN_NAME, ThreadMXBean.class);
        memory = newPlatformMXBeanProxy(server, MEMORY_MXBEAN_NAME, MemoryMXBean.class);
        MyListener listener = new MyListener();
        NotificationEmitter emitter = (NotificationEmitter) memory;
        emitter.addNotificationListener(listener, null, null);
        emitter.removeNotificationListener(listener);
        List<MemoryPoolMXBean> pools = getMemoryPoolMXBeans();
        for (MemoryPoolMXBean p : pools) {
            if (heapPool == null && p.getType() == MemoryType.HEAP && p.isUsageThresholdSupported() && p.isCollectionUsageThresholdSupported()) {
                heapPool = p;
            }
            if (nonHeapPool == null && p.getType() == MemoryType.NON_HEAP && p.isUsageThresholdSupported()) {
                nonHeapPool = p;
            }
        }
        checkEnum();
        checkList();
        checkMap();
        checkMemoryUsage();
        checkThreadInfo();
        checkOS();
        checkSunGC();
        System.out.println("Test passed.");
    }

    private static void checkEnum() throws Exception {
        MemoryType type = heapPool.getType();
        if (type != MemoryType.HEAP) {
            throw new RuntimeException("TEST FAILED: " + " incorrect memory type for " + heapPool.getName());
        }
        type = nonHeapPool.getType();
        if (type != MemoryType.NON_HEAP) {
            throw new RuntimeException("TEST FAILED: " + " incorrect memory type for " + nonHeapPool.getName());
        }
    }

    private static final String OPTION = "-verbose:gc";

    private static void checkList() throws Exception {
        List<String> args = runtime.getInputArguments();
        if (args.size() < 1) {
            throw new RuntimeException("TEST FAILED: " + " empty input arguments");
        }
        boolean found = false;
        for (String option : args) {
            if (option.equals(OPTION)) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new RuntimeException("TEST FAILED: " + "VM option " + OPTION + " not found");
        }
    }

    private static final String KEY1 = "test.property.key1";

    private static final String VALUE1 = "test.property.value1";

    private static final String KEY2 = "test.property.key2";

    private static final String VALUE2 = "test.property.value2";

    private static final String KEY3 = "test.property.key3";

    private static void checkMap() throws Exception {
        System.setProperty(KEY1, VALUE1);
        System.setProperty(KEY2, VALUE2);
        Map<String, String> props1 = runtime.getSystemProperties();
        String value1 = props1.get(KEY1);
        if (value1 == null || !value1.equals(VALUE1)) {
            throw new RuntimeException("TEST FAILED: " + KEY1 + " property found" + " with value = " + value1 + " but expected to be " + VALUE1);
        }
        String value2 = props1.get(KEY2);
        if (value2 == null || !value2.equals(VALUE2)) {
            throw new RuntimeException("TEST FAILED: " + KEY2 + " property found" + " with value = " + value2 + " but expected to be " + VALUE2);
        }
        String value3 = props1.get(KEY3);
        if (value3 != null) {
            throw new RuntimeException("TEST FAILED: " + KEY3 + " property found" + " but should not exist");
        }
    }

    private static void checkMemoryUsage() throws Exception {
        MemoryUsage u1 = memory.getHeapMemoryUsage();
        MemoryUsage u2 = memory.getNonHeapMemoryUsage();
        MemoryUsage u3 = heapPool.getUsage();
        MemoryUsage u4 = nonHeapPool.getUsage();
        if (u1.getCommitted() < 0 || u2.getCommitted() < 0 || u3.getCommitted() < 0 || u4.getCommitted() < 0) {
            throw new RuntimeException("TEST FAILED: " + " expected non-negative committed usage");
        }
        memory.gc();
        MemoryUsage u5 = heapPool.getCollectionUsage();
        if (u5.getCommitted() < 0) {
            throw new RuntimeException("TEST FAILED: " + " expected non-negative committed collected usage");
        }
    }

    private static void checkThreadInfo() throws Exception {
        long[] ids = thread.getAllThreadIds();
        ThreadInfo[] infos = thread.getThreadInfo(ids);
        for (ThreadInfo ti : infos) {
            printThreadInfo(ti);
        }
        infos = thread.getThreadInfo(ids, 2);
        for (ThreadInfo ti : infos) {
            printThreadInfo(ti);
        }
        long id = Thread.currentThread().getId();
        ThreadInfo info = thread.getThreadInfo(id);
        printThreadInfo(info);
        info = thread.getThreadInfo(id, 2);
        printThreadInfo(info);
    }

    private static void printThreadInfo(ThreadInfo info) {
        if (info == null) {
            throw new RuntimeException("TEST FAILED: " + " Null ThreadInfo");
        }
        System.out.print(info.getThreadName());
        System.out.print(" id=" + info.getThreadId());
        System.out.println(" " + info.getThreadState());
        for (StackTraceElement s : info.getStackTrace()) {
            System.out.println(s);
        }
    }

    private static void checkOS() throws Exception {
        com.sun.management.OperatingSystemMXBean os = newPlatformMXBeanProxy(server, OPERATING_SYSTEM_MXBEAN_NAME, com.sun.management.OperatingSystemMXBean.class);
        System.out.println("# CPUs = " + os.getAvailableProcessors());
        System.out.println("Committed virtual memory = " + os.getCommittedVirtualMemorySize());
    }

    private static void checkSunGC() throws Exception {
        List<GarbageCollectorMXBean> gcs = getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gc : gcs) {
            com.sun.management.GarbageCollectorMXBean sunGc = newPlatformMXBeanProxy(server, GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE + ",name=" + gc.getName(), com.sun.management.GarbageCollectorMXBean.class);
            GcInfo info = sunGc.getLastGcInfo();
            if (info != null) {
                System.out.println("GC statistic for : " + gc.getName());
                printGcInfo(info);
            }
        }
    }

    private static void printGcInfo(GcInfo info) throws Exception {
        System.out.print("GC #" + info.getId());
        System.out.print(" start:" + info.getStartTime());
        System.out.print(" end:" + info.getEndTime());
        System.out.println(" (" + info.getDuration() + "ms)");
        Map<String, MemoryUsage> usage = info.getMemoryUsageBeforeGc();
        for (Map.Entry<String, MemoryUsage> entry : usage.entrySet()) {
            String poolname = entry.getKey();
            MemoryUsage busage = entry.getValue();
            MemoryUsage ausage = info.getMemoryUsageAfterGc().get(poolname);
            if (ausage == null) {
                throw new RuntimeException("After Gc Memory does not exist" + " for " + poolname);
            }
            System.out.println("Usage for pool " + poolname);
            System.out.println("   Before GC: " + busage);
            System.out.println("   After GC: " + ausage);
        }
    }

    static class MyListener implements NotificationListener {

        public void handleNotification(Notification notif, Object handback) {
            return;
        }
    }
}
