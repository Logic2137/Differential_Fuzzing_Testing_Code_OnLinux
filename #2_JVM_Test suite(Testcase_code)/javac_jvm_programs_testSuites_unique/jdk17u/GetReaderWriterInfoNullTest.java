import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import javax.imageio.IIOException;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;

class TestImageReaderSpi extends ImageReaderSpi {

    public TestImageReaderSpi(String[] FORMATNAMES, String[] SUFFIXES, String[] MIMETYPES) {
        super("J Duke", "1.0", FORMATNAMES, SUFFIXES, MIMETYPES, "readTest.TestImageReader", new Class<?>[] { ImageInputStream.class }, null, true, null, null, null, null, true, null, null, null, null);
    }

    @Override
    public boolean canDecodeInput(Object source) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getDescription(Locale locale) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ImageReader createReaderInstance(Object extension) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

class TestImageReader extends ImageReader {

    public TestImageReader(ImageReaderSpi originatingProvider) {
        super(originatingProvider);
    }

    @Override
    public int getNumImages(boolean allowSearch) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getWidth(int imageIndex) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getHeight(int imageIndex) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator<ImageTypeSpecifier> getImageTypes(int imageIndex) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IIOMetadata getStreamMetadata() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IIOMetadata getImageMetadata(int imageIndex) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BufferedImage read(int imageIndex, ImageReadParam param) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

public class GetReaderWriterInfoNullTest {

    static final String[] FORMATNAMES = { "readTest" };

    static final String[] SUFFIXES = { "readTest" };

    static final String[] MIMETYPES = { "readTest" };

    public static void main(String[] args) throws IIOException {
        TestImageReaderSpi mimeNullReadSpi = new TestImageReaderSpi(FORMATNAMES, SUFFIXES, null);
        IIORegistry.getDefaultInstance().registerServiceProvider(mimeNullReadSpi);
        ImageIO.getReaderMIMETypes();
        IIORegistry.getDefaultInstance().deregisterServiceProvider(mimeNullReadSpi);
        TestImageReaderSpi suffixNullReadSpi = new TestImageReaderSpi(FORMATNAMES, null, MIMETYPES);
        IIORegistry.getDefaultInstance().registerServiceProvider(suffixNullReadSpi);
        ImageIO.getReaderFileSuffixes();
        IIORegistry.getDefaultInstance().deregisterServiceProvider(suffixNullReadSpi);
    }
}
