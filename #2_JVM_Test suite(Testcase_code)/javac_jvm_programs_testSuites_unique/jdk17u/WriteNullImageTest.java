import java.io.FileOutputStream;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;

public class WriteNullImageTest {

    public WriteNullImageTest() {
        boolean testFailed = false;
        String failMsg = "FAIL: IllegalArgumentException is not thrown by the " + "ImageWriter when the image passed to the write() method is " + "null, for the image formats: ";
        try {
            IIORegistry reg = IIORegistry.getDefaultInstance();
            ImageWriter writer = null;
            Iterator writerSpiIter = reg.getServiceProviders(ImageWriterSpi.class, true);
            while (writerSpiIter.hasNext()) {
                ImageWriterSpi writerSpi = (ImageWriterSpi) writerSpiIter.next();
                writer = writerSpi.createWriterInstance();
                String[] names = writerSpi.getFormatNames();
                FileOutputStream fos = new FileOutputStream("temp");
                ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
                writer.setOutput(ios);
                try {
                    writer.write(null, null, null);
                } catch (IllegalArgumentException iae) {
                    System.out.println("PASS: Expected exception is thrown when null img is passed " + "to the write method, for the image format: " + names[0]);
                    System.out.println("\n");
                } catch (Exception e) {
                    testFailed = true;
                    failMsg = failMsg + names[0] + ", ";
                }
            }
        } catch (Exception e) {
            testFailed = true;
            throw new RuntimeException("Test Failed. Exception thrown: " + e.toString());
        }
        if (testFailed) {
            failMsg = failMsg.substring(0, failMsg.lastIndexOf(","));
            throw new RuntimeException(failMsg);
        }
    }

    public static void main(String[] args) {
        WriteNullImageTest test = new WriteNullImageTest();
    }
}
