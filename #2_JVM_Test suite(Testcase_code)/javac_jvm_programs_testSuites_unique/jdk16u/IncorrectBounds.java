


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;


public final class IncorrectBounds {

    private static final int width = 400;
    private static final int height = 400;

    public static void main(final String[] args) {
        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsConfiguration gc =
                ge.getDefaultScreenDevice().getDefaultConfiguration();
        VolatileImage vi = gc.createCompatibleVolatileImage(width / 4,
                                                            height / 4);
        final BufferedImage bi = new BufferedImage(width, height,
                                                   BufferedImage.TYPE_INT_ARGB);
        while (true) {
            vi.validate(gc);
            Graphics2D g2d = vi.createGraphics();
            g2d.setColor(Color.green);
            g2d.fillRect(0, 0, width / 4, height / 4);
            g2d.dispose();

            if (vi.validate(gc) != VolatileImage.IMAGE_OK) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
                continue;
            }

            Graphics2D g = bi.createGraphics();
            g.setComposite(AlphaComposite.Src);
            g.setColor(Color.red);
            g.fillRect(0, 0, width, height);
            
            g.drawImage(vi, 0, 0, width / 2, height / 2, 0, 0, width * 2,
                        height * 2, null);
            g.dispose();

            if (vi.contentsLost()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
                continue;
            }

            for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++y) {
                    if (x < width / 16 && y < height / 16) {
                        if (bi.getRGB(x, y) != Color.green.getRGB()) {
                            throw new RuntimeException("Test failed.");
                        }
                    } else {
                        if (bi.getRGB(x, y) != Color.red.getRGB()) {
                            throw new RuntimeException("Test failed.");
                        }
                    }
                }
            }
            break;
        }
    }
}
