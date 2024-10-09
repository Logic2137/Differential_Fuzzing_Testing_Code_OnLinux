



import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class RotatedFontMetricsTest {
    static final int FONT_SIZE = Integer.getInteger("font.size", 20);

    public static void main(String ... args) {
        Font font = new Font(Font.DIALOG, Font.PLAIN, FONT_SIZE);
        Graphics2D g2d = createGraphics();

        FontMetrics ref = null;
        RuntimeException failure = null;
        for (int a = 0; a < 360; a += 15) {
            Graphics2D g = (Graphics2D)g2d.create();
            g.rotate(Math.toRadians(a));
            FontMetrics m = g.getFontMetrics(font);
            g.dispose();

            boolean status = true;
            if (ref == null) {
                ref = m;
            } else {
                status = ref.getAscent() == m.getAscent() &&
                        ref.getDescent() == m.getDescent() &&
                        ref.getLeading() == m.getLeading() &&
                        ref.getMaxAdvance() == m.getMaxAdvance();
            }

            System.out.printf("Metrics a%d, d%d, l%d, m%d (%d) %s\n",
                    m.getAscent(), m.getDescent(), m.getLeading(), m.getMaxAdvance(),
                    (int)a, status ? "OK" : "FAIL");

            if (!status && failure == null) {
                failure = new RuntimeException("Font metrics differ for angle " + a);
            }
        }
        if (failure != null) {
            throw failure;
        }
        System.out.println("done");
    }

    private static Graphics2D createGraphics() {
        BufferedImage dst = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        return dst.createGraphics();
    }
}
