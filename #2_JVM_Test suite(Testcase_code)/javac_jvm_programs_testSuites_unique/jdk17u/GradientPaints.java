import java.awt.*;
import java.awt.MultipleGradientPaint.ColorSpaceType;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.File;
import java.util.Arrays;
import javax.imageio.ImageIO;

public class GradientPaints extends Canvas {

    private static final int TESTW = 600;

    private static final int TESTH = 500;

    private static final int TOLERANCE = 5;

    private static final int ALLOWED_MISMATCHES_LINEAR = (int) (TESTW * TESTH * 0.18);

    private static final int ALLOWED_MISMATCHES_RADIAL = (int) (TESTW * TESTH * 0.45);

    private static boolean done;

    private static boolean verbose;

    private static final Color[] COLORS = { new Color(0, 0, 0), new Color(128, 128, 128), new Color(255, 0, 0), new Color(255, 255, 0), new Color(0, 255, 0), new Color(0, 255, 255), new Color(128, 0, 255), new Color(128, 128, 128) };

    private static enum PaintType {

        BASIC, LINEAR, RADIAL
    }

    private static enum XformType {

        IDENTITY, TRANSLATE, SCALE, SHEAR, ROTATE
    }

    private static final int[] numStopsArray = { 2, 4, 7 };

    private static final Object[] hints = { RenderingHints.VALUE_ANTIALIAS_OFF, RenderingHints.VALUE_ANTIALIAS_ON };

    public void paint(Graphics g) {
        synchronized (this) {
            if (!done) {
                done = true;
                notifyAll();
            }
        }
    }

    private void testOne(BufferedImage refImg, VolatileImage testImg) {
        Graphics2D gref = refImg.createGraphics();
        Graphics2D gtest = testImg.createGraphics();
        Paint paint = makePaint(PaintType.RADIAL, CycleMethod.REPEAT, ColorSpaceType.SRGB, XformType.IDENTITY, 7);
        Object aahint = hints[0];
        renderTest(gref, paint, aahint);
        renderTest(gtest, paint, aahint);
        Toolkit.getDefaultToolkit().sync();
        compareImages(refImg, testImg.getSnapshot(), TOLERANCE, 0, "");
        gref.dispose();
        gtest.dispose();
    }

    private void testAll(Graphics gscreen, BufferedImage refImg, VolatileImage testImg) {
        Graphics2D gref = refImg.createGraphics();
        Graphics2D gtest = testImg.createGraphics();
        for (PaintType paintType : PaintType.values()) {
            for (CycleMethod cycleMethod : CycleMethod.values()) {
                for (ColorSpaceType colorSpace : ColorSpaceType.values()) {
                    for (XformType xform : XformType.values()) {
                        for (Object aahint : hints) {
                            for (int numStops : numStopsArray) {
                                Paint paint = makePaint(paintType, cycleMethod, colorSpace, xform, numStops);
                                String msg = "type=" + paintType + " cycleMethod=" + cycleMethod + " colorSpace=" + colorSpace + " xformType=" + xform + " numStops=" + numStops + " aa=" + aahint;
                                renderTest(gref, paint, aahint);
                                renderTest(gtest, paint, aahint);
                                gscreen.drawImage(testImg, 0, 0, null);
                                Toolkit.getDefaultToolkit().sync();
                                int allowedMismatches = paintType == PaintType.RADIAL ? ALLOWED_MISMATCHES_RADIAL : ALLOWED_MISMATCHES_LINEAR;
                                compareImages(refImg, testImg.getSnapshot(), TOLERANCE, allowedMismatches, msg);
                            }
                        }
                    }
                }
            }
        }
        gref.dispose();
        gtest.dispose();
    }

    private Paint makePaint(PaintType paintType, CycleMethod cycleMethod, ColorSpaceType colorSpace, XformType xformType, int numStops) {
        int startX = TESTW / 6;
        int startY = TESTH / 6;
        int endX = TESTW / 2;
        int endY = TESTH / 2;
        int ctrX = TESTW / 2;
        int ctrY = TESTH / 2;
        int focusX = ctrX + 20;
        int focusY = ctrY + 20;
        float radius = 100.0f;
        Paint paint;
        AffineTransform transform;
        Color[] colors = Arrays.copyOf(COLORS, numStops);
        float[] fractions = new float[colors.length];
        for (int i = 0; i < fractions.length; i++) {
            fractions[i] = ((float) i) / (fractions.length - 1);
        }
        switch(xformType) {
            default:
            case IDENTITY:
                transform = new AffineTransform();
                break;
            case TRANSLATE:
                transform = AffineTransform.getTranslateInstance(2, 2);
                break;
            case SCALE:
                transform = AffineTransform.getScaleInstance(1.2, 1.4);
                break;
            case SHEAR:
                transform = AffineTransform.getShearInstance(0.1, 0.1);
                break;
            case ROTATE:
                transform = AffineTransform.getRotateInstance(Math.PI / 4, getWidth() / 2, getHeight() / 2);
                break;
        }
        switch(paintType) {
            case BASIC:
                boolean cyclic = (cycleMethod != CycleMethod.NO_CYCLE);
                paint = new GradientPaint(startX, startY, Color.RED, endX, endY, Color.BLUE, cyclic);
                break;
            default:
            case LINEAR:
                paint = new LinearGradientPaint(new Point2D.Float(startX, startY), new Point2D.Float(endX, endY), fractions, colors, cycleMethod, colorSpace, transform);
                break;
            case RADIAL:
                paint = new RadialGradientPaint(new Point2D.Float(ctrX, ctrY), radius, new Point2D.Float(focusX, focusY), fractions, colors, cycleMethod, colorSpace, transform);
                break;
        }
        return paint;
    }

    private void renderTest(Graphics2D g2d, Paint p, Object aahint) {
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, TESTW, TESTH);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, aahint);
        g2d.setPaint(p);
        g2d.fillOval(0, 0, TESTW, TESTH);
    }

    public Dimension getPreferredSize() {
        return new Dimension(TESTW, TESTH);
    }

    private static void compareImages(BufferedImage refImg, BufferedImage testImg, int tolerance, int allowedMismatches, String msg) {
        int numMismatches = 0;
        int x1 = 0;
        int y1 = 0;
        int x2 = refImg.getWidth();
        int y2 = refImg.getHeight();
        for (int y = y1; y < y2; y++) {
            for (int x = x1; x < x2; x++) {
                Color expected = new Color(refImg.getRGB(x, y));
                Color actual = new Color(testImg.getRGB(x, y));
                if (!isSameColor(expected, actual, tolerance)) {
                    numMismatches++;
                }
            }
        }
        if (verbose) {
            System.out.println(msg);
        }
        if (numMismatches > allowedMismatches) {
            try {
                ImageIO.write(refImg, "png", new File("GradientPaints.ref.png"));
                ImageIO.write(testImg, "png", new File("GradientPaints.cap.png"));
            } catch (Exception e) {
            }
            if (!verbose) {
                System.err.println(msg);
            }
            throw new RuntimeException("Test failed: Number of mismatches (" + numMismatches + ") exceeds limit (" + allowedMismatches + ") with tolerance=" + tolerance);
        }
    }

    private static boolean isSameColor(Color c1, Color c2, int e) {
        int r1 = c1.getRed();
        int g1 = c1.getGreen();
        int b1 = c1.getBlue();
        int r2 = c2.getRed();
        int g2 = c2.getGreen();
        int b2 = c2.getBlue();
        int rmin = Math.max(r2 - e, 0);
        int gmin = Math.max(g2 - e, 0);
        int bmin = Math.max(b2 - e, 0);
        int rmax = Math.min(r2 + e, 255);
        int gmax = Math.min(g2 + e, 255);
        int bmax = Math.min(b2 + e, 255);
        if (r1 >= rmin && r1 <= rmax && g1 >= gmin && g1 <= gmax && b1 >= bmin && b1 <= bmax) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("-verbose")) {
            verbose = true;
        }
        GradientPaints test = new GradientPaints();
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
        if (gc.getColorModel() instanceof IndexColorModel) {
            System.out.println("IndexColorModel detected: " + "test considered PASSED");
            frame.dispose();
            return;
        }
        BufferedImage refImg = new BufferedImage(TESTW, TESTH, BufferedImage.TYPE_INT_RGB);
        VolatileImage testImg = frame.createVolatileImage(TESTW, TESTH);
        testImg.validate(gc);
        try {
            test.testAll(test.getGraphics(), refImg, testImg);
        } finally {
            frame.dispose();
        }
    }
}
