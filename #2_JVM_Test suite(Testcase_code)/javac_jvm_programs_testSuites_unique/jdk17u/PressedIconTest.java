import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BaseMultiResolutionImage;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

public class PressedIconTest {

    private final static int IMAGE_SIZE = 300;

    private final static Color COLOR_1X = Color.RED;

    private final static Color COLOR_2X = Color.BLUE;

    private static JFrame frame;

    private static volatile double scale = -1;

    private static volatile int centerX;

    private static volatile int centerY;

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        robot.setAutoDelay(50);
        SwingUtilities.invokeAndWait(() -> createAndShowGUI());
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(() -> {
            scale = frame.getGraphicsConfiguration().getDefaultTransform().getScaleX();
            Point location = frame.getLocation();
            Dimension size = frame.getSize();
            centerX = location.x + size.width / 2;
            centerY = location.y + size.height / 2;
        });
        robot.waitForIdle();
        robot.mouseMove(centerX, centerY);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.waitForIdle();
        Thread.sleep(100);
        Color color = robot.getPixelColor(centerX, centerY);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        SwingUtilities.invokeAndWait(() -> frame.dispose());
        if ((scale == 1 && !similar(color, COLOR_1X)) || (scale == 2 && !similar(color, COLOR_2X))) {
            throw new RuntimeException("Colors are different!");
        }
    }

    private static void createAndShowGUI() {
        frame = new JFrame();
        frame.setSize(IMAGE_SIZE, IMAGE_SIZE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new BorderLayout());
        BufferedImage img1x = generateImage(1, COLOR_1X);
        BufferedImage img2x = generateImage(2, COLOR_2X);
        BaseMultiResolutionImage mri = new BaseMultiResolutionImage(new BufferedImage[] { img1x, img2x });
        Icon mrIcon = new ImageIcon(mri);
        JToggleButton button = new JToggleButton();
        button.setIcon(mrIcon);
        panel.add(button, BorderLayout.CENTER);
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    private static boolean similar(Color c1, Color c2) {
        return similar(c1.getRed(), c2.getRed()) && similar(c1.getGreen(), c2.getGreen()) && similar(c1.getBlue(), c2.getBlue());
    }

    private static boolean similar(int n, int m) {
        return Math.abs(n - m) <= 50;
    }

    private static BufferedImage generateImage(int scale, Color c) {
        int size = IMAGE_SIZE * scale;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.createGraphics();
        g.setColor(c);
        g.fillRect(0, 0, size, size);
        g.dispose();
        return img;
    }
}
