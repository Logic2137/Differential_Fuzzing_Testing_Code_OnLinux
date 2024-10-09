
package jdk.jfr.event.runtime;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class LatchedThread extends Thread {
    public final static ThreadGroup THREAD_GROUP = new ThreadGroup("Latched Threads");
    private final CountDownLatch latch = new CountDownLatch(1);
    private final AtomicBoolean alive = new AtomicBoolean(true);

    public LatchedThread(String name) {
        super(THREAD_GROUP, name);
    }

    public void awaitStarted() throws InterruptedException {
        latch.await();
    }

    public void stopAndJoin() throws InterruptedException {
        alive.set(false);
        synchronized (alive) {
            alive.notify();
        }
        join();
    }

    public void run() {
        latch.countDown();
        while (alive.get()) {
            try {
                synchronized (alive) {
                    alive.wait(10);
                }
            } catch (InterruptedException e) {
                
            }
        }
    }
}
