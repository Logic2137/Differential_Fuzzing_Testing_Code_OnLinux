

import java.lang.management.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;


public class ThreadDaemonTest {

    public static void main(String[] args) throws Exception {
        final int NUM_THREADS = 20;
        final String THREAD_PREFIX = "ThreadDaemonTest-";

        final CountDownLatch started = new CountDownLatch(NUM_THREADS);
        final CountDownLatch finished = new CountDownLatch(1);
        final AtomicReference<Exception> fail = new AtomicReference<>(null);

        Thread[] allThreads = new Thread[NUM_THREADS];
        ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
        Random rand = new Random();

        for (int i = 0; i < NUM_THREADS; i++) {
            allThreads[i] = new Thread(new Runnable() {
                    public void run() {
                        try {
                            started.countDown();
                            finished.await();
                        } catch (InterruptedException e) {
                            fail.set(new Exception(
                                "Unexpected InterruptedException"));
                        }
                    }
                }, THREAD_PREFIX + i);
            allThreads[i].setDaemon(rand.nextBoolean());
            allThreads[i].start();
        }

        started.await();
        try {
            ThreadInfo[] allThreadInfos = mbean.dumpAllThreads(false, false);
            int count = 0;
            for (int i = 0; i < allThreadInfos.length; i++) {
                String threadName = allThreadInfos[i].getThreadName();
                if (threadName.startsWith(THREAD_PREFIX)) {
                    count++;
                    String[] nameAndNumber = threadName.split("-");
                    int threadNum = Integer.parseInt(nameAndNumber[1]);
                    if (allThreads[threadNum].isDaemon() !=
                        allThreadInfos[i].isDaemon()) {
                        throw new RuntimeException(
                            allThreads[threadNum] + " is not like " +
                            allThreadInfos[i] + ". TEST FAILED.");
                    }
                }
            }
            if (count != NUM_THREADS) {
                throw new RuntimeException("Wrong number of threads examined");
            }
        }
        finally { finished.countDown(); }

        for (int i = 0; i < NUM_THREADS; i++) {
            allThreads[i].join();
        }
        if (fail.get() != null) {
            throw fail.get();
        }
    }
}
