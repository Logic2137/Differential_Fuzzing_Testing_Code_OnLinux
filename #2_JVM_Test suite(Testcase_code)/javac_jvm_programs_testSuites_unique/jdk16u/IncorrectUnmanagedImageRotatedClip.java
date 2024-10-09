

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import static java.awt.Transparency.TRANSLUCENT;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;


public final class IncorrectUnmanagedImageRotatedClip {

    public static void main(final String[] args) throws IOException {
        BufferedImage bi = makeUnmanagedBI();
        fill(bi);
        test(bi);
    }

    private static void test(final BufferedImage bi) throws IOException {
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = ge.getDefaultScreenDevice()
                                     .getDefaultConfiguration();
        VolatileImage vi = gc.createCompatibleVolatileImage(500, 200,
                                                            TRANSLUCENT);
        BufferedImage gold = gc.createCompatibleImage(500, 200, TRANSLUCENT);
        
        draw(bi, gold);
        
        int attempt = 0;
        BufferedImage snapshot;
        while (true) {
            if (++attempt > 10) {
                throw new RuntimeException("Too many attempts: " + attempt);
            }
            vi.validate(gc);
            if (vi.validate(gc) != VolatileImage.IMAGE_OK) {
                continue;
            }
            draw(bi, vi);
            snapshot = vi.getSnapshot();
            if (vi.contentsLost()) {
                continue;
            }
            break;
        }
        
        for (int x = 0; x < gold.getWidth(); ++x) {
            for (int y = 0; y < gold.getHeight(); ++y) {
                if (gold.getRGB(x, y) != snapshot.getRGB(x, y)) {
                    ImageIO.write(gold, "png", new File("gold.png"));
                    ImageIO.write(snapshot, "png", new File("bi.png"));
                    throw new RuntimeException("Test failed.");
                }
            }
        }
    }

    private static void draw(final BufferedImage from,final Image to) {
        final Graphics2D g2d = (Graphics2D) to.getGraphics();
        g2d.setComposite(AlphaComposite.Src);
        g2d.setColor(Color.ORANGE);
        g2d.fillRect(0, 0, to.getWidth(null), to.getHeight(null));
        g2d.rotate(Math.toRadians(45));
        g2d.clip(new Rectangle(41, 42, 43, 44));
        g2d.drawImage(from, 50, 50, Color.blue, null);
        g2d.dispose();
    }

    private static BufferedImage makeUnmanagedBI() {
        final BufferedImage bi = new BufferedImage(500, 200, TYPE_INT_ARGB);
        final DataBuffer db = bi.getRaster().getDataBuffer();
        if (db instanceof DataBufferInt) {
            ((DataBufferInt) db).getData();
        } else if (db instanceof DataBufferShort) {
            ((DataBufferShort) db).getData();
        } else if (db instanceof DataBufferByte) {
            ((DataBufferByte) db).getData();
        } else {
            try {
                bi.setAccelerationPriority(0.0f);
            } catch (final Throwable ignored) {
            }
        }
        return bi;
    }

    private static void fill(final Image image) {
        final Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setComposite(AlphaComposite.Src);
        for (int i = 0; i < image.getHeight(null); ++i) {
            graphics.setColor(new Color(i, 0, 0));
            graphics.fillRect(0, i, image.getWidth(null), 1);
        }
        graphics.dispose();
    }
}
