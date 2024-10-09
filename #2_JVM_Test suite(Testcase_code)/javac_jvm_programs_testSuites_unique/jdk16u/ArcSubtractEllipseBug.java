


import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class ArcSubtractEllipseBug {
    public static void main(String[] args) {
        double x = -4.250000000000002;
        double y = 0.0;
        double width = 8.5;
        double height = 8.5;
        double start = -450.0;
        double extent = 180.0;

        Arc2D outerArc = new Arc2D.Double(x, y, width, height,
                                          start, extent, Arc2D.PIE);

        Area tmp = new Area(outerArc);

        x = -4.000000000000002;
        y = 0.25;
        width = 8.0;
        height = 8.0;

        Ellipse2D innerArc = new Ellipse2D.Double(x, y, width, height);

        tmp.subtract(new Area(innerArc));
    }
}
