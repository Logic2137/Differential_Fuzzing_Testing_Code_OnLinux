import java.lang.management.*;
import java.util.concurrent.Phaser;

public class ThreadBlockedCount {

    static final long EXPECTED_BLOCKED_COUNT = 3;

    static final int DEPTH = 10;

    private static final ThreadMXBean mbean = ManagementFactory.getThreadMXBean();

    private static final Object a = new Object();

    private static final Object b = new Object();

    private static final Object c = new Object();

    private static final Object blockedObj1 = new Object();

    private static final Object blockedObj2 = new Object();

    private static final Object blockedObj3 = new Object();

    private static volatile boolean testOk = true;

    private static BlockingThread blocking;

    private static BlockedThread blocked;

    public static void main(String[] args) throws Exception {
        runTest();
        if (!testOk) {
            throw new RuntimeException("TEST FAILED.");
        }
        System.out.println("Test passed.");
    }

    private static void runTest() throws Exception {
        final Phaser p = new Phaser(2);
        blocking = new BlockingThread(p);
        blocking.start();
        blocked = new BlockedThread(p);
        blocked.start();
        try {
            blocking.join();
            testOk = checkBlocked();
            p.arriveAndAwaitAdvance();
        } catch (InterruptedException e) {
            System.err.println("Unexpected exception.");
            e.printStackTrace(System.err);
            throw e;
        }
    }

    static class BlockedThread extends Thread {

        private final Phaser p;

        BlockedThread(Phaser p) {
            super("BlockedThread");
            this.p = p;
        }

        public void run() {
            int accumulator = 0;
            p.arriveAndAwaitAdvance();
            synchronized (a) {
                p.arriveAndAwaitAdvance();
                synchronized (blockedObj1) {
                    accumulator++;
                }
            }
            synchronized (b) {
                p.arriveAndAwaitAdvance();
                synchronized (blockedObj2) {
                    accumulator++;
                }
            }
            synchronized (c) {
                p.arriveAndAwaitAdvance();
                synchronized (blockedObj3) {
                    accumulator++;
                }
            }
            System.out.println("Acquired " + accumulator + " monitors");
            p.arriveAndAwaitAdvance();
        }
    }

    static class BlockingThread extends Thread {

        private final Phaser p;

        BlockingThread(Phaser p) {
            super("BlockingThread");
            this.p = p;
        }

        private void waitForBlocked() {
            p.arriveAndAwaitAdvance();
            boolean threadBlocked = false;
            while (!threadBlocked) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    System.err.println("Unexpected exception.");
                    e.printStackTrace(System.err);
                    testOk = false;
                    break;
                }
                ThreadInfo info = mbean.getThreadInfo(blocked.getId());
                threadBlocked = (info.getThreadState() == Thread.State.BLOCKED);
            }
        }

        public void run() {
            p.arriveAndAwaitAdvance();
            synchronized (blockedObj1) {
                System.out.println("BlockingThread attempts to notify a");
                waitForBlocked();
            }
            synchronized (blockedObj2) {
                System.out.println("BlockingThread attempts to notify b");
                waitForBlocked();
            }
            synchronized (blockedObj3) {
                System.out.println("BlockingThread attempts to notify c");
                waitForBlocked();
            }
        }
    }

    private static long getBlockedCount() {
        long count;
        ThreadInfo ti = mbean.getThreadInfo(blocked.getId());
        count = ti.getBlockedCount();
        return count;
    }

    private static boolean checkBlocked() {
        long count = -1;
        for (int i = 0; i < 100; i++) {
            count = getBlockedCount();
            if (count >= EXPECTED_BLOCKED_COUNT) {
                return true;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.err.println("Unexpected exception.");
                e.printStackTrace(System.err);
                return false;
            }
        }
        System.err.println("TEST FAILED: Blocked thread has " + count + " blocked counts. Expected at least " + EXPECTED_BLOCKED_COUNT);
        return false;
    }
}
