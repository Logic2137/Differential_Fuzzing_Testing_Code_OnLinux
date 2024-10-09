import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import javax.imageio.ImageReader;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ServiceRegistry;
import javax.imageio.stream.ImageInputStream;

public class RegisterPluginTwiceTest {

    public RegisterPluginTwiceTest() throws Exception {
        BMPImageReaderSPI BMPSpi = new BMPImageReaderSPI();
        BMPImageReaderSPI BMPSpi1 = new BMPImageReaderSPI();
        IIORegistry regis = IIORegistry.getDefaultInstance();
        boolean res1 = regis.registerServiceProvider(BMPSpi, javax.imageio.spi.ImageReaderSpi.class);
        boolean res2 = regis.registerServiceProvider(BMPSpi1, javax.imageio.spi.ImageReaderSpi.class);
        if (!res1 || res2) {
            throw new RuntimeException("Bad returned values for registerServiceProvider");
        }
        Iterator it = regis.getServiceProviders(Class.forName("javax.imageio.spi.ImageReaderSpi"), true);
        int count = 0;
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof BMPImageReaderSPI) {
                count++;
                System.out.println("Found next BMPImageReaderSPI, count = " + count);
            }
        }
        if (count > 1) {
            throw new RuntimeException("Too many instances of the BMPImageReaderSPI was registered!");
        }
    }

    public static void main(String[] args) throws Exception {
        RegisterPluginTwiceTest fnio = new RegisterPluginTwiceTest();
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
            super.onRegistration(sr, category);
        }

        public void onDeregistration(ServiceRegistry sr, Class<?> category) {
        }
    }
}
