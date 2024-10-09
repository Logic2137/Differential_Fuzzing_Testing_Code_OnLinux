import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class NonComponentSourcePost {

    public static final int COUNT = 100;

    public static CountDownLatch go = new CountDownLatch(COUNT);

    public static void main(String[] args) throws Throwable {
        EventQueue q = new EventQueue();
        for (int i = 0; i < COUNT; i++) {
            q.postEvent(new NewActionEvent());
        }
        if (!go.await(30, TimeUnit.SECONDS)) {
            throw new RuntimeException("Timeout");
        }
        AWTEvent event = q.peekEvent();
        if (event != null) {
            throw new Exception("Non null event: " + event);
        }
        if (NewActionEvent.result != NewActionEvent.sum) {
            throw new Exception("result: " + NewActionEvent.result + "  sum: " + NewActionEvent.sum);
        }
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(q);
    }

    static class NewActionEvent extends ActionEvent implements ActiveEvent {

        static int counter = 1;

        static int sum = 0;

        static int result = 0;

        int myval;

        public NewActionEvent() {
            super("", ACTION_PERFORMED, "" + counter);
            myval = counter++;
            sum += myval;
        }

        public synchronized void dispatch() {
            result += myval;
            try {
                wait(100);
            } catch (InterruptedException e) {
            }
            go.countDown();
        }
    }
}
