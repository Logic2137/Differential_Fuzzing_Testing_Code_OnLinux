



import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;

public class BmpBigDestinationTest {
    static String format = "BMP";
    public static void main(String[] args) {
        try {
            BufferedImage src = new BufferedImage(100, 100,
                                                  BufferedImage.TYPE_INT_RGB);
            Graphics2D g = src.createGraphics();
            g.setColor(Color.red);
            g.fillRect(0,0,100, 100);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageWriter iw =
                (ImageWriter)ImageIO.getImageWritersByFormatName(format).next();
            if (iw == null) {
                throw new RuntimeException("No writer available. Test failed.");
            }

            iw.setOutput(ImageIO.createImageOutputStream(baos));
            iw.write(src);

            byte[] data = baos.toByteArray();

            ImageReader ir =
                (ImageReader)ImageIO.getImageReadersByFormatName(format).next();
            ir.setInput(
                ImageIO.createImageInputStream(
                    new ByteArrayInputStream(data)));

            Iterator specifiers = ir.getImageTypes(0);
            ImageTypeSpecifier typeSpecifier = null;

            if (specifiers.hasNext()) {
                typeSpecifier = (ImageTypeSpecifier) specifiers.next();
            }
            ImageReadParam param = new ImageReadParam();
            BufferedImage dst = typeSpecifier.createBufferedImage(200, 200);
            param.setDestination(dst);

            ir.read(0, param);

            checkResults(src,dst);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected exception. Test failed.");
        }
    }

    private static void checkResults(BufferedImage src, BufferedImage dst) {
        for(int x=0; x<src.getWidth(); x++) {
            for(int y=0; y<src.getHeight(); y++) {
                int srcRgb = src.getRGB(x,y);
                int dstRgb = dst.getRGB(x,y);
                if (srcRgb != dstRgb) {
                    throw new RuntimeException("Images are different at point ["
                                               + x + "," + y + "]");
                }
            }
        }
    }
}
