import java.awt.event.AWTEventListener;
import java.util.EventListener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.event.EventListenerList;

public final class GetUpToDateData {

    static final EventListenerList listeners = new EventListenerList();

    static final EventListener o1 = new EventListener() {
    };

    static final AWTEventListener o2 = event -> {
    };

    public static void main(final String[] args) throws Exception {
        CountDownLatch go = new CountDownLatch(3);
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            listeners.add(EventListener.class, o1);
        });
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            listeners.add(AWTEventListener.class, o2);
        });
        Thread t3 = new Thread(() -> {
            while (listeners.getListenerCount() != 2) {
            }
            go.countDown();
        });
        Thread t4 = new Thread(() -> {
            while (listeners.getListeners(EventListener.class).length != 1 || listeners.getListeners(EventListener.class)[0] != o1) {
            }
            go.countDown();
        });
        Thread t5 = new Thread(() -> {
            while (listeners.getListeners(AWTEventListener.class).length != 1 || listeners.getListeners(AWTEventListener.class)[0] != o2) {
            }
            go.countDown();
        });
        t1.setDaemon(true);
        t2.setDaemon(true);
        t3.setDaemon(true);
        t4.setDaemon(true);
        t5.setDaemon(true);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        if (!go.await(10, TimeUnit.SECONDS)) {
            throw new RuntimeException("The test hangs");
        }
    }
}
