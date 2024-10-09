import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageReaderSpi;

public class PluginSpiTest {

    public static void main(String[] args) {
        String[] format = { "GIF", "PNG", "JPEG", "BMP", "WBMP" };
        for (int i = 0; i < format.length; i++) {
            System.out.println("\nFormat " + format[i]);
            testFormat(format[i]);
        }
    }

    public static void testFormat(String format) {
        ImageReader reader = ImageIO.getImageReadersByFormatName(format).next();
        if (reader == null) {
            throw new RuntimeException("Failed to get reader for " + format);
        }
        ImageReaderSpi readerSpi = reader.getOriginatingProvider();
        System.out.println(format + " Reader SPI: " + readerSpi);
        String[] writerSpiNames = readerSpi.getImageWriterSpiNames();
        if (writerSpiNames == null || writerSpiNames.length == 0) {
            throw new RuntimeException("Failed to get writer spi names for " + format);
        }
        System.out.println("Available writer spi names:");
        for (int i = 0; i < writerSpiNames.length; i++) {
            System.out.println(writerSpiNames[i]);
            try {
                Class spiClass = Class.forName(writerSpiNames[i]);
                if (spiClass == null) {
                    throw new RuntimeException("Failed to get spi class " + writerSpiNames[i]);
                }
                System.out.println("Got class " + spiClass.getName());
                Object spiObject = spiClass.newInstance();
                if (spiObject == null) {
                    throw new RuntimeException("Failed to instantiate spi " + writerSpiNames[i]);
                }
                System.out.println("Got instance " + spiObject);
            } catch (Throwable e) {
                throw new RuntimeException("Failed to test spi " + writerSpiNames[i]);
            }
        }
        ImageWriter writer = ImageIO.getImageWriter(reader);
        if (writer == null) {
            throw new RuntimeException("Failed to get writer for " + format);
        }
    }
}
