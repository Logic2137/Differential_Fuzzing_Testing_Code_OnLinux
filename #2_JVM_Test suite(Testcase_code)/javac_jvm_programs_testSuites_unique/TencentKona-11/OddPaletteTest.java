



import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class OddPaletteTest {

    private static int w = 100;
    private static int h = 100;

    public static void main(String[] args) {
        BufferedImage[] srcs = new BufferedImage[2];
        srcs[0] = createTestImage(7); 
        srcs[1] = createTestImage(1); 

        for (int i = 0; i < srcs.length; i++) {
            doTest(srcs[i]);
        }
    }

    private static void doTest(BufferedImage src) {
        ImageWriter w = ImageIO.getImageWritersByFormatName("GIF").next();
        if (w == null) {
            throw new RuntimeException("No writer available!");
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
            w.setOutput(ios);
            w.write(src);
        } catch (IOException e) {
            throw new RuntimeException("Test failed.", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Test failed.", e);
        }
    }

    private static BufferedImage createTestImage(int paletteSize) {
        byte[] r = new byte[paletteSize];
        byte[] g = new byte[paletteSize];
        byte[] b = new byte[paletteSize];

        int shift = 256 / paletteSize;
        for (int i = 0; i < paletteSize; i++) {
            r[i] = g[i] = b[i] = (byte)(shift * i);
        }

        int numBits = getNumBits(paletteSize);

        System.out.println("num of bits " + numBits);

        IndexColorModel icm =
            new IndexColorModel(numBits, paletteSize,  r, g, b);

        BufferedImage img = new BufferedImage(w, h,
                                              BufferedImage.TYPE_BYTE_INDEXED,
                                              icm);
        Graphics2D  g2d = img.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, w, h);
        g2d.setColor(Color.black);
        g2d.drawLine(0, 0, w, h);
        g2d.drawLine(0, h, w, 0);

        return img;
    }

    private static int getNumBits(int paletteSize) {
        if (paletteSize < 0) {
            throw new IllegalArgumentException("negative palette size: " +
                                               paletteSize);
        }
        if (paletteSize < 2) {
            return 1;
        }
        int numBits = 0;

        paletteSize--;

        while (paletteSize > 0) {
            numBits++;
            paletteSize = paletteSize >> 1;
        }
        return numBits;
    }
}
