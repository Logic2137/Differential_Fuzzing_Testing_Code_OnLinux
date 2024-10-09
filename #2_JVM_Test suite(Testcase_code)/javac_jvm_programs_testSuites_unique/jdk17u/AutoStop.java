import java.util.Timer;
import java.util.TimerTask;
import java.lang.ref.Reference;

public class AutoStop {

    static final Object wakeup = new Object();

    static Thread tdThread = null;

    static volatile int counter = 0;

    static final int COUNTER_LIMIT = 10;

    public static void main(String[] args) throws Exception {
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            public void run() {
                synchronized (wakeup) {
                    tdThread = Thread.currentThread();
                    wakeup.notify();
                }
            }
        }, 0);
        try {
            synchronized (wakeup) {
                while (tdThread == null) {
                    wakeup.wait();
                }
            }
        } catch (InterruptedException e) {
        }
        for (int i = 0; i < COUNTER_LIMIT; ++i) {
            t.schedule(new TimerTask() {

                public void run() {
                    ++counter;
                }
            }, 100);
        }
        Reference.reachabilityFence(t);
        t = null;
        System.gc();
        tdThread.join();
        int finalCounter = counter;
        if (finalCounter != COUNTER_LIMIT) {
            throw new RuntimeException("Unrun events: counter = " + finalCounter);
        }
    }
}
