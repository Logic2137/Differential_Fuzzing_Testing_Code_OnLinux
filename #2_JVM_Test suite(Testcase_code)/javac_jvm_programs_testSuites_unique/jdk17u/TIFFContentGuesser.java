import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLConnection;

public class TIFFContentGuesser {

    private static final byte[] LITTLE_ENDIAN_MAGIC = new byte[] { (byte) 0x49, (byte) 0x49, (byte) 0x2a, (byte) 0 };

    private static final byte[] BIG_ENDIAN_MAGIC = new byte[] { (byte) 0x4d, (byte) 0x4d, (byte) 0, (byte) 0x2a };

    private static final String TIFF_MIME_TYPE = "image/tiff";

    public static void main(String[] args) throws Throwable {
        int failures = 0;
        InputStream stream = new ByteArrayInputStream(LITTLE_ENDIAN_MAGIC);
        String contentType = URLConnection.guessContentTypeFromStream(stream);
        if (contentType == null || !contentType.equals(TIFF_MIME_TYPE)) {
            failures++;
            System.err.println("Test failed for little endian magic");
        }
        stream = new ByteArrayInputStream(BIG_ENDIAN_MAGIC);
        contentType = URLConnection.guessContentTypeFromStream(stream);
        if (contentType == null || !contentType.equals(TIFF_MIME_TYPE)) {
            failures++;
            System.err.println("Test failed for big endian magic");
        }
        if (failures != 0) {
            throw new RuntimeException("Test failed with " + failures + " error(s)");
        }
    }
}
