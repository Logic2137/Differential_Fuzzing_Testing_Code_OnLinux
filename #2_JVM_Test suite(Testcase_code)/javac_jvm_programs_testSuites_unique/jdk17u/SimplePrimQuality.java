import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SimplePrimQuality extends Canvas {

    private static final int SIZE = 300;

    private static boolean done;

    private static boolean testVI;

    private static volatile BufferedImage capture;

    private static void doCapture(Component test) {
        try {
            Robot robot = new Robot();
            Point pt1 = test.getLocationOnScreen();
            Rectangle rect = new Rectangle(pt1.x, pt1.y, test.getWidth(), test.getHeight());
            capture = robot.createScreenCapture(rect);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final int[][] rpts = { { 2, 0, 0, 0 }, { 12, 0, 1, 0 }, { 22, 0, 0, 1 }, { 32, 0, 1, 1 }, { 42, 0, 2, 1 }, { 52, 0, 1, 2 }, { 62, 0, 2, 2 }, { 72, 0, 5, 5 }, { 82, 0, 10, 10 }, { 97, 0, 15, 15 } };

    private void drawLine(Graphics2D g, int x, int y, int dx, int dy) {
        g.drawLine(x, y, x + dx, y + dy);
    }

    private void drawLines(Graphics2D g, int s) {
        drawLine(g, 2, 0, 0, 0);
        drawLine(g, 12, 0, 0, s);
        drawLine(g, 22, 0, s, 0);
        drawLine(g, 32, 0, s, s);
        drawLine(g, 42, 0, 0, -s);
        drawLine(g, 52, 0, -s, 0);
        drawLine(g, 62, 0, -s, -s);
        drawLine(g, 72, 0, -s, s);
        drawLine(g, 82, 0, s, -s);
    }

    private void fillRects(Graphics2D g) {
        for (int i = 0; i < rpts.length; i++) {
            g.fillRect(rpts[i][0], rpts[i][1], rpts[i][2], rpts[i][3]);
        }
    }

    private void drawRects(Graphics2D g) {
        for (int i = 0; i < rpts.length; i++) {
            g.drawRect(rpts[i][0], rpts[i][1], rpts[i][2], rpts[i][3]);
        }
    }

    private void fillOvals(Graphics2D g) {
        for (int i = 0; i < rpts.length; i++) {
            g.fill(new Ellipse2D.Float(rpts[i][0], rpts[i][1], rpts[i][2], rpts[i][3]));
        }
    }

    private void drawOvals(Graphics2D g) {
        for (int i = 0; i < rpts.length; i++) {
            g.draw(new Ellipse2D.Float(rpts[i][0], rpts[i][1], rpts[i][2], rpts[i][3]));
        }
    }

    private void renderShapes(Graphics2D g) {
        g.translate(0, 5);
        drawLines(g, 1);
        g.translate(0, 10);
        drawLines(g, 4);
        g.translate(0, 10);
        fillRects(g);
        g.translate(0, 20);
        drawRects(g);
        g.translate(0, 20);
        fillOvals(g);
        g.translate(0, 20);
        drawOvals(g);
    }

    private void renderTest(Graphics2D g, int w, int h) {
        g.setColor(Color.black);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.green);
        renderShapes(g);
        g.setTransform(AffineTransform.getTranslateInstance(SIZE / 2, 0));
        g.setXORMode(Color.black);
        renderShapes(g);
        g.setTransform(AffineTransform.getTranslateInstance(SIZE / 2, 0));
        renderShapes(g);
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        renderTest(g2d, SIZE, SIZE);
        Toolkit.getDefaultToolkit().sync();
        synchronized (this) {
            if (!done) {
                doCapture(this);
                done = true;
            }
            notifyAll();
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(SIZE, SIZE);
    }

    public static void main(String[] args) {
        boolean show = false;
        for (String arg : args) {
            if (arg.equals("-testvi")) {
                System.out.println("Testing VolatileImage, not screen");
                testVI = true;
            } else if (arg.equals("-show")) {
                show = true;
            }
        }
        SimplePrimQuality test = new SimplePrimQuality();
        Frame frame = new Frame();
        frame.add(test);
        frame.pack();
        frame.setVisible(true);
        synchronized (test) {
            while (!done) {
                try {
                    test.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException("Failed: Interrupted");
                }
            }
        }
        GraphicsConfiguration gc = frame.getGraphicsConfiguration();
        if (gc.getClass().getSimpleName().startsWith("Win")) {
            System.out.println("GDI pipeline detected: " + "test considered PASSED");
            frame.dispose();
            return;
        }
        if (testVI) {
            VolatileImage vi = frame.createVolatileImage(SIZE, SIZE);
            do {
                vi.validate(frame.getGraphicsConfiguration());
                Graphics2D g1 = vi.createGraphics();
                test.renderTest(g1, SIZE, SIZE);
                g1.dispose();
                capture = vi.getSnapshot();
            } while (vi.contentsLost());
            frame.dispose();
        }
        if (!show) {
            frame.dispose();
        }
        if (capture == null) {
            throw new RuntimeException("Error capturing the rendering");
        }
        int w = SIZE, h = SIZE;
        BufferedImage refimg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = refimg.createGraphics();
        test.renderTest(g, w, h);
        g.dispose();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int actual = capture.getRGB(x, y);
                int expected = refimg.getRGB(x, y);
                if (actual != expected) {
                    String expectedName = "SimplePrimQuality_expected.png";
                    String actualName = "SimplePrimQuality_actual.png";
                    try {
                        System.out.println("Writing expected image to: " + expectedName);
                        ImageIO.write(refimg, "png", new File(expectedName));
                        System.out.println("Writing actual image   to: " + actualName);
                        ImageIO.write(capture, "png", new File(actualName));
                    } catch (IOException ex) {
                    }
                    throw new RuntimeException("Test failed at x=" + x + " y=" + y + " (expected=" + Integer.toHexString(expected) + " actual=" + Integer.toHexString(actual) + ")");
                }
            }
        }
    }
}
