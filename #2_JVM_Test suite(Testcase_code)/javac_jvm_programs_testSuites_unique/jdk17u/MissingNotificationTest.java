import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.management.timer.Timer;
import javax.management.Notification;
import javax.management.NotificationListener;

public class MissingNotificationTest {

    private static int TASK_COUNT = 10000;

    private static long fixedDelay = 0;

    private static class NotifListener implements NotificationListener {

        int count;

        public synchronized void handleNotification(Notification notification, Object handback) {
            count++;
        }

        synchronized int getCount() {
            return count;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(">>> Test for missing notifications.");
        System.out.println(">>> Create a Timer object.");
        final Timer timer = new Timer();
        timer.start();
        NotifListener listener = new NotifListener();
        timer.addNotificationListener(listener, null, null);
        ExecutorService executor = Executors.newFixedThreadPool(100);
        final Random rand = new Random();
        for (int i = 0; i < TASK_COUNT; i++) {
            executor.execute(new Runnable() {

                public void run() {
                    long dateMillis = System.currentTimeMillis() + fixedDelay + rand.nextInt(2000);
                    Date date = new Date(dateMillis);
                    timer.addNotification("type", "msg", "userData", date);
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(20, TimeUnit.SECONDS);
        waitForNotificationsToEnd(listener);
        timer.stop();
        if (listener.count < TASK_COUNT) {
            throw new RuntimeException("Not fired: " + (TASK_COUNT - listener.count));
        } else {
            System.out.println(">>> All notifications handled OK");
        }
        System.out.println(">>> Bye bye!");
    }

    private static void waitForNotificationsToEnd(NotifListener listener) throws InterruptedException {
        int oldCout = listener.getCount();
        int noChangeCounter = 1;
        while (listener.getCount() < TASK_COUNT) {
            Thread.sleep(1000);
            System.out.print('.');
            if (oldCout == listener.getCount()) {
                if (++noChangeCounter > 10) {
                    break;
                }
            } else {
                noChangeCounter = 1;
            }
            oldCout = listener.getCount();
        }
        System.out.println();
    }
}
