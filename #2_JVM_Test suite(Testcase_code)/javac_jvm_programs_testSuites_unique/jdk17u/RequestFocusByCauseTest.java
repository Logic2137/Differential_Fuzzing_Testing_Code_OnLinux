import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class RequestFocusByCauseTest {

    static boolean success;

    public static void main(String[] args) throws Exception {
        testRequestFocusCause();
        testRequestFocusTemporaryCause();
        testRequestFocusInWindowCause();
        System.out.println("ok");
    }

    private static void testRequestFocusCause() throws AWTException {
        Frame frame = new Frame();
        Component c = new Button();
        frame.add(new Button());
        frame.add(c);
        c.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                success = e.getCause() == FocusEvent.Cause.UNEXPECTED;
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
        Robot robot = new Robot();
        try {
            frame.setVisible(true);
            robot.waitForIdle();
            robot.delay(200);
            success = false;
            c.requestFocus(FocusEvent.Cause.UNEXPECTED);
            robot.waitForIdle();
            robot.delay(200);
            if (!success) {
                throw new RuntimeException("request failed");
            }
        } finally {
            frame.dispose();
        }
    }

    private static void testRequestFocusTemporaryCause() throws AWTException {
        Frame frame = new Frame();
        frame.add(new Button() {

            @Override
            protected boolean requestFocus(boolean temporary, FocusEvent.Cause cause) {
                success = cause == FocusEvent.Cause.ROLLBACK;
                return super.requestFocus(temporary, cause);
            }
        });
        Component c = new Button() {

            @Override
            public void requestFocus() {
                super.requestFocus();
                setFocusable(false);
            }
        };
        frame.add(c);
        Robot robot = new Robot();
        try {
            frame.setVisible(true);
            robot.waitForIdle();
            robot.delay(200);
            success = false;
            c.requestFocus();
            robot.waitForIdle();
            robot.delay(200);
            if (!success) {
                throw new RuntimeException("rollback request is not triggered");
            }
        } finally {
            frame.dispose();
        }
    }

    private static void testRequestFocusInWindowCause() throws AWTException {
        Frame frame = new Frame();
        Component c = new Button();
        frame.add(new Button());
        frame.add(c);
        c.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                success = e.getCause() == FocusEvent.Cause.UNEXPECTED;
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
        Robot robot = new Robot();
        try {
            frame.setVisible(true);
            robot.waitForIdle();
            robot.delay(200);
            success = false;
            c.requestFocusInWindow(FocusEvent.Cause.UNEXPECTED);
            robot.waitForIdle();
            robot.delay(200);
            if (!success) {
                throw new RuntimeException("request in window failed");
            }
        } finally {
            frame.dispose();
        }
    }
}
