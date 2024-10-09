import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import static sun.awt.OSInfo.*;

public class CopyScaledAreaTest {

    private static final int IMAGE_WIDTH = 800;

    private static final int IMAGE_HEIGHT = 800;

    private static final int X = 50;

    private static final int Y = 50;

    private static final int W = 100;

    private static final int H = 75;

    private static final int DX = 15;

    private static final int DY = 10;

    private static final int N = 3;

    private static final Color BACKGROUND_COLOR = Color.YELLOW;

    private static final Color FILL_COLOR = Color.ORANGE;

    private static final double[][] SCALES = { { 1.3, 1.4 }, { 0.3, 2.3 }, { 2.7, 0.1 } };

    private static boolean isSupported() {
        String d3d = System.getProperty("sun.java2d.d3d");
        return !Boolean.getBoolean(d3d) || getOSType() == OSType.WINDOWS;
    }

    private static int scale(int x, double scale) {
        return (int) Math.floor(x * scale);
    }

    private static VolatileImage createVolatileImage(GraphicsConfiguration conf) {
        return conf.createCompatibleVolatileImage(IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    private static void renderOffscreen(VolatileImage vImg, GraphicsConfiguration conf, double scaleX, double scaleY) {
        int attempts = 0;
        do {
            if (attempts > 10) {
                throw new RuntimeException("Too many attempts!");
            }
            if (vImg.validate(conf) == VolatileImage.IMAGE_INCOMPATIBLE) {
                vImg = createVolatileImage(conf);
            }
            Graphics2D g = vImg.createGraphics();
            g.setColor(BACKGROUND_COLOR);
            g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
            g.scale(scaleX, scaleY);
            g.setColor(FILL_COLOR);
            g.fillRect(X, Y, W, H);
            for (int i = 0; i < N; i++) {
                g.copyArea(X + i * DX, Y + i * DY, W, H, DX, DY);
            }
            g.dispose();
            attempts++;
        } while (vImg.contentsLost());
    }

    public static void main(String[] args) throws Exception {
        if (!isSupported()) {
            return;
        }
        GraphicsConfiguration graphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        for (double[] scales : SCALES) {
            testScale(scales[0], scales[1], graphicsConfiguration);
        }
    }

    private static void testScale(double scaleX, double scaleY, GraphicsConfiguration gc) throws Exception {
        BufferedImage buffImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = buffImage.createGraphics();
        VolatileImage vImg = createVolatileImage(gc);
        int attempts = 0;
        do {
            if (attempts > 10) {
                throw new RuntimeException("Too many attempts!");
            }
            int returnCode = vImg.validate(gc);
            if (returnCode == VolatileImage.IMAGE_RESTORED) {
                renderOffscreen(vImg, gc, scaleX, scaleY);
            } else if (returnCode == VolatileImage.IMAGE_INCOMPATIBLE) {
                vImg = createVolatileImage(gc);
                renderOffscreen(vImg, gc, scaleX, scaleY);
            }
            g.drawImage(vImg, 0, 0, null);
            attempts++;
        } while (vImg.contentsLost());
        g.dispose();
        int x = scale(X + N * DX, scaleX) + 1;
        int y = scale(Y + N * DY, scaleY) + 1;
        int w = scale(W, scaleX) - 2;
        int h = scale(H, scaleY) - 2;
        for (int i = x; i < x + w; i++) {
            for (int j = y; j < y + h; j++) {
                if (buffImage.getRGB(i, j) != FILL_COLOR.getRGB()) {
                    throw new RuntimeException("Wrong rectangle color!");
                }
            }
        }
    }
}
