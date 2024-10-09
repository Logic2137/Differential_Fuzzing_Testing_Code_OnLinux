



import java.awt.font.TransformAttribute;
import java.awt.geom.AffineTransform;

public class TransformEqualityTest {

    public static void main(String[] args) {
        AffineTransform tx1 = new AffineTransform(1, 0, 1, 1, 0, 0);
        AffineTransform tx2 = new AffineTransform(1, 0, 1, 1, 0, 0);
        AffineTransform tx3 = new AffineTransform(2, 0, 1, 1, 0, 0);
        TransformAttribute ta1a = new TransformAttribute(tx1);
        TransformAttribute ta1b = new TransformAttribute(tx1);
        TransformAttribute ta2 = new TransformAttribute(tx2);
        TransformAttribute ta3 = new TransformAttribute(tx3);
        if (ta1a.equals(null)) {
            throw new RuntimeException("should not be equal to null.");
        }
        if (!ta1a.equals(ta1a)) {
            throw new RuntimeException("(1) should be equal.");
        }
        if (!ta1a.equals(ta1b)) {
            throw new RuntimeException("(2) should be equal.");
        }
        if (!ta1a.equals(ta2)) {
            throw new RuntimeException("(3) should be equal.");
        }
        if (ta2.equals(ta3)) {
            throw new RuntimeException("should be different.");
        }
    }
}

