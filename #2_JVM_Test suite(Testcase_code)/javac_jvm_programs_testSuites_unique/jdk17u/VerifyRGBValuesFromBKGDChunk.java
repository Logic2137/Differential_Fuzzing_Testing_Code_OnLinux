import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.ImageReader;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.IIOImage;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class VerifyRGBValuesFromBKGDChunk {

    private static IIOMetadata encodeMetadata;

    private static ImageWriter writer;

    private static ImageReader reader;

    private static BufferedImage img;

    private static ImageWriteParam param;

    private static boolean nativeBKGDFail, standardBKGDFail;

    private static IIOMetadataNode bKGD_RGBNode;

    private static final String BKGDRED = "100";

    private static final String BKGDGREEN = "150";

    private static final String BKGDBLUE = "200";

    private static void mergeStandardMetadata() throws IIOInvalidTreeException {
        IIOMetadataNode background_rgb = new IIOMetadataNode("BackgroundColor");
        background_rgb.setAttribute("red", BKGDRED);
        background_rgb.setAttribute("green", BKGDGREEN);
        background_rgb.setAttribute("blue", BKGDBLUE);
        IIOMetadataNode chroma = new IIOMetadataNode("Chroma");
        chroma.appendChild(background_rgb);
        IIOMetadataNode encodeRoot = new IIOMetadataNode("javax_imageio_1.0");
        encodeRoot.appendChild(chroma);
        ImageTypeSpecifier specifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_BGR);
        encodeMetadata = writer.getDefaultImageMetadata(specifier, param);
        encodeMetadata.mergeTree("javax_imageio_1.0", encodeRoot);
    }

    private static void mergeNativeMetadata() throws IIOInvalidTreeException {
        IIOMetadataNode bKGD_rgb = new IIOMetadataNode("bKGD_RGB");
        bKGD_rgb.setAttribute("red", BKGDRED);
        bKGD_rgb.setAttribute("green", BKGDGREEN);
        bKGD_rgb.setAttribute("blue", BKGDBLUE);
        IIOMetadataNode bKGD = new IIOMetadataNode("bKGD");
        bKGD.appendChild(bKGD_rgb);
        IIOMetadataNode encodeRoot = new IIOMetadataNode("javax_imageio_png_1.0");
        encodeRoot.appendChild(bKGD);
        ImageTypeSpecifier specifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_BGR);
        encodeMetadata = writer.getDefaultImageMetadata(specifier, param);
        encodeMetadata.mergeTree("javax_imageio_png_1.0", encodeRoot);
    }

    private static void writeAndReadMetadata() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        writer.setOutput(ios);
        writer.write(encodeMetadata, new IIOImage(img, null, encodeMetadata), param);
        baos.flush();
        byte[] imageByteArray = baos.toByteArray();
        baos.close();
        InputStream input = new ByteArrayInputStream(imageByteArray);
        ImageInputStream iis = ImageIO.createImageInputStream(input);
        reader.setInput(iis, false, false);
        IIOMetadata decodeMetadata = reader.getImageMetadata(0);
        IIOMetadataNode decodeRoot = (IIOMetadataNode) decodeMetadata.getAsTree("javax_imageio_png_1.0");
        bKGD_RGBNode = (IIOMetadataNode) decodeRoot.getElementsByTagName("bKGD_RGB").item(0);
    }

    private static boolean verifyRGBValues() {
        return (!(BKGDRED.equals(bKGD_RGBNode.getAttribute("red")) && BKGDGREEN.equals(bKGD_RGBNode.getAttribute("green")) && BKGDBLUE.equals(bKGD_RGBNode.getAttribute("blue"))));
    }

    private static void VerifyNativeRGBValuesFromBKGDChunk() throws IOException {
        mergeNativeMetadata();
        writeAndReadMetadata();
        nativeBKGDFail = verifyRGBValues();
    }

    private static void VerifyStandardRGBValuesFromBKGDChunk() throws IOException {
        mergeStandardMetadata();
        writeAndReadMetadata();
        standardBKGDFail = verifyRGBValues();
    }

    public static void main(String[] args) throws IOException {
        int width = 1;
        int height = 1;
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Iterator<ImageWriter> iterWriter = ImageIO.getImageWritersBySuffix("png");
        writer = iterWriter.next();
        param = writer.getDefaultWriteParam();
        Iterator<ImageReader> iterReader = ImageIO.getImageReadersBySuffix("png");
        reader = iterReader.next();
        VerifyNativeRGBValuesFromBKGDChunk();
        VerifyStandardRGBValuesFromBKGDChunk();
        writer.dispose();
        reader.dispose();
        if (nativeBKGDFail || standardBKGDFail) {
            throw new RuntimeException("bKGD RGB values are not stored" + " properly");
        }
    }
}
