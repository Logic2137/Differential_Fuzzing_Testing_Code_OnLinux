

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB_PRE;


public class StrokedLinePerf {

    public static void main(String[] unused) {
        BufferedImage bi = new BufferedImage(400, 400, TYPE_INT_ARGB_PRE);
        test(bi, true);
        test(bi, false);
    }

    private static void test(BufferedImage bi, boolean useAA) {
        final long start = System.nanoTime();

        final Graphics2D g2d = bi.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                (useAA) ? RenderingHints.VALUE_ANTIALIAS_ON
                        : RenderingHints.VALUE_ANTIALIAS_OFF);

        Stroke stroke = new BasicStroke(2.0f, 1, 0, 1.0f, new float[]{0.0f, 4.0f}, 0.0f);
        g2d.setStroke(stroke);

        
        g2d.draw(new Line2D.Double(4.0, 1.794369841E9, 567.0, -2.147483648E9));

        System.out.println("StrokedLinePerf(" + useAA + "): Test duration= " + (1e-6 * (System.nanoTime() - start)) + " ms.");
        g2d.dispose();
    }
}
