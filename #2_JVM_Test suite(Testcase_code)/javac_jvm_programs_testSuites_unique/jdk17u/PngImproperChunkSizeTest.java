import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

public class PngImproperChunkSizeTest {

    private static ImageReader reader;

    private static String zTXTMalformedData = "iVBORw0KGgoAAAANSUhEUgAAAAEAAA" + "ABCAAAAAA6fptVAAAABHpUWHRhYWFhYWFhYQAAAApJREFUGFdj+A8AAQEBAFpNb" + "/EAAAAASUVORK5CYIIK";

    private static String tEXTMalformedData = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAAB" + "CAMAAAA6fptVAAAABHRFWHRhYWFhYWFhYQAAAApJREFUGFdj+A8AAQEBAFpNb" + "/EAAAAASUVORK5CYIIK";

    private static String iCCPMalformedData = "iVBORw0KGgoAAAANSUhEUgAAAAEAAA" + "ABCAAAAAA6fptVAAAABGlDQ1BhYWFhYWFhYQAAAApJREFUGFdj+A8AAQEBAFpNb" + "/EAAAAASUVORK5CYIIK";

    private static ByteArrayInputStream initializeInputStream(String input) {
        byte[] inputBytes = Base64.getDecoder().decode(input);
        return new ByteArrayInputStream(inputBytes);
    }

    private static Boolean readzTXTData(InputStream input) throws IOException {
        reader.setInput(ImageIO.createImageInputStream(input), true, false);
        try {
            reader.read(0);
        } catch (IIOException e) {
            Throwable cause = e.getCause();
            if (cause == null || !cause.getMessage().equals("zTXt chunk length is not proper")) {
                return true;
            }
        }
        return false;
    }

    private static Boolean readtEXTData(InputStream input) throws IOException {
        reader.setInput(ImageIO.createImageInputStream(input), true, false);
        try {
            reader.read(0);
        } catch (IIOException e) {
            Throwable cause = e.getCause();
            if (cause == null || !cause.getMessage().equals("tEXt chunk length is not proper")) {
                return true;
            }
        }
        return false;
    }

    private static Boolean readiCCPData(InputStream input) throws IOException {
        reader.setInput(ImageIO.createImageInputStream(input), true, false);
        try {
            reader.read(0);
        } catch (IIOException e) {
            Throwable cause = e.getCause();
            if (cause == null || !cause.getMessage().equals("iCCP chunk length is not proper")) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws java.io.IOException {
        reader = ImageIO.getImageReadersByFormatName("png").next();
        InputStream in = initializeInputStream(zTXTMalformedData);
        Boolean zTXTFailed = readzTXTData(in);
        in = initializeInputStream(tEXTMalformedData);
        Boolean tEXTFailed = readtEXTData(in);
        in = initializeInputStream(iCCPMalformedData);
        Boolean iCCPFailed = readiCCPData(in);
        reader.dispose();
        if (zTXTFailed || tEXTFailed || iCCPFailed) {
            throw new RuntimeException("Test didn't throw the required" + " Exception");
        }
    }
}
