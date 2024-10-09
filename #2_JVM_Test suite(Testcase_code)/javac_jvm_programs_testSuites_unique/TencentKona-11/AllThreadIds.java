



import java.lang.management.*;
import java.time.Instant;
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
    private static final boolean live[] = new boolean[ALL_THREADS];
    private static final Thread allThreads[] = new Thread[ALL_THREADS];
    private static final ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
    private static boolean testFailed = false;
    private static boolean trace = false;

    private static long prevTotalThreadCount = 0;
    private static int prevLiveThreadCount = 0;
    private static int prevPeakThreadCount = 0;

    private static final Phaser startupCheck = new Phaser(ALL_THREADS + 1);

    private static void printThreadList() {
        long[] list = mbean.getAllThreadIds();
        for (int i = 1; i <= list.length; i++) {
            System.out.println(i + ": Thread id = " + list[i-1]);
        }
        for (int i = 0; i < ALL_THREADS; i++) {
            Thread t = allThreads[i];
            System.out.println(t.getName() + " Id = " + t.getId() +
                " die = " + live[i] +
                " alive = " + t.isAlive());
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
            allThreads[i] = new MyThread(i);
            allThreads[i].setDaemon(i < DAEMON_THREADS);
            allThreads[i].start();
        }
        
        startupCheck.arriveAndAwaitAdvance();

        checkThreadCount(ALL_THREADS, 0);
        if (trace) {
            printThreadList();
        }
        
        long[] list = mbean.getAllThreadIds();

        for (int i = 0; i < ALL_THREADS; i++) {
            long expectedId = allThreads[i].getId();
            boolean found = false;

            if (trace) {
                System.out.print("Looking for thread with id " + expectedId);
            }
            for (int j = 0; j < list.length; j++) {
                if (expectedId == list[j]) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                testFailed = true;
            }
            if (trace) {
                if (!found) {
                    System.out.print(". TEST FAILED.");
                }
                System.out.println();
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
                System.out.print("Looking for thread with id " + expectedId +
                    (alive ? " expected alive." : " expected terminated."));
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

    private static void checkThreadCount(int numNewThreads,
                                         int numTerminatedThreads)
        throws Exception {

        checkLiveThreads(numNewThreads, numTerminatedThreads);
        checkPeakThreads(numNewThreads);
        checkTotalThreads(numNewThreads);
        checkThreadIds();
    }

    private static void checkLiveThreads(int numNewThreads,
                                         int numTerminatedThreads)
        throws InterruptedException {
        int diff = numNewThreads - numTerminatedThreads;

        waitTillEquals(
            diff + prevLiveThreadCount,
            ()->(long)mbean.getThreadCount(),
            "Unexpected number of live threads: " +
                " Prev live = %1$d Current live = ${provided} Threads added = %2$d" +
                " Threads terminated = %3$d",
            ()->prevLiveThreadCount,
            ()->numNewThreads,
            ()->numTerminatedThreads
        );
    }

    private static void checkPeakThreads(int numNewThreads)
        throws InterruptedException {

        waitTillEquals(numNewThreads + prevPeakThreadCount,
            ()->(long)mbean.getPeakThreadCount(),
            "Unexpected number of peak threads: " +
                " Prev peak = %1$d Current peak = ${provided} Threads added = %2$d",
            ()->prevPeakThreadCount,
            ()->numNewThreads
        );
    }

    private static void checkTotalThreads(int numNewThreads)
        throws InterruptedException {

        waitTillEquals(numNewThreads + prevTotalThreadCount,
            ()->mbean.getTotalStartedThreadCount(),
            "Unexpected number of total threads: " +
                " Prev Total = %1$d Current Total = ${provided} Threads added = %2$d",
            ()->prevTotalThreadCount,
            ()->numNewThreads
        );
    }

    private static void checkThreadIds() throws InterruptedException {
        long[] list = mbean.getAllThreadIds();

        waitTillEquals(
            list.length,
            ()->(long)mbean.getThreadCount(),
            "Array length returned by " +
                "getAllThreadIds() = %1$d not matched count = ${provided}",
            ()->list.length
        );
    }

    
    private static void waitTillEquals(long expectedVal, Supplier<Long> retrievedVal,
                                        String msgFormat, Supplier<Object> ... msgArgs)
        throws InterruptedException {
        Object[] args = null;

        long countPrev = -1;
        while (true) {
            Long count = retrievedVal.get();
            if (count == expectedVal) break;
            if (countPrev == -1 || countPrev != count) {
                if (args == null) {
                    args = new Object[msgArgs.length];
                    for(int i=0; i < msgArgs.length; i++) {
                        args[i] = new ArgWrapper<>((Supplier<Object>)msgArgs[i]);
                    }
                }
                System.err.format("TS: %s\n", Instant.now());
                System.err.format(
                    msgFormat
                        .replace("${provided}", String.valueOf(count))
                        .replace("$d", "$s"),
                    args
                ).flush();
                printThreadList();
                System.err.println("\nRetrying ...\n");
            }
            countPrev = count;
            Thread.sleep(1);
        }
    }

    private static void updateCounters() {
        prevTotalThreadCount = mbean.getTotalStartedThreadCount();
        prevLiveThreadCount = mbean.getThreadCount();
        prevPeakThreadCount = mbean.getPeakThreadCount();
    }

    public static void main(String args[]) throws Exception {
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
        synchronized(live) {
            live[i] = val;
        }
    }

    private static boolean isLive(int i) {
        synchronized(live) {
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
