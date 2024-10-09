import java.lang.management.*;

public class ThreadAllocatedMemory {

    private static com.sun.management.ThreadMXBean mbean = (com.sun.management.ThreadMXBean) ManagementFactory.getThreadMXBean();

    private static volatile boolean done = false;

    private static volatile boolean done1 = false;

    private static Object obj = new Object();

    private static final int NUM_THREADS = 10;

    private static Thread[] threads = new Thread[NUM_THREADS];

    private static long[] sizes = new long[NUM_THREADS];

    public static void main(String[] argv) throws Exception {
        testSupportEnableDisable();
        testGetCurrentThreadAllocatedBytes();
        testCurrentThreadGetThreadAllocatedBytes();
        testGetThreadAllocatedBytes();
        testGetThreadsAllocatedBytes();
        System.out.println("Test passed");
    }

    private static void testSupportEnableDisable() {
        if (!mbean.isThreadAllocatedMemorySupported()) {
            return;
        }
        if (mbean.isThreadAllocatedMemoryEnabled()) {
            mbean.setThreadAllocatedMemoryEnabled(false);
        }
        if (mbean.isThreadAllocatedMemoryEnabled()) {
            throw new RuntimeException("ThreadAllocatedMemory is expected to be disabled");
        }
        long s = mbean.getCurrentThreadAllocatedBytes();
        if (s != -1) {
            throw new RuntimeException("Invalid ThreadAllocatedBytes returned = " + s + " expected = -1");
        }
        if (!mbean.isThreadAllocatedMemoryEnabled()) {
            mbean.setThreadAllocatedMemoryEnabled(true);
        }
        if (!mbean.isThreadAllocatedMemoryEnabled()) {
            throw new RuntimeException("ThreadAllocatedMemory is expected to be enabled");
        }
    }

    private static void testGetCurrentThreadAllocatedBytes() {
        long size = mbean.getCurrentThreadAllocatedBytes();
        ensureValidSize(size);
        doit();
        checkResult(Thread.currentThread(), size, mbean.getCurrentThreadAllocatedBytes());
    }

    private static void testCurrentThreadGetThreadAllocatedBytes() {
        Thread curThread = Thread.currentThread();
        long id = curThread.getId();
        long size = mbean.getThreadAllocatedBytes(id);
        ensureValidSize(size);
        doit();
        checkResult(curThread, size, mbean.getThreadAllocatedBytes(id));
    }

    private static void testGetThreadAllocatedBytes() throws Exception {
        done = false;
        done1 = false;
        Thread curThread = new MyThread("MyThread");
        curThread.start();
        long id = curThread.getId();
        waitUntilThreadBlocked(curThread);
        long size = mbean.getThreadAllocatedBytes(id);
        ensureValidSize(size);
        synchronized (obj) {
            done = true;
            obj.notifyAll();
        }
        goSleep(400);
        checkResult(curThread, size, mbean.getThreadAllocatedBytes(id));
        synchronized (obj) {
            done1 = true;
            obj.notifyAll();
        }
        try {
            curThread.join();
        } catch (InterruptedException e) {
            System.out.println("Unexpected exception is thrown.");
            e.printStackTrace(System.out);
        }
    }

    private static void testGetThreadsAllocatedBytes() throws Exception {
        done = false;
        done1 = false;
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new MyThread("MyThread-" + i);
            threads[i].start();
        }
        waitUntilThreadsBlocked();
        for (int i = 0; i < NUM_THREADS; i++) {
            sizes[i] = mbean.getThreadAllocatedBytes(threads[i].getId());
            ensureValidSize(sizes[i]);
        }
        synchronized (obj) {
            done = true;
            obj.notifyAll();
        }
        goSleep(400);
        for (int i = 0; i < NUM_THREADS; i++) {
            checkResult(threads[i], sizes[i], mbean.getThreadAllocatedBytes(threads[i].getId()));
        }
        synchronized (obj) {
            done1 = true;
            obj.notifyAll();
        }
        for (int i = 0; i < NUM_THREADS; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                System.out.println("Unexpected exception is thrown.");
                e.printStackTrace(System.out);
                break;
            }
        }
    }

    private static void ensureValidSize(long size) {
        if (size < 0) {
            throw new RuntimeException("Invalid allocated bytes returned = " + size);
        }
    }

    private static void checkResult(Thread curThread, long prev_size, long curr_size) {
        if (curr_size < prev_size) {
            throw new RuntimeException("Allocated bytes " + curr_size + " expected >= " + prev_size);
        }
        System.out.println(curThread.getName() + " Previous allocated bytes = " + prev_size + " Current allocated bytes = " + curr_size);
    }

    private static void goSleep(long ms) throws Exception {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.out.println("Unexpected exception is thrown.");
            throw e;
        }
    }

    private static void waitUntilThreadBlocked(Thread thread) throws Exception {
        while (true) {
            goSleep(100);
            ThreadInfo info = mbean.getThreadInfo(thread.getId());
            if (info.getThreadState() == Thread.State.WAITING) {
                break;
            }
        }
    }

    private static void waitUntilThreadsBlocked() throws Exception {
        int count = 0;
        while (count != NUM_THREADS) {
            goSleep(100);
            count = 0;
            for (int i = 0; i < NUM_THREADS; i++) {
                ThreadInfo info = mbean.getThreadInfo(threads[i].getId());
                if (info.getThreadState() == Thread.State.WAITING) {
                    count++;
                }
            }
        }
    }

    public static void doit() {
        String tmp = "";
        long hashCode = 0;
        for (int counter = 0; counter < 1000; counter++) {
            tmp += counter;
            hashCode = tmp.hashCode();
        }
        System.out.println(Thread.currentThread().getName() + " hashcode: " + hashCode);
    }

    static class MyThread extends Thread {

        public MyThread(String name) {
            super(name);
        }

        public void run() {
            ThreadAllocatedMemory.doit();
            synchronized (obj) {
                while (!done) {
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        System.out.println("Unexpected exception is thrown.");
                        e.printStackTrace(System.out);
                        break;
                    }
                }
            }
            long size1 = mbean.getThreadAllocatedBytes(getId());
            ThreadAllocatedMemory.doit();
            long size2 = mbean.getThreadAllocatedBytes(getId());
            System.out.println(getName() + ": " + "ThreadAllocatedBytes  = " + size1 + " ThreadAllocatedBytes  = " + size2);
            if (size1 > size2) {
                throw new RuntimeException(getName() + " ThreadAllocatedBytes = " + size1 + " > ThreadAllocatedBytes = " + size2);
            }
            synchronized (obj) {
                while (!done1) {
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        System.out.println("Unexpected exception is thrown.");
                        e.printStackTrace(System.out);
                        break;
                    }
                }
            }
        }
    }
}
