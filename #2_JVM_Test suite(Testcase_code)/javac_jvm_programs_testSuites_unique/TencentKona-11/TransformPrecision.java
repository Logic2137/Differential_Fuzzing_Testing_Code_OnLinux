



import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

public class TransformPrecision {
    public static void main(String argv[]) {
        int x = 1 << 27;
        int y = 1 << 26;

        AffineTransform tx = AffineTransform.getTranslateInstance(x, y);
        AffineTransform untx = AffineTransform.getTranslateInstance(-x, -y);

        Rectangle r = new Rectangle(10, 10, 20, 20);
        Area a = new Area(r);

        Area b = new Area(r);
        b.transform(tx);
        b.transform(untx);
        if (!a.equals(b)) {
            throw new RuntimeException("large translation hurt precision!");
        }

        b = a.createTransformedArea(tx);
        b = b.createTransformedArea(untx);
        if (!a.equals(b)) {
            throw new RuntimeException("large translation hurt precision!");
        }
    }
}
