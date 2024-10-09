



import java.awt.geom.Arc2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

public class SmallExtAngleTest {

    public static void main(String[] args) {

        String errorMsg = "NaN occured in coordinates of SEG_CUBICTO";
        Rectangle2D bounds = new Rectangle2D.Double(-100, -100, 200, 200);
        Arc2D arc = new Arc2D.Double(bounds, 90, -1.0E-7, Arc2D.PIE);
        double[] coords = new double[6];

        PathIterator p = arc.getPathIterator(null);
        while (!p.isDone()) {

            if (p.currentSegment(coords) == PathIterator.SEG_CUBICTO) {

                for (int i = 0; i < 6; i++) {
                    if (coords[i] != coords[i]) {
                        throw new RuntimeException(errorMsg);
                    }
                }
            }
            p.next();
        }
    }
}
