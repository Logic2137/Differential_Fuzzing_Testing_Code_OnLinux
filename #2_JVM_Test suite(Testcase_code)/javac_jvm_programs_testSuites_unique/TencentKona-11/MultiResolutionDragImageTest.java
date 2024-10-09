import sun.awt.image.MultiResolutionToolkitImage;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import static java.awt.event.InputEvent.BUTTON1_DOWN_MASK;

public class MultiResolutionDragImageTest {

    private static final Color COLOR_1X = Color.BLUE;

    private static final Color COLOR_2X = Color.RED;

    private static JFrame frame;

    private static JTextField field;

    private static Point p;

    public static void main(String[] args) throws Exception {
        final String test = args[0];
        switch(test) {
            case "TEST_DRAG":
                testDrag();
                break;
            default:
                throw new RuntimeException("Unknown test: " + test);
        }
    }

    private static void testDrag() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            frame = new JFrame();
            field = new JTextField("Drag Me");
            setupFrame(frame, field);
            frame.setVisible(true);
        });
        final Robot robot = new Robot();
        robot.setAutoDelay(500);
        robot.setAutoWaitForIdle(true);
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(() -> {
            p = new Point(field.getWidth() / 2, field.getHeight() / 2);
            SwingUtilities.convertPointToScreen(p, field);
        });
        robot.mouseMove(p.x, p.y);
        robot.mousePress(BUTTON1_DOWN_MASK);
        p.translate(10, 10);
        robot.mouseMove(p.x, p.y);
        p.translate(5, 5);
        final Color color = robot.getPixelColor(p.x, p.y);
        robot.mouseRelease(BUTTON1_DOWN_MASK);
        SwingUtilities.invokeAndWait(frame::dispose);
        final float scaleFactor = getScaleFactor();
        final Color testColor = (1 < scaleFactor) ? COLOR_2X : COLOR_1X;
        if (!similar(testColor, color)) {
            throw new RuntimeException("TEST FAILED: Image with wrong resolution is used for drag image!");
        }
        System.out.println("TEST PASSED!");
    }

    private static void setupFrame(final JFrame frame, final JTextField field) {
        frame.setBounds(0, 0, 50, 50);
        frame.setLayout(new BorderLayout());
        field.setDragEnabled(true);
        final TransferHandler transferHandler = field.getTransferHandler();
        transferHandler.setDragImage(createMultiResolutionImage());
        frame.getContentPane().add(field, BorderLayout.CENTER);
    }

    private static boolean similar(Color c1, Color c2) {
        return similar(c1.getRed(), c2.getRed()) && similar(c1.getGreen(), c2.getGreen()) && similar(c1.getBlue(), c2.getBlue());
    }

    private static boolean similar(int n, int m) {
        return Math.abs(n - m) <= 50;
    }

    static float getScaleFactor() {
        return (float) GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getDefaultTransform().getScaleX();
    }

    private static Image createMultiResolutionImage() {
        return new MultiResolutionToolkitImage(createImage(50, COLOR_1X), createImage(100, COLOR_2X));
    }

    private static Image createImage(final int length, final Color color) {
        final BufferedImage image = new BufferedImage(length, length, BufferedImage.TYPE_INT_ARGB_PRE);
        final Graphics graphics = image.getGraphics();
        graphics.setColor(color);
        graphics.fillRect(0, 0, length, length);
        graphics.dispose();
        return image;
    }
}
