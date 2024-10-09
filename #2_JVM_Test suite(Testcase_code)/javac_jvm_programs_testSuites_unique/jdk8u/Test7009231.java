import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class Test7009231 {

    public static void main(String[] args) throws InterruptedException {
        doTest(8);
    }

    private static void doTest(int nThreads) throws InterruptedException {
        Thread[] aThreads = new Thread[nThreads];
        final AtomicLong atl = new AtomicLong();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < nThreads; i++) {
            aThreads[i] = new RunnerThread(atl, 1L << (8 * i));
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < nThreads; i++) {
            aThreads[i].start();
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < nThreads; i++) {
            aThreads[i].join();
        }
    }

    public static class RunnerThread extends Thread {

        public RunnerThread(AtomicLong atomic, long lMask) {
            m_lMask = lMask;
            m_atomic = atomic;
        }

        public void run() {
            AtomicLong atomic = m_atomic;
            long lMask = m_lMask;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            for (int i = 0; i < 100000; i++) {
                setBit(atomic, lMask);
                clearBit(atomic, lMask);
            }
        }

        protected void setBit(AtomicLong atomic, long lMask) {
            long lWord;
            do {
                lWord = atomic.get();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            } while (!atomic.compareAndSet(lWord, lWord | lMask));
            if ((atomic.get() & lMask) == 0L) {
                throw new InternalError();
            }
        }

        protected void clearBit(AtomicLong atomic, long lMask) {
            long lWord;
            do {
                lWord = atomic.get();
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
            } while (!atomic.compareAndSet(lWord, lWord & ~lMask));
            if ((atomic.get() & lMask) != 0L) {
                throw new InternalError();
            }
        }

        private long m_lMask;

        private AtomicLong m_atomic;
    }
}
