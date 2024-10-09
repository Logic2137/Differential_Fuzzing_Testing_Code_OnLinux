



import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class BoundsCache {

    public static void main(String s[]) {
        Ellipse2D cir = new Ellipse2D.Double(110.0,100.0, 50.0, 50.0);
        Ellipse2D cir2 = new Ellipse2D.Double(110.0,100.0, 50.0, 50.0);
        Area area_cir = new Area (cir);

        System.out.println("before operation (bounds should be non-empty):");
        System.out.println("   bounds are "+area_cir.getBounds());
        System.out.println("   2D bounds are "+area_cir.getBounds2D());
        System.out.println("   isEmpty returns "+area_cir.isEmpty());
        area_cir.subtract(new Area (cir2));
        System.out.println("after operation (bounds should be empty):");
        System.out.println("   bounds are "+area_cir.getBounds());
        System.out.println("   2D bounds are "+area_cir.getBounds2D());
        System.out.println("   isEmpty returns "+area_cir.isEmpty());
        if (!area_cir.isEmpty() ||
            !area_cir.getBounds().isEmpty() ||
            !area_cir.getBounds2D().isEmpty())
        {
            throw new RuntimeException("result was not empty!");
        }

        Area area = new Area(cir);
        area.getBounds();
        area.reset();
        if (!area.getBounds().isEmpty()) {
            throw new RuntimeException("result was not empty!");
        }

        area = new Area(cir);
        Rectangle r = area.getBounds();
        area.transform(new AffineTransform(1, 0, 0, 1, 10, 0));
        if (area.getBounds().equals(r)) {
            throw new RuntimeException("bounds not updated in transform!");
        }
    }
}
