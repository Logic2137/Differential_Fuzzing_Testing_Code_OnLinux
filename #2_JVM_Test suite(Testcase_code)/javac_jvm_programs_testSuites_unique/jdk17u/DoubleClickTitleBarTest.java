import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Robot;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.AWTException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

public class DoubleClickTitleBarTest {

    private static Point position = null;

    private static JFrame frame = null;

    private static boolean windowMinimizedState = false, windowMaximizedState = false;

    final private static int X_OFFSET = 100, Y_OFFSET = 7;

    public static void main(String[] args) throws Exception {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    doTest();
                }
            });
            Robot robot = new Robot();
            robot.delay(500);
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    position = frame.getLocationOnScreen();
                }
            });
            robot.mouseMove(position.x + X_OFFSET, position.y + Y_OFFSET);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseMove(frame.getLocationOnScreen().x + 200, frame.getLocationOnScreen().y + 200);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.delay(500);
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    position = frame.getLocationOnScreen();
                }
            });
            robot.mouseMove(position.x + X_OFFSET, position.y + Y_OFFSET);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.delay(1000);
            if (!(windowMinimizedState && windowMaximizedState)) {
                throw new RuntimeException("Test failed:");
            }
        } finally {
            if (frame != null) {
                frame.dispose();
            }
        }
    }

    private static void doTest() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        WindowStateListener listener = new WindowAdapter() {

            public void windowStateChanged(WindowEvent evt) {
                int oldState = evt.getOldState();
                int newState = evt.getNewState();
                if ((oldState & JFrame.MAXIMIZED_BOTH) != 0 && (newState & JFrame.MAXIMIZED_BOTH) == 0) {
                    windowMinimizedState = true;
                } else if (windowMinimizedState && (oldState & JFrame.MAXIMIZED_BOTH) == 0 && (newState & JFrame.MAXIMIZED_BOTH) != 0) {
                    windowMaximizedState = true;
                }
            }
        };
        frame.addWindowStateListener(listener);
        frame.setSize(200, 200);
        frame.setLocation(100, 100);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(true);
        frame.setVisible(true);
    }
}
