

import java.awt.image.*;
import java.awt.geom.*;



public class HeadlessBufferedImageFilter {
    public static void main(String args[]) {
        new BufferedImageFilter(new AffineTransformOp(new AffineTransform(), AffineTransformOp.TYPE_BILINEAR)).clone();
    }
}
