import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class TranslucentJAppletTest {

    private static volatile GraphicsConfiguration graphicsConfig = null;

    private static JFrame frame;

    private static volatile boolean paintComponentCalled = false;

    private static void initAndShowGUI() {
        frame = new JFrame(graphicsConfig);
        JApplet applet = new JApplet();
        applet.setBackground(new Color(0, 0, 0, 0));
        JPanel panel = new JPanel() {

            protected void paintComponent(Graphics g) {
                paintComponentCalled = true;
                g.setColor(Color.RED);
                g.fillOval(0, 0, getWidth(), getHeight());
            }
        };
        panel.setDoubleBuffered(false);
        panel.setOpaque(false);
        applet.add(panel);
        frame.add(applet);
        frame.setBounds(100, 100, 200, 200);
        frame.setUndecorated(true);
        frame.setBackground(new Color(0, 0, 0, 0));
        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (GraphicsDevice gd : ge.getScreenDevices()) {
            if (gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT)) {
                for (GraphicsConfiguration gc : gd.getConfigurations()) {
                    if (gc.isTranslucencyCapable()) {
                        graphicsConfig = gc;
                        break;
                    }
                }
            }
            if (graphicsConfig != null) {
                break;
            }
        }
        if (graphicsConfig == null) {
            System.out.println("The system does not support translucency. Consider the test passed.");
            return;
        }
        Robot r = new Robot();
        Color color1 = r.getPixelColor(100, 100);
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                initAndShowGUI();
            }
        });
        r.waitForIdle();
        if (!paintComponentCalled) {
            throw new RuntimeException("Test FAILED: panel's paintComponent() method is not called");
        }
        Thread.sleep(1500);
        Color newColor1 = r.getPixelColor(100, 100);
        Color newColor2 = r.getPixelColor(200, 200);
        if (!color1.equals(newColor1)) {
            System.err.println("color1 = " + color1);
            System.err.println("newColor1 = " + newColor1);
            throw new RuntimeException("Test FAILED: frame pixel at (0, 0) is not transparent");
        }
        if (!newColor2.equals(Color.RED)) {
            System.err.println("newColor2 = " + newColor2);
            throw new RuntimeException("Test FAILED: frame pixel at (100, 100) is not red (transparent?)");
        }
        System.out.println("Test PASSED");
    }
}
