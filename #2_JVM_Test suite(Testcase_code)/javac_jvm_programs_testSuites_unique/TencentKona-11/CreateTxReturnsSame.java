



import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;

public class CreateTxReturnsSame {
    public static void main(String argv[]) {
        test(new GeneralPath());
        test(new Path2D.Float());
        test(new Path2D.Double());
    }

    public static void test(Path2D p2d) {
        p2d.moveTo(0, 0);
        p2d.lineTo(10, 10);
        Shape s1 = p2d.createTransformedShape(null);
        Shape s2 = p2d.createTransformedShape(new AffineTransform());
        if (s1.getClass() != p2d.getClass() ||
            s2.getClass() != p2d.getClass())
        {
            throw new RuntimeException(p2d.getClass().getName()+
                                       ".createTransformedShape() "+
                                       "did not return a "+
                                       p2d.getClass().getName());
        }
    }
}
