

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;


public final class bug7181438 {

    private static final int SIZE = 500;

    public static void main(final String[] args) {

        final BufferedImage bi = createBufferedImage();
        final VolatileImage vi = createVolatileImage();
        final Graphics s2dVi = vi.getGraphics();

        
        s2dVi.drawImage(bi, 0, 0, null);

        final BufferedImage results = vi.getSnapshot();
        for (int i = 0; i < SIZE; ++i) {
            for (int j = 0; j < SIZE; ++j) {
                
                if (results.getRGB(i, j) != 0xFF000000) {
                    throw new RuntimeException("Failed: Wrong alpha");
                }
            }
        }
        System.out.println("Passed");
    }


    private static VolatileImage createVolatileImage() {
        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
        return gc.createCompatibleVolatileImage(SIZE, SIZE,
                                                Transparency.TRANSLUCENT);
    }

    private static BufferedImage createBufferedImage() {
        final BufferedImage bi = new BufferedImage(SIZE, SIZE,
                                                   BufferedImage.TYPE_INT_RGB);
        final Graphics bg = bi.getGraphics();
        
        bg.setColor(new Color(0, 0, 0, 0));
        bg.fillRect(0, 0, SIZE, SIZE);
        bg.dispose();
        return bi;
    }
}
