

import java.awt.BasicStroke;
import java.awt.Shape;

public class TestNullShape {

    public static void main(String[] args) {

        BasicStroke bs = new BasicStroke();
        try {
            Shape s = bs.createStrokedShape(null);
            System.out.println("result: false");
            throw new RuntimeException("NPE is expected");
        } catch (NullPointerException ne) {
            System.out.println("result (npe): true");
        }
    }
}
