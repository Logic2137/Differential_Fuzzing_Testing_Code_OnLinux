



import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedTransferQueue;

public class NameConstructors {
    private static final String NAME1 = "Norm D. Plume";
    private static final String NAME2 = "Ann Onymous";

    public static void main (String[] args) throws InterruptedException {
        test(new Timer(NAME1), NAME1);
        test(new Timer(NAME2, true), NAME2);
    }

    public static void test(Timer timer, String expected) throws InterruptedException {
        try {
            LinkedTransferQueue<String> queue = new LinkedTransferQueue<>();

            TimerTask task = new TimerTask() {
                public void run() {
                    queue.put(Thread.currentThread().getName());
                }
            };

            timer.schedule(task, 0L); 
            String actual = queue.take();

            if (!expected.equals(actual)) {
                throw new AssertionError(
                    String.format("expected='%s', actual='%s'", expected, actual));
            }
        } finally {
            timer.cancel();
        }
    }
}
