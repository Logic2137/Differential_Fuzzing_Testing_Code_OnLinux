



import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

public class ChordContainsTest {

    public void runTest() {

        Arc2D a = new Arc2D.Double(-20, -20, 40, 40, -60, 120, Arc2D.CHORD);

        
        
        Point2D p = new Point2D.Double( a.getWidth() / 6, 0);

        if (a.contains(p.getX(), p.getY())) {
            throw new RuntimeException("Point out of arc recognized as containing");
        }
    }

    public static void main(String[] args) {
        ChordContainsTest test = new ChordContainsTest();
        test.runTest();
    }
}
