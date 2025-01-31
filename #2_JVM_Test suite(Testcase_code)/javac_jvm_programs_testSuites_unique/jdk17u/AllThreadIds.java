import java.lang.management.*;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Phaser;
import java.util.function.Supplier;

public class AllThreadIds {

    private static final class ArgWrapper<T> {

        private final Supplier<T> val;

        public ArgWrapper(Supplier<T> val) {
            this.val = val;
        }

        @Override
        public String toString() {
            T resolved = val.get();
            return resolved != null ? resolved.toString() : null;
        }
    }

    static final int DAEMON_THREADS = 20;

    static final int USER_THREADS = 5;

    static final int ALL_THREADS = DAEMON_THREADS + USER_THREADS;

    private static final boolean[] live = new boolean[ALL_THREADS];

    private static final Thread[] allThreads = new Thread[ALL_THREADS];

    private static final Set<Long> allThreadIds = new HashSet<>();

    private static final ThreadMXBean mbean = ManagementFactory.getThreadMXBean();

    private static boolean testFailed = false;

    private static boolean trace = false;

    private static long prevTotalThreadCount = 0;

    private static long prevLiveTestThreadCount = 0;

    private static int prevPeakThreadCount = 0;

    private static final Phaser startupCheck = new Phaser(ALL_THREADS + 1);

    private static void printThreadList() {
        long[] list = mbean.getAllThreadIds();
        for (int i = 1; i <= list.length; i++) {
            System.out.println(i + ": Thread id = " + list[i - 1]);
        }
        for (int i = 0; i < ALL_THREADS; i++) {
            Thread t = allThreads[i];
            System.out.println(t.getName() + " Id = " + t.getId() + " die = " + live[i] + " alive = " + t.isAlive());
        }
    }

    private static void checkInitialState() throws Exception {
        updateCounters();
        checkThreadCount(0, 0);
    }

    private static void checkAllThreadsAlive() throws Exception {
        updateCounters();
        for (int i = 0; i < ALL_THREADS; i++) {
            setLive(i, true);
            Thread thread = new MyThread(i);
            thread.setDaemon(i < DAEMON_THREADS);
            thread.start();
            allThreadIds.add(thread.getId());
            allThreads[i] = thread;
        }
        startupCheck.arriveAndAwaitAdvance();
        checkThreadCount(ALL_THREADS, 0);
        if (trace) {
            printThreadList();
        }
        Set<Long> currentThreadIds = new HashSet<>();
        Arrays.stream(mbean.getAllThreadIds()).forEach(currentThreadIds::add);
        if (!currentThreadIds.containsAll(allThreadIds)) {
            testFailed = true;
            if (trace) {
                System.out.print(". TEST FAILED.");
            }
        }
        if (trace) {
            System.out.println();
        }
    }

    private static void checkDaemonThreadsDead() throws Exception {
        updateCounters();
        for (int i = 0; i < DAEMON_THREADS; i++) {
            setLive(i, false);
        }
        joinDaemonThreads();
        checkThreadCount(0, DAEMON_THREADS);
        long[] list = mbean.getAllThreadIds();
        for (int i = 0; i < ALL_THREADS; i++) {
            long expectedId = allThreads[i].getId();
            boolean found = false;
            boolean alive = (i >= DAEMON_THREADS);
            if (trace) {
                System.out.print("Looking for thread with id " + expectedId + (alive ? " expected alive." : " expected terminated."));
            }
            for (int j = 0; j < list.length; j++) {
                if (expectedId == list[j]) {
                    found = true;
                    break;
                }
            }
            if (alive != found) {
                testFailed = true;
            }
            if (trace) {
                if (alive != found) {
                    System.out.println(" TEST FAILED.");
                } else {
                    System.out.println();
                }
            }
        }
    }

    private static void checkAllThreadsDead() throws Exception {
        updateCounters();
        for (int i = DAEMON_THREADS; i < ALL_THREADS; i++) {
            setLive(i, false);
        }
        joinNonDaemonThreads();
        checkThreadCount(0, ALL_THREADS - DAEMON_THREADS);
    }

    private static void checkThreadCount(int numNewThreads, int numTerminatedThreads) {
        checkLiveThreads(numNewThreads, numTerminatedThreads);
        checkPeakThreads(numNewThreads);
        checkTotalThreads(numNewThreads);
        checkThreadIds(numNewThreads, numTerminatedThreads);
    }

    private static void checkLiveThreads(int numNewThreads, int numTerminatedThreads) {
        int diff = numNewThreads - numTerminatedThreads;
        long threadCount = mbean.getThreadCount();
        long expectedThreadCount = prevLiveTestThreadCount + diff;
        if (threadCount < expectedThreadCount) {
            testFailed = true;
            System.err.println(MessageFormat.format("Unexpected number of threads count %d." + "The expected number is %d or greater", threadCount, expectedThreadCount));
        }
    }

    private static void checkPeakThreads(int numNewThreads) {
        long peakThreadCount = mbean.getPeakThreadCount();
        long expectedThreadCount = Math.max(prevPeakThreadCount, numNewThreads);
        if (peakThreadCount < expectedThreadCount) {
            testFailed = true;
            System.err.println(MessageFormat.format("Unexpected number of peak threads count %d." + "The expected number is %d or greater", peakThreadCount, expectedThreadCount));
        }
    }

    private static void checkTotalThreads(int numNewThreads) {
        long totalThreadCount = mbean.getTotalStartedThreadCount();
        long expectedThreadCount = prevTotalThreadCount + numNewThreads;
        if (totalThreadCount < expectedThreadCount) {
            testFailed = true;
            System.err.println(MessageFormat.format("Unexpected number of total threads %d." + "The expected number is %d or greater", totalThreadCount, expectedThreadCount));
        }
    }

    private static void checkThreadIds(int numNewThreads, int numTerminatedThreads) {
        int threadCount = mbean.getAllThreadIds().length;
        int expectedThreadCount = numNewThreads - numTerminatedThreads;
        if (threadCount < expectedThreadCount) {
            testFailed = true;
            System.err.println(MessageFormat.format("Unexpected number of threads %d." + "The expected number is %d or greater", threadCount, expectedThreadCount));
        }
    }

    private static long getTestThreadCount() {
        return Thread.getAllStackTraces().keySet().stream().filter(thread -> thread.isAlive() && allThreadIds.contains(thread.getId())).count();
    }

    private static void updateCounters() {
        prevTotalThreadCount = mbean.getTotalStartedThreadCount();
        prevLiveTestThreadCount = getTestThreadCount();
        prevPeakThreadCount = mbean.getPeakThreadCount();
    }

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("trace")) {
            trace = true;
        }
        checkInitialState();
        checkAllThreadsAlive();
        checkDaemonThreadsDead();
        checkAllThreadsDead();
        if (testFailed)
            throw new RuntimeException("TEST FAILED.");
        System.out.println("Test passed.");
    }

    private static void joinDaemonThreads() throws InterruptedException {
        for (int i = 0; i < DAEMON_THREADS; i++) {
            allThreads[i].join();
        }
    }

    private static void joinNonDaemonThreads() throws InterruptedException {
        for (int i = DAEMON_THREADS; i < ALL_THREADS; i++) {
            allThreads[i].join();
        }
    }

    private static void setLive(int i, boolean val) {
        synchronized (live) {
            live[i] = val;
        }
    }

    private static boolean isLive(int i) {
        synchronized (live) {
            return live[i];
        }
    }

    private static class MyThread extends Thread {

        int id;

        MyThread(int id) {
            this.id = id;
        }

        public void run() {
            startupCheck.arrive();
            while (isLive(id)) {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("Unexpected exception is thrown.");
                    e.printStackTrace(System.out);
                    testFailed = true;
                }
            }
        }
    }
}
