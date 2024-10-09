

import java.awt.*;



public class HeadlessRectangle {
    public static void main(String args[]) {
        Rectangle r;
        r = new Rectangle();
        r = new Rectangle(new Rectangle());
        r = new Rectangle(100, 200);
        r = new Rectangle(new Point(100, 200), new Dimension(300, 400));
        r = new Rectangle(new Point(100, 200));
        r = new Rectangle(new Dimension(300, 400));
        r = new Rectangle(100, 200, 300, 400);
        r.getX();
        r.getY();
        r.getWidth();
        r.getHeight();
        r.getBounds();
        r.getBounds2D();
        r.getLocation();
        r.getSize();
        r.contains(new Point(1, 2));
        r.contains(1, 2);
        r.contains(new Rectangle(1, 2, 3, 4));
        r.contains(1, 2, 3, 4);
        r.add(1, 2);
        r.add(new Point(1, 2));
        r.add(new Rectangle(1, 2, 3, 4));
        r.grow(1, 2);
        r.isEmpty();
        r.toString();
        r.hashCode();
        r.getMinX();
        r.getMinY();
        r.getMaxX();
        r.getMaxY();
        r.getCenterX();
        r.getCenterY();
        r.getFrame();
    }
}
