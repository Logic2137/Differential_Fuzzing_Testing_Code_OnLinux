import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GCRetention {

    static class CustomPool extends ScheduledThreadPoolExecutor {

        CustomPool(int n) {
            super(n);
        }

        protected <V> RunnableScheduledFuture<V> decorateTask(final Runnable r, final RunnableScheduledFuture<V> task) {
            return new RunnableScheduledFuture<V>() {

                public void run() {
                    System.err.println(r);
                    task.run();
                }

                public boolean isPeriodic() {
                    return task.isPeriodic();
                }

                public V get() throws InterruptedException, ExecutionException {
                    return task.get();
                }

                public V get(long x, TimeUnit y) throws InterruptedException, ExecutionException, TimeoutException {
                    return task.get(x, y);
                }

                public boolean isDone() {
                    return task.isDone();
                }

                public boolean isCancelled() {
                    return task.isCancelled();
                }

                public boolean cancel(boolean x) {
                    return task.cancel(x);
                }

                public long getDelay(TimeUnit x) {
                    return task.getDelay(x);
                }

                public int compareTo(Delayed x) {
                    return task.compareTo(x);
                }
            };
        }
    }

    void removeAll(ReferenceQueue<?> q, int n) throws InterruptedException {
        for (int j = n; j-- > 0; ) {
            if (q.poll() == null) {
                for (; ; ) {
                    System.gc();
                    if (q.remove(1000) != null)
                        break;
                    System.out.printf("%d/%d unqueued references remaining%n", j + 1, n);
                }
            }
        }
        check(q.poll() == null);
    }

    void test(String[] args) throws Throwable {
        final CustomPool pool = new CustomPool(10);
        final int size = 100;
        final ReferenceQueue<Object> q = new ReferenceQueue<>();
        final List<WeakReference<?>> refs = new ArrayList<>(size);
        final List<Future<?>> futures = new ArrayList<>(size);
        class Task implements Runnable {

            final Object x;

            Task() {
                refs.add(new WeakReference<>(x = new Object(), q));
            }

            public void run() {
                System.out.println(x);
            }
        }
        for (int i = size; i-- > 0; ) futures.add(pool.schedule(new Task(), i + 1, TimeUnit.MINUTES));
        futures.forEach(future -> future.cancel(false));
        futures.clear();
        pool.purge();
        removeAll(q, size);
        for (WeakReference<?> ref : refs) check(ref.get() == null);
        pool.shutdown();
        pool.awaitTermination(1L, TimeUnit.DAYS);
    }

    volatile int passed = 0, failed = 0;

    void pass() {
        passed++;
    }

    void fail() {
        failed++;
        Thread.dumpStack();
    }

    void fail(String msg) {
        System.err.println(msg);
        fail();
    }

    void unexpected(Throwable t) {
        failed++;
        t.printStackTrace();
    }

    void check(boolean cond) {
        if (cond)
            pass();
        else
            fail();
    }

    void equal(Object x, Object y) {
        if (x == null ? y == null : x.equals(y))
            pass();
        else
            fail(x + " not equal to " + y);
    }

    public static void main(String[] args) throws Throwable {
        new GCRetention().instanceMain(args);
    }

    void instanceMain(String[] args) throws Throwable {
        try {
            test(args);
        } catch (Throwable t) {
            unexpected(t);
        }
        System.out.printf("%nPassed = %d, failed = %d%n%n", passed, failed);
        if (failed > 0)
            throw new AssertionError("Some tests failed");
    }
}
