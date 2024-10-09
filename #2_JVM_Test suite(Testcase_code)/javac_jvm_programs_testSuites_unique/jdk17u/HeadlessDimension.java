import java.awt.*;

public class HeadlessDimension {

    public static void main(String[] args) {
        Dimension d;
        d = new Dimension();
        d = new Dimension(new Dimension());
        d = new Dimension(100, 100);
        double f = d.getWidth();
        f = d.getHeight();
        d = new Dimension();
        d.setSize(100.0, 100.0);
        d.setSize(100, 100);
        d = new Dimension(100, 100);
        Dimension d2 = d.getSize();
        d = new Dimension(100, 100);
        d2 = new Dimension(200, 200);
        d.setSize(d2);
        new Dimension(100, 100).equals(new Dimension(200, 200));
        d.hashCode();
        d.toString();
    }
}
