import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.VolatileImage;
import static java.awt.Transparency.*;
import static java.awt.image.BufferedImage.*;

public final class UnmanagedDrawImagePerformance {

    private static final int[] TYPES = { TYPE_INT_RGB, TYPE_INT_ARGB, TYPE_INT_ARGB_PRE, TYPE_INT_BGR, TYPE_3BYTE_BGR, TYPE_4BYTE_ABGR, TYPE_4BYTE_ABGR_PRE, TYPE_USHORT_565_RGB, TYPE_USHORT_555_RGB, TYPE_BYTE_GRAY, TYPE_USHORT_GRAY, TYPE_BYTE_BINARY, TYPE_BYTE_INDEXED };

    private static final int[] TRANSPARENCIES = { OPAQUE, BITMASK, TRANSLUCENT };

    private static final int SIZE = 1000;

    private static final AffineTransform[] TRANSFORMS = { AffineTransform.getScaleInstance(.5, .5), AffineTransform.getScaleInstance(1, 1), AffineTransform.getScaleInstance(2, 2), AffineTransform.getShearInstance(7, 11) };

    public static void main(final String[] args) {
        for (final AffineTransform atfm : TRANSFORMS) {
            for (final int viType : TRANSPARENCIES) {
                for (final int biType : TYPES) {
                    final BufferedImage bi = makeUnmanagedBI(biType);
                    final VolatileImage vi = makeVI(viType);
                    final long time = test(bi, vi, atfm) / 1000000000;
                    if (time > 1) {
                        throw new RuntimeException(String.format("drawImage is slow: %d seconds", time));
                    }
                }
            }
        }
    }

    private static long test(Image bi, Image vi, AffineTransform atfm) {
        final Polygon p = new Polygon();
        p.addPoint(0, 0);
        p.addPoint(SIZE, 0);
        p.addPoint(0, SIZE);
        p.addPoint(SIZE, SIZE);
        p.addPoint(0, 0);
        Graphics2D g2d = (Graphics2D) vi.getGraphics();
        g2d.clip(p);
        g2d.transform(atfm);
        g2d.setComposite(AlphaComposite.SrcOver);
        final long start = System.nanoTime();
        g2d.drawImage(bi, 0, 0, null);
        final long time = System.nanoTime() - start;
        g2d.dispose();
        return time;
    }

    private static VolatileImage makeVI(final int type) {
        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice gd = ge.getDefaultScreenDevice();
        final GraphicsConfiguration gc = gd.getDefaultConfiguration();
        return gc.createCompatibleVolatileImage(SIZE, SIZE, type);
    }

    private static BufferedImage makeUnmanagedBI(final int type) {
        final BufferedImage img = new BufferedImage(SIZE, SIZE, type);
        final DataBuffer db = img.getRaster().getDataBuffer();
        if (db instanceof DataBufferInt) {
            ((DataBufferInt) db).getData();
        } else if (db instanceof DataBufferShort) {
            ((DataBufferShort) db).getData();
        } else if (db instanceof DataBufferByte) {
            ((DataBufferByte) db).getData();
        } else {
            try {
                img.setAccelerationPriority(0.0f);
            } catch (final Throwable ignored) {
            }
        }
        return img;
    }
}
