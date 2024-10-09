import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

public class SetAnglesTest {

    public void runTest() {
        Arc2D arc = new Arc2D.Double(-10, -40, 20, 80, 0, 0, Arc2D.PIE);
        Point2D p1 = new Point2D.Double(10, 10);
        Point2D p2 = new Point2D.Double(10, -10);
        double threshold = 1.0E-10;
        arc.setAngles(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        Point2D start = arc.getStartPoint();
        Point2D end = arc.getEndPoint();
        checkColinear(start.getX(), start.getY(), p1.getX(), p1.getY());
        checkColinear(end.getX(), end.getY(), p2.getX(), p2.getY());
    }

    void checkColinear(double dx1, double dy1, double dx2, double dy2) {
        double threshold = 1.0E-10;
        if (!(dx1 * dx2 >= 0 && dy1 * dy2 >= 0 && Math.abs(dx1 * dy2 - dx2 * dy1) <= threshold)) {
            throw new RuntimeException("Angles difference is too much");
        }
    }

    public static void main(String[] args) {
        SetAnglesTest test = new SetAnglesTest();
        test.runTest();
    }
}
