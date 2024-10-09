import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;

public class PngPLTEChunkMissingTest {

    private static String inputImageBase64 = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAAB" + "CAMAAAA6fptVAAAACklEQVQYV2P4DwABAQEAWk1v8QAAAABJRU5ErkJgggo=";

    public static void main(String[] args) throws Exception {
        byte[] inputBytes = Base64.getDecoder().decode(inputImageBase64);
        InputStream in = new ByteArrayInputStream(inputBytes);
        try {
            ImageIO.read(in);
        } catch (IIOException e) {
            Throwable cause = e.getCause();
            if (cause == null || (!(cause.getMessage().equals("Required PLTE chunk missing")))) {
                throw e;
            }
        }
    }
}
