import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;
import org.w3c.dom.Node;

public class MergeTreeTest {

    public static void main(String[] args) throws IOException {
        ImageWriter iw = (ImageWriter) ImageIO.getImageWritersByFormatName("jpeg").next();
        ImageTypeSpecifier type = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
        ImageOutputStream ios = ImageIO.createImageOutputStream(new File("MergeTreeTest.jpeg"));
        iw.setOutput(ios);
        IIOMetadata meta = iw.getDefaultImageMetadata(type, null);
        boolean isFailed = false;
        String[] fmts = meta.getMetadataFormatNames();
        for (int i = 0; i < fmts.length; i++) {
            System.out.print("Format: " + fmts[i] + " ... ");
            Node root = meta.getAsTree(fmts[i]);
            try {
                meta.mergeTree(fmts[i], root);
            } catch (NullPointerException e) {
                throw new RuntimeException("Test failed for format " + fmts[i], e);
            }
            System.out.println("PASSED");
        }
    }
}
