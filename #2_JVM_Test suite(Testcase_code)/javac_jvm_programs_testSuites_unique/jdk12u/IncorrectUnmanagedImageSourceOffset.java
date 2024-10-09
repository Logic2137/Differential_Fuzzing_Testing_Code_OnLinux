

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import static java.awt.Transparency.*;
import static java.awt.image.BufferedImage.*;


public final class IncorrectUnmanagedImageSourceOffset {

    private static final int[] TYPES = {TYPE_INT_RGB, TYPE_INT_ARGB,
                                        TYPE_INT_ARGB_PRE, TYPE_INT_BGR,
                                        TYPE_3BYTE_BGR, TYPE_4BYTE_ABGR,
                                        TYPE_4BYTE_ABGR_PRE,
                                         TYPE_BYTE_BINARY,
                                        TYPE_BYTE_INDEXED};
    private static final int[] TRANSPARENCIES = {OPAQUE, BITMASK, TRANSLUCENT};

    public static void main(final String[] args) throws IOException {
        for (final int viType : TRANSPARENCIES) {
            for (final int biType : TYPES) {
                BufferedImage bi = makeUnmanagedBI(biType);
                fill(bi);
                test(bi, viType);
            }
        }
    }

    private static void test(BufferedImage bi, int type)
            throws IOException {
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = ge.getDefaultScreenDevice()
                                     .getDefaultConfiguration();
        VolatileImage vi = gc.createCompatibleVolatileImage(511, 255, type);
        BufferedImage gold = gc.createCompatibleImage(511, 255, type);
        
        Graphics2D big = gold.createGraphics();
        
        big.drawImage(bi, 7, 11, 127, 111, 7, 11, 127 * 2, 111, null);
        big.dispose();
        
        BufferedImage snapshot;
        while (true) {
            vi.validate(gc);
            if (vi.validate(gc) != VolatileImage.IMAGE_OK) {
                try {
                    Thread.sleep(100);
                } catch (final InterruptedException ignored) {
                }
                continue;
            }
            Graphics2D vig = vi.createGraphics();
            
            vig.drawImage(bi, 7, 11, 127, 111, 7, 11, 127 * 2, 111, null);
            vig.dispose();
            snapshot = vi.getSnapshot();
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
                if (gold.getRGB(x, y) != snapshot.getRGB(x, y)) {
                    ImageIO.write(gold, "png", new File("gold.png"));
                    ImageIO.write(snapshot, "png", new File("bi.png"));
                    throw new RuntimeException("Test failed.");
                }
            }
        }
    }

    private static BufferedImage makeUnmanagedBI(final int type) {
        final BufferedImage bi = new BufferedImage(511, 255, type);
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
