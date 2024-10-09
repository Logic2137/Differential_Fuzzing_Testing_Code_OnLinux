import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import javax.imageio.IIOImage;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.MemoryCacheImageInputStream;

public class ImageReaderReadAll {

    private final static byte[] ba = {};

    public static void main(String[] argv) {
        ImageReader ireader;
        ImageReadParam irp;
        IIOImage image;
        BufferedImage bi;
        BufferedImage bi_1;
        BufferedImage bi_2;
        ireader = new DummyImageReaderImpl(null);
        MemoryCacheImageInputStream mciis = new MemoryCacheImageInputStream(new ByteArrayInputStream(ba));
        ireader.setInput(mciis);
        irp = new ImageReadParam();
        irp.setDestination(new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR));
        try {
            image = ireader.readAll(0, irp);
            bi_1 = ireader.read(0, irp);
            bi_2 = ireader.read(0);
        } catch (java.io.IOException ee) {
            throw new RuntimeException("Unexpected exception: " + ee);
        }
        bi = (BufferedImage) image.getRenderedImage();
        if (bi.getType() != bi_1.getType()) {
            throw new RuntimeException("Images have different type!");
        }
    }

    public static class DummyImageReaderImpl extends ImageReader {

        public DummyImageReaderImpl(ImageReaderSpi originatingProvider) {
            super(originatingProvider);
        }

        public BufferedImage read(int imageIndex, ImageReadParam param) throws IOException {
            if (input == null)
                throw new IllegalStateException();
            if (imageIndex >= 1 || imageIndex < 0)
                throw new IndexOutOfBoundsException();
            if (seekForwardOnly) {
                if (imageIndex < minIndex)
                    throw new IndexOutOfBoundsException();
                minIndex = imageIndex;
            }
            return getDestination(param, getImageTypes(imageIndex), 10, 15);
        }

        public Iterator getImageTypes(int imageIndex) throws IOException {
            if (input == null)
                throw new IllegalStateException();
            if (imageIndex >= 1 || imageIndex < 0)
                throw new IndexOutOfBoundsException();
            Vector imageTypes = new Vector();
            imageTypes.add(ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_BYTE_GRAY));
            return imageTypes.iterator();
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

        public IIOMetadata getStreamMetadata() throws IOException {
            return null;
        }

        public IIOMetadata getImageMetadata(int imageIndex) throws IOException {
            return null;
        }
    }
}
