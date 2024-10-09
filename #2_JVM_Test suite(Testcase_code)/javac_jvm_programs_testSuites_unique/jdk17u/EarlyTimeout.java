import java.lang.InterruptedException;
import java.lang.System;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class EarlyTimeout extends Thread {

    static final int THREADS_COUNT = 2;

    static final int TIMEOUT = 1000;

    static Object referent = new Object();

    static final ReferenceQueue<Object> queue = new ReferenceQueue<Object>();

    static final WeakReference<Object> weakReference = new WeakReference<Object>(referent, queue);

    static final CountDownLatch startedSignal = new CountDownLatch(THREADS_COUNT);

    long actual;

    Reference<?> reference;

    public static void main(String[] args) throws Exception {
        EarlyTimeout[] threads = new EarlyTimeout[THREADS_COUNT];
        for (int i = 0; i < THREADS_COUNT; ++i) {
            threads[i] = new EarlyTimeout();
            threads[i].start();
        }
        startedSignal.await();
        Thread.sleep(TIMEOUT / 2);
        referent = null;
        System.gc();
        for (EarlyTimeout thread : threads) {
            thread.join();
        }
        if (weakReference.get() != null) {
            throw new RuntimeException("weakReference was not cleared");
        }
        int nonNullRefCount = 0;
        for (EarlyTimeout thread : threads) {
            if (thread.reference == null && thread.actual < TIMEOUT) {
                throw new RuntimeException("elapsed time " + thread.actual + " is less than timeout " + TIMEOUT);
            }
            if (thread.reference != null && thread.reference == weakReference) {
                nonNullRefCount++;
            }
        }
        if (nonNullRefCount > 1) {
            throw new RuntimeException("more than one references were removed from queue");
        }
    }

    public void run() {
        try {
            startedSignal.countDown();
            long start = System.nanoTime();
            reference = queue.remove(TIMEOUT);
            actual = NANOSECONDS.toMillis(System.nanoTime() - start);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
