import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;

public class GetReaderWriterInfo {

    private static void testGetReaderFormatNames() {
        String[] names = ImageIO.getReaderFormatNames();
        for (String n : names) {
            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(n);
            if (!it.hasNext()) {
                throw new RuntimeException("getReaderFormatNames returned " + "an unknown name: " + n);
            }
        }
    }

    private static void testGetReaderMIMETypes() {
        String[] types = ImageIO.getReaderMIMETypes();
        for (String t : types) {
            Iterator<ImageReader> it = ImageIO.getImageReadersByMIMEType(t);
            if (!it.hasNext()) {
                throw new RuntimeException("getReaderMIMETypes returned " + "an unknown type: " + t);
            }
        }
    }

    private static void testGetReaderFileSuffixes() {
        String[] suffixes = ImageIO.getReaderFileSuffixes();
        for (String s : suffixes) {
            Iterator<ImageReader> it = ImageIO.getImageReadersBySuffix(s);
            if (!it.hasNext()) {
                throw new RuntimeException("getReaderFileSuffixes returned " + "an unknown suffix: " + s);
            }
        }
    }

    private static void testGetWriterFormatNames() {
        String[] names = ImageIO.getWriterFormatNames();
        for (String n : names) {
            Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName(n);
            if (!it.hasNext()) {
                throw new RuntimeException("getWriterFormatNames returned " + "an unknown name: " + n);
            }
        }
    }

    private static void testGetWriterMIMETypes() {
        String[] types = ImageIO.getWriterMIMETypes();
        for (String t : types) {
            Iterator<ImageWriter> it = ImageIO.getImageWritersByMIMEType(t);
            if (!it.hasNext()) {
                throw new RuntimeException("getWriterMIMETypes returned " + "an unknown type: " + t);
            }
        }
    }

    private static void testGetWriterFileSuffixes() {
        String[] suffixes = ImageIO.getWriterFileSuffixes();
        for (String s : suffixes) {
            Iterator<ImageWriter> it = ImageIO.getImageWritersBySuffix(s);
            if (!it.hasNext()) {
                throw new RuntimeException("getWriterFileSuffixes returned " + "an unknown suffix: " + s);
            }
        }
    }

    public static void main(String[] args) {
        testGetReaderFormatNames();
        testGetReaderMIMETypes();
        testGetReaderFileSuffixes();
        testGetWriterFormatNames();
        testGetWriterMIMETypes();
        testGetWriterFileSuffixes();
    }
}
