



import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;

public class UshortOutOfMemoryTest {
    private int type;
    private ImageWriter w;

    public UshortOutOfMemoryTest(int type) {
        this.type = type;
        w = ImageIO.getImageWritersByFormatName("GIF").next();
    }

    public void testGetAsTree() {
        ImageWriteParam p = w.getDefaultWriteParam();
        IIOMetadata m =
            w.getDefaultImageMetadata(ImageTypeSpecifier.createFromBufferedImageType(type), p);

        String format = m.getNativeMetadataFormatName();
        System.out.println("native format: " + format);

        int count = 0;
        try {
            while (count < 100) {
                System.out.println(" test " + count++);
                m.getAsTree(format);
            }
        } catch (OutOfMemoryError e) {
            System.gc();
            throw new RuntimeException("Test failed. Number of performed operations: " + count, e);
        }
    }


    public static void main(String[] args) throws IOException {
        UshortOutOfMemoryTest t = new UshortOutOfMemoryTest(
                BufferedImage.TYPE_USHORT_GRAY);
        t.testGetAsTree();
    }
}
