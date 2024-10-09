import java.io.ByteArrayInputStream;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

public class GetNumImages {

    public static void main(String[] args) throws Exception {
        IIORegistry registry = IIORegistry.getDefaultInstance();
        Iterator readerspis = registry.getServiceProviders(ImageReaderSpi.class, false);
        while (readerspis.hasNext()) {
            boolean caughtEx = false;
            ImageReaderSpi readerspi = (ImageReaderSpi) readerspis.next();
            ImageReader reader = readerspi.createReaderInstance();
            try {
                reader.getNumImages(false);
            } catch (IllegalStateException ise) {
                caughtEx = true;
            }
            if (!caughtEx) {
                throw new RuntimeException("Test failed: exception was not " + "thrown for null input: " + reader);
            }
        }
        readerspis = registry.getServiceProviders(ImageReaderSpi.class, false);
        while (readerspis.hasNext()) {
            boolean caughtEx = false;
            ImageReaderSpi readerspi = (ImageReaderSpi) readerspis.next();
            ImageReader reader = readerspi.createReaderInstance();
            byte[] barr = new byte[100];
            ByteArrayInputStream bais = new ByteArrayInputStream(barr);
            ImageInputStream iis = ImageIO.createImageInputStream(bais);
            try {
                reader.setInput(iis, true);
                reader.getNumImages(true);
            } catch (IllegalStateException ise) {
                caughtEx = true;
            }
            if (!caughtEx) {
                throw new RuntimeException("Test failed: exception was not " + "thrown when allowSearch and " + "seekForwardOnly are both true: " + reader);
            }
        }
    }
}
