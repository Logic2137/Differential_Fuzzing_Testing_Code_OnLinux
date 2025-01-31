import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import static java.awt.RenderingHints.KEY_STROKE_CONTROL;
import static java.awt.RenderingHints.VALUE_STROKE_PURE;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public final class IncorrectFractionalClip {

    private static final int SIZE = 128;

    public static final Color RED = new Color(255, 0, 0, 100);

    public static final Color GREEN = new Color(0, 255, 0, 100);

    public static final Color WHITE = new Color(0, 0, 0, 0);

    public static final BasicStroke STROKE = new BasicStroke(2.01f);

    private static final double[] SCALES = { 0.1, 0.25, 0.4, 0.5, 0.6, 1, 1.4, 1.5, 1.6, 2.0, 2.4, 2.5, 2.6, 4 };

    static BufferedImage bi;

    static BufferedImage gold;

    static BufferedImage redI;

    static BufferedImage greenI;

    public static void main(final String[] args) throws Exception {
        bi = new BufferedImage(SIZE, SIZE, TYPE_INT_ARGB);
        gold = new BufferedImage(SIZE, SIZE, TYPE_INT_ARGB);
        redI = createImage(RED);
        greenI = createImage(GREEN);
        System.out.println("Will test fillRect");
        test(0, true);
        test(0, false);
        System.out.println("Will test DrawImage");
        test(1, true);
        test(1, false);
        System.out.println("Will test drawLine");
        test(2, true);
        test(2, false);
    }

    private static void test(final int testId, final boolean horiz) throws Exception {
        for (final double scale : SCALES) {
            drawToImage(testId, horiz, scale, bi, false);
            drawToImage(testId, horiz, scale, gold, true);
            validate(bi, gold, testId);
        }
    }

    private static void drawToImage(int testId, boolean horiz, double scale, BufferedImage image, boolean shape) {
        Graphics2D g = image.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setColor(WHITE);
        g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        g.setComposite(AlphaComposite.SrcOver);
        g.setRenderingHint(KEY_STROKE_CONTROL, VALUE_STROKE_PURE);
        if (horiz) {
            g.scale(scale, 1);
        } else {
            g.scale(1, scale);
        }
        final int destSize = (int) Math.ceil(SIZE / scale);
        final int destW;
        final int destH;
        if (horiz) {
            destW = destSize;
            destH = SIZE;
        } else {
            destW = SIZE;
            destH = destSize;
        }
        for (int step = 0; step < destSize; ++step) {
            if (horiz) {
                if (!shape) {
                    g.setClip(step, 0, 1, SIZE);
                } else {
                    g.setClip(new Area(new Rectangle(step, 0, 1, SIZE)));
                }
            } else {
                if (!shape) {
                    g.setClip(0, step, SIZE, 1);
                } else {
                    g.setClip(new Area(new Rectangle(0, step, SIZE, 1)));
                }
            }
            switch(testId) {
                case 0:
                    g.setColor(step % 2 == 0 ? RED : GREEN);
                    g.fillRect(0, 0, destW, destH);
                    break;
                case 1:
                    g.drawImage(step % 2 == 0 ? redI : greenI, 0, 0, destW, destH, null);
                    break;
                case 2:
                    g.setColor(step % 2 == 0 ? RED : GREEN);
                    g.setStroke(STROKE);
                    if (horiz) {
                        g.drawLine(step, 0, step, SIZE);
                    } else {
                        g.drawLine(0, step, SIZE, step);
                    }
                    break;
                default:
                    throw new RuntimeException();
            }
        }
        g.dispose();
    }

    private static void validate(final BufferedImage bi, BufferedImage gold, final int testID) throws Exception {
        for (int x = 0; x < SIZE; ++x) {
            for (int y = 0; y < SIZE; ++y) {
                int rgb = bi.getRGB(x, y);
                int goldRGB = gold.getRGB(x, y);
                if ((rgb != GREEN.getRGB() && rgb != RED.getRGB()) || rgb != goldRGB) {
                    ImageIO.write(bi, "png", new File("image.png"));
                    ImageIO.write(gold, "png", new File("gold.png"));
                    throw new RuntimeException("Test failed.");
                }
            }
        }
    }

    private static BufferedImage createImage(final Color color) {
        BufferedImage bi = new BufferedImage(SIZE, SIZE, TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setColor(color);
        g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        g.dispose();
        return bi;
    }
}
