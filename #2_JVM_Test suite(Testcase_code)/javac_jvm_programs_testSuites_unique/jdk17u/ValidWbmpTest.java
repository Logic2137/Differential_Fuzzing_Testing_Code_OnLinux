import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ValidWbmpTest {

    public static void main(String[] args) {
        try {
            String[] formats = { "JPEG", "PNG", "BMP" };
            BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_GRAY);
            Graphics g = img.createGraphics();
            g.setColor(Color.white);
            g.fillRect(0, 0, 100, 100);
            g.setColor(Color.black);
            g.fillRect(10, 10, 80, 80);
            ImageReader ir = (ImageReader) ImageIO.getImageReadersByFormatName("WBMP").next();
            if (ir == null) {
                throw new RuntimeException("No readers for WBMP format!");
            }
            for (int i = 0; i < formats.length; i++) {
                System.out.println("Test " + formats[i] + " stream...");
                boolean passed = false;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(img, formats[i], baos);
                baos.close();
                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                ImageInputStream iis = null;
                iis = ImageIO.createImageInputStream(bais);
                ir.setInput(iis);
                try {
                    BufferedImage res = ir.read(0);
                } catch (IIOException e) {
                    StackTraceElement[] stack = e.getStackTrace();
                    if (ir.getClass().getName().equals(stack[0].getClassName()) && "readHeader".equals(stack[0].getMethodName())) {
                        passed = true;
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                if (!passed) {
                    throw new RuntimeException("Test failed!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected exception. Test failed.");
        }
    }
}
