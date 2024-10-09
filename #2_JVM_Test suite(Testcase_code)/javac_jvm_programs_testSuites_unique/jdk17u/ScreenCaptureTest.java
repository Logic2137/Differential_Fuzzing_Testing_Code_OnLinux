import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.MultiResolutionImage;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Robot;
import java.util.List;

public class ScreenCaptureTest {

    private static Robot robot;

    private static Frame frame;

    private static boolean isHiDPI = true;

    private static final Color[] COLORS = { Color.GREEN, Color.BLUE, Color.ORANGE, Color.RED };

    public static void main(String[] args) throws Exception {
        frame = new Frame();
        frame.setBounds(0, 0, 400, 400);
        frame.setUndecorated(true);
        robot = new Robot();
        Panel panel = new Panel(new BorderLayout());
        Canvas canvas = new Canvas() {

            public void paint(Graphics g) {
                super.paint(g);
                int w = getWidth();
                int h = getHeight();
                g.setColor(COLORS[0]);
                g.fillRect(0, 0, w / 2, h / 2);
                g.setColor(COLORS[1]);
                g.fillRect(w / 2, 0, w / 2, h / 2);
                g.setColor(COLORS[2]);
                g.fillRect(0, h / 2, w / 2, h / 2);
                g.setColor(COLORS[3]);
                g.fillRect(w / 2, h / 2, w / 2, h / 2);
            }
        };
        panel.add(canvas);
        frame.add(panel);
        frame.setVisible(true);
        robot.delay(500);
        robot.waitForIdle();
        int w = frame.getWidth();
        int h = frame.getHeight();
        if (!robot.getPixelColor(w / 4, h / 4).equals(COLORS[0])) {
            throw new RuntimeException("Wrong Pixel Color! Expected GREEN");
        }
        if (!robot.getPixelColor(3 * w / 4, h / 4).equals(COLORS[1])) {
            throw new RuntimeException("Wrong Pixel Color! Expected BLUE");
        }
        if (!robot.getPixelColor(w / 4, 3 * h / 4).equals(COLORS[2])) {
            throw new RuntimeException("Wrong Pixel Color! Expected ORANGE");
        }
        if (!robot.getPixelColor(3 * w / 4, 3 * h / 4).equals(COLORS[3])) {
            throw new RuntimeException("Wrong Pixel Color! Expected RED");
        }
        AffineTransform tx = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getDefaultTransform();
        if (tx.getScaleX() == 1 && tx.getScaleY() == 1) {
            isHiDPI = false;
        }
        MultiResolutionImage image = robot.createMultiResolutionScreenCapture(frame.getBounds());
        List<Image> imageList = image.getResolutionVariants();
        int size = imageList.size();
        BufferedImage lowResImage;
        BufferedImage highResImage;
        if (!isHiDPI) {
            if (size != 1) {
                throw new RuntimeException(" Invalid variant size");
            }
            lowResImage = (BufferedImage) imageList.get(0);
            System.out.println(frame.getBounds());
            System.out.println(lowResImage.getWidth() + " " + lowResImage.getHeight());
            if (frame.getWidth() != lowResImage.getWidth() || frame.getHeight() != lowResImage.getHeight()) {
                throw new RuntimeException(" Invalid Image size");
            }
        } else {
            if (size != 2) {
                throw new RuntimeException(" Invalid variant size");
            }
            lowResImage = (BufferedImage) imageList.get(0);
            highResImage = (BufferedImage) imageList.get(1);
            int lW = (int) lowResImage.getWidth();
            int lH = (int) lowResImage.getHeight();
            int hW = (int) highResImage.getWidth();
            int hH = (int) highResImage.getHeight();
            if (hW != (tx.getScaleX() * lW) || hH != (tx.getScaleY() * lH)) {
                throw new RuntimeException(" Invalid Resolution Variants");
            }
            if (lowResImage.getRGB(lW / 4, lH / 4) != highResImage.getRGB(hW / 4, hH / 4)) {
                throw new RuntimeException("Wrong image color!");
            }
            if (lowResImage.getRGB(3 * lW / 4, lH / 4) != highResImage.getRGB(3 * hW / 4, hH / 4)) {
                throw new RuntimeException("Wrong image color!");
            }
            if (lowResImage.getRGB(lW / 4, 3 * lH / 4) != highResImage.getRGB(hW / 4, 3 * hH / 4)) {
                throw new RuntimeException("Wrong image color!");
            }
            if (lowResImage.getRGB(3 * lW / 4, 3 * lH / 4) != highResImage.getRGB(3 * hW / 4, 3 * hH / 4)) {
                throw new RuntimeException("Wrong image color!");
            }
        }
        frame.dispose();
    }
}
