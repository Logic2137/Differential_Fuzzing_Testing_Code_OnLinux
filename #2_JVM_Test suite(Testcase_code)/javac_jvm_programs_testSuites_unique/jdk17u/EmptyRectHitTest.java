import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

public class EmptyRectHitTest {

    public static int numerrors;

    public static void main(String[] argv) {
        Rectangle select1 = new Rectangle(1, 1, 1, 1);
        Rectangle select2 = new Rectangle(1, 1, 0, 0);
        Rectangle select3 = new Rectangle(100, 100, 1, 1);
        Rectangle select4 = new Rectangle(100, 100, 0, 0);
        Rectangle rect = new Rectangle(-5, -5, 10, 10);
        test(rect, select1, true, true);
        test(rect, select2, false, false);
        test(rect, select3, false, false);
        test(rect, select4, false, false);
        GeneralPath gp = new GeneralPath(rect);
        test(gp, select1, true, true);
        test(gp, select2, false, false);
        test(gp, select3, false, false);
        test(gp, select4, false, false);
        AffineTransform xform = new AffineTransform();
        xform.setToRotation(Math.PI / 4);
        Shape shape = xform.createTransformedShape(rect);
        test(shape, select1, true, true);
        test(shape, select2, false, false);
        test(shape, select3, false, false);
        test(shape, select4, false, false);
        if (numerrors > 0) {
            throw new RuntimeException(numerrors + " errors in tests");
        }
    }

    public static void test(Shape testshape, Rectangle rect, boolean shouldcontain, boolean shouldintersect) {
        if (testshape.contains(rect) != shouldcontain) {
            error(testshape, rect, "contains", !shouldcontain);
        }
        if (testshape.intersects(rect) != shouldintersect) {
            error(testshape, rect, "intersects", !shouldintersect);
        }
    }

    public static void error(Shape t, Rectangle r, String type, boolean res) {
        numerrors++;
        System.err.println(t + type + "(" + r + ") == " + res);
    }
}
