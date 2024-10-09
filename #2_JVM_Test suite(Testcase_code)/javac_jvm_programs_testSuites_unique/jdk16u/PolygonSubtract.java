



import java.awt.geom.Area;
import java.awt.geom.GeneralPath;

public class PolygonSubtract {
    public static void main(String argv[]) {
        Area area = new Area();

        GeneralPath ring1 = new GeneralPath();

        ring1.moveTo(0, 45);
        ring1.lineTo(90, 0);
        ring1.lineTo(60, -45);
        ring1.lineTo(-60, -45);
        ring1.lineTo(-90, 0);

        ring1.closePath();

        area.add(new Area(ring1));

        GeneralPath ring2 = new GeneralPath();

        ring2.moveTo(0, 20);
        ring2.lineTo(100, 0);
        ring2.lineTo(30, -20);
        ring2.lineTo(-30, -20);
        ring2.lineTo(-100, 0);
        ring2.closePath();

        area.subtract(new Area(ring2));

        if (!area.contains(50, 13, 2, 2)) {
            throw new RuntimeException("Area subtraction failed!");
        }
    }
}
