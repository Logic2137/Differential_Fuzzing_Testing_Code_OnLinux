import java.awt.*;

public class HeadlessGradientPaint {

    public static void main(String[] args) {
        GradientPaint gp;
        gp = new GradientPaint(10, 10, Color.red, 20, 20, Color.blue);
        gp = new GradientPaint(new Point(10, 10), Color.red, new Point(20, 20), Color.blue);
        gp = new GradientPaint(10, 10, Color.red, 20, 20, Color.blue, true);
        gp = new GradientPaint(10, 10, Color.red, 20, 20, Color.blue, false);
        gp = new GradientPaint(new Point(10, 10), Color.red, new Point(20, 20), Color.blue, true);
        gp = new GradientPaint(new Point(10, 10), Color.red, new Point(20, 20), Color.blue, false);
        gp = new GradientPaint(10, 10, Color.red, 20, 20, Color.blue, false);
        gp.getPoint1();
        gp.getColor1();
        gp.getPoint2();
        gp.getColor2();
        gp.isCyclic();
        gp.getTransparency();
    }
}
