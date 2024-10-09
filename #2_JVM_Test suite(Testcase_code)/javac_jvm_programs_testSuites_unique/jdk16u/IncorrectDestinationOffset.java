

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public final class IncorrectDestinationOffset {

    private static final int SIZE = 128;
    private static final double[] SCALES = {0.25, 0.5, 1, 1.5, 2.0, 4};

    public static void main(final String[] args) throws IOException {
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = ge.getDefaultScreenDevice()
                                     .getDefaultConfiguration();
        VolatileImage vi = gc.createCompatibleVolatileImage(SIZE, SIZE);
        BufferedImage bi = new BufferedImage(SIZE, SIZE,
                                             BufferedImage.TYPE_INT_ARGB);
        for (double scale : SCALES) {
            while (true) {
                
                vi.validate(gc);
                Graphics2D g2d = vi.createGraphics();
                g2d.setColor(Color.green);
                g2d.fillRect(0, 0, SIZE, SIZE);
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
                g.setColor(Color.RED);
                g.fillRect(0, 0, SIZE / 2, SIZE / 2);
                g.setColor(Color.BLUE);
                g.fillRect(SIZE / 2, 0, SIZE / 2, SIZE / 2);
                g.setColor(Color.ORANGE);
                g.fillRect(0, SIZE / 2, SIZE / 2, SIZE / 2);
                g.setColor(Color.MAGENTA);
                g.fillRect(SIZE / 2, SIZE / 2, SIZE / 2, SIZE / 2);

                int point2draw = (int) (100 * scale);
                int size2draw = (int) (SIZE * scale);
                g.drawImage(vi, point2draw, point2draw, size2draw, size2draw,
                            null);
                g.dispose();

                if (vi.contentsLost()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                    }
                    continue;
                }
                validate(bi, point2draw, size2draw);
                break;
            }
        }
    }

    private static void validate(BufferedImage bi, int point2draw,
                                 int size2draw)
            throws IOException {
        for (int x = 0; x < SIZE; ++x) {
            for (int y = 0; y < SIZE; ++y) {
                if (isInsideGreenArea(point2draw, size2draw, x, y)) {
                    if (bi.getRGB(x, y) != Color.green.getRGB()) {
                        ImageIO.write(bi, "png", new File("image.png"));
                        throw new RuntimeException("Test failed.");
                    }
                } else {
                    if (isRedArea(x, y)) {
                        if (bi.getRGB(x, y) != Color.red.getRGB()) {
                            ImageIO.write(bi, "png", new File("image.png"));
                            throw new RuntimeException("Test failed.");
                        }
                    }
                    if (isBlueArea(x, y)) {
                        if (bi.getRGB(x, y) != Color.blue.getRGB()) {
                            ImageIO.write(bi, "png", new File("image.png"));
                            throw new RuntimeException("Test failed.");
                        }
                    }
                    if (isOrangeArea(x, y)) {
                        if (bi.getRGB(x, y) != Color.orange.getRGB()) {
                            ImageIO.write(bi, "png", new File("image.png"));
                            throw new RuntimeException("Test failed.");
                        }
                    }
                    if (isMagentaArea(x, y)) {
                        if (bi.getRGB(x, y) != Color.magenta.getRGB()) {
                            ImageIO.write(bi, "png", new File("image.png"));
                            throw new RuntimeException("Test failed.");
                        }
                    }
                }
            }
        }
    }

    private static boolean isRedArea(int x, int y) {
        return x < SIZE / 2 && y < SIZE / 2;
    }

    private static boolean isBlueArea(int x, int y) {
        return x >= SIZE / 2 && y < SIZE / 2;
    }

    private static boolean isOrangeArea(int x, int y) {
        return x < SIZE / 2 && y >= SIZE / 2;
    }

    private static boolean isMagentaArea(int x, int y) {
        return x >= SIZE / 2 && y >= SIZE / 2;
    }

    private static boolean isInsideGreenArea(int point2draw, int size2draw,
                                             int x, int y) {
        return x >= point2draw && x < point2draw + size2draw && y >=
                point2draw && y < point2draw + size2draw;
    }
}
