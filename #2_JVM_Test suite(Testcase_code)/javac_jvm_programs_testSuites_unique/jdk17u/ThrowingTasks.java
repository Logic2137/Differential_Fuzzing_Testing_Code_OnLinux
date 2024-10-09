import java.security.Permission;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class ThrowingTasks {

    static final Random rnd = new Random();

    @SuppressWarnings("serial")
    static class UncaughtExceptions extends ConcurrentHashMap<Class<?>, Integer> {

        void inc(Class<?> key) {
            compute(key, (k, v) -> (v == null) ? 1 : v + 1);
        }
    }

    @SuppressWarnings("serial")
    static class UncaughtExceptionsTable extends Hashtable<Class<?>, Integer> {

        synchronized void inc(Class<?> key) {
            Integer v = get(key);
            put(key, (v == null) ? 1 : v + 1);
        }
    }

    static final UncaughtExceptions uncaughtExceptions = new UncaughtExceptions();

    static final UncaughtExceptionsTable uncaughtExceptionsTable = new UncaughtExceptionsTable();

    static final AtomicInteger totalUncaughtExceptions = new AtomicInteger(0);

    static final CountDownLatch uncaughtExceptionsLatch = new CountDownLatch(24);

    static final ThreadGroup tg = new ThreadGroup("Flaky");

    static final RuntimeException rte = new RuntimeException();

    static final Error error = new Error();

    static final Throwable weird = new Throwable();

    static final Exception checkedException = new Exception();

    static class Thrower implements Runnable {

        final Throwable t;

        Thrower(Throwable t) {
            this.t = t;
        }

        public void run() {
            if (t != null)
                ThrowingTasks.<RuntimeException>uncheckedThrow(t);
        }
    }

    static final List<Thrower> throwers = Arrays.asList(new Thrower(null), new Thrower(rte), new Thrower(error), new Thrower(weird), new Thrower(checkedException));

    static class Flaky implements Runnable {

        final Runnable beforeExecute;

        final Runnable execute;

        Flaky(Runnable beforeExecute, Runnable execute) {
            this.beforeExecute = beforeExecute;
            this.execute = execute;
        }

        public void run() {
            execute.run();
        }
    }

    static final List<Flaky> flakes = new ArrayList<>();

    static {
        for (Thrower x : throwers) for (Thrower y : throwers) flakes.add(new Flaky(x, y));
        Collections.shuffle(flakes);
    }

    static final CountDownLatch allStarted = new CountDownLatch(flakes.size());

    static final CountDownLatch allContinue = new CountDownLatch(1);

    static class PermissiveSecurityManger extends SecurityManager {

        public void checkPermission(Permission p) {
        }
    }

    static void checkTerminated(ThreadPoolExecutor tpe) {
        try {
            check(tpe.getQueue().isEmpty());
            check(tpe.isShutdown());
            check(tpe.isTerminated());
            check(!tpe.isTerminating());
            equal(tpe.getActiveCount(), 0);
            equal(tpe.getPoolSize(), 0);
            equal(tpe.getTaskCount(), tpe.getCompletedTaskCount());
            check(tpe.awaitTermination(0L, TimeUnit.SECONDS));
        } catch (Throwable t) {
            unexpected(t);
        }
    }

    static void spinAwait(Supplier<Boolean> waitingForGodot) {
        for (int spins = 0; !waitingForGodot.get(); ) {
            if ((spins = (spins + 1) & 3) > 0) {
                Thread.yield();
            } else {
                try {
                    Thread.sleep(4);
                } catch (InterruptedException unexpected) {
                    throw new AssertionError(unexpected);
                }
            }
        }
    }

    static class CheckingExecutor extends ThreadPoolExecutor {

        private final ReentrantLock lock = new ReentrantLock();

        CheckingExecutor() {
            super(10, 10, 1L, TimeUnit.HOURS, new LinkedBlockingQueue<Runnable>(), (ThreadFactory) (runnable) -> {
                Thread thread = new Thread(tg, runnable);
                Thread.UncaughtExceptionHandler handler = (t, e) -> {
                    check(!t.isInterrupted());
                    totalUncaughtExceptions.getAndIncrement();
                    uncaughtExceptions.inc(e.getClass());
                    uncaughtExceptionsTable.inc(e.getClass());
                    uncaughtExceptionsLatch.countDown();
                };
                thread.setUncaughtExceptionHandler(handler);
                return thread;
            });
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            final boolean lessThanCorePoolSize;
            lock.lock();
            try {
                allStarted.countDown();
                lessThanCorePoolSize = allStarted.getCount() < getCorePoolSize();
            } finally {
                lock.unlock();
            }
            if (lessThanCorePoolSize) {
                try {
                    allContinue.await();
                } catch (InterruptedException x) {
                    unexpected(x);
                }
            }
            beforeExecuteCount.getAndIncrement();
            check(!isTerminated());
            ((Flaky) r).beforeExecute.run();
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            afterExecuteCount.getAndIncrement();
            check(((Thrower) ((Flaky) r).execute).t == t);
            check(!isTerminated());
        }

        @Override
        protected void terminated() {
            try {
                terminatedCount.getAndIncrement();
                if (rnd.nextBoolean()) {
                    check(isShutdown());
                    check(isTerminating());
                    check(!isTerminated());
                    check(!awaitTermination(0L, TimeUnit.MINUTES));
                }
            } catch (Throwable t) {
                unexpected(t);
            }
        }
    }

    static final AtomicInteger beforeExecuteCount = new AtomicInteger(0);

    static final AtomicInteger afterExecuteCount = new AtomicInteger(0);

    static final AtomicInteger terminatedCount = new AtomicInteger(0);

    private static void realMain(String[] args) throws Throwable {
        if (rnd.nextBoolean())
            System.setSecurityManager(new PermissiveSecurityManger());
        CheckingExecutor tpe = new CheckingExecutor();
        for (Runnable task : flakes) tpe.execute(task);
        if (rnd.nextBoolean()) {
            allStarted.await();
            equal(tpe.getTaskCount(), (long) flakes.size());
            equal(tpe.getCompletedTaskCount(), (long) flakes.size() - tpe.getCorePoolSize());
        }
        allContinue.countDown();
        uncaughtExceptionsLatch.await();
        spinAwait(() -> tg.activeCount() == tpe.getCorePoolSize());
        tpe.shutdown();
        check(tpe.awaitTermination(10L, TimeUnit.MINUTES));
        checkTerminated(tpe);
        List<Map<Class<?>, Integer>> maps = new ArrayList<>();
        maps.add(uncaughtExceptions);
        maps.add(uncaughtExceptionsTable);
        for (Map<Class<?>, Integer> map : maps) {
            equal(map.get(Exception.class), throwers.size() + 1);
            equal(map.get(weird.getClass()), throwers.size() + 1);
            equal(map.get(Error.class), throwers.size() + 1);
            equal(map.get(RuntimeException.class), throwers.size() + 1);
            equal(map.size(), 4);
        }
        equal(totalUncaughtExceptions.get(), 4 * throwers.size() + 4);
        equal(beforeExecuteCount.get(), flakes.size());
        equal(afterExecuteCount.get(), throwers.size());
        equal(tpe.getCompletedTaskCount(), (long) flakes.size());
        equal(terminatedCount.get(), 1);
        tpe.shutdown();
        tpe.shutdownNow();
        check(tpe.awaitTermination(10L, TimeUnit.MINUTES));
        checkTerminated(tpe);
        equal(terminatedCount.get(), 1);
    }

    static volatile int passed = 0, failed = 0;

    static void pass() {
        passed++;
    }

    static void fail() {
        failed++;
        Thread.dumpStack();
    }

    static void fail(String msg) {
        System.out.println(msg);
        fail();
    }

    static void unexpected(Throwable t) {
        failed++;
        t.printStackTrace();
    }

    static void check(boolean cond) {
        if (cond)
            pass();
        else
            fail();
    }

    static void equal(Object x, Object y) {
        if (x == null ? y == null : x.equals(y))
            pass();
        else
            fail(x + " not equal to " + y);
    }

    public static void main(String[] args) throws Throwable {
        try {
            realMain(args);
        } catch (Throwable t) {
            unexpected(t);
        }
        System.out.printf("%nPassed = %d, failed = %d%n%n", passed, failed);
        if (failed > 0)
            throw new AssertionError("Some tests failed");
    }

    @SuppressWarnings("unchecked")
    static <T extends Throwable> void uncheckedThrow(Throwable t) throws T {
        throw (T) t;
    }
}
