import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;

public class WriteInvalidKeywordTest {

    private static BufferedImage img;

    private static ImageWriter writer;

    private static ImageWriteParam param;

    private static IIOMetadata metadata;

    private static void initialize(int type) {
        int width = 1;
        int height = 1;
        img = new BufferedImage(width, height, type);
        Graphics2D g2D = img.createGraphics();
        g2D.setColor(new Color(255, 255, 255));
        g2D.fillRect(0, 0, width, width);
        g2D.dispose();
        Iterator<ImageWriter> iterWriter = ImageIO.getImageWritersBySuffix("png");
        writer = iterWriter.next();
        param = writer.getDefaultWriteParam();
        ImageTypeSpecifier specifier = ImageTypeSpecifier.createFromBufferedImageType(type);
        metadata = writer.getDefaultImageMetadata(specifier, param);
    }

    private static void createTEXTNode() throws IIOInvalidTreeException {
        IIOMetadataNode tEXt_Entry = new IIOMetadataNode("tEXtEntry");
        tEXt_Entry.setAttribute("keyword", "Authored" + "AuthoredAuthoredAuthoredAuthoredAuthoredAuthored" + "AuthoredAuthoredAuthoredAuthored");
        tEXt_Entry.setAttribute("value", "");
        IIOMetadataNode tEXt = new IIOMetadataNode("tEXt");
        tEXt.appendChild(tEXt_Entry);
        IIOMetadataNode root = new IIOMetadataNode("javax_imageio_png_1.0");
        root.appendChild(tEXt);
        metadata.mergeTree("javax_imageio_png_1.0", root);
    }

    private static void writeImage() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        writer.setOutput(ios);
        writer.write(metadata, new IIOImage(img, null, metadata), param);
        writer.dispose();
        baos.close();
        ios.close();
    }

    private static void writePNGTEXTChunk() throws IOException {
        initialize(BufferedImage.TYPE_BYTE_GRAY);
        createTEXTNode();
        writeImage();
    }

    public static void main(String[] args) throws IOException {
        boolean failed = true;
        try {
            writePNGTEXTChunk();
        } catch (IIOException e) {
            if (e.getCause().getMessage() == "tEXt keyword is longer than 79") {
                failed = false;
            }
        }
        if (failed) {
            throw new RuntimeException("Test failed, did not throw " + "expected exception");
        }
    }
}
