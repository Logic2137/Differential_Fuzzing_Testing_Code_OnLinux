













import java.util.*;
import java.util.concurrent.atomic.*;
import javax.management.*;
import java.lang.management.*;
import javax.management.openmbean.*;

import com.sun.management.GarbageCollectionNotificationInfo;

public class TestPauseNotifications {

    static final long HEAP_MB = 128;                           
    static final long TARGET_MB = Long.getLong("target", 2_000); 

    static volatile Object sink;

    public static void main(String[] args) throws Exception {
        final AtomicLong pausesDuration = new AtomicLong();
        final AtomicLong cyclesDuration = new AtomicLong();

        NotificationListener listener = new NotificationListener() {
            @Override
            public void handleNotification(Notification n, Object o) {
                if (n.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) n.getUserData());

                    System.out.println(info.getGcInfo().toString());
                    System.out.println(info.getGcName());
                    System.out.println();

                    long d = info.getGcInfo().getDuration();

                    String name = info.getGcName();
                    if (name.contains("Shenandoah")) {
                        if (name.equals("Shenandoah Pauses")) {
                            pausesDuration.addAndGet(d);
                        } else if (name.equals("Shenandoah Cycles")) {
                            cyclesDuration.addAndGet(d);
                        } else {
                            throw new IllegalStateException("Unknown name: " + name);
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

        for (int c = 0; c < count; c++) {
            sink = new int[size];
        }

        
        
        while (pausesDuration.get() == 0) {
            Thread.sleep(1000);
        }
        Thread.sleep(5000);

        long pausesActual = pausesDuration.get();
        long cyclesActual = cyclesDuration.get();

        long minExpected = 1;
        long maxExpected = Long.MAX_VALUE;

        {
            String msg = "Pauses expected = [" + minExpected + "; " + maxExpected + "], actual = " + pausesActual;
            if (minExpected <= pausesActual && pausesActual <= maxExpected) {
                System.out.println(msg);
            } else {
                throw new IllegalStateException(msg);
            }
        }

        {
            String msg = "Cycles expected = [" + minExpected + "; " + maxExpected + "], actual = " + cyclesActual;
            if (minExpected <= cyclesActual && cyclesActual <= maxExpected) {
                System.out.println(msg);
            } else {
                throw new IllegalStateException(msg);
            }
        }

        {
            String msg = "Cycle duration (" + cyclesActual + "), pause duration (" + pausesActual + ")";
            if (pausesActual <= cyclesActual) {
                System.out.println(msg);
            } else {
                throw new IllegalStateException(msg);
            }
        }
    }
}
