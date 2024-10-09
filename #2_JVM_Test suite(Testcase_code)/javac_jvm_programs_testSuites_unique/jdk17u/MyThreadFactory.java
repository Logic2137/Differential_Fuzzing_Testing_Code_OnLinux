import java.util.concurrent.ThreadFactory;
import java.util.*;

public class MyThreadFactory implements ThreadFactory {

    private static final Set<Thread> threads = new HashSet<Thread>();

    static boolean created(Thread t) {
        synchronized (threads) {
            return threads.contains(t);
        }
    }

    public MyThreadFactory() {
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setDaemon(true);
        synchronized (threads) {
            threads.add(t);
        }
        return t;
    }
}
