

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Random;





public class TestFalseDeadLock {
    private static ThreadMXBean bean;
    private static volatile boolean running = true;
    private static volatile boolean found = false;

    public static void main(String[] args) throws Exception {
        bean = ManagementFactory.getThreadMXBean();
        Thread[] threads = new Thread[500];
        for (int i = 0; i < threads.length; i++) {
            Test t = new Test();
            threads[i] = new Thread(t);
            threads[i].start();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
        }
        running = false;
        for (Thread t : threads) {
            t.join();
        }
        if (found) {
            throw new Exception("Deadlock reported, but there is no deadlock.");
        }
    }

    public static class Test implements Runnable {
        public void run() {
            Random r = new Random();
            while (running) {
                try {
                    synchronized (this) {
                        wait(r.nextInt(1000) + 1);
                    }
                } catch (InterruptedException ex) {
                }
                recurse(2000);
            }
            if (bean.findDeadlockedThreads() != null) {
                System.out.println("FOUND!");
                found = true;
            }
        }

        private void recurse(int i) {
            if (!running) {
                
                
                System.out.println("Hullo");
            }
            else if (i > 0) {
                recurse(i - 1);
            }
        }
    }
}
