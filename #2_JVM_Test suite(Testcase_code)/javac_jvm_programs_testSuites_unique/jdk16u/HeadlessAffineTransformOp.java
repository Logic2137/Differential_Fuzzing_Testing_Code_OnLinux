

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;



public class HeadlessAffineTransformOp {
    public static void main(String args[]) {
        AffineTransformOp ato;

        ato = new AffineTransformOp(new AffineTransform(), AffineTransformOp.TYPE_BILINEAR);
        ato = new AffineTransformOp(new AffineTransform(), AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        ato = new AffineTransformOp(new AffineTransform(), AffineTransformOp.TYPE_BILINEAR);
        ato.getInterpolationType();
        ato.getTransform();
    }
}
