

import java.io.IOException;
import java.util.Locale;

import javax.imageio.IIOException;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ServiceRegistry;

public class DummyReaderPluginSpi extends ImageReaderSpi {

    private static String [] writerSpiNames =
        {"DummyWriterPluginSpi"};
    public static String[] formatNames = {"test_5076692", "TEST_5076692"};
    public static String[] entensions = {"test_5076692"};
    public static String[] mimeType = {"image/test_5076692"};

    private boolean registered = false;

    public DummyReaderPluginSpi() {
        super("Sun Microsystems, Inc.",
              "1.0",
              formatNames,
              entensions,
              mimeType,
              "DummyPluginReader",
              STANDARD_INPUT_TYPE,
              writerSpiNames,
              false,
              null, null, null, null,
              false,
              "",
              "",
              null, null);
    }

    public void onRegistration(ServiceRegistry registry,
                               Class<?> category) {
        if (registered) {
            return;
        }

        System.getProperty("test.5076692.property", "not found");

        registered = true;
    }

    public String getDescription(Locale locale) {
        return "Standard Dummy Image Reader";
    }

    public boolean canDecodeInput(Object source) throws IOException {
        return false;
    }

    public ImageReader createReaderInstance(Object extension)
        throws IIOException {
        return null;
    }
}
