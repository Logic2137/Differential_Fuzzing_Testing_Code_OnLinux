



import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class RLECompressionTest {
    public static final int TEST_RLE8 = 0x01;
    public static final int TEST_RLE4 = 0x02;

    private static Color[] usedColors = new Color[] {
        Color.black, Color.white, Color.red,
        Color.green, Color.blue, Color.yellow };

    int w = 100;
    
    
    
    
    
    
    
    int h = 2 * w;

    private IndexColorModel getTestColorModel(int type) {
        IndexColorModel icm = null;
        int bpp = 8;
        int size = 256;

        switch(type) {
            case TEST_RLE8:
                bpp = 8;
                size = 256;
                break;
            case TEST_RLE4:
                bpp = 4;
                size = 16;
                break;
            default:
                throw new IllegalArgumentException("Wrong test type: " + type);
        }

        byte[] palette = new byte[size * 3];
        for (int i = 0; i < usedColors.length; i++) {
            palette[3 * i] = (byte)(0xff & usedColors[i].getRed());
            palette[3 * i + 1] = (byte)(0xff & usedColors[i].getGreen());
            palette[3 * i + 2] = (byte)(0xff & usedColors[i].getBlue());
        }
        

        icm = new IndexColorModel(bpp, size, palette, 0, false);
        return icm;
    }

    private BufferedImage getTestImage(int type) {
        BufferedImage src = null;
        IndexColorModel icm = getTestColorModel(type);
        src = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_INDEXED, icm);
        Graphics2D g = src.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        g.dispose();

        return src;
    }

    public void doTest(int type) throws IOException {
        BufferedImage src = getTestImage(type);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);

        ImageWriter writer = ImageIO.getImageWritersByFormatName("BMP").next();
        writer.setOutput(ios);

        ImageWriteParam wparam = writer.getDefaultWriteParam();
        wparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        switch(type) {
            case TEST_RLE8:
                wparam.setCompressionType("BI_RLE8");
                break;
            case TEST_RLE4:
                wparam.setCompressionType("BI_RLE4");
                break;
            default:
                throw new IllegalArgumentException("Wrong test type: " + type);
        }

        writer.write(null, new IIOImage(src, null, null), wparam);

        ios.close();
        baos.close();

        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        BufferedImage dst = ImageIO.read(bais);

        checkResult(src, dst);
    }

    private void checkResult(BufferedImage src, BufferedImage dst) {
        int x = w / 2;
        for (int y = 0; y < h; y++) {
            int srcRgb = src.getRGB(x, y);
            int dstRgb = dst.getRGB(x, y);

            if (srcRgb != dstRgb) {
                throw new RuntimeException("Test failed due to color difference: " +
                        Integer.toHexString(dstRgb) + " instead of " + Integer.toHexString(srcRgb) +
                        " at [" + x + ", " + y + "]");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        RLECompressionTest test = new RLECompressionTest();
        test.doTest(TEST_RLE8);
        test.doTest(TEST_RLE4);
    }
}
