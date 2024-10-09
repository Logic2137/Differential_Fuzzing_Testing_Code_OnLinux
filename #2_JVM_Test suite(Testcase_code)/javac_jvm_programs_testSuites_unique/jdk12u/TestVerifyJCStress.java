



import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class TestVerifyJCStress {

    public static void main(String[] args) throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(
                2,
                r -> {
                    Thread t = new Thread(r);
                    t.setDaemon(true);
                    return t;
                }
        );

        for (int c = 0; c < 10000; c++) {
            final Test[] tests = new Test[10000];
            for (int t = 0; t < tests.length; t++) {
                tests[t] = new Test();
            }

            Future<?> f1 = service.submit(() -> {
                IntResult2 r = new IntResult2();
                for (Test test : tests) {
                    test.RL_Us(r);
                }
            });
            Future<?> f2 = service.submit(() -> {
                for (Test test : tests) {
                    test.WLI_Us();
                }
            });

            f1.get();
            f2.get();
        }
    }

    public static class IntResult2 {
        int r1, r2;
    }

    public static class Test {
        final StampedLock lock = new StampedLock();

        int x, y;

        public void RL_Us(IntResult2 r) {
            StampedLock lock = this.lock;
            long stamp = lock.readLock();
            r.r1 = x;
            r.r2 = y;
            lock.unlock(stamp);
        }

        public void WLI_Us() {
            try {
                StampedLock lock = this.lock;
                long stamp = lock.writeLockInterruptibly();
                x = 1;
                y = 2;
                lock.unlock(stamp);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
