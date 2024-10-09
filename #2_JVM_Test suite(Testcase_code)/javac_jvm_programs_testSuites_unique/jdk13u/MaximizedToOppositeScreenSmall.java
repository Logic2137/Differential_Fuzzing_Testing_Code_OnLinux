import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;

public final class MaximizedToOppositeScreenSmall {

    public static void main(String[] args) throws Exception {
        String os = System.getProperty("os.name").toLowerCase();
        if (!os.contains("windows") && !os.contains("os x")) {
            return;
        }
        if (!Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH)) {
            return;
        }
        var ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gds = ge.getScreenDevices();
        Robot robot = new Robot();
        for (GraphicsDevice gd1 : gds) {
            Rectangle framAt = gd1.getDefaultConfiguration().getBounds();
            framAt.grow(-framAt.width / 2 + 100, -framAt.height / 2 + 100);
            for (GraphicsDevice gd2 : gds) {
                Rectangle maxTo = gd2.getDefaultConfiguration().getBounds();
                maxTo.grow(-maxTo.width / 2 + 120, -maxTo.height / 2 + 120);
                Frame frame = new Frame(gd1.getDefaultConfiguration());
                try {
                    frame.setBounds(framAt);
                    frame.setVisible(true);
                    robot.waitForIdle();
                    robot.delay(1000);
                    frame.setMaximizedBounds(maxTo);
                    frame.setExtendedState(Frame.MAXIMIZED_BOTH);
                    robot.waitForIdle();
                    robot.delay(1000);
                    Rectangle actual = frame.getBounds();
                    if (!actual.equals(maxTo)) {
                        System.err.println("Actual: " + actual);
                        System.err.println("Expected: " + maxTo);
                        throw new RuntimeException("Wrong bounds");
                    }
                } finally {
                    frame.dispose();
                }
            }
        }
    }
}
