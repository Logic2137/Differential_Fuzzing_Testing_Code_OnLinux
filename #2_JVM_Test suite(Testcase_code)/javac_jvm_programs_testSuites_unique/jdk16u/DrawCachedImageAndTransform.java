

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;


public final class DrawCachedImageAndTransform {

    public static void main(String[] args) {
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = ge.getDefaultScreenDevice()
                                     .getDefaultConfiguration();
        VolatileImage vi = gc.createCompatibleVolatileImage(100, 100);

        Graphics2D g2d = vi.createGraphics();
        g2d.scale(2, 2);
        BufferedImage img = new BufferedImage(50, 50,
                                              BufferedImage.TYPE_INT_ARGB);

        g2d.drawImage(img, 10, 25, Color.blue, null);
        g2d.dispose();
    }
}
