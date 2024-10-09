



import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.image.IndexColorModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.IIOImage;

public class VerifyBitDepthScalingWithTRNSChunk {

    private static BufferedImage img;
    private static ImageWriter writer;
    private static ImageWriteParam param;
    private static IIOMetadata metadata;
    private static byte[] imageByteArray;

    private static void initialize(int type) {
        int width = 1;
        int height = 1;
        
        int size = 2;
        int bitDepth = 1;
        byte[] r = new byte[size];
        byte[] g = new byte[size];
        byte[] b = new byte[size];

        r[0] = g[0] = b[0] = 0;
        r[1] = g[1] = b[1] = (byte)255;

        IndexColorModel cm = new IndexColorModel(bitDepth, size, r, g, b);
        img = new BufferedImage(width, height, type, cm);
        Graphics2D g2D = img.createGraphics();
        g2D.setColor(new Color(255, 255, 255));
        g2D.fillRect(0, 0, width, height);

        Iterator<ImageWriter> iterWriter =
                ImageIO.getImageWritersBySuffix("png");
        writer = iterWriter.next();

        param = writer.getDefaultWriteParam();
        ImageTypeSpecifier specifier =
                ImageTypeSpecifier.
                        createFromBufferedImageType(type);
        metadata = writer.getDefaultImageMetadata(specifier, param);
    }

    private static void createTRNSNode(String tRNS_value)
            throws IIOInvalidTreeException {
        IIOMetadataNode tRNS_gray = new IIOMetadataNode("tRNS_Grayscale");
        tRNS_gray.setAttribute("gray", tRNS_value);

        IIOMetadataNode tRNS = new IIOMetadataNode("tRNS");
        tRNS.appendChild(tRNS_gray);
        IIOMetadataNode root = new IIOMetadataNode("javax_imageio_png_1.0");
        root.appendChild(tRNS);
        metadata.mergeTree("javax_imageio_png_1.0", root);
    }

    private static void writeImage() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        writer.setOutput(ios);
        writer.write(metadata, new IIOImage(img, null, metadata), param);
        writer.dispose();

        baos.flush();
        imageByteArray = baos.toByteArray();
        baos.close();
    }

    private static void verifyBitDepthScalingWithTRNSChunk()
        throws IOException {
        initialize(BufferedImage.TYPE_BYTE_BINARY);
        
        createTRNSNode("255");

        writeImage();

        InputStream input= new ByteArrayInputStream(imageByteArray);
        
        ImageIO.read(input);
        input.close();
    }

    public static void main(String[] args) throws IOException {
        verifyBitDepthScalingWithTRNSChunk();
    }
}

