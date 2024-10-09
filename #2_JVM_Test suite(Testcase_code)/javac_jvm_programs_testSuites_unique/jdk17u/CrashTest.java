import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import static java.awt.geom.Path2D.WIND_NON_ZERO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class CrashTest {

    static final boolean SAVE_IMAGE = false;

    static boolean USE_ROUND_CAPS_AND_JOINS = true;

    public static void main(String[] args) {
        boolean runSlowTests = (args.length != 0 && "-slow".equals(args[0]));
        System.setProperty("sun.java2d.renderer.verbose", "true");
        if (runSlowTests) {
            testHugeImage((Integer.MAX_VALUE >> 4) - 100, 16);
        }
        testHugeImage(8388608 + 1, 10);
        if (runSlowTests) {
            test(0.1f, false, 0);
            test(0.1f, true, 7f);
        }
        try {
            USE_ROUND_CAPS_AND_JOINS = true;
            test(0.1f, true, 0.1f);
            System.out.println("Exception MISSING.");
        } catch (Throwable th) {
            if (th instanceof ArrayIndexOutOfBoundsException) {
                System.out.println("ArrayIndexOutOfBoundsException expected.");
            } else {
                throw new RuntimeException("Unexpected exception", th);
            }
        }
    }

    private static void test(final float lineStroke, final boolean useDashes, final float dashMinLen) throws ArrayIndexOutOfBoundsException {
        System.out.println("---\n" + "test: " + "lineStroke=" + lineStroke + ", useDashes=" + useDashes + ", dashMinLen=" + dashMinLen);
        final BasicStroke stroke = createStroke(lineStroke, useDashes, dashMinLen);
        final int size = 9000;
        System.out.println("image size = " + size);
        final BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2d = (Graphics2D) image.getGraphics();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setClip(0, 0, size, size);
            g2d.setBackground(Color.WHITE);
            g2d.clearRect(0, 0, size, size);
            g2d.setStroke(stroke);
            g2d.setColor(Color.BLACK);
            final long start = System.nanoTime();
            paint(g2d, size - 10f);
            final long time = System.nanoTime() - start;
            System.out.println("paint: duration= " + (1e-6 * time) + " ms.");
            if (SAVE_IMAGE) {
                try {
                    final File file = new File("CrashTest-dash-" + useDashes + ".bmp");
                    System.out.println("Writing file: " + file.getAbsolutePath());
                    ImageIO.write(image, "BMP", file);
                } catch (IOException ex) {
                    System.out.println("Writing file failure:");
                    ex.printStackTrace();
                }
            }
        } finally {
            g2d.dispose();
        }
    }

    private static void testHugeImage(final int width, final int height) throws ArrayIndexOutOfBoundsException {
        System.out.println("---\n" + "testHugeImage: " + "width=" + width + ", height=" + height);
        final BasicStroke stroke = createStroke(2.5f, false, 0);
        System.out.println("image size = " + width + " x " + height);
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        final Graphics2D g2d = (Graphics2D) image.getGraphics();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setBackground(Color.WHITE);
            g2d.clearRect(0, 0, width, height);
            g2d.setStroke(stroke);
            g2d.setColor(Color.BLACK);
            final Path2D.Float path = new Path2D.Float(WIND_NON_ZERO, 32);
            path.moveTo(0, 0);
            path.lineTo(width, 0);
            path.lineTo(width, height);
            path.lineTo(0, height);
            path.lineTo(0, 0);
            final long start = System.nanoTime();
            g2d.draw(path);
            final long time = System.nanoTime() - start;
            System.out.println("paint: duration= " + (1e-6 * time) + " ms.");
            if (SAVE_IMAGE) {
                try {
                    final File file = new File("CrashTest-huge-" + width + "x" + height + ".bmp");
                    System.out.println("Writing file: " + file.getAbsolutePath());
                    ImageIO.write(image, "BMP", file);
                } catch (IOException ex) {
                    System.out.println("Writing file failure:");
                    ex.printStackTrace();
                }
            }
        } finally {
            g2d.dispose();
        }
    }

    private static void paint(final Graphics2D g2d, final float size) {
        final double halfSize = size / 2.0;
        final Path2D.Float path = new Path2D.Float(WIND_NON_ZERO, 32 * 1024);
        path.moveTo(0, 0);
        path.lineTo(size, size);
        path.moveTo(size, 0);
        path.lineTo(0, size);
        path.moveTo(0, 0);
        path.lineTo(size, 0);
        path.moveTo(0, 0);
        path.lineTo(0, size);
        path.moveTo(0, 0);
        double r = size;
        final int ratio = 100;
        int repeats = 1;
        int n = 0;
        while (r > 1.0) {
            repeats *= ratio;
            if (repeats > 10000) {
                repeats = 10000;
            }
            for (int i = 0; i < repeats; i++) {
                path.lineTo(halfSize - 0.5 * r + i * r / repeats, halfSize - 0.5 * r);
                n++;
                path.lineTo(halfSize - 0.5 * r + i * r / repeats + 0.1, halfSize + 0.5 * r);
                n++;
            }
            r -= halfSize;
        }
        System.out.println("draw : " + n + " lines.");
        g2d.draw(path);
    }

    private static BasicStroke createStroke(final float width, final boolean useDashes, final float dashMinLen) {
        final float[] dashes;
        if (useDashes) {
            dashes = new float[512];
            float cur = dashMinLen;
            float step = 0.01f;
            for (int i = 0; i < dashes.length; i += 2) {
                dashes[i] = cur;
                dashes[i + 1] = cur;
                cur += step;
            }
        } else {
            dashes = null;
        }
        if (USE_ROUND_CAPS_AND_JOINS) {
            return new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 100.0f, dashes, 0.0f);
        }
        return new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 100.0f, dashes, 0.0f);
    }
}
