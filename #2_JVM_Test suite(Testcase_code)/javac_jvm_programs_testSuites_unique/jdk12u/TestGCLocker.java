




import java.lang.management.MemoryPoolMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

final class ThreadUtils {
    public static void sleep(long durationMS) {
        try {
            Thread.sleep(durationMS);
        } catch (Exception e) {
        }
    }
}

class Filler {
    private static final int SIZE = 250000;

    private int[] i1 = new int[SIZE];
    private int[] i2 = new int[SIZE];
    private short[] s1 = new short[SIZE];
    private short[] s2 = new short[SIZE];

    private Map<Object, Object> map = new HashMap<>();

    public Filler() {
        for (int i = 0; i < 10000; i++) {
            map.put(new Object(), new Object());
        }
    }
}

class Exitable {
    private volatile boolean shouldExit = false;

    protected boolean shouldExit() {
        return shouldExit;
    }

    public void exit() {
        shouldExit = true;
    }
}

class MemoryWatcher {
    private MemoryPoolMXBean bean;
    private final int thresholdPromille = 750;
    private final int criticalThresholdPromille = 800;
    private final int minGCWaitMS = 1000;
    private final int minFreeWaitElapsedMS = 30000;
    private final int minFreeCriticalWaitMS = 500;

    private int lastUsage = 0;
    private long lastGCDetected = System.currentTimeMillis();
    private long lastFree = System.currentTimeMillis();

    public MemoryWatcher(String mxBeanName) {
        List<MemoryPoolMXBean> memoryBeans = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean bean : memoryBeans) {
            if (bean.getName().equals(mxBeanName)) {
                this.bean = bean;
                break;
            }
        }
    }

    private int getMemoryUsage() {
        if (bean == null) {
            Runtime r = Runtime.getRuntime();
            float free = (float) r.freeMemory() / r.maxMemory();
            return Math.round((1 - free) * 1000);
        } else {
            MemoryUsage usage = bean.getUsage();
            float used = (float) usage.getUsed() / usage.getCommitted();
            return Math.round(used * 1000);
        }
    }

    public synchronized boolean shouldFreeUpSpace() {
        int usage = getMemoryUsage();
        long now = System.currentTimeMillis();

        boolean detectedGC = false;
        if (usage < lastUsage) {
            lastGCDetected = now;
            detectedGC = true;
        }

        lastUsage = usage;

        long elapsed = now - lastFree;
        long timeSinceLastGC = now - lastGCDetected;

        if (usage > criticalThresholdPromille && elapsed > minFreeCriticalWaitMS) {
            lastFree = now;
            return true;
        } else if (usage > thresholdPromille && !detectedGC) {
            if (elapsed > minFreeWaitElapsedMS || timeSinceLastGC > minGCWaitMS) {
                lastFree = now;
                return true;
            }
        }

        return false;
    }
}

class MemoryUser extends Exitable implements Runnable {
    private final Queue<Filler> cache = new ArrayDeque<Filler>();
    private final MemoryWatcher watcher;

    private void load() {
        if (watcher.shouldFreeUpSpace()) {
            int toRemove = cache.size() / 5;
            for (int i = 0; i < toRemove; i++) {
                cache.remove();
            }
        }
        cache.add(new Filler());
    }

    public MemoryUser(String mxBeanName) {
        watcher = new MemoryWatcher(mxBeanName);
    }

    @Override
    public void run() {
        for (int i = 0; i < 200; i++) {
            load();
        }

        while (!shouldExit()) {
            load();
        }
    }
}

class GCLockerStresser extends Exitable implements Runnable {
    static native void fillWithRandomValues(byte[] array);

    @Override
    public void run() {
        byte[] array = new byte[1024 * 1024];
        while (!shouldExit()) {
            fillWithRandomValues(array);
        }
    }
}

public class TestGCLocker {
    private static Exitable startGCLockerStresser(String name) {
        GCLockerStresser task = new GCLockerStresser();

        Thread thread = new Thread(task);
        thread.setName(name);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();

        return task;
    }

    private static Exitable startMemoryUser(String mxBeanName) {
        MemoryUser task = new MemoryUser(mxBeanName);

        Thread thread = new Thread(task);
        thread.setName("Memory User");
        thread.start();

        return task;
    }

    public static void main(String[] args) {
        System.loadLibrary("TestGCLocker");

        long durationMinutes = args.length > 0 ? Long.parseLong(args[0]) : 5;
        String mxBeanName = args.length > 1 ? args[1] : null;

        long startMS = System.currentTimeMillis();

        Exitable stresser1 = startGCLockerStresser("GCLockerStresser1");
        Exitable stresser2 = startGCLockerStresser("GCLockerStresser2");
        Exitable memoryUser = startMemoryUser(mxBeanName);

        long durationMS = durationMinutes * 60 * 1000;
        while ((System.currentTimeMillis() - startMS) < durationMS) {
            ThreadUtils.sleep(10 * 1010);
        }

        stresser1.exit();
        stresser2.exit();
        memoryUser.exit();
    }
}
