import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class WritingColorChangeTest {

    private static int width = 100;

    private static int height = 100;

    private static Color color = new Color(0x10, 0x20, 0x30);

    static int[] bufferedImageType = { BufferedImage.TYPE_USHORT_565_RGB, BufferedImage.TYPE_INT_BGR, BufferedImage.TYPE_INT_RGB, BufferedImage.TYPE_USHORT_555_RGB };

    static String[] bufferedImageStringType = { "BufferedImage.TYPE_USHORT_565_RGB", "BufferedImage.TYPE_INT_BGR", "BufferedImage.TYPE_INT_RGB", "BufferedImage.TYPE_USHORT_555_RGB" };

    private static String writingFormat = "BMP";

    private static ImageWriter writer = (ImageWriter) ImageIO.getImageWritersByFormatName(writingFormat).next();

    private int type;

    public static void main(String[] args) {
        for (int i = 0; i < bufferedImageType.length; i++) {
            System.out.println("\n\nTest for type " + bufferedImageStringType[i]);
            WritingColorChangeTest t1 = new WritingColorChangeTest(bufferedImageType[i]);
            t1.doTest();
        }
    }

    private WritingColorChangeTest(int type) {
        this.type = type;
    }

    private void doTest() {
        BufferedImage src = createTestImage(type);
        System.out.println("Sample model is " + src.getSampleModel());
        BufferedImage dst = doModification(src);
        Object dstPixel = dst.getRaster().getDataElements(width / 2, height / 2, null);
        Object srcPixel = src.getRaster().getDataElements(width / 2, height / 2, null);
        if (src.getType() == BufferedImage.TYPE_USHORT_555_RGB || src.getType() == BufferedImage.TYPE_USHORT_565_RGB) {
            Color cmpColor = new Color(dst.getColorModel().getRed(dstPixel), dst.getColorModel().getGreen(dstPixel), dst.getColorModel().getBlue(dstPixel));
            BufferedImage cmp = createTestImage(src.getType(), cmpColor);
            Object cmpPixel = cmp.getRaster().getDataElements(width / 2, height / 2, null);
            dst = cmp;
            dstPixel = cmpPixel;
        }
        if ((src.getColorModel().getRed(srcPixel) != dst.getColorModel().getRed(dstPixel)) || (src.getColorModel().getGreen(srcPixel) != dst.getColorModel().getGreen(dstPixel)) || (src.getColorModel().getBlue(srcPixel) != dst.getColorModel().getBlue(dstPixel)) || (src.getColorModel().getAlpha(srcPixel) != dst.getColorModel().getAlpha(dstPixel))) {
            showPixel(src, width / 2, height / 2);
            showPixel(dst, width / 2, height / 2);
            showRes(dst, src);
            throw new RuntimeException("Colors are different: " + Integer.toHexString(src.getColorModel().getRGB(srcPixel)) + " and " + Integer.toHexString(dst.getColorModel().getRGB(dstPixel)));
        }
    }

    private BufferedImage doModification(BufferedImage src) {
        try {
            BufferedImage dst = null;
            if (!writer.getOriginatingProvider().canEncodeImage(src)) {
                throw new RuntimeException(writingFormat + " writer does not support the image type " + type);
            }
            System.out.println(writingFormat + " writer claims it can encode the image " + type);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
            writer.setOutput(ios);
            writer.write(src);
            ios.close();
            baos.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            dst = ImageIO.read(bais);
            return dst;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void showPixel(BufferedImage src, int x, int y) {
        System.out.println("Img is " + src);
        Object p = src.getRaster().getDataElements(x, y, null);
        System.out.println("RGB:   " + Integer.toHexString(src.getColorModel().getRGB(p)));
        System.out.println("Red:   " + Integer.toHexString(src.getColorModel().getRed(p)));
        System.out.println("Green: " + Integer.toHexString(src.getColorModel().getGreen(p)));
        System.out.println("Blue:  " + Integer.toHexString(src.getColorModel().getBlue(p)));
        System.out.println("Alpha: " + Integer.toHexString(src.getColorModel().getAlpha(p)));
    }

    private static BufferedImage createTestImage(int type) {
        return createTestImage(type, color);
    }

    private static BufferedImage createTestImage(int type, Color c) {
        BufferedImage i = new BufferedImage(width, height, type);
        Graphics2D g = i.createGraphics();
        g.setColor(c);
        g.fillRect(0, 0, width, height);
        return i;
    }

    private static void showRes(final BufferedImage src, final BufferedImage dst) {
        final int w = src.getWidth() + dst.getWidth();
        final int h = Math.max(src.getHeight(), dst.getHeight());
        JFrame f = new JFrame("Test results");
        f.getContentPane().add(new JComponent() {

            public Dimension getPreferredSize() {
                return new Dimension(w, h);
            }

            public void paintComponent(Graphics g) {
                g.drawImage(src, 0, 0, null);
                g.drawImage(dst, src.getWidth(), 0, null);
            }
        });
        f.pack();
        f.setVisible(true);
    }
}
