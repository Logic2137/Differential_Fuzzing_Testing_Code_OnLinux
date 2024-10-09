import java.io.IOException;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ServiceRegistry;
import javax.imageio.stream.ImageInputStream;

public class DeregisterAllSpiTest {

    public DeregisterAllSpiTest() throws Exception {
        ImageReaderSpi BMPSpi = new BMPImageReaderSPI();
        IIORegistry.getDefaultInstance().registerServiceProvider(BMPSpi);
        System.out.println("Reader Format Names available in the registry");
        String[] formatNames = ImageIO.getReaderFormatNames();
        if (formatNames == null || formatNames.length <= 0) {
            throw new RuntimeException("No registered ImageReaders!");
        }
        for (int x = 0; x < formatNames.length; x++) {
            System.out.println("format " + formatNames[x]);
        }
        IIORegistry.getDefaultInstance().deregisterAll();
        System.out.println("\nReader Format Names after deregistering all SPIs");
        formatNames = ImageIO.getReaderFormatNames();
        if (formatNames.length == 0) {
            System.out.println("No readers available\n");
        } else {
            throw new RuntimeException("Some providers was not deregistered!");
        }
        IIORegistry.getDefaultInstance().registerServiceProvider(BMPSpi);
        System.out.println("Reader Format Names after re-register of BMP Plugin");
        formatNames = ImageIO.getReaderFormatNames();
        if (formatNames.length == 0) {
            throw new RuntimeException("Unable to register new SPI after deregisterAll()!");
        }
    }

    public static void main(String[] args) throws Exception {
        DeregisterAllSpiTest regis = new DeregisterAllSpiTest();
    }

    public static class BMPImageReaderSPI extends javax.imageio.spi.ImageReaderSpi {

        private static final String vendorName = "Javasoft";

        private static final String version = "2.0";

        private static final String[] names = { "bmp" };

        private static final String[] suffixes = { "bmp" };

        private static final String[] MIMETypes = { "image/x-bmp" };

        private static final String readerClassName = "com.sun.imageio.plugins.png.PNGImageReader";

        private static final String[] writerSpiNames = { "com.sun.imageio.plugins.png.PNGImageWriterSpi" };

        public BMPImageReaderSPI() {
            super(vendorName, version, names, suffixes, MIMETypes, readerClassName, STANDARD_INPUT_TYPE, writerSpiNames, false, null, null, null, null, true, "BMP Native Metadata", "com.sun.imageio.plugins.png.PNGMetadataFormat", null, null);
        }

        public String getDescription(Locale locale) {
            return "Standard BMP image reader";
        }

        public boolean canDecodeInput(Object input) throws IOException {
            if (!(input instanceof ImageInputStream)) {
                return false;
            }
            ImageInputStream stream = (ImageInputStream) input;
            byte[] b = new byte[8];
            stream.mark();
            stream.readFully(b);
            stream.reset();
            return (b[0] == (byte) 137 && b[1] == (byte) 80 && b[2] == (byte) 78 && b[3] == (byte) 71 && b[4] == (byte) 13 && b[5] == (byte) 10 && b[6] == (byte) 26 && b[7] == (byte) 10);
        }

        public ImageReader createReaderInstance(Object extension) {
            return null;
        }

        public void onRegistration(ServiceRegistry sr, Class<?> category) {
            System.out.println("\nfrom OnRegistration: BMP plugin Registered\n");
            super.onRegistration(sr, category);
        }

        public void onDeregistration(ServiceRegistry sr, Class<?> category) {
            System.out.println("\nfrom OnDeregistration: BMP plugin De-Registered\n");
        }
    }
}
