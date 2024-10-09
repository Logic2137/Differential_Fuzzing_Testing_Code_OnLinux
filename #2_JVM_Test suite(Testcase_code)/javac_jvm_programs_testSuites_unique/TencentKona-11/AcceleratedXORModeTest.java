



import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class AcceleratedXORModeTest {
    public static void main(String argv[]) {
        String fileName = argv.length > 0 ? argv[0] : null;
        new AcceleratedXORModeTest(fileName).test();
    }

    static final Color backColor = Color.red;
    static final Color color1 = Color.green;
    static final Color color2 = Color.yellow;
    static final Color xorColor1 = Color.blue;
    static final Color xorColor2 = Color.white;

    static final int width = 700, height = 300;

    VolatileImage vImg = null;
    String fileName;

    public AcceleratedXORModeTest(String fileName) {
        this.fileName = fileName;
    }

    void draw(Graphics2D g) {
        g.setColor(backColor);
        g.fillRect(0, 0, width, height);
        g.setXORMode(xorColor1);
        drawPattern(g, 100);
        g.setXORMode(xorColor2);
        drawPattern(g, 400);
        g.dispose();
    }

    void test(BufferedImage bi) {
        comparePattern(bi, 150, xorColor1.getRGB());
        comparePattern(bi, 450, xorColor2.getRGB());
    }

    void comparePattern(BufferedImage bi, int startX, int xorColor) {
        int[] expectedColors = {
            backColor.getRGB() ^ color1.getRGB() ^ xorColor,
            backColor.getRGB() ^ color1.getRGB() ^ xorColor ^
                color2.getRGB() ^ xorColor,
            backColor.getRGB() ^ color2.getRGB() ^ xorColor
        };
        for (int i = 0; i < 3; i++) {
            int x = startX + 100 * i;
            int rgb = bi.getRGB(x, 150);
            if (rgb != expectedColors[i]) {
                String msg = "Colors mismatch: x = " + x +
                        ", got " + new Color(rgb) + ", expected " +
                        new Color(expectedColors[i]);
                System.err.println(msg);
                write(bi);
                throw new RuntimeException("FAILED: " + msg);
            }
        }
    }

    void drawPattern(Graphics2D g, int x) {
        g.setColor(color1);
        g.fillRect(x, 0, 200, 300);
        g.setColor(color2);
        g.fillRect(x+100, 0, 200, 300);
    }

    GraphicsConfiguration getDefaultGC() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice().getDefaultConfiguration();
    }

    void createVImg() {
        if (vImg != null) {
            vImg.flush();
            vImg = null;
        }
        vImg = getDefaultGC().createCompatibleVolatileImage(width, height);
    }

    void write(BufferedImage bi) {
        if (fileName != null) {
            try {
                ImageIO.write(bi, "png", new File(fileName));
            } catch (IOException e) {
                System.err.println("Can't write image file " + fileName);
            }
        }
    }

    void test() {
        createVImg();
        BufferedImage bi = null;
        do {
            int valCode = vImg.validate(getDefaultGC());
            if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
                createVImg();
            }
            Graphics2D g = vImg.createGraphics();
            draw(g);
            bi = vImg.getSnapshot();
        } while (vImg.contentsLost());
        if (bi != null) {
            test(bi);
            write(bi);
        }
    }
}
