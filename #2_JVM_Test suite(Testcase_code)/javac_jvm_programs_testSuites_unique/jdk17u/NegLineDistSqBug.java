import java.awt.geom.Line2D;

public class NegLineDistSqBug {

    static int errored;

    public static void main(String[] args) {
        test(1, 2, 3, 4, 15, 19, 21, 32);
        test(-313.0, 241.0, -97.0, 75.0, 126.15362619253153, -96.49769420351959, -97.0, 75.0);
        for (double fract = 0.0; fract <= 0.5; fract += 0.01) {
            test(-313.0, 241.0, -97.0, 75.0, interp(-313.0, -97.0, fract), interp(241.0, 75.0, fract), interp(-313.0, -97.0, 1 - fract), interp(241.0, 75.0, 1 - fract));
        }
        if (errored > 0) {
            throw new RuntimeException(errored + " negative distances!");
        }
    }

    public static double interp(double v1, double v2, double t) {
        return (v1 * (1 - t) + v2 * t);
    }

    public static void test(double l1x1, double l1y1, double l1x2, double l1y2, double l2x1, double l2y1, double l2x2, double l2y2) {
        Line2D l1 = new Line2D.Double(l1x1, l1y1, l1x2, l1y2);
        Line2D l2 = new Line2D.Double(l2x1, l2y1, l2x2, l2y2);
        System.out.println("Line distances:");
        verify(l1.ptLineDistSq(l2.getP1()));
        verify(l1.ptLineDistSq(l2.getP2()));
        System.out.println("Segment distances:");
        verify(l1.ptSegDistSq(l2.getP1()));
        verify(l1.ptSegDistSq(l2.getP2()));
    }

    public static void verify(double distSq) {
        System.out.println(distSq);
        if (distSq < 0) {
            errored++;
        }
    }
}
