
package jdk.test.lib.jfr;

import java.util.ArrayList;
import java.util.List;


public class Stressor {
    public static void execute(int numberOfThreads, Thread.UncaughtExceptionHandler eh, Runnable task) throws Exception {
        List<Thread> threads = new ArrayList<>();
        for (int n = 0; n < numberOfThreads; ++n) {
            Thread t = new Thread(task);
            t.setUncaughtExceptionHandler(eh);
            threads.add(t);
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
    }
}
