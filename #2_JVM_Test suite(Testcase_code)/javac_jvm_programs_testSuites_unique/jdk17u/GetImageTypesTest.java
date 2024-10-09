import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

public class GetImageTypesTest {

    private static final String format = "wbmp";

    public static void main(String[] args) {
        boolean passed = false;
        ImageReader ir = (ImageReader) ImageIO.getImageReadersByFormatName(format).next();
        if (ir == null) {
            throw new RuntimeException("No matching reader found. Test Failed");
        }
        try {
            Iterator types = ir.getImageTypes(0);
        } catch (IllegalStateException e) {
            System.out.println("Test passed.");
            passed = true;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception was thrown. " + "Test failed.");
        }
        if (!passed) {
            throw new RuntimeException("IllegalStateException is not thrown when " + "calling getImageTypes() without setting " + "the input source for the image format: " + format + ". Test failed");
        }
    }
}
