

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


public class EventWhenTest {

    private static volatile int eventsCount = 0;
    private static volatile boolean failed = false;

    static {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            long lastWhen = 0;

            @Override
            public void eventDispatched(AWTEvent event) {
                long curWhen;
                if (event instanceof KeyEvent) {
                    curWhen = ((KeyEvent) event).getWhen();
                } else if (event instanceof MouseEvent) {
                    curWhen = ((MouseEvent) event).getWhen();
                } else {
                    return;
                }

                eventsCount++;

                if (curWhen < lastWhen) {
                    System.err.println("FAILED: " + curWhen + " < " + lastWhen +
                        " for " + event);
                    failed = true;
                } else {
                    lastWhen = curWhen;
                }
            }
        }, AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
    }

    public static void main(String[] args) throws Exception {

        Frame frame = new Frame();

        try {
            Button b = new Button("Button");
            frame.setBounds(300, 300, 300, 300);
            frame.add(b);
            frame.setVisible(true);

            Robot robot = new Robot();
            robot.waitForIdle();
            robot.mouseMove((int)frame.getLocationOnScreen().getX() + 150,
                    (int)frame.getLocationOnScreen().getY() + 150);

            eventsCount = 0;
            System.out.println("Clicking mouse...");
            for (int i = 0; i < 300 && !failed; i++) {
                robot.mousePress(InputEvent.BUTTON1_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
                Thread.sleep(10);
                b.setLabel("Click: " + i);
            }

            if (eventsCount == 0) {
                throw new RuntimeException("No events were received");
            }

            if (failed) {
                throw new RuntimeException("Test failed.");
            }
            System.out.println("Clicking mouse done: " + eventsCount + " events.");

            b.requestFocusInWindow();
            robot.waitForIdle();

            eventsCount = 0;
            System.out.println("Typing a key...");
            for (int i = 0; i < 300 && !failed; i++) {
                robot.keyPress(KeyEvent.VK_A);
                robot.keyRelease(KeyEvent.VK_A);
                Thread.sleep(10);
                b.setLabel("Type: " + i);
            }
            System.out.println("Key typing done: " + eventsCount + " events.");

            if (eventsCount == 0) {
                throw new RuntimeException("No events were received");
            }

            if (failed) {
                throw new RuntimeException("Test failed.");
            }

            System.out.println("Success!");
        } finally {
            frame.dispose();
        }
    }
}
