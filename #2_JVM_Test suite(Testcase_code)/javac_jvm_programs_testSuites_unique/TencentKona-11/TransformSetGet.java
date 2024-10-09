import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.VolatileImage;
import sun.java2d.SunGraphics2D;

public class TransformSetGet {

    public static void main(final String[] args) {
        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
        final VolatileImage vi = gc.createCompatibleVolatileImage(200, 200);
        final SunGraphics2D sg2d = (SunGraphics2D) vi.createGraphics();
        sg2d.constrain(0, 61, 100, 100);
        final AffineTransform expected = sg2d.cloneTransform();
        sg2d.setTransform(sg2d.getTransform());
        final AffineTransform actual = sg2d.cloneTransform();
        sg2d.dispose();
        vi.flush();
        if (!expected.equals(actual)) {
            System.out.println("Expected = " + expected);
            System.out.println("Actual = " + actual);
            throw new RuntimeException("Wrong transform");
        }
    }
}
