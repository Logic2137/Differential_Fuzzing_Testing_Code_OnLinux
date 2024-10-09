



import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.StampedLock;

public class ReadersUnlockAfterWriteUnlock {
    public static void main(String[] args) throws InterruptedException {
        final int RNUM = 2;
        final int REPS = 128;
        final StampedLock sl = new StampedLock();
        final AtomicReference<Throwable> bad = new AtomicReference<>();

        final CyclicBarrier iterationStart = new CyclicBarrier(RNUM + 1);
        final CyclicBarrier readersHaveLocks = new CyclicBarrier(RNUM);
        final CyclicBarrier writerHasLock = new CyclicBarrier(RNUM + 1);

        Runnable reader = () -> {
            try {
                for (int i = 0; i < REPS; i++) {
                    iterationStart.await();
                    writerHasLock.await();
                    long rs = sl.readLock();

                    
                    
                    readersHaveLocks.await();

                    sl.unlockRead(rs);
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
                bad.set(ex);
            }
        };

        Thread[] threads = new Thread[RNUM];
        for (int i = 0 ; i < RNUM; i++) {
            Thread thread = new Thread(reader, "Reader");
            threads[i] = thread;
            thread.start();
        }
        for (int i = 0; i < REPS; i++) {
            try {
                iterationStart.await();
                long ws = sl.writeLock();
                writerHasLock.await();
                awaitWaitState(threads);
                sl.unlockWrite(ws);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        for (Thread thread : threads)
            thread.join();
        if (bad.get() != null)
            throw new AssertionError(bad.get());
    }

    static void awaitWaitState(Thread[] threads) {
        restart: for (;;) {
            for (Thread thread : threads) {
                if (thread.getState() != Thread.State.WAITING) {
                    Thread.yield();
                    continue restart;
                }
            }
            break;
        }
    }
}
