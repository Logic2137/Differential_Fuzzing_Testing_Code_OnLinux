



import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

public class Test8027025 {

    private static Frame frame;
    private static Window window;

    public static void main(String[] args) throws Exception {
        try {
            SwingUtilities.invokeAndWait(() -> {
                frame = new Frame("Dummy Frame");
                window = new Window(frame);
                window.setSize(200, 200);
                window.setLocationRelativeTo(frame);
                window.setVisible(true);
            });

            Robot robot = new Robot();
            robot.waitForIdle();

            AtomicReference<Point> point = new AtomicReference<>();
            SwingUtilities.invokeAndWait(() -> point.set(window.getLocationOnScreen()));

            if (point.get().getX() == 0 || point.get().getY() == 0) {
                throw new RuntimeException("Test failed. The location was not set");
            }
        } finally {
            if (frame != null) {
                frame.dispose();
            }
            if (window != null) {
                window.dispose();
            }
        }
    }
}
