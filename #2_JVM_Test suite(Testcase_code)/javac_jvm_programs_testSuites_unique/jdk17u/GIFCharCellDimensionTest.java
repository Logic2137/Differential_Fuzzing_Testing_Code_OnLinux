import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.metadata.IIOMetadataFormat;
import java.util.Iterator;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;

public class GIFCharCellDimensionTest {

    private static IIOMetadata imageMetadata = null;

    private static IIOMetadataFormat metadataFormat = null;

    private static String formatName = null;

    private static void initializeTest() {
        ImageWriter gifWriter = null;
        Iterator<ImageWriter> iterGifWriter = null;
        BufferedImage bufImage = null;
        ImageTypeSpecifier imageType = null;
        try {
            iterGifWriter = ImageIO.getImageWritersBySuffix("GIF");
            if (iterGifWriter.hasNext()) {
                gifWriter = iterGifWriter.next();
                bufImage = new BufferedImage(32, 32, TYPE_4BYTE_ABGR);
                imageMetadata = gifWriter.getDefaultImageMetadata(ImageTypeSpecifier.createFromRenderedImage(bufImage), gifWriter.getDefaultWriteParam());
                if (imageMetadata == null) {
                    reportException("Test Failed. Could not get image" + " metadata.");
                }
                formatName = imageMetadata.getNativeMetadataFormatName();
                metadataFormat = imageMetadata.getMetadataFormat(formatName);
                if (metadataFormat == null) {
                    reportException("Test Failed. Could not get native" + " metadata format.");
                }
            } else {
                reportException("Test Failed. No GIF image writer found.");
            }
        } finally {
            gifWriter.dispose();
        }
    }

    private static IIOMetadataNode createPlainTextExtensionNode(String value) {
        IIOMetadataNode rootNode = null;
        if (imageMetadata != null && formatName != null) {
            IIOMetadataNode plainTextNode = null;
            rootNode = new IIOMetadataNode(formatName);
            plainTextNode = new IIOMetadataNode("PlainTextExtension");
            plainTextNode.setAttribute("textGridLeft", "0");
            plainTextNode.setAttribute("textGridTop", "0");
            plainTextNode.setAttribute("textGridWidth", "32");
            plainTextNode.setAttribute("textGridHeight", "32");
            plainTextNode.setAttribute("characterCellWidth", value);
            plainTextNode.setAttribute("characterCellHeight", value);
            plainTextNode.setAttribute("textForegroundColor", "0");
            plainTextNode.setAttribute("textBackgroundColor", "1");
            rootNode.appendChild(plainTextNode);
        } else {
            reportException("Test Failed. Un-initialized image metadata.");
        }
        return rootNode;
    }

    private static void testCharacterCellDimensions() {
        if (imageMetadata != null && metadataFormat != null) {
            String cellWidth = metadataFormat.getAttributeMaxValue("PlainTextExtension", "characterCellWidth");
            String cellHeight = metadataFormat.getAttributeMaxValue("PlainTextExtension", "characterCellHeight");
            int maxCharCellWidth = Integer.parseInt(cellWidth);
            int maxCharCellHeight = Integer.parseInt(cellHeight);
            if (maxCharCellWidth > 255 || maxCharCellHeight > 255) {
                reportException("Test Failed. Invalid max range for" + " character cell width or character cell height.");
            }
            try {
                IIOMetadataNode root = createPlainTextExtensionNode("256");
                imageMetadata.setFromTree(formatName, root);
            } catch (IIOInvalidTreeException exception) {
            }
        } else {
            reportException("Test Failed. Un-initialized image metadata or" + " metadata format.");
        }
    }

    private static void reportException(String message) {
        throw new RuntimeException(message);
    }

    public static void main(String[] args) {
        initializeTest();
        testCharacterCellDimensions();
    }
}
