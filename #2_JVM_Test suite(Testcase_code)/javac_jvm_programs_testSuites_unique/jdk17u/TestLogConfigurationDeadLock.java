import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.security.Permission;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class TestLogConfigurationDeadLock {

    static volatile Exception thrown = null;

    static volatile boolean goOn = true;

    static final int READERS = 2;

    static final int LOGGERS = 2;

    static final long TIME = 4 * 1000;

    static final long STEP = 1 * 1000;

    static final int LCOUNT = 50;

    static final AtomicLong nextLogger = new AtomicLong(0);

    static final AtomicLong readCount = new AtomicLong(0);

    static final AtomicLong checkCount = new AtomicLong(0);

    public static void main(String[] args) throws Exception {
        System.out.println("No security");
        test();
        System.out.println("\nWith security");
        Policy.setPolicy(new Policy() {

            @Override
            public boolean implies(ProtectionDomain domain, Permission permission) {
                if (super.implies(domain, permission))
                    return true;
                return true;
            }
        });
        System.setSecurityManager(new SecurityManager());
        test();
    }

    public static void test() throws Exception {
        goOn = true;
        thrown = null;
        long sNextLogger = nextLogger.get();
        long sReadCount = readCount.get();
        long sCheckCount = checkCount.get();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < READERS; i++) {
            threads.add(new ReadConf());
        }
        for (int i = 0; i < LOGGERS; i++) {
            threads.add(new AddLogger());
        }
        threads.add(new DeadlockDetector());
        threads.add(0, new Stopper(TIME));
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            try {
                t.join();
            } catch (Exception x) {
                fail(x);
            }
        }
        if (thrown != null) {
            throw thrown;
        }
        System.out.println("Passed: " + (nextLogger.get() - sNextLogger) + " loggers created by " + LOGGERS + " Thread(s),");
        System.out.println("\t LogManager.readConfiguration() called " + (readCount.get() - sReadCount) + " times by " + READERS + " Thread(s).");
        System.out.println("\t ThreadMXBean.findDeadlockedThreads called " + (checkCount.get() - sCheckCount) + " times by 1 Thread.");
    }

    static final class ReadConf extends Thread {

        @Override
        public void run() {
            while (goOn) {
                try {
                    LogManager.getLogManager().readConfiguration();
                    readCount.incrementAndGet();
                    Thread.sleep(1);
                } catch (Exception x) {
                    fail(x);
                }
            }
        }
    }

    static final class AddLogger extends Thread {

        @Override
        public void run() {
            try {
                while (goOn) {
                    Logger l;
                    Logger foo = Logger.getLogger("foo");
                    Logger bar = Logger.getLogger("foo.bar");
                    for (int i = 0; i < LCOUNT; i++) {
                        l = Logger.getLogger("foo.bar.l" + nextLogger.incrementAndGet());
                        l.fine("I'm fine");
                        if (!goOn)
                            break;
                        Thread.sleep(1);
                    }
                }
            } catch (InterruptedException | RuntimeException x) {
                fail(x);
            }
        }
    }

    static final class DeadlockDetector extends Thread {

        @Override
        public void run() {
            while (goOn) {
                try {
                    long[] ids = ManagementFactory.getThreadMXBean().findDeadlockedThreads();
                    checkCount.incrementAndGet();
                    ids = ids == null ? new long[0] : ids;
                    if (ids.length == 1) {
                        throw new RuntimeException("Found 1 deadlocked thread: " + ids[0]);
                    } else if (ids.length > 0) {
                        ThreadInfo[] infos = ManagementFactory.getThreadMXBean().getThreadInfo(ids, Integer.MAX_VALUE);
                        System.err.println("Found " + ids.length + " deadlocked threads: ");
                        for (ThreadInfo inf : infos) {
                            System.err.println(inf.toString());
                        }
                        throw new RuntimeException("Found " + ids.length + " deadlocked threads");
                    }
                    Thread.sleep(100);
                } catch (InterruptedException | RuntimeException x) {
                    fail(x);
                }
            }
        }
    }

    static final class Stopper extends Thread {

        long start;

        long time;

        Stopper(long time) {
            start = System.currentTimeMillis();
            this.time = time;
        }

        @Override
        public void run() {
            try {
                long rest, previous;
                previous = time;
                while (goOn && (rest = start - System.currentTimeMillis() + time) > 0) {
                    if (previous == time || previous - rest >= STEP) {
                        Logger.getLogger("remaining").info(String.valueOf(rest) + "ms remaining...");
                        previous = rest == time ? rest - 1 : rest;
                        System.gc();
                    }
                    if (goOn == false)
                        break;
                    Thread.sleep(Math.min(rest, 100));
                }
                System.out.println(System.currentTimeMillis() - start + " ms elapsed (" + time + " requested)");
                goOn = false;
            } catch (InterruptedException | RuntimeException x) {
                fail(x);
            }
        }
    }

    static void fail(Exception x) {
        x.printStackTrace();
        if (thrown == null) {
            thrown = x;
        }
        goOn = false;
    }
}
