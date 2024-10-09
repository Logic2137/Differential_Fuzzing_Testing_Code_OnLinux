



import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;

public class Arc2DIntersectsTest {

    static Shape[][] trues = {
        {
            new Arc2D.Double(0, 0, 100, 100, -45, 90, Arc2D.PIE),
            new Rectangle2D.Double(0, 0, 100, 100),
        },
        {
            new Arc2D.Double(0, 0, 100, 100, -45, 90, Arc2D.PIE),
            new Rectangle2D.Double(25, 25, 50, 50)
        },
        {
            new Arc2D.Double(0, 0, 100, 100, -45, 90, Arc2D.PIE),
            new Rectangle2D.Double(60, 0, 20, 100)
        },
        {
            new Arc2D.Double(0, 0, 100, 100, -135, 270, Arc2D.CHORD),
            new Rectangle2D.Double(20, 0, 20, 100)
        }
    };
    static Shape[][] falses = {
        {
            new Arc2D.Double(0, 0, 100, 100, 0, 360, Arc2D.PIE),
            new Rectangle2D.Double(0, 100, 100, 100)
        },
        {
            new Arc2D.Double(0, 0, 100, 100, 45, 20, Arc2D.PIE),
            new Rectangle2D.Double(75, 75, 100, 100)
        },
        {
            new Arc2D.Double(0, 0, 100, 100, -10, 10, Arc2D.PIE),
            new Rectangle2D.Double(50, 75, 50, 100)
        },
        {
            new Arc2D.Double(0, 0, 100, 100, -10, 10, Arc2D.CHORD),
            new Rectangle2D.Double(60, 0, 10, 100)
        }
    };

    public static void main(String[] args) {

        for (int i = 0; i < trues.length; i++) {
            checkPair((Arc2D)trues[i][0], (Rectangle2D)trues[i][1], true);
        }

        for (int i = 0; i < falses.length; i++) {
            checkPair((Arc2D)falses[i][0], (Rectangle2D)falses[i][1], false);
        }
    }

    public static void checkPair(Arc2D arc, Rectangle2D rect, boolean expect) {

        if (arc.intersects(rect) != expect) {
            String errMsg = "Intersection of Arc(" +
                            arc.getX() + ", " + arc.getY() + ", " +
                            arc.getWidth() + ", " + arc.getHeight() + ", " +
                            "start = " + arc.getAngleStart() + ", " +
                            "extent = " + arc.getAngleExtent() + ", " +
                            typeName(arc.getArcType()) + ") and Rectangle(" +
                            rect.getX() + ", " + rect.getY() + ", " +
                            rect.getWidth() + ", " + rect.getHeight() + ") " +
                            "should be " + expect + ", BUT IT IS NOT.";
            throw new RuntimeException(errMsg);
        }
    }

    public static String typeName(int type) {

        if (type == Arc2D.OPEN)
            return "Open";
        if (type == Arc2D.CHORD)
            return "Chord";
        if (type == Arc2D.PIE)
            return "Pie";
        return null;
    }
}
