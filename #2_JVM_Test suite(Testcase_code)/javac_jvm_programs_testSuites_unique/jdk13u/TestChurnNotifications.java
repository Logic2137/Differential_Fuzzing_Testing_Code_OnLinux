import java.util.*;
import java.util.concurrent.atomic.*;
import javax.management.*;
import java.lang.management.*;
import javax.management.openmbean.*;
import com.sun.management.GarbageCollectionNotificationInfo;

public class TestChurnNotifications {

    static final long HEAP_MB = 128;

    static final long TARGET_MB = Long.getLong("target", 8_000);

    static final boolean PRECISE = Boolean.getBoolean("precise");

    static final long M = 1024 * 1024;

    static volatile Object sink;

    public static void main(String[] args) throws Exception {
        final AtomicLong churnBytes = new AtomicLong();
        NotificationListener listener = new NotificationListener() {

            @Override
            public void handleNotification(Notification n, Object o) {
                if (n.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) n.getUserData());
                    Map<String, MemoryUsage> mapBefore = info.getGcInfo().getMemoryUsageBeforeGc();
                    Map<String, MemoryUsage> mapAfter = info.getGcInfo().getMemoryUsageAfterGc();
                    MemoryUsage before = mapBefore.get("Shenandoah");
                    MemoryUsage after = mapAfter.get("Shenandoah");
                    if ((before != null) && (after != null)) {
                        long diff = before.getUsed() - after.getUsed();
                        if (diff > 0) {
                            churnBytes.addAndGet(diff);
                        }
                    }
                }
            }
        };
        for (GarbageCollectorMXBean bean : ManagementFactory.getGarbageCollectorMXBeans()) {
            ((NotificationEmitter) bean).addNotificationListener(listener, null, null);
        }
        final int size = 100_000;
        long count = TARGET_MB * 1024 * 1024 / (16 + 4 * size);
        long mem = count * (16 + 4 * size);
        for (int c = 0; c < count; c++) {
            sink = new int[size];
        }
        System.gc();
        Thread.sleep(1000);
        long actual = churnBytes.get();
        long minExpected = PRECISE ? (mem - HEAP_MB * 1024 * 1024) : 1;
        long maxExpected = mem + HEAP_MB * 1024 * 1024;
        String msg = "Expected = [" + minExpected / M + "; " + maxExpected / M + "] (" + mem / M + "), actual = " + actual / M;
        if (minExpected < actual && actual < maxExpected) {
            System.out.println(msg);
        } else {
            throw new IllegalStateException(msg);
        }
    }
}
