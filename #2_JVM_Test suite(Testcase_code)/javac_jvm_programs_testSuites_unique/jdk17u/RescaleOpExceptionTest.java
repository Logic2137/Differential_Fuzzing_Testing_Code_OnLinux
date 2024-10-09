import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;

public class RescaleOpExceptionTest {

    public static void main(String[] args) throws Exception {
        RescaleOp op = new RescaleOp(1.0f, 0.0f, null);
        BufferedImage srcI = new BufferedImage(1, 1, TYPE_INT_RGB);
        BufferedImage dstI = new BufferedImage(1, 2, TYPE_INT_RGB);
        boolean caughtIAE = false;
        try {
            op.filter(srcI, dstI);
        } catch (IllegalArgumentException e) {
            caughtIAE = true;
        }
        if (!caughtIAE) {
            throw new RuntimeException("Expected IllegalArgumentException");
        }
        WritableRaster srcR = srcI.getRaster();
        WritableRaster dstR = dstI.getRaster();
        caughtIAE = false;
        try {
            op.filter(srcR, dstR);
        } catch (IllegalArgumentException e) {
            caughtIAE = true;
        }
        if (!caughtIAE) {
            throw new RuntimeException("Expected IllegalArgumentException");
        }
    }
}
