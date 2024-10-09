

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

import static java.awt.Transparency.BITMASK;


public final class BitmaskVolatileImage {

    public static final int S = 8;

    public static void main(final String[] args) {
        GraphicsConfiguration gc =
                GraphicsEnvironment.getLocalGraphicsEnvironment()
                        .getDefaultScreenDevice().getDefaultConfiguration();
        VolatileImage vi = gc.createCompatibleVolatileImage(S, S, BITMASK);
        BufferedImage ci = gc.createCompatibleImage(S, S, BITMASK);

        int attempt = 0;
        do {
            if (++attempt > 10) {
                throw new RuntimeException("Too many attempts: " + attempt);
            }
            vi.validate(gc);
            test(vi, ci, gc);
        } while (vi.contentsLost());
    }

    private static void test(VolatileImage vi, BufferedImage ci, GraphicsConfiguration gc) {
        for (int r = 0; r <= 255; ++r) {
            for (int a = 0; a <= 255; ++a) {
                fill(vi, new Color(r, 0, 0, a));
                fill(ci, new Color(r, 0, 0, a));
                validate(ci, vi.getSnapshot());
            }
        }
    }

    private static void fill(Image image, Color color) {
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setColor(color);
        g2d.setComposite(AlphaComposite.Src);
        g2d.fillRect(0, 0, S, S);
        g2d.dispose();
    }

    private static void validate(BufferedImage ci, BufferedImage snapshot) {
        for (int y = 0; y < ci.getHeight(); y++) {
            for (int x = 0; x < ci.getWidth(); x++) {
                int ci_rgb = ci.getRGB(x, y);
                int vi_rgb = snapshot.getRGB(x, y);
                if (ci_rgb != vi_rgb) {
                    System.err.println("Exp:" + Integer.toHexString(ci_rgb));
                    System.err.println("Actual:" + Integer.toHexString(vi_rgb));
                    throw new RuntimeException("Colors mismatch!");
                }
            }
        }
    }
}
