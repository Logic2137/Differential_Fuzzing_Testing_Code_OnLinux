import java.lang.management.*;
import java.util.concurrent.Phaser;
import java.util.function.Supplier;

public class SynchronizationStatistics {

    private static class LockerThread extends Thread {

        public LockerThread(Runnable r) {
            super(r, "LockerThread");
        }
    }

    private static final ThreadMXBean mbean = ManagementFactory.getThreadMXBean();

    private static final boolean blockedTimeCheck = mbean.isThreadContentionMonitoringSupported();

    public static void main(String[] args) throws Exception {
        if (blockedTimeCheck) {
            mbean.setThreadContentionMonitoringEnabled(true);
        }
        if (!mbean.isThreadContentionMonitoringEnabled()) {
            throw new RuntimeException("TEST FAILED: " + "Thread Contention Monitoring is not enabled");
        }
        testBlockingOnSimpleMonitor();
        testBlockingOnNestedMonitor();
        testWaitingOnSimpleMonitor();
        testMultiWaitingOnSimpleMonitor();
        testWaitingOnNestedMonitor();
        System.out.println("Test passed.");
    }

    private static LockerThread newLockerThread(Runnable r) {
        LockerThread t = new LockerThread(r);
        t.setDaemon(true);
        return t;
    }

    private static void waitForThreadState(Thread t, Thread.State state) throws InterruptedException {
        while (t.getState() != state) {
            Thread.sleep(3);
        }
    }

    private static void testBlockingOnSimpleMonitor() throws Exception {
        System.out.println("testBlockingOnSimpleMonitor");
        final Object lock1 = new Object();
        System.out.println("Lock1 = " + lock1);
        final Phaser p = new Phaser(2);
        LockerThread lt = newLockerThread(new Runnable() {

            @Override
            public void run() {
                p.arriveAndAwaitAdvance();
                synchronized (lock1) {
                    System.out.println("[LockerThread obtained Lock1]");
                    p.arriveAndAwaitAdvance();
                }
                p.arriveAndAwaitAdvance();
            }
        });
        lt.start();
        long tid = lt.getId();
        ThreadInfo ti = mbean.getThreadInfo(tid);
        String lockName = null;
        synchronized (lock1) {
            p.arriveAndAwaitAdvance();
            waitForThreadState(lt, Thread.State.BLOCKED);
            do {
                lockName = mbean.getThreadInfo(tid).getLockName();
            } while (lockName == null);
        }
        p.arriveAndAwaitAdvance();
        testBlocked(ti, () -> mbean.getThreadInfo(tid), lockName, lock1);
        p.arriveAndDeregister();
        lt.join();
        printok();
    }

    private static void testBlockingOnNestedMonitor() throws Exception {
        System.out.println("testBlockingOnNestedMonitor");
        final Object lock1 = new Object();
        final Object lock2 = new Object();
        System.out.println("Lock1 = " + lock1);
        System.out.println("Lock2 = " + lock2);
        final Phaser p = new Phaser(2);
        LockerThread lt = newLockerThread(new Runnable() {

            @Override
            public void run() {
                p.arriveAndAwaitAdvance();
                synchronized (lock1) {
                    System.out.println("[LockerThread obtained Lock1]");
                    p.arriveAndAwaitAdvance();
                    p.arriveAndAwaitAdvance();
                    synchronized (lock2) {
                        System.out.println("[LockerThread obtained Lock2]");
                        p.arriveAndAwaitAdvance();
                    }
                    p.arriveAndAwaitAdvance();
                }
            }
        });
        lt.start();
        long tid = lt.getId();
        ThreadInfo ti = mbean.getThreadInfo(tid);
        String lockName = null;
        synchronized (lock1) {
            p.arriveAndAwaitAdvance();
            waitForThreadState(lt, Thread.State.BLOCKED);
            do {
                lockName = mbean.getThreadInfo(tid).getLockName();
            } while (lockName == null);
        }
        p.arriveAndAwaitAdvance();
        ti = testBlocked(ti, () -> mbean.getThreadInfo(tid), lockName, lock1);
        synchronized (lock2) {
            p.arriveAndAwaitAdvance();
            waitForThreadState(lt, Thread.State.BLOCKED);
            do {
                lockName = mbean.getThreadInfo(tid).getLockName();
            } while (lockName == null);
        }
        p.arriveAndAwaitAdvance();
        testBlocked(ti, () -> mbean.getThreadInfo(tid), lockName, lock2);
        p.arriveAndDeregister();
        lt.join();
        printok();
    }

    private static void testWaitingOnSimpleMonitor() throws Exception {
        System.out.println("testWaitingOnSimpleMonitor");
        final Object lock1 = new Object();
        final Phaser p = new Phaser(2);
        LockerThread lt = newLockerThread(new Runnable() {

            @Override
            public void run() {
                p.arriveAndAwaitAdvance();
                synchronized (lock1) {
                    System.out.println("[LockerThread obtained Lock1]");
                    try {
                        lock1.wait(300);
                    } catch (InterruptedException ex) {
                    }
                    p.arriveAndAwaitAdvance();
                }
                p.arriveAndAwaitAdvance();
            }
        });
        lt.start();
        ThreadInfo ti1 = mbean.getThreadInfo(lt.getId());
        synchronized (lock1) {
            p.arriveAndAwaitAdvance();
            waitForThreadState(lt, Thread.State.BLOCKED);
        }
        p.arriveAndAwaitAdvance();
        testWaited(ti1, () -> mbean.getThreadInfo(lt.getId()), 1);
        p.arriveAndDeregister();
        lt.join();
        printok();
    }

    private static void testMultiWaitingOnSimpleMonitor() throws Exception {
        System.out.println("testWaitingOnMultipleMonitors");
        final Object lock1 = new Object();
        final Phaser p = new Phaser(2);
        LockerThread lt = newLockerThread(new Runnable() {

            @Override
            public void run() {
                p.arriveAndAwaitAdvance();
                synchronized (lock1) {
                    System.out.println("[LockerThread obtained Lock1]");
                    for (int i = 0; i < 3; i++) {
                        try {
                            lock1.wait(300);
                        } catch (InterruptedException ex) {
                        }
                        p.arriveAndAwaitAdvance();
                    }
                }
                p.arriveAndAwaitAdvance();
            }
        });
        lt.start();
        ThreadInfo ti1 = mbean.getThreadInfo(lt.getId());
        synchronized (lock1) {
            p.arriveAndAwaitAdvance();
            waitForThreadState(lt, Thread.State.BLOCKED);
        }
        int phase = p.getPhase();
        while ((p.arriveAndAwaitAdvance() - phase) < 3) ;
        testWaited(ti1, () -> mbean.getThreadInfo(lt.getId()), 3);
        p.arriveAndDeregister();
        lt.join();
        printok();
    }

    private static void testWaitingOnNestedMonitor() throws Exception {
        System.out.println("testWaitingOnNestedMonitor");
        final Object lock1 = new Object();
        final Object lock2 = new Object();
        final Object lock3 = new Object();
        final Phaser p = new Phaser(2);
        LockerThread lt = newLockerThread(new Runnable() {

            @Override
            public void run() {
                p.arriveAndAwaitAdvance();
                synchronized (lock1) {
                    System.out.println("[LockerThread obtained Lock1]");
                    try {
                        lock1.wait(300);
                    } catch (InterruptedException ex) {
                    }
                    p.arriveAndAwaitAdvance();
                    synchronized (lock2) {
                        System.out.println("[LockerThread obtained Lock2]");
                        try {
                            lock2.wait(300);
                        } catch (InterruptedException ex) {
                        }
                        p.arriveAndAwaitAdvance();
                        synchronized (lock3) {
                            System.out.println("[LockerThread obtained Lock3]");
                            try {
                                lock3.wait(300);
                            } catch (InterruptedException ex) {
                            }
                            p.arriveAndAwaitAdvance();
                        }
                    }
                }
                p.arriveAndAwaitAdvance();
            }
        });
        lt.start();
        ThreadInfo ti1 = mbean.getThreadInfo(lt.getId());
        synchronized (lock1) {
            p.arriveAndAwaitAdvance();
            waitForThreadState(lt, Thread.State.BLOCKED);
        }
        synchronized (lock2) {
            p.arriveAndAwaitAdvance();
            waitForThreadState(lt, Thread.State.BLOCKED);
        }
        synchronized (lock3) {
            p.arriveAndAwaitAdvance();
            waitForThreadState(lt, Thread.State.BLOCKED);
        }
        p.arriveAndAwaitAdvance();
        testWaited(ti1, () -> mbean.getThreadInfo(lt.getId()), 3);
        p.arriveAndDeregister();
        lt.join();
        printok();
    }

    private static void printok() {
        System.out.println("OK\n");
    }

    private static void testWaited(ThreadInfo ti1, Supplier<ThreadInfo> ti2, int waited) throws InterruptedException {
        boolean error;
        do {
            error = false;
            ThreadInfo ti = ti2.get();
            long waitCntDiff = ti.getWaitedCount() - ti1.getWaitedCount();
            long waitTimeDiff = ti.getWaitedTime() - ti1.getWaitedTime();
            if (waitCntDiff < waited) {
                System.err.println("Unexpected diff in waited count. Expecting at least " + waited + " , got " + waitCntDiff);
                error = true;
            }
            if (waitTimeDiff <= 0) {
                System.err.println("Unexpected diff in waited time. Expecting increasing " + "value, got " + waitTimeDiff + "ms");
                error = true;
            }
            if (error) {
                System.err.println("Retrying in 20ms ...");
                Thread.sleep(20);
            }
        } while (error);
    }

    private static ThreadInfo testBlocked(ThreadInfo ti1, Supplier<ThreadInfo> ti2, String lockName, final Object lock) throws InterruptedException {
        boolean error;
        ThreadInfo ti = null;
        do {
            error = false;
            ti = ti2.get();
            long blkCntDiff = ti.getBlockedCount() - ti1.getBlockedCount();
            long blkTimeDiff = ti.getBlockedTime() - ti1.getBlockedTime();
            System.out.println("testBlocked: [" + blkCntDiff + ", " + blkTimeDiff + ", " + lockName + "]");
            if (blkCntDiff < 1) {
                System.err.println("Unexpected diff in blocked count. Expecting at least 1, " + "got " + blkCntDiff);
                error = true;
            }
            if (blkTimeDiff < 0) {
                System.err.println("Unexpected diff in blocked time. Expecting a positive " + "number, got " + blkTimeDiff);
                error = true;
            }
            if (!lockName.equals(lock.toString())) {
                System.err.println("Unexpected blocked monitor name. Expecting " + lock.toString() + ", got " + lockName);
                error = true;
            }
            if (error) {
                System.err.println("Retrying in 20ms ...");
                Thread.sleep(20);
            }
        } while (error);
        return ti;
    }
}
