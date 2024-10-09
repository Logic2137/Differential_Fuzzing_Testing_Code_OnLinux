

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public final class ScaledCopyArea {

    public static void main(final String[] args) {
        final BufferedImage bi = new BufferedImage(100, 300,
                                                   BufferedImage.TYPE_INT_RGB);
        final Graphics2D g = bi.createGraphics();
        g.scale(2, 2);
        g.setColor(Color.RED);
        g.fillRect(0, 0, 100, 300);
        g.setColor(Color.GREEN);
        g.fillRect(0, 100, 100, 100);
        g.copyArea(0, 100, 100, 100, 0, -100);
        g.dispose();
        for (int x = 0; x < 100; ++x) {
            for (int y = 0; y < 100; ++y) {
                final int actual = bi.getRGB(x, y);
                final int exp = Color.GREEN.getRGB();
                if (actual != exp) {
                    System.err.println("Expected:" + Integer.toHexString(exp));
                    System.err.println("Actual:" + Integer.toHexString(actual));
                    throw new RuntimeException("Test " + "failed");
                }
            }
        }
    }
}
