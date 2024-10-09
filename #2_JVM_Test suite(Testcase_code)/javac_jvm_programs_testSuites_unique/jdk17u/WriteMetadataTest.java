import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

public class WriteMetadataTest {

    private static String format = "GIF";

    public static void main(String[] args) {
        ImageWriter w = ImageIO.getImageWritersByFormatName(format).next();
        if (w == null) {
            throw new RuntimeException("No available writers for format " + format);
        }
        ImageWriteParam p = w.getDefaultWriteParam();
        ImageTypeSpecifier t = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
        IIOMetadata m = w.getDefaultImageMetadata(t, p);
        System.out.println("Default image metadata is " + m);
        testWritableMetadata(m);
        IIOMetadata sm = w.getDefaultStreamMetadata(p);
        System.out.println("Default stream metadata is " + sm);
        testWritableMetadata(sm);
    }

    public static void testWritableMetadata(IIOMetadata m) {
        String nativeFormatName = m.getNativeMetadataFormatName();
        System.out.println("Format: " + nativeFormatName);
        IIOMetadataNode root = (IIOMetadataNode) m.getAsTree(nativeFormatName);
        if (m.isReadOnly()) {
            throw new RuntimeException("Metadata is read only!");
        }
        try {
            m.setFromTree(nativeFormatName, root);
        } catch (IIOInvalidTreeException e) {
            e.printStackTrace();
            throw new RuntimeException("Test failed!", e);
        } catch (IllegalStateException e) {
            throw new RuntimeException("Test failed!", e);
        }
        System.out.println("Test passed.\n\n");
    }
}
