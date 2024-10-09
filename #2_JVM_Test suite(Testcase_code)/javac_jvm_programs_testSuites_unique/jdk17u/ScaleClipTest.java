import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ScaleClipTest {

    static final boolean SAVE_IMAGE = false;

    static final int SIZE = 50;

    enum SCALE_MODE {

        ORTHO, NON_ORTHO, COMPLEX
    }

    public static void main(String[] args) {
        System.setProperty("sun.java2d.renderer.verbose", "true");
        System.out.println("ScaleClipTest: size = " + SIZE);
        final BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        boolean fail = false;
        for (SCALE_MODE mode : SCALE_MODE.values()) {
            try {
                testNegativeScale(image, mode);
            } catch (IllegalStateException ise) {
                System.err.println("testNegativeScale[" + mode + "] failed:");
                ise.printStackTrace();
                fail = true;
            }
        }
        for (SCALE_MODE mode : SCALE_MODE.values()) {
            try {
                testMarginScale(image, mode);
            } catch (IllegalStateException ise) {
                System.err.println("testMarginScale[" + mode + "] failed:");
                ise.printStackTrace();
                fail = true;
            }
        }
        if (fail) {
            throw new RuntimeException("ScaleClipTest has failures.");
        }
    }

    private static void testNegativeScale(final BufferedImage image, final SCALE_MODE mode) {
        final Graphics2D g2d = (Graphics2D) image.getGraphics();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2d.setBackground(Color.WHITE);
            g2d.clearRect(0, 0, SIZE, SIZE);
            g2d.setColor(Color.BLACK);
            final double scale = -1.0;
            final AffineTransform at;
            switch(mode) {
                default:
                case ORTHO:
                    at = AffineTransform.getScaleInstance(scale, scale);
                    break;
                case NON_ORTHO:
                    at = AffineTransform.getScaleInstance(scale, scale + 1e-5);
                    break;
                case COMPLEX:
                    at = AffineTransform.getScaleInstance(scale, scale);
                    at.concatenate(AffineTransform.getShearInstance(1e-4, 1e-4));
                    break;
            }
            g2d.setTransform(at);
            g2d.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
            final Path2D p = new Path2D.Double();
            p.moveTo(scale * 10, scale * 10);
            p.lineTo(scale * (SIZE - 10), scale * (SIZE - 10));
            g2d.draw(p);
            if (SAVE_IMAGE) {
                try {
                    final File file = new File("ScaleClipTest-testNegativeScale-" + mode + ".png");
                    System.out.println("Writing file: " + file.getAbsolutePath());
                    ImageIO.write(image, "PNG", file);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            checkPixel(image.getData(), 25, 25, Color.BLACK.getRGB());
        } finally {
            g2d.dispose();
        }
    }

    private static void testMarginScale(final BufferedImage image, final SCALE_MODE mode) {
        final Graphics2D g2d = (Graphics2D) image.getGraphics();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2d.setBackground(Color.WHITE);
            g2d.clearRect(0, 0, SIZE, SIZE);
            g2d.setColor(Color.BLACK);
            final double scale = 1e-2;
            final AffineTransform at;
            switch(mode) {
                default:
                case ORTHO:
                    at = AffineTransform.getScaleInstance(scale, scale);
                    break;
                case NON_ORTHO:
                    at = AffineTransform.getScaleInstance(scale, scale + 1e-5);
                    break;
                case COMPLEX:
                    at = AffineTransform.getScaleInstance(scale, scale);
                    at.concatenate(AffineTransform.getShearInstance(1e-4, 1e-4));
                    break;
            }
            g2d.setTransform(at);
            final double invScale = 1.0 / scale;
            final float w = (float) (3.0 * invScale);
            g2d.setStroke(new BasicStroke(w, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
            final Path2D p = new Path2D.Double();
            p.moveTo(invScale * -0.5, invScale * 10);
            p.lineTo(invScale * -0.5, invScale * (SIZE - 10));
            g2d.draw(p);
            if (SAVE_IMAGE) {
                try {
                    final File file = new File("ScaleClipTest-testMarginScale-" + mode + ".png");
                    System.out.println("Writing file: " + file.getAbsolutePath());
                    ImageIO.write(image, "PNG", file);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            checkPixel(image.getData(), 0, 25, Color.BLACK.getRGB());
        } finally {
            g2d.dispose();
        }
    }

    private static void checkPixel(final Raster raster, final int x, final int y, final int expected) {
        final int[] rgb = (int[]) raster.getDataElements(x, y, null);
        if (rgb[0] != expected) {
            throw new IllegalStateException("bad pixel at (" + x + ", " + y + ") = " + rgb[0] + " expected: " + expected);
        }
    }
}
