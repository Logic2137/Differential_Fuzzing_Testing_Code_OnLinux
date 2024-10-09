import java.awt.*;

public class HeadlessPoint {

    public static void main(String[] args) {
        Point p;
        p = new Point();
        p = new Point(new Point(1, 2));
        p = new Point(1, 2);
        p.getX();
        p.getY();
        p.getLocation();
        p.setLocation(new Point(3, 4));
    }
}
