


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Destroy {

    static final class Task implements Runnable {
        final Semaphore sem;
        final CountDownLatch count;

        public Task(Semaphore sem, CountDownLatch count) {
            this.sem = sem;
            this.count = count;
        }

        @Override
        public void run() {
            try {
                count.countDown();
                sem.acquire();
            } catch (Throwable t) {
                t.printStackTrace();
            } finally {
                System.out.println(Thread.currentThread().getName()
                        + " exiting");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        testDestroyChild();
    }

    public static void testDestroyChild() throws Exception {
        ThreadGroup root = new ThreadGroup("root");
        ThreadGroup parent = new ThreadGroup(root,"parent");
        ThreadGroup child1 = new ThreadGroup(parent, "child1");
        CountDownLatch count = new CountDownLatch(2);
        Semaphore sem1 = new Semaphore(1);
        Semaphore sem2 = new Semaphore(1);
        Thread t1 = new Thread(parent, new Task(sem1, count), "PT1");
        Thread t2 = new Thread(parent, new Task(sem2, count), "PT2");
        sem1.acquire();
        sem2.acquire();
        try {

            t1.start();
            t2.start();

            System.out.println("\nAwaiting parent threads...");
            count.await();
            Thread[] threads = new Thread[2];
            int nb = root.enumerate(threads, true);
            if (nb != 2) {
                throw new AssertionError("wrong number of threads: " + nb);
            }

            Thread t3 = new Thread(child1::destroy, "destroy");
            AtomicInteger nbr = new AtomicInteger();
            Thread t4 = new Thread("enumerate") {
                public void run() {
                    Thread[] threads = new Thread[42];
                    nbr.addAndGet(root.enumerate(threads, true));
                }
            };
            t4.start();
            t3.start();
            t4.join();
            t3.join();
            if (nbr.get() != nb) {
                throw new AssertionError("wrong number of threads: " + nbr.get());
            }

        } finally {
            sem1.release();
            sem2.release();
        }
        t1.join();
        t2.join();
    }
}
