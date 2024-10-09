



import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import javax.imageio.ImageIO;

public class PngNegativeDimensionTest {

    private static String negativeWidthString = "iVBORw0KGgoAAAANSUhEUoAAAAEAA"
            + "AABCAAAAAA6fptVAAAACklEQVQYV2P4DwABAQEAWk1v8QAAAABJRU5ErkJgggo=";

    private static String negativeHeightString = "iVBORw0KGgoAAAANSUhEUgAAAAGAA"
            + "AABCAAAAAA6fptVAAAACklEQVQYV2P4DwABAQEAWk1v8QAAAABJRU5ErkJgggo=";

    private static InputStream input;
    private static Boolean failed = false;

    public static void main(String[] args) {
        
        byte[] inputBytes = Base64.getDecoder().decode(negativeWidthString);
        input = new ByteArrayInputStream(inputBytes);
        
        readNegativeIHDRWidthImage();

        inputBytes = Base64.getDecoder().decode(negativeHeightString);
        input = new ByteArrayInputStream(inputBytes);
        
        readNegativeIHDRHeightImage();

        if (failed) {
            throw new RuntimeException("Test didnt throw proper IIOException"
                    + " when IHDR width/height is negative");
        }
    }

    private static void readNegativeIHDRWidthImage() {
        try {
            ImageIO.read(input);
        } catch (Exception e) {
            
            Throwable cause = e.getCause();
            if (cause == null ||
                (!(cause.getMessage().equals("Image width <= 0!"))))
            {
                failed = true;
            }
        }
    }

    private static void readNegativeIHDRHeightImage() {
        try {
            ImageIO.read(input);
        } catch (Exception e) {
            
            Throwable cause = e.getCause();
            if (cause == null ||
                (!(cause.getMessage().equals("Image height <= 0!"))))
            {
                failed = true;
            }
        }
    }
}

