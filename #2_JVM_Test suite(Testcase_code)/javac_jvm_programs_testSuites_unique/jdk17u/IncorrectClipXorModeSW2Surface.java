import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferShort;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import static java.awt.geom.Rectangle2D.Double;

public final class IncorrectClipXorModeSW2Surface {

    private static int[] SIZES = { 2, 10, 100 };

    private static final Shape[] SHAPES = { new Rectangle(0, 0, 0, 0), new Rectangle(0, 0, 1, 1), new Rectangle(0, 1, 1, 1), new Rectangle(1, 0, 1, 1), new Rectangle(1, 1, 1, 1), new Double(0, 0, 0.5, 0.5), new Double(0, 0.5, 0.5, 0.5), new Double(0.5, 0, 0.5, 0.5), new Double(0.5, 0.5, 0.5, 0.5), new Double(0.25, 0.25, 0.5, 0.5), new Double(0, 0.25, 1, 0.5), new Double(0.25, 0, 0.5, 1), new Double(.10, .10, .20, .20), new Double(.75, .75, .20, .20), new Double(.75, .10, .20, .20), new Double(.10, .75, .20, .20) };

    public static void main(final String[] args) throws IOException {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
        AffineTransform at;
        for (int size : SIZES) {
            at = AffineTransform.getScaleInstance(size, size);
            for (Shape clip : SHAPES) {
                clip = at.createTransformedShape(clip);
                for (Shape to : SHAPES) {
                    to = at.createTransformedShape(to);
                    BufferedImage snapshot;
                    BufferedImage bi = getBufferedImage(size);
                    VolatileImage vi = getVolatileImage(gc, size);
                    while (true) {
                        vi.validate(gc);
                        Graphics2D g2d = vi.createGraphics();
                        g2d.setColor(Color.GREEN);
                        g2d.fillRect(0, 0, size, size);
                        g2d.dispose();
                        if (vi.validate(gc) != VolatileImage.IMAGE_OK) {
                            continue;
                        }
                        draw(clip, to, bi, vi);
                        snapshot = vi.getSnapshot();
                        if (vi.contentsLost()) {
                            continue;
                        }
                        break;
                    }
                    BufferedImage goldvi = getCompatibleImage(gc, size);
                    BufferedImage goldbi = getBufferedImage(size);
                    draw(clip, to, goldbi, goldvi);
                    validate(snapshot, goldvi);
                    vi.flush();
                }
            }
        }
    }

    private static void draw(Shape clip, Shape shape, Image from, Image to) {
        Graphics2D g2d = (Graphics2D) to.getGraphics();
        g2d.setXORMode(Color.BLACK);
        g2d.setClip(clip);
        Rectangle toBounds = shape.getBounds();
        g2d.drawImage(from, toBounds.x, toBounds.y, toBounds.width, toBounds.height, null);
        g2d.dispose();
    }

    private static BufferedImage getBufferedImage(int sw) {
        final BufferedImage bi = new BufferedImage(sw, sw, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, sw, sw);
        g2d.dispose();
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

    private static VolatileImage getVolatileImage(GraphicsConfiguration gc, int size) {
        return gc.createCompatibleVolatileImage(size, size);
    }

    private static BufferedImage getCompatibleImage(GraphicsConfiguration gc, int size) {
        BufferedImage image = gc.createCompatibleImage(size, size);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.GREEN);
        g2d.fillRect(0, 0, size, size);
        g2d.dispose();
        return image;
    }

    private static void validate(BufferedImage bi, BufferedImage goldbi) throws IOException {
        for (int x = 0; x < bi.getWidth(); ++x) {
            for (int y = 0; y < bi.getHeight(); ++y) {
                if (goldbi.getRGB(x, y) != bi.getRGB(x, y)) {
                    ImageIO.write(bi, "png", new File("actual.png"));
                    ImageIO.write(goldbi, "png", new File("expected.png"));
                    throw new RuntimeException("Test failed.");
                }
            }
        }
    }
}
