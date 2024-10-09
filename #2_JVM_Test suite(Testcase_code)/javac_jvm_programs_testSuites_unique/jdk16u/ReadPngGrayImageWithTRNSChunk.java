



import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Color;
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

public class ReadPngGrayImageWithTRNSChunk {

    private static BufferedImage img;
    private static ImageWriter writer;
    private static ImageWriteParam param;
    private static IIOMetadata metadata;
    private static byte[] imageByteArray;

    private static void initialize(int type) {
        int width = 2;
        int height = 1;
        img = new BufferedImage(width, height, type);
        Graphics2D g2D = img.createGraphics();

        
        g2D.setColor(new Color(255, 255, 255));
        g2D.fillRect(0, 0, 1, 1);
        
        g2D.setColor(new Color(128, 128,128));
        g2D.fillRect(1, 0, 1, 1);
        g2D.dispose();

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

    private static boolean verifyAlphaValue(BufferedImage img) {
        Color firstPixel = new Color(img.getRGB(0, 0), true);
        Color secondPixel = new Color(img.getRGB(1, 0), true);

        return firstPixel.getAlpha() != 0 ||
        secondPixel.getAlpha() != 255;
    }

    private static boolean read8BitGrayPNGWithTRNSChunk() throws IOException {
        initialize(BufferedImage.TYPE_BYTE_GRAY);
        
        createTRNSNode("255");

        writeImage();

        InputStream input= new ByteArrayInputStream(imageByteArray);
        
        BufferedImage verify_img = ImageIO.read(input);
        input.close();
        
        return verifyAlphaValue(verify_img);
    }

    private static boolean read16BitGrayPNGWithTRNSChunk() throws IOException {
        initialize(BufferedImage.TYPE_USHORT_GRAY);
        
        createTRNSNode("65535");

        writeImage();

        InputStream input= new ByteArrayInputStream(imageByteArray);
        
        BufferedImage verify_img = ImageIO.read(input);
        input.close();
        
        return verifyAlphaValue(verify_img);
    }

    public static void main(String[] args) throws IOException {
        boolean read8BitFail, read16BitFail;
        
        read8BitFail = read8BitGrayPNGWithTRNSChunk();

        
        read16BitFail = read16BitGrayPNGWithTRNSChunk();

        if (read8BitFail || read16BitFail) {
            throw new RuntimeException("PNGImageReader is not using" +
            " transparent pixel information from tRNS chunk properly");
        }
    }
}

