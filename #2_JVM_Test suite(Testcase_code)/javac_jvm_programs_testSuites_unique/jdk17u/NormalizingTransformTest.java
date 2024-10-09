import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;

public class NormalizingTransformTest {

    public static void main(String[] args) {
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        AffineTransform normTransform = gc.getNormalizingTransform();
        int dpiX = Toolkit.getDefaultToolkit().getScreenResolution();
        int normDpiX = (int) (normTransform.getScaleX() * 72.0);
        if (dpiX != normDpiX) {
            throw new RuntimeException("Test FAILED. Toolkit.getScreenResolution()=" + dpiX + " GraphicsConfiguration.getNormalizingTransform()=" + normDpiX);
        }
        System.out.println("Test PASSED. DPI=" + normDpiX);
    }
}
