




import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class GifTransparencyTest {

    BufferedImage src;
    BufferedImage dst;

    public GifTransparencyTest() {
        src = createTestImage();
    }

    public void doTest() {
        File pwd = new File(".");
        try {
            File f = File.createTempFile("transparency_test_", ".gif", pwd);
            System.out.println("file: " + f.getCanonicalPath());

            ImageWriter w = ImageIO.getImageWritersByFormatName("GIF").next();

            ImageWriterSpi spi = w.getOriginatingProvider();

            boolean succeed_write = ImageIO.write(src, "gif", f);

            if (!succeed_write) {
                throw new RuntimeException("Test failed: failed to write src.");
            }

            dst = ImageIO.read(f);

            checkResult(src, dst);

        } catch (IOException e) {
            throw new RuntimeException("Test failed.", e);
        }
    }

    
    protected void checkResult(BufferedImage src, BufferedImage dst) {
        int w = src.getWidth();
        int h = src.getHeight();


        if (dst.getWidth() != w || dst.getHeight() != h) {
            throw new RuntimeException("Test failed: wrong result dimension");
        }

        BufferedImage bg = new BufferedImage(2 * w, h, BufferedImage.TYPE_INT_RGB);
        Graphics g = bg.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, 2 * w, h);

        g.drawImage(src, 0, 0, null);
        g.drawImage(dst, w, 0, null);

        g.dispose();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int src_rgb = bg.getRGB(x, y);
                int dst_rgb = bg.getRGB(x + w, y);

                if (dst_rgb != src_rgb) {
                    throw new RuntimeException("Test failed: wrong color " +
                            Integer.toHexString(dst_rgb) + " at " + x + ", " +
                            y + " (instead of " + Integer.toHexString(src_rgb) +
                            ")");
                }
            }
        }
        System.out.println("Test passed.");
    }

    public void show() {
        JPanel p = new JPanel(new BorderLayout()) {
            public void paintComponent(Graphics g) {
                g.setColor(Color.blue);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        p.add(new ImageComponent(src), BorderLayout.WEST);
        if (dst != null) {
        p.add(new ImageComponent(dst), BorderLayout.EAST);
        }

        JFrame f = new JFrame("Transparency");
        f.add(p);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }

    public static class ImageComponent extends JComponent {
        BufferedImage img;

        public ImageComponent(BufferedImage img) {
            this.img = img;
        }

        public Dimension getPreferredSize() {
            return new Dimension(img.getWidth() + 2, img.getHeight() + 2);
        }

        public void paintComponent(Graphics g) {
            g.drawImage(img, 1, 1, this);
        }
    }

    protected BufferedImage createTestImage() {
        BufferedImage img = new BufferedImage(200, 200,
                                              BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.createGraphics();

        g.setColor(Color.red);
        g.fillRect(50, 50, 100, 100);
        g.dispose();

        return img;
    }

    public static class Empty extends GifTransparencyTest {
        protected BufferedImage createTestImage() {
            return new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        }
    }

    public static class Opaque extends GifTransparencyTest {
        protected BufferedImage createTestImage() {
            BufferedImage img = new BufferedImage(200, 200,
                                                  BufferedImage.TYPE_INT_ARGB);
            Graphics g = img.createGraphics();
            g.setColor(Color.cyan);
            g.fillRect(0, 0, 200, 200);

            g.setColor(Color.red);
            g.fillRect(50, 50, 100, 100);
            g.dispose();

            return img;
        }
    }

    public static void main(String[] args) {
        System.out.println("Test bitmask...");
        new GifTransparencyTest().doTest();

        System.out.println("Test opaque...");
        new GifTransparencyTest.Opaque().doTest();

        System.out.println("Test empty...");
        new GifTransparencyTest.Empty().doTest();
    }
}
