



import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;

public class Quad2DContainsTest {

    static Object[][] trues = {
        {
            new QuadCurve2D.Double(0, 0, 50, 200, 100, 0),
            new Point2D.Double(50, 50)
        },
        {
            new QuadCurve2D.Double(0, 100, 100, 100, 100, 0),
            new Point2D.Double(50, 50)
        }
    };
    static Object[][] falses = {
        {
            new QuadCurve2D.Double(0, 0, 50, 200, 100, 0),
            new Point2D.Double(0, 50)
        },
        {
            new QuadCurve2D.Double(0, 0, 50, 200, 100, 0),
            new Point2D.Double(100, 50)
        },
        {
            new QuadCurve2D.Double(0, 0, 0, 100, 100, 100),
            new Point2D.Double(0, 100)
        },
        {
            new QuadCurve2D.Double(0, 0, 60, 60, 100, 100),
            new Point2D.Double(30, 30)
        }
    };

    public static void main(String[] args) {

        for (int i = 0; i < trues.length; i++) {
            checkPair((QuadCurve2D)trues[i][0], (Point2D)trues[i][1], true);
        }

        for (int i = 0; i < falses.length; i++) {
            checkPair((QuadCurve2D)falses[i][0], (Point2D)falses[i][1], false);
        }
    }

    public static void checkPair(QuadCurve2D q, Point2D p, boolean expect) {

        if (q.contains(p.getX(), p.getY()) != expect) {
            String errMsg = "QuadCurve2D " +
                            "p1 = (" + q.getX1() + ", " + q.getY1() + ") " +
                            "p2 = (" + q.getX2() + ", " + q.getY2() + ") " +
                            "control = (" + q.getCtrlX() + ", " +
                                            q.getCtrlY() + ") " +
                            "should " + (expect ? "" : "not ") +
                            "contain the point (" +
                            p.getX() + ", " + p.getY() + "), " +
                            "but method returns wrong value!";
            throw new RuntimeException(errMsg);
        }
    }
}
