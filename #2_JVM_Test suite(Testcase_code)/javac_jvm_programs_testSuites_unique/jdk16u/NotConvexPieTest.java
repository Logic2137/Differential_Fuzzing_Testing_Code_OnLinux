



import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;

public class NotConvexPieTest {

    Arc2D aNegative = new Arc2D.Double(-100, -100, 200, 200,
                                       -45, -270, Arc2D.PIE);
    
    
    Rectangle2D rect = new Rectangle2D.Double(-20, -40, 40, 80);

    String failText = "Test failed: rect should not be contained in arc due to "
            + "intersections with radii";

    public void runTest() {

        boolean contains = aNegative.contains(rect.getX(),
                                              rect.getY(),
                                              rect.getWidth(),
                                              rect.getHeight());
        if (contains) {
            
            throw new RuntimeException(failText);
        }
    }

    public static void main(String[] args) {
        NotConvexPieTest test = new NotConvexPieTest();
        test.runTest();
    }
}
