import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedLongSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@SuppressWarnings("serial")
public class FlakyMutex implements Lock {

    static class MyError extends Error {
    }

    static class MyException extends Exception {
    }

    static class MyRuntimeException extends RuntimeException {
    }

    static void checkThrowable(Throwable t) {
        if (!((t instanceof MyError) || (t instanceof MyException) || (t instanceof MyRuntimeException)))
            unexpected(t);
    }

    static void realMain(String[] args) throws Throwable {
        final ThreadLocalRandom rndMain = ThreadLocalRandom.current();
        final int nCpus = Runtime.getRuntime().availableProcessors();
        final int maxThreads = Math.min(4, nCpus);
        final int nThreads = rndMain.nextInt(1, maxThreads + 1);
        final int iterations = 10_000;
        final CyclicBarrier startingGate = new CyclicBarrier(nThreads);
        final ExecutorService es = Executors.newFixedThreadPool(nThreads);
        final FlakyMutex mutex = new FlakyMutex();
        final Runnable task = () -> {
            try {
                ThreadLocalRandom rnd = ThreadLocalRandom.current();
                startingGate.await();
                for (int i = 0; i < iterations; i++) {
                    for (; ; ) {
                        try {
                            if (rnd.nextBoolean())
                                mutex.lock();
                            else
                                mutex.lockInterruptibly();
                            break;
                        } catch (Throwable t) {
                            checkThrowable(t);
                        }
                    }
                    if (rnd.nextBoolean()) {
                        try {
                            check(!mutex.tryLock());
                        } catch (Throwable t) {
                            checkThrowable(t);
                        }
                    }
                    if (rnd.nextInt(10) == 0) {
                        try {
                            check(!mutex.tryLock(1, TimeUnit.MICROSECONDS));
                        } catch (Throwable t) {
                            checkThrowable(t);
                        }
                    }
                    if (rnd.nextBoolean()) {
                        check(mutex.isLocked());
                    }
                    mutex.unlock();
                }
            } catch (Throwable t) {
                unexpected(t);
            }
        };
        for (int i = 0; i < nThreads; i++) es.submit(task);
        es.shutdown();
        check(es.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS));
    }

    private static class FlakySync extends AbstractQueuedLongSynchronizer {

        private static final long serialVersionUID = -1L;

        public boolean isHeldExclusively() {
            return getState() == 1;
        }

        public boolean tryAcquire(long acquires) {
            if (hasQueuedPredecessors())
                check(getFirstQueuedThread() != Thread.currentThread());
            if (getFirstQueuedThread() == Thread.currentThread()) {
                check(hasQueuedThreads());
                check(!hasQueuedPredecessors());
            } else {
                do {
                } while (hasQueuedPredecessors() != hasQueuedThreads());
            }
            switch(ThreadLocalRandom.current().nextInt(10)) {
                case 0:
                    throw new MyError();
                case 1:
                    throw new MyRuntimeException();
                case 2:
                    FlakyMutex.<RuntimeException>uncheckedThrow(new MyException());
                default:
                    return compareAndSetState(0, 1);
            }
        }

        public boolean tryRelease(long releases) {
            setState(0);
            return true;
        }

        Condition newCondition() {
            return new ConditionObject();
        }
    }

    private final FlakySync sync = new FlakySync();

    public void lock() {
        sync.acquire(1);
    }

    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(timeout));
    }

    public void unlock() {
        sync.release(1);
    }

    public Condition newCondition() {
        return sync.newCondition();
    }

    public boolean isLocked() {
        return sync.isHeldExclusively();
    }

    public boolean hasQueuedThreads() {
        return sync.hasQueuedThreads();
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
        int runsPerTest = Integer.getInteger("jsr166.runsPerTest", 1);
        try {
            for (int i = runsPerTest; i-- > 0; ) realMain(args);
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
