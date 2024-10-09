



import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;

public class EmptyInputBmpMetadataTest {
    private static String fmt = "BMP";

    public static void main(String[] args) {
        boolean isPassed = false;
        ImageReader ir = (ImageReader)ImageIO.getImageReadersByFormatName(fmt).next();

        if (ir == null) {
            throw new RuntimeException("No available reader for " + fmt);
        }
        IIOMetadata meta = null;
        try {
            meta = ir.getImageMetadata(0);
        } catch (IllegalStateException e) {
            System.out.println("Correct exception was thrown. Test passed.");
            isPassed = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!isPassed) {
            throw new RuntimeException("The IllegalStateException was not thrown."
                                       +"Test failed.");
        }
    }
}
