import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class JpegNumThumbnailsTest {

    public static void main(String[] args) {
        Iterator<ImageWriter> iterWriter = null;
        ImageWriter jpgWriter = null;
        IIOMetadata imgMetadata = null;
        BufferedImage testImage = null;
        ImageTypeSpecifier imgType = null;
        int numThumbnails = 0;
        iterWriter = ImageIO.getImageWritersByFormatName("JPEG");
        if (iterWriter.hasNext()) {
            try {
                jpgWriter = iterWriter.next();
                testImage = new BufferedImage(32, 32, TYPE_INT_RGB);
                imgType = ImageTypeSpecifier.createFromRenderedImage(testImage);
                imgMetadata = jpgWriter.getDefaultImageMetadata(imgType, null);
                numThumbnails = jpgWriter.getNumThumbnailsSupported(null, null, null, null);
                if (numThumbnails != -1) {
                    reportException("Incorrect number of thumbnails returned.");
                }
                numThumbnails = jpgWriter.getNumThumbnailsSupported(imgType, null, null, null);
                if (numThumbnails != Integer.MAX_VALUE) {
                    reportException("Incorrect number of thumbnails returned.");
                }
                numThumbnails = jpgWriter.getNumThumbnailsSupported(null, null, null, imgMetadata);
                if (numThumbnails != Integer.MAX_VALUE) {
                    reportException("Incorrect number of thumbnails returned.");
                }
                numThumbnails = jpgWriter.getNumThumbnailsSupported(imgType, null, null, imgMetadata);
                if (numThumbnails != Integer.MAX_VALUE) {
                    reportException("Incorrect number of thumbnails returned.");
                }
            } finally {
                jpgWriter.dispose();
            }
        }
    }

    private static void reportException(String message) {
        throw new RuntimeException("Test Failed. " + message);
    }
}
