import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import javax.imageio.IIOException;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;

public class ImageReaderGetDestination {

    public static void main(String[] argv) {
        Vector imageTypes = new Vector();
        boolean gotIAE1 = false;
        boolean gotIAE2 = false;
        boolean gotIAE3 = false;
        boolean gotIAE4 = false;
        try {
            DummyImageReaderImpl.getDestination(null, null, 5, 10);
        } catch (IllegalArgumentException iae) {
            gotIAE1 = true;
        } catch (Throwable ee) {
            System.out.println("Unexpected exception 1:");
            ee.printStackTrace();
        }
        if (!gotIAE1) {
            throw new RuntimeException("Failed to get IAE #1!");
        }
        try {
            DummyImageReaderImpl.getDestination(null, imageTypes.iterator(), 5, 10);
        } catch (IllegalArgumentException iae) {
            gotIAE2 = true;
        } catch (Throwable ee) {
            System.out.println("Unexpected exception 2:");
            ee.printStackTrace();
        }
        if (!gotIAE2) {
            throw new RuntimeException("Failed to get IAE #2!");
        }
        imageTypes.add("abc");
        try {
            DummyImageReaderImpl.getDestination(null, imageTypes.iterator(), 5, 10);
        } catch (IllegalArgumentException iae) {
            gotIAE3 = true;
        } catch (Throwable ee) {
            System.out.println("Unexpected exception 3:");
            ee.printStackTrace();
        }
        if (!gotIAE3) {
            throw new RuntimeException("Failed to get IAE #3!");
        }
        imageTypes.clear();
        ImageTypeSpecifier its = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
        imageTypes.add(its);
        try {
            DummyImageReaderImpl.getDestination(null, imageTypes.iterator(), Integer.MAX_VALUE, Integer.MAX_VALUE);
        } catch (IllegalArgumentException iae) {
            gotIAE4 = true;
        } catch (Throwable ee) {
            System.out.println("Unexpected exception 4: ");
            ee.printStackTrace();
        }
        if (!gotIAE4) {
            throw new RuntimeException("Failed to get IAE #4!");
        }
    }

    public static class DummyImageReaderImpl extends ImageReader {

        public DummyImageReaderImpl(ImageReaderSpi originatingProvider) {
            super(originatingProvider);
        }

        public static BufferedImage getDestination(ImageReadParam param, Iterator imageTypes, int width, int height) throws IIOException {
            return ImageReader.getDestination(param, imageTypes, width, height);
        }

        public int getNumImages(boolean allowSearch) throws IOException {
            return 1;
        }

        public int getWidth(int imageIndex) throws IOException {
            return 1;
        }

        public int getHeight(int imageIndex) throws IOException {
            return 1;
        }

        public Iterator getImageTypes(int imageIndex) throws IOException {
            return null;
        }

        public IIOMetadata getStreamMetadata() throws IOException {
            return null;
        }

        public IIOMetadata getImageMetadata(int imageIndex) throws IOException {
            return null;
        }

        public BufferedImage read(int imageIndex, ImageReadParam param) throws IOException {
            return null;
        }
    }
}
