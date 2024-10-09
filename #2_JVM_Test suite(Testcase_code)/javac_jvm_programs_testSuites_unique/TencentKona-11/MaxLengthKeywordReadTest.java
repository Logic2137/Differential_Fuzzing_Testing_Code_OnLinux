



import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Iterator;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class MaxLengthKeywordReadTest {
    
    
    private static String inputImageBase64 = "iVBORw0KGgoAAAANSUhEUgAAAAEAAA" +
            "ABCAAAAAA6fptVAAAAUXRFWHRBdXRob3JlZEF1dGhvcmVkQXV0aG9yZWRBdXRob" +
            "3JlZEF1dGhvcmVkQXV0aG9yZWRBdXRob3JlZEF1dGhvcmVkQXV0aG9yZWRBdXRo" +
            "b3JlZAAhn0sLAAAACklEQVR4XmP4DwABAQEAJwnd2gAAAABJRU5ErkJggg==";

    public static void main(String[] args) throws IOException {
        byte[] inputBytes = Base64.getDecoder().decode(inputImageBase64);
        ByteArrayInputStream bais = new ByteArrayInputStream(inputBytes);
        ImageInputStream input = ImageIO.createImageInputStream(bais);
        Iterator iter = ImageIO.getImageReaders(input);
        ImageReader reader = (ImageReader) iter.next();
        reader.setInput(input, false, false);
        try {
            reader.read(0, reader.getDefaultReadParam());
        } catch (IIOException e) {
            
            if (e.getCause().getMessage() !=
                "Found non null terminated string") {
                throw new RuntimeException("Test failed. Did not get " +
                        "expected IIOException");
            }
        }
    }
}
