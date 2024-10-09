



import java.awt.*;
import java.awt.event.*;
import sun.awt.AppContext;
import sun.awt.SunToolkit;

public class PostEventOrderingTest {
    static boolean testPassed = true;

    public static void main(String[] args) throws Throwable {
        EventQueue q = Toolkit.getDefaultToolkit().getSystemEventQueue();
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                q.postEvent(new PostActionEvent());
                for (int k = 0; k < 10; k++) {
                    SunToolkit.postEvent(AppContext.getAppContext(), new PostActionEvent());
                }
            }
            for (int k = 0; k < 100; k++) {
                SunToolkit.postEvent(AppContext.getAppContext(), new PostActionEvent());
            }
        }

        for (;;) {
            Thread.currentThread().sleep(100);
            if (q.peekEvent() == null) {
                Thread.currentThread().sleep(100);
                if (q.peekEvent() == null)
                    break;
            }
        }

        if (!testPassed) {
            throw new Exception("PostEventOrderingTest FAILED -- events dispatched out of order.");
        } else {
            System.out.println("PostEventOrderingTest passed!");
        }
    }
}

class PostActionEvent extends ActionEvent implements ActiveEvent {
    static int counter = 0;
    static int mostRecent = -1;

    int myval;

    public PostActionEvent() {
        super("", ACTION_PERFORMED, "" + counter);
        myval = counter++;
    }

    public void dispatch() {
        
        if ((myval - mostRecent) != 1)
            PostEventOrderingTest.testPassed = false;
        mostRecent = myval;
    }
}
