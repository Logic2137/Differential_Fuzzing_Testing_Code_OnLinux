

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public final class IncorrectSourceOffset {

    public static void main(final String[] args) throws IOException {
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = ge.getDefaultScreenDevice()
                                     .getDefaultConfiguration();
        VolatileImage vi = gc.createCompatibleVolatileImage(511, 255);
        BufferedImage bi = new BufferedImage(511, 255,
                                             BufferedImage.TYPE_INT_ARGB);
        BufferedImage gold = new BufferedImage(511, 255,
                                               BufferedImage.TYPE_INT_ARGB);
        fill(gold);
        while (true) {
            vi.validate(gc);
            fill(vi);
            if (vi.validate(gc) != VolatileImage.IMAGE_OK) {
                try {
                    Thread.sleep(100);
                } catch (final InterruptedException ignored) {
                }
                continue;
            }

            Graphics2D big = bi.createGraphics();
            big.drawImage(vi, 7, 11, 127, 111, 7, 11, 127, 111, null);
            big.dispose();
            if (vi.contentsLost()) {
                try {
                    Thread.sleep(100);
                } catch (final InterruptedException ignored) {
                }
                continue;
            }
            break;
        }

        for (int x = 7; x < 127; ++x) {
            for (int y = 11; y < 111; ++y) {
                if (gold.getRGB(x, y) != bi.getRGB(x, y)) {
                    ImageIO.write(gold, "png", new File("gold.png"));
                    ImageIO.write(bi, "png", new File("bi.png"));
                    throw new RuntimeException("Test failed.");
                }
            }
        }
    }

    private static void fill(Image image) {
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setComposite(AlphaComposite.Src);
        for (int i = 0; i < image.getHeight(null); ++i) {
            graphics.setColor(new Color(i, 0, 0));
            graphics.fillRect(0, i, image.getWidth(null), 1);
        }
        graphics.dispose();
    }
}
