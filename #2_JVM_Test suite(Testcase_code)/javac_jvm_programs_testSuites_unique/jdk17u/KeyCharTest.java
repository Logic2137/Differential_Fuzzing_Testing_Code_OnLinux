import java.awt.AWTEvent;
import java.awt.Frame;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.util.Locale;

public class KeyCharTest {

    private static volatile int eventsCount = 0;

    static {
        Locale.setDefault(Locale.ENGLISH);
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {

            @Override
            public void eventDispatched(AWTEvent event) {
                eventsCount++;
                char delete = ((KeyEvent) event).getKeyChar();
                if (delete != '\u007f') {
                    throw new RuntimeException("Key char is not delete: '" + delete + "'");
                }
            }
        }, AWTEvent.KEY_EVENT_MASK);
    }

    public static void main(String[] args) throws Exception {
        Frame frame = new Frame();
        frame.setSize(300, 300);
        frame.setVisible(true);
        Robot robot = new Robot();
        robot.setAutoDelay(50);
        robot.waitForIdle();
        robot.keyPress(KeyEvent.VK_DELETE);
        robot.keyRelease(KeyEvent.VK_DELETE);
        robot.waitForIdle();
        frame.dispose();
        if (eventsCount != 3) {
            throw new RuntimeException("Wrong number of key events: " + eventsCount);
        }
    }
}
