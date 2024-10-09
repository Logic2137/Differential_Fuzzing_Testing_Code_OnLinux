
package gc.stress;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TestStressIHOPMultiThread {

    public final static List<Object> GARBAGE = new LinkedList<>();

    private final long HEAP_SIZE;

    private final long HEAP_PREALLOC_SIZE;

    private final long HEAP_ALLOC_SIZE;

    private final int CHUNK_SIZE = 100000;

    private final int TIMEOUT;

    private final int THREADS;

    private final int HEAP_LOW_BOUND;

    private final int HEAP_HIGH_BOUND;

    private volatile boolean running = true;

    private final List<AllocationThread> threads;

    public static void main(String[] args) throws InterruptedException {
        new TestStressIHOPMultiThread().start();
    }

    TestStressIHOPMultiThread() {
        TIMEOUT = Integer.getInteger("timeout") * 60;
        THREADS = Integer.getInteger("threads");
        HEAP_LOW_BOUND = Integer.getInteger("heapUsageMinBound");
        HEAP_HIGH_BOUND = Integer.getInteger("heapUsageMaxBound");
        HEAP_SIZE = Runtime.getRuntime().maxMemory();
        HEAP_PREALLOC_SIZE = HEAP_SIZE * HEAP_LOW_BOUND / 100;
        HEAP_ALLOC_SIZE = HEAP_SIZE * (HEAP_HIGH_BOUND - HEAP_LOW_BOUND) / 100;
        threads = new ArrayList<>(THREADS);
    }

    public void start() throws InterruptedException {
        fill();
        createThreads();
        waitForStress();
        stressDone();
        waitForFinish();
    }

    private void fill() {
        long allocated = 0;
        while (allocated < HEAP_PREALLOC_SIZE) {
            GARBAGE.add(new byte[CHUNK_SIZE]);
            allocated += CHUNK_SIZE;
        }
    }

    private void createThreads() {
        for (int i = 0; i < THREADS; ++i) {
            System.out.println("Create thread " + i);
            AllocationThread thread = new TestStressIHOPMultiThread.AllocationThread(i, HEAP_ALLOC_SIZE / THREADS);
            GARBAGE.add(thread.getList());
            threads.add(thread);
        }
        threads.forEach(t -> t.start());
    }

    private void waitForFinish() {
        threads.forEach(thread -> {
            thread.silentJoin();
        });
    }

    private boolean isRunning() {
        return running;
    }

    private void stressDone() {
        running = false;
    }

    private void waitForStress() throws InterruptedException {
        Thread.sleep(TIMEOUT * 1000);
    }

    private class AllocationThread extends Thread {

        private final List<Object> garbage;

        private final long amountOfGarbage;

        private final int threadId;

        public AllocationThread(int id, long amount) {
            super("Thread " + id);
            threadId = id;
            amountOfGarbage = amount;
            garbage = new LinkedList<>();
        }

        public List<Object> getList() {
            return garbage;
        }

        @Override
        public void run() {
            System.out.println("Start the thread " + threadId);
            while (TestStressIHOPMultiThread.this.isRunning()) {
                try {
                    allocate(amountOfGarbage);
                } catch (OutOfMemoryError e) {
                    free();
                    System.out.println("OutOfMemoryError occurred in thread " + threadId);
                    break;
                }
                free();
            }
        }

        private void silentJoin() {
            System.out.println("Join the thread " + threadId);
            try {
                join();
            } catch (InterruptedException ie) {
                throw new RuntimeException(ie);
            }
        }

        private void allocate(long amount) {
            long allocated = 0;
            while (allocated < amount && TestStressIHOPMultiThread.this.isRunning()) {
                garbage.add(new byte[CHUNK_SIZE]);
                allocated += CHUNK_SIZE;
            }
        }

        private void free() {
            garbage.clear();
        }
    }
}
