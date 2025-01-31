import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import sun.java2d.SunGraphics2D;

public final class Test8004859 {

    private static Shape[] clips = { new Rectangle(0, 0, -1, -1), new Rectangle(100, 100, -100, -100) };

    private static boolean status = true;

    public static void main(final String[] args) throws NoninvertibleTransformException {
        final BufferedImage bi = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = (Graphics2D) bi.getGraphics();
        test(g);
        g.translate(2.0, 2.0);
        test(g);
        g.translate(-4.0, -4.0);
        test(g);
        g.scale(2.0, 2.0);
        test(g);
        g.scale(-4.0, -4.0);
        test(g);
        g.rotate(Math.toRadians(90));
        test(g);
        g.rotate(Math.toRadians(90));
        test(g);
        g.rotate(Math.toRadians(90));
        test(g);
        g.rotate(Math.toRadians(90));
        test(g);
        g.dispose();
        if (!status) {
            throw new RuntimeException("Test failed");
        }
    }

    private static void test(final Graphics2D g) {
        for (final Shape clip : clips) {
            g.setClip(clip);
            if (!g.getClip().equals(clip)) {
                System.err.println("Expected clip: " + clip);
                System.err.println("Actual clip: " + g.getClip());
                System.err.println("bounds=" + g.getClip().getBounds2D());
                System.err.println("bounds=" + g.getClip().getBounds());
                status = false;
            }
            final Rectangle bounds = g.getClipBounds();
            if (!clip.equals(bounds)) {
                System.err.println("Expected getClipBounds(): " + clip);
                System.err.println("Actual getClipBounds(): " + bounds);
                status = false;
            }
            g.getClipBounds(bounds);
            if (!clip.equals(bounds)) {
                System.err.println("Expected getClipBounds(r): " + clip);
                System.err.println("Actual getClipBounds(r): " + bounds);
                status = false;
            }
            if (!clip.getBounds2D().isEmpty() && ((SunGraphics2D) g).clipRegion.isEmpty()) {
                System.err.println("clipRegion should not be empty");
                status = false;
            }
        }
    }
}
