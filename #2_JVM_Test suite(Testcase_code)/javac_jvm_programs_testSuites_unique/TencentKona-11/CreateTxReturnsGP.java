



import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

public class CreateTxReturnsGP {
    public static void main(String argv[]) {
        GeneralPath gp = new GeneralPath();
        gp.moveTo(0, 0);
        gp.lineTo(10, 10);
        Shape s1 = gp.createTransformedShape(null);
        Shape s2 = gp.createTransformedShape(new AffineTransform());
        if (!(s1 instanceof GeneralPath) || !(s2 instanceof GeneralPath)) {
            throw new RuntimeException("GeneralPath.createTransformedShape() "+
                                       "did not return a GeneralPath");
        }
    }
}
