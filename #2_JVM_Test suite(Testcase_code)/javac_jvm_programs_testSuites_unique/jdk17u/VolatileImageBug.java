import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.VolatileImage;

public class VolatileImageBug {

    public static void main(String[] args) {
        boolean iaeThrown = false;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
        try {
            VolatileImage volatileImage = gc.createCompatibleVolatileImage(0, 0);
        } catch (IllegalArgumentException iae) {
            iaeThrown = true;
        }
        if (!iaeThrown) {
            throw new RuntimeException("IllegalArgumentException not thrown " + "for createCompatibleVolatileImage(0,0)");
        }
    }
}
