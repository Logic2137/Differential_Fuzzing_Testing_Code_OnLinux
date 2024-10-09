import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Math.toRadians;

public final class DrawRotatedStringUsingRotatedFont {

    private static final int SIZE = 500;

    private static final String STR = "MMMMMMMMMMMMMMMM";

    private static AffineTransform[] txs = { AffineTransform.getRotateInstance(toRadians(00)), AffineTransform.getRotateInstance(toRadians(45)), AffineTransform.getRotateInstance(toRadians(-45)), AffineTransform.getRotateInstance(toRadians(90)), AffineTransform.getRotateInstance(toRadians(-90)), AffineTransform.getRotateInstance(toRadians(135)), AffineTransform.getRotateInstance(toRadians(-135)), AffineTransform.getRotateInstance(toRadians(180)), AffineTransform.getRotateInstance(toRadians(-180)), AffineTransform.getRotateInstance(toRadians(225)), AffineTransform.getRotateInstance(toRadians(-225)), AffineTransform.getRotateInstance(toRadians(270)), AffineTransform.getRotateInstance(toRadians(-270)), AffineTransform.getRotateInstance(toRadians(315)), AffineTransform.getRotateInstance(toRadians(-315)), AffineTransform.getRotateInstance(toRadians(360)), AffineTransform.getRotateInstance(toRadians(-360)) };

    public static void main(final String[] args) throws IOException {
        for (final AffineTransform tx2 : txs) {
            for (final AffineTransform tx1 : txs) {
                for (final boolean aa : new boolean[] { true, false }) {
                    final BufferedImage bi1 = createImage(aa, tx1, tx2);
                    final BufferedImage bi2 = createImage(aa, tx2, tx1);
                    compareImage(bi1, bi2);
                    fillTextArea(bi1, tx1, tx2);
                    fillTextArea(bi2, tx2, tx1);
                    checkColors(bi1, bi2);
                }
            }
        }
        System.out.println("Passed");
    }

    private static void compareImage(final BufferedImage bi1, final BufferedImage bi2) throws IOException {
        for (int i = 0; i < SIZE; ++i) {
            for (int j = 0; j < SIZE; ++j) {
                if (bi1.getRGB(i, j) != bi2.getRGB(i, j)) {
                    ImageIO.write(bi1, "png", new File("image1.png"));
                    ImageIO.write(bi2, "png", new File("image2.png"));
                    throw new RuntimeException("Failed: wrong text location");
                }
            }
        }
    }

    private static void checkColors(final BufferedImage bi1, final BufferedImage bi2) throws IOException {
        for (int i = 0; i < SIZE; ++i) {
            for (int j = 0; j < SIZE; ++j) {
                final int rgb1 = bi1.getRGB(i, j);
                final int rgb2 = bi2.getRGB(i, j);
                if (rgb1 != rgb2 || rgb1 != 0xFFFF0000 && rgb1 != 0xFF00FF00) {
                    ImageIO.write(bi1, "png", new File("image1.png"));
                    ImageIO.write(bi2, "png", new File("image2.png"));
                    throw new RuntimeException("Failed: wrong text location");
                }
            }
        }
    }

    private static BufferedImage createImage(final boolean aa, final AffineTransform gtx, final AffineTransform ftx) {
        final BufferedImage bi = new BufferedImage(SIZE, SIZE, TYPE_INT_RGB);
        final Graphics2D bg = bi.createGraphics();
        bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, aa ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        bg.setColor(Color.RED);
        bg.fillRect(0, 0, SIZE, SIZE);
        bg.translate(100, 100);
        bg.transform(gtx);
        bg.setColor(Color.BLACK);
        bg.setFont(bg.getFont().deriveFont(20.0f).deriveFont(ftx));
        bg.drawString(STR, 0, 0);
        bg.dispose();
        return bi;
    }

    private static void fillTextArea(final BufferedImage bi, final AffineTransform tx1, final AffineTransform tx2) {
        final Graphics2D bg = bi.createGraphics();
        bg.translate(100, 100);
        bg.transform(tx1);
        bg.transform(tx2);
        bg.setColor(Color.GREEN);
        final Font font = bg.getFont().deriveFont(20.0f);
        bg.setFont(font);
        bg.fill(font.getStringBounds(STR, bg.getFontRenderContext()));
        bg.dispose();
    }
}
