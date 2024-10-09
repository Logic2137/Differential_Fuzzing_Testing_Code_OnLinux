



import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageWriterSpi;

import com.sun.imageio.plugins.png.PNGImageReaderSpi;
import com.sun.imageio.plugins.png.PNGImageWriterSpi;

public class PNGSpiStreamMetadata {

    private static void fatal() {
        throw new RuntimeException("Got a non-null stream metadata format!");
    }

    public static void main(String[] args) {
        ImageReaderSpi rspi = new PNGImageReaderSpi();
        if (rspi.getNativeStreamMetadataFormatName() != null) {
            fatal();
        }
        if (rspi.isStandardStreamMetadataFormatSupported() != false) {
            fatal();
        }
        if (rspi.getExtraStreamMetadataFormatNames() != null) {
            fatal();
        }

        ImageWriterSpi wspi = new PNGImageWriterSpi();
        if (wspi.getNativeStreamMetadataFormatName() != null) {
            fatal();
        }
        if (wspi.isStandardStreamMetadataFormatSupported() != false) {
            fatal();
        }
        if (wspi.getExtraStreamMetadataFormatNames() != null) {
            fatal();
        }
    }
}
