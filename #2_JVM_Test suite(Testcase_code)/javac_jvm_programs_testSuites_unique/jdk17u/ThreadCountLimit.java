import java.util.concurrent.CountDownLatch;
import java.util.ArrayList;

public class ThreadCountLimit {

    static final int TIME_LIMIT_MS = 5000;

    static class Worker extends Thread {

        private final int index;

        private final CountDownLatch startSignal;

        Worker(int index, CountDownLatch startSignal) {
            this.index = index;
            this.startSignal = startSignal;
        }

        @Override
        public void run() {
            if ((index % 250) == 0) {
                System.out.println("INFO: thread " + index + " waiting to start");
            }
            try {
                startSignal.await();
            } catch (InterruptedException e) {
                throw new Error("Unexpected: " + e);
            }
            setName(String.valueOf(index));
            Thread.yield();
            if (index != Integer.parseInt(getName())) {
                throw new Error("setName/getName failed!");
            }
            if ((index % 250) == 0) {
                System.out.println("INFO: thread " + getName() + " working");
            }
        }
    }

    public static void main(String[] args) {
        CountDownLatch startSignal = new CountDownLatch(1);
        ArrayList<Worker> workers = new ArrayList<Worker>();
        int count = 1;
        long start = System.currentTimeMillis();
        try {
            while (true) {
                Worker w = new Worker(count, startSignal);
                w.start();
                workers.add(w);
                count++;
                long end = System.currentTimeMillis();
                if ((end - start) > TIME_LIMIT_MS) {
                    System.out.println("INFO: reached the time limit " + TIME_LIMIT_MS + " ms, with " + count + " threads created");
                    break;
                }
            }
        } catch (OutOfMemoryError e) {
            if (e.getMessage().contains("unable to create native thread")) {
                long end = System.currentTimeMillis();
                System.out.println("INFO: reached this process thread count limit at " + count + " [" + (end - start) + " ms]");
            } else {
                throw e;
            }
        }
        startSignal.countDown();
        try {
            for (Worker w : workers) {
                w.join();
            }
        } catch (InterruptedException e) {
            throw new Error("Unexpected: " + e);
        }
    }
}
